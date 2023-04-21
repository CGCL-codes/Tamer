package org.gruposp2p.controldatosgob.servicio.jpa;

import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.gruposp2p.controldatosgob.modelo.ConjuntoDeDatos;
import org.gruposp2p.controldatosgob.modelo.ConjuntoDeDatosEtiqueta;
import org.gruposp2p.controldatosgob.servicio.ConjuntoDeDatosDAO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

/**
 *
 * @author jj
 */
@Repository("conjuntoDeDatosDAO")
public class JpaConjuntoDeDatosDAO implements ConjuntoDeDatosDAO {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public ConjuntoDeDatos encontrarPorId(Integer id) {
        return entityManager.find(ConjuntoDeDatos.class, id);
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public List<ConjuntoDeDatos> encontrarLista(Integer primerResultado, Integer numMaxResultados) {
        Query query = entityManager.createQuery("SELECT c FROM ConjuntoDeDatos c");
        query.setFirstResult(primerResultado.intValue());
        query.setMaxResults(numMaxResultados.intValue());
        List<ConjuntoDeDatos> conjuntosDeDatos = query.getResultList();
        return conjuntosDeDatos;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ConjuntoDeDatos salvar(ConjuntoDeDatos conjuntoDeDatos) {
        Collection<ConjuntoDeDatosEtiqueta> conjuntosDeDatosEtiqueta = conjuntoDeDatos.getConjuntoDeDatosEtiquetaCollection();
        if (conjuntosDeDatosEtiqueta != null) {
            for (ConjuntoDeDatosEtiqueta conjuntoDeDatosEtiqueta : conjuntosDeDatosEtiqueta) {
                conjuntoDeDatosEtiqueta.setConjuntoDeDatosid(conjuntoDeDatos);
            }
        }
        entityManager.persist(conjuntoDeDatos);
        entityManager.flush();
        return conjuntoDeDatos;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public ConjuntoDeDatos actualizar(ConjuntoDeDatos conjuntoDeDatos) {
        Collection<ConjuntoDeDatosEtiqueta> conjuntosDeDatosEtiqueta = conjuntoDeDatos.getConjuntoDeDatosEtiquetaCollection();
        if (conjuntosDeDatosEtiqueta != null) {
            for (ConjuntoDeDatosEtiqueta conjuntoDeDatosEtiqueta : conjuntosDeDatosEtiqueta) {
                conjuntoDeDatosEtiqueta.setConjuntoDeDatosid(conjuntoDeDatos);
            }
        }
        entityManager.merge(conjuntoDeDatos);
        entityManager.flush();
        return conjuntoDeDatos;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public boolean borrar(Integer conjuntoDeDatosId) {
        ConjuntoDeDatos conjuntoDeDatos = entityManager.getReference(ConjuntoDeDatos.class, conjuntoDeDatosId);
        if (conjuntoDeDatos == null) return false;
        entityManager.remove(conjuntoDeDatos);
        entityManager.flush();
        return true;
    }
}
