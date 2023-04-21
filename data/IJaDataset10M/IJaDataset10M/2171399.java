package javango.contrib.hibernate;

import java.io.Serializable;
import java.util.Map;
import javango.db.AbstractManagers;
import javango.db.ManagedBy;
import javango.db.Manager;
import javango.db.ManagerException;
import javango.db.Managers;
import javango.db.QuerySet;
import junit.framework.TestCase;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;

public class ManagerTest extends TestCase {

    public static class MyManager implements Manager<MyModel> {

        public QuerySet<MyModel> filterByProperty(String arg0, String arg1) throws ManagerException {
            return null;
        }

        public QuerySet<MyModel> all() throws ManagerException {
            return null;
        }

        public void delete(MyModel object) throws ManagerException {
        }

        public QuerySet<MyModel> filter(Map<String, Object> params) throws ManagerException {
            return null;
        }

        public QuerySet<MyModel> filter(Object params) throws ManagerException {
            return null;
        }

        public QuerySet<MyModel> filter(String property, Object value) throws ManagerException {
            return null;
        }

        public MyModel get(Serializable pk) throws ManagerException {
            return null;
        }

        public Serializable getPk(MyModel object) throws ManagerException {
            return null;
        }

        public Class[] getPkClass() throws ManagerException {
            return null;
        }

        public String getPkProperty() throws ManagerException {
            return null;
        }

        public MyModel save(MyModel object) throws ManagerException {
            return null;
        }
    }

    @ManagedBy(MyManager.class)
    public static class MyModel {
    }

    public static class MyOtherModel {
    }

    public void testCustomManager() throws Exception {
        Injector injector = Guice.createInjector(new HibernateModule());
        Managers managers = injector.getInstance(Managers.class);
        Manager<? extends MyModel> manager = managers.forClass(MyModel.class);
        assertEquals(manager.getClass(), MyManager.class);
    }

    public void testDefaultManager() throws Exception {
        Injector injector = Guice.createInjector(new HibernateModule());
        Managers managers = injector.getInstance(Managers.class);
        Manager<? extends MyOtherModel> manager = managers.forClass(MyOtherModel.class);
        assertEquals(manager.getClass(), HibernateManager.class);
    }
}
