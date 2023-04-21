package launcher.runtime.process;

import java.util.List;
import java.util.regex.Matcher;
import launcher.runtime.ExecutingCommandImpl;
import launcher.util.TextUtils;

/**
 * A process manager that handles OS commands for Windows XP professional. This
 * basically means using TASKKILL and TASKLIST. This process manager cannot
 * identify child processes.
 * 
 * @deprecated XP tasklist is too unreliable to use, especially if the command
 *             kicks off batch files. The process is basically lost by the
 *             manager so there is no control to kill the process.
 * @author Ramon Servadei
 * @version $Revision: 1.2 $
 * 
 */
public class XpProfessionalProcessManager extends ProcessManager {

    private static final CommandsLibrary commands;

    static final String DEFAULT_COMMAND_GROUP = "1";

    static final String DEFAULT_PID_GROUP = "2";

    static final String DEFAULT_PROCESS_KILL_COMMAND = "taskkill /F /PID";

    static final String DEFAULT_PROCESS_LIST_COMMAND = "tasklist /FO csv /NH";

    static final String DEFAULT_REGEX = "\"([\\w \\.]*)\",\"([\\d]*)\".*";

    static {
        commands = new CommandsLibrary(TextUtils.getUnqualifiedClassname(XpProfessionalProcessManager.class), DEFAULT_PROCESS_LIST_COMMAND, DEFAULT_REGEX, DEFAULT_PID_GROUP, DEFAULT_PID_GROUP, DEFAULT_COMMAND_GROUP, DEFAULT_PROCESS_KILL_COMMAND);
    }

    public XpProfessionalProcessManager(ExecutingCommandImpl command) {
        super(command);
    }

    /**
   * Old school, less reliable - cannot find true child processes... it just
   * adds all pids as children to the tree. Hopefully the order of the pids is
   * at least consistent with when it started (thats windows for you)
   */
    @Override
    public final ProcessTree getProcessTreeFromProcessList(List<String> processes) throws Exception {
        ProcessTree currentProcessTree = new ProcessTree();
        for (String line : processes) {
            Matcher matcher = commands.REGEX.matcher(line.trim());
            if (matcher.matches()) {
                currentProcessTree.addChild(matcher.group(commands.PID_GROUP)).setProcessCommandLine(matcher.group(commands.COMMAND_GROUP));
            }
        }
        return currentProcessTree;
    }

    @Override
    CommandsLibrary getCommandsLibrary() {
        return commands;
    }

    /**
   * For XP's tasklist command, we have no parent-child relationship, so we need
   * to find the user command as we cannot manage a parent-child relationship
   * with the shell and usercommand
   */
    @Override
    void getInitialProcessTree(List<String> processesAfter, String pidOfShellCommand, String pidOfUserCommand) throws Exception {
        if (pidOfUserCommand != null) {
            setOurProcessTree(getProcessTreeFromProcessList(processesAfter).getChildWithPid(pidOfUserCommand));
        }
    }
}
