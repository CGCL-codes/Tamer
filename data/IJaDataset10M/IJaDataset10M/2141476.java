package com.byterefinery.rmbench.util.dbimport;

import java.io.IOException;
import java.text.MessageFormat;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.ui.console.MessageConsoleStream;
import com.byterefinery.rmbench.RMBenchPlugin;
import com.byterefinery.rmbench.external.IDatabaseInfo;
import com.byterefinery.rmbench.external.model.IDataType;
import com.byterefinery.rmbench.model.Model;
import com.byterefinery.rmbench.model.dbimport.DBColumn;
import com.byterefinery.rmbench.model.dbimport.DBForeignKey;
import com.byterefinery.rmbench.model.dbimport.DBIndex;
import com.byterefinery.rmbench.model.dbimport.DBTable;
import com.byterefinery.rmbench.model.schema.Column;
import com.byterefinery.rmbench.model.schema.ForeignKey;
import com.byterefinery.rmbench.model.schema.Index;
import com.byterefinery.rmbench.model.schema.PrimaryKey;
import com.byterefinery.rmbench.model.schema.Schema;
import com.byterefinery.rmbench.model.schema.Table;

/**
 * a helper class used to import database metadata into a RMBench model. Informational
 * messages generated during import will be written to a console stream.
 * 
 * @author cse
 */
public class ImportHelper {

    private MessageConsoleStream errorStream, infoStream;

    /**
     * import table metadata into a given schema, creating the appropriate model representations
     * 
     * @param dbtable the table metadata to import
     * @param model the target schema
     * @return the new model table object that corresponds to this object
     */
    public Table importTable(DBTable dbtable, Schema targetSchema) {
        final Table newTable = new Table(targetSchema, dbtable.getName());
        for (DBColumn column : dbtable.getColumns()) {
            importColumn(column, newTable);
        }
        if (dbtable.getPrimaryKey() != null) {
            new PrimaryKey(dbtable.getPrimaryKey().name, dbtable.getPrimaryKey().getColumns(), newTable);
        }
        for (DBIndex dbIndex : dbtable.getIndexes()) {
            Column columns[] = new Column[dbIndex.getColumns().length];
            String names[] = dbIndex.getColumnNames();
            for (int i = 0; i < names.length; i++) {
                columns[i] = newTable.getColumn(names[i]);
            }
            new Index(dbIndex.getIIndex().getName(), columns, newTable, dbIndex.getIIndex().isUnique());
        }
        return newTable;
    }

    /**
     * import column metadata into a given table, creating the appropriate model representations
     * 
     * @param dbcolumn the column metadata
     * @param targetTable the target table
     * @return the new model column object that corresponds to this object
     */
    private Column importColumn(DBColumn dbcolumn, Table targetTable) {
        IDataType dataType = null;
        if (targetTable.getDatabaseInfo() != dbcolumn.getDatabaseInfo()) {
            IDatabaseInfo.TypeConverter typeConverter = targetTable.getDatabaseInfo().getTypeConverter(dbcolumn.dataType, dbcolumn.getDatabaseInfo());
            if (typeConverter != null) dataType = typeConverter.convert(dbcolumn.dataType, dbcolumn.getDatabaseInfo());
            if (dataType == null) {
                String message = MessageFormat.format(ImportMessages.importColumn_errorConvertType, new Object[] { dbcolumn.dataType.getPrimaryName(), dbcolumn.name, targetTable.getSchema().getName(), targetTable.getName() });
                RMBenchPlugin.logError(message);
                dataType = targetTable.getDatabaseInfo().getDefaultDataType();
            }
        } else {
            dataType = dbcolumn.dataType;
        }
        String defaultValue = targetTable.getDatabaseInfo().impliesDefault(dataType) ? null : dbcolumn.defaultValue;
        return new Column(targetTable, dbcolumn.name, dataType, dbcolumn.nullable, defaultValue, dbcolumn.comment);
    }

    /**
     * import foreign key data into a given schema, creating the appropriate model representations.
     * Creation will fail silently if the primary key table is not present in the target schema.  
     * 
     * @param dbkey the foreign key metadata
     * @param model the target model which should contain the parent and target table
     * @return the new model foreign key object that corresponds to this object, or <code>null</code>
     * if the foreign key could not be created
     */
    public ForeignKey importForeignKey(DBForeignKey dbkey, Model model) {
        Table modelOwnerTable = (model.getSchema(dbkey.table.getSchemaName())).getTable(dbkey.table.getName());
        Table modelTargetTable = (model.getSchema(dbkey.targetSchema)).getTable(dbkey.targetTable);
        return new ForeignKey(dbkey.name, dbkey.getColumns(), modelOwnerTable, modelTargetTable, dbkey.deleteRule, dbkey.updateRule);
    }

    public MessageConsoleStream getErrorStream() {
        if (errorStream == null) {
            errorStream = new MessageConsoleStream(RMBenchPlugin.getDefault().getMessageConsole());
            errorStream.setActivateOnWrite(true);
            errorStream.setColor(ColorConstants.red);
        }
        return errorStream;
    }

    public MessageConsoleStream getInfoStream() {
        if (infoStream == null) {
            infoStream = new MessageConsoleStream(RMBenchPlugin.getDefault().getMessageConsole());
            infoStream.setActivateOnWrite(true);
            infoStream.setColor(ColorConstants.red);
        }
        return infoStream;
    }

    /**
     * close the underlying console streams
     */
    public void close() {
        try {
            if (errorStream != null) errorStream.close();
            if (infoStream != null) infoStream.close();
        } catch (IOException e) {
            RMBenchPlugin.logError(e);
        }
    }
}
