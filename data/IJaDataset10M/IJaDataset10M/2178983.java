package protoj.lang;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import protoj.lang.command.JavaCommand;
import protoj.lang.internal.InformationException;
import protoj.lang.internal.ProjectReporter;
import protoj.util.ArgRunnable;
import protoj.util.JavaTask;

/**
 * Responsible for dispatching each instruction from the
 * {@link InstructionChain} to the matching command and executing that command.
 * See {@link #dispatchCommands()}.
 * 
 * 
 * @author Ashley Williams
 * 
 */
public final class DispatchFeature {

    /**
	 * System property added when a new vm is being started via
	 * {@link #startVm(String, String[], String, ArgRunnable)} to indicate the
	 * vm is being started in-code.
	 */
    private static final String SCRIPT_VM_PROPERTY = "protoj.scriptVm";

    /**
	 * See {@link #getJavaCommand()}.
	 */
    private JavaCommand javaCommand;

    /**
	 * See {@link #getReporter()}.
	 */
    private ProjectReporter reporter;

    /**
	 * See {@link #getMainClass()}.
	 */
    private String mainClass;

    /**
	 * See {@link #getMainMemory()}.
	 */
    private String mainMemory;

    /**
	 * The parent project.
	 */
    private final StandardProject parent;

    /**
	 * See {@link #getCurrentMainClass()}.
	 */
    private String currentMainClass;

    /**
	 * See {@link #isScriptVm()}.
	 */
    private boolean scriptVm;

    /**
	 * {@link #isStatusWindow()}.
	 */
    private boolean statusWindow;

    /**
	 * See {@link #getStartVmConfig()}.
	 */
    private ArgRunnable<JavaTask> startVmConfig;

    /**
	 * See {@link #getElapsedHours()}.
	 */
    private long elapsedHours;

    /**
	 * See {@link #getElapsedMinutes()}.
	 */
    private long elapsedMinutes;

    /**
	 * See {@link #getElapsedSeconds()}.
	 */
    private long elapsedSeconds;

    /**
	 * See {@link #getWeaverJar()}.
	 */
    private File weaverJar;

    /**
	 * Thrown by {@link #startVm(String, String[], String, ArgRunnable)} to
	 * indicate the started vm has returned with a non-zero exit code.
	 */
    private static final class StartVmException extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public StartVmException(String message) {
            super(message);
        }
    }

    /**
	 * The created instance will be configured to bootstrap to the currently
	 * running jvm.
	 * 
	 * @param parent
	 */
    public DispatchFeature(StandardProject parent) {
        this.parent = parent;
        this.javaCommand = new JavaCommand(this);
        this.reporter = new ProjectReporter(parent);
        this.currentMainClass = null;
        this.currentMainClass = createCurrentMainClass();
        this.scriptVm = createScriptVm();
        this.statusWindow = false;
        initBootstrap(getCurrentMainClass(), null);
    }

    /**
	 * Call in order to be able to start to the specified project main with the
	 * specified default amount of memory. Instructions that don't have commands
	 * available in the initial vm are delegated to the project vm.
	 * <p>
	 * This memory may be overridden by the
	 * {@link StandardProperties#getMainMemory()} value as configured in the
	 * CoreConfig aspect.
	 * 
	 * @param mainClass
	 * @param mainMemory
	 */
    public void initBootstrap(String mainClass, String mainMemory) {
        this.mainClass = mainClass;
        this.mainMemory = mainMemory;
    }

    /**
	 * ProtoJ commands are each executed in their own virtual machines. Use this
	 * method to ensure that aspectj load time weaving is globally used. This
	 * works by using the weaver jar as a java agent when the vm is started, so
	 * the name of the jar file must be specified.
	 * <p>
	 * The lib directory will be searched for a jar file whose name contains the
	 * partial name parameter, which will be used as the javaagent jarpath.
	 * Specify null to use the default, which is "aspectjweaver.jar". If there
	 * is no match or more than one match is found then an exception is thrown
	 * 
	 * @param partialName
	 *            the partial name, specify null to use "aspectjweaver.jar"
	 */
    public void initLoadTimeWeaving(String partialName) {
        final String searchName;
        if (partialName == null) {
            searchName = "aspectjweaver.jar";
        } else {
            searchName = partialName;
        }
        File libDir = parent.getLayout().getLibDir();
        String[] matches = libDir.list(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.contains(searchName);
            }
        });
        if (matches.length == 0) {
            throw new InformationException("load time weaving bad config: couldn't find a jar file whose name contains " + searchName);
        } else if (matches.length > 1) {
            throw new InformationException("load time weaving bad config: found more than one jar file whose name contains " + searchName);
        }
        weaverJar = new File(libDir, matches[0]);
    }

    /**
	 * Creation method for the currentMainClass property. See
	 * {@link #getCurrentMainClass()}.
	 * 
	 * @return
	 */
    private String createCurrentMainClass() {
        try {
            throw new RuntimeException();
        } catch (Exception e) {
            StackTraceElement[] trace = e.getStackTrace();
            return trace[trace.length - 1].getClassName();
        }
    }

    /**
	 * Creation method for the scriptVm property. See {@link #isScriptVm()}.
	 * 
	 * @return
	 */
    private boolean createScriptVm() {
        String scriptVm = System.getProperty(SCRIPT_VM_PROPERTY, Boolean.TRUE.toString());
        return Boolean.valueOf(scriptVm);
    }

    /**
	 * The helper used to report dispatch progress.
	 * 
	 * @return
	 */
    public ProjectReporter getReporter() {
        return reporter;
    }

    /**
	 * The elapsed time in hours.
	 * 
	 * @return
	 */
    public long getElapsedHours() {
        return elapsedHours;
    }

    /**
	 * The elapsed time in minutes.
	 * 
	 * @return
	 */
    public long getElapsedMinutes() {
        return elapsedMinutes;
    }

    /**
	 * The elapsed time in seconds.
	 * 
	 * @return
	 */
    public long getElapsedSeconds() {
        return elapsedSeconds;
    }

    /**
	 * Iterates over all the instructions from
	 * {@link StandardProject#getInstructionChain()} and executes each
	 * corresponding command from {@link StandardProject#getCommandStore()}. The
	 * instructions usually originate from the command line but may have been
	 * specified in-code.
	 * <p>
	 * Where there is no corresponding command in the command store then we
	 * assume that this code is running in the hand-compiled bootstrapping vm
	 * that contains just the bare minimum of commands. Therefore we attempt to
	 * bootstrap to the main project vm resulting in the following behavior:
	 * <ul>
	 * <li>if we are already in the main project vm then there is nowhere else
	 * to try and so we log that the command cannot be found and terminate with
	 * an error code of 1</li>
	 * <li>if we are not in the main project vm then we bootstrap to it with the
	 * remaining instructions and don't process any more here</li>
	 * </ul>
	 */
    public void dispatchCommands() {
        try {
            long startTimeMillis = new Date().getTime();
            try {
                parent.getInstructionChain().visitAndRemove(new Dispatcher());
            } finally {
                updateElapsedTime(startTimeMillis);
            }
            if (isScriptVm()) {
                reporter.projectSucceeded();
            }
            exitApplication(0);
        } catch (StartVmException e) {
            if (e.getMessage() != null) {
                System.out.println(e.getMessage());
            }
            exitApplication(1);
        } catch (InformationException e) {
            reporter.projectFailed(e.getMessage());
            exitApplication(1);
        } catch (Throwable e) {
            reporter.projectFailed(e);
            exitApplication(1);
        }
    }

    /**
	 * Exits the application with the specified error code. A status window will
	 * be shown in a non-headless environment when the exitWindow property is
	 * set to true.
	 * 
	 * @param code
	 */
    private void exitApplication(int code) {
        if (!isScriptVm()) {
            System.exit(code);
        } else {
            if (isStatusWindow()) {
                showStatusWindow(code);
            } else {
                System.exit(code);
            }
        }
    }

    /**
	 * Updates the amount of time the build took message. The results are
	 * available by calling getElapsedXXX methods.
	 * 
	 * @param startTimeMillis
	 */
    private void updateElapsedTime(long startTimeMillis) {
        long endTime = new Date().getTime();
        long elapsedMillis = endTime - startTimeMillis;
        long oneHourInMillis = 60 * 60 * 1000;
        long oneMinuteInMillis = 60 * 1000;
        long oneSecondInMillis = 1000;
        elapsedHours = (elapsedMillis / oneHourInMillis);
        float leftoverFromHours = (float) elapsedMillis % (float) oneHourInMillis;
        elapsedMinutes = (long) (leftoverFromHours / oneMinuteInMillis);
        float leftoverFromMinutes = leftoverFromHours % oneMinuteInMillis;
        elapsedSeconds = (long) (leftoverFromMinutes / oneSecondInMillis);
    }

    /**
	 * Shows the status window, but only from the script vm. Lots of vms get
	 * launched, so we don't want a window for all of them. TODO need to find a
	 * way to quit the swing frame with an error code of 1 not 0.
	 * 
	 * @param code
	 */
    private void showStatusWindow(int code) {
        JFrame frame = new JFrame();
        frame.setSize(300, 100);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        String elapsedTime = String.format("%02d:%02d:%02d", getElapsedHours(), getElapsedMinutes(), getElapsedSeconds());
        JLabel label = new JLabel(elapsedTime, SwingConstants.CENTER);
        Font font = label.getFont().deriveFont(200f);
        label.setFont(font);
        panel.add(label);
        panel.setBackground(code == 0 ? Color.green : Color.red);
        frame.getContentPane().add(panel);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.validate();
        frame.setVisible(true);
    }

    /**
	 * Executes the command for the current instruction or delegates to the
	 * project vm if it can't be found. Requests early termination by calling
	 * {@link InstructionChain#breakVisit()} if after all this the command still
	 * can't be found.
	 * 
	 * @author Ashley Williams
	 * 
	 */
    private final class Dispatcher implements ArgRunnable<Instruction> {

        public void run(Instruction instruction) {
            CommandStore commandStore = parent.getCommandStore();
            InstructionChain instructionChain = parent.getInstructionChain();
            String name = instruction.getName();
            Command command = commandStore.getCommand(name);
            if (command != null) {
                command.execute(instruction);
                if (command.equals(javaCommand)) {
                    instructionChain.breakVisit();
                }
            } else {
                if (!isInProjectMain()) {
                    startProjectMainVm();
                } else {
                    StringBuilder reason = new StringBuilder();
                    reason.append("the following instruction isn't recognized: \"");
                    reason.append(instruction.getText());
                    reason.append("\"");
                    throw new InformationException(reason.toString());
                }
                instructionChain.breakVisit();
            }
        }
    }

    /**
	 * Returns true if we are in the main class specified by
	 * {@link #getMainClass()}, false otherwise.
	 * 
	 * @return
	 */
    private boolean isInProjectMain() {
        return getCurrentMainClass().equals(getMainClass());
    }

    /**
	 * Starts a forked vm to the project main.
	 * <p>
	 * This method was added to support {@link CoreProject#dispatchCommands()}
	 * since it needs to start the project vm for the purposes of accessing the
	 * set of new classes that may have been compiled.
	 */
    private void startProjectMainVm() {
        String[] args = parent.getInstructionChain().createArgsAndRemove(false);
        startVm(mainClass, args, mainMemory, getStartVmConfig());
    }

    /**
	 * Starts a forked vm with the specified parameters. Advice is also applied
	 * at a joinpoint for this method from the StandardConfig aspect in order to
	 * apply any debug settings.
	 * <p>
	 * This method was added to support {@link Command#execute(Instruction)},
	 * since it needs to restart the same vm for the purpose of reconfiguring
	 * memory.
	 * 
	 * @param mainClass
	 *            the class that contains the main() method
	 * @param args
	 *            the arguments to the main method
	 * @param memory
	 *            the maximum amount of memory to be assigned to the new vm
	 * @param config
	 *            the callback used to configure the vm before it is executed
	 * 
	 * @return
	 */
    public JavaTask startVm(String mainClass, String[] args, String memory, ArgRunnable<JavaTask> config) {
        StringBuilder builder = new StringBuilder();
        builder.append("starting vm " + mainClass + ":" + memory);
        for (String arg : args) {
            builder.append(" ");
            builder.append(arg);
        }
        parent.getLogger().info(builder);
        ProjectLayout layout = parent.getLayout();
        JavaTask vm = new JavaTask();
        vm.setMainClass(mainClass);
        vm.initFork(memory, false);
        vm.initArgs(args);
        vm.initClasspath(layout.getClasspathConfig());
        if (weaverJar != null) {
            vm.initWeaving(weaverJar);
        }
        vm.initJvmargs("-D" + SCRIPT_VM_PROPERTY + "=false");
        if (config != null) {
            config.run(vm);
        }
        vm.execute();
        vm.writeOutput();
        if (!vm.isSuccess()) {
            String message;
            String stderr = vm.getStderr();
            if (stderr != null && stderr.contains("java.lang.NoClassDefFoundError")) {
                message = "did you remember to execute the compile command first of all?";
            } else {
                message = "";
            }
            throw new StartVmException(message);
        }
        return vm;
    }

    /**
	 * Calculates the name of the main() method containing class that was used
	 * to start the currently executing vm.
	 * 
	 * @return
	 */
    public String getCurrentMainClass() {
        return currentMainClass;
    }

    /**
	 * The main() method containing entry point class for the new vm.
	 * 
	 * @return
	 */
    public String getMainClass() {
        return mainClass;
    }

    /**
	 * The maximum amount of memory that should be allocated by the new vm.
	 * 
	 * @return
	 */
    public String getMainMemory() {
        return mainMemory;
    }

    /**
	 * Returns whether or not the current vm was started directly from the shell
	 * script or in code by using the
	 * {@link #startVm(String, String[], String, ArgRunnable)} method. Each time
	 * startVm() is called, the {@link #SCRIPT_VM_PROPERTY} is added as a jvm
	 * arg so that we can tell the difference.
	 * 
	 * @return
	 */
    public boolean isScriptVm() {
        return scriptVm;
    }

    /**
	 * See {@link #isStatusWindow()}.
	 * 
	 * @param statusWindow
	 */
    public void setStatusWindow(boolean statusWindow) {
        if (statusWindow && GraphicsEnvironment.isHeadless()) {
            throw new RuntimeException("can't configure a status window in a headless environment");
        }
        this.statusWindow = statusWindow;
    }

    /**
	 * Returns whether or not the {@link #dispatchCommands()} will exit with a
	 * gui. If true then a window will be displayed with a green background
	 * indicating no errors or a red background indicating errors. The window
	 * will fill the screen and the application will exit when it is closed. It
	 * is an error to set this property to true in a headless environment
	 * resulting in an exception.
	 * 
	 * @return
	 */
    public boolean isStatusWindow() {
        return statusWindow;
    }

    /**
	 * See {@link #setStartVmConfig(ArgRunnable)}.
	 * 
	 * @param startVmConfig
	 */
    public void setStartVmConfig(ArgRunnable<JavaTask> startVmConfig) {
        this.startVmConfig = startVmConfig;
    }

    /**
	 * The instance used to provide additional configuration before the vm is
	 * launched inside the
	 * {@link #startVm(String, String[], String, ArgRunnable)} method.
	 * 
	 * @return
	 */
    public ArgRunnable<JavaTask> getStartVmConfig() {
        return startVmConfig;
    }

    public StandardProject getParent() {
        return parent;
    }

    /**
	 * The jar file used for load time weaving, or null if not configured.
	 * 
	 * @return
	 */
    public File getWeaverJar() {
        return weaverJar;
    }

    /**
	 * The command to execute a java process.
	 * 
	 * @return
	 */
    public JavaCommand getJavaCommand() {
        return javaCommand;
    }
}
