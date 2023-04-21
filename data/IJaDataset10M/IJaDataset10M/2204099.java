package Managers;

import Entidades.EInformefisio;
import Logica.InformeFisio;
import Principal.MainSofia;
import java.util.Iterator;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import javax.persistence.RollbackException;

/**
 *
 * @author Crisfalt
 */
public class ManagerInformeFisio {

    @PersistenceUnit
    private EntityManagerFactory managerFactory;

    private EntityManager entityManager;

    public ManagerInformeFisio() {
        managerFactory = MainSofia.managerFactory;
        entityManager = managerFactory.createEntityManager();
    }

    public void crearInformeFisio(final InformeFisio informe) throws RollbackException, EntityExistsException {
        entityManager.clear();
        EInformefisio entidadInforme = new EInformefisio();
        entidadInforme.setDescripcion(informe.getDescripcion());
        entidadInforme.setComentarioCoordinador("");
        EntityTransaction transaccion = entityManager.getTransaction();
        try {
            transaccion.begin();
            entityManager.persist(entidadInforme);
        } catch (EntityExistsException ex) {
            throw ex;
        } finally {
            transaccion.commit();
        }
        entityManager.clear();
    }

    public void eliminarInforme(final int id) {
        entityManager.clear();
        EInformefisio entidadInforme;
        EntityTransaction transaccion = entityManager.getTransaction();
        transaccion.begin();
        entidadInforme = entityManager.find(EInformefisio.class, id);
        entityManager.remove(entidadInforme);
        transaccion.commit();
    }

    public String obtenerComentario(final int id) {
        entityManager.clear();
        EntityTransaction transaccion = entityManager.getTransaction();
        transaccion.begin();
        List results = null;
        Query query = entityManager.createQuery("SELECT eInforme FROM EInformefisio eInforme WHERE eInforme.idInforme = :ParametroId");
        query.setParameter("ParametroId", id);
        results = query.getResultList();
        transaccion.commit();
        String comentario = "";
        if (!results.isEmpty()) {
            System.out.println("Leyendo " + results.size() + " comentarios");
            Iterator stIterator = results.iterator();
            while (stIterator.hasNext()) {
                EInformefisio entidadInformefi = (EInformefisio) stIterator.next();
                comentario = entidadInformefi.getComentarioCoordinador();
            }
        }
        entityManager.clear();
        return comentario;
    }

    public List obtenerComentariosRegistrados() {
        entityManager.clear();
        EntityTransaction transaccion = entityManager.getTransaction();
        transaccion.begin();
        List comentarios = null;
        Query query = entityManager.createQuery("SELECT eInforme FROM EInformefisio eInforme");
        comentarios = query.getResultList();
        transaccion.commit();
        if (!comentarios.isEmpty()) {
            System.out.println("Leyendo " + comentarios.size() + " comentarios");
            Iterator stIterator = comentarios.iterator();
            while (stIterator.hasNext()) {
                EInformefisio entidadInformefi = (EInformefisio) stIterator.next();
                System.out.println("id Informe " + entidadInformefi.getIdInforme());
                System.out.println("comentario Coordinador " + entidadInformefi.getComentarioCoordinador());
                System.out.println("descrpcion " + entidadInformefi.getDescripcion());
            }
        }
        entityManager.clear();
        return comentarios;
    }

    public void actualizarInformeFisio(final int id, final InformeFisio informe) {
        entityManager.clear();
        EInformefisio entidadInforme;
        EntityTransaction transaccion = entityManager.getTransaction();
        transaccion.begin();
        entidadInforme = entityManager.find(EInformefisio.class, id);
        transaccion.commit();
        if (informe != null) {
            entidadInforme.setDescripcion(informe.getDescripcion());
            entidadInforme.setComentarioCoordinador(informe.getComentarioCoordinador());
            transaccion.begin();
            entityManager.merge(entidadInforme);
            transaccion.commit();
        }
        entityManager.clear();
    }

    public InformeFisio obtenerInformeFisio(final int id) {
        entityManager.clear();
        EInformefisio entidadInforme;
        InformeFisio informeFisio = null;
        EntityTransaction transaccion = entityManager.getTransaction();
        transaccion.begin();
        entidadInforme = entityManager.find(EInformefisio.class, id);
        transaccion.commit();
        if (entidadInforme != null) {
            informeFisio = new InformeFisio();
            informeFisio.setComentarioCoordinador(entidadInforme.getComentarioCoordinador());
            informeFisio.setDescripcion(entidadInforme.getDescripcion());
        }
        entityManager.clear();
        return informeFisio;
    }
}
