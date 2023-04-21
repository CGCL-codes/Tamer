package net.sourceforge.ant4hg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.sound.midi.SysexMessage;
import net.sourceforge.ant4hg.consumers.Consumer;
import net.sourceforge.ant4hg.consumers.ConsumerFactory;
import net.sourceforge.ant4hg.parameters.AddRemoveParameter;
import net.sourceforge.ant4hg.parameters.AfterParameter;
import net.sourceforge.ant4hg.parameters.ChangeParameter;
import net.sourceforge.ant4hg.parameters.CheckParameter;
import net.sourceforge.ant4hg.parameters.CleanParameter;
import net.sourceforge.ant4hg.parameters.CloseBranchParameter;
import net.sourceforge.ant4hg.parameters.DateParameter;
import net.sourceforge.ant4hg.parameters.DestinationParameter;
import net.sourceforge.ant4hg.parameters.ForceParameter;
import net.sourceforge.ant4hg.parameters.GitParameter;
import net.sourceforge.ant4hg.parameters.MessageParameter;
import net.sourceforge.ant4hg.parameters.NoDatesParameter;
import net.sourceforge.ant4hg.parameters.NoupdateParameter;
import net.sourceforge.ant4hg.parameters.OutputParameter;
import net.sourceforge.ant4hg.parameters.Parameter;
import net.sourceforge.ant4hg.parameters.PullParameter;
import net.sourceforge.ant4hg.parameters.RemotecmdParameter;
import net.sourceforge.ant4hg.parameters.RevisionParameter;
import net.sourceforge.ant4hg.parameters.ShowFunctionParameter;
import net.sourceforge.ant4hg.parameters.SourceParameter;
import net.sourceforge.ant4hg.parameters.SshParameter;
import net.sourceforge.ant4hg.parameters.StatusParameter;
import net.sourceforge.ant4hg.parameters.SwitchParentParameter;
import net.sourceforge.ant4hg.parameters.TextParameter;
import net.sourceforge.ant4hg.parameters.UncompressedParameter;
import net.sourceforge.ant4hg.parameters.UnifiedParameter;
import net.sourceforge.ant4hg.parameters.UpdateParameter;
import net.sourceforge.ant4hg.parameters.UserParameter;
import net.sourceforge.ant4hg.types.Auth;
import net.sourceforge.ant4hg.types.DirSet;
import net.sourceforge.ant4hg.types.Ignore;
import net.sourceforge.ant4hg.util.UrlBuilder;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecTask;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.condition.Condition;
import org.apache.tools.ant.taskdefs.condition.ConditionBase;
import org.apache.tools.ant.types.PatternSet;
import org.apache.tools.ant.types.Reference;

/**
 * Defines the &lt;hg&gt; tag and its parameters.
 * 
 * @author Benjamin de Dardel <ant4hg[at]free.fr>
 * @since V0.1
 */
public class HgTask extends ExecTask {

    /**
     * Defines the command parameter.
     */
    private String p_cmd = null;

    /**
     * Set the command parameter.
     * 
     * @param cmd
     *            , the command
     */
    public void setCmd(String cmd) {
        Logger.debug("SET CMD = " + cmd);
        this.p_cmd = cmd;
    }

    public String getCmd() {
        return p_cmd;
    }

    /**
     * Defines the base directory parameter.
     */
    private File p_file = null;

    /**
     * Set the base directory parameter.
     * 
     * @param dir
     *            , the base directory
     */
    public void setDir(File dir) {
        Logger.debug("SET DIR = " + dir.getAbsolutePath());
        if (!dir.isDirectory()) {
            throw new BuildException(dir.getAbsolutePath() + " IS NOT A DIRECTORY");
        }
        this.p_file = dir;
        super.setDir(getBaseDir());
    }

    protected File getFile() {
        return p_file;
    }

    /**
     * Set the base directory parameter.
     * 
     * @param file
     *            , the base directory
     * @throws FileNotFoundException
     */
    public void setFile(File file) {
        if (file.isDirectory()) {
            setDir(file);
            return;
        }
        this.p_file = file;
        super.setDir(getBaseDir());
    }

    /**
     * Create dirset with given pattern.
     * 
     * @param pattern
     *            , the pattern
     */
    public void setPattern(String pattern) {
        if (p_file == null) {
            throw new BuildException("SET DIR PARAMETER BEFORE PATTERN");
        }
        if (this.getProject() == null) {
            throw new BuildException("UNDEFINED PROJECT");
        }
        DirSet dirset = new DirSet();
        dirset.setDir(this.p_file);
        dirset.setProject(getProject());
        PatternSet patternset = dirset.createPatternSet();
        patternset.setRefid(new Reference(getProject(), pattern));
        p_dirsets.add(dirset);
    }

    /**
     * Defines dirset parameters.
     */
    private Vector<DirSet> p_dirsets = new Vector<DirSet>();

    /**
     * Add dirset parameter.
     * 
     * @param set
     *            , the new dirset parameter.
     */
    public void addDirset(DirSet set) {
        if (p_cmd != null && p_cmd.equals("update")) {
            throw new BuildException(Error.DIRSET_ELEMENT_FORBIDDEN + " FOR COMMAND : " + p_cmd);
        }
        p_dirsets.add(set);
    }

    /**
     * Defines the status parameter.
     */
    private Parameter status;

    /**
     * Set the status parameter (specific to status command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : status
     * </p>
     * 
     * @param status
     *            , the parameter value
     * @throws BuildException
     *             on command or parameter error
     */
    public void setStatus(String status) throws BuildException {
        this.status = new StatusParameter(status);
    }

    /**
     * Defines the status parameter.
     */
    private Parameter force;

    /**
     * Set the force parameter (specific to remove,pull,push command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : remove,pull,push
     * </p>
     * 
     * @param force
     *            , the parameter value
     * @throws BuildException
     *             on command or parameter error
     */
    public void setForce(String force) throws BuildException {
        this.force = new ForceParameter(force);
    }

    /**
     * Defines the after parameter.
     */
    private Parameter after;

    /**
     * Set the after parameter (specific to remove command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : remove
     * </p>
     * 
     * @param after
     *            , the parameter value
     * @throws BuildException
     *             on command or parameter error
     */
    public void setAfter(String after) throws BuildException {
        this.after = new AfterParameter(after);
    }

    /**
     * Defines the message parameter.
     */
    private Parameter message;

    /**
     * Set the message parameter (specific to commit command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : commit
     * </p>
     * 
     * @param message
     *            , the parameter value
     */
    public void setMessage(String msg) {
        if (msg == null || msg.isEmpty()) {
            this.message = null;
        }
        this.message = new MessageParameter(msg);
    }

    public String getMessage() {
        if (this.message == null) {
            return null;
        }
        return this.message.getValue();
    }

    /**
     * Defines the user parameter.
     */
    private Parameter user;

    /**
     * Set the user parameter (specific to commit command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : commit
     * </p>
     * 
     * @param user
     *            , the parameter value
     */
    public void setUser(String user) {
        this.user = new UserParameter(user);
    }

    /**
     * Defines the addremove parameter.
     */
    private Parameter addremove;

    /**
     * Set the addremove parameter (specific to commit command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : commit
     * </p>
     * 
     * @param addremove
     *            , the parameter value
     */
    public void setAddremove(String addremove) {
        this.addremove = new AddRemoveParameter(addremove);
    }

    /**
     * Defines the closebranch parameter.
     */
    private Parameter closebranch;

    /**
     * Set the closebranch parameter (specific to commit command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : commit
     * </p>
     * 
     * @param closebranch
     *            , the parameter value
     */
    public void setClosebranch(String closebranch) {
        this.closebranch = new CloseBranchParameter(closebranch);
    }

    /**
     * Defines the date parameter.
     */
    private Parameter date;

    /**
     * Set the date parameter (specific to commit,update command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : commit,update
     * </p>
     * 
     * @param date
     *            , the parameter value
     */
    public void setDate(String date) {
        this.date = new DateParameter(date);
    }

    /**
     * Defines the logfile parameter (specific to commit command).
     */
    private File p_logfile = null;

    /**
     * Set the logfile parameter (specific to commit command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : commit
     * </p>
     * 
     * @param logfile
     *            , the parameter value
     */
    public void setLogfile(File logfile) {
        Logger.debug("SET LOGFILE = " + logfile.getAbsolutePath());
        p_logfile = logfile;
    }

    /**
     * Defines the clean parameter.
     */
    private Parameter clean;

    /**
     * Set the clean parameter (specific to update command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : update
     * </p>
     * 
     * @param clean
     *            , the parameter value
     * @throws BuildException
     *             on command or parameter error
     */
    public void setClean(String clean) throws BuildException {
        this.clean = new CleanParameter(clean);
    }

    /**
     * Defines the check parameter.
     */
    private Parameter check;

    /**
     * Set the check parameter (specific to update command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : update
     * </p>
     * 
     * @param check
     *            , the parameter value
     * @throws BuildException
     *             on command or parameter error
     */
    public void setCheck(String check) throws BuildException {
        this.check = new CheckParameter(check);
    }

    /**
     * Defines the revision parameter.
     */
    private Parameter revision;

    /**
     * Set the revision parameter (specific to update,clone,pull,push,export,diff command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : update,clone,pull,push,export,diff
     * </p>
     * 
     * @param revision
     *            , the parameter value
     */
    public void setRevision(String revision) {
        this.revision = new RevisionParameter(revision);
    }

    /**
     * Defines the noupdate parameter.
     */
    private Parameter noupdate;

    /**
     * Set the noupdate parameter (specific to clone command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : clone
     * </p>
     * 
     * @param noupdate
     *            , the parameter value
     */
    public void setNoupdate(String noupdate) {
        this.noupdate = new NoupdateParameter(noupdate);
    }

    /**
     * Defines the pull parameter.
     */
    private Parameter pull;

    /**
     * Set the pull parameter (specific to clone command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : clone
     * </p>
     * 
     * @param pull
     *            , the parameter value
     */
    public void setPull(String pull) {
        this.pull = new PullParameter(pull);
    }

    /**
     * Defines the uncompressed parameter.
     */
    private Parameter uncompressed;

    /**
     * Set the uncompressed parameter (specific to clone command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : clone
     * </p>
     * 
     * @param uncompressed
     *            , the parameter value
     */
    public void setUncompressed(String uncompressed) {
        this.uncompressed = new UncompressedParameter(uncompressed);
    }

    /**
     * Defines the ssh parameter.
     */
    private Parameter ssh;

    /**
     * Set the ssh parameter (specific to clone,pull,push command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : clone,pull,push
     * </p>
     * 
     * @param ssh
     *            , the parameter value
     */
    public void setSsh(String ssh) {
        this.ssh = new SshParameter(ssh);
    }

    /**
     * Defines the remotecmd parameter.
     */
    private Parameter remotecmd;

    /**
     * Set the remotecmd parameter (specific to clone,pull,push command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : clone,pull,push
     * </p>
     * 
     * @param remotecmd
     *            , the parameter value
     */
    public void setRemotecmd(String remotecmd) {
        this.remotecmd = new RemotecmdParameter(remotecmd);
    }

    /**
     * Defines the destination parameter.
     */
    private Parameter destination;

    /**
     * Set the destination parameter (specific to clone,push command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : clone,push
     * </p>
     * 
     * @param destination
     *            , the parameter value
     */
    public void setDestination(String destination) {
        this.destination = new DestinationParameter(destination);
    }

    /**
     * Defines the update parameter.
     */
    private Parameter update;

    /**
     * Set the update parameter (specific to pull command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : pull
     * </p>
     * 
     * @param update
     *            , the parameter value
     */
    public void setUpdate(String update) {
        this.update = new UpdateParameter(update);
    }

    /**
     * Defines the source parameter.
     */
    private Parameter source;

    /**
     * Set the source parameter (specific to clone,pull command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : clone,pull
     * </p>
     * 
     * @param source
     *            , the parameter value
     */
    public void setSource(String source) {
        this.source = new SourceParameter(source);
    }

    /**
     * Defines the output parameter.
     */
    private Parameter output;

    /**
     * Set the output parameter (specific to export command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : export
     * </p>
     * 
     * @param output
     *            , the parameter value
     * @override
     */
    public void setOutput(File output) {
        try {
            this.output = new OutputParameter(output.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Defines the switchparent parameter.
     */
    private Parameter switchparent;

    /**
     * Set the switchparent parameter (specific to export command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : export
     * </p>
     * 
     * @param switchparent
     *            , the parameter value
     */
    public void setSwitchparent(String switchparent) {
        this.switchparent = new SwitchParentParameter(switchparent);
    }

    /**
     * Defines the text parameter.
     */
    private Parameter text;

    /**
     * Set the text parameter (specific to export,diff command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : export,diff
     * </p>
     * 
     * @param text
     *            , the parameter value
     */
    public void setText(String text) {
        this.text = new TextParameter(text);
    }

    /**
     * Defines the git parameter.
     */
    private Parameter git;

    /**
     * Set the git parameter (specific to export,diff command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : export,diff
     * </p>
     * 
     * @param git
     *            , the parameter value
     */
    public void setGit(String git) {
        this.git = new GitParameter(git);
    }

    /**
     * Defines the nodates parameter.
     */
    private Parameter nodates;

    /**
     * Set the nodates parameter (specific to export,diff command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : export,diff
     * </p>
     * 
     * @param nodates
     *            , the parameter value
     */
    public void setNodates(String nodates) {
        this.nodates = new NoDatesParameter(nodates);
    }

    /**
     * Defines the change parameter.
     */
    private Parameter change;

    /**
     * Set the change parameter (specific to diff command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : diff
     * </p>
     * 
     * @param change
     *            , the parameter value
     */
    public void setChange(String change) {
        this.change = new ChangeParameter(change);
    }

    /**
     * Defines the unified parameter.
     */
    private Parameter unified;

    /**
     * Set the unified parameter (specific to diff command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : diff
     * </p>
     * 
     * @param unified
     *            , the parameter value
     */
    public void setUnified(String unified) {
        this.unified = new UnifiedParameter(unified);
    }

    /**
     * Defines the showFunction parameter.
     */
    private Parameter showFunction;

    /**
     * Set the showFunction parameter (specific to diff command).
     * 
     * <p>
     * /!\ ONLY AVAILABLE FOR COMMAND : diff
     * </p>
     * 
     * @param showFunction
     *            , the parameter value
     */
    public void setShowFunction(String showFunction) {
        this.showFunction = new ShowFunctionParameter(showFunction);
    }

    /**
     * Defines ignore parameters (specific to diff command).
     */
    private Vector<Ignore> ignores = new Vector<Ignore>();

    /**
     * Add ignore parameter.
     * 
     * @param ignore
     *            , the new ignore parameter.
     */
    public void addIgnore(Ignore ignore) {
        if (p_cmd != null || !p_cmd.equals("diff")) {
            throw new BuildException(Error.IGNORE_ELEMENT_FORBIDDEN + " FOR COMMAND : " + p_cmd);
        }
        ignores.add(ignore);
    }

    /**
     * Defines auth parameters (specific to push command).
     */
    private Auth auth = null;

    /**
     * Add auth parameter.
     * 
     * @param auth
     *            , the auth parameter.
     */
    public void addAuth(Auth auth) {
        this.auth = auth;
    }

    /**
     * Defines Hg command line consumer.
     */
    private Consumer m_consumer = null;

    public void setConsumer(Consumer consumer) {
        if (consumer == null) {
            Logger.error("NULL CONSUMER");
            return;
        }
        this.m_consumer = consumer;
    }

    /**
     * Default constructor
     */
    public HgTask() {
        super();
        if (this.getProject() == null) {
            Project p = new Project();
            this.setProject(p);
        }
        this.setTaskName("hg");
        Logger.getInstance().init(this);
        Logger.debug("NEW DEFAULT HG TASK");
    }

    public HgTask(HgTask task) {
        super();
        this.setProject(task.getProject());
        this.setTaskName("hg");
        Logger.debug("NEW HG TASK BY COPY");
    }

    /**
     * Constructor with consumer
     */
    public HgTask(Consumer consumer) {
        this();
        this.m_consumer = consumer;
        this.m_consumer.setTask(this);
        Logger.debug("NEW HG TASK WITH CONSUMER");
    }

    /**
     * Implements {@link org.apache.tools.ant.Task#execute}
     */
    @Override
    public void execute() {
        try {
            Logger.debug("EXECUTE HG TASK");
            if (!checkParameters()) {
                throw new BuildException("CHECK PARAMETERS FAILED");
            }
            if (!initTask()) {
                Logger.error("FAIL TO INIT TASK, RETURN");
                return;
            }
            Execute exe = prepareExec();
            String commandline = "";
            String[] cmd = super.cmdl.getCommandline();
            for (int i = 0; i < cmd.length; i++) {
                if (!commandline.isEmpty()) {
                    commandline += " ";
                }
                commandline += cmd[i];
            }
            if (commandline.matches(".*[:][/][/].*[:].*[@].*")) {
                commandline = commandline.substring(0, commandline.lastIndexOf(":") + 1) + "***" + commandline.substring(commandline.indexOf("@"), commandline.length());
            }
            Logger.info("$ " + commandline);
            super.setFailIfExecutionFails(true);
            super.setTimeout(1000);
            runExec(exe);
            if (exe.getExitValue() != 0) {
                throw new BuildException("FAIL TO EXECUTE : " + commandline + " (" + exe.getExitValue() + ")");
            }
        } catch (Exception e) {
            if (e.getMessage() != null) {
                Logger.error(e.getMessage());
            } else {
                Logger.error(e.toString());
                e.printStackTrace();
            }
            throw new BuildException(e);
        }
    }

    /**
     * Checks HgTask parameters.
     * 
     * @param task
     *            , the task to check
     * @throws BuildException
     *             on command or parameter error
     */
    private boolean checkParameters() throws BuildException {
        Logger.debug("CHECK PARAMETERS");
        try {
            Field[] fields = Class.forName("net.sourceforge.ant4hg.HgTask").getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getType().equals(Parameter.class)) {
                    Class[] params = new Class[] { String.class };
                    Method checkMethod = fields[i].getType().getDeclaredMethod("check", params);
                    String[] args = new String[] { p_cmd };
                    Parameter p = (Parameter) fields[i].get(this);
                    if (p != null) {
                        Logger.debug("INVOKE CHECK METHOD ON PARAMETER : " + fields[i].getName());
                        Boolean result = (Boolean) checkMethod.invoke(p, (Object[]) args);
                        if (result == false) {
                            Logger.error("BAD PARAMETER : " + fields[i].getName());
                            return false;
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            Logger.error(Error.SET_PARAMETER_FAILED + "1");
            throw new BuildException(Error.SET_PARAMETER_FAILED);
        } catch (IllegalArgumentException e) {
            Logger.error(Error.SET_PARAMETER_FAILED + "2");
            Logger.error(e.getMessage());
            throw new BuildException(Error.SET_PARAMETER_FAILED);
        } catch (NoSuchMethodException e) {
            Logger.error("NO SUCH METHOD");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Logger.error("ILLEGAL ACCESS");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Logger.error("INVOCATION TARGET");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * Checks HgTask parameters.
     * 
     * @param task
     *            , the task to check
     * @throws BuildException
     *             on command or parameter error
     */
    private boolean initArguments() throws BuildException {
        Logger.debug("INIT ARGUMENTS");
        try {
            Field[] fields = Class.forName("net.sourceforge.ant4hg.HgTask").getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getType().equals(Parameter.class)) {
                    Class[] params = new Class[] { ArgumentFriender.class };
                    Method initMethod = fields[i].getType().getDeclaredMethod("initArgument", params);
                    ArgumentFriender[] args = new ArgumentFriender[] { new ArgumentFriender() };
                    Parameter p = (Parameter) fields[i].get(this);
                    if (p == null) {
                        continue;
                    }
                    if (p_cmd.equals("export") || p_cmd.equals("diff")) {
                        if (fields[i].getName().equals("revision")) {
                            Logger.debug("EXPORT : SKIP REVISION PARAMETER (at the beginning)");
                            continue;
                        }
                    }
                    Logger.debug("INVOKE INIT ARGUMENT ON PARAMETER : " + fields[i].getName());
                    initMethod.invoke(p, (Object[]) args);
                }
            }
        } catch (SecurityException e) {
            Logger.error(Error.SET_PARAMETER_FAILED + "1");
            throw new BuildException(Error.SET_PARAMETER_FAILED);
        } catch (IllegalArgumentException e) {
            Logger.error(Error.SET_PARAMETER_FAILED + "2");
            Logger.error(e.getMessage());
            throw new BuildException(Error.SET_PARAMETER_FAILED);
        } catch (NoSuchMethodException e) {
            Logger.error("NO SUCH METHOD");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            Logger.error("ILLEGAL ACCESS");
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            Logger.error("INVOCATION TARGET");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    protected Execute prepareExec() {
        boolean isSet = false;
        String path = System.getenv("Path");
        if (path != null) {
            for (String component : path.split(File.pathSeparator)) {
                if (new File(component, "hg.bat").isFile() || new File(component, "hg.cmd").isFile()) {
                    setExecutable("cmd");
                    super.cmdl.createArgument().setValue("/c");
                    super.cmdl.createArgument().setValue("hg");
                    isSet = true;
                    break;
                } else if (new File(component, "hg.exe").isFile() || new File(component, "hg").isFile()) {
                    setExecutable("hg");
                    isSet = true;
                    break;
                }
            }
        }
        if (!isSet) {
            setExecutable("hg");
        }
        Execute exe = super.prepareExec();
        HgProducer hgProducer = null;
        if (m_consumer != null) {
            m_consumer.setRootDir(p_file);
            hgProducer = new HgProducer(m_consumer);
        } else {
            hgProducer = new HgProducer(ConsumerFactory.createConsumer(p_cmd).setRootDir(p_file));
        }
        exe.setStreamHandler(hgProducer);
        return exe;
    }

    private boolean initTask() {
        try {
            initCmd();
            initIncludesAndExcludes();
            initArguments();
            initMessage();
            initLogfile();
            initIgnores();
            if (p_file == null) {
                p_file = getProject().getBaseDir();
            }
            if (!p_file.exists()) {
                if (p_cmd.equals("remove") && after != null && after.isTrue()) {
                } else if (p_cmd.equals("commit") && addremove != null && addremove.isTrue()) {
                    p_file = p_file.getParentFile();
                } else {
                    throw new FileNotFoundException(p_file.getAbsolutePath());
                }
            }
            if (p_cmd.equals("init")) {
                super.cmdl.createArgument().setValue(p_file.getAbsolutePath());
            } else if (p_cmd.equals("update")) {
                checkHgDirectory();
            } else if (p_cmd.equals("clone")) {
                initSource();
                initDestination();
            } else if (p_cmd.equals("pull")) {
                checkHgDirectory();
                initSource();
            } else if (p_cmd.equals("push")) {
                checkHgDirectory();
                initDestination();
            } else if (p_cmd.equals("export")) {
                checkHgDirectory();
                initRevision();
            } else {
                checkHgDirectory();
                super.cmdl.createArgument().setValue(p_file.getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            if (e.getMessage() != null) {
                Logger.error("FAIL TO INIT TASK : " + e.getMessage());
            } else {
                Logger.error("FAIL TO INIT TASK");
            }
            return false;
        }
        return true;
    }

    private void checkHgDirectory() throws MissingHgDirectoryException {
        if (!HgHelper.hasHgDirectory(p_file)) {
            throw new MissingHgDirectoryException(Error.HG_DIRECTORY_NOT_FOUND + " FOR " + p_file.getAbsolutePath());
        }
    }

    private void initCmd() {
        if (p_cmd == null) {
            throw new BuildException("COMMAND IS UNDEFINED");
        } else if (p_cmd.isEmpty()) {
            throw new BuildException("COMMAND IS EMPTY");
        }
        StringTokenizer st = new StringTokenizer(p_cmd, " ");
        if (st.countTokens() > 1) {
            super.cmdl.clear();
            while (st.hasMoreTokens()) {
                super.cmdl.createArgument().setValue(st.nextToken());
            }
        } else {
            java.lang.String[] args = super.cmdl.getArguments();
            super.cmdl.clear();
            super.cmdl.createArgument().setValue(p_cmd);
            for (int i = 0; i < args.length; i++) {
                if (args[i] == null || args[i].isEmpty()) {
                    continue;
                }
                super.cmdl.createArgument().setValue(args[i]);
            }
        }
    }

    private void initIncludesAndExcludes() throws BuildException {
        for (DirSet set : p_dirsets) {
            Vector<String> includes = set.getIncludes(getBaseDir());
            String prefix = set.getPrefix(getBaseDir());
            if (!prefix.isEmpty() && includes.size() <= 0) {
                super.cmdl.createArgument().setValue("--include");
                super.cmdl.createArgument().setValue(prefix + "**");
            }
            Vector<String> excludes = set.getExcludes(getBaseDir());
            if (excludes != null) {
                for (String exclude : excludes) {
                    super.cmdl.createArgument().setValue("--exclude");
                    super.cmdl.createArgument().setValue(exclude);
                }
            }
            for (String include : includes) {
                super.cmdl.createArgument().setValue("--include");
                super.cmdl.createArgument().setValue(include);
            }
        }
    }

    private void initMessage() {
        if (message != null && message.isSet()) {
            boolean addMessage = true;
            String[] args = super.cmdl.getArguments();
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("--message") || args[i].equals("-m")) {
                    addMessage = false;
                    break;
                }
            }
            if (addMessage == true) {
                super.cmdl.createArgument().setValue("--message");
                super.cmdl.createArgument().setValue(message.getValue());
            }
        }
    }

    private void initLogfile() {
        if (p_logfile != null) {
            if (!p_logfile.exists()) {
                Logger.error("FILE NOT FOUND : " + p_logfile.getAbsolutePath());
                return;
            }
            super.cmdl.createArgument().setValue("--logfile");
            super.cmdl.createArgument().setValue(p_logfile.getAbsolutePath());
        }
    }

    private void initDestination() {
        if (destination != null) {
            if (p_cmd.equals("clone") && UrlBuilder.isRemoteUrl(destination.getValue())) {
                throw new BuildException("IMPOSSIBLE TO CLONE TO A REMOTE DESTINATION : " + destination.getValue());
            }
            super.cmdl.createArgument().setValue(UrlBuilder.getUrl(destination.getValue(), auth));
        }
    }

    private void initSource() {
        if (source != null) {
            super.cmdl.createArgument().setValue(UrlBuilder.getUrl(source.getValue()));
        } else if (p_cmd.equals("clone")) {
            if (!HgHelper.hasHgDirectory(p_file, false)) {
                throw new BuildException("UNDEFINED SOURCE : CURRENT DIRECTORY IS NOT A LOCAL REPOSITORY : " + p_file.getAbsolutePath());
            }
        }
    }

    private void initRevision() {
        if (!p_cmd.equals("diff") && !p_cmd.equals("export")) {
            return;
        }
        if (revision == null) {
            Logger.error("NO REVISION");
            throw new BuildException("REVISION MUST BE SET");
        }
        if (p_cmd.equals("diff")) {
            String[] revs = revision.getValue().split(";");
            for (int i = 0; i < revs.length; i++) {
                super.cmdl.createArgument().setValue("--rev");
                super.cmdl.createArgument().setValue(revs[i]);
            }
        } else if (p_cmd.equals("diff")) {
            super.cmdl.createArgument().setValue(revision.getValue());
        }
    }

    private void initIgnores() throws BuildException {
        for (Ignore ignore : ignores) {
            super.cmdl.createArgument().setValue(ignore.getName());
        }
    }

    /**
     * Returns the base directory
     * 
     * @return the base directory
     */
    private File getBaseDir() {
        if (p_file == null) {
            throw new NullPointerException(Error.MISSING_PARAMETER + " : file");
        }
        try {
            p_file = p_file.getCanonicalFile();
        } catch (IOException e) {
            throw new BuildException(e);
        }
        if (p_file.isDirectory()) {
            return p_file;
        }
        if (!p_file.exists()) {
            File parent = p_file.getParentFile();
            if (parent == null) {
                throw new BuildException("PARENT NOT FOUND FOR FILE : " + p_file.getAbsolutePath());
            }
            if (!parent.exists()) {
                throw new BuildException(Error.DIRECTORY_NOT_FOUND + " : " + parent.getAbsolutePath());
            }
        }
        return p_file.getParentFile();
    }

    public class ArgumentFriender {

        public void setArgument(String arg) {
            if (arg == null || arg.isEmpty()) {
                Logger.error("EMPTY ARGUMENT");
            }
            cmdl.createArgument().setValue(arg);
        }

        public void setArgument(String arg, String value) {
            if (arg == null || arg.isEmpty()) {
                Logger.error("EMPTY ARGUMENT");
            }
            cmdl.createArgument().setValue(arg);
            cmdl.createArgument().setValue(value);
        }

        private ArgumentFriender() {
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println("ANT4HG");
            HgTask t = new HgTask();
            t.setCmd("clone");
            t.setSource("ssh://bdedardel@ant4docbook.hg.sourceforge.net/hgroot/an4docbook/ant4docbook");
            t.execute();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
