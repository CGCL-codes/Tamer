package com.lagerplan.basisdienste.wissensbasis.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.lagerplan.basisdienste.util.persistence.SQliteSQL;
import com.lagerplan.basisdienste.wissensbasis.data.ZuordnungXYTO;

/**
 * <p>
 * Title: ZuordnungAuswahlkriteriumQualitativLagermittelDAO<br>
 * Description: CRUD Operationen auf Zuordnung Auswahlkriterien Qualitativ - Lagermittel.
 * Die Zuordnung wird �ber die Zuordnung der ID Kriterium - ID Lagermittel hergestellt.
 * Codiert als X-Y-Wert Paar. X = Auswahlkriterium, Y = Lagermittel.
 * Hinter der Zuordnungstabelle verbirgt sich folgende Fachlichkeit: Qualitative Bewertung von Lagermittel anhand von
 * Auswahlkriterien<br>
 * Copyright: Copyright (c) 2009<br>
 * Company: LAGERPLAN
 * </p>
 * .
 * 
 * @author %author: Michael Felber%
 * @version %version: 1%
 */
public class ZuordnungAuswahlkriteriumQualitativLagermittelDAO {

    private static Logger logger = Logger.getLogger(ZuordnungAuswahlkriteriumQualitativLagermittelDAO.class);

    private static final String GET_ALL_WISSENSDATENBANK_ZUORDNUNG_AUSWAHLKRITERIENQUALITATIV_LM = "SQL_SELECT_WISSENSDATENBANK_ZUORDNUNG_AUSWAHLKRITERIENQUALITATIV_LM";

    private static final String DEL_ALL_WISSENSDATENBANK_ZUORDNUNG_AUSWAHLKRITERIENQUALITATIV_LM = "SQL_DELETE_WISSENSDATENBANK_ZUORDNUNG_AUSWAHLKRITERIENQUALITATIV_LM";

    private static final String SET_WISSENSDATENBANK_ZUORDNUNG_AUSWAHLKRITERIENQUALITATIV_LM = "SQL_INSERT_WISSENSDATENBANK_ZUORDNUNG_AUSWAHLKRITERIENQUALITATIV_LM";

    public static final String KRITERIUM_ID = "id_kriterium";

    public static final String LAGERMITTEL_ID = "id_lagermittel";

    public static final String WERT = "wert";

    /**
	 * Load all. basic method: Loads all the data items which can be found in corresponding table
	 * 
	 * @return ArrayList<ZuordnungXYPBean> collection of persistObject items
	 */
    private ArrayList<ZuordnungXYPBean> loadAll() {
        ArrayList<ZuordnungXYPBean> persistentDatensaetze = new ArrayList<ZuordnungXYPBean>();
        SQliteSQL sql = new SQliteSQL();
        try {
            ResultSet oResult = sql.executeReadSQL(GET_ALL_WISSENSDATENBANK_ZUORDNUNG_AUSWAHLKRITERIENQUALITATIV_LM);
            while (oResult.next()) {
                ZuordnungXYPBean persistentDatensatz = loadFromDatabase(oResult);
                if (persistentDatensatz != null) {
                    persistentDatensaetze.add(persistentDatensatz);
                }
            }
            logger.debug("[loadAll()] ArrayList holds " + persistentDatensaetze.size() + " elements in total.");
        } catch (Exception e) {
            logger.error("[loadAll()] Error fetching data: " + e.getMessage());
        } finally {
            if (sql != null) sql.close();
        }
        return persistentDatensaetze;
    }

    /**
	 * Load from database. Takes the resultSet and transforms retrieved data into persist object container
	 * 
	 * Use this method whenever you want to convert result of SQL select into Java Object (object relational mapping of one database table
	 * into Java object)
	 * 
	 * @param oResult - SQL result set
	 * 
	 * @return ZuordnungXYPBean - persist object, Java Object which contains one row of database table
	 * 
	 * @throws SQLException - Exception
	 */
    private ZuordnungXYPBean loadFromDatabase(final ResultSet rs) throws Exception {
        ZuordnungXYPBean persistentObject = null;
        try {
            persistentObject = new ZuordnungXYPBean();
            Integer kriteriumId = rs.getInt(KRITERIUM_ID);
            Integer lagermittelId = rs.getInt(LAGERMITTEL_ID);
            Integer wert = rs.getInt(WERT);
            persistentObject.setIdY(lagermittelId);
            persistentObject.setIdX(kriteriumId);
            persistentObject.setWert(wert);
        } catch (Exception e) {
            logger.error("[loadFromDatabase()] Error: " + e.getMessage());
            throw e;
        }
        return persistentObject;
    }

    /**
	 * BeforeInsert. Operationen, die als Vorbereitung f�r Einf�gen notwendig sind (z.b. Primary Key Erzeugung) Hier werden alle Datens�tze
	 * gel�scht komplett und auch komplett wieder in die Tabelle geschrieben.
	 * 
	 * @throws Exception - Der Fehlerwurf
	 */
    private void beforeInsert() throws Exception {
        SQliteSQL sql = new SQliteSQL();
        try {
            sql.executeWriteSQL(DEL_ALL_WISSENSDATENBANK_ZUORDNUNG_AUSWAHLKRITERIENQUALITATIV_LM, new Vector<String>());
        } catch (Exception e) {
            logger.error("[beforeInsert()] Error: " + e.getMessage());
            throw e;
        } finally {
            if (sql != null) sql.close();
        }
    }

    /**
	 * SaveInsert. Speichern genau eines Datensatzes per INSERT Strategie.
	 * 
	 * @param datensatz - das persistent gemachte Objekt
	 * 
	 * @return der persistierte Datensatz
	 * 
	 * @throws Exception - Fehlerwurf
	 */
    private ZuordnungXYPBean saveInsert(final ZuordnungXYPBean datensatz) throws Exception {
        SQliteSQL sql = new SQliteSQL();
        try {
            Vector<String> vParams = new Vector<String>();
            vParams.add(String.valueOf(datensatz.getIdY()));
            vParams.add(String.valueOf(datensatz.getIdX()));
            vParams.add(String.valueOf(datensatz.getWert()));
            String strSQLKey = SET_WISSENSDATENBANK_ZUORDNUNG_AUSWAHLKRITERIENQUALITATIV_LM;
            sql.executeWriteSQL(strSQLKey, vParams);
            return datensatz;
        } catch (Exception e) {
            logger.error("[beforeInsert()] Error: " + e.getMessage());
            throw e;
        } finally {
            if (sql != null) sql.close();
        }
    }

    /**
	 * Save all. Speichern aller Datens�tze. Aktuell per INSERT Strategie - f�r Updates (aktuell nicht gewollt) ist UPDATE Strategie
	 * notwendig.
	 * 
	 * @param datensaetze - die Sammlung von zu persistierenden Datens�tzen
	 */
    private void saveAll(final ArrayList<ZuordnungXYPBean> datensaetze) {
        try {
            beforeInsert();
            Iterator<ZuordnungXYPBean> datensatzIterator = datensaetze.iterator();
            while (datensatzIterator.hasNext()) {
                ZuordnungXYPBean zuordnungXYPBean = datensatzIterator.next();
                saveInsert(zuordnungXYPBean);
            }
        } catch (Exception e) {
            logger.error("[saveAll()] Error fetching data: " + e.getMessage());
        } finally {
        }
    }

    /**
	 * Bef�llt den Java-Datencontainer mit Daten aus der PersistSchicht Nimmt daf�r das ResultSet
	 * 
	 * @param oResult
	 * @return
	 * @throws SQLException
	 */
    private ZuordnungXYTO fillZuordnungDetails(final ZuordnungXYPBean persistObject) throws Exception {
        ZuordnungXYTO zuordnungXYTO = null;
        try {
            zuordnungXYTO = new ZuordnungXYTO();
            Integer lagermittelId = persistObject.getIdY();
            Integer kriteriumId = persistObject.getIdX();
            Integer wert = persistObject.getWert();
            zuordnungXYTO.setIdX(kriteriumId);
            zuordnungXYTO.setIdY(lagermittelId);
            zuordnungXYTO.setWert(wert);
        } catch (Exception e) {
            logger.error("[fillZuordnungDetails()]: " + e.getMessage());
            throw e;
        }
        return zuordnungXYTO;
    }

    /**
	 * Convert2 to. converts data into a more higher level of representation. tranforms some raw table data into java object representation
	 * 
	 * @param persistobjekte - collection of data items (match to database table)
	 * 
	 * @return ArrayList<ZuordnungXYTO> - colection of data items to use within application
	 * 
	 * @throws Exception - Exception
	 */
    private Hashtable<ZuordnungXYTO, Boolean> convert2TO(final ArrayList<ZuordnungXYPBean> persistobjekte) throws Exception {
        Hashtable<ZuordnungXYTO, Boolean> transportObjects = new Hashtable<ZuordnungXYTO, Boolean>();
        try {
            Iterator<ZuordnungXYPBean> objectIterator = persistobjekte.iterator();
            while (objectIterator.hasNext()) {
                ZuordnungXYTO transportObject = fillZuordnungDetails(objectIterator.next());
                if (transportObject != null) {
                    if (transportObjects.containsKey(transportObject)) {
                        logger.warn("(convert2TO) Zuordnung[" + transportObject.getIdX() + "|" + transportObject.getIdY() + "] bereits in Collection -> doppelte Eintr�ge?");
                    } else {
                        transportObjects.put(transportObject, true);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("[convert2TO()] Error: " + e.getMessage());
            throw e;
        }
        return transportObjects;
    }

    /**
	 * Convert from to. Konvertieren von Transportobjekten nach PersistentObjekten.
	 * 
	 * @param transportobjekte - die Collection aller Transportobjekte
	 * 
	 * @return ArrayList<ZuordnungXYPBean> - die Collection aller entsprechender PersistentObjekte
	 * 
	 * @throws Exception - Fehlerwurf
	 */
    private ArrayList<ZuordnungXYPBean> convertFromTO(Hashtable<ZuordnungXYTO, Boolean> transportobjekte) throws Exception {
        ArrayList<ZuordnungXYPBean> persistObjects = new ArrayList<ZuordnungXYPBean>();
        try {
            Enumeration<ZuordnungXYTO> enumIndexComponents = transportobjekte.keys();
            while (enumIndexComponents.hasMoreElements()) {
                ZuordnungXYTO zuordnungXYTO = enumIndexComponents.nextElement();
                ZuordnungXYPBean persistObject = convertFromTO(zuordnungXYTO);
                if (persistObject != null) {
                    persistObjects.add(persistObject);
                }
            }
        } catch (Exception e) {
            logger.error("[convertFromTO()] Error: " + e.getMessage());
            throw e;
        }
        return persistObjects;
    }

    /**
	 * Convert from to. Konvertieren von Transportobjekt nach PersistentObjekt.
	 * 
	 * @param transportObjekt - der Datensatz aus der Transportschicht.
	 * 
	 * @return ZuordnungXYPBean - das Pendant in der Persistenzschicht
	 * 
	 * @throws Exception - der Fehlerwurf
	 */
    private ZuordnungXYPBean convertFromTO(ZuordnungXYTO transportObjekt) throws Exception {
        ZuordnungXYPBean zuordnungXYPBean = null;
        try {
            zuordnungXYPBean = new ZuordnungXYPBean();
            zuordnungXYPBean.setIdX(transportObjekt.getIdX());
            zuordnungXYPBean.setIdY(transportObjekt.getIdY());
            zuordnungXYPBean.setWert(transportObjekt.getWert());
        } catch (Exception e) {
            logger.error("[convertFromTO()]: " + e.getMessage());
            throw e;
        }
        return zuordnungXYPBean;
    }

    /**
	 * Die Zuordnungen von Lagermittel zu qualitativen Auswahlkriterien in Form von XY Paaren.
	 * Codiert als X-Y-Wert Paar. X = Auswahlkriterium, Y = Lagermittel.
	 * 
	 * @return the zuordnungen auswahlkriterium lagermittel
	 */
    public Hashtable<ZuordnungXYTO, Boolean> getZuordnungenAuswahlkriteriumQualitativLagermittel() {
        Hashtable<ZuordnungXYTO, Boolean> zuordnungen = null;
        try {
            ArrayList<ZuordnungXYPBean> datensaetze = null;
            datensaetze = loadAll();
            zuordnungen = convert2TO(datensaetze);
            logger.debug("[getZuordnungenAuswahlkriteriumQualitativLagermittel()] ArrayList holds " + zuordnungen.size() + " elements in total.");
        } catch (Exception e) {
            logger.error("[getZuordnungenAuswahlkriteriumQualitativLagermittel()] Error: " + e.getMessage());
        }
        return zuordnungen;
    }

    /**
	 * saveZuordnungenAuswahlkriteriumQualitativLagermittel.
	 * 
	 * @param transportobjekte - Transportobjekte die persistiert werden sollen
	 */
    public void saveZuordnungenAuswahlkriteriumQualitativLagermittel(Hashtable<ZuordnungXYTO, Boolean> transportobjekte) {
        try {
            ArrayList<ZuordnungXYPBean> datensaetze = convertFromTO(transportobjekte);
            saveAll(datensaetze);
        } catch (Exception e) {
            logger.error("[saveZuordnungenAuswahlkriteriumQualitativLagermittel()] Error: " + e.getMessage());
        }
    }
}
