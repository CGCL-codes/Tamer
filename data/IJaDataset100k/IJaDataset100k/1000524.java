package com.jacum.cms.rcp.ui.editors.item.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.ui.part.MultiPageEditorPart;
import com.jacum.cms.rcp.ui.Messages;

/**
 * Action for popup menu of the multipage editor tabs.
 * Action close all pages.
 * 
 * @author rich
 */
public class CloseAllAction extends Action {

    private static final String ACTION_TEXT = Messages.getString("CloseAllAction.0");

    private MultiPageEditorPart editor;

    private CTabFolder folder;

    /**
     * Constructor
     * 
     * @param editor multi page editor
     * @param folder tab folder which contain tab controls 
     */
    public CloseAllAction(MultiPageEditorPart editor, CTabFolder folder) {
        super();
        this.editor = editor;
        this.folder = folder;
    }

    public String getText() {
        return ACTION_TEXT;
    }

    public void run() {
        for (int i = folder.getItemCount() - 1; i > 0; i--) {
            editor.removePage(i);
        }
    }
}
