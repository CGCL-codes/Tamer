package es.cea.seamcea.session;

import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.Begin;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import com.enjava.discografica.model.Cantante;

@Stateful
@Name("listaCantantes")
@Scope(ScopeType.CONVERSATION)
public class listaCantantesBean implements listaCantantes {

    List<Cantante> lista;

    public List<Cantante> getLista() {
        return lista;
    }

    @PersistenceContext
    EntityManager entityManager;

    @Begin(join = true)
    public void carga() {
        Query createQuery = entityManager.createQuery("from Cantante");
        lista = createQuery.getResultList();
    }

    @Destroy
    @Remove
    public void destroy() {
    }
}
