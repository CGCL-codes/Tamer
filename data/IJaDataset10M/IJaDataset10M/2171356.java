package pe.edu.cibertec.reservas.dao.sqlserver;

import pe.edu.cibertec.reservas.dao.factory.DAOFactory;
import pe.edu.cibertec.reservas.dao.interfaces.CanchaDAO;
import pe.edu.cibertec.reservas.dao.interfaces.ClienteDAO;
import pe.edu.cibertec.reservas.dao.interfaces.UsuarioDAO;
import pe.edu.cibertec.reservas.dao.interfaces.VentaDAO;

/**
 *
 * @author Instructor
 */
public class SQLServerDAOFactory extends DAOFactory {

    @Override
    public UsuarioDAO getUsuarioDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ClienteDAO getClienteDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public VentaDAO getVentaDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CanchaDAO getCanchaDAO() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
