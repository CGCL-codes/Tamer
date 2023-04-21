package net.sf.brightside.dentalwizard.metamodel.beans;

import static org.easymock.EasyMock.createStrictMock;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;
import java.util.Date;
import java.util.LinkedList;
import net.sf.brightside.dentalwizard.core.beans.BaseBeanTest;
import net.sf.brightside.dentalwizard.datatype.Address;
import net.sf.brightside.dentalwizard.enums.StaffType;
import net.sf.brightside.dentalwizard.metamodel.Paycheck;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class StaffMemberBeanTest extends BaseBeanTest {

    private StaffMemberBean staffMemberBeanUnderTest;

    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        staffMemberBeanUnderTest = createUnderTest();
        staffMemberBeanUnderTest.setPaychecks(new LinkedList<Paycheck>());
    }

    protected StaffMemberBean createUnderTest() {
        return new StaffMemberBean();
    }

    @Test
    public void testFirstName() {
        String firstName = "first";
        assertNull(staffMemberBeanUnderTest.getFirstName());
        staffMemberBeanUnderTest.setFirstName(firstName);
        assertEquals(firstName, staffMemberBeanUnderTest.getFirstName());
    }

    @Test
    public void testFirstNameSetNull() {
        String firstName = "first";
        staffMemberBeanUnderTest.setFirstName(firstName);
        assertEquals(firstName, staffMemberBeanUnderTest.getFirstName());
        staffMemberBeanUnderTest.setFirstName(null);
        assertNull(staffMemberBeanUnderTest.getFirstName());
    }

    @Test
    public void testLastName() {
        String lastName = "last";
        assertNull(staffMemberBeanUnderTest.getLastName());
        staffMemberBeanUnderTest.setLastName(lastName);
        assertEquals(lastName, staffMemberBeanUnderTest.getLastName());
    }

    @Test
    public void testLastNameSetNull() {
        String lastName = "last";
        staffMemberBeanUnderTest.setLastName(lastName);
        assertEquals(lastName, staffMemberBeanUnderTest.getLastName());
        staffMemberBeanUnderTest.setLastName(null);
        assertNull(staffMemberBeanUnderTest.getLastName());
    }

    @Test
    public void testAddress() {
        Address address = new Address();
        assertNotNull(staffMemberBeanUnderTest.getAddress());
        staffMemberBeanUnderTest.setAddress(address);
        assertEquals(address, staffMemberBeanUnderTest.getAddress());
    }

    @Test
    public void testAddressSetNull() {
        Address address = new Address();
        staffMemberBeanUnderTest.setAddress(address);
        assertEquals(address, staffMemberBeanUnderTest.getAddress());
        staffMemberBeanUnderTest.setAddress(null);
        assertNull(staffMemberBeanUnderTest.getAddress());
    }

    @Test
    public void testSsn() {
        String ssn = "12345";
        assertNull(staffMemberBeanUnderTest.getSsn());
        staffMemberBeanUnderTest.setSsn(ssn);
        assertEquals(ssn, staffMemberBeanUnderTest.getSsn());
    }

    @Test
    public void testSsnSetNull() {
        String ssn = "12345";
        staffMemberBeanUnderTest.setSsn(ssn);
        assertEquals(ssn, staffMemberBeanUnderTest.getSsn());
        staffMemberBeanUnderTest.setSsn(null);
        assertNull(staffMemberBeanUnderTest.getSsn());
    }

    @Test
    public void testDateofBirth() {
        Date dateOfBirth = new Date();
        assertNull(staffMemberBeanUnderTest.getDateOfBirth());
        staffMemberBeanUnderTest.setDateOfBirth(dateOfBirth);
        assertEquals(dateOfBirth, staffMemberBeanUnderTest.getDateOfBirth());
    }

    @Test
    public void testDateofBirthSetNull() {
        Date dateOfBirth = new Date();
        staffMemberBeanUnderTest.setDateOfBirth(dateOfBirth);
        assertEquals(dateOfBirth, staffMemberBeanUnderTest.getDateOfBirth());
        staffMemberBeanUnderTest.setDateOfBirth(null);
        assertNull(staffMemberBeanUnderTest.getDateOfBirth());
    }

    @Test
    public void testEmploymentDate() {
        Date employmentDate = new Date();
        assertNull(staffMemberBeanUnderTest.getEmploymentDate());
        staffMemberBeanUnderTest.setEmploymentDate(employmentDate);
        assertEquals(employmentDate, staffMemberBeanUnderTest.getEmploymentDate());
    }

    @Test
    public void testEmploymentDateSetNull() {
        Date employmentDate = new Date();
        staffMemberBeanUnderTest.setEmploymentDate(employmentDate);
        assertEquals(employmentDate, staffMemberBeanUnderTest.getEmploymentDate());
        staffMemberBeanUnderTest.setEmploymentDate(null);
        assertNull(staffMemberBeanUnderTest.getEmploymentDate());
    }

    @Test
    public void testTelephone() {
        String telephone = "123456";
        assertNull(staffMemberBeanUnderTest.getTelephone());
        staffMemberBeanUnderTest.setTelephone(telephone);
        assertEquals(telephone, staffMemberBeanUnderTest.getTelephone());
    }

    @Test
    public void testTelephoneSetNull() {
        String telephone = "123456";
        staffMemberBeanUnderTest.setTelephone(telephone);
        assertEquals(telephone, staffMemberBeanUnderTest.getTelephone());
        staffMemberBeanUnderTest.setTelephone(null);
        assertNull(staffMemberBeanUnderTest.getTelephone());
    }

    @Test
    public void testType() {
        StaffType type = StaffType.DENTIST;
        assertNull(staffMemberBeanUnderTest.getType());
        staffMemberBeanUnderTest.setType(type);
        assertEquals(type, staffMemberBeanUnderTest.getType());
    }

    @Test
    public void testTypeSetNull() {
        StaffType type = StaffType.DENTIST;
        staffMemberBeanUnderTest.setType(type);
        assertEquals(type, staffMemberBeanUnderTest.getType());
        staffMemberBeanUnderTest.setType(null);
        assertNull(staffMemberBeanUnderTest.getType());
    }

    @Test
    public void testAccount() {
        UserBean account = new UserBean();
        assertNull(staffMemberBeanUnderTest.getAccount());
        staffMemberBeanUnderTest.setAccount(account);
        assertEquals(account, staffMemberBeanUnderTest.getAccount());
    }

    @Test
    public void testAccountSetNull() {
        UserBean account = new UserBean();
        staffMemberBeanUnderTest.setAccount(account);
        assertEquals(account, staffMemberBeanUnderTest.getAccount());
        staffMemberBeanUnderTest.setAccount(null);
        assertNull(staffMemberBeanUnderTest.getAccount());
    }

    @Test
    public void testPaychecksAssociation() {
        Paycheck paycheck = createStrictMock(Paycheck.class);
        assertFalse(staffMemberBeanUnderTest.getPaychecks().contains(paycheck));
        staffMemberBeanUnderTest.getPaychecks().add(paycheck);
        assertTrue(staffMemberBeanUnderTest.getPaychecks().contains(paycheck));
    }
}
