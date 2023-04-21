package model.concept;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class PersonTest {

    @Test
    public void personCreated() {
        Person person = new Person("Dzenan", "Ridjanovic");
        assertEquals("Dzenan", person.getFirstName());
        assertEquals("Ridjanovic", person.getLastName());
        assertEquals(1, person.getMaxNumberOfBooks());
    }

    @Test
    public void maxBooksSet() {
        Person person = new Person("Dzenan", "Ridjanovic");
        person.setMaxNumberOfBooks(10);
        assertEquals(10, person.getMaxNumberOfBooks());
    }

    @Test
    public void toStringDisplayed() {
        Person person = new Person("Dzenan", "Ridjanovic");
        assertEquals("Ridjanovic, Dzenan (1 max. number of books)", person.toString());
    }
}
