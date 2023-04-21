package toxTree.qsar;

import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import toxTree.exceptions.ShellException;
import toxTree.logging.TTLogger;

/**
 * "Mac OS","Windows","AIX","Linux","HP-UX","Solaris" 
 * @author nina
 *
 */
public abstract class CommandShell<INPUT, OUTPUT> implements PropertyChangeListener {

    public static final String os_MAC = "Mac OS";

    public static final String os_WINDOWS = "Windows";

    public static final String os_LINUX = "Linux";

    protected static TTLogger logger = new TTLogger(CommandShell.class);

    protected Hashtable<String, String> executables;

    protected String inputFile = null;

    protected String outputFile = null;

    protected int exitCode = 0;

    public synchronized int getExitCode() {
        return exitCode;
    }

    public synchronized void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    protected CommandShell() throws ShellException {
        executables = new Hashtable<String, String>();
        initialize();
    }

    protected void initialize() throws ShellException {
    }

    public String addExecutable(String osname, String executable) throws ShellException {
        File file = new File(executable);
        return executables.put(osname, executable);
    }

    public String addExecutableMac(String executable) throws ShellException {
        return addExecutable(os_MAC, executable);
    }

    public String addExecutableWin(String executable) throws ShellException {
        return addExecutable(os_WINDOWS, executable);
    }

    public String addExecutableLinux(String executable) throws ShellException {
        return addExecutable(os_LINUX, executable);
    }

    public String getExecutable(String osname) {
        return executables.get(osname);
    }

    public OUTPUT runShell(INPUT mol) throws ShellException {
        String osName = System.getProperty("os.name");
        Enumeration<String> oss = executables.keys();
        while (oss.hasMoreElements()) {
            String os = oss.nextElement();
            if (osName.startsWith(os)) {
                String exeString = executables.get(os);
                if (exeString == null) throw new ShellException(this, "Not supported for " + osName); else try {
                    return runShell(mol, exeString);
                } catch (Exception x) {
                    throw new ShellException(this, x);
                }
            }
        }
        return null;
    }

    /**
     * Returns empty string, override with smth meaningfull
     * @param mol
     * @return
     * @throws ShellException
     */
    protected List<String> prepareInput(String path, INPUT mol) throws ShellException {
        return null;
    }

    /**
     * Does nothing, override with smth meaningfull
     */
    protected abstract OUTPUT parseOutput(String path, INPUT mol) throws ShellException;

    protected String getPath(File file) {
        String path = file.getAbsolutePath();
        int i = path.lastIndexOf(File.separatorChar);
        if (i > -1) path = path.substring(0, i); else path = "";
        return path;
    }

    protected OUTPUT runShell(INPUT mol, String execString) throws ShellException {
        try {
            setExitCode(0);
            File file = new File(execString);
            String path = getPath(file);
            List<String> inFile = prepareInput(path, mol);
            List<String> command = new ArrayList<String>();
            command.add(execString);
            if (inFile != null) for (int j = 0; j < inFile.size(); j++) command.add(inFile.get(j));
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Map<String, String> environ = builder.environment();
            builder.directory(new File(path));
            logger.info("<" + toString() + " filename=\"" + execString + "\">");
            logger.debug("<environ>");
            logger.debug(environ);
            logger.debug("</environ>");
            long now = System.currentTimeMillis();
            final Process process = builder.start();
            InputStream is = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            logger.info("<stdout>");
            while ((line = br.readLine()) != null) {
                logger.info(line);
            }
            logger.info("</stdout>");
            logger.info("<wait process=\"" + execString + "\">");
            setExitCode(process.waitFor());
            logger.info("</wait>");
            logger.info("<exitcode value=\"" + Integer.toString(exitCode) + "\">");
            logger.info("<elapsed_time units=\"ms\">" + Long.toString(System.currentTimeMillis() - now) + "</elapsed_time>");
            logger.info("</" + toString() + ">");
            OUTPUT newmol = null;
            if (exitCodeOK(exitCode)) {
                logger.info("<parse>");
                newmol = parseOutput(path, mol);
                logger.info("</parse>");
            }
            return newmol;
        } catch (Throwable x) {
            logger.debug(x.getMessage());
            throw new ShellException(this, x);
        }
    }

    protected boolean exitCodeOK(int exitVal) {
        return exitVal == 0;
    }

    public String getInputFile() {
        return inputFile;
    }

    public void setInputFile(String inputFile) {
        this.inputFile = inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    protected abstract OUTPUT transform(INPUT mol);
}
