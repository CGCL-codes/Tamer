package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.PathManager;
import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.VMConnection;
import com.bluemarsh.jswat.util.JVMArguments;

/**
 * Defines the class that handles the 'load' command.
 *
 * @author  Nathan Fiedler
 */
public class loadCommand extends JSwatCommand {

    /** True if the VM we start should be suspended. */
    protected boolean startSuspended = true;

    /**
     * Perform the 'load' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        if (session.isActive()) {
            session.deactivate(false, this);
        }
        String mainClass = session.getProperty("mainClass");
        String jvmOptions = session.getProperty("jvmOptions");
        JVMArguments jvmArgs = null;
        if (!args.hasMoreTokens()) {
            jvmArgs = new JVMArguments(jvmOptions + ' ' + mainClass);
            mainClass = jvmArgs.stuffAfterOptions();
        } else {
            args.returnAsIs(true);
            jvmArgs = new JVMArguments(args.rest());
            mainClass = jvmArgs.stuffAfterOptions();
        }
        PathManager pathman = (PathManager) session.getManager(PathManager.class);
        String classpath = pathman.getClassPathAsString();
        jvmOptions = jvmArgs.normalizedOptions(classpath);
        if (mainClass == null || mainClass.length() == 0) {
            throw new MissingArgumentsException(Bundle.getString("load.missingClass"));
        }
        VMConnection connection = VMConnection.buildConnection(null, null, jvmOptions, mainClass);
        out.writeln(connection.loadingString());
        session.setProperty("mainClass", mainClass);
        session.setProperty("jvmOptions", jvmArgs.parsedOptions());
        session.setProperty("startSuspended", String.valueOf(startSuspended));
        if (connection.launchDebuggee(session, true)) {
            if (!startSuspended) {
                session.resumeVM(this, false, false);
            }
        } else {
            StringBuffer buf = new StringBuffer();
            buf.append(com.bluemarsh.jswat.Bundle.getString("vmLoadFailed"));
            if (!startSuspended) {
                buf.append('\n');
                buf.append(Bundle.getString("run.tryLoadInstead"));
            }
            throw new CommandException(buf.toString());
        }
    }
}
