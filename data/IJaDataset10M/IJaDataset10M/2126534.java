package pl.edu.agh.ssm.component.tests;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class MCTests {

    private static Logger logger = Logger.getLogger("MCTests");

    @BeforeClass
    public static void beforeClass() {
        BasicConfigurator.configure();
    }

    @Test
    public void testStaticMethod() {
        Class<?> cl = Integer.class;
        Method[] m = cl.getDeclaredMethods();
        for (Method m1 : m) {
            logger.debug(m1.getName());
            if (m1.getName().equals("parseInt") && m1.getParameterTypes().length == 1) {
                try {
                    logger.info(m1.invoke(Integer.class, "123"));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
