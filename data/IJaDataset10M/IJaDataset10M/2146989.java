package com.objectsql.statement.update;

import com.objectsql.introspection.ObjectMappingHelper;
import com.objectsql.statement.query.filter.BaseFilter;

/**
 * Genera el stamtement sql para eliminar un objeto en particular teniendo en
 * cuenta la informacion proporcionada mediante annotations de la clase a la que
 * pertenece para poder determinar cuales atributos conforman la clave primaria
 * <p/>
 * Dicha clase debe tener declarada a nivel clase la anotacion
 * <code>@PrimaryKeyJoinColumns</code>
 * <p/>
 * Ejemplo:
 *
 * @author plagreca
 * @PrimaryKeyJoinColumns({@PrimaryKeyJoinColumn(name =
 * "nombre"),@PrimaryKeyJoinColumn(
 * name = "apellido")})
 * public class Usuario { ...
 * }
 */
public class EntityDeleteStatementUniqueObject extends AbstractDeleteStatement {

    private BaseFilter filter;

    private ObjectMappingHelper objectMappingHelper;

    public EntityDeleteStatementUniqueObject(Object o) {
        this.objectMappingHelper = new ObjectMappingHelper(o);
        this.filter = this.objectMappingHelper.getFilterByPrimaryKey();
    }

    @Override
    protected BaseFilter getFilters() {
        return filter;
    }

    @Override
    protected String getTableName() {
        return this.objectMappingHelper.getTableName();
    }
}
