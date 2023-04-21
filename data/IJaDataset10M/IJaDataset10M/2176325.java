package org.datanucleus.store.rdbms.sql.method;

import java.util.ArrayList;
import java.util.List;
import org.datanucleus.exceptions.NucleusUserException;
import org.datanucleus.store.mapped.mapping.JavaTypeMapping;
import org.datanucleus.store.rdbms.sql.expression.GeometryExpression;
import org.datanucleus.store.rdbms.sql.expression.SQLExpression;

/**
 * Implementation of Spatial "interiorRingN" method for Oracle.
 */
public class SpatialInteriorRingNMethod2 extends AbstractSQLMethod {

    public SQLExpression getExpression(SQLExpression expr, List args) {
        if (args == null || args.size() != 2) {
            throw new NucleusUserException("Cannot invoke Spatial.interiorRingN without 2 arguments");
        }
        SQLExpression geomExpr = (SQLExpression) args.get(0);
        SQLExpression distExpr = (SQLExpression) args.get(1);
        ArrayList geomFuncArgs = new ArrayList();
        geomFuncArgs.add(geomExpr);
        GeometryExpression geomSdoExpr = new GeometryExpression(stmt, null, "geometry.from_sdo_geom", geomFuncArgs, null);
        ArrayList treatFuncArgs = new ArrayList();
        treatFuncArgs.add(geomSdoExpr);
        ArrayList treatFuncTypeArgs = new ArrayList();
        treatFuncTypeArgs.add("Polygon");
        GeometryExpression treatExpr = new GeometryExpression(stmt, null, "treat", treatFuncArgs, treatFuncTypeArgs);
        ArrayList startFuncArgs = new ArrayList();
        startFuncArgs.add(treatExpr);
        startFuncArgs.add(distExpr);
        GeometryExpression startExpr = new GeometryExpression(stmt, null, "interiorRingN", startFuncArgs, null);
        ArrayList funcArgs = new ArrayList();
        funcArgs.add(startExpr);
        JavaTypeMapping geomMapping = SpatialMethodHelper.getGeometryMapping(clr, geomExpr);
        return new GeometryExpression(stmt, geomMapping, "geometry.get_sdo_geom", funcArgs, null);
    }
}
