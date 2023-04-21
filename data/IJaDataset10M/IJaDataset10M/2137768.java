package org.datanucleus.metadata;

import org.datanucleus.util.StringUtils;

/**
 * Foreign keys in metadata serve two quite different purposes. First, when
 * generating schema, the foreign key element identifies foreign keys to be
 * generated. Second, when using the database, foreign key elements identify
 * foreign keys that are assumed to exist in the database. This is important for
 * the runtime to properly order insert, update, and delete statements to avoid
 * constraint violations. A foreign-key element can be contained by a field,
 * element, key, value, or join element, if all of the columns mapped are to be
 * part of the same foreign key. A foreign-key element can be contained within a
 * class element. In this case, the column elements are mapped elsewhere, and
 * the column elements contained in the foreign-key element have only the column
 * name.
 */
public class ForeignKeyMetaData extends AbstractConstraintMetaData implements ColumnMetaDataContainer {

    /**
     * The unique attribute specifies whether the foreign key constraint is
     * defined to be a unique constraint as well. This is most often used with
     * one-to-one mappings.
     */
    protected boolean unique = false;

    /**
     * The deferred attribute specifies whether the foreign key constraint is
     * defined to be checked only at commit time.
     */
    protected boolean deferred = false;

    /**
     * Foreign keys represent a consistency constraint in the database that must
     * be maintained. The user can specify by the value of the delete-action
     * attribute what happens if the target row of a foreign key is deleted.
     */
    protected ForeignKeyAction deleteAction;

    /**
     * Foreign keys represent a consistency constraint in the database that must
     * be maintained. The user can specify by the update-action attribute what
     * happens if the target row of a foreign key is updated.
     */
    protected ForeignKeyAction updateAction;

    /**
     * Constructor to create a copy of the passed metadata using the provided parent.
     * @param fkmd The metadata to copy
     */
    public ForeignKeyMetaData(ForeignKeyMetaData fkmd) {
        super(fkmd);
        this.name = fkmd.name;
        this.table = fkmd.table;
        this.deferred = fkmd.deferred;
        this.deleteAction = fkmd.deleteAction;
        this.updateAction = fkmd.updateAction;
        this.unique = fkmd.unique;
        if (fkmd.isInitialised()) {
            if (fkmd.getMemberMetaData() != null) {
                for (int i = 0; i < fkmd.getMemberMetaData().length; i++) {
                    if (fkmd.getMemberMetaData()[i] instanceof PropertyMetaData) {
                        addMember(new PropertyMetaData(this, (PropertyMetaData) fkmd.getMemberMetaData()[i]));
                    } else {
                        addMember(new FieldMetaData(this, fkmd.getMemberMetaData()[i]));
                    }
                }
            }
            if (fkmd.getColumnMetaData() != null) {
                for (int i = 0; i < fkmd.getColumnMetaData().length; i++) {
                    addColumn(new ColumnMetaData(fkmd.getColumnMetaData()[i]));
                }
            }
        } else {
            for (int i = 0; i < fkmd.members.size(); i++) {
                if (fkmd.members.get(i) instanceof PropertyMetaData) {
                    addMember(new PropertyMetaData(this, (PropertyMetaData) fkmd.members.get(i)));
                } else {
                    addMember(new FieldMetaData(this, fkmd.members.get(i)));
                }
            }
            for (int i = 0; i < fkmd.columns.size(); i++) {
                addColumn(new ColumnMetaData(fkmd.columns.get(i)));
            }
        }
    }

    /**
     * Default constructor. Set fields using setters, before populate().
     */
    public ForeignKeyMetaData() {
    }

    public final String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = (StringUtils.isWhitespace(name) ? null : name);
    }

    public final String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = (StringUtils.isWhitespace(table) ? null : table);
    }

    public final boolean isDeferred() {
        return deferred;
    }

    public ForeignKeyMetaData setDeferred(boolean deferred) {
        this.deferred = deferred;
        return this;
    }

    public ForeignKeyMetaData setDeferred(String deferred) {
        if (!StringUtils.isWhitespace(deferred)) {
            this.deferred = Boolean.parseBoolean(deferred);
        }
        return this;
    }

    public final ForeignKeyAction getDeleteAction() {
        return deleteAction;
    }

    public void setDeleteAction(ForeignKeyAction deleteAction) {
        this.deleteAction = deleteAction;
    }

    public final boolean isUnique() {
        return unique;
    }

    public ForeignKeyMetaData setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    public ForeignKeyMetaData setUnique(String unique) {
        if (!StringUtils.isWhitespace(unique)) {
            this.deferred = Boolean.parseBoolean(unique);
        }
        return this;
    }

    public final ForeignKeyAction getUpdateAction() {
        return updateAction;
    }

    public ForeignKeyMetaData setUpdateAction(ForeignKeyAction updateAction) {
        this.updateAction = updateAction;
        return this;
    }

    /**
     * Returns a string representation of the object using a prefix
     * This can be used as part of a facility to output a MetaData file. 
     * @param prefix prefix string
     * @param indent indent string
     * @return a string representation of the object.
     */
    public String toString(String prefix, String indent) {
        StringBuffer sb = new StringBuffer();
        sb.append(prefix).append("<foreign-key deferred=\"" + deferred + "\"\n");
        sb.append(prefix).append("       unique=\"" + unique + "\"");
        if (updateAction != null) {
            sb.append("\n").append(prefix).append("       update-action=\"" + updateAction + "\"");
        }
        if (deleteAction != null) {
            sb.append("\n").append(prefix).append("       delete-action=\"" + deleteAction + "\"");
        }
        if (table != null) {
            sb.append("\n").append(prefix).append("       table=\"" + table + "\"");
        }
        if (name != null) {
            sb.append("\n").append(prefix).append("       name=\"" + name + "\"");
        }
        sb.append(">\n");
        if (memberMetaData != null) {
            for (int i = 0; i < memberMetaData.length; i++) {
                sb.append(memberMetaData[i].toString(prefix + indent, indent));
            }
        }
        if (columnMetaData != null) {
            for (int i = 0; i < columnMetaData.length; i++) {
                sb.append(columnMetaData[i].toString(prefix + indent, indent));
            }
        }
        sb.append(super.toString(prefix + indent, indent));
        sb.append(prefix).append("</foreign-key>\n");
        return sb.toString();
    }
}
