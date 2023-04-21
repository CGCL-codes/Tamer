package br.edu.unifei.VRaptor.dao;

import br.com.caelum.vraptor.ioc.Component;
import br.edu.unifei.VRaptor.modelo.Professor;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author Douglas Soares
 */
@Component
public class ProfessorDao {

    private EntityManager em;

    public ProfessorDao(EntityManager em) {
        this.em = em;
    }

    public void create(Professor professor) {
        em.getTransaction().begin();
        em.persist(professor);
        em.getTransaction().commit();
    }

    public void update(Professor professor) {
        em.getTransaction().begin();
        em.merge(professor);
        em.getTransaction().commit();
    }

    public void delete(String cpf) {
        em.getTransaction().begin();
        Professor c = findProfessor(cpf);
        em.remove(c);
        em.getTransaction().commit();
    }

    public Professor findProfessor(String cpf) {
        return (Professor) em.createQuery("from Professor where cpf = '" + cpf + "'").getSingleResult();
    }

    public List<Professor> findAllProfessor() {
        return em.createQuery("from Professor").getResultList();
    }

    public List<Professor> findPageProfessor(int max, int first) {
        Query query = em.createQuery("from Professor");
        query.setMaxResults(max);
        query.setFirstResult(first);
        return query.getResultList();
    }

    public long getCountProfessor() {
        Query query = em.createQuery("from Professor");
        return (Long) em.createQuery("select count(*) from Professor").getSingleResult();
    }
}
