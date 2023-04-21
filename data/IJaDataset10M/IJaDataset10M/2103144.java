package es.cea.seamcea.session;

import java.io.Serializable;
import java.util.List;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.jboss.seam.annotations.Destroy;
import org.jboss.seam.annotations.Name;

@Stateful
@Name("discosCantantes")
public class DiscosCantantesBean implements Serializable, DiscosCantantes {

    private static final long serialVersionUID = 1L;

    String nombreDisco;

    String nombreCantante;

    List resultados;

    @PersistenceContext
    EntityManager entityManager;

    public void buscar() {
        Query createQuery = entityManager.createQuery("from Cantante c Disco d where c.nombre=:cantante AND d.nombre=:disco");
        createQuery.setParameter("disco", nombreDisco);
        createQuery.setParameter("cantante", nombreCantante);
        resultados = createQuery.getResultList();
    }

    public String getNombreDisco() {
        return nombreDisco;
    }

    public void setNombreDisco(String nombreDisco) {
        this.nombreDisco = nombreDisco;
    }

    public String getNombreCantante() {
        return nombreCantante;
    }

    public void setNombreCantante(String nombreCantante) {
        this.nombreCantante = nombreCantante;
    }

    public List getResultados() {
        return resultados;
    }

    public void setResultados(List resultados) {
        this.resultados = resultados;
    }

    @Destroy
    @Remove
    public void destroy() {
    }
}
