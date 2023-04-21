package org.avaje.ebean.server.deploy.meta;

import java.util.ArrayList;
import org.avaje.ebean.server.deploy.BeanCascadeInfo;
import org.avaje.ebean.server.deploy.TableJoin;
import org.avaje.ebean.server.lib.sql.Fkey;
import org.avaje.ebean.server.lib.sql.FkeyColumn;
import org.avaje.ebean.util.Message;

/**
 * Represents a join to another table during deployment phase
 */
public class DeployTableJoin {

    /**
     * Flag set when the imported key maps to the primary key.
     * This occurs for intersection tables (ManyToMany).
     */
    boolean importedPrimaryKey;

    /**
     * The joined table.
     */
    String table;

    /**
     * The table alias for the joined table.
     */
    String foreignTableAlias;

    /**
     * The table alias for local base table.
     */
    String localTableAlias;

    /**
     * The type of join. LEFT OUTER etc.
     */
    String type = TableJoin.JOIN;

    /**
     * The list of properties mapped to this joined table.
     */
    ArrayList<DeployBeanProperty> properties = new ArrayList<DeployBeanProperty>();

    /**
     * The list of join column pairs. Used to generate the on clause.
     */
    ArrayList<DeployTableJoinColumn> columns = new ArrayList<DeployTableJoinColumn>();

    /**
     * The persist cascade info.
     */
    BeanCascadeInfo cascadeInfo = new BeanCascadeInfo();

    /**
     * Create a TableJoin.
     */
    public DeployTableJoin() {
    }

    public String toString() {
        return type + " " + table + " " + columns;
    }

    /**
     * Return true if the imported foreign key maps to the primary key.
     */
    public boolean isImportedPrimaryKey() {
        return importedPrimaryKey;
    }

    /**
     * Flag set when the imported key maps to the primary key.
     * This occurs for intersection tables (ManyToMany).
     */
    public void setImportedPrimaryKey(boolean importedPrimaryKey) {
        this.importedPrimaryKey = importedPrimaryKey;
    }

    /**
     * Return true if the JoinOnPair have been set.
     */
    public boolean hasJoinColumns() {
        return columns.size() > 0;
    }

    /**
     * Return the persist info.
     */
    public BeanCascadeInfo getCascadeInfo() {
        return cascadeInfo;
    }

    public void addColumns(Fkey fkey) {
        boolean exported = fkey.isExported();
        if (fkey.isPrimaryKey()) {
            setImportedPrimaryKey(true);
        }
        FkeyColumn[] cols = fkey.columns();
        for (int i = 0; i < cols.length; i++) {
            FkeyColumn col = cols[i];
            DeployTableJoinColumn joinColumn;
            if (exported) {
                joinColumn = new DeployTableJoinColumn(col.getPkColumnName(), col.getFkColumnName());
            } else {
                joinColumn = new DeployTableJoinColumn(col.getFkColumnName(), col.getPkColumnName());
            }
            addTableJoinColumn(joinColumn);
        }
        if (!exported) {
            if (fkey.isImportNullable()) {
                setType(TableJoin.LEFT_OUTER);
            }
        }
    }

    /**
     * Add a join pair
     */
    public void addTableJoinColumn(DeployTableJoinColumn pair) {
        columns.add(pair);
    }

    /**
     * Return the join columns.
     */
    public DeployTableJoinColumn[] columns() {
        return (DeployTableJoinColumn[]) columns.toArray(new DeployTableJoinColumn[columns.size()]);
    }

    /**
     * For secondary table joins returns the properties mapped to that table.
     */
    public DeployBeanProperty[] properties() {
        return (DeployBeanProperty[]) properties.toArray(new DeployBeanProperty[properties.size()]);
    }

    /**
     * Add a property for this tableJoin.
     */
    public void addProperty(DeployBeanProperty prop) {
        prop.setDbTableAlias(getForeignTableAlias());
        prop.setSecondaryTable();
        properties.add(prop);
    }

    /**
     * Return the table alias used by this join.
     */
    public String getForeignTableAlias() {
        return foreignTableAlias;
    }

    /**
     * Set the table alias used by this join.
     */
    public void setForeignTableAlias(String alias) {
        this.foreignTableAlias = alias;
    }

    /**
     * Return the local base table alias.
     */
    public String getLocalTableAlias() {
        return localTableAlias;
    }

    /**
     * set the local base table alias.
     */
    public void setLocalTableAlias(String localTableAlias) {
        this.localTableAlias = localTableAlias;
    }

    /**
     * Return the joined table name.
     */
    public String getTable() {
        return table;
    }

    /**
     * set the joined table name.
     */
    public void setTable(String table) {
        this.table = table;
    }

    /**
     * Return the type of join. LEFT OUTER JOIN etc.
     */
    public String getType() {
        return type;
    }

    /**
     * Return true if this join is a left outer join.
     */
    public boolean isOuterJoin() {
        return type.equals(TableJoin.LEFT_OUTER);
    }

    /**
     * Set the type of join.
     */
    public void setType(String joinType) {
        joinType = joinType.toUpperCase();
        if (joinType.equalsIgnoreCase(TableJoin.JOIN)) {
            type = TableJoin.JOIN;
        } else if (joinType.indexOf("LEFT") > -1) {
            type = TableJoin.LEFT_OUTER;
        } else if (joinType.indexOf("OUTER") > -1) {
            type = TableJoin.LEFT_OUTER;
        } else if (joinType.indexOf("INNER") > -1) {
            type = TableJoin.JOIN;
        } else {
            throw new RuntimeException(Message.msg("join.type.unknown", joinType));
        }
    }

    public DeployTableJoin createInverse() {
        DeployTableJoin inverse = new DeployTableJoin();
        inverse.setTable("ERROR:CHANGE THE TABLE");
        inverse.setForeignTableAlias(localTableAlias);
        inverse.setLocalTableAlias(foreignTableAlias);
        inverse.setType(type);
        DeployTableJoinColumn[] cols = columns();
        for (int i = 0; i < cols.length; i++) {
            inverse.addTableJoinColumn(cols[i].createInverse());
        }
        return inverse;
    }
}
