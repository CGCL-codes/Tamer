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
public class PaymentFilterBeanTest {

    PaymentFilterBean instance;

    public PaymentFilterBeanTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        instance = new PaymentFilterBean();
        instance.setUserBean(UserBeanFactory.constructUserBean());
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of reset method, of class PaymentFilterBean.
     */
    @Test
    public void testReset_0args() {
        System.out.println("reset");
        String result = instance.reset();
        assertNull(result);
        assertNull(instance.getPayments());
    }

    /**
     * Test of reset method, of class PaymentFilterBean.
     */
    @Test
    public void testReset_ActionEvent() {
        System.out.println("reset");
        ActionEvent e = null;
        instance.reset(e);
        assertNotNull(instance.getFilter());
    }

    /**
     * Test of search method, of class PaymentFilterBean.
     */
    @Test
    public void testSearch() {
        System.out.println("search");
        String result = instance.search();
        assertNull(result);
        assertNotNull(instance.getPayments());
    }
}
