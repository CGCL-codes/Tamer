package de.guhsoft.jinto.core.model;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import de.guhsoft.jinto.core.JIntoCorePlugin;

public class ModifyRowValueOperation extends AbstractResourceBundleOperation {

    private ResourceRow fRow;

    private String fColumnKey;

    private String fOldValue = null;

    private String fNewValue;

    public ModifyRowValueOperation(ResourceBundleModel model, ResourceRow row, String columnKey, String newValue) {
        super(model, JIntoCorePlugin.getResourceString("operations.modifyRowKey.label"));
        this.fRow = row;
        this.fColumnKey = columnKey;
        this.fNewValue = newValue;
    }

    @Override
    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        this.fOldValue = this.fRow.getColumnValue(this.fColumnKey);
        this.fRow.setColumnValue(this.fColumnKey, this.fNewValue);
        return Status.OK_STATUS;
    }

    @Override
    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        this.fRow.setColumnValue(this.fColumnKey, this.fNewValue);
        return Status.OK_STATUS;
    }

    @Override
    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        this.fRow.setColumnValue(this.fColumnKey, this.fOldValue);
        return Status.OK_STATUS;
    }
}
