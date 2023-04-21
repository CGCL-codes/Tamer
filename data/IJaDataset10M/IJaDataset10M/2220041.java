package com.sync.extractor.mysql;

import com.sync.dbms.OneRowChange;
import com.sync.dbms.RowChangeData;

/**
 * @author <a href="mailto:seppo.jaakola@continuent.com">Seppo Jaakola</a>
 * @author <a href="mailto:stephane.giron@continuent.com">Stephane Giron</a>
 * @version 1.0
 */
public class UpdateRowsLogEvent extends RowsLogEvent {

    public UpdateRowsLogEvent(byte[] buffer, int eventLength, FormatDescriptionLogEvent descriptionEvent, boolean useBytesForString) throws MySQLExtractException {
        super(buffer, eventLength, descriptionEvent, MysqlBinlog.UPDATE_ROWS_EVENT, useBytesForString);
    }

    /**
     * {@inheritDoc}
     * 
     * @see com.continuent.tungsten.replicator.extractor.mysql.RowsLogEvent#processExtractedEvent(com.continuent.tungsten.replicator.dbms.RowChangeData,
     *      com.continuent.tungsten.replicator.extractor.mysql.TableMapLogEvent)
     */
    @Override
    public void processExtractedEvent(RowChangeData rowChanges, TableMapLogEvent map) throws ExtractorException {
        if (map == null) {
            logger.error("Update row event for unknown table");
            throw new MySQLExtractException("Update row event for unknown table");
        }
        OneRowChange oneRowChange = new OneRowChange();
        oneRowChange.setSchemaName(map.getDatabaseName());
        oneRowChange.setTableName(map.getTableName());
        oneRowChange.setTableId(map.getTableId());
        oneRowChange.setAction(RowChangeData.ActionType.UPDATE);
        int rowIndex = 0;
        int bufferIndex = 0;
        while (bufferIndex < bufferSize) {
            int length = 0;
            try {
                length = processExtractedEventRow(oneRowChange, rowIndex, usedColumns, bufferIndex, packedRowsBuffer, map, true);
                if (length == 0) break;
                bufferIndex += length;
                length = processExtractedEventRow(oneRowChange, rowIndex, usedColumnsForUpdate, bufferIndex, packedRowsBuffer, map, false);
            } catch (ExtractorException e) {
                logger.error("Failure while processing extracted update row event", e);
                throw (e);
            }
            rowIndex++;
            if (length == 0) break;
            bufferIndex += length;
        }
        rowChanges.appendOneRowChange(oneRowChange);
    }
}
