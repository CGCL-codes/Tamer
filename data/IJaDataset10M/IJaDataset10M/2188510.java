package net.sourceforge.cruisecontrol.bootstrappers;

import net.sourceforge.cruisecontrol.Bootstrapper;
import net.sourceforge.cruisecontrol.CruiseControlException;
import net.sourceforge.cruisecontrol.util.Commandline;
import net.sourceforge.cruisecontrol.util.Util;
import net.sourceforge.cruisecontrol.util.ValidationHelper;
import org.apache.log4j.Logger;

/**
 * This class allows you to start up ClearCase dynamic views and mount VOBs before you initiate your build. If your view
 * has been stopped, a VOB unmounted or your machine rebooted, the likelihood is that your build will fail when using
 * dynamic views. The class therefore allows you to specify a viewpath, from which it works out the view tag and starts
 * it, optionally you can specify voblist, a comma separated list of VOBs to mount. The reason a viewpath is used rather
 * than just the view path is that you can reuse a CruiseControl property which defines the source of your build. You
 * should always specify the viewpath via the root location, i.e. M:\... on Windows or /view/... on Unix Usage:
 * &lt;clearcaseviewstrapper viewpath="M:\dynamic_view\some_vob\src" voblist="\SourceVOB,\ReleaseVOB"/%gt;
 *
 * @author <a href="mailto:kevin.lee@buildmeister.com">Kevin Lee</a>
 */
public class ClearCaseViewstrapper implements Bootstrapper {

    private static final Logger LOG = Logger.getLogger(ClearCaseViewstrapper.class);

    private String viewpath;

    private String voblist;

    /**
     * set the path to the view to be started
     *
     * @param path
     *            path to view to be started
     */
    public void setViewpath(String path) {
        viewpath = path;
    }

    /**
     * set the list of VOBs to mount, the list is comma separated
     *
     * @param list
     *            comma separated list of VOBs to mount
     */
    public void setVoblist(String list) {
        voblist = list;
    }

    public void bootstrap() throws CruiseControlException {
        Commandline commandLine = buildStartViewCommand();
        try {
            commandLine.executeAndWait(LOG);
        } catch (Exception e) {
            throw new CruiseControlException("Error executing ClearCase startview command", e);
        }
        if (voblist != null) {
            String[] vobs = getVobsFromList(voblist);
            for (int i = 0; i < vobs.length; i++) {
                commandLine = buildMountVOBCommand(vobs[i]);
                try {
                    commandLine.executeAndWait(LOG);
                } catch (Exception e) {
                    throw new CruiseControlException("Error executing ClearCase mount command", e);
                }
            }
        }
    }

    private String[] getVobsFromList(String voblist) {
        return voblist.split(",");
    }

    public void validate() throws CruiseControlException {
        ValidationHelper.assertIsSet(viewpath, "viewpath", this.getClass());
    }

    protected Commandline buildStartViewCommand() {
        Commandline commandLine = new Commandline();
        commandLine.setExecutable("cleartool");
        commandLine.createArguments("startview", getViewName());
        return commandLine;
    }

    protected Commandline buildMountVOBCommand(String vob) {
        Commandline commandLine = new Commandline();
        commandLine.setExecutable("cleartool");
        commandLine.createArguments("mount", vob);
        return commandLine;
    }

    private String getViewName() {
        String viewname;
        if (isWindows()) {
            viewname = getWindowsViewname(viewpath);
        } else {
            viewname = getUnixViewname(viewpath);
        }
        return viewname;
    }

    private String getUnixViewname(String viewpath) {
        String[] details = viewpath.split("/", 4);
        return details.length < 3 ? null : details[2];
    }

    private String getWindowsViewname(String viewpath) {
        String[] details = viewpath.split("\\\\", 3);
        return details.length < 2 ? null : details[1];
    }

    protected boolean isWindows() {
        return Util.isWindows();
    }
}
