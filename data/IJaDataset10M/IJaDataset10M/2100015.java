package dscript.connect;

import dscript.DApplet;
import dscript.Var;
import dscript.ActionContainer;
import dscript.VarContainer;
import dscript.Output;
import java.io.InputStream;

class DustySystem extends Dustyable {

    private static String file_sep = "";

    private static String line_sep = "";

    private static String base_dir = "";

    static {
        if (!DApplet.using_as_applet) {
            file_sep = System.getProperty("file.separator");
            line_sep = System.getProperty("line.separator");
            base_dir = System.getProperty("user.dir");
        } else {
            file_sep = "/";
            line_sep = "\n";
            base_dir = DApplet.urlpath;
        }
    }

    public DustySystem() {
    }

    public boolean processCommand(String message, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (message.equals("execute")) {
            Output out = getJavaConnector().getOutput();
            InputStream in = System.in;
            VarContainer vc = new VarContainer(out);
            ActionContainer ac = new ActionContainer(out);
            getJavaConnector().runProcessor(args[0], ac, vc);
            return true;
        }
        return false;
    }

    public boolean processCommand(String message) {
        if (message.equalsIgnoreCase("settime")) {
            getJavaConnector().sendActionMessage("" + System.currentTimeMillis());
            return true;
        }
        if (message.equalsIgnoreCase("setfile")) {
            getJavaConnector().sendActionMessage(file_sep);
            return true;
        }
        if (message.equalsIgnoreCase("setline")) {
            getJavaConnector().sendActionMessage(line_sep);
            return true;
        }
        if (message.equalsIgnoreCase("setuser")) {
            getJavaConnector().sendActionMessage(base_dir);
            return true;
        }
        if (message.equalsIgnoreCase("gc")) {
            System.gc();
            return true;
        }
        return false;
    }

    public Var getVar(String[] args) {
        if (args.length < 1) {
            return Dustyable.NOT_THERE;
        }
        if (args[0].equals("random")) {
            return new Var(Math.random(), "random");
        }
        if (args.length == 2) {
            if (args[0].equals("toupper")) {
                return new Var(args[1].toUpperCase(), "toupper");
            }
            if (args[0].equals("tolower")) {
                return new Var(args[1].toLowerCase(), "tolower");
            }
        }
        return Dustyable.NOT_THERE;
    }
}
