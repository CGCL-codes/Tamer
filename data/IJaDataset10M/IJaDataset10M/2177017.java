package org.eclipse.ui.internal.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.ui.IWorkbenchPreferenceConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.internal.IWorkbenchGraphicConstants;
import org.eclipse.ui.internal.IWorkbenchHelpContextIds;
import org.eclipse.ui.internal.WorkbenchImages;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.internal.util.PrefUtil;

/**
 * Action to open the help search.
 * 
 * @since 3.1
 */
public class HelpSearchAction extends Action implements IWorkbenchAction {

    /**
     * The workbench window; or <code>null</code> if this
     * action has been <code>dispose</code>d.
     */
    private IWorkbenchWindow workbenchWindow;

    /**
     * Zero-arg constructor to allow cheat sheets to reuse this action.
     */
    public HelpSearchAction() {
        this(PlatformUI.getWorkbench().getActiveWorkbenchWindow());
    }

    /**
     * Constructor for use by ActionFactory.
     * 
     * @param window the window
     */
    public HelpSearchAction(IWorkbenchWindow window) {
        if (window == null) {
            throw new IllegalArgumentException();
        }
        this.workbenchWindow = window;
        setActionDefinitionId("org.eclipse.ui.help.helpSearch");
        String overrideText = PrefUtil.getAPIPreferenceStore().getString(IWorkbenchPreferenceConstants.HELP_SEARCH_ACTION_TEXT);
        if ("".equals(overrideText)) {
            setText(WorkbenchMessages.HelpSearchAction_text);
            setToolTipText(WorkbenchMessages.HelpSearchAction_toolTip);
        } else {
            setText(overrideText);
            setToolTipText(Action.removeMnemonics(overrideText));
        }
        setImageDescriptor(WorkbenchImages.getImageDescriptor(IWorkbenchGraphicConstants.IMG_ETOOL_HELP_SEARCH));
        window.getWorkbench().getHelpSystem().setHelp(this, IWorkbenchHelpContextIds.HELP_SEARCH_ACTION);
    }

    public void run() {
        if (workbenchWindow == null) {
            return;
        }
        BusyIndicator.showWhile(null, new Runnable() {

            public void run() {
                workbenchWindow.getWorkbench().getHelpSystem().displaySearch();
            }
        });
    }

    public void dispose() {
        workbenchWindow = null;
    }
}
