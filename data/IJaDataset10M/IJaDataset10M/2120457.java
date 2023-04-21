package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Implementation of Spatial "geomCollFromText" method.
 */
public class SpatialGeomCollFromTextMethod extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (args == null || args.size() != 2) {
            throw new NucleusUserException("Cannot invoke Spatial.geomCollFromText without 2 arguments");
        }
        SQLExpression wktExpr = (SQLExpression) args.get(0);
        SQLExpression sridExpr = (SQLExpression) args.get(1);
        ArrayList funcArgs = new ArrayList();
        funcArgs.add(wktExpr);
        funcArgs.add(sridExpr);
        return new GeometryExpression(stmt, null, "GeomCollFromText", funcArgs, null);
    }
}
