package org.cyberaide.gridshell.commands;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.cyberaide.gridshell.commands.runtime.BasicRuntimeListener;
import org.cyberaide.gridshell.commands.runtime.RuntimeAdapter;
import org.cyberaide.gridshell.commands.util.SSHUtil;
import org.cyberaide.gridshell.commands.util.logging.LoggingUtil;

public class GSSystem {

    public static boolean DEBUG = false;

    public static String SERVER_USERNAME = null;

    public static String SERVER_URI = null;

    public static String INSTALL_DIR = null;

    public static final String HOME_DIR = System.getProperty("user.home");

    public static final String GRIDSHELL_DIR = HOME_DIR + File.separator + ".cyberaide";

    public static final String OBJECT_DIR = GRIDSHELL_DIR + File.separator + "objects";

    public static final String TYPE_DIR = GRIDSHELL_DIR + File.separator + "types";

    public static final String WF_DIR = GRIDSHELL_DIR + File.separator + "karajan";

    public static final String RESOURCE_DIR = OBJECT_DIR + File.separator + "resource";

    public static final String TASK_DIR = OBJECT_DIR + File.separator + "task";

    public static final String JOB_DIR = OBJECT_DIR + File.separator + "job";

    public static final String PROFILE_LOC = GRIDSHELL_DIR + File.separator + "profiles";

    public static final String DEFAULT_PROFILE_LOC = PROFILE_LOC + File.separator + "default";

    public static final String EXTENSION = ".txt";

    public static final String DELIMITER = ":";

    public static final String COMMENT_MARKER = "#";

    private static final String LINE0 = "[PROPERTIES]";

    private static final String LINE1 = "idCount:";

    private static final String CLINE0 = "[CONFIGURATION]";

    private static final String CLINE1 = "install_dir:";

    private static final String CLINE2 = "jvm_path:";

    private static final String CLINE3 = "startup_script_path:";

    private static File propFile = null;

    private static File confFile = null;

    private static long objIdCount = 0;

    private static boolean initialized = false;

    public static Properties profile = null;

    private static Map<String, Object> pythonObjs;

    static {
        pythonObjs = new HashMap<String, Object>();
    }

    public static boolean isInitialized() {
        return initialized;
    }

    /**
	 * Initialize the client system.
	 */
    public static void setupClient(String installDir, String serverUserName, String serverURI) {
        INSTALL_DIR = installDir;
        SERVER_USERNAME = serverUserName;
        SERVER_URI = serverURI;
        confFile = new File(GRIDSHELL_DIR + File.separator + "gridshell.conf");
        boolean valid = true;
        String errMsg = null;
        if (DEBUG) {
            System.out.println("Client DEBUG activated");
            LoggingUtil.setup();
        } else {
            if (serverURI == null || serverURI.trim().equals("")) {
                DEBUG = true;
                LoggingUtil.setup();
                System.out.println("No server was specified in " + GRIDSHELL_DIR + File.separator + "gridshell.conf.");
                System.out.println("Server commands will be performed locally.");
            } else {
                String msg;
                if (!SSHUtil.verifyHostExists(SERVER_URI)) {
                    System.err.println(">>> WARNING: Could not find \"" + SERVER_URI + "\" GridShell server");
                } else {
                    System.out.println("Verified \"" + SERVER_URI + "\" GridShell server exists");
                }
            }
        }
        propFile = new File(GRIDSHELL_DIR + File.separator + "gridshell.prop");
        File profLoc = new File(PROFILE_LOC);
        if (!profLoc.exists()) {
            profLoc.mkdir();
        }
        if (!propFile.exists()) {
            valid = false;
            errMsg = ">>> WARNING: Did not find \"" + propFile.getAbsolutePath() + "\"";
        }
        if (valid) {
            if (!loadProps()) {
                valid = false;
                errMsg = ">>> ERROR: There was a problem parsing \"" + propFile.getAbsolutePath() + "\"";
            }
        }
        profile = new Properties();
        File profileFile = new File(DEFAULT_PROFILE_LOC);
        if (profileFile.exists()) {
            try {
                profile.load(new FileInputStream(profileFile));
                System.out.println("Loaded default profile found in " + DEFAULT_PROFILE_LOC + ".");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.err.println("Default profile not found in " + DEFAULT_PROFILE_LOC + ".");
            System.err.println("No profile loaded.");
        }
        if (!valid) {
            System.err.println();
            System.err.println(errMsg);
            System.err.println(">>> USE THE PROGRAM AT YOUR OWN RISK!");
            System.err.println();
        }
        initialized = true;
    }

    /**
	 * Initialize the server system.
	 */
    public static void setupServer() {
        boolean valid = true;
        String errMsg = null;
        confFile = new File(GRIDSHELL_DIR + File.separator + "gridshell.conf");
        propFile = new File(GRIDSHELL_DIR + File.separator + "gridshell.prop");
        INSTALL_DIR = loadInstallDir();
        if (valid) {
            try {
                LoggingUtil.setup();
            } catch (Exception e) {
                valid = false;
                errMsg = ">>> ERROR: There was a problem setting up the logging system.";
                e.printStackTrace();
            }
        }
        if (!valid) {
            System.err.println();
            System.err.println(errMsg);
            System.err.println(">>> USE THE PROGRAM AT YOUR OWN RISK!");
            System.err.println();
        }
        initialized = true;
        System.out.println(">>> Setup GridShell server.");
    }

    /**
	 * Load the properties file.
	 * 
	 * @return true if the file was successfully loaded, false ow
	 */
    private static boolean loadProps() {
        boolean valid = true;
        String line = null;
        int lineCount = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(propFile));
            valid = reader.ready();
            while (((line = reader.readLine()) != null) && valid) {
                switch(lineCount) {
                    case 0:
                        valid = (line.equals(LINE0));
                        break;
                    case 1:
                        valid = (line.startsWith(LINE1));
                        if (valid) {
                            valid = line.contains(DELIMITER);
                            if (valid) {
                                String tokens[] = line.split(DELIMITER, 2);
                                objIdCount = Long.valueOf(tokens[1].trim());
                            }
                        }
                        break;
                }
                lineCount++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            valid = false;
        } catch (IOException e) {
            e.printStackTrace();
            valid = false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            valid = false;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return valid;
    }

    /**
	 * Load the install directory from the gridshell.conf file
	 *
	 */
    public static String loadInstallDir() {
        String retval = null;
        boolean valid = true;
        String line = null;
        int lineCount = 0;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(confFile));
            valid = reader.ready();
            while (((line = reader.readLine()) != null) && valid) {
                switch(lineCount) {
                    case 0:
                        valid = (line.equals(CLINE0));
                        break;
                    case 1:
                        valid = (line.startsWith(CLINE1));
                        if (valid) {
                            valid = line.contains(DELIMITER);
                            if (valid) {
                                String tokens[] = line.split(DELIMITER, 2);
                                retval = tokens[1];
                            }
                        }
                        break;
                    case 2:
                        valid = (line.startsWith(CLINE2));
                        break;
                    case 3:
                        valid = (line.startsWith(CLINE3));
                        break;
                }
                lineCount++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            valid = false;
        } catch (IOException e) {
            e.printStackTrace();
            valid = false;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            valid = false;
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return retval;
    }

    /**
	 * Store the properties file.
	 * Do this every time a unique id is given out.
	 */
    private static void storeProps() {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(propFile));
            writer.write(LINE0 + "\n");
            writer.write(LINE1 + objIdCount + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Get a unique general-purpose id
	 * 
	 * @return id
	 */
    public static long getUniqueId() {
        objIdCount++;
        storeProps();
        return objIdCount;
    }

    /**
	 * Register a python object for use as a Java object.
	 * 
	 * @param name - name of the object
	 * @param object - python object
	 */
    public static void registerPythonObj(String name, Object object) {
        pythonObjs.put(name, object);
    }

    /**
	 * Obtain a python object for use as a Java object.
	 * 
	 * @param name - name of the object
	 * @return python object
	 */
    public static Object getPythonObj(String name) {
        return pythonObjs.get(name);
    }

    public static void test() {
        System.out.println("GSSYSTEM TEST!");
    }
}
