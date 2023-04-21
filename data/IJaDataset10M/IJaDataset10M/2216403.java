package net.cepra.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import net.cepra.test.pool.ActionPool;
import net.cepra.test.pool.LocationPool;
import net.cepra.test.pool.ResourcePool;
import net.cepra.test.pool.TaskPool;
import net.cepra.timecard.domain.Activity;
import org.junit.Test;

public class TestActivity {

    @Test
    public void createActivity() {
        EntityManager em = PersistenceHelper.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        Activity act = new Activity();
        act.setResource(ResourcePool.HUT.get(em));
        act.setTask(TaskPool.Selector.get(em));
        act.setType(ActionPool.Impl.get(em));
        act.setLocation(LocationPool.Intern.get(em));
        Date d = new Date();
        act.setTimeFrom(d);
        Calendar cal = new GregorianCalendar();
        cal.setTime(d);
        cal.add(Calendar.HOUR, 1);
        act.setTimeTo(cal.getTime());
        act.setDescription("Modelle implemetiert.");
        em.persist(act);
        tx.commit();
        em.close();
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSearch() {
        EntityManager em = PersistenceHelper.createEntityManager();
        Query createQuery = em.createQuery("select act from " + Activity.class.getName() + " act where act.resource.id = ?");
        createQuery.setParameter(1, ResourcePool.HUT.get(em).getId());
        List<Activity> activities = createQuery.getResultList();
        for (Activity act : activities) {
            System.out.println(act.getTimeFrom() + " - " + act.getTimeTo() + " - " + act.getDuration() + " - " + act.getTaskPath() + " - " + act.getDescription());
        }
        em.close();
    }
}
