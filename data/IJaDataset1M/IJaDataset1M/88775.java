package org.rubypeople.rdt.internal.ui.callhierarchy;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.PlatformUI;
import org.rubypeople.rdt.internal.ui.IRubyHelpContextIds;
import org.rubypeople.rdt.internal.ui.RubyPluginImages;

class RefreshAction extends Action {

    private CallHierarchyViewPart fPart;

    public RefreshAction(CallHierarchyViewPart part) {
        fPart = part;
        setText(CallHierarchyMessages.RefreshAction_text);
        setToolTipText(CallHierarchyMessages.RefreshAction_tooltip);
        RubyPluginImages.setLocalImageDescriptors(this, "refresh_nav.gif");
        setActionDefinitionId("org.eclipse.ui.file.refresh");
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IRubyHelpContextIds.CALL_HIERARCHY_REFRESH_ACTION);
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        fPart.refresh();
    }
}
