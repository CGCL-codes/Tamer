package jacol;

import java.net.*;
import java.util.*;
import java.io.*;
import java.lang.reflect.*;
import jacol.server.*;
import jacol.util.*;

/**
 * This is the main jacol class. It is responsible for parsing command line parameters,
 * starting and initializing the lisp and java servers, and accepting inbound connections.
 *
 * @author Jason Lowdermilk <a href="mailto:lowdermilk@users.sourceforge.net">Email</a>
 * @version 0.25 beta
 */
public class Jacol {

    private static boolean serverMode = false;

    private static boolean scriptMode = false;

    private static int lispPort = -1;

    private static int javaPort = -1;

    private static String fileName = null;

    private static Class stringArrayType = null;

    private static String[] subargs = null;

    private static Process lispProcess = null;

    private static String password = null;

    private static final boolean debug = false;

    private static void usage() {
        System.err.println("usage: jacol [-jp <num>] [-lp <num>] {-s | {className|file.lisp|file.fas}}");
        System.exit(1);
    }

    public static void main(String[] args) {
        try {
            String thisArg = null;
            stringArrayType = args.getClass();
            int filenameIndex = 0;
            for (int i = 0; i < args.length; i++) {
                thisArg = args[i];
                if (thisArg.equals("-jp")) {
                    javaPort = Integer.parseInt(args[i + 1]);
                    filenameIndex = i + 2;
                } else if (thisArg.equals("-lp")) {
                    lispPort = Integer.parseInt(args[i + 1]);
                    filenameIndex = i + 2;
                } else if (thisArg.equals("-i")) {
                    scriptMode = true;
                    serverMode = true;
                } else if (thisArg.equals("-s")) {
                    serverMode = true;
                }
            }
            if (!serverMode) {
                fileName = args[filenameIndex];
                subargs = new String[args.length - filenameIndex - 1];
                int j = 0;
                for (int i = filenameIndex + 1; i < args.length; i++) {
                    subargs[j] = args[i];
                    j++;
                }
            }
            if (lispPort == -1) lispPort = getRandomPort();
            if (javaPort == -1) javaPort = getRandomPort();
        } catch (Exception _e) {
            System.out.println("Error parsing command line parameters.");
            usage();
        }
        new Jacol();
    }

    private static int getRandomPort() {
        boolean done = false;
        Random r = new Random();
        int attempt = 0;
        while (!done) {
            try {
                attempt = (Math.abs(r.nextInt()) % 24000) + 8000;
                ServerSocket ss = new ServerSocket(attempt);
                ss.close();
                done = true;
            } catch (Exception _e) {
                done = false;
            }
        }
        return attempt;
    }

    /**
     * Get the port being used by the lisp server.
     */
    public static int getLispPort() {
        return lispPort;
    }

    /**
     * Execute the lisp server in a separate process, and redirect it's stdin, stdout, and stderr
     * to those of jacol. Start the java server in a separate thread. If not in server mode,
     * load the lisp program or java class that was specified on the command line.
     */
    public Jacol() {
        try {
            new JavaServer(javaPort, serverMode);
            String jacolDir = System.getProperty("jacol.home", ".");
            File f = new File(jacolDir + "/lib/server.fas");
            if (!f.exists()) {
                System.err.println("Unable to locate server.lisp module.");
                System.exit(1);
            }
            try {
                lispProcess = Runtime.getRuntime().exec("clisp " + jacolDir + "/lib/server.fas " + lispPort + " " + jacolDir);
            } catch (IOException _ioe) {
                System.err.println("Unable to run lisp interpreter.");
                System.exit(2);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(lispProcess.getInputStream()));
            String line = reader.readLine();
            new LispReader(lispProcess.getInputStream(), System.out);
            new LispReader(lispProcess.getErrorStream(), System.err);
            new LispWriter(lispProcess.getOutputStream());
            try {
                Thread.sleep(250);
            } catch (InterruptedException _ie) {
            }
            LispLoader ll = new LispLoader();
            if (!serverMode) {
                ll.init(javaPort, subargs);
                if ((fileName.endsWith(".fas") || (fileName.endsWith(".lisp")))) {
                    ll.load(fileName);
                } else {
                    Class c = null;
                    try {
                        c = Class.forName(fileName);
                    } catch (ClassNotFoundException _cnfe) {
                        usage();
                    }
                    Class[] parms = new Class[1];
                    parms[0] = stringArrayType;
                    Method m = c.getMethod("main", parms);
                    Object[] args = new Object[1];
                    args[0] = subargs;
                    m.invoke(null, args);
                }
            } else {
                ll.init(javaPort);
                System.out.println(line);
            }
        } catch (Exception _e) {
            _e.printStackTrace();
        }
    }

    public static String getPassword() {
        if (password == null) {
            String homeDir = System.getProperty("user.home", ".");
            File rcFile = new File(homeDir + "/.jacol/jacolrc");
            if (!rcFile.exists()) {
                System.err.println("Unable to open rc file ~/.jacol/jacolrc");
                System.exit(1);
            }
            Properties props = new Properties();
            try {
                props.load(new FileInputStream(rcFile));
            } catch (IOException _ioe) {
                _ioe.printStackTrace();
            }
            password = props.getProperty("password", "none");
        }
        return password;
    }

    private void setPassword(String password) {
        Jacol.password = password;
    }

    /**
     * Cleanly bring down the java and lisp servers.
     */
    public static void shutdown() {
        if (debug) {
            System.out.println("Shutting down.");
        }
        if (scriptMode) System.out.print('\0');
        if (lispProcess != null) {
            lispProcess.destroy();
            if (debug) {
                System.out.println("Lisp process destroyed.");
            }
        }
        System.exit(0);
    }

    /**
     * This class contains the logic used when initializing the lisp server.
     */
    class LispLoader {

        private LispInterpreter interp = null;

        public LispLoader() {
            interp = InterpreterFactory.getInterpreter();
        }

        /**
         * Initialize the lisp server with the java server port and the command line arguments to use.
         *
         * @param port an int indicating which port is being used by the java server
         * @param args an array of Strings which are the "virtual" command line parameters of the lisp program
         */
        public void init(int port, String[] args) {
            interp.eval("(get-jvm-connection " + port + ")");
            interp.eval("(setq *ARGS* '(" + ListOps.arrayToString(args) + "))");
        }

        /**
         * Intialize the lisp server with the java server port.
         *
         * @param port an int indicating which port is being used by the java server
         */
        public void init(int port) {
            interp.eval("(get-jvm-connection " + port + ")");
        }

        /**
         * Load a file into the lisp server.
         *
         * @param fileName a String indicating which file to load
         */
        public void load(String fileName) {
            interp.eval("(load \"" + fileName + "\")");
            shutdown();
        }
    }

    /**
     * This copies character from an inputstream to a printstream, in a separate thread.
     */
    class LispReader implements Runnable {

        private InputStream in = null;

        private PrintStream printer = null;

        /**
         * Create a LispReader and spawn a new thread.
         *
         * @param in an InputStream to read from
         * @param printer a PrintStream to write to
         */
        public LispReader(InputStream in, PrintStream printer) {
            this.in = in;
            this.printer = printer;
            new Thread(this).start();
        }

        public void run() {
            int c;
            try {
                while ((c = in.read()) != -1) {
                    printer.print((char) c);
                }
            } catch (Exception _e) {
                _e.printStackTrace();
            }
        }
    }

    /**
     * This reads from standard input and copies characters to an output stream, in a separate thread.
     */
    class LispWriter implements Runnable {

        private PrintStream stream = null;

        /**
         * Create a LispWriter and spawn a new thread.
         *
         * @param stream the OutputStream to write to
         */
        public LispWriter(OutputStream stream) {
            this.stream = new PrintStream(stream, true);
            (new Thread(this)).start();
        }

        public void run() {
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String line;
                while ((line = br.readLine()) != null) {
                    stream.println(line);
                }
            } catch (Exception _e) {
                _e.printStackTrace();
            }
        }
    }
}
