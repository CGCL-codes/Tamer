package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;
import org.datanucleus.store.rdbms.sql.expression.StringExpression;

/**
 * Implementation of Spatial "geometryType" method.
 */
public class SpatialGeometryTypeMethod extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (args == null || args.size() != 1) {
            throw new NucleusUserException("Cannot invoke Spatial.geometryType without 1 argument");
        }
        SQLExpression argExpr1 = (SQLExpression) args.get(0);
        ArrayList funcArgs = new ArrayList();
        funcArgs.add(argExpr1);
        JavaTypeMapping m = getMappingForClass(String.class);
        return new StringExpression(stmt, m, "GeometryType", funcArgs);
    }
}
