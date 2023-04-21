package com.foursoft.fourever.objectmodel;

import java.util.Arrays;
import java.util.Iterator;
import junit.framework.TestCase;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import com.foursoft.fourever.objectmodel.exception.IllegalValueException;

/**
 * Test Case for StringEnumerationInstance
 * 
 */
@SuppressWarnings("all")
public class StringEnumerationInstanceTest extends TestCase {

    private ClassPathXmlApplicationContext ctx = null;

    private ObjectModelManager omm;

    private ObjectModel om;

    private StringEnumerationType st;

    private StringEnumerationInstance s;

    private String[] testvalues;

    /**
	 * @see junit.framework.TestCase#setUp()
	 */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ctx = new ClassPathXmlApplicationContext("objectmodel/beans.xml");
        omm = (ObjectModelManager) ctx.getBean("objectmodelmanager");
        testvalues = new String[] { "Anton", "Berta", "C�sar", "Dora", "Emil", "Friedrich", "Gustav", "Heinrich", "Ida", "Jakob", "Konrad", "Ludwig", "Martha", "Nordpol", "Oskar", "Paula", "Quelle", "Richard", "Siegfried", "Theodor", "Ulrich", "Viktor", "Wilhelm", "Xanthippe", "Ypsilon", "Zeppelin", "�rger", "�konom", "�bel" };
        om = omm.createObjectModel();
        st = om.createStringEnumerationType("Alphabet", "Alphabet des deutschen Fernmeldewesens", Arrays.asList(testvalues).iterator());
        s = (StringEnumerationInstance) st.createInstance(false);
    }

    /**
	 * @see junit.framework.TestCase#tearDown()
	 */
    @Override
    protected void tearDown() throws Exception {
        s.remove();
        omm.removeObjectModel(om);
        ctx.close();
        super.tearDown();
    }

    /**
	 * test setValue() & getValue()
	 */
    public void testSetGetValue() {
        int i;
        try {
            s.setValue(s.getValue());
        } catch (IllegalValueException e) {
            fail();
        }
        for (i = 0; i < testvalues.length; i++) {
            try {
                s.setValue(testvalues[i]);
            } catch (Exception e) {
                fail();
            }
            assertEquals(s.getValue(), testvalues[i]);
        }
    }

    /**
	 * test setValue() with failure
	 */
    public void setValueFail() {
        try {
            s.setValue("Schnitzelsemmel");
        } catch (IllegalValueException e) {
            return;
        }
        fail();
    }

    /**
	 * test setValueFromString() (inherited from SimpleInstance)
	 */
    public void testSetValueFromString() {
        int i;
        for (i = 0; i < testvalues.length; i++) {
            try {
                s.setValueFromString(testvalues[i]);
            } catch (Exception e) {
                fail();
            }
            assertEquals(s.getValue(), testvalues[i]);
        }
    }

    /**
	 * test setValueFromString() with failure (inherited from SimpleInstance)
	 */
    public void testSetValueFromStringFail() {
        try {
            s.setValueFromString("Hugo");
        } catch (IllegalValueException e) {
            return;
        }
        fail();
    }

    /**
	 * test getValueAsString() (inherited from SimpleInstance)
	 */
    public void testGetValueAsString() {
        int i;
        for (i = 0; i < testvalues.length; i++) {
            try {
                s.setValue(testvalues[i]);
            } catch (Exception e) {
                fail();
            }
            assertEquals(s.getValueAsString(), testvalues[i]);
        }
    }

    /**
	 * test getType() (inherited from Instance)
	 */
    public void testGetType() {
        assertSame(s.getType(), om.getTypeByName("Alphabet"));
    }

    /**
	 * test copy() (inherited from Instance)
	 */
    public void testCopy() {
        StringEnumerationInstance scopy;
        Iterator i;
        int precount = 0;
        int postcount = 0;
        for (i = st.getInstances(); i.hasNext(); i.next(), precount++) {
        }
        scopy = (StringEnumerationInstance) s.copy();
        assertEquals(s.getValue(), scopy.getValue());
        assertNotSame(s, scopy);
        assertSame(s.getType(), scopy.getType());
        for (i = st.getInstances(); i.hasNext(); i.next(), postcount++) {
        }
        assertEquals(precount + 1, postcount);
        scopy.remove();
    }

    /**
	 * test remove() (inherited from Instance)
	 */
    public void testRemove() {
        StringEnumerationInstance s2;
        Iterator i;
        int precount = 0;
        int postcount = 0;
        s2 = (StringEnumerationInstance) st.createInstance(true);
        for (i = st.getInstances(); i.hasNext(); i.next(), precount++) {
        }
        s2.remove();
        for (i = st.getInstances(); i.hasNext(); postcount++) {
            assertNotSame(i.next(), s2);
        }
        assertEquals(precount - 1, postcount);
    }
}
