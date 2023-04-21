package pe.lamborgini.dao;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import pe.lamborgini.domain.mapping.Concesionario;
import pe.lamborgini.util.AppUtil;

/**
 *
 * @author Cesardl
 */
public class ConcesionarioDAO {

    public List<Concesionario> getConcesionarios() {
        Session session = AppUtil.getSessionFactory().openSession();
        List<Concesionario> concesionarios;
        try {
            Criteria c = session.createCriteria(Concesionario.class);
            concesionarios = new ArrayList<Concesionario>(c.list());
        } catch (Exception e) {
            System.err.println("ERROR: ConcesionarioDAO.getConcesionarios " + e);
            concesionarios = null;
        } finally {
            session.close();
        }
        return concesionarios;
    }
}
