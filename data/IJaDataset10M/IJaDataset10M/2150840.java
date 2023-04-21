package se.kth.ict.id2203.application.assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.kth.ict.id2203.application.ApplicationContinue;
import se.kth.ict.id2203.application.ApplicationInit;
import se.kth.ict.id2203.detectors.failure.epfd.EventuallyPerfectFailureDetector;
import se.kth.ict.id2203.detectors.failure.epfd.Restore;
import se.kth.ict.id2203.detectors.failure.epfd.Suspect;
import se.sics.kompics.*;
import se.sics.kompics.timer.ScheduleTimeout;
import se.sics.kompics.timer.Timer;

/**
 *
 * @author Igor
 */
public final class Application1b extends ComponentDefinition {

    Positive<EventuallyPerfectFailureDetector> epfd = requires(EventuallyPerfectFailureDetector.class);

    Positive<Timer> timer = requires(Timer.class);

    private static final Logger logger = LoggerFactory.getLogger(Application1b.class);

    private String[] commands;

    private int lastCommand;

    public Application1b() {
        subscribe(initHandler, control);
        subscribe(startHandler, control);
        subscribe(continueHandler, timer);
        subscribe(suspectHandler, epfd);
        subscribe(restoreHandler, epfd);
    }

    Handler<ApplicationInit> initHandler = new Handler<ApplicationInit>() {

        @Override
        public void handle(ApplicationInit event) {
            commands = event.getCommandScript().split(":");
            lastCommand = -1;
        }
    };

    Handler<Start> startHandler = new Handler<Start>() {

        @Override
        public void handle(Start event) {
            doNextCommand();
        }
    };

    Handler<ApplicationContinue> continueHandler = new Handler<ApplicationContinue>() {

        @Override
        public void handle(ApplicationContinue event) {
            doNextCommand();
        }
    };

    Handler<Suspect> suspectHandler = new Handler<Suspect>() {

        @Override
        public void handle(Suspect event) {
            logger.info("Node {} suspected of crash (period={})", event.getNode(), event.getPeriod());
        }
    };

    Handler<Restore> restoreHandler = new Handler<Restore>() {

        @Override
        public void handle(Restore event) {
            logger.info("Node {} is alive (period={})", event.getNode(), event.getPeriod());
        }
    };

    private void doNextCommand() {
        lastCommand++;
        if (lastCommand > commands.length) {
            return;
        }
        if (lastCommand == commands.length) {
            logger.info("DONE ALL OPERATIONS");
            Thread applicationThread = new Thread("ApplicationThread") {

                @Override
                public void run() {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    while (true) {
                        try {
                            String line = in.readLine();
                            doCommand(line);
                        } catch (Throwable e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            applicationThread.start();
            return;
        }
        String op = commands[lastCommand];
        doCommand(op);
    }

    private void doCommand(String cmd) {
        if (cmd.startsWith("S")) {
            doSleep(Integer.parseInt(cmd.substring(1)));
        } else if (cmd.startsWith("X")) {
            doShutdown();
        } else if (cmd.equals("help")) {
            doHelp();
            doNextCommand();
        } else {
            logger.info("Bad command: '{}'. Try 'help'", cmd);
            doNextCommand();
        }
    }

    private void doHelp() {
        logger.info("Available commands: S<n>, help, X");
        logger.info("Sn: sleeps 'n' milliseconds before the next command");
        logger.info("help: shows this help message");
        logger.info("X: terminates this process");
    }

    private void doSleep(long delay) {
        logger.info("Sleeping {} milliseconds...", delay);
        ScheduleTimeout st = new ScheduleTimeout(delay);
        st.setTimeoutEvent(new ApplicationContinue(st));
        trigger(st, timer);
    }

    private void doShutdown() {
        System.out.println("2DIE");
        System.out.close();
        System.err.close();
        Kompics.shutdown();
    }
}
