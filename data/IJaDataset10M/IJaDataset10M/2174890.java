package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Implementation of "Spatial.intersects()" method for Oracle.
 */
public class SpatialIntersectsMethod2 extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression ignore, List args) {
        if (args == null || args.size() != 2) {
            throw new NucleusUserException("Cannot invoke Spatial.intersects without 2 arguments");
        }
        SQLExpression argExpr1 = (SQLExpression) args.get(0);
        SQLExpression argExpr2 = (SQLExpression) args.get(1);
        ArrayList geomFunc1Args = new ArrayList();
        geomFunc1Args.add(argExpr1);
        GeometryExpression geomExpr1 = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFunc1Args, null);
        ArrayList geomFunc2Args = new ArrayList();
        geomFunc1Args.add(argExpr2);
        GeometryExpression geomExpr2 = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFunc2Args, null);
        ArrayList funcArgs = new ArrayList();
        funcArgs.add(geomExpr1);
        funcArgs.add(geomExpr2);
        return SpatialMethodHelper.getBooleanExpression(stmt, "intersects", funcArgs, exprFactory);
    }
}
