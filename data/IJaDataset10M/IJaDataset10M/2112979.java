package gov.sandia.ccaffeine.dc.user_iface.ccacmd;

import gov.sandia.ccaffeine.cmd.*;
import java.util.*;
import gov.sandia.ccaffeine.dc.user_iface.ccacmd.CmdActionCCA;

public class CmdActionCCARepository extends CmdActionCCA implements CmdAction {

    public CmdActionCCARepository() {
    }

    public String argtype() {
        return "Ss";
    }

    public String[] names() {
        return namelist;
    }

    public String help() {
        return "repository list\n" + "       - show components available in the repository.\n" + "  The following get options cannot be abbreviated:\n" + "    repository get class\n" + "       - load component from the repository list (private and immediate linking).\n" + "    repository get-global class\n" + "       - load component from the repository list (global and immediate linking).\n" + "    repository get-lazy class\n" + "       - load component from the repository list (private and lazy linking).\n" + "    repository get-lazy-global class\n" + "       - load component from the repository list (global and lazy linking).\n" + "Global linking may be appropriate for components that, as a side effect,\n" + "for example include libblas, liblapack, libppm, and other legacy C/f77 routines.\n" + "Such components will need to be loaded before any other components that use the\n" + "global symbols.";
    }

    private static final String[] namelist = { "repository" };

    public void doIt(CmdContext cc, Vector args) {
        int numberOfArguments = args.size();
        String command = null;
        if (numberOfArguments > 0) command = (String) args.get(0);
        String className = null;
        if (numberOfArguments > 1) className = (String) args.get(1);
        this.broadcastRepository(numberOfArguments, command, className);
    }
}
