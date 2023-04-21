package modelo.manager;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import modelo.manager.exceptions.NonexistentEntityException;
import modelo.manager.exceptions.PreexistingEntityException;
import modelo.persistencia.Juego;
import modelo.persistencia.JuegoPK;

/**
 * La clase <code>JuegoJpaController</code> a traves de sus metodos se comunica con la base
 * de datos para realizar operaciones sobre un objeto Juego
 *
 * @author Gema Diaz, Elizabeth Vargas
 * @version 1.0
 */
public class JuegoJpaController {

    /**
 * Contructor de la clase <code>JuegoJpaController</code>
 * inicializa las variables globales del sistema
 */
    public JuegoJpaController() {
        emf = Persistence.createEntityManagerFactory("SapiensPU");
    }

    private EntityManagerFactory emf = null;

    /**
 * Retorna la entidad que hace referencia a una relacion de la base de datos
 * @return entidad que permite manejar las relaciones de la base de datos
 */
    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    /**
 * Crea un nuevo juego y lo registra en la base de datos
 * @param juego objeto juego
 * @exception Exception si se produce algun error durante el proceso de insercion
 */
    public void create(Juego juego) throws PreexistingEntityException, Exception {
        if (juego.getJuegoPK() == null) {
            juego.setJuegoPK(new JuegoPK());
        }
        juego.getJuegoPK().setIdA(juego.getAsignatura().getIdA());
        juego.getJuegoPK().setIdE(juego.getEstudiante().getIdE());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(juego);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findJuego(juego.getJuegoPK()) != null) {
                throw new PreexistingEntityException("Juego " + juego + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
 * Modifica un juego y actualiza la base de datos
 * @param juego objeto juego
 * @exception Exception si se produce algun error durante la modificacion del juego
 */
    public void edit(Juego juego) throws NonexistentEntityException, Exception {
        juego.getJuegoPK().setIdA(juego.getAsignatura().getIdA());
        juego.getJuegoPK().setIdE(juego.getEstudiante().getIdE());
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            juego = em.merge(juego);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                JuegoPK id = juego.getJuegoPK();
                if (findJuego(id) == null) {
                    throw new NonexistentEntityException("The juego with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
 * Elimina un juego y actualiza la base de datos
 * @param id identificador del juego
 * @exception NonexistentEntityException si el juego no existe en la base de datos
 * @exception IllegalOrphanException si se produce algun error durante el proceso de eliminacion
 */
    public void destroy(JuegoPK id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Juego juego;
            try {
                juego = em.getReference(Juego.class, id);
                juego.getJuegoPK();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The juego with id " + id + " no longer exists.", enfe);
            }
            em.remove(juego);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Juego> findJuegoEntities() {
        return findJuegoEntities(true, -1, -1);
    }

    public List<Juego> findJuegoEntities(int maxResults, int firstResult) {
        return findJuegoEntities(false, maxResults, firstResult);
    }

    private List<Juego> findJuegoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("select object(o) from Juego as o");
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    /**
 *Retorna el objeto juego a partir de su identificador
 * @param id identificador del juego
 * @return objeto juego
 */
    public Juego findJuego(JuegoPK id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Juego.class, id);
        } finally {
            em.close();
        }
    }

    /**
 *Eliminar un objeto juego por el codigo del estudiante
 * @param codigoEstudiante codigo del estudiante
 */
    public void eliminarJuegoPorEstudiante(String codigoEstudiante) {
        EntityManager em = null;
        int numFilas = 0;
        EstudianteJpaController estJpaC = new EstudianteJpaController();
        String idEstudiante = estJpaC.findIdEstudiante(codigoEstudiante);
        System.out.println(idEstudiante);
        String consulta = "DELETE FROM Juego " + "WHERE id_e=" + "'" + idEstudiante + "'";
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.createNativeQuery(consulta);
            System.out.println("nuemro de filas eliminadas: " + em.createNativeQuery(consulta).executeUpdate());
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    /**
 *Retorna un numero que representa la cantidad de juegos que se encuentran registrados en el sistema
 * @return numero de juegos registrados en el sistema
 */
    public int getJuegoCount() {
        EntityManager em = getEntityManager();
        try {
            return ((Long) em.createQuery("select count(o) from Juego as o").getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
}
