package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.breakpoint.Breakpoint;
import com.bluemarsh.jswat.breakpoint.BreakpointManager;
import com.bluemarsh.jswat.breakpoint.ExceptionBreakpoint;
import com.bluemarsh.jswat.breakpoint.ResolveException;
import com.sun.jdi.request.EventRequest;

/**
 * Defines the class that handles the 'catch' command.
 *
 * @author  Nathan Fiedler
 */
public class catchCommand extends JSwatCommand {

    /**
     * Perform the 'catch' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        if (!args.hasMoreTokens()) {
            throw new MissingArgumentsException();
        }
        String cname = args.nextToken();
        int suspendPolicy = EventRequest.SUSPEND_ALL;
        if (cname.equals("go")) {
            suspendPolicy = EventRequest.SUSPEND_NONE;
            cname = null;
        } else if (cname.equals("thread")) {
            suspendPolicy = EventRequest.SUSPEND_EVENT_THREAD;
            cname = null;
        }
        if (cname == null) {
            if (!args.hasMoreTokens()) {
                throw new MissingArgumentsException();
            }
            cname = args.nextToken();
        }
        boolean caught = true;
        boolean uncaught = true;
        try {
            BreakpointManager brkman = (BreakpointManager) session.getManager(BreakpointManager.class);
            Breakpoint bp = new ExceptionBreakpoint(cname, caught, uncaught);
            brkman.addNewBreakpoint(bp);
            bp.setEnabled(false);
            bp.setSuspendPolicy(suspendPolicy);
            bp.setEnabled(true);
            out.writeln(Bundle.getString("catch.added"));
        } catch (ClassNotFoundException cnfe) {
            StringBuffer buf = new StringBuffer(Bundle.getString("catch.badClassName"));
            buf.append(": ");
            buf.append(cname);
            throw new CommandException(buf.toString());
        } catch (ResolveException re) {
            throw new CommandException(re);
        }
    }
}
