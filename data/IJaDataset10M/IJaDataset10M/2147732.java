package be.beeworks.moneypile.model;

import static org.junit.Assert.*;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class ContactTest {

    private static Validator validator;

    @BeforeClass
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidContact() {
        Contact contact = new Contact();
        contact.setAddress("Oudenaardsesteenweg 647");
        contact.setZip("9420");
        contact.setCity("Bambrugge");
        contact.setContactName("Jurgen Lust");
        contact.setCountry("BE");
        contact.setName("BeeWorks");
        Set<ConstraintViolation<Contact>> constraintViolations = validator.validate(contact);
        for (ConstraintViolation<Contact> constraintViolation : constraintViolations) {
            System.err.println(constraintViolation.getMessage());
        }
        assertEquals(0, constraintViolations.size());
    }
}
