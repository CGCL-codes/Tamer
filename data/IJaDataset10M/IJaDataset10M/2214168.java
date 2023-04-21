package com.foursoft.fourever.objectmodel;

import java.util.Arrays;
import java.util.Iterator;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.foursoft.fourever.objectmodel.exception.InstanceEvolutionNotSupportedException;
import com.foursoft.fourever.objectmodel.exception.NamingException;
import com.foursoft.fourever.objectmodel.exception.NoSuchPathException;
import com.foursoft.fourever.objectmodel.exception.TypeMismatchException;

/**
 *
 */
@SuppressWarnings("all")
public class ObjectModelTest extends TestCase {

    private ClassPathXmlApplicationContext ctx = null;

    private ObjectModel om = null;

    private ObjectModelManager omm = null;

    /**
	 * @param name
	 *            Name of Testcase
	 */
    public ObjectModelTest(String name) {
        super(name);
    }

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ctx = new ClassPathXmlApplicationContext("objectmodel/beans.xml");
        omm = (ObjectModelManager) ctx.getBean("objectmodelmanager");
        om = omm.createObjectModel();
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    @Override
    protected void tearDown() throws Exception {
        omm.removeObjectModel(om);
        ctx.close();
        super.tearDown();
    }

    /**
	 * test createEntityType
	 * 
	 * @throws NamingException
	 */
    public void testCreateEntityType() throws NamingException {
        EntityType et = om.createEntityType("testType", "testTypeDescription");
        assertNotNull(et);
    }

    /**
	 * test createSimpleBinding
	 * 
	 * @throws NamingException
	 * @throws InstanceEvolutionNotSupportedException
	 * @throws TypeMismatchException
	 */
    public void testCreateSimpleBinding() throws NamingException, InstanceEvolutionNotSupportedException, TypeMismatchException {
        SimpleType st = (SimpleType) om.getTypeByName(StringType.TYPE_NAME);
        EntityType et = om.createEntityType("testType1", "testTypeDescription1");
        CompositeBinding sb = om.createSimpleCompositeBinding("sb", true, et, "test", null, st);
        assertNotNull(sb);
    }

    /**
	 * test createComplexBinding
	 * 
	 * @throws NamingException
	 * @throws InstanceEvolutionNotSupportedException
	 * @throws TypeMismatchException
	 */
    public void testCreateComplexBinding() throws NamingException, InstanceEvolutionNotSupportedException, TypeMismatchException {
        EntityType et = om.createEntityType("testType2", "testTypeDescription2");
        EntityType bound = om.createEntityType("testType3", "testTypeDescription3");
        CompositeBinding cb = om.createCompositeBinding("cb", 0, Integer.MAX_VALUE, et, "test", null, bound);
        assertNotNull(cb);
    }

    /**
	 * test removeInstance
	 * 
	 * @throws NamingException
	 */
    public void testRemoveInstance() throws NamingException {
        EntityType et = om.createEntityType("testType4", "testTypeDescription4");
        Instance inst = et.createInstance(false);
        assertNotNull(inst);
        Iterator it = et.getInstances();
        assertNotNull(it);
        assertTrue(it.hasNext());
        inst.remove();
        it = et.getInstances();
        assertNotNull(it);
        assertFalse(it.hasNext());
    }

    /**
	 * test createStringEnumerationType
	 * 
	 * @throws NamingException
	 */
    public void testCreateStringEnumerationType() throws NamingException {
        String[] c = { "red", "yellow", "green" };
        Iterator<String> col = Arrays.asList(c).iterator();
        StringEnumerationType set = om.createStringEnumerationType("colours", "traffic light", col);
        assertNotNull(set);
    }

    /**
	 * test findAllTypes
	 */
    public void testFindAllTypes() {
        Iterator all = om.getTypes();
        assertNotNull(all);
        assertTrue(all.hasNext());
    }

    /**
	 * test findRootLinks
	 * 
	 * @throws TypeMismatchException
	 * @throws NamingException
	 * @throws InstanceEvolutionNotSupportedException
	 */
    public void testFindRootLinks() throws TypeMismatchException, NamingException, InstanceEvolutionNotSupportedException {
        Iterator rl = om.getRootLinks();
        assertNotNull(rl);
        EntityType bound = om.createEntityType("testType7", "testTypeDescription7");
        CompositeBinding cb = om.createCompositeBinding("cb", 0, Integer.MAX_VALUE, null, "test", null, bound);
        assertNotNull(cb);
        rl = om.getRootLinks();
        assertTrue(rl.hasNext());
        assertTrue(((Link) rl.next()).getBinding().equals(cb));
        assertFalse(rl.hasNext());
    }

    /**
	 * test findType
	 * 
	 * @throws NamingException
	 */
    public void testFindType() throws NamingException {
        om.createEntityType("testType5", "testTypeDescription5");
        Type found = om.getTypeByName("testType5");
        assertNotNull(found);
        assertEquals("testType5", found.getTypeName());
        assertEquals("testTypeDescription5", found.getDescription());
    }

    /**
	 * test exception for duplicate type names
	 */
    public void testNamingException() {
        try {
            om.createEntityType("testType6", "testTypeDescription6");
            om.createEntityType("testType6", "testTypeDescription6");
        } catch (NamingException e) {
            return;
        }
        fail("Expected NamingException");
    }

    /**
	 * test InstanceEvolutionNotSupportedException
	 * 
	 * @throws NamingException
	 */
    public void testInstanceEvolutionNotSupportedException() throws NamingException {
        EntityType et = om.createEntityType("testType2", "testTypeDescription2");
        et.createInstance(false);
        EntityType bound = om.createEntityType("testType3", "testTypeDescription3");
        try {
            om.createCompositeBinding("cb", 0, Integer.MAX_VALUE, et, "test", null, bound);
        } catch (InstanceEvolutionNotSupportedException e) {
            return;
        } catch (Exception e) {
            fail("Unexpected exception");
        }
        fail("Expected InstanceEvolutionNotSupportedException");
    }

    /**
	 * Model:
	 * 
	 * a +------------+ b +---------+ e +---------+ --------| A |--+-------| B
	 * |----------| E | +------------+ | +---------+ +---------+ | | | c
	 * +---------+ +-------| C | | +---------+ | | Simple: | d +---------+
	 * +-------| D | +---------+
	 * 
	 * 
	 */
    public void testGetTypeByPath() {
        EntityType typeA = null;
        EntityType typeB = null;
        EntityType typeC = null;
        StringType typeD = null;
        EntityType typeE = null;
        try {
            typeA = om.createEntityType("A", "noDescription");
            typeB = om.createEntityType("B", "noDescription");
            typeC = om.createEntityType("C", "noDescription");
            typeD = (StringType) om.getTypeByName("String");
            typeE = om.createEntityType("D", "noDescription");
            om.createCompositeBinding("a", 0, 999, null, "test", null, typeA);
            om.createCompositeBinding("b", 0, 999, typeA, "test", null, typeB);
            om.createCompositeBinding("c", 0, 999, typeA, "test", null, typeC);
            om.createSimpleCompositeBinding("d", false, typeA, "test", (StringInstance) typeD.createInstance(false), typeD);
            om.createCompositeBinding("e", 0, 999, typeB, "test", null, typeE);
            assertTrue(om.getTypeByPath("/a") == typeA);
            assertTrue(om.getTypeByPath("/a/b") == typeB);
            assertTrue(om.getTypeByPath("/a/b/e") == typeE);
            assertTrue(om.getTypeByPath("/a/c") == typeC);
            assertTrue(om.getTypeByPath("/a/d") == typeD);
            assertFalse(om.getTypeByPath("/a/b") == typeC);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
        try {
            om.getTypeByPath("/d");
            fail();
        } catch (NoSuchPathException e) {
            assertTrue(true);
        }
    }
}
