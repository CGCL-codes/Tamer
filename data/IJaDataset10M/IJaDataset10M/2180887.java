package cz.cvut.phone.gui.bean.filter;

import cz.cvut.phone.test.factory.UserBeanFactory;
import javax.faces.event.ActionEvent;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Frantisek Hradil
 */
public class PersonFilterBeanTest {

    PersonFilterBean instance;

    public PersonFilterBeanTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new PersonFilterBean();
        instance.setUserBean(UserBeanFactory.constructUserBean());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of reset method, of class PersonFilterBean.
     */
    @Test
    public void testReset_0args() {
        System.out.println("reset");
        String result = instance.reset();
        assertNull(result);
        assertNull(instance.getPersons());
    }

    /**
     * Test of reset method, of class PersonFilterBean.
     */
    @Test
    public void testReset_ActionEvent() {
        System.out.println("reset");
        ActionEvent e = null;
        instance.reset(e);
        assertNotNull(instance.getFilterPerson());
    }

    /**
     * Test of search method, of class PersonFilterBean.
     */
    @Test
    public void testSearch() {
        System.out.println("search");
        String result = instance.search();
        assertNotNull(instance.getPersons());
        assertNull(result);
    }
}
