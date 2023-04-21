package org.jaqlib.db;

import java.util.List;
import junit.framework.TestCase;
import org.jaqlib.db.java.typehandler.JavaTypeHandler;
import org.jaqlib.db.java.typehandler.JavaTypeHandlerRegistry;
import org.jaqlib.db.sql.typehandler.SqlTypeHandler;
import org.jaqlib.db.sql.typehandler.SqlTypeHandlerRegistry;

public class DefaultsTest extends TestCase {

    @Override
    public void tearDown() {
        Defaults.reset();
    }

    public void testGetBeanFactory() {
        assertNotNull(Defaults.getBeanFactory());
    }

    public void testSetBeanFactory_Null() {
        try {
            Defaults.setBeanFactory(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSetBeanFactory() {
        BeanFactory factory = new BeanFactory() {

            public <T> T newInstance(Class<T> beanClass) {
                return null;
            }
        };
        Defaults.setBeanFactory(factory);
        assertSame(factory, Defaults.getBeanFactory());
    }

    public void testGetMappingStrategy() {
        assertNotNull(Defaults.getMappingStrategy());
    }

    public void testSetMappingStrategy_Null() {
        try {
            Defaults.setMappingStrategy(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSetMappingStrategy() {
        MappingStrategy strategy = new MappingStrategy() {

            public List<AbstractMapping<?>> getMappings(Class<?> beanClass) {
                return null;
            }
        };
        Defaults.setMappingStrategy(strategy);
        assertSame(strategy, Defaults.getMappingStrategy());
    }

    public void testGetJavaTypeHandlerRegistry() {
        assertNotNull(Defaults.getJavaTypeHandlerRegistry());
    }

    public void testSetJavaTypeHandlerRegistry_Null() {
        try {
            Defaults.setJavaTypeHandlerRegistry(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSetJavaTypeHandlerRegistry() {
        JavaTypeHandlerRegistry registry = new JavaTypeHandlerRegistry() {

            public JavaTypeHandler getTypeHandler(Class<?> fieldType) {
                return null;
            }

            public void registerTypeHandler(Class<?> fieldType, JavaTypeHandler typeHandler) {
            }
        };
        Defaults.setJavaTypeHandlerRegistry(registry);
        assertSame(registry, Defaults.getJavaTypeHandlerRegistry());
    }

    public void testGetSqlTypeHandlerRegistry() {
        assertNotNull(Defaults.getSqlTypeHandlerRegistry());
    }

    public void testSetSqlTypeHandlerRegistry_Null() {
        try {
            Defaults.setSqlTypeHandlerRegistry(null);
            fail("Did not throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testSetSqlTypeHandlerRegistry() {
        SqlTypeHandlerRegistry registry = new SqlTypeHandlerRegistry() {

            public SqlTypeHandler getTypeHandler(int sqlDataType) {
                return null;
            }

            public void registerTypeHandler(int sqlDataType, SqlTypeHandler typeHandler) {
            }
        };
        Defaults.setSqlTypeHandlerRegistry(registry);
        assertSame(registry, Defaults.getSqlTypeHandlerRegistry());
    }

    public void testSetStrictColumnCheck() {
        boolean prevValue = Defaults.getStrictColumnCheck();
        try {
            assertFalse(Defaults.getStrictColumnCheck());
            Defaults.setStrictColumnCheck(true);
            assertTrue(Defaults.getStrictColumnCheck());
        } finally {
            Defaults.setStrictColumnCheck(prevValue);
        }
    }

    public void testGetStrictColumnCheck() {
        assertFalse(Defaults.getStrictColumnCheck());
    }
}
