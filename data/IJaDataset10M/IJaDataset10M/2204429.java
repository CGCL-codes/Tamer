package com.byterefinery.rmbench.operations;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import com.byterefinery.rmbench.EventManager;
import com.byterefinery.rmbench.RMBenchPlugin;
import com.byterefinery.rmbench.dialogs.SchemaDependencyDialog;
import com.byterefinery.rmbench.model.Model;
import com.byterefinery.rmbench.model.diagram.DTable;
import com.byterefinery.rmbench.model.diagram.Diagram;
import com.byterefinery.rmbench.model.schema.ForeignKey;
import com.byterefinery.rmbench.model.schema.Schema;
import com.byterefinery.rmbench.model.schema.Table;

/**
 * undoable operation for delete schema
 * 
 * @author cse
 */
public class DeleteSchemaOperation extends RMBenchOperation {

    private final Model model;

    private final Schema schema;

    private List<Diagram> diagrams;

    private List<DTable> dTables;

    private List<ForeignKey> foreignKeys = new ArrayList<ForeignKey>();

    private Schema newDefaultSchema;

    /** tables which are in the schema */
    private Table[] tables;

    /**
     * The undoable operation to delete a schema
     * @param model the model of the schema
     * @param schema the schema, which will be delted
     * @param diagrams a list of diagrams, which has <i>schema</i> as default schema
     * @param dTables a list of DTable of diagrams which are representaions of tables in the schema
     * @param foreignKeys list of schema dependent foreignkeys
     * @param new defaultschema for diagrams or <i>null</i> if diagram should be deleted
     */
    public DeleteSchemaOperation(Model model, Schema schema) {
        super(Messages.Operation_DeleteSchema);
        this.model = model;
        this.schema = schema;
    }

    public IStatus execute(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        SchemaDependencyDialog dialog = new SchemaDependencyDialog(schema);
        if (dialog.calculateDependencies()) {
            if (dialog.open() != MessageDialog.OK) {
                return Status.CANCEL_STATUS;
            }
        }
        dTables = dialog.getDTables();
        diagrams = dialog.getDiagrams();
        foreignKeys = dialog.getForeignKeys();
        newDefaultSchema = dialog.getNewSchema();
        for (DTable dtable : dTables) {
            dtable.getDiagram().removeTable(dtable);
        }
        for (ForeignKey fk : foreignKeys) {
            fk.abandon();
            RMBenchPlugin.getEventManager().fireForeignKeyDeleted(null, fk);
        }
        for (Diagram diagram : diagrams) {
            if (newDefaultSchema == null) {
                model.removeDiagram(diagram);
                RMBenchPlugin.getEventManager().fireDiagramDeleted(eventSource, model, null, diagram);
            } else {
                diagram.setDefaultSchema(newDefaultSchema);
                RMBenchPlugin.getEventManager().fireDiagramModified(eventSource, EventManager.Properties.SCHEMA, diagram);
            }
        }
        tables = (Table[]) schema.getTables().toArray(new Table[schema.getTables().size()]);
        for (int i = 0; i < tables.length; i++) tables[i].abandon();
        model.removeSchema(schema);
        RMBenchPlugin.getEventManager().fireSchemaDeleted(eventSource, model, schema);
        return Status.OK_STATUS;
    }

    public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        for (DTable dTable : dTables) {
            dTable.getDiagram().removeTable(dTable);
        }
        for (Diagram diagram : diagrams) {
            if (newDefaultSchema == null) {
                model.removeDiagram(diagram);
                RMBenchPlugin.getEventManager().fireDiagramDeleted(eventSource, model, null, diagram);
            } else {
                diagram.setDefaultSchema(newDefaultSchema);
                RMBenchPlugin.getEventManager().fireDiagramModified(eventSource, EventManager.Properties.SCHEMA, diagram);
            }
        }
        for (ForeignKey fk : foreignKeys) {
            fk.abandon();
            RMBenchPlugin.getEventManager().fireForeignKeyDeleted(null, fk);
        }
        for (int i = 0; i < tables.length; i++) tables[i].abandon();
        model.removeSchema(schema);
        RMBenchPlugin.getEventManager().fireSchemaDeleted(this, model, schema);
        return Status.OK_STATUS;
    }

    public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
        model.addSchema(schema);
        for (int i = 0; i < tables.length; i++) tables[i].restore();
        for (DTable dTable : dTables) {
            dTable.getDiagram().addTable(dTable);
        }
        for (Diagram diagram : diagrams) {
            if (newDefaultSchema == null) {
                model.addDiagram(diagram);
                RMBenchPlugin.getEventManager().fireDiagramAdded(eventSource, model, null, diagram);
            } else {
                diagram.setDefaultSchema(schema);
                RMBenchPlugin.getEventManager().fireDiagramModified(eventSource, EventManager.Properties.SCHEMA, diagram);
            }
        }
        for (ForeignKey fk : foreignKeys) {
            fk.restore();
            RMBenchPlugin.getEventManager().fireForeignKeyAdded(null, fk);
        }
        RMBenchPlugin.getEventManager().fireSchemaAdded(this, model, schema);
        return Status.OK_STATUS;
    }
}
