package com.objectsql.introspection;

/**
 * Descriptor de caracteristicas de mapeo de un atributo
 * mapeado con la anotacion  <code>@ModificationDate</code>
 *
 * @author plagreca
 */
public class ModificationDateMappingDefinition extends MappedFieldDefinition {

    @Override
    public boolean isInsertable() {
        return false;
    }

    @Override
    public Object getColumnValue() {
        throw new UnsupportedOperationException("Los campos mapeados como fecha de modificacion no poseen valor");
    }

    @Override
    public Class<? extends Object> getColumnType() {
        throw new UnsupportedOperationException("Los campos mapeados como fecha de modificacion no deben indicar el tipo");
    }

    @Override
    public boolean isFunctionValue() {
        return true;
    }

    @Override
    public String getAssigmentValue() {
        return getSQLWriter().getFunctionSysdate();
    }

    @Override
    public boolean hasNotNullValue() {
        return true;
    }
}
