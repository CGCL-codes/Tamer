package apdol.model;

import apdol.entity.Output;
import apdol.model.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 *
 * @author Accio
 */
public class DaftarOutput {

    public DaftarOutput() {
        emf = Persistence.createEntityManagerFactory("ApdolPU");
    }

    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public List<Output> getOutput() {
        List<Output> output = new ArrayList<Output>();
        EntityManager em = getEntityManager();
        try {
            Query q = em.createQuery("SELECT a FROM Output AS a");
            output = q.getResultList();
        } finally {
            em.close();
        }
        return output;
    }

    public Output findOutput(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Output.class, id);
        } finally {
            em.close();
        }
    }

    public List<Output> findOutputByKode(String kode) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createNamedQuery("Output.findByKode");
            query.setParameter("kodeOutput", "%" + kode + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public void rekamOutput(Output output) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(output);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Output output) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            output = em.merge(output);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Output output;
            try {
                output = em.getReference(Output.class, id);
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("Output belum dipilih.", enfe);
            }
            em.remove(output);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public boolean isKodeExist(String kode) {
        DaftarOutput daftarOutput = new DaftarOutput();
        List<Output> listOutput = daftarOutput.getOutput();
        Iterator<Output> iterator = listOutput.iterator();
        Output tes = new Output();
        while (iterator.hasNext()) {
            tes = iterator.next();
            if (kode.equalsIgnoreCase(tes.getKodeOutput())) {
                return true;
            }
        }
        return false;
    }

    public boolean isOutputExist(String nama) {
        DaftarOutput daftarOutput = new DaftarOutput();
        List<Output> listOutput = daftarOutput.getOutput();
        Iterator<Output> iterator = listOutput.iterator();
        Output tes = new Output();
        while (iterator.hasNext()) {
            tes = iterator.next();
            if (nama.equalsIgnoreCase(tes.getNamaOutput())) {
                return true;
            }
        }
        return false;
    }
}
