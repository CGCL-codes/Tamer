package gruntspud.ui.commandoptions;

import gruntspud.Constants;
import gruntspud.GruntspudContext;
import gruntspud.ui.Tabber;
import gruntspud.ui.UIUtil;
import gruntspud.ui.preferences.GlobalOptionsTab;
import gruntspud.ui.preferences.StickyOptionsTab;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.update.UpdateCommand;

/**
 *  Description of the Class
 *
 *@author     magicthize
 *@created    26 May 2002
 */
public class RetrieveOptionsPane extends Tabber {

    private GlobalOptionsTab globalOptionsTab;

    private StickyOptionsTab stickyTab;

    private GruntspudContext context;

    /**
     *  Constructor for the RetrieveOptionsPane object
     *
     *@param  host  Description of the Parameter
     */
    public RetrieveOptionsPane(GruntspudContext context) {
        super();
        this.context = context;
        stickyTab = new StickyOptionsTab(UIUtil.getCachedIcon(Constants.ICON_TOOL_RETRIEVE), UIUtil.getCachedIcon(Constants.ICON_TOOL_LARGE_RETRIEVE), false, "Retrieve", "Retrieve options. You will be given options to\n" + "to replace the current revision, or to save\n" + "the retrieved version as a file when the command\n" + "has completed.");
        stickyTab.init(context);
        globalOptionsTab = new GlobalOptionsTab();
        globalOptionsTab.init(context);
        addTab(stickyTab);
        addTab(globalOptionsTab);
    }

    /**
     *  Gets the commandsForSettings attribute of the UpdateOptionsPane object
     *
     *@return    The commandsForSettings value
     */
    public Command[] getCommandsForSettings() {
        UpdateCommand cmd = new UpdateCommand();
        cmd.setPipeToOutput(true);
        String d = stickyTab.getSelectedDate();
        if ((d != null) && (d.length() != 0)) {
            cmd.setUpdateByDate(d);
        }
        String r = stickyTab.getSelectedRevision();
        if ((r != null) && (r.length() != 0)) {
            cmd.setUpdateByRevision(r);
        }
        cmd.setUseHeadIfNotFound(stickyTab.isUseHeadIfNotFound());
        return new Command[] { cmd };
    }
}
