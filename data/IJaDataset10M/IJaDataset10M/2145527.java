package cw.customermanagementmodul.pojo.manager;

import cw.boardingschoolmanagement.app.HibernateUtil;
import cw.boardingschoolmanagement.pojo.manager.AbstractPOJOManager;
import java.util.List;
import cw.customermanagementmodul.pojo.PostingCategory;
import java.util.logging.Logger;
import javax.persistence.NoResultException;

/**
 *
 * @author CreativeWorkers.at
 */
public class PostingCategoryManager extends AbstractPOJOManager<PostingCategory> {

    private static PostingCategoryManager instance;

    private static Logger logger = Logger.getLogger(PostingCategoryManager.class.getName());

    private PostingCategoryManager() {
    }

    public static PostingCategoryManager getInstance() {
        if (instance == null) {
            instance = new PostingCategoryManager();
        }
        return instance;
    }

    public int size() {
        return ((Long) HibernateUtil.getEntityManager().createQuery("SELECT COUNT(*) FROM PostingCategory").getResultList().iterator().next()).intValue();
    }

    @Override
    public List<PostingCategory> getAll() {
        return HibernateUtil.getEntityManager().createQuery("FROM PostingCategory").getResultList();
    }

    public List<PostingCategory> getAllUnlocked() {
        return HibernateUtil.getEntityManager().createQuery("FROM PostingCategory WHERE key=null OR key=''").getResultList();
    }

    public PostingCategory get(String key) {
        try {
            return (PostingCategory) HibernateUtil.getEntityManager().createQuery("FROM PostingCategory WHERE key='" + key + "'").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }
}
