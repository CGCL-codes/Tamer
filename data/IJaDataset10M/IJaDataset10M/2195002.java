package com.daffodilwoods.daffodildb.server.sql99.ddl.descriptors;

import com.daffodilwoods.daffodildb.server.datadictionarysystem.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.serversystem.dmlvalidation.constraintsystem.*;
import com.daffodilwoods.daffodildb.server.sql99.dql.iterator.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.booleanvalueexpression.*;
import com.daffodilwoods.database.general.*;
import com.daffodilwoods.database.resource.*;

public class ColumnPrivilegeDescriptor extends PrivilegeDescriptor {

    public String column_name;

    public String privilege_type;

    public ColumnPrivilegeDescriptor() throws DException {
    }

    public ColumnPrivilegeDescriptor(ColumnPrivilegeDescriptor cpd) throws DException {
        object_catalog = (cpd.object_catalog == null) ? null : cpd.object_catalog.toString();
        object_name = (cpd.object_name == null) ? null : cpd.object_name.toString();
        object_schema = (cpd.object_schema == null) ? null : cpd.object_schema.toString();
        privilege_type = (cpd.privilege_type == null) ? null : cpd.privilege_type;
        column_name = (cpd.column_name == null) ? null : cpd.column_name;
        grantee = (cpd.grantee == null) ? null : cpd.grantee.toString();
        grantor = (cpd.grantor == null) ? null : cpd.grantor.toString();
        is_grantable = (cpd.is_grantable == null) ? null : cpd.is_grantable.toString();
    }

    public int hashCode() {
        return privilege_type.hashCode() * 3 + column_name.toUpperCase().hashCode() * 5 + getGrantee().toUpperCase().hashCode() * 7 + getGrantor().toUpperCase().hashCode() * 9 + object_catalog.toUpperCase().hashCode() * 11 + object_schema.toUpperCase().hashCode() * 13 + object_name.toUpperCase().hashCode() * 17;
    }

    public boolean equals(Object temp) {
        if (!(temp instanceof ColumnPrivilegeDescriptor)) {
            return false;
        }
        ColumnPrivilegeDescriptor pd = (ColumnPrivilegeDescriptor) temp;
        return (((privilege_type == null) && (pd.privilege_type == null)) || privilege_type.equalsIgnoreCase(pd.privilege_type)) && (((column_name == null) && (pd.column_name == null)) || column_name.equalsIgnoreCase(pd.column_name)) && (((getGrantee() == null) && (pd.getGrantee() == null)) || getGrantee().equalsIgnoreCase(pd.getGrantee())) && (((getGrantor() == null) && (pd.getGrantor() == null)) || getGrantor().equalsIgnoreCase(pd.getGrantor())) && (((object_catalog == null) && (pd.object_catalog == null)) || object_catalog.equalsIgnoreCase(pd.object_catalog)) && (((object_schema == null) && (pd.object_schema == null)) || object_schema.equalsIgnoreCase(pd.object_schema)) && (((object_name == null) && (pd.object_name == null)) || object_name.equalsIgnoreCase(pd.object_name)) && (((is_grantable == null) && (pd.is_grantable == null)) || is_grantable.equalsIgnoreCase(pd.is_grantable));
    }

    public void load(_ServerSession serverSession) throws DException {
        DataDictionary dd = (DataDictionary) serverSession.getDataDictionary();
        _SelectQueryIterator iter = (_SelectQueryIterator) dd.getPreparedStatementGetter().getColumnPrivilegesTableExecuter().executeForFresh(new Object[] { grantor, grantee, object_catalog, object_schema, object_name, column_name, privilege_type });
        if (!iter.first()) {
            throw new DException("DSE264", new Object[] { object_catalog, object_schema, object_name, column_name, privilege_type });
        } else {
            Object[] obj = (Object[]) iter.getObject();
            loadDataFromRecord(obj);
        }
    }

    public void save(_ServerSession serverSession) throws DException {
        Object[] parameters = new Object[] { grantor, grantee, object_catalog, object_schema, object_name, column_name, privilege_type, is_grantable };
        try {
            SqlSchemaConstants.insert(serverSession, SqlSchemaConstants.column_privileges_TableName, null, parameters);
        } catch (PrimaryConstraintException de) {
            DException tde = new DException("DSE1139", new Object[] { privilege_type, grantor, grantee, getTableName(), column_name });
            throw tde;
        } catch (SizeMisMatchException de) {
            if (de.getDseCode().equals("DSE773")) {
                DException tde = new DException("DSE8103", null);
                throw tde;
            }
        } catch (DException de) {
            if (de.getDseCode().equals("DSE1255")) {
                DException tde = new DException("DSE1139", new Object[] { privilege_type, grantor, grantee, getTableName(), column_name });
                throw tde;
            }
            if (de.getDseCode().equals("DSE773")) {
                DException tde = new DException("DSE8103", null);
                throw tde;
            }
            throw de;
        }
    }

    public void delete(_ServerSession serverSession) throws DException {
        booleanvalueexpression condition = ((DataDictionary) serverSession.getDataDictionary()).getPreparedStatementGetter().getColumnPrivilegesTableCondition();
        super.deleteColumnPrivilegeDescriptor(serverSession, SqlSchemaConstants.column_privileges_TableName, condition, new Object[] { grantor, grantee, object_catalog, object_schema, object_name, privilege_type, column_name });
    }

    private void loadDataFromRecord(Object[] values) throws DException {
        grantor = values[SystemTablesFields.columnPrivileges_grantor];
        grantee = values[SystemTablesFields.columnPrivileges_grantee];
        object_catalog = (String) values[SystemTablesFields.columnPrivileges_object_catalog];
        object_schema = (String) values[SystemTablesFields.columnPrivileges_object_schema];
        object_name = (String) values[SystemTablesFields.columnPrivileges_object_name];
        column_name = (String) values[SystemTablesFields.columnPrivileges_column_name];
        privilege_type = (String) values[SystemTablesFields.columnPrivileges_privilege_type];
        is_grantable = (String) values[SystemTablesFields.columnPrivileges_is_grantable];
    }

    public void loadDataFromRecord(_SelectQueryIterator iter) throws DException {
        loadDataFromRecord((Object[]) iter.getObject());
    }

    private String getTableName() {
        StringBuffer tableName = new StringBuffer();
        tableName.append(object_catalog).append(".").append(object_schema).append(".").append(object_name);
        return tableName.toString();
    }

    public int getDescriptorType() {
        return COLUMN_PRIVILEGE_DESCRIPTOR;
    }

    public void updateIsGrantableValue(_ServerSession serversession, String isGrantable) throws DException {
        booleanvalueexpression condition = ((DataDictionary) serversession.getDataDictionary()).getPreparedStatementGetter().getColumnPrivilegesTableCondition();
        super.update(serversession, SystemTables.column_privileges_TableName, condition, new Object[] { grantor, grantee, object_catalog, object_schema, object_name, privilege_type, column_name }, new Object[] { isGrantable }, new int[] { SystemTablesFields.columnPrivileges_is_grantable });
    }
}
