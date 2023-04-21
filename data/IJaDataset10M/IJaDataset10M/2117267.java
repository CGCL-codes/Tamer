package uk.org.ogsadai.views.schema;

import uk.org.ogsadai.converters.databaseschema.TableMetaData;

/**
 * Callback interface for a class that will receive a some table metadata.
 *
 * @author The OGSA-DAI Project Team
 */
public interface TableMetaDataReceiver {

    /**
     * Gives the table metadata object to the callback class.
     * 
     * @param tableMetaData
     */
    public void setTableMetaData(TableMetaData tableMetaData);
}
