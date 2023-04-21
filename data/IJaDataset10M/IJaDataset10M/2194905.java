package org.objectstyle.cayenne.map;

import java.util.Iterator;
import org.objectstyle.cayenne.dba.TypesMapping;
import org.objectstyle.cayenne.map.event.AttributeEvent;
import org.objectstyle.cayenne.map.event.DbAttributeListener;
import org.objectstyle.cayenne.util.Util;
import org.objectstyle.cayenne.util.XMLEncoder;

/**
 * A DbAttribute defines a descriptor for a single database table column.
 * 
 * @author Misha Shengaout
 * @author Andrei Adamchik
 */
public class DbAttribute extends Attribute {

    /**
     * Defines JDBC type of the column.
     */
    protected int type = TypesMapping.NOT_DEFINED;

    /**
     * Defines whether the attribute allows nulls.
     */
    protected boolean mandatory;

    /**
     * Defines whether the attribute is a part of the table primary key.
     */
    protected boolean primaryKey;

    /**
     * Defines whether this column value is generated by the database. Other terms for
     * such columns are "auto-increment" or "identity".
     * 
     * @since 1.2
     */
    protected boolean generated;

    protected int maxLength = -1;

    protected int precision = -1;

    public DbAttribute() {
        super();
    }

    public DbAttribute(String name) {
        super(name);
    }

    public DbAttribute(String name, int type, DbEntity entity) {
        this.setName(name);
        this.setType(type);
        this.setEntity(entity);
    }

    /**
     * Prints itself as XML to the provided XMLEncoder.
     * 
     * @since 1.1
     */
    public void encodeAsXML(XMLEncoder encoder) {
        encoder.print("<db-attribute name=\"");
        encoder.print(Util.encodeXmlAttribute(getName()));
        encoder.print('\"');
        String type = TypesMapping.getSqlNameByType(getType());
        if (type != null) {
            encoder.print(" type=\"" + type + '\"');
        }
        if (isPrimaryKey()) {
            encoder.print(" isPrimaryKey=\"true\"");
            if (isGenerated()) {
                encoder.print(" isGenerated=\"true\"");
            }
        }
        if (isMandatory()) {
            encoder.print(" isMandatory=\"true\"");
        }
        if (getMaxLength() > 0) {
            encoder.print(" length=\"");
            encoder.print(getMaxLength());
            encoder.print('\"');
        }
        if (getPrecision() > 0) {
            encoder.print(" precision=\"");
            encoder.print(getPrecision());
            encoder.print('\"');
        }
        encoder.println("/>");
    }

    public String getAliasedName(String alias) {
        return (alias != null) ? alias + '.' + this.getName() : this.getName();
    }

    /**
     * Returns the SQL type of the column.
     * 
     * @see java.sql.Types
     */
    public int getType() {
        return type;
    }

    /**
     * Sets the SQL type for the column.
     * 
     * @see java.sql.Types
     */
    public void setType(int type) {
        this.type = type;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * Returns <code>true</code> if the DB column represented by this attribute is a
     * foreign key, referencing another table.
     * 
     * @since 1.1
     */
    public boolean isForeignKey() {
        String name = getName();
        if (name == null) {
            return false;
        }
        Iterator relationships = getEntity().getRelationships().iterator();
        while (relationships.hasNext()) {
            DbRelationship relationship = (DbRelationship) relationships.next();
            Iterator joins = relationship.getJoins().iterator();
            while (joins.hasNext()) {
                DbJoin join = (DbJoin) joins.next();
                if (name.equals(join.getSourceName())) {
                    DbAttribute target = join.getTarget();
                    if (target != null && target.isPrimaryKey()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Updates attribute "primaryKey" property.
     */
    public void setPrimaryKey(boolean primaryKey) {
        if (this.primaryKey != primaryKey) {
            this.primaryKey = primaryKey;
            Entity e = this.getEntity();
            if (e instanceof DbAttributeListener) {
                ((DbAttributeListener) e).dbAttributeChanged(new AttributeEvent(this, this, e));
            }
        }
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    /**
     * Returns the length of database column described by this attribute.
     */
    public int getMaxLength() {
        return maxLength;
    }

    /**
     * Sets the length of character or binary type or max num of digits for DECIMAL.
     */
    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * Returns the number of digits after period for DECIMAL.
     */
    public int getPrecision() {
        return precision;
    }

    /** Sets the number of digits after period for DECIMAL. */
    public void setPrecision(int precision) {
        this.precision = precision;
    }

    /**
     * Returns true if this column value is generated by the database. Other terms for
     * such columns are "auto-increment" or "identity".
     * 
     * @since 1.2
     */
    public boolean isGenerated() {
        return generated;
    }

    /**
     * Updates attribute "generated" property.
     * 
     * @since 1.2
     */
    public void setGenerated(boolean generated) {
        if (this.generated != generated) {
            this.generated = generated;
            Entity e = this.getEntity();
            if (e instanceof DbAttributeListener) {
                ((DbAttributeListener) e).dbAttributeChanged(new AttributeEvent(this, this, e));
            }
        }
    }
}