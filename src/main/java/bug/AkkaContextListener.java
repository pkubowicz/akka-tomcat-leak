package bug;

import akka.actor.ActorSystem;
import scala.concurrent.duration.FiniteDuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.TimeUnit;

public class AkkaContextListener implements ServletContextListener {
    private ActorSystem actorSystem;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Started");
        startAkka();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        stopAkka();
        System.out.println("Stopped");
    }

    private void startAkka() {
        actorSystem = ActorSystem.create();
        actorSystem.scheduler().schedule(FiniteDuration.create(1, TimeUnit.SECONDS),
                FiniteDuration.create(20, TimeUnit.SECONDS),
                new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("Work");
                    }
                },
                actorSystem.dispatcher());
        System.out.println("Started Akka");
    }

    private void stopAkka() {
        if (actorSystem != null) {
            actorSystem.shutdown();
            actorSystem.awaitTermination();
            System.out.println("Stopped actor system");
        }
    }
}
