package gruntspud.actions;

import gruntspud.CVSCommandHandler;
import gruntspud.CVSFileNode;
import gruntspud.Constants;
import gruntspud.GruntspudContext;
import gruntspud.ResourceUtil;
import gruntspud.ui.OptionDialog;
import gruntspud.ui.OutputOptionWrapperPanel;
import gruntspud.ui.UIUtil;
import gruntspud.ui.OptionDialog.Option;
import gruntspud.ui.commandoptions.UpdateOptionsPane;
import gruntspud.ui.report.FileInfoPane;
import gruntspud.ui.report.UpdateFileInfoPane;
import gruntspud.ui.view.ViewManager;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ResourceBundle;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.event.FileUpdatedEvent;

/**
 * CVS action to bring the selected files and directories up-to-date with whats
 * in the repository
 * 
 * @author magicthize
 */
public class UpdateAction extends ColoredReportingGruntspudAction {

    static ResourceBundle res = ResourceBundle.getBundle("gruntspud.actions.ResourceBundle");

    private UpdateOptionsPane updateOptions;

    /**
     * Constructor for the UpdateAction object
     * 
     * @param context
     *            context
     */
    public UpdateAction(GruntspudContext context) {
        super(res, "updateAction", context);
        putValue(GruntspudAction.ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_UPDATE));
        putValue(DefaultGruntspudAction.SMALL_ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_SMALL_UPDATE));
        setUpdatesFiles(true);
    }

    public void fileUpdated(FileUpdatedEvent e) {
        super.fileUpdated(e);
        CVSFileNode n = getContext().getViewManager().getNodeForFile(new File(e.getFilePath()));
        Constants.UI_LOG.debug("Checking for remote status of " + n);
        if (n != null) {
            n.setRemoteStatus(null);
        }
    }

    public FileInfoPane createFileInfoPane() {
        return new UpdateFileInfoPane(getContext());
    }

    public String getFileInfoName() {
        return "updateFileInfo";
    }

    public String getFileInfoText() {
        return res.getString("updateAction.fileInfoText");
    }

    public boolean checkAvailable() {
        ViewManager mg = getContext().getViewManager();
        CVSFileNode[] sel = mg.getSelectedNodes();
        return !CVSCommandHandler.getInstance().isCommandRunning() && mg.isHomeExists() && ((sel == null) || ((sel.length == mg.getSelectedDirectoryCount()) && (mg.getSelectedFileCount() == 0) && (mg.getSelectedHaveRootCount() == sel.length)) || ((mg.getSelectedFileCount() == sel.length) && (mg.getSelectedHaveRootCount() == sel.length)));
    }

    public void actionPerformed(final ActionEvent evt) {
        final CVSFileNode[] sel = getContext().getViewManager().getNodesToPerformActionOn();
        final Component parent = getParentComponentForEvent(evt);
        final OptionDialog.Option ok = new OptionDialog.Option(res.getString("updateAction.optionDialog.option.ok.text"), res.getString("updateAction.optionDialog.option.ok.toolTipText"), ResourceUtil.getResourceMnemonic(res, "updateAction.optionDialog.option.ok.mnemonic"));
        final OptionDialog.Option cancel = new OptionDialog.Option(res.getString("updateAction.optionDialog.option.cancel.text"), res.getString("updateAction.optionDialog.option.cancel.toolTipText"), ResourceUtil.getResourceMnemonic(res, "updateAction.optionDialog.option.cancel.mnemonic"));
        updateOptions = new UpdateOptionsPane(getContext());
        if (!isBypassOptions(evt.getModifiers())) {
            OptionDialog.Option opt = OptionDialog.showOptionDialog("update", getContext(), parent, new OptionDialog.Option[] { ok, cancel }, new OutputOptionWrapperPanel(getContext(), updateOptions, this, "update"), res.getString("updateAction.optionDialog.title"), ok, new OptionDialog.Callback() {

                public boolean canClose(OptionDialog dialog, OptionDialog.Option option) {
                    return (option == cancel) || updateOptions.validateTabs();
                }

                public void close(OptionDialog dialog, Option option) {
                    if (option == ok) {
                        runCommand(updateOptions, parent, sel);
                    }
                }
            }, true, false, null, null, !getContext().getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_COMMAND_OPTIONS_NON_MODAL, false));
            if (opt != ok) {
                return;
            }
        } else {
            if (!updateOptions.validateTabs()) return;
            runCommand(updateOptions, parent, sel);
        }
    }

    private void runCommand(UpdateOptionsPane updateOptions, Component parent, CVSFileNode[] sel) {
        updateOptions.applyTabs();
        Command[] cmd = updateOptions.getCommandsForSettings();
        CVSCommandHandler.getInstance().runCommandGroup(parent, getContext(), (sel == null) ? getContext().getViewManager().getCWDNode().getFile() : null, cmd, sel, null, false, null, null, this, getEnabledOptionalListeners());
    }
}
