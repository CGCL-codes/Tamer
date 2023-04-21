package com.tensegrity.palorules.source;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.widgets.Display;
import com.tensegrity.palorules.IRuleEditor;
import com.tensegrity.palorules.IRuleSourcePartEditor;
import com.tensegrity.palorules.RuleEditorMessages;

/**
 * Utility functions for drag and drop.
 * 
 * @author AndreasEbbert
 * @version $Id: DNDUtil.java,v 1.2 2008/03/26 12:29:24 AndreasEbbert Exp $
 */
class DNDUtil {

    private DNDUtil() {
    }

    static final IContentItem findMovedItem(IRuleSourcePartEditor editor, DropTargetEvent event) {
        final String dd = (String) event.data;
        if (dd == null) return null;
        final String hash = dd.substring(IContentItem.DRAG_DATA_KEY_PREFIX.length());
        final int hashCode = Integer.parseInt(hash);
        IContentItem[] items = editor.getTopLevelContainer().getContentItems();
        for (int i = 0; i < items.length; i++) {
            if (items[i].hashCode() == hashCode) return items[i];
        }
        return null;
    }

    static final String getFunctionID(DropTargetEvent dte) {
        String dd = (String) TextTransfer.getInstance().nativeToJava(dte.currentDataType);
        if (dd == null) return null;
        return getFunctionID(dd);
    }

    static final String getFunctionID(String dndKey) {
        if (dndKey.startsWith(IRuleEditor.DD_PREFIX)) return dndKey.substring(IRuleEditor.DD_PREFIX.length());
        return null;
    }

    static final boolean isFunctionDrag(DropTargetEvent dte) {
        String dd = (String) TextTransfer.getInstance().nativeToJava(dte.currentDataType);
        if (dd == null) return false;
        if (dd.startsWith(IRuleEditor.DD_PREFIX)) return true;
        return false;
    }

    static final boolean isDragFromFunctionTree(String dd) {
        if (dd == null) return false;
        return dd.startsWith(IRuleEditor.DD_PREFIX);
    }

    static final boolean isMove(DropTargetEvent e) {
        String dd = (String) TextTransfer.getInstance().nativeToJava(e.currentDataType);
        if (dd == null) return false;
        if (dd.startsWith(IContentItem.DRAG_DATA_KEY_PREFIX)) return true;
        return false;
    }

    static final boolean isMove(String dd) {
        if (dd == null) return false;
        if (dd.startsWith(IContentItem.DRAG_DATA_KEY_PREFIX)) return true;
        return false;
    }

    static final boolean editorHasActiveRuleTarget(RuleSourcePartEditor editor) {
        if (!editor.getRuleEditor().hasActiveTarget()) {
            MessageDialog.openInformation(Display.getCurrent().getActiveShell(), RuleEditorMessages.getString("DNDInterpreter.Dlg.No_Target.Title"), RuleEditorMessages.getString("DNDInterpreter.Dlg.No_Target.Msg"));
            return false;
        }
        return true;
    }
}
