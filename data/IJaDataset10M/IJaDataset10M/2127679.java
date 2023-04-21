package com.rubecula.beanpot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.rubecula.beanpot.test.TestUtilities;

/**
 * @author Robin Hillyard
 * 
 */
public class BeanPotTest {

    /**
	 */
    @Before
    public void setUp() {
    }

    /**
	 */
    @SuppressWarnings("static-method")
    @After
    public void tearDown() {
        BeanPot.setValidate(false);
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot0() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans0.xml");
        BeanPot.setDebug(true);
        try {
            BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
            assertEquals("name", "test beans", BeanPot.getIdentifier());
            BeanPot.getInstance().run();
            fail("should throw exception in configure");
        } catch (final Exception e) {
            assertEquals("message", "no id defined for node: bean", e.getLocalizedMessage());
        }
        BeanPot.cleanup();
    }

    /**
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot00() {
        BeanPot.setValidate();
        try {
            BeanPot.setConfigurationByResource(BeanPot.class, "testBeans0.xml");
            fail("should throw exception in setConfiguration");
        } catch (final Exception e) {
            final URI uri = new File(USER_DIR + "/target/test-classes/com/rubecula/beanpot/testBeans0.xml").toURI();
            assertEquals("message", "setConfiguration(URL) configuration exception: " + uri, e.getLocalizedMessage());
        }
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot01() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans1.xml");
        BeanPot.setDebug(true);
        try {
            BeanPot.getInstance().run();
            fail("should throw exception in run");
        } catch (final RuntimeException e) {
            final String message = e.getLocalizedMessage();
            assertEquals("message", "Configuration exception (see log): thrown adding bean: bean #1", message);
            final Throwable cause = e.getCause();
            assertNotNull(cause);
            assertEquals("cause", "addBean(): no class defined for bean: bean #1", cause.getLocalizedMessage());
        }
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot02() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans2.xml");
        BeanPot.setDebug(true);
        try {
            BeanPot.getInstance().run();
            fail("should throw exception in run");
        } catch (final RuntimeException e) {
            final String message = e.getLocalizedMessage();
            assertEquals("message", "Configuration exception (see log): thrown adding bean: bean #1", message);
            final Throwable cause = e.getCause();
            assertNotNull(cause);
            assertEquals("cause", "unable to interpret com.rubecula.beanpot.BeanPotTest.Bean as class name", cause.getLocalizedMessage());
        }
        BeanPot.cleanup();
    }

    /**
	 * 
	 * TODO consider using StringWriterWriter and checking that whole output is
	 * as expected.
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "boxing", "static-method" })
    @Test
    public void testPot03() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans3.xml");
        BeanPot.setDebug(true);
        assertEquals("count (initial)", 0, BeanPot.count());
        final Configuration configuration = BeanPot.getInstance().getConfiguration();
        assertTrue("keys", configuration.getKeys().hasNext());
        BeanPot.getInstance().run();
        assertEquals("count (final)", 2, BeanPot.count());
        final String[] keys = BeanPot.getBeanKeys().toArray(new String[0]);
        assertEquals("count (beans)", 2, keys.length);
        for (final String key : keys) {
            final Object bean = BeanPot.getInstance().getBean(key);
            if (bean instanceof Messagey) {
                if (((Messagey) bean).getId() == 101) {
                    assertEquals("message", "_Hello_", ((Messagey) bean).getMessage().toString());
                } else fail("incorrect id for Messagey object: " + ((Messagey) bean).getId());
            } else if (bean instanceof Fooey) {
                if (((Fooey) bean).getId() == 1) {
                    assertEquals("property1", 1, ((Fooey) bean).getProperty1().intValue());
                } else fail("incorrect id for Fooey object: " + ((Fooey) bean).getId());
            } else fail("invalid bean: " + bean);
            assertEquals("bean", bean, BeanPot.removeBean(key));
        }
        assertEquals("count (terminal)", 0, BeanPot.count());
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot04() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans4.xml");
        BeanPot.setDebug(true);
        assertEquals("count (initial)", 0, BeanPot.count());
        final Configuration configuration = BeanPot.getInstance().getConfiguration();
        assertTrue("keys", configuration.getKeys().hasNext());
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        assertEquals("count (final)", 2, BeanPot.count());
        final Object nonBean = BeanPot.getInstance().getBean("non-existent");
        assertNull(nonBean);
        BeanPot.getInstance().run();
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot05() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans5.xml");
        BeanPot.setDebug(true);
        assertEquals("count (initial)", 0, BeanPot.count());
        final BeanPot bp = BeanPot.getInstance();
        final Configuration configuration = bp.getConfiguration();
        assertTrue("keys", configuration.getKeys().hasNext());
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        assertEquals("count (final)", 5, BeanPot.count());
        final List<String> userConfigurationFiles = BeanPot.getBeanPot().getMap().getUserConfigurationFiles();
        assertEquals("user configuration files", 1, userConfigurationFiles.size());
        final String userConfigurationFile = userConfigurationFiles.get(0);
        assertEquals("user configuration file", USER_DIR + DELIMITER + "user.ini", userConfigurationFile);
        final Object nonBean = bp.getBean("non-existent");
        assertNull(nonBean);
        bp.run();
        final Fooey bean1 = (Fooey) bp.getBean("bean #1");
        final Integer property1 = bean1.getProperty1();
        final Double property2 = bean1.getProperty2();
        final long property3 = bean1.getProperty3();
        final boolean property4 = bean1.isProperty4();
        final float property5 = bean1.getProperty5();
        final byte property6 = bean1.getProperty6();
        final short property7 = bean1.getProperty7();
        final String property8 = bean1.getProperty8();
        final BigInteger property9 = bean1.getProperty9();
        final BigDecimal property10 = bean1.getProperty10();
        final Object property11 = bean1.getProperty11();
        assertEquals("Prop 1", Integer.valueOf(1), property1);
        assertEquals("Prop 2", Double.valueOf(2.718), property2);
        assertEquals("Prop 3", 9223372036854775807L, property3);
        assertTrue("Prop 4", property4);
        assertEquals("Prop 5", Float.valueOf("3.14").floatValue(), property5, 0.001);
        final byte b = 12;
        assertEquals("Prop 6", Byte.valueOf(b).byteValue(), property6);
        assertEquals("Prop 7", Short.valueOf("1").shortValue(), property7);
        assertEquals("Prop 8", "Life is Good", property8);
        assertEquals("Prop 9", new BigInteger("92233720368547758071"), property9);
        assertEquals("Prop 10", new BigDecimal("922337203685.47758071"), property10);
        assertEquals("Prop 11", new Double("3.1415927"), property11);
        final Bar bean2 = (Bar) bp.getBean("bean #2");
        assertEquals("Message", "_Hello World! from " + System.getProperty("user.name") + "_", bean2.getMessage().toString());
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot06() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans6.xml");
        BeanPot.setDebug(true);
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        assertEquals("count (final)", 9, BeanPot.count());
        final Friends friends = (Friends) BeanPot.getInstance().getBean("Friends");
        assertNotNull(friends);
        BeanPot.getInstance().run();
        final List<String> names = friends.getNames();
        assertEquals("friends", 3, names.size());
        assertEquals("friend1", "Kim", names.get(0));
        assertEquals("friend2", "Miranda", names.get(1));
        assertEquals("friend3", "Will", names.get(2));
        final IntArrayTest intArrayBean = (IntArrayTest) BeanPot.getInstance().getBean("IntArray");
        final int[] intArray = intArrayBean.getArray();
        assertEquals("array", 3, intArray.length);
        assertEquals("array[0]", 0, intArray[0]);
        final IntArrayTest intArrayBean2 = (IntArrayTest) BeanPot.getInstance().getBean("IntArray2");
        final int[] intArray2 = intArrayBean2.getArray();
        assertEquals("array", 3, intArray2.length);
        assertEquals("array[0]", 0, intArray2[0]);
        final IntegerArrayTest integerArrayBean = (IntegerArrayTest) BeanPot.getInstance().getBean("IntegerArray");
        final Integer[] integerArray = integerArrayBean.getArray();
        assertEquals("array", 3, integerArray.length);
        assertEquals("array[0]", 0, integerArray[0].intValue());
        final IntegerArrayTest integerArrayBean2 = (IntegerArrayTest) BeanPot.getInstance().getBean("IntegerArray2");
        final Integer[] integerArray2 = integerArrayBean2.getArray();
        assertEquals("array", 3, integerArray2.length);
        assertEquals("array[0]", 0, integerArray2[0].intValue());
        final DoubleArrayTest doubleArrayBean = (DoubleArrayTest) BeanPot.getInstance().getBean("DoubleArray");
        final double[] doubleArray = doubleArrayBean.getArray();
        assertEquals("array", 3, doubleArray.length);
        assertEquals("array[0]", 0.5, doubleArray[0], 0.00001);
        final DoubleArrayTest doubleArrayBean2 = (DoubleArrayTest) BeanPot.getInstance().getBean("DoubleArray2");
        final double[] doubleArray2 = doubleArrayBean2.getArray();
        assertEquals("array", 3, doubleArray2.length);
        assertEquals("array[0]", 0.5, doubleArray2[0], 0.00001);
        final DoubleObjArrayTest doubleObjArrayBean = (DoubleObjArrayTest) BeanPot.getInstance().getBean("DoubleObjArray");
        final Double[] doubleObjArray = doubleObjArrayBean.getArray();
        assertEquals("array", 3, doubleObjArray.length);
        assertEquals("array[0]", 0.5, doubleObjArray[0].doubleValue(), 0.00001);
        final DoubleObjArrayTest doubleObjArrayBean2 = (DoubleObjArrayTest) BeanPot.getInstance().getBean("DoubleObjArray2");
        final Double[] doubleObjArray2 = doubleObjArrayBean2.getArray();
        assertEquals("array", 3, doubleObjArray2.length);
        assertEquals("array[0]", 0.5, doubleObjArray2[0].doubleValue(), 0.00001);
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot07() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans7.xml");
        BeanPot.setDebug(true);
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        assertEquals("count (final)", 3, BeanPot.count());
        final Beans bean = (Beans) BeanPot.getInstance().getBean("Beans");
        assertNotNull(bean);
        BeanPot.getInstance().run();
        final Collection<Object> beans = bean.getBeanValues();
        assertEquals("beans", 2, beans.size());
        final Map<String, Object> map = bean.getBeans();
        assertEquals("id1", Integer.valueOf(1), ((Foo) map.get("1")).getId());
        assertEquals("id2", Long.valueOf(101), Long.valueOf(((Bar) map.get("2")).getId()));
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot08() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans8.xml");
        BeanPot.setDebug(true);
        final BeanPot bp = BeanPot.getInstance();
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        assertEquals("count (final)", 2, BeanPot.count());
        final StringWriterWriter writer = new StringWriterWriter();
        BeanPot.getBeanPot().showBeans(writer);
        final String showBeans = writer.getBuffer().toString();
        System.out.print(showBeans);
        assertEquals("showBeans", "Bean pot identifier: test beans" + NEWLINE + "Bean: Mapple (com.rubecula.beanpot.BeanPotTest.Mapple)" + NEWLINE + "    map={Melanism=1, NonMelanism=4}" + NEWLINE + "Bean: Mapple2 (com.rubecula.beanpot.BeanPotTest.Mapple)" + NEWLINE + "    map={Melanism=1, NonMelanism=4}" + NEWLINE, showBeans);
        final Mapple mapple = (Mapple) bp.getBean("Mapple");
        assertNotNull(mapple);
        bp.run();
        final Map<String, Integer> map = mapple.getMap();
        assertEquals("map", 2, map.size());
        for (final String key : map.keySet()) {
            System.out.println(key + "=" + map.get(key).intValue());
        }
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot09() {
        try {
            BeanPot.setConfigurationByResource(BeanPot.class, "testBeans9.xml");
            BeanPot.setDebug(true);
            BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
            fail("should throw exception because we don't support lists for constructor args");
        } catch (final Exception e) {
        }
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot10() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans10.xml");
        BeanPot.setDebug(true);
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        assertEquals("count (final)", 2, BeanPot.count());
        final Mapple mapple = (Mapple) BeanPot.getInstance().getBean("Mapple");
        assertNotNull(mapple);
        BeanPot.getInstance().run();
        assertEquals("map", 2, mapple.getMap().size());
        showMap(mapple.getMap());
        final Mapple2 mapple2 = (Mapple2) BeanPot.getInstance().getBean("Mapple2");
        assertEquals("map", 2, mapple2.getMap().size());
        showMap(mapple2.getMap());
        assertEquals("melanism", 1, mapple.getMap().get("Melanism").intValue());
        assertEquals("melanism", "_1-3_", mapple2.getMap().get("Melanism").getValue());
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot11() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans11.xml");
        BeanPot.setDebug(true);
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        assertEquals("count (final)", 3, BeanPot.count());
        final MapMap mapple = (MapMap) BeanPot.getInstance().getBean("MapMap");
        assertNotNull(mapple);
        BeanPot.getInstance().run();
        showMap(mapple.getMapMap());
        showMap(((MapMap) BeanPot.getInstance().getBean("MapMapSort")).getMapMap());
        showMap(((MapMap) BeanPot.getInstance().getBean("MapMapReverseSort")).getMapMap());
        BeanPot.cleanup();
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot12() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans12.xml");
        BeanPot.setDebug(true);
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        assertEquals("count (final)", 3, BeanPot.count());
        final SingletonClass singleton1 = (SingletonClass) BeanPot.getInstance().getBean("Singleton1");
        assertNotNull(singleton1);
        final SingletonClass singleton2 = (SingletonClass) BeanPot.getInstance().getBean("Singleton2");
        assertNotNull(singleton2);
        assertEquals("beans", singleton1, singleton2);
        final Bar bar = (Bar) BeanPot.getInstance().getBean("BarFactoryProduct");
        assertEquals("id", 1001, bar.getId());
        BeanPot.cleanup();
    }

    /**
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot13() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans13.xml");
        BeanPot.setDebug(true);
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        final Bar2 bar2 = (Bar2) BeanPot.getInstance().getBean("bean #1");
        assertNotNull(bar2);
        final Collection<Object> list2 = bar2.getList();
        assertEquals("list2", 2, list2.size());
        for (final Object object : list2) {
            if (object instanceof Integer) {
                final int intValue = ((Integer) object).intValue();
                assertTrue("list2[i]", intValue == 99 || intValue == 101);
            } else fail("list2[i] is not an Integer");
        }
        final Bar3 bar3 = (Bar3) BeanPot.getInstance().getBean("bean #2");
        assertNotNull(bar3);
        final Integer[] list3 = bar3.getList();
        assertEquals("list3", 2, list3.length);
        assertEquals("list3[0]", 99, list3[0].intValue());
    }

    /**
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot14() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans14.xml");
        BeanPot.setDebug(true);
        BeanPot.imposeBean("Applet", new MockApplet());
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        final MockApplet applet = (MockApplet) BeanPot.getInstance().getBean("Applet");
        assertNotNull(applet);
    }

    /**
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot15() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans15.xml");
        BeanPot.setDebug(true);
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        final Beans parentBean = (Beans) BeanPot.getInstance().getBean("Beans");
        final Collection<Object> beans = parentBean.getBeanValues();
        assertEquals("# beans", 3, beans.size());
        final Iterator<Object> iterator = beans.iterator();
        assertTrue(iterator.hasNext());
        assertEquals("bean", MockApplet.class, iterator.next().getClass());
        assertTrue(iterator.hasNext());
        assertEquals("bean", MockApplet.class, iterator.next().getClass());
        assertTrue(iterator.hasNext());
        assertEquals("bean", MockApplet.class, iterator.next().getClass());
        assertEquals("bean", BeanPot.getInstance().getBean("Bean2"), parentBean.getBean("myKey"));
        assertEquals("bean value", Integer.valueOf(99), parentBean.getBeanValue(BeanPot.getInstance().getBean("Bean2")));
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot16() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans16.xml");
        BeanPot.setDebug(true);
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        final Bar bean1 = (Bar) BeanPot.getInstance().getBean("bean #1");
        final SpecialProperty originalMessage = bean1.getMessage();
        System.out.println(originalMessage.getValue());
        BeanPot.setBeanProperty("bean #1.message", "Goodbye");
        final String goodbye = bean1.getMessage().getValue();
        assertEquals("", "_Goodbye_", goodbye);
        BeanPot.cleanup();
    }

    /**
	 * Test method for include files
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot17() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans17.xml");
        BeanPot.setDebug(true);
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        final Bar bean1 = (Bar) BeanPot.getInstance().getBean("bean #1");
        final SpecialProperty originalMessage = bean1.getMessage();
        System.out.println(originalMessage.getValue());
        BeanPot.setBeanProperty("bean #1.message", "Goodbye");
        final String goodbye = bean1.getMessage().getValue();
        assertEquals("", "_Goodbye_", goodbye);
        BeanPot.cleanup();
    }

    /**
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot18() throws Exception {
        final String[] args1 = new String[] { "-config", "testBeans18.xml", "beanpothelloworld.jar" };
        final int beans = BeanPot.doMain(new ArgVector(args1), BeanPot.class, true);
        assertEquals("beans", 1, beans);
        final String[] args2 = new String[] { "-config", "testBeans18.xml", "http://www.rubecula.com/beanpothelloworld.jar" };
        assertEquals("beans", 1, BeanPot.doMain(new ArgVector(args2), BeanPot.class, true));
    }

    /**
	 * 
	 * @throws Exception
	 */
    @SuppressWarnings({ "nls", "static-method" })
    @Test
    public void testPot19() throws Exception {
        BeanPot.setConfigurationByResource(BeanPot.class, "testBeans19.xml");
        BeanPot.setDebug(true);
        assertEquals("count (initial)", 0, BeanPot.count());
        final BeanPot bp = BeanPot.getInstance();
        final Configuration configuration = bp.getConfiguration();
        assertTrue("keys", configuration.getKeys().hasNext());
        BeanPot.configure(BeanPot.preConfigData, BeanPot.postConfigData);
        assertEquals("count (final)", 0, BeanPot.count());
        final List<String> userConfigurationFiles = BeanPot.getBeanPot().getMap().getUserConfigurationFiles();
        assertEquals("user configuration files", 1, userConfigurationFiles.size());
        final String userConfigurationFile = userConfigurationFiles.get(0);
        assertEquals("user configuration file", USER_DIR + DELIMITER + "user.ini", userConfigurationFile);
        final File file = new File(userConfigurationFile);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        final String line = reader.readLine();
        System.out.println("Contents of user INI file " + userConfigurationFile + " are:");
        System.out.println("    " + line);
        assertEquals("Hello=...", HELLO + "=" + HELLO_WORLD, line);
        final ConfigurationStringLookup stringLookup = new ConfigurationStringLookup();
        final String lookup = stringLookup.lookup(HELLO);
        final Object userConfigurations = TestUtilities.invokeMethod(ConfigurationStringLookup.class, stringLookup, "getUserConfigurations", new Class[0], new Object[0]);
        final int size = ((Collection<?>) userConfigurations).size();
        assertEquals("user configurations", 1, size);
        assertEquals(HELLO_WORLD, HELLO_WORLD, lookup);
        BeanPot.cleanup();
    }

    /**
	 * @param map
	 *            map to be output
	 */
    private static void showMap(final Map<String, ? extends Object> map) {
        for (final String key : map.keySet()) {
            final Object object = map.get(key);
            if (object instanceof Integer) {
                System.out.println(key + "= Integer " + ((Integer) object).intValue());
            } else System.out.println(key + "= " + object);
        }
    }

    public static class Bar implements Configurable, Messagey {

        /**
		 * @param id
		 * @throws NegativeValueException
		 */
        public Bar(final long id) throws NegativeValueException {
            super();
            this._id = id;
            if (id < 0) throw new NegativeValueException("" + id);
            System.out.println("MBeanTest class Bar instantiated with id: " + id);
        }

        /**
		 * @see com.rubecula.beanpot.Messagey#getId()
		 */
        @Override
        public long getId() {
            return this._id;
        }

        /**
		 * @see com.rubecula.beanpot.Messagey#getMessage()
		 */
        @Override
        public SpecialProperty getMessage() {
            return this.message;
        }

        /**
		 * @see com.rubecula.beanpot.Configurable#postConfigure(java.lang.Object)
		 */
        @Override
        public void postConfigure(final Object data) {
            System.out.println("post configure _bar");
        }

        /**
		 * @see com.rubecula.beanpot.Configurable#preConfigure(java.lang.Object)
		 */
        @Override
        public void preConfigure(final Object data) {
            System.out.println("pre configure _bar");
        }

        /**
		 * @param message
		 */
        public void setMessage(@SuppressWarnings("hiding") final SpecialProperty message) {
            System.out.println("message set to " + message);
            this.message = message;
        }

        private final long _id;

        private SpecialProperty message;
    }

    /**
	 * @author Robin Hillyard
	 * 
	 */
    public static class Bar2 {

        /**
		 * @param list
		 */
        public Bar2(final List<Object> list) {
            super();
            this._list = list;
            System.out.println("MBeanTest class Bar2 instantiated with id: " + list);
        }

        /**
		 * @return this list
		 */
        public Collection<Object> getList() {
            return this._list;
        }

        private final Collection<Object> _list;
    }

    /**
	 */
    public static class Bar3 {

        /**
		 * @param list
		 */
        public Bar3(final Integer[] list) {
            super();
            this._list = list;
            System.out.println("MBeanTest class instantiated with list of length: " + list.length);
        }

        /**
		 * @return the value of {@link #_list}
		 */
        public Integer[] getList() {
            return this._list;
        }

        private final Integer[] _list;
    }

    public static class BarFactory {

        public static Bar createBar(final long id) throws Exception {
            return new Bar(id);
        }
    }

    /**
	 *
	 */
    public static class Beans {

        /**
		 * 
		 */
        public Beans() {
            super();
            this._beans = new HashMap<String, Object>();
            this._beanValues = new HashMap<Object, Integer>();
        }

        /**
		 * @param e
		 * @return true if the bean was added
		 * @see java.util.List#add(java.lang.Object)
		 */
        public boolean addBean(final Object e) {
            return putKeyedBean(e.toString(), e);
        }

        public Object getBean(final String key) {
            return this._beans.get(key);
        }

        /**
		 * @return the beans
		 */
        public Map<String, Object> getBeans() {
            return this._beans;
        }

        /**
		 * @param bean
		 * @return
		 * @see java.util.Map#get(java.lang.Object)
		 */
        public Integer getBeanValue(final Object bean) {
            return this._beanValues.get(bean);
        }

        public Collection<Object> getBeanValues() {
            return this._beans.values();
        }

        /**
		 * @param key
		 * @param value
		 * @return
		 * @see java.util.Map#put(java.lang.Object, java.lang.Object)
		 */
        public Integer putBeanWithValue(final Object key, final Integer value) {
            return this._beanValues.put(key, value);
        }

        /**
		 * @param key
		 * @param e
		 * @return true if the bean was added
		 * @see java.util.List#add(java.lang.Object)
		 */
        public boolean putKeyedBean(final String key, final Object e) {
            return this._beans.put(key, e) == null;
        }

        /**
		 * @param m
		 * @see java.util.Map#putAll(java.util.Map)
		 */
        public void setBeans(final Map<? extends String, ? extends Object> m) {
            this._beans.clear();
            this._beans.putAll(m);
        }

        private final Map<String, Object> _beans;

        private final Map<Object, Integer> _beanValues;
    }

    public static class ConstructorTest {

        /**
		 * @param _short
		 *            XXX
		 * @param _float
		 *            XXX
		 * @param _boolean
		 *            XXX
		 * @param _long
		 *            XXX
		 * @param _double
		 *            XXX
		 * @param _int
		 *            XXX
		 * 
		 */
        @SuppressWarnings("unused")
        public ConstructorTest(final short _short, final float _float, final boolean _boolean, final long _long, final double _double, final int _int) {
            super();
        }
    }

    public static class DoubleArrayTest {

        /**
		 * 
		 */
        public DoubleArrayTest() {
            super();
            this._array = new double[0];
        }

        public double[] getArray() {
            return this._array;
        }

        public void setArray(final double[] array) {
            this._array = array;
        }

        private double[] _array;
    }

    public static class DoubleObjArrayTest {

        /**
		 * 
		 */
        public DoubleObjArrayTest() {
            super();
            this._array = new Double[0];
        }

        public Double[] getArray() {
            return this._array;
        }

        public void setArray(final Double[] array) {
            this._array = array;
        }

        private Double[] _array;
    }

    public static class Foo implements Runnable, Fooey, Configurable {

        public Foo(final Messagey bar) {
            this(bar, 99, null, 0.);
        }

        public Foo(final Messagey bar, final double value) {
            this(bar, 99, "Goodbye", value);
        }

        public Foo(final Messagey bar, final int id) {
            this(bar, id, "Goodbye", 3.1415927);
        }

        @SuppressWarnings("hiding")
        public Foo(final Messagey bar, final int id, final String property8, final double property2) {
            super();
            this._bar = bar;
            this._id = id;
            this.property8 = property8;
            this.property2 = property2;
            System.out.println("Foo class instantiated with _bar having id " + bar.getId() + " with message: " + bar.getMessage());
        }

        @Override
        public Messagey getBar() {
            return this._bar;
        }

        /**
		 * 
		 * @see com.rubecula.beanpot.Fooey#getId()
		 */
        @Override
        @SuppressWarnings("boxing")
        public Integer getId() {
            return this._id;
        }

        @Override
        @SuppressWarnings("boxing")
        public Integer getProperty1() {
            return this.property1;
        }

        @Override
        public BigDecimal getProperty10() {
            return this.property10;
        }

        @Override
        public Object getProperty11() {
            return this.property11;
        }

        @Override
        @SuppressWarnings("boxing")
        public Double getProperty2() {
            return this.property2;
        }

        @Override
        public long getProperty3() {
            return this.property3;
        }

        @Override
        public float getProperty5() {
            return this.property5;
        }

        @Override
        public byte getProperty6() {
            return this.property6;
        }

        @Override
        public short getProperty7() {
            return this.property7;
        }

        @Override
        public String getProperty8() {
            return this.property8;
        }

        @Override
        public BigInteger getProperty9() {
            return this.property9;
        }

        @Override
        public boolean isProperty4() {
            return this.property4;
        }

        @Override
        public void postConfigure(final Object data) {
        }

        @Override
        public void preConfigure(final Object data) {
        }

        /**
		 * 
		 * @see java.lang.Runnable#run()
		 */
        @Override
        public void run() {
            System.out.println("Running bean MBeanTest " + getId());
        }

        /**
		 * @param property1
		 */
        @SuppressWarnings("boxing")
        public void setProperty1(@SuppressWarnings("hiding") final Integer property1) {
            this.property1 = property1;
        }

        public void setProperty10(@SuppressWarnings("hiding") final BigDecimal property10) {
            this.property10 = property10;
        }

        /**
		 * @param property11
		 *            the property11 to set
		 */
        public void setProperty11(@SuppressWarnings("hiding") final Object property11) {
            this.property11 = property11;
        }

        /**
		 * @param property2
		 */
        @SuppressWarnings("boxing")
        public void setProperty2(@SuppressWarnings("hiding") final Double property2) {
            this.property2 = property2;
        }

        /**
		 * @param property3
		 */
        public void setProperty3(@SuppressWarnings("hiding") final long property3) {
            this.property3 = property3;
        }

        public void setProperty4(@SuppressWarnings("hiding") final boolean property4) {
            this.property4 = property4;
        }

        public void setProperty5(@SuppressWarnings("hiding") final float property5) {
            this.property5 = property5;
        }

        public void setProperty6(@SuppressWarnings("hiding") final byte property6) {
            this.property6 = property6;
        }

        public void setProperty7(@SuppressWarnings("hiding") final short property7) {
            this.property7 = property7;
        }

        public void setProperty8(@SuppressWarnings("hiding") final String property8) {
            this.property8 = property8;
        }

        public void setProperty9(@SuppressWarnings("hiding") final BigInteger property9) {
            this.property9 = property9;
        }

        private final int _id;

        private int property1;

        private double property2;

        private long property3;

        private boolean property4;

        private float property5;

        private byte property6;

        private short property7;

        private String property8;

        private BigInteger property9;

        private BigDecimal property10;

        private Object property11;

        private final Messagey _bar;
    }

    public static class Friends {

        /**
		 * 
		 */
        public Friends() {
            super();
            this._names = new ArrayList<String>();
        }

        public List<String> getNames() {
            return this._names;
        }

        public void setNames(final List<String> names) {
            this._names = names;
        }

        private List<String> _names;
    }

    public static class IntArrayTest {

        /**
		 * 
		 */
        public IntArrayTest() {
            super();
            this._array = new int[0];
        }

        public int[] getArray() {
            return this._array;
        }

        public void setArray(final int[] array) {
            this._array = array;
        }

        private int[] _array;
    }

    public static class IntegerArrayTest {

        /**
		 * 
		 */
        public IntegerArrayTest() {
            super();
            this._array = new Integer[0];
        }

        public Integer[] getArray() {
            return this._array;
        }

        public void setArray(final Integer[] array) {
            this._array = array;
        }

        private Integer[] _array;
    }

    /**
	 * @author Robin
	 * 
	 */
    public static class MapMap {

        /**
		 * 
		 */
        public MapMap() {
            super();
        }

        public Map<String, Map<String, Integer>> getMapMap() {
            return this.mapMap;
        }

        public void setMapMap(@SuppressWarnings("hiding") final Map<String, Map<String, Integer>> mapMap) {
            this.mapMap = mapMap;
        }

        private Map<String, Map<String, Integer>> mapMap;
    }

    public static class Mapple {

        /**
		 * 
		 */
        public Mapple() {
            super();
            this._map = new HashMap<String, Integer>();
        }

        public Map<String, Integer> getMap() {
            return this._map;
        }

        public void setMap(final Map<String, Integer> map) {
            this._map = map;
        }

        private Map<String, Integer> _map;
    }

    public static class Mapple2 {

        /**
		 * 
		 */
        public Mapple2() {
            super();
            this._map = new HashMap<String, SpecialProperty>();
        }

        public Map<String, SpecialProperty> getMap() {
            return this._map;
        }

        public void setMap(final Map<String, SpecialProperty> map) {
            this._map = map;
        }

        private Map<String, SpecialProperty> _map;
    }

    public static class MockApplet {

        public MockApplet() {
            super();
        }
    }

    /**
	 * @author Robin Hillyard
	 * 
	 */
    public static class NegativeValueException extends Exception {

        /**
		 * @param message
		 */
        public NegativeValueException(final String message) {
            super(message);
        }

        private static final long serialVersionUID = 1L;
    }

    public static class SingletonClass {

        private SingletonClass() {
            super();
        }

        public static SingletonClass getInstance() {
            if (instance == null) instance = new SingletonClass();
            return instance;
        }

        private static SingletonClass instance = null;
    }

    public static class SpecialProperty {

        public SpecialProperty(final String s) {
            super();
            this._value = s;
        }

        public String getValue() {
            return this._value;
        }

        @Override
        public String toString() {
            return getValue();
        }

        public static SpecialProperty valueOf(final String s) {
            return new SpecialProperty("_" + s + "_");
        }

        private final String _value;
    }

    public static class TestLogger extends BeanPotLogger {

        /**
		 * @param name
		 * @param pattern
		 *            the pattern for outputting the entries
		 */
        public TestLogger(final String name, final String pattern) {
            super(name, pattern);
        }
    }

    public static class TestWriter extends PrintWriter {

        /**
		 */
        public TestWriter() {
            super(System.out, true);
            System.out.println("TestWriter constructed");
        }

        @Override
        public void println(final String x) {
            super.println("TestWriter: " + x);
        }
    }

    public static class TestWriterSpecial extends TestWriter {

        /**
		 */
        public TestWriterSpecial() {
            super();
            System.out.println("TestWriterSpecial constructed");
        }

        @Override
        public void println(final String x) {
            super.println("TestWriterSpecial: " + x);
        }
    }

    private static final String HELLO = "Hello";

    private static final String HELLO_WORLD = "Hello World!";

    private static final String USER_DIR = System.getProperty("user.dir");

    private static final String NEWLINE = System.getProperty("line.separator");

    private static final String DELIMITER = System.getProperty("file.separator");
}
