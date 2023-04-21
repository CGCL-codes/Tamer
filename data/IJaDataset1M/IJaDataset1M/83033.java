package GDisc;

import CompGDisc.*;
import Domini.categoriaTasca;
import Errors.excepcio;
import CompDomini.objecteID;
import java.sql.SQLException;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Frederic P�rez Ordeig i Canvis per Xavier Cusc�
 * @version 1.1
 */
public class GDBcategoriaTasca extends GDBgen implements GDBconsts {

    private static int CONSULTANOM = 13;

    private static int CONSULTATOT = 14;

    private static int COUNTID = 15;

    /**
   *
   * @throws excepcio
   */
    private void iniSQL() throws excepcio {
        preparaConsultaP("INSERT INTO \"categoriesTasques\" (\"nom\",\"importancia\")" + " VALUES (?,?)", ALTA);
        preparaConsultaP("SELECT * FROM \"categoriesTasques\" WHERE \"nom\"=? ", CONSULTANOM);
        preparaConsultaP("SELECT * FROM \"categoriesTasques\" WHERE \"idCategoriaTasca\"=?", CONSULTAID);
        preparaConsultaP("DELETE FROM \"categoriesTasques\" WHERE \"idCategoriaTasca\"=?", BAIXAID);
        preparaConsultaP("UPDATE \"categoriesTasques\" SET \"nom\"=?,\"importancia\"=?" + " WHERE \"idCategoriaTasca\"=?", MODIFICACIOID);
        preparaConsultaP("SELECT * FROM \"categoriesTasques\"", CONSULTATOT);
        preparaConsultaP("SELECT COUNT(*) AS \"Comptador\" FROM \"categoriesTasques\"", COUNTID);
    }

    /**
   *
   * @param test
   * @throws excepcio
   */
    public GDBcategoriaTasca(boolean test) throws excepcio {
        super("test.jds", "user", "");
        iniSQL();
    }

    /**
   *
   * @throws excepcio
   */
    public GDBcategoriaTasca() throws excepcio {
        super("agendac.jds", "user", "");
        iniSQL();
    }

    public void buscaTot() throws excepcio {
        rsGen = resultatConsultaP(CONSULTATOT);
    }

    /**
   *
   * @param oID
   * @throws excepcio
   */
    protected void baixaEspecifica(objecteID oID) throws Errors.excepcio {
        Object oLlista[] = new Object[1];
        categoriaTasca cat = (categoriaTasca) oID;
        oLlista[0] = new Long(cat.getID());
        executaConsultaP(oLlista, BAIXAID);
    }

    /**
   *
   * @return
   * @throws excepcio
   */
    public Object getObjecte() throws Errors.excepcio {
        try {
            categoriaTasca cat = new categoriaTasca(rsGen.getString("nom"), rsGen.getInt("importancia"));
            cat.setID(rsGen.getLong("idCategoriaTasca"));
            cat.guardat();
            return cat;
        } catch (SQLException esql) {
            throw new excepcio(excepcio.GREU, "No s'ha pogut crear la categoria tasca", excepcio.PARAMETREINCORRECTE);
        }
    }

    /**
   *
   * @param oID
   * @throws excepcio
   */
    protected void altaEspecifica(objecteID oID) throws Errors.excepcio {
        Object oLlista[] = new Object[2];
        categoriaTasca cat = (categoriaTasca) oID;
        oLlista[0] = cat.getNom();
        oLlista[1] = new Integer(cat.getImportancia());
        executaConsultaP(oLlista, ALTA);
    }

    /**
   *
   * @param oID
   * @throws excepcio
   */
    protected void modificacioEspecifica(objecteID oID) throws Errors.excepcio {
        Object oLlista[] = new Object[3];
        categoriaTasca cat = (categoriaTasca) oID;
        oLlista[0] = cat.getNom();
        oLlista[1] = new Integer(cat.getImportancia());
        oLlista[2] = new Long(cat.getID());
        executaConsultaP(oLlista, MODIFICACIOID);
    }

    /**
   *
   * @param l_id
   * @throws excepcio
   */
    protected void buscaid(long l_id) throws excepcio {
        Object oLlista[] = new Object[1];
        oLlista[0] = new Long(l_id);
        rsGen = resultatConsultaP(oLlista, CONSULTAID);
    }

    public int quants() throws excepcio {
        try {
            rsGen = resultatConsultaP(COUNTID);
            rsGen.next();
            return rsGen.getInt("Comptador");
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut crear persona " + esql.getMessage(), 0);
        }
    }

    public Object getFirst() throws excepcio {
        rsGen = resultatConsultaP(CONSULTATOT);
        try {
            rsGen.next();
            return getObjecte();
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut trobar persona " + esql.getMessage(), 0);
        }
    }
}
