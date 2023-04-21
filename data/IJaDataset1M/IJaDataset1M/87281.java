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
import gruntspud.ui.commandoptions.AnnotateOptionsPane;
import gruntspud.ui.report.AnnotateFileInfoPane;
import gruntspud.ui.report.FileInfoPane;
import gruntspud.ui.view.ViewManager;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ResourceBundle;
import org.netbeans.lib.cvsclient.command.Command;

/**
 * Action to show annotations on the selected files or directorys
 * 
 * @author magicthize
 */
public class AnnotateAction extends ReportingGruntspudAction {

    static ResourceBundle res = ResourceBundle.getBundle("gruntspud.actions.ResourceBundle");

    /**
	 * Constructor for the AnnotateAction object
	 * 
	 * @param context context
	 */
    public AnnotateAction(GruntspudContext context) {
        super(res, "annotateAction", context);
        putValue(GruntspudAction.ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_ANNOTATE));
        putValue(DefaultGruntspudAction.SMALL_ICON, UIUtil.getCachedIcon(Constants.ICON_TOOL_SMALL_ANNOTATE));
    }

    public FileInfoPane createFileInfoPane() {
        return new AnnotateFileInfoPane(getContext());
    }

    public String getFileInfoName() {
        return "annotateFileInfo";
    }

    public String getFileInfoText() {
        return res.getString("annotateAction.fileInfoText");
    }

    public boolean checkAvailable() {
        ViewManager mg = getContext().getViewManager();
        CVSFileNode[] sel = mg.getSelectedNodes();
        return !CVSCommandHandler.getInstance().isCommandRunning() && mg.isHomeExists() && ((sel != null) && (sel.length > 0) && (mg.getSelectedHaveRootCount() == sel.length));
    }

    public void actionPerformed(final ActionEvent evt) {
        final Component parent = getParentComponentForEvent(evt);
        final CVSFileNode[] sel = getContext().getViewManager().getNodesToPerformActionOn();
        final OptionDialog.Option ok = new OptionDialog.Option(res.getString("annotateAction.optionDialog.option.ok.text"), res.getString("annotateAction.optionDialog.option.ok.toolTipText"), ResourceUtil.getResourceMnemonic(res, "annotateAction.optionDialog.option.ok.mnemonic"));
        final OptionDialog.Option cancel = new OptionDialog.Option(res.getString("annotateAction.optionDialog.option.cancel.text"), res.getString("annotateAction.optionDialog.option.cancel.toolTipText"), ResourceUtil.getResourceMnemonic(res, "annotateAction.optionDialog.option.cancel.mnemonic"));
        final AnnotateOptionsPane annotateOptions = new AnnotateOptionsPane(getContext());
        if (!isBypassOptions(evt.getModifiers())) {
            OptionDialog.Option opt = OptionDialog.showOptionDialog("annotate", getContext(), parent, new OptionDialog.Option[] { ok, cancel }, new OutputOptionWrapperPanel(getContext(), annotateOptions, this, "annotate"), res.getString("annotateAction.optionDialog.title"), ok, new OptionDialog.Callback() {

                public boolean canClose(OptionDialog dialog, OptionDialog.Option option) {
                    return annotateOptions.validateTabs();
                }

                public void close(OptionDialog dialog, Option option) {
                    if (option == ok) {
                        runCommand(annotateOptions, parent, sel);
                    }
                }
            }, true, false, null, null, !getContext().getHost().getBooleanProperty(Constants.OPTIONS_DISPLAY_COMMAND_OPTIONS_NON_MODAL, false));
            if (opt != ok) {
                return;
            }
        } else {
            if (!annotateOptions.validateTabs()) return;
            runCommand(annotateOptions, parent, sel);
        }
    }

    private void runCommand(AnnotateOptionsPane annotateOptions, Component parent, CVSFileNode[] sel) {
        annotateOptions.applyTabs();
        Command[] cmd = annotateOptions.getCommandsForSettings();
        CVSCommandHandler.getInstance().runCommandGroup(parent, getContext(), (sel == null) ? getContext().getViewManager().getCWDNode().getFile() : null, cmd, sel, null, false, null, null, this, getEnabledOptionalListeners());
    }
}
