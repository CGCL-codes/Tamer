package guestbook;

import javax.jdo.PersistenceManager;

public class DBMethods {

    public static void saveInDB(Object obj, boolean flag) {
        if (flag == true) {
            PersistenceManager pm = PMF.get().getPersistenceManager();
            try {
                pm.makePersistent(obj);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                pm.close();
            }
        }
    }
}
