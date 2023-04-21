package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.Session;
import com.bluemarsh.jswat.breakpoint.Breakpoint;
import com.bluemarsh.jswat.breakpoint.BreakpointManager;
import java.util.NoSuchElementException;

/**
 * Defines the class that handles the 'filter' command.
 *
 * @author  Nathan Fiedler
 */
public class filterCommand extends JSwatCommand {

    /**
     * Perform the 'filter' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        String action = null;
        String brknumStr = null;
        String type = null;
        String filter = null;
        try {
            action = args.nextToken();
            brknumStr = args.nextToken();
            type = args.nextToken();
            filter = args.nextToken();
        } catch (NoSuchElementException nsee) {
            throw new MissingArgumentsException();
        }
        int brknum = -1;
        try {
            brknum = Integer.parseInt(brknumStr);
        } catch (NumberFormatException nfe) {
            throw new CommandException(Bundle.getString("filter.badbrk"));
        }
        BreakpointManager brkman = (BreakpointManager) session.getManager(BreakpointManager.class);
        Breakpoint brk = brkman.getBreakpoint(brknum);
        if (brk == null) {
            throw new CommandException(Bundle.getString("filter.nobrk"));
        }
        if (action.equals("add")) {
            String filters;
            if (type.equals("thread")) {
                filters = brk.getThreadFilters();
            } else if (type.equals("class")) {
                filters = brk.getClassFilters();
            } else {
                throw new CommandException(Bundle.getString("filter.badtype"));
            }
            if (filters != null && filters.length() > 0) {
                filters = filters + "," + filter;
            } else {
                filters = filter;
            }
            if (type.equals("thread")) {
                brk.setThreadFilters(filters);
            } else if (type.equals("class")) {
                brk.setClassFilters(filters);
            }
            out.writeln(Bundle.getString("filter.added"));
        } else if (action.equals("del")) {
            String filters;
            if (type.equals("thread")) {
                filters = brk.getThreadFilters();
            } else if (type.equals("class")) {
                filters = brk.getClassFilters();
            } else {
                throw new CommandException(Bundle.getString("filter.badtype"));
            }
            if (filters == null) {
                throw new CommandException(Bundle.getString("filter.nofilter"));
            }
            int idx = filters.indexOf(filter);
            if (idx == 0) {
                int comma = filters.indexOf(',', idx);
                if (comma > 0) {
                    filters = filters.substring(comma + 1);
                } else {
                    filters = "";
                }
            } else if (idx + filter.length() == filters.length()) {
                int comma = filters.lastIndexOf(',', idx);
                filters = filters.substring(0, comma);
            } else if (idx > 0) {
                int comma = filters.lastIndexOf(',', idx);
                filters = filters.substring(0, comma) + filters.substring(idx + filter.length());
            } else {
                throw new CommandException(Bundle.getString("filter.nofilter"));
            }
            if (type.equals("thread")) {
                brk.setThreadFilters(filters);
            } else if (type.equals("class")) {
                brk.setClassFilters(filters);
            }
            out.writeln(Bundle.getString("filter.removed"));
        } else {
            throw new CommandException(Bundle.getString("filter.badaction"));
        }
    }
}
