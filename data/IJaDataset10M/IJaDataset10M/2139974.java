package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Implementation of Spatial "centroid" method for Oracle.
 */
public class SpatialCentroidMethod2 extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (args == null || args.size() != 1) {
            throw new NucleusUserException("Cannot invoke Spatial.centroid without 1 arguments");
        }
        SQLExpression argExpr1 = (SQLExpression) args.get(0);
        ArrayList geomFuncArgs = new ArrayList();
        geomFuncArgs.add(argExpr1);
        GeometryExpression geomExpr = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFuncArgs, null);
        ArrayList treatFuncArgs = new ArrayList();
        treatFuncArgs.add(geomExpr);
        ArrayList treatFuncTypeArgs = new ArrayList();
        treatFuncTypeArgs.add("surface");
        GeometryExpression treatExpr = new GeometryExpression(stmt, null, "treat", treatFuncArgs, treatFuncTypeArgs);
        ArrayList startFuncArgs = new ArrayList();
        startFuncArgs.add(treatExpr);
        GeometryExpression startExpr = new GeometryExpression(stmt, null, "centroid", startFuncArgs, null);
        ArrayList funcArgs = new ArrayList();
        funcArgs.add(startExpr);
        JavaTypeMapping geomMapping = SpatialMethodHelper.getGeometryMapping(clr, argExpr1);
        return new GeometryExpression(stmt, geomMapping, "geometry.get_sdo_geom", funcArgs, null);
    }
}
