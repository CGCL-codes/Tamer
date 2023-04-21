package ranking.persistence.dao;

import java.util.List;
import org.hibernate.Query;
import ranking.model.Similitud;
import org.hibernate.Session;
import ranking.model.Usuario;

public class SimilitudDAO extends GenericDAO<Similitud> {

    public SimilitudDAO(Session session) {
        this.persistentClass = getDomainClass();
        this.session = session;
    }

    @Override
    protected Class<Similitud> getDomainClass() {
        return Similitud.class;
    }

    public Similitud obtenerSimilitudPorUsuarios(Usuario usuario1, Usuario usuario2) {
        final String consulta = "FROM Similitud S WHERE S.usuario1.nick = \'" + usuario1.getNick() + "\' AND S.usuario2.nick = \'" + usuario2.getNick() + "\'";
        Query query = (Query) session.createQuery(consulta);
        List<Similitud> similitudes = query.list();
        final String consulta2 = "FROM Similitud S WHERE S.usuario1.nick = \'" + usuario2.getNick() + "\' AND S.usuario2.nick = \'" + usuario1.getNick() + "\'";
        Query query2 = (Query) session.createQuery(consulta2);
        List<Similitud> similitudes2 = query2.list();
        similitudes.addAll(similitudes2);
        if (similitudes.size() > 0) {
            return similitudes.get(0);
        } else {
            return null;
        }
    }

    public Similitud obtenerSimilitudesDeUsuario(Usuario usuario) {
        final String consulta = "FROM Similitud S WHERE S.usuario1.nick = \'" + usuario.getNick() + "\'";
        Query query = (Query) session.createQuery(consulta);
        List<Similitud> similitudes = query.list();
        final String consulta2 = "FROM Similitud S WHERE S.usuario2.nick = \'" + usuario.getNick() + "\'";
        Query query2 = (Query) session.createQuery(consulta2);
        List<Similitud> similitudes2 = query2.list();
        similitudes.addAll(similitudes2);
        if (similitudes.size() > 0) {
            return similitudes.get(0);
        } else {
            return null;
        }
    }
}
