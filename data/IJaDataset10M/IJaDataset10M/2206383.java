package org.databene.jdbacl.identity.mem;

import org.databene.commons.ArrayFormat;
import org.databene.commons.bean.HashCodeBuilder;
import org.databene.commons.bean.ObjectOrArray;

/**
 * Global technical identifier for a database table rows 
 * which aggregates database id, table name and primary key.<br/><br/>
 * Created: 31.08.2010 16:19:41
 * @since 1.0
 * @author Volker Bergmann
 */
public class GlobalRowId {

    private String schemaId;

    private String tableName;

    private ObjectOrArray pk;

    public GlobalRowId(String schemaId, String tableName, Object pk) {
        this.schemaId = schemaId;
        this.tableName = tableName;
        this.pk = (pk instanceof ObjectOrArray ? (ObjectOrArray) pk : new ObjectOrArray(pk));
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.hashCode(schemaId, tableName, pk);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        GlobalRowId that = (GlobalRowId) obj;
        return (this.schemaId.equals(that.schemaId) && this.tableName.equals(that.tableName) && this.pk.equals(that.pk));
    }

    @Override
    public String toString() {
        return schemaId + '.' + tableName + '#' + renderPK(pk);
    }

    private String renderPK(Object pk) {
        if (pk.getClass().isArray()) return ArrayFormat.format((Object[]) pk); else return String.valueOf(pk);
    }
}
