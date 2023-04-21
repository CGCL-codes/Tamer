package mobat.tuning;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import mobat.satin.messages.Finished;
import mobat.satin.messages.ISMessageHandler;
import mobat.satin.messages.IbisMessageServer;
import mobat.tuning.info.Complete;
import mobat.tuning.info.IInfoCollector;
import mobat.tuning.info.SaveMessage;
import mobat.tuning.info.StopMessage;
import mobat.tuning.info.TraceCollector;
import mobat.tuning.store.IAlgorithmDescription;
import mobat.tuning.store.IRemoteAlgorithmDescription;
import mobat.tuning.utils.Utils;

/**
 *
 * @author S.K. Smit
 * @institution: Vrije Universiteit Amsterdam
 */
public class Launcher implements ISMessageHandler, Runnable {

    public static final String PROPERTY_RUNDISTRIBUTED = "bonesa.distributed";

    public static final String PROPERTY_PARAMETERTUNER = "bonesa.tuner";

    public static final String PROPERTY_ALGORITHMDESCRIPTION = "bonesa.algorithm";

    public static final String PROPERTY_SEED = "bonesa.seed";

    public static final String ARG_HEADNODE = "-headnode";

    public static final String ARG_SLAVE = "-slave";

    public static final String ARG_HOST = "-host";

    public static final String WAIT_WHEN_FINISHED = "bonesa.waitwhenfinished";

    private IParameterTuner[] tuner = null;

    private File[] targetPath;

    private static int mode = 0;

    private ArrayList<ISMessageHandler> subhandlers = new ArrayList<ISMessageHandler>();

    private boolean finishConfirmed = false;

    public Launcher(int size) {
        tuner = new IParameterTuner[size];
    }

    public Launcher(IParameterTuner tuner, File targetPath) {
        this.tuner = new IParameterTuner[] { tuner };
        this.targetPath = new File[] { targetPath };
    }

    public Launcher(IParameterTuner[] tuner, File[] targetPath) {
        this.tuner = tuner;
        this.targetPath = targetPath;
    }

    public void setupFromProperties() {
        try {
            System.getProperties().load(new FileInputStream("autorun.properties"));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        try {
            String files = System.getProperty(PROPERTY_ALGORITHMDESCRIPTION);
            int index = 0;
            tuner = new IParameterTuner[files.split(",").length];
            targetPath = new File[files.split(",").length];
            for (String f : files.split(",")) {
                f = Utils.getProgramDirectory().getCanonicalPath() + File.separator + f.trim();
                Object tunerinstance = Class.forName(System.getProperty(PROPERTY_PARAMETERTUNER)).newInstance();
                if (tunerinstance instanceof IParameterTuner) {
                    tuner[index] = (IParameterTuner) tunerinstance;
                    targetPath[index] = new File(f);
                    System.out.println(targetPath[index]);
                    IAlgorithmDescription description = null;
                    try {
                        description = (IAlgorithmDescription) Utils.loadFromFile(targetPath[index]);
                    } catch (Exception ex) {
                        throw new RuntimeException("Invalid property for " + PROPERTY_ALGORITHMDESCRIPTION + " :: " + ex.getMessage());
                    }
                    if (description instanceof IRemoteAlgorithmDescription) {
                        description = ((IRemoteAlgorithmDescription) description).getRoot();
                    }
                    tuner[index].setup(description, Integer.parseInt(System.getProperty(PROPERTY_SEED)));
                } else {
                    throw new RuntimeException("Invalid property for " + PROPERTY_PARAMETERTUNER);
                }
                index++;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void run() {
        if (mobat.satin.Launcher.getHost() != null && !mobat.satin.Launcher.getHost().equals("") && (System.getProperty(PROPERTY_RUNDISTRIBUTED) != null && System.getProperty(PROPERTY_RUNDISTRIBUTED).equals("true"))) {
            mobat.satin.Launcher.launchDistributed(tuner, mobat.satin.Launcher.getHost(), this, mode);
        } else {
            mobat.satin.Launcher.launchLocal(tuner, this);
        }
    }

    public static void main(String[] args) {
        mobat.satin.Launcher.loadDependencies();
        Launcher l = new Launcher(1);
        l.setupFromProperties();
        if (args.length > 0) {
            for (String arg : args) {
                if (arg.equals(ARG_HEADNODE)) {
                    System.getProperties().setProperty(mobat.satin.Launcher.BONESA_PROPERTY_PROCESSORS, "-1");
                    System.out.println("- HEADNODE MODE -");
                    mode = 1;
                }
                if (arg.equals(ARG_SLAVE)) {
                    System.out.println("- SLAVE MODE -");
                    mode = 2;
                }
                if (arg.equals(ARG_HOST)) {
                    System.out.println("- HOST MODE -");
                    mode = 1;
                }
            }
        }
        l.run();
    }

    public void hook(ISMessageHandler handler) {
        subhandlers.add(handler);
    }

    public Serializable handleMessage(Serializable message) {
        for (ISMessageHandler h : subhandlers) {
            try {
                h.handleMessage(message);
            } catch (Exception ex) {
            }
        }
        if (message instanceof StopMessage) {
            saveTraceToFile();
            finishConfirmed = true;
            for (IParameterTuner p : tuner) {
                p.stop();
            }
            return null;
        }
        if (message instanceof SaveMessage) {
            saveTraceToFile();
            return null;
        }
        if (message instanceof Finished) {
            saveTraceToFile();
            if (System.getProperty(WAIT_WHEN_FINISHED).equals("true") && (mobat.satin.Launcher.getHost() != null && !mobat.satin.Launcher.getHost().equals(""))) {
                while (!finishConfirmed) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                }
            }
            return null;
        }
        if (message instanceof IInfoCollector) {
            for (IParameterTuner p : tuner) {
                if (message instanceof TraceCollector) {
                    if (((TraceCollector) message).getName().equals(p.getSetup().getName())) {
                        ((IInfoCollector) message).collect(p);
                        if (p.getProgress() == 1 && tuner[tuner.length - 1] == p) {
                            finishConfirmed = true;
                        }
                    }
                } else {
                    if (p.isRunning()) {
                        ((IInfoCollector) message).collect(p);
                    }
                }
            }
            return message;
        }
        return null;
    }

    public void saveTraceToFile() {
        while (true) {
            try {
                for (int i = 0; i < tuner.length; i++) {
                    Utils.saveToFile(tuner[i].getSetup(), targetPath[i]);
                }
                return;
            } catch (ConcurrentModificationException ex) {
            }
        }
    }

    public static void saveTraceMessage() throws IOException {
        try {
            IbisMessageServer.sendMessage(new SaveMessage());
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void notifyFinished() {
        try {
            IbisMessageServer.sendMessage(new Complete());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
