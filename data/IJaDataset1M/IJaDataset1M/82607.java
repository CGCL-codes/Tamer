package de.uni_leipzig.lots.server.persist;

import de.uni_leipzig.lots.common.exceptions.NoSuchAttributeException;
import de.uni_leipzig.lots.common.exceptions.NoSuchRegistrationTemplateException;
import de.uni_leipzig.lots.common.exceptions.RegistrationException;
import de.uni_leipzig.lots.common.objects.registration.Attribute;
import de.uni_leipzig.lots.common.objects.registration.RegistrationTemplate;
import org.hibernate.HibernateException;
import org.springframework.test.AbstractTransactionalSpringContextTests;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Alexander Kiel
 * @author Stephan Kühn
 * @version $Id: RegistrationTemplateRepositoryTest.java,v 1.3 2007/10/23 06:30:27 mai99bxd Exp $
 */
public class RegistrationTemplateRepositoryTest extends AbstractTransactionalSpringContextTests {

    static {
        Logger.getLogger("").setLevel(Level.OFF);
        Logger.getLogger("de.uni_leipzig.lots").setLevel(Level.SEVERE);
    }

    private RegistrationTemplateRepository registrationTemplateRepository;

    public void setRegistrationTemplateRepository(RegistrationTemplateRepository repository) {
        this.registrationTemplateRepository = repository;
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[] { "testContext.xml", "de/uni_leipzig/lots/server/persist/registrationTemplateRepositoryTestContext.xml", "de/uni_leipzig/lots/server/persist/repositoryTestData.xml" };
    }

    public void testLoadFail() {
        try {
            registrationTemplateRepository.load("identifier doesn't exists");
            fail();
        } catch (NoSuchRegistrationTemplateException e) {
        }
    }

    public void testLoadGuest() throws NoSuchRegistrationTemplateException {
        RegistrationTemplate guestRegistrationTemplate = registrationTemplateRepository.load("guest");
        assertEquals("identifier", "guest", guestRegistrationTemplate.getIdentifier());
        assertEquals("title", "Registrierung als Gast", guestRegistrationTemplate.getTitle());
        assertEquals("note", "Bitte füllen Sie folgende Angaben ordnungsgemäß aus.", guestRegistrationTemplate.getNote());
        Attribute[] attributes = new Attribute[] { Attribute.salutation, Attribute.firstName, Attribute.surname, Attribute.address, Attribute.tel, Attribute.cause };
        for (int i = 0; i < attributes.length; i++) {
            Attribute attribute = attributes[i];
            assertTrue("attribute: " + attribute, guestRegistrationTemplate.getVisibleAttributes().contains(attribute));
        }
    }

    public void testSave() throws NoSuchAttributeException, RegistrationException, NoSuchRegistrationTemplateException, HibernateException {
        RegistrationTemplate registrationTemplate = new RegistrationTemplate();
        registrationTemplate.setIdentifier("identifier");
        registrationTemplate.setLocked(false);
        registrationTemplate.setTitle("name");
        registrationTemplate.setNote("note");
        registrationTemplate.getVisibleAttributes().add(Attribute.firstName);
        registrationTemplate.getVisibleAttributes().add(Attribute.firstName);
        registrationTemplate.getRequiredAttributes().add(Attribute.surname);
        registrationTemplateRepository.save(registrationTemplate);
    }
}
