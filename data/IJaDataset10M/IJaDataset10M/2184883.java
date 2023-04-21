package cu.ftpd.commands.site.actions;

import cu.ftpd.Connection;
import cu.ftpd.Server;
import cu.ftpd.ServiceManager;
import cu.ftpd.filesystem.FileSystem;
import cu.ftpd.logging.Formatter;
import cu.ftpd.user.User;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Markus Jevring <markus@jevring.net>
 * @since 2007-okt-26 : 17:46:25
 * @version $Id: Action.java 258 2008-10-26 12:47:23Z jevring $
 */
public abstract class Action {

    protected static final Pattern ip = Pattern.compile("(.+)@(.+)");

    protected String name;

    protected Action(String name) {
        this.name = name;
    }

    protected Action() {
    }

    public String getName() {
        return name;
    }

    public abstract void execute(String[] parameterList, Connection connection, User user, FileSystem fs);

    public void help(boolean error, Connection connection, FileSystem fs) {
        help(error, connection, fs, this.name);
    }

    protected void help(boolean error, Connection connection, FileSystem fs, String commandName) {
        try {
            connection.reply((error ? 500 : 200), fs.readExternalTextFile(ServiceManager.getServices().getSettings().getDataDirectory().getAbsolutePath() + "/help/" + commandName + ".txt"), true);
            connection.respond((error ? 500 : 200) + " you have been helped.");
        } catch (IOException e) {
            connection.respond("500 Could not read helpfile: " + e.getMessage());
        }
    }

    protected String createHeader() {
        return Formatter.createHeader(this.name);
    }

    protected String escape(String source) {
        return source.replace("\\", "\\\\").replace(";", "\\;");
    }

    /**
     * Goes over an array of strings to see if "-parameter=value" exists, and if it if does,
     * splits the entry and returns the value.
     *
     * @param array the array to search.
     * @param parameter the parameter to search for.
     * @return the value if the parameter was found, null otherwise.
     */
    protected String getParameter(String[] array, String parameter) {
        for (int i = 0; i < array.length; i++) {
            String s = array[i];
            if (s.toLowerCase().startsWith("-" + parameter.toLowerCase() + "=")) {
                return s.substring(parameter.length() + 2);
            }
        }
        return null;
    }
}
