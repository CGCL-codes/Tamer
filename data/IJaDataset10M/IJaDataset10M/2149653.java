package com.continuent.tungsten.replicator.extractor.mysql;

import java.io.IOException;
import com.continuent.tungsten.replicator.extractor.mysql.conversion.LittleEndianConversion;

/**
 * @author <a href="mailto:stephane.giron@continuent.com">Stephane Giron</a>
 * @version 1.0
 */
public class BeginLoadQueryLogEvent extends LogEvent {

    int fileID;

    byte[] fileData;

    private String schemaName;

    public BeginLoadQueryLogEvent(byte[] buffer, int eventLength, FormatDescriptionLogEvent descriptionEvent) throws MySQLExtractException {
        super(buffer, descriptionEvent, MysqlBinlog.BEGIN_LOAD_QUERY_EVENT);
        int commonHeaderLength, postHeaderLength;
        int fixedPartIndex;
        commonHeaderLength = descriptionEvent.commonHeaderLength;
        postHeaderLength = descriptionEvent.postHeaderLength[type - 1];
        if (logger.isDebugEnabled()) logger.debug("event length: " + eventLength + " common header length: " + commonHeaderLength + " post header length: " + postHeaderLength);
        fixedPartIndex = commonHeaderLength;
        try {
            fileID = LittleEndianConversion.convert4BytesToInt(buffer, fixedPartIndex);
            fixedPartIndex += 4;
            int dataLength = buffer.length - fixedPartIndex;
            fileData = new byte[dataLength];
            System.arraycopy(buffer, fixedPartIndex, fileData, 0, dataLength);
        } catch (IOException e) {
            logger.error("Rows log event parsing failed : ", e);
        }
    }

    public int getFileID() {
        return fileID;
    }

    public byte[] getData() {
        return fileData;
    }

    public void setSchemaName(String schema) {
        this.schemaName = schema;
    }

    public String getSchemaName() {
        return schemaName;
    }
}
