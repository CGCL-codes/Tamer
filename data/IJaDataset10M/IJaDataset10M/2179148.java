package GDisc;

import Domini.*;
import Errors.*;
import java.sql.*;
import CompDomini.*;
import CompGDisc.*;

/**
 * <p>T�tol: Projecte PP</p>
 * <p>Descripci�: Projecte PP primavera 2002</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Cl�ster 3</p>
 * @author Frederic P�rez Ordeig i canvis per Xavier Cusco Sureda
 * @version 0.5
 * Gestor de disc provisional per a les persones.
 */
public class GDBpersones extends GDBgen implements GDBconsts {

    /**
   * Constructora on es defineixen les consultes compilades.
   * Es guanya en efici�ncia si es fa aix�.
   * @throws excepcio
   */
    public static final int CONSULTANOMCOGNOM = 5;

    public static final int COUNTiID = 6;

    public static final int TOTiID = 7;

    public static final int CGENID = 8;

    public static final int CNOMID = 9;

    public static final int CPOBID = 10;

    public static final int CPROVID = 11;

    public static final int CTELFID = 12;

    public static final int CMAILID = 13;

    public static final int CCOGID = 14;

    public static final int QCCOGID = 15;

    public static final int QCNOMID = 16;

    public static final int QCMAILID = 17;

    public static final int QCPOBID = 18;

    public static final int QCPROVID = 19;

    public static final int QCTELFID = 20;

    public static final int QCGENID = 21;

    public static final int CGEN2ID = 22;

    public static final int QCGEN2ID = 23;

    public static final int CNOM2ID = 24;

    public static final int QCNOM2ID = 25;

    public static final int CCOG2ID = 26;

    public static final int QCCOG2ID = 27;

    public static final int CNAID = 28;

    public static final int QCNAID = 29;

    public static final int CCACHEID = 32;

    public static final int QCCACHEID = 33;

    public static final int CCONVID = 30;

    public static final int QCCONVID = 31;

    public static final int CONSULTAESDEVENIMENT = 34;

    public GDBpersones(boolean b) {
        super("test.jds", "user", "");
        iniSQL();
    }

    public GDBpersones() throws excepcio {
        super("agendac.jds", "user", "");
        iniSQL();
    }

    private void iniSQL() throws excepcio {
        preparaConsultaP("INSERT INTO \"Persones\" (\"nom\",\"cognoms\",\"tipus\",\"rang\",\"refDireccio\",\"telefon1\",\"telefon2\",\"email\",\"refManager\",\"cache\",\"convocatoriaEst\",\"nomArt�stic\")" + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?)", ALTA);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"nom\"=? AND \"cognoms\"=?", CONSULTANOMCOGNOM);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"idPersona\"=?", CONSULTAID);
        preparaConsultaP("DELETE FROM \"Persones\" WHERE \"idPersona\"=?", BAIXAID);
        preparaConsultaP("UPDATE \"Persones\" SET \"nom\"=?,\"cognoms\"=?,\"tipus\"=?,\"rang\"=?,\"refDireccio\"=?,\"telefon1\"=?,\"telefon2\"=?,\"email\"=?,\"refManager\"=?,\"cache\"=?,\"convocatoriaEst\"=?,\"nomArt�stic\"=?" + " WHERE \"idPersona\"=?", MODIFICACIOID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE \"tipus\"=?", COUNTiID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"tipus\"=?", TOTiID);
        preparaConsultaP("SELECT * FROM \"Persones\" INNER JOIN \"Direccions\" ON (\"refDireccio\"=\"idDireccio\") WHERE (\"nom\"=? OR \"cognoms\"=? OR \"localitat\"=? OR \"provincia\"=? OR \"telefon1\"=? OR \"telefon2\"=? or \"email\"=? OR \"pais\"=? OR \"npp\"=?) AND \"tipus\"=1", CGENID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" INNER JOIN \"Direccions\" ON (\"refDireccio\"=\"idDireccio\") WHERE (\"nom\"=? OR \"cognoms\"=? OR \"localitat\"=? OR \"provincia\"=? OR \"telefon1\"=? OR \"telefon2\"=? OR \"email\"=? OR \"pais\"=? OR \"npp\"=?) AND \"tipus\"=1", QCGENID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"nom\"=? AND \"tipus\"=1", CNOMID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE \"nom\"=? AND \"tipus\"=1", QCNOMID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"cognoms\"=? AND \"tipus\"=1", CCOGID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE \"cognoms\"=? AND \"tipus\"=1", QCCOGID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"nom\"=? AND \"tipus\"=2", CNOM2ID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE \"nom\"=? AND \"tipus\"=2", QCNOM2ID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"cognoms\"=? AND \"tipus\"=2", CCOG2ID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE \"cognoms\"=? AND \"tipus\"=2", QCCOG2ID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"nomArt�stic\"=? AND \"tipus\"=2", CNAID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE \"nomArt�stic\"=? AND \"tipus\"=2", QCNAID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE (\"nom\"=? OR \"cognoms\"=? OR \"nomArt�stic\"=?)AND \"tipus\"=2", CGEN2ID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE (\"nom\"=? OR \"cognoms\"=? OR \"nomArt�stic\"=?)AND \"tipus\"=2", QCGEN2ID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"convocatoriaEst\"=? AND \"tipus\"=2", CCONVID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE \"convocatoriaEst\"=? AND \"tipus\"=2", QCCONVID);
        preparaConsultaP("SELECT * FROM \"Persones\" INNER JOIN \"Direccions\" ON (\"refDireccio\"=\"idDireccio\") WHERE \"localitat\"=? AND \"tipus\"=1", CPOBID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" INNER JOIN \"Direccions\" ON (\"refDireccio\"=\"idDireccio\") WHERE \"localitat\"=? AND \"tipus\"=1", QCPOBID);
        preparaConsultaP("SELECT * FROM \"Persones\" INNER JOIN \"Direccions\" ON (\"refDireccio\"=\"idDireccio\") WHERE \"provincia\"=? AND \"tipus\"=1", CPROVID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" INNER JOIN \"Direccions\" ON (\"refDireccio\"=\"idDireccio\") WHERE \"provincia\"=? AND \"tipus\"=1", QCPROVID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE (\"telefon1\"=? OR \"telefon2\"=?) AND \"tipus\"=1", CTELFID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE (\"telefon1\"=? OR \"telefon2\"=?) AND \"tipus\"=1", QCTELFID);
        preparaConsultaP("SELECT * FROM \"Persones\" WHERE \"email\"=? AND \"tipus\"=1", CMAILID);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"Persones\" WHERE \"email\"=? AND \"tipus\"=1", QCMAILID);
        preparaConsultaP("SELECT * FROM \"Persones\" INNER JOIN \"relEsdevenimentPersona\" ON (\"refPersona\"=\"idPersona\") WHERE \"refEsdeveniment\"=?", CONSULTAESDEVENIMENT);
    }

    /**
  * Fa que els resultats siguin les persones que coincideix de criteris amb nom i cognoms.
  * Fer servir seguent i anterior per navegar pels resultats i getPersona per
  * tenir l'objecte persona que s'ha trobat. La primera vegada s'ha de fer un seguent.
  * @param szNom
  * @param szCognoms
  * @throws excepcio
  */
    public void buscaNomCognoms(String szNom, String szCognoms) throws excepcio {
        Object oLlista[] = new Object[2];
        oLlista[0] = szNom;
        oLlista[1] = szCognoms;
        rsGen = resultatConsultaP(oLlista, CONSULTANOMCOGNOM);
    }

    /**
   * Posa a
   * @param lID
   * @throws excepcio
   */
    public void buscaID(long lID) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = new Long(lID);
        rsGen = resultatConsultaP(oLlista, CONSULTAID);
    }

    public void buscaRelEsdeveniment(esdeveniment esd) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = new Long(esd.getID());
        rsGen = resultatConsultaP(oLlista, CONSULTAESDEVENIMENT);
    }

    /**
   * Sobreescriu el metode abstracte de la
   * @return Object
   * @throws excepcio
   */
    public Object getObjecte() throws excepcio {
        int nTipus;
        GDBdireccions gdbdir = new GDBdireccions();
        GDBpersones gdbpers = new GDBpersones();
        participant part;
        persona pers;
        try {
            nTipus = rsGen.getInt("tipus");
            if (nTipus == 1) {
                pers = new persona(rsGen.getString("nom"), rsGen.getString("cognoms"));
                pers.setID(rsGen.getLong("idPersona"));
                pers.setEmail(rsGen.getString("email"));
                pers.setRang(rsGen.getInt("rang"));
                pers.setTelefon1(rsGen.getString("telefon1"));
                pers.setTelefon2(rsGen.getString("telefon2"));
                long lRefDir = rsGen.getLong("refDireccio");
                if (!rsGen.wasNull()) {
                    gdbdir.buscaID(lRefDir);
                    if (gdbdir.seguent()) {
                        pers.setDir((direccio) gdbdir.getObjecte());
                    }
                }
                pers.guardat();
                return (pers);
            } else {
                part = new participant(rsGen.getString("nom"), rsGen.getString("cognoms"), rsGen.getString("nomArt�stic"));
                part.setID(rsGen.getLong("idPersona"));
                part.setEmail(rsGen.getString("email"));
                part.setRang(rsGen.getInt("rang"));
                part.setTelefon1(rsGen.getString("telefon1"));
                part.setTelefon2(rsGen.getString("telefon2"));
                long lRefDir = rsGen.getLong("refDireccio");
                if (!rsGen.wasNull()) {
                    gdbdir.buscaID(lRefDir);
                    if (gdbdir.seguent()) {
                        part.setDir((direccio) gdbdir.getObjecte());
                    }
                }
                part.setCache(rsGen.getFloat("cache"));
                part.setConvocatoriaEst(rsGen.getInt("convocatoriaEst"));
                long lRefManager = rsGen.getLong("refManager");
                if (!rsGen.wasNull()) {
                    gdbpers.buscaID(lRefManager);
                    if (gdbpers.seguent()) {
                        part.setManager((persona) gdbpers.getObjecte());
                    }
                }
                part.setNomArtistic(rsGen.getString("nomArt�stic"));
                part.guardat();
                return part;
            }
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut crear persona " + esql.getMessage(), 0);
        }
    }

    /**
   * Dona d'alta una persona a la BD
   * @param oID
   * @throws excepcio
   */
    protected void altaEspecifica(objecteID oID) throws excepcio {
        persona pers1 = (persona) oID;
        Object oLlista[] = new Object[12];
        GDBdireccions gdbdir;
        long lIDDir;
        gdbdir = new GDBdireccions();
        oLlista[0] = pers1.getNom();
        oLlista[1] = pers1.getCognoms();
        oLlista[3] = new Integer(pers1.getRang());
        try {
            gdbdir.guarda(pers1.getDir());
            oLlista[4] = new Long(pers1.getDir().getID());
        } catch (excepcio e) {
            if (e.getCodi() != e.ALTABUIDA) throw e;
        }
        oLlista[5] = pers1.getTelefon1();
        oLlista[6] = pers1.getTelefon2();
        oLlista[7] = pers1.getEmail();
        if (oID.getClass().getName().equals("Domini.participant")) {
            participant part1 = (participant) oID;
            oLlista[2] = new Integer(part1.getTipus());
            oLlista[8] = new Long(part1.getManager().getID());
            oLlista[9] = new Float(part1.getCache());
            oLlista[10] = new Integer(part1.getConvocatoriaEst());
            oLlista[11] = part1.getNomArtistic();
        } else {
            oLlista[2] = new Integer(pers1.getTipus());
            oLlista[8] = oLlista[9] = oLlista[10] = oLlista[11] = null;
        }
        executaConsultaP(oLlista, ALTA);
    }

    /**
   * Dona de baixa una persona
   * @param oID
   * @throws excepcio
   */
    protected void baixaEspecifica(objecteID oID) throws excepcio {
        persona pers = (persona) oID;
        GDBdireccions gdb_dir = new GDBdireccions();
        Object oLlista[] = new Object[1];
        try {
            gdb_dir.baixa(pers.getDir());
        } catch (excepcio e) {
            if (e.getCodi() != e.BAIXAFALLA) throw (e);
        }
        oLlista[0] = new Long(pers.getID());
        executaConsultaP(oLlista, BAIXAID);
    }

    /**
   * Modifica les dades d'una persona
   * @param oID
   * @throws excepcio
   */
    protected void modificacioEspecifica(objecteID oID) throws excepcio {
        persona pers1 = (persona) oID;
        Object oLlista[] = new Object[13];
        GDBdireccions gdbdir;
        long lIDDir;
        gdbdir = new GDBdireccions();
        oLlista[0] = pers1.getNom();
        oLlista[1] = pers1.getCognoms();
        oLlista[2] = new Integer(pers1.getTipus());
        oLlista[3] = new Integer(pers1.getRang());
        try {
            gdbdir.guarda(pers1.getDir());
            oLlista[4] = new Long(pers1.getDir().getID());
        } catch (excepcio e) {
            if (e.getCodi() != e.ALTABUIDA) throw e;
        }
        oLlista[5] = pers1.getTelefon1();
        oLlista[6] = pers1.getTelefon2();
        oLlista[7] = pers1.getEmail();
        if (oID.getClass().getName().equals("Domini.participant")) {
            participant part1 = (participant) oID;
            oLlista[8] = new Long(part1.getManager().getID());
            oLlista[9] = new Float(part1.getCache());
            oLlista[10] = new Integer(part1.getConvocatoriaEst());
            oLlista[11] = part1.getNomArtistic();
        } else {
            oLlista[8] = oLlista[9] = oLlista[10] = oLlista[11] = null;
        }
        oLlista[12] = new Long(pers1.getID());
        executaConsultaP(oLlista, MODIFICACIOID);
    }

    public void baixaE(objecteID oID) throws excepcio {
        baixaEspecifica(oID);
    }

    public int quants(int i) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = new Integer(i);
        rsGen = resultatConsultaP(oLlista, COUNTiID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut crear persona " + esql.getMessage(), 0);
        }
    }

    public Object getFirst(int i) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = new Integer(i);
        rsGen = resultatConsultaP(oLlista, TOTiID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public Object cercaGen(String szCond) throws excepcio {
        Object[] oLlista = new Object[9];
        int i;
        for (i = 0; i < 9; i++) {
            oLlista[i] = szCond;
        }
        rsGen = resultatConsultaP(oLlista, CGENID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaGen(String szCond) throws excepcio {
        Object[] oLlista = new Object[9];
        int i;
        for (i = 0; i < 9; i++) {
            oLlista[i] = szCond;
        }
        rsGen = resultatConsultaP(oLlista, QCGENID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaNom(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, CNOMID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaNom(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, QCNOMID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaCog(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, CCOGID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaCog(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, QCCOGID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaPob(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, CPOBID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaPob(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, QCPOBID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaProv(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, CPROVID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaProv(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, QCPROVID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaTelf(String szCond) throws excepcio {
        Object oLlista[] = new Object[2];
        oLlista[0] = szCond;
        oLlista[1] = szCond;
        rsGen = resultatConsultaP(oLlista, CTELFID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaTelf(String szCond) throws excepcio {
        Object oLlista[] = new Object[2];
        oLlista[0] = szCond;
        oLlista[1] = szCond;
        rsGen = resultatConsultaP(oLlista, QCTELFID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaMail(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, CMAILID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaMail(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, QCMAILID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaNom2(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, CNOM2ID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaNom2(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, QCNOM2ID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaGen2(String szCond) throws excepcio {
        Object oLlista[] = new Object[3];
        oLlista[0] = szCond;
        oLlista[1] = szCond;
        oLlista[2] = szCond;
        rsGen = resultatConsultaP(oLlista, CGEN2ID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaGen2(String szCond) throws excepcio {
        Object oLlista[] = new Object[3];
        oLlista[0] = szCond;
        oLlista[1] = szCond;
        oLlista[2] = szCond;
        rsGen = resultatConsultaP(oLlista, QCGEN2ID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaCog2(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, CCOG2ID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaCog2(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, QCCOG2ID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaNA(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, CNAID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaNA(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = szCond;
        rsGen = resultatConsultaP(oLlista, QCNAID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaConv(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = new Integer(Integer.parseInt(szCond));
        rsGen = resultatConsultaP(oLlista, CCONVID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaConv(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = new Integer(Integer.parseInt(szCond));
        rsGen = resultatConsultaP(oLlista, QCCONVID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }

    public Object cercaCache(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = new Float(Float.parseFloat(szCond));
        rsGen = resultatConsultaP(oLlista, CCACHEID);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }

    public int QcercaCache(String szCond) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = new Float(Float.parseFloat(szCond));
        rsGen = resultatConsultaP(oLlista, QCCACHEID);
        try {
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            return 0;
        }
    }
}
