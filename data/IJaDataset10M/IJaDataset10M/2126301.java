package pattern.data_access_object;

import java.sql.Connection;
import componente.agenda.model.AgendaDAO;
import componente.agenda.model.AgendaDAOODBC;
import componente.cid.model.CidDAO;
import componente.cid.model.CidDAOODBC;
import componente.cirurgia.model.CirurgiaDAO;
import componente.cirurgia.model.CirurgiaDAOODBC;
import componente.cliente.model.*;
import componente.consulta.model.*;
import componente.convenio.model.ConvenioDAO;
import componente.convenio.model.ConvenioDAOODBC;
import componente.medico.model.MedicoDAO;
import componente.usuario.model.UsuarioDAO;

public class DAOFactoryODBC extends DAOFactory {

    /**
	 *Retorna um objeto para acessar os dados do produto.
	 *
	 *@return produto MySQL
	 */
    public ConsultaDAO getConsultaDAO() {
        return new ConsultaDAOODBC();
    }

    /**
	 *Retorna um objeto para acessar os dados do CID.
	 *
	 *@return produto MySQL
	 */
    public CidDAO getCidDAO() {
        return new CidDAOODBC();
    }

    /**
	 *Retorna um objeto para acessar os dados do cliente.
	 *
	 *@return cliente MySQL
	 */
    public ClienteDAO getClienteDAO() {
        return new ClienteDAOODBC();
    }

    /**
	 *Retorna uma conex&atilde;o com a base de dados MySQL.
	 *
	 *@return conex&atilde;o MySQL
	 */
    public static Connection createConnection() {
        Connection conn = null;
        String address = "jdbc:odbc:bd_doutor_um";
        String login = "";
        String senha = "";
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
            conn = java.sql.DriverManager.getConnection(address, login, senha);
            System.out.println("Conex�o com o ODBC ok");
        } catch (Exception ex) {
            System.out.println("Conex�o com o ODBC n�o ok");
        }
        return conn;
    }

    public ConvenioDAO getConvenioDAO() {
        return new ConvenioDAOODBC();
    }

    public UsuarioDAO getUsuarioDAO() {
        return null;
    }

    public MedicoDAO getMedicoDAO() {
        return null;
    }

    public CirurgiaDAO getCirurgiaDAO() {
        return new CirurgiaDAOODBC();
    }

    public AgendaDAO getAgendaDAO() {
        return new AgendaDAOODBC();
    }
}
