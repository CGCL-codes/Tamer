package unclej.utasks.process;

import unclej.filepath.ClassPath;
import unclej.filepath.Path;
import unclej.framework.ExitException;
import unclej.framework.UTask;
import unclej.framework.UTaskFailedException;
import unclej.javatype.ClassName;
import unclej.javatype.TypeFactory;
import unclej.log.ULog;
import unclej.util.ExceptionAdapter;
import unclej.validate.AutoValidate;
import unclej.validate.Dir;
import unclej.validate.NonNull;
import unclej.validate.ValidationException;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Runs a Java <code>static void main(String[])</code> entry point, either in this process or by starting an external
 * process.
 * @author scottv
 */
public abstract class JavaMainUTask implements UTask {

    public JavaMainUTask(String className) {
        this.className = TypeFactory.getClass(className);
    }

    public JavaMainUTask(ClassName className) {
        this.className = className;
    }

    public abstract CommandLineArguments getClassArguments(ULog log);

    public CommandLineArguments getJvmArguments() {
        CommandLineArguments args = new CommandLineArguments();
        args.addSwitch(serverVM ? "-server" : "-client");
        if (mainClassPath == null) {
            mainClassPath = ClassPath.getCurrent();
        }
        args.addSwitch("-classpath", mainClassPath.toString());
        args.addSwitch("-Xms" + initialHeapM + "M", initialHeapM > 0);
        args.addSwitch("-Xmx" + maxHeapM + "M", maxHeapM > 0);
        args.addSwitch("-Xss" + stackSizeK + "K", stackSizeK > 0);
        for (Map.Entry<String, Object> entry : systemProperties.entrySet()) {
            args.addSwitch("-D" + entry.getKey() + "=" + entry.getValue());
        }
        for (String misc : miscArgs) {
            args.addSwitch(misc);
        }
        return args;
    }

    public void setForked(boolean forked) {
        this.forked = forked;
    }

    public void setInitialHeapM(int megabytes) {
        this.initialHeapM = megabytes;
    }

    public void setMaxHeapM(int megabytes) {
        this.maxHeapM = megabytes;
    }

    public void setStackSizeK(int kilobytes) {
        this.stackSizeK = kilobytes;
    }

    public void setMainClassPath(Path classPath) {
        this.mainClassPath = classPath;
    }

    public void setServerVM(boolean serverVM) {
        this.serverVM = serverVM;
    }

    public void setWorkingDir(File workingDirectory) {
        this.workingDir = workingDirectory;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void setErrorStream(OutputStream errorStream) {
        this.errorStream = errorStream;
    }

    public void setSystemProperty(String key, Object value) {
        systemProperties.put(key, value);
    }

    public void clearMiscArgs() {
        miscArgs.clear();
    }

    public void addMiscArg(String argument) {
        miscArgs.add(argument);
    }

    public void validate() throws ValidationException {
        AutoValidate.basic(this);
    }

    public void execute(ULog log) throws UTaskFailedException {
        AutoValidate.full(this);
        CommandLineArguments classArguments = getClassArguments(log);
        if (classArguments == null) {
            log.finer("no arguments provided, skipping call to {0}", className);
        } else if (forked) {
            runExternal(classArguments, log);
        } else {
            runInProcess(classArguments, log);
        }
    }

    private void runExternal(CommandLineArguments classArguments, ULog log) throws UTaskFailedException {
        ExecUTask runner = new ExecUTask(locateJava());
        CommandLineArguments args = new CommandLineArguments();
        args.addArguments(getJvmArguments());
        args.addSwitch(className.toString());
        args.addArguments(classArguments);
        runner.setArgs(args);
        runner.setWorkingDirectory(workingDir);
        runner.setOutputStream(outputStream);
        runner.setErrorStream(errorStream);
        log.fine("running: {0}", args);
        try {
            runner.startProcess(log);
            runner.waitForProcess();
        } catch (IOException x) {
            throw ExceptionAdapter.unchecked(x);
        } catch (InterruptedException x) {
            Thread.currentThread().interrupt();
        }
    }

    public static String locateJava() {
        String result = "java";
        File home = new File(System.getProperty("java.home"));
        String executable = System.getProperty("os.name").startsWith("Windows") ? "java.exe" : "java";
        final String sep = File.separator;
        File java = new File(home + sep + "bin" + sep + executable);
        if (java.exists() && java.isFile()) {
            result = java.getAbsolutePath();
        } else {
            java = new File(home + sep + ".." + sep + "bin" + sep + executable);
            if (java.exists() && java.isFile()) {
                result = java.getAbsolutePath();
            }
        }
        if (result.indexOf(' ') >= 0) {
            result = '"' + result + '"';
        }
        return result;
    }

    private void runInProcess(CommandLineArguments classArguments, ULog log) {
        Properties originalSystem = System.getProperties();
        try {
            Properties newSystem = new Properties(originalSystem);
            for (Map.Entry<String, Object> entry : systemProperties.entrySet()) {
                newSystem.put(entry.getKey(), entry.getValue());
            }
            System.setProperties(newSystem);
            Class target;
            if (mainClassPath != null) {
                target = mainClassPath.getURLClassLoader().loadClass(className.toString());
            } else {
                target = Class.forName(className.toString());
            }
            Method main = target.getMethod("main", new Class[] { String[].class });
            String[] args = classArguments.toArray();
            log.fine("invoking {0} {1}", className, classArguments.toString());
            main.invoke(null, new Object[] { args });
        } catch (InvocationTargetException x) {
            Throwable cause = x.getCause();
            if (cause instanceof ExitException) {
                int status = ((ExitException) cause).getStatus();
                if (status == 0) {
                    log.finer("{0} called System.exit(0)", className);
                } else {
                    log.severe("{0} exited with exit code {1}", className, status);
                    throw (ExitException) cause;
                }
            } else {
                throw ExceptionAdapter.unchecked(cause);
            }
        } catch (Exception x) {
            throw ExceptionAdapter.unchecked(x);
        } finally {
            System.setProperties(originalSystem);
        }
    }

    protected boolean isForked() {
        return forked;
    }

    protected ClassName getClassName() {
        return className;
    }

    protected Path getMainClassPath() {
        return mainClassPath;
    }

    protected File getWorkingDir() {
        return workingDir;
    }

    protected Map<String, Object> getSystemProperties() {
        return Collections.unmodifiableMap(systemProperties);
    }

    private boolean forked;

    @NonNull
    private final ClassName className;

    private Path mainClassPath;

    @Dir
    private File workingDir;

    private OutputStream outputStream;

    private OutputStream errorStream;

    private int initialHeapM;

    private int maxHeapM;

    private int stackSizeK;

    private final Map<String, Object> systemProperties = new HashMap<String, Object>();

    private final List<String> miscArgs = new ArrayList<String>();

    private boolean serverVM;
}
