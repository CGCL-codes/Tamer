package org.javalite.activejdbc;

import org.javalite.activejdbc.test.ActiveJDBCTest;
import org.javalite.activejdbc.test_models.Person;
import org.junit.Test;

/**
 * @author Igor Polevoy
 */
public class LongIdTest extends ActiveJDBCTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPEIfIdIsNull() {
        Person p = new Person();
        p.getLongId();
    }

    @Test
    public void shouldReturnValidLongId() {
        deleteAndPopulateTable("people");
        Person p = (Person) Person.findById(1);
        a(p.getLongId()).shouldBeEqual(1);
    }
}
