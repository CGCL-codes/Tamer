package org.databene.domain.address;

import org.junit.Test;
import static junit.framework.Assert.*;

/**
 * Tests the {@link City} class.<br/><br/>
 * Created at 02.05.2008 13:27:53
 * @since 0.5.3
 * @author Volker Bergmann
 */
public class CityTest {

    @Test
    public void testEquals() {
        State bavaria = new State("BY");
        City city = new City(bavaria, "Munich", null, null, "89");
        assertFalse(city.equals(null));
        assertFalse(city.equals(bavaria));
        assertTrue(city.equals(city));
        assertTrue(city.equals(new City(bavaria, "Munich", null, null, "89")));
        assertFalse(city.equals(new City(bavaria, "Nuremberg", null, null, "89")));
    }
}
