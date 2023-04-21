package org.gvsig.crs.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.gvsig.crs.Crs;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.ICrs;
import org.gvsig.crs.ogr.Esri2wkt;
import es.idr.teledeteccion.connection.EpsgConnection;
import es.idr.teledeteccion.connection.Query;

/**
 * Repositorio de CRSs de EPSG
 * 
 * @author Jos� Luis G�mez Mart�nez (jolugomar@gmail.com)
 *
 */
public class EsriRepository implements ICrsRepository {

    EpsgConnection connection = null;

    public EsriRepository() {
        connection = new EpsgConnection();
        connection.setConnectionEsri();
    }

    public ICrs getCrs(String code) {
        String cadWKT = "";
        Crs crs = null;
        String sentence = "SELECT esri_code, esri_wkt, esri_proj, esri_geog, esri_datum " + "FROM ESRI " + "WHERE esri_code = " + code;
        ResultSet result = Query.select(sentence, connection.getConnection());
        try {
            if (!result.next()) return null;
            cadWKT = result.getString("esri_wkt");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
        cadWKT = cadWKT.substring(0, cadWKT.length() - 1) + ", AUTHORITY[\"ESRI\"," + Integer.parseInt(code) + "]]";
        if (cadWKT.charAt(0) == 'P') {
            Esri2wkt wk = new Esri2wkt(cadWKT);
            cadWKT = wk.getWkt();
        }
        try {
            crs = new Crs(Integer.parseInt(code), cadWKT);
        } catch (CrsException e) {
            e.printStackTrace();
        }
        return crs;
    }
}
