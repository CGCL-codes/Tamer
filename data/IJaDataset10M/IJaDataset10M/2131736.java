package dsb.mbc.odbc;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import dsb.mbc.model.Administration;
import dsb.mbc.model.AdministrationFactory;
import dsb.mbc.model.PaymentCondition;

public class PaymentConditionDAOTest {

    private static Administration administration;

    private PaymentConditionDAO dao;

    @BeforeClass
    public static void setUpBeforeClass() {
        administration = AdministrationFactory.openDSBAdministration(2006);
    }

    @AfterClass
    public static void tearDownAfterClass() {
    }

    @Before
    public void setUp() {
        this.dao = new PaymentConditionDAO(administration);
    }

    @After
    public void tearDown() {
        this.dao = null;
    }

    @Test
    public void testGetAllPaymentConditions() {
        List<PaymentCondition> list = this.dao.getAll();
        for (PaymentCondition pc : list) {
            System.out.println(pc);
            System.out.println(pc.getID());
            System.out.println(pc.getDescription());
        }
    }

    @Test
    public void f() {
    }
}
