package net.sourceforge.hyperrecord.model;

import java.lang.reflect.Constructor;
import com.googlecode.hyperrecord.dao.hibernate.GenericHibernateDao;
import net.sourceforge.hyperrecord.IntegrationTests;

public class PersonIntTests extends IntegrationTests {

    public void testSavePerson() throws Exception {
        logger.debug("Here's Terry's test message");
        Person person = new Person("John", "Doe");
        person.persistOrUpdate();
        Person person2 = Person.findById(person.getId(), Person.class);
        assertEquals("John", person2.getFirstName());
    }

    public void testGetRowCount() {
        new Person("One", "Jackson").persist();
        new Person("Two", "Jackson").persist();
        new Person("Three", "Jackson").persist();
        new Person("Four", "Jackson").persist();
        assertEquals("Should have been 4 Person objects found", 4, Person.getCount(Person.class));
    }

    public void testInstantGenericHibernateDao() throws Exception {
        Constructor<?>[] constructors = GenericHibernateDao.class.getConstructors();
        for (Constructor myctr : constructors) {
            logger.debug(myctr.getName());
            logger.debug("Accessible: " + myctr.isAccessible());
            logger.debug("Varargs?: " + myctr.isVarArgs());
            logger.debug("Synthetic: " + myctr.isSynthetic());
            Class[] parameterTypes = myctr.getParameterTypes();
            for (Class ptype : parameterTypes) {
                logger.debug(ptype);
            }
        }
        Constructor ctr = constructors[0];
        Object obj = null;
        obj = ctr.newInstance(null);
        assertNotNull(obj);
    }
}
