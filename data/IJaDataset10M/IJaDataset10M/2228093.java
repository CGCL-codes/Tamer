package JDODataManager;

import JDOModel.*;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

/**
 *
 * @author Administrator
 */
public class CourseCompletedDM {

    public static void addCourseCompleted(String userId, String courseCode) {
        PersistenceManager pm = PMF.get().getPersistenceManager();
        Transaction tx = pm.currentTransaction();
        try {
            tx.begin();
            CourseCompleted tmp = new CourseCompleted(userId, courseCode);
            pm.makePersistent(tmp);
            tx.commit();
        } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
        }
    }
}
