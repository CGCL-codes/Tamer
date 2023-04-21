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
import gruntspud.ui.commandoptions.DefaultOptionsPane;
import gruntspud.ui.report.EditorsFileInfoPane;
import gruntspud.ui.report.FileInfoPane;
import gruntspud.ui.view.ViewManager;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import org.netbeans.lib.cvsclient.command.Command;
import org.netbeans.lib.cvsclient.command.editors.EditorsCommand;

/**
 * CVS action to show the current editors of the selection
 * 
 * @author magicthize
 */
public class EditorsAction extends ReportingGruntspudAction {

    static ResourceBundle res = ResourceBundle.getBundle("gruntspud.actions.ResourceBundle");

    /**
	 * Constructor for the EditorsAction object
	 * 
	 * @param context context
	 */
    public EditorsAction(GruntspudContext context) {
        super(res, "editorsAction", context);
        putValue(GruntspudAction.ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_EDITORS));
        putValue(DefaultGruntspudAction.SMALL_ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_SMALL_EDITORS));
    }

    public boolean checkAvailable() {
        ViewManager mg = getContext().getViewManager();
        CVSFileNode[] sel = mg.getSelectedNodes();
        return !CVSCommandHandler.getInstance().isCommandRunning() && mg.isHomeExists() && ((sel != null) && (sel.length > 0) && (mg.getSelectedHaveRootCount() == sel.length));
    }

    public FileInfoPane createFileInfoPane() {
        return new EditorsFileInfoPane(getContext());
    }

    public String getFileInfoName() {
        return "editorsInfo";
    }

    public String getFileInfoText() {
        return res.getString("editorsAction.fileInfoText");
    }

    public void actionPerformed(final ActionEvent evt) {
        final Component parent = getParentComponentForEvent(evt);
        final CVSFileNode[] sel = getContext().getViewManager().getNodesToPerformActionOn();
        final OptionDialog.Option ok = new OptionDialog.Option(res.getString("editorsAction.optionDialog.option.ok.text"), res.getString("editorsAction.optionDialog.option.ok.toolTipText"), ResourceUtil.getResourceMnemonic(res, "editorsAction.optionDialog.option.ok.mnemonic"));
        OptionDialog.Option cancel = new OptionDialog.Option(res.getString("editorsAction.optionDialog.option.cancel.text"), res.getString("editorsAction.optionDialog.option.cancel.toolTipText"), ResourceUtil.getResourceMnemonic(res, "editorsAction.optionDialog.option.cancel.mnemonic"));
        final DefaultOptionsPane editorsOptions = new DefaultOptionsPane(getContext(), UIUtil.getCachedIcon(Constants.ICON_TOOL_EDITORS), "Editors", Constants.EDITORS_GENERAL_DO_NOT_RECURSE, new EditorsCommand(), UIUtil.getCachedIcon(Constants.ICON_TOOL_LARGE_EDITORS));
        if (!isBypassOptions(evt.getModifiers())) {
            OptionDialog.Option opt = OptionDialog.showOptionDialog("editors", getContext(), parent, new OptionDialog.Option[] { ok, cancel }, new OutputOptionWrapperPanel(getContext(), editorsOptions, this, "editors"), res.getString("editorsAction.optionDialog.title"), ok, new OptionDialog.Callback() {

                public boolean canClose(OptionDialog dialog, OptionDialog.Option option) {
                    return editorsOptions.validateTabs();
                }

                public void close(OptionDialog dialog, Option option) {
                    if (option == ok) {
                        runCommand(editorsOptions, parent, sel);
                    }
                }
            }, true, false, null, null, !getContext().getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_COMMAND_OPTIONS_NON_MODAL, false));
            if (opt != ok) {
                return;
            }
        } else {
            if (!editorsOptions.validateTabs()) return;
            runCommand(editorsOptions, parent, sel);
        }
    }

    private void runCommand(DefaultOptionsPane editorsOptions, Component parent, CVSFileNode[] sel) {
        editorsOptions.applyTabs();
        Command[] cmd = editorsOptions.getCommandsForSettings();
        CVSCommandHandler.getInstance().runCommandGroup(parent, getContext(), null, cmd, sel, null, false, null, null, this, getEnabledOptionalListeners());
    }
}
