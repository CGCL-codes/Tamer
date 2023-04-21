package es.cea;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import com.enjava.discografica.model.Cantante;
import com.enjava.discografica.model.Disco;
import com.enjava.discografica.model.UsuarioDisco;

@Stateless
public class DiscograficaDAO implements DiscograficaDAORemote {

    @PersistenceContext(unitName = "discografica")
    EntityManager entityManager;

    public UsuarioDisco registra(String nombre, String clave) {
        Query query = entityManager.createQuery("FROM UsuarioDisco ud WHERE ud.nombre=:nombrepp AND ud.clave=:clavepp");
        query.setParameter("nombrepp", nombre);
        query.setParameter("clavepp", clave);
        try {
            Object resultadoConsulta = query.getSingleResult();
            return (UsuarioDisco) resultadoConsulta;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Cantante busca(String nombre) {
        Query query = entityManager.createQuery("FROM Cancion nombreCantante WHERE nombreCantante.nombre=:nombrep");
        query.setParameter("nombrep", nombre);
        Object resultadoConsulta = query.getSingleResult();
        return (Cantante) resultadoConsulta;
    }

    @Override
    public List<Disco> buscaDisco(String nombre) {
        Query query = entityManager.createQuery("FROM Disco nombreDisco WHERE nombreDisco.nombre LIKE concat(:nombreD,'%')");
        query.setParameter("nombreD", nombre);
        try {
            Object resultadoConsulta = query.getResultList();
            return (List<Disco>) resultadoConsulta;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Disco> buscaDiscoCantante(String nombre, String nombreCantante) {
        Query query = entityManager.createQuery("from Disco nombreDisco " + "where nombreDisco.nombre=:nombreD " + "and nombreDisco.cantante.nombre=:nombreC");
        query.setParameter("nombreD", nombre);
        query.setParameter("nombreC", nombreCantante);
        Object resultadoConsulta = query.getResultList();
        return (List<Disco>) resultadoConsulta;
    }

    @Override
    public Disco getDisco(Long id) {
        return entityManager.find(Disco.class, new Long(id));
    }

    @Override
    public void setDisco(Disco disco) {
        entityManager.persist(disco);
    }

    @Override
    public List<Disco> getDiscos() {
        return entityManager.createQuery("from Disco").getResultList();
    }
}
