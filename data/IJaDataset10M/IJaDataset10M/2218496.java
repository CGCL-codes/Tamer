package org.rubypeople.rdt.internal.ui.callhierarchy;

import org.eclipse.ui.PlatformUI;
import org.rubypeople.rdt.core.search.IRubySearchScope;
import org.rubypeople.rdt.core.search.SearchEngine;
import org.rubypeople.rdt.internal.ui.IRubyHelpContextIds;
import org.rubypeople.rdt.internal.ui.search.RubySearchScopeFactory;

class SearchScopeWorkspaceAction extends SearchScopeAction {

    public SearchScopeWorkspaceAction(SearchScopeActionGroup group) {
        super(group, CallHierarchyMessages.SearchScopeActionGroup_workspace_text);
        setToolTipText(CallHierarchyMessages.SearchScopeActionGroup_workspace_tooltip);
        PlatformUI.getWorkbench().getHelpSystem().setHelp(this, IRubyHelpContextIds.CALL_HIERARCHY_SEARCH_SCOPE_ACTION);
    }

    public IRubySearchScope getSearchScope() {
        return SearchEngine.createWorkspaceScope();
    }

    public int getSearchScopeType() {
        return SearchScopeActionGroup.SEARCH_SCOPE_TYPE_WORKSPACE;
    }

    public String getFullDescription() {
        RubySearchScopeFactory factory = RubySearchScopeFactory.getInstance();
        return factory.getWorkspaceScopeDescription(true);
    }
}
