package com.beetop.ui.views.busyws;

import org.eclipse.jface.action.Action;

/**
 * The RefreshEditorListAction refreshes the contents of
 * an <code>EditorList</code> view.
 */
public class RefreshEditorListAction extends Action {

    private BusyEditorView view;

    public RefreshEditorListAction(BusyEditorView viewPart) {
        setDescription(BusyWSResource.getString("RefreshEditorListAction.Refresh_editor_list_1"));
        setText(BusyWSResource.getString("RefreshEditorListAction.Refresh_2"));
        view = viewPart;
    }

    /**
	 * @see IAction#run()
	 */
    public void run() {
        view.refreshContents();
    }
}
