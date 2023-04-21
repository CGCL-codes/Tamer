package com.aptana.ide.debug.internal.ui.actions;

import org.eclipse.debug.core.DebugException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import com.aptana.ide.core.IdeLog;
import com.aptana.ide.core.StringUtils;
import com.aptana.ide.debug.core.DetailFormatter;
import com.aptana.ide.debug.core.JSDetailFormattersManager;
import com.aptana.ide.debug.core.model.IJSVariable;
import com.aptana.ide.debug.internal.ui.dialogs.DetailFormatterDialog;
import com.aptana.ide.debug.ui.DebugUiPlugin;

/**
 * @author Max Stepanov
 */
public class EditDetailFormatterAction extends ObjectActionDelegate {

    public void run(IAction action) {
        IStructuredSelection selection = getCurrentSelection();
        if (selection == null || selection.size() != 1) {
            return;
        }
        Object element = selection.getFirstElement();
        String typeName;
        try {
            if (element instanceof IJSVariable) {
                typeName = ((IJSVariable) element).getReferenceTypeName();
            } else {
                return;
            }
        } catch (DebugException e) {
            IdeLog.logError(DebugUiPlugin.getDefault(), StringUtils.EMPTY, e);
            return;
        }
        JSDetailFormattersManager detailFormattersManager = JSDetailFormattersManager.getDefault();
        DetailFormatter detailFormatter = detailFormattersManager.getAssociatedDetailFormatter(typeName);
        if (new DetailFormatterDialog(DebugUiPlugin.getActivePage().getWorkbenchWindow().getShell(), detailFormatter, null, false, true).open() == Window.OK) {
            detailFormattersManager.setAssociatedDetailFormatter(detailFormatter);
            refreshCurrentSelection();
        }
    }
}
