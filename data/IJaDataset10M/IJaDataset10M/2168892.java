package br.ufpa.spider.mplan.persistence;

import br.ufpa.spider.mplan.model.InformationNecessity;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Bernardo
 */
public class InformationNecessityDAO {

    private InformationNecessityDAO() {
    }

    ;

    private static List<InformationNecessity> listWithAllInformationNecessity = null;

    public static void addInformationNecessity(InformationNecessity in) {
        GenericDAO.create(in);
        updateListWithAllInformationNecessity();
    }

    public static void updateInformationNecessity(InformationNecessity in) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("spider_mplan");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        InformationNecessity persistedUser = em.find(InformationNecessity.class, in.getId());
        persistedUser.setName(in.getName());
        persistedUser.setPriority(in.getPriority());
        persistedUser.setDerivateType(in.getDerivateType());
        em.getTransaction().commit();
        em.close();
        emf.close();
        updateListWithAllInformationNecessity();
    }

    public static void updateListWithAllInformationNecessity() {
        List list = GenericDAO.getAll("informartionnecessity");
        listWithAllInformationNecessity = new ArrayList<InformationNecessity>();
        for (int i = 0; i < list.size(); ++i) {
            listWithAllInformationNecessity.add((InformationNecessity) list.get(i));
        }
    }

    public static List<InformationNecessity> searchInformartionNecessity(String keyword, Long id) {
        List<InformationNecessity> listOfNecessity = InformationNecessityDAO.getAllInByOrganization(id);
        List<InformationNecessity> listOfOrganization = new ArrayList<InformationNecessity>();
        String userName, name;
        for (InformationNecessity user : listOfNecessity) {
            userName = user.getName().toLowerCase();
            name = user.getName().toLowerCase();
            keyword = keyword.toLowerCase();
            if (name.contains(keyword) || name.contains(keyword)) {
                listOfOrganization.add(user);
            }
        }
        return listOfOrganization;
    }

    public static List<InformationNecessity> getAll() {
        List lisfOfEntities = null;
        List<InformationNecessity> in = new ArrayList<InformationNecessity>();
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("spider_mplan");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        lisfOfEntities = em.createQuery("select a from informartionnecessity a").getResultList();
        em.getTransaction().commit();
        em.close();
        emf.close();
        for (int i = 0; i < lisfOfEntities.size(); ++i) {
            in.add((InformationNecessity) lisfOfEntities.get(i));
        }
        return in;
    }

    public static List<InformationNecessity> getAllInByOrganization(Long id) {
        List lisfOfEntities = null;
        List<InformationNecessity> in = new ArrayList<InformationNecessity>();
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("spider_mplan");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        lisfOfEntities = em.createQuery("select a from informartionnecessity a where a.organization=" + id).getResultList();
        em.getTransaction().commit();
        em.close();
        emf.close();
        for (int i = 0; i < lisfOfEntities.size(); ++i) {
            in.add((InformationNecessity) lisfOfEntities.get(i));
        }
        return in;
    }

    public static List<InformationNecessity> getAllInformationNecessity() {
        if (InformationNecessityDAO.listWithAllInformationNecessity == null) {
            updateListWithAllInformationNecessity();
        }
        List<InformationNecessity> listWithAllOrganization = new ArrayList<InformationNecessity>();
        for (InformationNecessity in : InformationNecessityDAO.listWithAllInformationNecessity) {
            listWithAllOrganization.add(in);
        }
        return listWithAllOrganization;
    }

    public static InformationNecessity getInformationNecessityById(long id) {
        if (listWithAllInformationNecessity == null) {
            updateListWithAllInformationNecessity();
        }
        for (InformationNecessity in : listWithAllInformationNecessity) {
            if (in.getId() == id) {
                return in;
            }
        }
        return null;
    }

    public static void deleteProject(InformationNecessity in) {
        EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("spider_mplan");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        InformationNecessity persistedUser = em.find(InformationNecessity.class, in.getId());
        em.remove(persistedUser);
        em.getTransaction().commit();
        em.close();
        emf.close();
        updateListWithAllInformationNecessity();
    }
}
