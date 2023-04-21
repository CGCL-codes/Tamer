package de.mpg.mpiz.koeln.anna.server.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import de.kerner.commons.file.FileUtils;
import de.kerner.commons.logging.Log;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEvent;
import de.mpg.mpiz.koeln.anna.core.events.AnnaEventListener;
import de.mpg.mpiz.koeln.anna.server.AnnaServer;
import de.mpg.mpiz.koeln.anna.step.AnnaStep;
import de.mpg.mpiz.koeln.anna.step.ExecutableStep;
import de.mpg.mpiz.koeln.anna.step.ObservableStep.State;

/**
 * 
 * @author Alexander Kerner
 * @lastVisit 2009-11-12
 * @thradSave custom
 * 
 */
public class AnnaServerImpl implements AnnaServer {

    private class Terminator extends Thread {

        private Collection<AnnaStep> registeredSteps;

        private final long timeout = 5000;

        Terminator(Collection<AnnaStep> registeredSteps) {
            this.registeredSteps = registeredSteps;
        }

        @Override
        public void run() {
            logger.debug("doing dirty shutdown");
            try {
                sleep(timeout);
                while (!areWeDone()) {
                    sleep(timeout);
                }
                exe.shutdown();
                activator.shutdown();
                System.exit(0);
            } catch (Exception e) {
                System.err.println("dirty shutdown failed! (" + e.getLocalizedMessage() + ")");
            }
        }

        private boolean areWeDone() {
            synchronized (registeredSteps) {
                for (AnnaStep s : registeredSteps) {
                    if (!(s.getState().isFinished())) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    private static final Log logger = new Log(AnnaServerImpl.class);

    private static final File PROPERTIES_FILE = new File(FileUtils.WORKING_DIR, "configuration" + File.separatorChar + "server.properties");

    private final Collection<AnnaStep> registeredSteps = new HashSet<AnnaStep>();

    private final EventHandler handler;

    private final Properties properties;

    private final ExecutorService exe = Executors.newCachedThreadPool();

    private final AnnaServerActivator activator;

    AnnaServerImpl(AnnaServerActivator activator) {
        properties = getPropertes();
        logger.debug("loaded properties: " + properties);
        handler = new EventHandler(registeredSteps);
        handler.addEventListener(new NotifyOthersListener());
        this.activator = activator;
    }

    public void shutdown() {
        logger.debug("starting shutting down");
        new Terminator(registeredSteps).start();
    }

    public synchronized void unregisterStep(ExecutableStep step) {
        logger.debug("TODO");
    }

    public synchronized void registerStep(ExecutableStep step) {
        registeredSteps.add((AnnaStep) step);
        setStepState(step, State.REGISTERED);
        logger.debug("registered step " + step);
        if (((AnnaStep) step).getState().equals(State.ERROR)) {
            logger.debug("step is dummy, will not execute");
            return;
        }
        StepSheduler ss;
        if (step.isCyclic()) {
            ss = new CyclicStepSheduler((AnnaStep) step, handler);
        } else {
            ss = new ImmediateStepSheduler((AnnaStep) step, handler);
        }
        synchronized (AnnaSepExecutor.LOCK) {
            logger.debug("notifying others (new step)");
            AnnaSepExecutor.LOCK.notifyAll();
        }
        exe.submit(ss);
    }

    public synchronized Properties getServerProperties() {
        return new Properties(properties);
    }

    public void addEventListener(AnnaEventListener observer) {
        handler.addEventListener(observer);
    }

    public void removeEventListener(AnnaEventListener observer) {
        handler.removeEventListener(observer);
    }

    public String toString() {
        return this.getClass().getSimpleName();
    }

    private synchronized Properties getPropertes() {
        final Properties defaultProperties = initDefaults();
        final Properties pro = new Properties(defaultProperties);
        try {
            logger.debug("loading settings from " + PROPERTIES_FILE);
            final FileInputStream fi = new FileInputStream(PROPERTIES_FILE);
            pro.load(fi);
            fi.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.warn(this + ": could not load settings from " + PROPERTIES_FILE.getAbsolutePath() + ", using defaults");
        } catch (IOException e) {
            e.printStackTrace();
            logger.warn(this + ": could not load settings from " + PROPERTIES_FILE.getAbsolutePath() + ", using defaults");
        }
        return pro;
    }

    private Properties initDefaults() {
        Properties pro = new Properties();
        return pro;
    }

    private void setStepState(ExecutableStep step, State state) {
        if (!((AnnaStep) step).getState().equals(State.ERROR)) ((AnnaStep) step).setState(state); else logger.debug("step was errorgenious, will not change state");
        handler.stepStateChanged((AnnaStep) step);
    }
}
