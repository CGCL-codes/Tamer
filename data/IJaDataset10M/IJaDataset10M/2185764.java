package fr.ign.cogit.geoxygene.datatools.ojb;

import java.sql.Connection;
import oracle.jdbc.driver.OracleConnection;
import oracle.sdoapi.adapter.SDOGeometry;
import oracle.sdoapi.adapter.SDOTemplateFactory;
import oracle.sdoapi.adapter.SDOTemplateFactoryImpl;
import oracle.sdoapi.geom.Geometry;
import oracle.sdoapi.geom.GeometryFactory;
import oracle.sdoapi.sref.SRManager;
import oracle.sql.STRUCT;
import org.apache.ojb.broker.accesslayer.conversions.FieldConversion;
import org.apache.ojb.broker.util.batch.BatchConnection;
import fr.ign.cogit.geoxygene.datatools.oracle.IsoAndSdo;
import fr.ign.cogit.geoxygene.spatial.geomroot.GM_Object;

/**
 * Conversion dans les 2 sens entre une SDO_GEOMETRY (format sql.STRUCT) et un GM_Object.
 * Ceci est utilise par OJB.
 * 
 * @author Thierry Badard & Arnaud Braun
 * @version 1.0
 *
 */
public class GeomGeOxygene2Oracle implements FieldConversion {

    public static Connection CONNECTION;

    public static SRManager SRM;

    public static GeometryFactory GF;

    public Object sqlToJava(Object object) {
        try {
            Geometry sdoGeom = SDOGeometry.STRUCTtoGeometry((STRUCT) object, GF, SRM);
            GM_Object isoGeom = IsoAndSdo.sdoapi2iso(sdoGeom);
            return isoGeom;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Methode utilisee si on n'utilise PAS les classes maison "OxygenePresistenceBroker" et "GeOxygeneStatementManager".
     * Ceci est a definir dans OJB.properties.
     * Inconvenient : on utilise une variable connection statique, 
     * donc impossible de se connecter a plusieurs bases Oracle simultanement.
     * De plus il y a un bug avec OJB : impossible d'ecrire des geometries nulles.
     */
    public Object javaToSql(Object object) {
        try {
            Geometry sdoGeom = IsoAndSdo.iso2sdoapi(GF, (GM_Object) object);
            SDOTemplateFactory sdoTF;
            if (CONNECTION instanceof BatchConnection) {
                OracleConnection oConn = (OracleConnection) ((BatchConnection) CONNECTION).getDelegate();
                sdoTF = new SDOTemplateFactoryImpl(oConn);
            } else sdoTF = new SDOTemplateFactoryImpl((OracleConnection) CONNECTION);
            STRUCT str = SDOGeometry.geometryToSTRUCT(sdoGeom, sdoTF);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** Methode utilisee si on utilise les classes maison "OxygenePresistenceBroker" et "GeOxygeneStatementManager".
	 * Ceci est a definir dans OJB.properties.
	 * Ceci corrige les deux defauts de la methode qui ne passe pas de connection en parametre.
	 */
    public Object javaToSql(Object object, Connection conn) {
        try {
            Geometry sdoGeom = IsoAndSdo.iso2sdoapi(GF, (GM_Object) object);
            SDOTemplateFactory sdoTF;
            if (conn instanceof BatchConnection) {
                OracleConnection oConn = (OracleConnection) ((BatchConnection) conn).getDelegate();
                sdoTF = new SDOTemplateFactoryImpl(oConn);
            } else sdoTF = new SDOTemplateFactoryImpl((OracleConnection) conn);
            STRUCT str = SDOGeometry.geometryToSTRUCT(sdoGeom, sdoTF);
            return str;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
