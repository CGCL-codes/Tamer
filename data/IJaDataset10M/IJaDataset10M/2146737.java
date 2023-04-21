package adressepostale.logic;

import static org.fest.assertions.Assertions.assertThat;
import org.junit.Test;

/**
 * Unit test of {@link MotMajuscule}.
 */
public class MotMajusculeTest {

    /**
     * Test method for {@link adressepostale.logic.MotMajuscule#getTexte()}.
     */
    @Test
    public void testGetTexte() {
        assertThat(new MotMajuscule("Arsène Lupin").getTexte()).isEqualTo("ARSENE LUPIN");
        assertThat(new MotMajuscule("Arsène Lupin").getTexteOrigine()).isEqualTo("Arsène Lupin");
    }
}
