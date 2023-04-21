package org.datanucleus.store.rdbms.mapping.oracle;

import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.store.ObjectProvider;
import org.datanucleus.store.mapped.mapping.MappingCallbacks;
import org.datanucleus.store.mapped.mapping.StringMapping;
import org.datanucleus.store.rdbms.adapter.RDBMSAdapter;

/**
 * Mapping for a String type for Oracle when stored in a BLOB or CLOB column.
 */
public class OracleStringMapping extends StringMapping implements MappingCallbacks {

    /**
     * Retrieve the empty BLOB/CLOB locator created by the insert statement
     * and write out the current BLOB/CLOB field value to the Oracle BLOB/CLOB object
     * @param op The StateManager owner of this field
     */
    public void insertPostProcessing(ObjectProvider op) {
        String value = (String) op.provideField(mmd.getAbsoluteFieldNumber());
        op.isLoaded(mmd.getAbsoluteFieldNumber());
        if (value == null) {
            value = "";
        } else if (value.length() == 0) {
            if (storeMgr.getBooleanProperty("datanucleus.rdbms.persistEmptyStringAsNull")) {
                value = "";
            } else {
                value = ((RDBMSAdapter) storeMgr.getDatastoreAdapter()).getSurrogateForEmptyStrings();
            }
        }
        if (mmd.getColumnMetaData()[0].getJdbcType().toUpperCase().equals("BLOB")) {
            OracleBlobRDBMSMapping.updateBlobColumn(op, getDatastoreContainer(), getDatastoreMapping(0), value.getBytes());
        } else if (mmd.getColumnMetaData()[0].getJdbcType().toUpperCase().equals("CLOB")) {
            OracleClobRDBMSMapping.updateClobColumn(op, getDatastoreContainer(), getDatastoreMapping(0), value);
        } else {
            throw new NucleusException("AssertionError: Only JDBC types BLOB and CLOB are allowed!");
        }
    }

    public void postInsert(ObjectProvider op) {
    }

    public void postFetch(ObjectProvider op) {
    }

    public void postUpdate(ObjectProvider op) {
        insertPostProcessing(op);
    }

    public void preDelete(ObjectProvider op) {
    }
}
