package com.byterefinery.rmbench.database.mssql;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import com.byterefinery.rmbench.external.IDataTypeEditorFactory;
import com.byterefinery.rmbench.external.model.IDataType;

/**
 * @author cse
 */
public class SizeMaxTypeEditorFactory implements IDataTypeEditorFactory {

    @Override
    public boolean openEditor(Shell shell, IDataType datatype) {
        if (datatype instanceof SizeMaxDataType) {
            SizeMaxDataTypeDialog dialog = new SizeMaxDataTypeDialog(shell, (SizeMaxDataType) datatype);
            if (dialog.open() == Dialog.OK) return true;
        }
        return false;
    }
}
