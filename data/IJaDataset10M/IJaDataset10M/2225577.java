package org.datanucleus.store.rdbms.key;

import java.util.ArrayList;
import org.datanucleus.exceptions.NucleusException;
import org.datanucleus.metadata.ForeignKeyAction;
import org.datanucleus.metadata.ForeignKeyMetaData;
import org.datanucleus.store.mapped.DatastoreAdapter;
import org.datanucleus.store.mapped.DatastoreClass;
import org.datanucleus.store.mapped.DatastoreContainerObject;
import org.datanucleus.store.mapped.DatastoreField;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.adapter.RDBMSAdapter;

/**
 * Representation of a foreign key to another table.
 */
public class ForeignKey extends Key {

    /** Enum representing an action on the FK. */
    public static enum FKAction {

        CASCADE("CASCADE"), RESTRICT("RESTRICT"), NULL("SET NULL"), DEFAULT("SET DEFAULT");

        String keyword;

        private FKAction(String word) {
            this.keyword = word;
        }

        public String toString() {
            return keyword;
        }
    }

    private boolean initiallyDeferred;

    private DatastoreClass refTable;

    private DatastoreAdapter dba;

    /** Action to perform on update */
    private FKAction updateAction;

    /** Action to perform on delete */
    private FKAction deleteAction;

    private ArrayList refColumns = new ArrayList();

    /**
     * Constructor.
     * @param initiallyDeferred Whether the constraints are deferred
     */
    public ForeignKey(boolean initiallyDeferred) {
        super(null);
        this.initiallyDeferred = initiallyDeferred;
        this.refTable = null;
        this.dba = null;
    }

    /**
     * Constructor.
     * @param mapping The type mapping for this Foreign-key field
     * @param dba Datastore adapter
     * @param refTable Referred to table
     * @param initiallyDeferred Whether they are deferred
     */
    public ForeignKey(JavaTypeMapping mapping, DatastoreAdapter dba, DatastoreClass refTable, boolean initiallyDeferred) {
        super(mapping.getDatastoreContainer());
        this.initiallyDeferred = initiallyDeferred;
        this.refTable = refTable;
        this.dba = dba;
        if (refTable.getIdMapping() == null) {
            throw new NucleusException("ForeignKey ID mapping is not initilized for " + mapping + ". Table referenced: " + refTable.toString()).setFatal();
        }
        for (int i = 0; i < refTable.getIdMapping().getNumberOfDatastoreMappings(); i++) {
            setDatastoreField(i, mapping.getDatastoreMapping(i).getDatastoreField(), refTable.getIdMapping().getDatastoreMapping(i).getDatastoreField());
        }
    }

    /**
     * Convenience mutator for setting the specification based on MetaData
     * @param fkmd ForeignKey MetaData definition
     */
    public void setForMetaData(ForeignKeyMetaData fkmd) {
        if (fkmd == null) {
            return;
        }
        if (fkmd.getName() != null) {
            setName(fkmd.getName());
        }
        ForeignKeyAction deleteAction = fkmd.getDeleteAction();
        if (deleteAction != null) {
            if (deleteAction.equals(ForeignKeyAction.CASCADE)) {
                setDeleteAction(FKAction.CASCADE);
            } else if (deleteAction.equals(ForeignKeyAction.RESTRICT)) {
                setDeleteAction(FKAction.RESTRICT);
            } else if (deleteAction.equals(ForeignKeyAction.NULL)) {
                setDeleteAction(FKAction.NULL);
            } else if (deleteAction.equals(ForeignKeyAction.DEFAULT)) {
                setDeleteAction(FKAction.DEFAULT);
            }
        }
        ForeignKeyAction updateAction = fkmd.getUpdateAction();
        if (updateAction != null) {
            if (updateAction.equals(ForeignKeyAction.CASCADE)) {
                setUpdateAction(FKAction.CASCADE);
            } else if (updateAction.equals(ForeignKeyAction.RESTRICT)) {
                setUpdateAction(FKAction.RESTRICT);
            } else if (updateAction.equals(ForeignKeyAction.NULL)) {
                setUpdateAction(FKAction.NULL);
            } else if (updateAction.equals(ForeignKeyAction.DEFAULT)) {
                setUpdateAction(FKAction.DEFAULT);
            }
        }
        if (fkmd.isDeferred()) {
            initiallyDeferred = true;
        }
    }

    public DatastoreContainerObject getReferredTable() {
        return refTable;
    }

    /**
     * Accessor for deleteAction.
     * @return Returns the deleteAction.
     */
    public FKAction getDeleteAction() {
        return deleteAction;
    }

    /**
     * Mutator for deleteAction.
     * @param deleteAction The deleteAction to set.
     */
    public void setDeleteAction(FKAction deleteAction) {
        this.deleteAction = deleteAction;
    }

    /**
     * Accessor for updateAction.
     * @return Returns the updateAction.
     */
    public FKAction getUpdateAction() {
        return updateAction;
    }

    /**
     * Mutator for updateAction.
     * @param updateAction The updateAction to set.
     */
    public void setUpdateAction(FKAction updateAction) {
        this.updateAction = updateAction;
    }

    /**
     * Method to add a Column.
     * @param col The column to add
     * @param refCol The column to reference 
     **/
    public void addDatastoreField(DatastoreField col, DatastoreField refCol) {
        setDatastoreField(columns.size(), col, refCol);
    }

    /**
     * Set the datastore field for the specified position <code>seq</code>
     * @param seq the specified position
     * @param col the datastore field
     * @param refCol the foreign (refered) datastore field
     */
    public void setDatastoreField(int seq, DatastoreField col, DatastoreField refCol) {
        if (table == null) {
            table = col.getDatastoreContainerObject();
            refTable = (DatastoreClass) refCol.getDatastoreContainerObject();
            dba = table.getStoreManager().getDatastoreAdapter();
        } else {
            if (!table.equals(col.getDatastoreContainerObject())) {
                throw new NucleusException("Cannot add " + col + " as FK column for " + table).setFatal();
            }
            if (!refTable.equals(refCol.getDatastoreContainerObject())) {
                throw new NucleusException("Cannot add " + refCol + " as referenced FK column for " + refTable).setFatal();
            }
        }
        setMinSize(columns, seq + 1);
        setMinSize(refColumns, seq + 1);
        columns.set(seq, col);
        refColumns.set(seq, refCol);
    }

    /**
     * Hashcode operator.
     * @return The hashcode
     **/
    public int hashCode() {
        return super.hashCode() ^ refColumns.hashCode();
    }

    /**
     * Equality operator.
     * @param obj Object to compare against
     * @return Whether they are equal.
     **/
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof ForeignKey)) {
            return false;
        }
        ForeignKey fk = (ForeignKey) obj;
        if (!refColumns.equals(fk.refColumns)) {
            return false;
        }
        if (!toString().equals(fk.toString())) {
            return false;
        }
        return super.equals(obj);
    }

    /**
     * Stringify method. Generates the foreign key statement ready for use in an SQL call.
     * @return String version of this object.
     **/
    public String toString() {
        StringBuffer s = new StringBuffer("FOREIGN KEY ");
        s.append(getColumnList(columns));
        if (refTable != null) {
            s.append(" REFERENCES ");
            s.append(refTable.toString());
            s.append(" ").append(getColumnList(refColumns));
        }
        if (deleteAction != null) {
            if ((deleteAction == FKAction.CASCADE && dba.supportsOption(RDBMSAdapter.FK_DELETE_ACTION_CASCADE)) || (deleteAction == FKAction.RESTRICT && dba.supportsOption(RDBMSAdapter.FK_DELETE_ACTION_RESTRICT)) || (deleteAction == FKAction.NULL && dba.supportsOption(RDBMSAdapter.FK_DELETE_ACTION_NULL)) || (deleteAction == FKAction.DEFAULT && dba.supportsOption(RDBMSAdapter.FK_DELETE_ACTION_DEFAULT))) {
                s.append(" ON DELETE ").append(deleteAction.toString());
            }
        }
        if (updateAction != null) {
            if ((updateAction == FKAction.CASCADE && dba.supportsOption(RDBMSAdapter.FK_UPDATE_ACTION_CASCADE)) || (updateAction == FKAction.RESTRICT && dba.supportsOption(RDBMSAdapter.FK_UPDATE_ACTION_RESTRICT)) || (updateAction == FKAction.NULL && dba.supportsOption(RDBMSAdapter.FK_UPDATE_ACTION_NULL)) || (updateAction == FKAction.DEFAULT && dba.supportsOption(RDBMSAdapter.FK_UPDATE_ACTION_DEFAULT))) {
                s.append(" ON UPDATE ").append(updateAction.toString());
            }
        }
        if (initiallyDeferred && ((RDBMSAdapter) dba).supportsOption(RDBMSAdapter.DEFERRED_CONSTRAINTS)) {
            s.append(" INITIALLY DEFERRED");
        }
        s.append(" ");
        return s.toString();
    }
}
