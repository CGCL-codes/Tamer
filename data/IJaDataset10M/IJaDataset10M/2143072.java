package GDisc;

import java.sql.*;
import CompDomini.*;
import CompGDisc.*;
import Domini.*;
import Errors.*;

/**
 * <p>Title: Agenda Cultural</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author Frederic P�rez Ordeig
 * @version 1.0
 */
public class GDBrelEsdevenimentTasca extends GDBgen implements GDBconsts {

    public static final int CONSULTAESDEVENIMENT = 20;

    public static final int CONSULTAESDEVENIMENTTASCA = 21;

    public static final int CONSULTATASCA = 22;

    public static final int BAIXAESDEVENIMENT = 23;

    protected void iniSQL() throws excepcio {
        preparaConsultaP("INSERT INTO \"relEsdevenimentTasca\" (\"refTasca\",\"refEsdeveniment\")" + " VALUES (?,?)", ALTA);
        preparaConsultaP("SELECT * FROM \"relEsdevenimentTasca\" WHERE \"refEsdeveniment\"=? ", CONSULTAESDEVENIMENT);
        preparaConsultaP("SELECT * FROM \"relEsdevenimentTasca\" WHERE \"refEsdeveniment\"=? AND \"refTasca\"=?", CONSULTAESDEVENIMENTTASCA);
        preparaConsultaP("SELECT * FROM \"relEsdevenimentTasca\" WHERE \"refTasca\"=? ", CONSULTATASCA);
        preparaConsultaP("SELECT * FROM \"relEsdevenimentTasca\" WHERE \"idRel\"=?", CONSULTAID);
        preparaConsultaP("DELETE FROM \"relEsdevenimentTasca\" WHERE \"idRel\"=?", BAIXAID);
        preparaConsultaP("DELETE FROM \"relEsdevenimentTasca\" WHERE \"refEsdeveniement\"=?", BAIXAESDEVENIMENT);
        preparaConsultaP("UPDATE \"relEsdevenimentTasca\" SET \"refEsdeveniment\"=?,\"refTasca\"=?" + " WHERE \"idRel\"=?", MODIFICACIOID);
    }

    public GDBrelEsdevenimentTasca(boolean b) throws excepcio {
        super("test.jds", "user", "");
        iniSQL();
    }

    public GDBrelEsdevenimentTasca() throws excepcio {
        super("agendac.jds", "user", "");
        iniSQL();
    }

    public void buscaTasca(long idTasca) throws excepcio {
        Object[] oLlista = new Object[1];
        oLlista[0] = new Long(idTasca);
        rsGen = resultatConsultaP(oLlista, CONSULTATASCA);
    }

    public void buscaEsdeveniment(long idEsdeveniment) throws excepcio {
        Object[] oLlista = new Object[1];
        oLlista[0] = new Long(idEsdeveniment);
        rsGen = resultatConsultaP(oLlista, CONSULTAESDEVENIMENT);
    }

    public void buscaEsdevenimentTasca(long idEsdeveniment, long idTasca) throws excepcio {
        Object[] oLlista = new Object[2];
        oLlista[0] = new Long(idEsdeveniment);
        oLlista[1] = new Long(idTasca);
        rsGen = resultatConsultaP(oLlista, CONSULTAESDEVENIMENTTASCA);
    }

    public void buscaId(long id) throws excepcio {
        Object[] oLlista = new Object[1];
        oLlista[0] = new Long(id);
        rsGen = resultatConsultaP(oLlista, CONSULTAID);
    }

    protected void baixaEspecifica(objecteID oID) throws Errors.excepcio {
        Object[] oLlista = new Object[1];
        relEsdevenimentTasca res = (relEsdevenimentTasca) oID;
        oLlista[0] = new Long(res.getID());
        executaConsultaP(oLlista, BAIXAID);
    }

    public void baixaEsdeveniment(long p_lidEsdeveniment) throws Errors.excepcio {
        Object[] oLlista = new Object[1];
        oLlista[0] = new Long(p_lidEsdeveniment);
        executaConsultaP(oLlista, BAIXAESDEVENIMENT);
    }

    public Object getObjecte() throws Errors.excepcio {
        try {
            relEsdevenimentTasca res = new relEsdevenimentTasca(rsGen.getLong("refEsdeveniment"), rsGen.getLong("refTasca"));
            res.setID(rsGen.getLong("idRel"));
            res.guardat();
            return res;
        } catch (SQLException esql) {
            throw new excepcio(excepcio.LLEU, "No s'ha pogut crear persona " + esql.getMessage(), 0);
        }
    }

    protected void altaEspecifica(objecteID oID) throws Errors.excepcio {
        Object[] oLlista = new Object[2];
        relEsdevenimentTasca res = (relEsdevenimentTasca) oID;
        oLlista[0] = new Long(res.getIdTasca());
        oLlista[1] = new Long(res.getIdEsdeveniment());
        executaConsultaP(oLlista, ALTA);
    }

    protected void modificacioEspecifica(objecteID oID) throws Errors.excepcio {
        Object[] oLlista = new Object[3];
        relEsdevenimentTasca res = (relEsdevenimentTasca) oID;
        oLlista[0] = new Long(res.getIdEsdeveniment());
        oLlista[1] = new Long(res.getIdTasca());
        oLlista[2] = new Long(res.getID());
        executaConsultaP(oLlista, MODIFICACIOID);
    }
}
