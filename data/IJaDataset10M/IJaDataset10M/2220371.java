package dsb.bar.barkassa.persistence.model.verkoopgegevens;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.log4j.BasicConfigurator;
import dsb.bar.barkassav1.persistence.dao.verkoopgegevens.VerkoopgegevensSDAO;
import dsb.bar.barkassav1.persistence.dao.verkoopgegevens.VerkoopgegevensSDAOBean;
import dsb.bar.barkassav1.persistence.model.verkoopgegevens.VerkoopgegevensS;

public class VerkoopgegevensSTest {

    public static void main(String[] args) {
        BasicConfigurator.configure();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("barkassav1");
        VerkoopgegevensSDAO dao = new VerkoopgegevensSDAOBean();
        EntityManager em = emf.createEntityManager();
        dao.setEntityManager(em);
        List<VerkoopgegevensS> objects = dao.getAll();
        for (Object o : objects) System.out.println(o);
        dao = null;
        em.close();
        emf.close();
    }
}
