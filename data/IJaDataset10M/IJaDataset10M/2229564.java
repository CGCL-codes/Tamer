package org.agilercp.demo.contacts.ui.internal.contacts;

import static org.easymock.EasyMock.createStrictMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.reset;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;
import java.util.Collection;
import org.agilercp.demo.contacts.IPersonRepository;
import org.agilercp.demo.contacts.Person;
import org.agilercp.demo.contacts.ui.internal.contacts.ContactsModel;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Heiko Seeberger
 */
public class ContactsModelTest {

    /**
     * The ContactsModelTest.java.
     */
    private static final Person PERSON = new Person();

    private final IPersonRepository personRepository = createStrictMock(IPersonRepository.class);

    @Before
    public void setUp() throws Exception {
        reset(personRepository);
    }

    @Test
    public void testAddPerson() {
        personRepository.addPerson(PERSON);
        replay(personRepository);
        final ContactsModel contactsModel = new ContactsModel(personRepository);
        contactsModel.addPerson(PERSON);
        verify(personRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContactsModel() {
        new ContactsModel(null);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testGetPersons() {
        final Collection<Person> persons = createStrictMock(Collection.class);
        expect(personRepository.getAllPersons()).andReturn(persons);
        replay(personRepository);
        final ContactsModel contactsModel = new ContactsModel(personRepository);
        assertSame(persons, contactsModel.getPersons());
        verify(personRepository);
    }
}
