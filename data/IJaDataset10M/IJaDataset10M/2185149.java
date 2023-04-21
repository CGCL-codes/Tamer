package org.radrails.rails.internal.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.radrails.rails.core.IRailsConstants;
import org.radrails.rails.core.RailsLog;
import org.radrails.rails.core.launching.IRailsLaunchConfigAttributes;
import org.rubypeople.rdt.launching.IVMInstall;

/**
 * Manages the installations of Rails on the user's system. Provides access to the currently selected install and the
 * ability to run commands relative to the Rails directory.
 * 
 * @author mkent
 */
public class RailsRuntime {

    /**
	 * Runs the rails command. If a Rails install location is specified, the script will be executed from the bin
	 * directory of the installation.
	 * 
	 * @param cmds
	 *            the arguments for the script
	 * @param workingDir
	 *            the working directory
	 * @return a system process handle
	 * @throws CoreException
	 *             if an error occurs launching the process
	 */
    public static Process railsExec(String[] cmds, File workingDir) throws CoreException {
        List<String> cmdsList = new ArrayList<String>();
        String railsPath = RailsPlugin.getInstance().getRailsPath();
        if ((railsPath != null) && !railsPath.equals("")) {
            cmdsList.add(railsPath);
            cmdsList.addAll(Arrays.asList(cmds));
            return rubyExec((String[]) cmdsList.toArray(new String[0]), workingDir);
        } else {
            if (Platform.getOS().equals(Platform.OS_WIN32)) {
                cmdsList.add("cmd.exe");
                cmdsList.add("/C");
            }
            cmdsList.add("rails");
            cmdsList.addAll(Arrays.asList(cmds));
            return DebugPlugin.exec((String[]) cmdsList.toArray(new String[0]), workingDir);
        }
    }

    /**
	 * Runs a Ruby script on the command line. If a Ruby interpreter is selected in the RDT preferences, it will be
	 * used.
	 * 
	 * @param preCmds
	 *            Commands to be placed at the very beginning of the launch string
	 * @param cmds
	 *            the script and its arguments
	 * @param workingDir
	 *            the working directory
	 * @param cmdPrompt
	 *            if true, the Windows command prompt will be launched
	 * @return a system process handle
	 * @throws CoreException
	 *             if an error occurs launching the process
	 */
    public static Process rubyExec(String[] preCmds, String[] cmds, File workingDir, boolean cmdPrompt) throws CoreException {
        int allCmdsCount = 0;
        int cmdsCount = 0;
        int preCmdsCount = 0;
        int syncCmdsCount = IRailsConstants.OUTPUT_SYNC.length;
        if (preCmds != null) preCmdsCount = preCmds.length;
        if (cmds != null) cmdsCount = cmds.length;
        String[] allCmds = new String[preCmdsCount + syncCmdsCount + cmdsCount + 1];
        IVMInstall interp = org.rubypeople.rdt.launching.RubyRuntime.getDefaultVMInstall();
        if (interp != null) {
            String rubyPath = interp.getInstallLocation().getAbsolutePath();
            File rubyParentFile = new File(rubyPath).getParentFile();
            if (rubyPath.substring(rubyParentFile.getAbsolutePath().length() + 1).equals("bin")) rubyPath = rubyPath + "/ruby"; else rubyPath = rubyPath + "/bin/ruby";
            allCmds[allCmdsCount] = rubyPath;
            allCmdsCount += 1;
        } else {
            if (cmdPrompt && Platform.getOS().equals(Platform.OS_WIN32)) {
                allCmds = new String[preCmdsCount + syncCmdsCount + cmdsCount + 3];
                allCmds[allCmdsCount] = "cmd.exe";
                allCmdsCount += 1;
                allCmds[allCmdsCount] = "/C";
                allCmdsCount += 1;
            }
            allCmds[allCmdsCount] = "ruby";
            allCmdsCount += 1;
        }
        if (preCmdsCount > 0) {
            System.arraycopy(preCmds, 0, allCmds, allCmdsCount, preCmdsCount);
            allCmdsCount += preCmdsCount;
        }
        System.arraycopy(IRailsConstants.OUTPUT_SYNC, 0, allCmds, allCmdsCount, syncCmdsCount);
        allCmdsCount += syncCmdsCount;
        System.arraycopy(cmds, 0, allCmds, allCmdsCount, cmdsCount);
        RailsLog.logInfo("RailsRuntime.rubyExec: executing: " + Arrays.toString(allCmds), null);
        try {
            return DebugPlugin.exec(allCmds, workingDir);
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
	 * Runs a Ruby script on the command line. If a Ruby interpreter is selected in the RDT preferences, it will be
	 * used.
	 * 
	 * @param cmds
	 *            the script and its arguments
	 * @param workingDir
	 *            the working directory
	 * @param cmdPrompt
	 *            if true, the Windows command prompt will be launched
	 * @return a system process handle
	 * @throws CoreException
	 *             if an error occurs launching the process
	 */
    public static Process rubyExec(String[] cmds, File workingDir, boolean cmdPrompt) throws CoreException {
        return rubyExec(null, cmds, workingDir, cmdPrompt);
    }

    /**
	 * Convenience method that calls rubyExec with a default value of <code>true</code> for the cmdPrompt parameter.
	 * 
	 * @param cmds
	 *            the script and its arguments
	 * @param workingDir
	 *            the working directory
	 * @return a system process handle
	 * @throws CoreException
	 *             if an error occures launching the process
	 */
    public static Process rubyExec(String[] cmds, File workingDir) throws CoreException {
        return rubyExec(null, cmds, workingDir, true);
    }

    /**
	 * Convenience method that calls exec with a default value of <code>true</code> for the cmdPrompt parameter.
	 * 
	 * @param cmds
	 *            the commands to execute
	 * @param workingDir
	 *            the working directory
	 * @return a system process handle
	 * @throws CoreException
	 *             if an error occurs launching the process
	 */
    public static Process exec(String[] cmds, File workingDir) throws CoreException {
        return exec(cmds, workingDir, true);
    }

    /**
	 * Runs a command on the command line. Opens the command prompt if necessary on Windows systems.
	 * 
	 * @param cmds
	 *            the commands to execute
	 * @param workingDir
	 *            the working directory
	 * @param cmdPrompt
	 *            if true, the Windows command prompt will be launched.
	 * @return a system process handle
	 * @throws CoreException
	 *             if an error occurs launching the process
	 */
    public static Process exec(String[] cmds, File workingDir, boolean cmdPrompt) throws CoreException {
        List<String> cmdsList = new ArrayList<String>();
        if (cmdPrompt && Platform.getOS().equals(Platform.OS_WIN32)) {
            cmdsList.add("cmd.exe");
            cmdsList.add("/C");
        }
        cmdsList.addAll(Arrays.asList(cmds));
        return DebugPlugin.exec((String[]) cmdsList.toArray(new String[0]), workingDir);
    }

    /**
	 * Runs a Rake task. If a Rake gem is specified, runs the command from that directory. Otherwise, attempts to run on
	 * the path.
	 * 
	 * @param cmds
	 *            the arguments to the Rake command
	 * @param workingDir
	 *            the working directory
	 * @return a system process handle
	 * @throws CoreException
	 *             if an error occurs launching the process
	 */
    public static Process rakeExec(String[] cmds, File workingDir) throws CoreException {
        List<String> cmdsList = new ArrayList<String>();
        String rakePath = RailsPlugin.getInstance().getRakePath();
        if (rakePath != null) {
            cmdsList.add(rakePath);
            cmdsList.addAll(Arrays.asList(cmds));
            return rubyExec((String[]) cmdsList.toArray(new String[0]), workingDir);
        } else {
            if (Platform.getOS().equals(Platform.OS_WIN32)) {
                cmdsList.add("cmd.exe");
                cmdsList.add("/C");
            }
            cmdsList.add("rake");
            cmdsList.addAll(Arrays.asList(cmds));
            return DebugPlugin.exec((String[]) cmdsList.toArray(new String[0]), workingDir);
        }
    }

    public static ILaunchConfigurationWorkingCopy createInternalLaunch(String procType, String label) throws CoreException {
        ILaunchConfigurationType configType = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationType(IRailsLaunchConfigAttributes.ID);
        ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, label);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy h:mm:ss aa");
        String date = dateFormat.format(new Date());
        String procLabel = procType + " (" + date + ")";
        wc.setAttribute(IRailsLaunchConfigAttributes.PROCESS_LABEL, procLabel);
        return wc;
    }
}
