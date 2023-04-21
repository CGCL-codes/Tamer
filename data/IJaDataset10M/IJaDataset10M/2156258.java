package com.bluemarsh.jswat.command;

import com.bluemarsh.jswat.Log;
import com.bluemarsh.jswat.PathManager;
import com.bluemarsh.jswat.Session;
import java.util.List;

/**
 * Defines the class that handles the 'classpath' command.
 *
 * @author  Nathan Fiedler
 */
public class classpathCommand extends JSwatCommand {

    /**
     * Perform the 'classpath' command.
     *
     * @param  session  JSwat session on which to operate.
     * @param  args     Tokenized string of command arguments.
     * @param  out      Output to write messages to.
     */
    public void perform(Session session, CommandArguments args, Log out) {
        PathManager pathman = (PathManager) session.getManager(PathManager.class);
        if (!args.hasMoreTokens()) {
            String[] paths = pathman.getClassPath();
            if (paths != null && paths.length > 0) {
                StringBuffer buf = new StringBuffer(Bundle.getString("classpath.path"));
                buf.append('\n');
                for (int i = 0; i < paths.length; i++) {
                    buf.append(paths[i]);
                    buf.append('\n');
                }
                out.write(buf.toString());
            } else {
                out.writeln(Bundle.getString("classpath.nopath"));
            }
        } else {
            if (session.isActive()) {
                throw new CommandException(Bundle.getString("classpath.active"));
            } else {
                String path = args.rest();
                pathman.setClassPath(path);
                out.writeln(Bundle.getString("classpath.set"));
            }
        }
    }
}
