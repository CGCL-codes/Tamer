package com.ecs.etrade.unittest;

import java.util.Calendar;
import java.util.Date;
import junit.framework.TestCase;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import com.ecs.etrade.bo.cust.CustomerManagerImpl;
import com.ecs.etrade.da.Account;
import com.ecs.etrade.da.BankDetails;
import com.ecs.etrade.da.BankDetailsDAO;
import com.ecs.etrade.da.BankDetailsId;
import com.ecs.etrade.da.ContactDetails;
import com.ecs.etrade.da.Cust;
import com.ecs.etrade.daextn.AccountDAOExtn;
import com.ecs.etrade.daextn.ContactDetailsDAOExtn;
import com.ecs.etrade.daextn.CustDAOExtn;

/**
 * @author Alok Ranjan
 *
 */
public class TestCustomerManager extends TestCase {

    private CustomerManagerImpl custManager;

    public void setUp() throws Exception {
        BeanFactory factory = new XmlBeanFactory(new FileSystemResource("src/applicationContext.xml"));
        custManager = (CustomerManagerImpl) factory.getBean("customerManagerService");
        CustDAOExtn custDAO = (CustDAOExtn) factory.getBean("CustDAOExtn");
        ContactDetailsDAOExtn contactDetailsDAO = (ContactDetailsDAOExtn) factory.getBean("ContactDetailsDAOExtn");
        AccountDAOExtn accountDAO = (AccountDAOExtn) factory.getBean("AccountDAOExtn");
        BankDetailsDAO bankDetailDAO = (BankDetailsDAO) factory.getBean("BankDetailsDAO");
        custManager.setCustDAO(custDAO);
        custManager.setContactDetailsDAO(contactDetailsDAO);
        custManager.setAccountDAO(accountDAO);
        custManager.setBankDetailDAO(bankDetailDAO);
    }

    public void testAddCustomer() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(1980, 1, 1);
            Cust customer = new Cust("001", "Mr", "Sreekanth", "K", new Date(), "Ajit", cal.getTime());
            ContactDetails contactDetail = new ContactDetails();
            contactDetail.setAddress1("Somajiguda");
            contactDetail.setAddress2("Hyderabad");
            contactDetail.setPhone1("9848473539");
            contactDetail.setPhone1("9441955210");
            Account[] acctArray = new Account[1];
            ContactDetails contact = new ContactDetails();
            contact.setContactDetailsId(new Integer(5));
            BankDetails bankDetail = new BankDetails(new BankDetailsId("12", "12"), contact, "Citi", "Somajiguda");
            acctArray[0] = new Account("0001", customer, bankDetail, new Date(), "Y", "96117", "96117", 10.0f, 30);
            custManager.createCustomer(customer, contactDetail, acctArray);
            assertTrue(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            assertTrue(false);
        }
    }
}
