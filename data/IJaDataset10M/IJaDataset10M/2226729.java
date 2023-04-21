package com.byterefinery.rmbench.operations;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import com.byterefinery.rmbench.RMBenchPlugin;
import com.byterefinery.rmbench.model.schema.Column;
import com.byterefinery.rmbench.model.schema.Index;
import com.byterefinery.rmbench.model.schema.Table;

/**
 * an undoable operation for adding a new index
 * 
 * @author cse
 */
public class AddIndexOperation extends RMBenchOperation {

    private final Table table;

    private final String name;

    private final Column[] columns;

    private final boolean unique;

    private final boolean[] ascvalues;

    private Index index;

    public AddIndexOperation(String name, Column[] columns, Table table, boolean unique, boolean[] ascvalues) {
        super(Messages.Operation_AddIndex);
        this.table = table;
        this.name = name;
        this.columns = columns;
        this.unique = unique;
        this.ascvalues = ascvalues;
    }

    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        index = new Index(name, columns, table, unique, ascvalues);
        RMBenchPlugin.getEventManager().fireIndexAdded(table, index);
        return Status.OK_STATUS;
    }

    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        index.restore();
        RMBenchPlugin.getEventManager().fireIndexAdded(table, index);
        return Status.OK_STATUS;
    }

    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        index.abandon();
        RMBenchPlugin.getEventManager().fireIndexDeleted(table, index);
        return Status.OK_STATUS;
    }
}
