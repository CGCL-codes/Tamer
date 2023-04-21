package papertoolkit.pen.synch;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.swing.JOptionPane;
import papertoolkit.PaperToolkit;
import papertoolkit.util.DebugUtils;
import papertoolkit.util.SystemUtils;
import papertoolkit.util.classpath.EclipseProjectClassLoader;
import papertoolkit.util.files.FileUtils;

/**
 * <p>
 * The batched importer will be called by the pen importer (.NET code) every time you synchronize the pen. It
 * sends the information to the BatchServer, which will notify any running applications.
 * 
 * TODO: In the future, applications do not need to be running all the time. They will be notified of new data
 * upon booting.
 * 
 * This class is launched by PaperToolkit\penSynch\bin\BatchImporter.exe. It creates a log file in the same
 * directory, that you can tail using an app such as BareTail.
 * </p>
 * <p>
 * <span class="BSDLicense"> This software is distributed under the <a
 * href="http://hci.stanford.edu/research/copyright.txt">BSD License</a>. </span>
 * </p>
 * 
 * @author <a href="http://graphics.stanford.edu/~ronyeh">Ron B Yeh</a> (ronyeh(AT)cs.stanford.edu)
 */
public class BatchedDataImporter {

    /**
	 * Pops up a Dialog to tell us this Importer was run...
	 */
    private static final boolean DEBUG = true;

    /**
	 * @param args
	 */
    public BatchedDataImporter(String[] args) {
        try {
            System.setOut(new PrintStream(new FileOutputStream(new File(PaperToolkit.getToolkitRootPath(), "penSynch/bin/BatchImporter.log"))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        DebugUtils.println("Current Time is: " + new Date());
        if (DEBUG) {
            JOptionPane.showMessageDialog(null, "Batched Pen Data saved to penSynch/Data/XML/." + "\nClick OK to Notify Registered Listeners...");
        }
        sendToRegisteredMonitors(args);
        if (DEBUG) {
            JOptionPane.showMessageDialog(null, "Batched Pen Data saved to penSynch/Data/XML/." + "\nClick OK to Notify Live Listeners...");
        }
        sendToBatchedDataServer(args);
    }

    /**
	 * Send the data to the monitors that we have placed in the
	 * PaperToolkit/penSynch/RegisteredBatchedMonitors/ directory.
	 */
    private void sendToRegisteredMonitors(String[] args) {
        final File monitorsDir = new File(PaperToolkit.getToolkitRootPath(), "/penSynch/RegisteredBatchedMonitors/");
        final List<File> monitors = FileUtils.listVisibleFiles(monitorsDir);
        for (File f : monitors) {
            String fileName = f.getName().toLowerCase();
            if (fileName.endsWith(".jar")) {
                DebugUtils.println("Running JAR: " + fileName);
                SystemUtils.runJar(f, new String[] { '"' + new File("penSynch/RegisteredBatchedMonitors/Examples/Example.txt").getAbsolutePath() + '"' });
            } else if (fileName.endsWith(".bat") || fileName.endsWith(".exe")) {
                DebugUtils.println("Running BAT/EXE: " + fileName);
                SystemUtils.run(f, new String[] { '"' + new File("penSynch/RegisteredBatchedMonitors/Examples/Example.txt").getAbsolutePath() + '"' });
            } else if (fileName.endsWith(".monitor")) {
                DebugUtils.println("Running Monitor: " + f.getAbsolutePath());
                runMonitorFromConfigFile(f, args);
            } else {
            }
        }
    }

    /**
	 * @param args
	 */
    private void sendToBatchedDataServer(String[] args) {
        try {
            DebugUtils.println("Running the Batched Pen Data Importer");
            for (String arg : args) {
                DebugUtils.println("Argument: " + arg);
            }
            final Socket socket = new Socket("localhost", BatchedDataDispatcher.DEFAULT_PLAINTEXT_PORT);
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bw.write("XML: " + args[0] + SystemUtils.LINE_SEPARATOR);
            bw.write(BatchedDataDispatcher.EXIT_COMMAND + SystemUtils.LINE_SEPARATOR);
            bw.flush();
            bw.close();
            socket.close();
        } catch (ConnectException e) {
            DebugUtils.println("No Batched Importer is Currently Listening to Port: " + BatchedDataDispatcher.DEFAULT_PLAINTEXT_PORT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            DebugUtils.println(e.getLocalizedMessage());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            DebugUtils.println(e.getLocalizedMessage());
        } catch (IOException e) {
            e.printStackTrace();
            DebugUtils.println(e.getLocalizedMessage());
        }
    }

    /**
	 * Adds a monitor into the configuration file. This monitor will be run every time a digital pen is
	 * docked.
	 */
    public static void registerMonitor(BatchedDataImportMonitor monitor) {
        DebugUtils.println("Registering " + monitor.getName());
        DebugUtils.println(monitor.getClass());
        DebugUtils.println(monitor.getClass().getResource("/"));
        try {
            Class<?> c = Class.forName("edu.stanford.hci.r3.demos.batched.monitor.BatchedMonitor");
            Object newInstance = c.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
	 * The BatchImporter.exe calls this through a wrapper JAR...
	 * 
	 * @param args
	 */
    public static void main(String[] args) {
        new BatchedDataImporter(args);
        System.exit(0);
    }

    /**
	 * Reads in the file, figures out which directory our monitor resides in, and runs it, with the correct
	 * classpath!
	 * 
	 * @param monitorConfigFile
	 *            the *.monitor configuration file, in Java Properties (non-xml) format.
	 * @param args
	 */
    private static void runMonitorFromConfigFile(File monitorConfigFile, String[] args) {
        Properties p = new Properties();
        try {
            p.load(new FileInputStream(monitorConfigFile));
            String mainClass = p.getProperty("MainClass");
            String eclipseProjectDirectory = p.getProperty("EclipseProjectDirectory");
            DebugUtils.println(mainClass);
            DebugUtils.println(eclipseProjectDirectory);
            File resolvedProjectDirectory = new File(eclipseProjectDirectory);
            if (resolvedProjectDirectory.exists()) {
            } else {
                resolvedProjectDirectory = new File(PaperToolkit.getToolkitRootPath(), eclipseProjectDirectory);
            }
            EclipseProjectClassLoader eclipesProjectClassLoader = new EclipseProjectClassLoader(resolvedProjectDirectory);
            try {
                Class<?> classObj = eclipesProjectClassLoader.loadClass(mainClass);
                Method m = classObj.getMethod("main", new Class[] { args.getClass() });
                m.setAccessible(true);
                int mods = m.getModifiers();
                if (m.getReturnType() != void.class || !Modifier.isStatic(mods) || !Modifier.isPublic(mods)) {
                    throw new NoSuchMethodException("main");
                }
                m.invoke(null, new Object[] { args });
            } catch (ClassNotFoundException e) {
                System.err.println("BatchedDataImporter:: Could not find the class: " + mainClass);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        DebugUtils.println(p.toString());
    }

    /**
	 * @param monitor
	 */
    public static void removeMonitor(BatchedDataImportMonitor monitor) {
    }
}
