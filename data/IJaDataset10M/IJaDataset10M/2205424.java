package es.randres.jdo.server.domain;

import java.util.List;
import javax.jdo.annotations.PrimaryKey;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Id;
import javax.persistence.Query;
import javax.persistence.Version;

@Entity
public class Activity {

    @SuppressWarnings("unchecked")
    public static List<Activity> findAll() {
        EntityManager em = entityManager();
        try {
            List<Activity> list = em.createQuery("select o from Activity o").getResultList();
            list.size();
            return list;
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("unchecked")
    public static PageList findPaged(int firstResult, int maxResults, String orderby, boolean isAscending) {
        EntityManager em = entityManager();
        try {
            String queryStr = "select o from Activity o ";
            if (!orderby.isEmpty()) {
                queryStr += String.format(" order by %s %s", orderby, (isAscending ? "ASC" : "DESC"));
            }
            Query query = em.createQuery(queryStr);
            List<Activity> resultList = query.getResultList();
            Integer counter = resultList.size();
            PageList pl = new PageList();
            int lastResult = Math.min(counter, firstResult + maxResults);
            pl.setList(resultList.subList(firstResult, lastResult));
            pl.setCounter(counter);
            return pl;
        } finally {
            em.close();
        }
    }

    public static Activity findActivity(String id) {
        if (id == null) {
            return null;
        }
        EntityManager em = entityManager();
        try {
            Activity payload = em.find(Activity.class, id);
            return payload;
        } finally {
            em.close();
        }
    }

    @Id
    @PrimaryKey
    @Column(name = "id")
    private String activityId;

    @Column(name = "description")
    private String description;

    @Version
    @Column(name = "version")
    private Integer version;

    public String getId() {
        return this.activityId;
    }

    public String getDescription() {
        return description;
    }

    public Integer getVersion() {
        return version;
    }

    public void setId(String activityId) {
        this.activityId = activityId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static final EntityManager entityManager() {
        return EMF.get().createEntityManager();
    }

    public void persist() {
        EntityManager em = entityManager();
        try {
            em.persist(this);
        } finally {
            em.close();
        }
    }

    public void remove() {
        EntityManager em = entityManager();
        try {
            Activity attached = em.find(Activity.class, this.activityId);
            em.remove(attached);
        } finally {
            em.close();
        }
    }
}
