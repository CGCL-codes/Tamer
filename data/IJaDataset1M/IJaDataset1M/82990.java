package gphoto.services.impl;

import gphoto.bo.Concerne;
import gphoto.dao.ConcerneDAO;
import gphoto.dao.impl.ConcerneDAOImpl;
import gphoto.services.ConcerneServices;
import java.sql.SQLException;
import java.util.List;

public class ConcerneServicesImpl implements ConcerneServices {

    private static ConcerneServices concerneServices = null;

    private ConcerneServicesImpl() {
    }

    public static ConcerneServices getInstance() {
        if (concerneServices == null) {
            concerneServices = new ConcerneServicesImpl();
        }
        return concerneServices;
    }

    public List<Concerne> getAllConcernes() throws SQLException {
        ConcerneDAO concerneDAO = ConcerneDAOImpl.getInstance();
        return concerneDAO.getAllConcernes();
    }

    /**
	 * Ajoute en BD le concerne pass� en param�tre
	 * @return Le concerne avec un identifiant
	 * @throws SQLException 
	 */
    public Concerne ajouterConcerne(Concerne c) throws SQLException {
        ConcerneDAO concerneDAO = ConcerneDAOImpl.getInstance();
        return concerneDAO.addConcerne(c);
    }

    public Concerne getConcerne(int id) throws SQLException {
        ConcerneDAO concerneDAO = ConcerneDAOImpl.getInstance();
        return concerneDAO.getConcerne(id);
    }
}
