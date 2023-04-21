package com.jomijushi.fid.servicios;

import com.jomijushi.fid.dao.ComplejidadImpresionImplementsDAO;
import com.jomijushi.fid.dao.ComplejidadImpresionInterfazDAO;
import com.jomijushi.fid.dominio.FidComplejidadImpresion;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;

/**
 * Servicio de consultas y operaciones de datos para la entidad
 * ComplejidadImpresion.
 * @author Pedro Shiguihara J
 */
public class ComplejidadImpresionImplementsService extends JpaService<Integer, FidComplejidadImpresion> implements ComplejidadImpresionInterfazService {

    private ComplejidadImpresionInterfazDAO complejidadImpresionDAO = new ComplejidadImpresionImplementsDAO();

    /**
    * Dado un objeto FidComplejidadImpresion, se registra en la base de datos.
    * @param objComplejidadImpresion Objeto FidComplejidadImpresion que será registrada en la base de datos.
    */
    public void crearComplejidadImpresion(FidComplejidadImpresion objComplejidadImpresion) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        complejidadImpresionDAO.setEntityManager(em);
        complejidadImpresionDAO.persist(objComplejidadImpresion);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Dado un objeto FidComplejidadImpresion, se modifica en la base de datos.
     * @param objComplejidadImpresion Objeto FidComplejidadImpresion que será modificada en la base de datos.
     */
    public void editarComplejidadImpresion(FidComplejidadImpresion objComplejidadImpresion) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        complejidadImpresionDAO.setEntityManager(em);
        complejidadImpresionDAO.merge(objComplejidadImpresion);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Dado un objeto FidComplejidadImpresion, se elimina de la base de datos.
     * @param id ID de un objeto FidComplejidadImpresion que será eliminada de la base de datos.
     */
    public void eliminarComplejidadImpresion(int id) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        complejidadImpresionDAO.setEntityManager(em);
        FidComplejidadImpresion fidComplejidadImpresion = null;
        fidComplejidadImpresion = em.getReference(FidComplejidadImpresion.class, id);
        fidComplejidadImpresion.getId();
        complejidadImpresionDAO.remove(fidComplejidadImpresion);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Dado un id de un objeto FidComplejidadImpresion, se recupera de la base de datos.
     * @param id Identificador (id) del objeto FidComplejidadImpresion.
     * @return id ID de un objeto FidComplejidadImpresion que será recuperada de la base de datos.
     */
    public FidComplejidadImpresion buscarComplejidadImpresion(int id) {
        EntityManager em = emf.createEntityManager();
        try {
            complejidadImpresionDAO.setEntityManager(em);
            FidComplejidadImpresion frt = null;
            frt = complejidadImpresionDAO.findById(id);
            return frt;
        } finally {
            em.close();
        }
    }

    /**
     * Dado un conjunto de parámetros, se recuperará una lista de objetos FidComplejidadImpresion de la base de datos.
     * @param hayConsulta Determina si el parametro "jpql" viene con una consulta (true) o vacío (false).
     * @param jpql Consulta JPQL para retornar objetos de la base de datos.
     * @param esListarTodo Indicador para determinar si se hará un listado de todos los objetos encontrador (true)
     * o sólo de una parte, segmentada por los parametros "nRegs" y "regIni" (false)
     * @param nRegs Cantidad de registros a ser retornados de la base de datos de todos los encontrados.
     * @param regIni Número de registro recuperado desde donde parte el conteo de los nRegs a ser listados.
     * @return objComplejidadImpresion Objeto FidComplejidadImpresion que será recuperada de la base de datos.
     */
    public List<FidComplejidadImpresion> listarComplejidadesImpresion(boolean hayConsulta, String jpql, boolean esListarTodo, int nRegs, int regIni) {
        EntityManager em = emf.createEntityManager();
        try {
            complejidadImpresionDAO.setEntityManager(em);
            List<FidComplejidadImpresion> lst = complejidadImpresionDAO.listarComplejidadImpresion(hayConsulta, jpql, esListarTodo, nRegs, regIni);
            return lst;
        } finally {
            em.close();
        }
    }
}
