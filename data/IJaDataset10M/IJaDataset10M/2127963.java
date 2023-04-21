package org.cmsuite2.business.validator;

import it.ec.commons.security.spring.jpa.model.account.Account;
import it.ec.commons.web.ValidateException;
import java.util.Date;
import org.cmsuite2.model.country.Country;
import org.cmsuite2.model.customer.Customer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/context/spring-context.xml" })
public class CustomerValidatorTest {

    @Autowired
    private CustomerValidator customerValidator;

    private Customer customer = new Customer();

    @Before
    public void setUp() {
        customer.setId(1);
        customer.setAddress("aaa");
        customer.setCity("aaa");
        customer.setFirstName("aaa");
        customer.setFiscalCode("aaa");
        customer.setGender("aaa");
        customer.setInsDate(new Date());
        customer.setLastName("aaa");
        customer.setMail("aaa@mail.it");
        customer.setMiddleName("aaa");
        customer.setName("aaa");
        customer.setPhone("3339995356");
        customer.setVatCode("aaa");
        customer.setZipCode("aaa");
        Account account = new Account();
        account.setUsername("username");
        account.setPassword("password@1");
        account.setPasswordConfirm("password@1");
        customer.setAccount(account);
        Country country = new Country();
        customer.setCountry(country);
    }

    @Test
    public void validateOk() {
        ValidateException ex = null;
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoNull() {
        ValidateException ex = null;
        try {
            customerValidator.validateStep1(null);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoNameTooLong() {
        ValidateException ex = null;
        char[] chars = new char[256];
        customer.setName(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoFirstNameEmpty() {
        ValidateException ex = null;
        customer.setFirstName(null);
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoFirstNameTooLong() {
        ValidateException ex = null;
        char[] chars = new char[101];
        customer.setFirstName(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoMiddleNameTooLong() {
        ValidateException ex = null;
        char[] chars = new char[101];
        customer.setMiddleName(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoLastNameEmpty() {
        ValidateException ex = null;
        customer.setLastName(null);
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoLastNameTooLong() {
        ValidateException ex = null;
        char[] chars = new char[101];
        customer.setLastName(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoVatTooLong() {
        ValidateException ex = null;
        char[] chars = new char[51];
        customer.setVatCode(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoAddressEmpty() {
        ValidateException ex = null;
        customer.setAddress(null);
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoAddressTooLong() {
        ValidateException ex = null;
        char[] chars = new char[256];
        customer.setAddress(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoCityEmpty() {
        ValidateException ex = null;
        customer.setCity(null);
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoCityTooLong() {
        ValidateException ex = null;
        char[] chars = new char[101];
        customer.setCity(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoCountryNull() {
        ValidateException ex = null;
        customer.setCountry(null);
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoZipCodeEmpty() {
        ValidateException ex = null;
        customer.setZipCode(null);
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoZipCodeTooLong() {
        ValidateException ex = null;
        char[] chars = new char[51];
        customer.setZipCode(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoMailTooLong() {
        ValidateException ex = null;
        char[] chars = new char[256];
        customer.setMail(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoMailNotValid() {
        ValidateException ex = null;
        customer.setMail("cane");
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoPhoneTooLong() {
        ValidateException ex = null;
        char[] chars = new char[17];
        customer.setPhone(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoUsernameEmpty() {
        ValidateException ex = null;
        customer.getAccount().setUsername(null);
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoUsernameTooShort() {
        ValidateException ex = null;
        char[] chars = new char[5];
        customer.getAccount().setUsername(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoUsernameTooLong() {
        ValidateException ex = null;
        char[] chars = new char[65];
        customer.getAccount().setUsername(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoPasswordEmpty() {
        ValidateException ex = null;
        customer.getAccount().setPassword(null);
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoPasswordTooLong() {
        ValidateException ex = null;
        char[] chars = new char[65];
        customer.getAccount().setPassword(new String(chars));
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @Test
    public void validateKoPasswordMatch() {
        ValidateException ex = null;
        customer.getAccount().setPasswordConfirm("wrong");
        try {
            customerValidator.validateStep1(customer);
        } catch (ValidateException e) {
            ex = e;
        }
        Assert.assertNotNull(String.valueOf(ex), ex);
    }

    @After
    public void tearDown() {
    }

    public CustomerValidator getCustomerValidator() {
        return customerValidator;
    }

    public void setCustomerValidator(CustomerValidator customerValidator) {
        this.customerValidator = customerValidator;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
