package dk.mirasola.systemtraining.server;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class ObjectifyUtil {

    public interface ObjectifyCallback<T> {

        T doInTransaction(Objectify ofy);
    }

    public static <T> T doTransactional(ObjectifyCallback<T> callback) {
        Objectify ofy = ObjectifyService.beginTransaction();
        try {
            T t = callback.doInTransaction(ofy);
            ofy.getTxn().commit();
            return t;
        } finally {
            if (ofy.getTxn().isActive()) {
                ofy.getTxn().rollback();
            }
        }
    }

    public static String entityKeyToString(Object entity, Objectify ofy) {
        return ofy.getFactory().keyToString(ofy.getFactory().<Object>getKey(entity));
    }

    private ObjectifyUtil() {
    }
}
