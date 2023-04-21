package ua.org.nuos.sdms.middle;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by IntelliJ IDEA.
 * User: dio
 * Date: 11.10.11
 * Time: 19:34
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractDaoTestCase<T> {

    protected static EntityManagerFactory emf;

    protected EntityManager em;

    protected T bean;

    protected abstract T createBean();

    @BeforeClass
    public static void createEntityManagerFactory() throws IOException {
        emf = Persistence.createEntityManagerFactory("testSDMS");
    }

    @AfterClass
    public static void closeEntityManagerFactory() {
        emf.close();
    }

    @Before
    public void beginTransaction() {
        em = emf.createEntityManager();
        em.getTransaction().begin();
        bean = createBean();
    }

    @After
    public void rollbackTransaction() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
        if (em.isOpen()) {
            em.close();
        }
        bean = null;
        em = null;
    }

    protected void injectPrivateField(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
            field.setAccessible(false);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    protected void firePrivateMethod(Object obj, String methodName) {
        try {
            Method method = obj.getClass().getDeclaredMethod(methodName);
            method.setAccessible(true);
            method.invoke(obj);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
