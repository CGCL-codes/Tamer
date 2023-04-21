package com.byterefinery.rmbench.operations;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import com.byterefinery.rmbench.EventManager;
import com.byterefinery.rmbench.RMBenchPlugin;
import com.byterefinery.rmbench.model.schema.Column;
import com.byterefinery.rmbench.model.schema.ForeignKey;

/**
 * Operation for changing the scale of a column
 * @author cse
 */
public class ColumnScaleOperation extends ModifyColumnOperation {

    private final int oldScale, newScale;

    public ColumnScaleOperation(Column column, int newScale) {
        super(Messages.Operation_ModifyColumn, column);
        this.oldScale = column.getScale();
        this.newScale = newScale;
    }

    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        column.setScale(newScale);
        updateForeignKeys(newScale);
        RMBenchPlugin.getEventManager().fireColumnModified(column.getTable(), EventManager.Properties.COLUMN_SCALE, column);
        fireForeignKeyEvents(EventManager.Properties.COLUMN_SCALE);
        fireReferencesEvents(EventManager.Properties.COLUMN_SCALE);
        return Status.OK_STATUS;
    }

    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        column.setScale(oldScale);
        updateForeignKeys(oldScale);
        RMBenchPlugin.getEventManager().fireColumnModified(column.getTable(), EventManager.Properties.COLUMN_SCALE, column);
        fireForeignKeyEvents(EventManager.Properties.COLUMN_SCALE);
        fireReferencesEvents(EventManager.Properties.COLUMN_SCALE);
        return Status.OK_STATUS;
    }

    private void updateForeignKeys(int scale) {
        if (column.belongsToPrimaryKey()) {
            int keyIndex = column.getTable().getPrimaryKey().getIndex(column);
            for (ForeignKey foreignKey : column.getTable().getReferences()) {
                foreignKey.getColumn(keyIndex).setScale(scale);
            }
        }
    }
}
