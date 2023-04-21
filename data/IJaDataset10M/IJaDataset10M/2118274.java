package br.usp.ime.mytales.tests;

import br.usp.ime.mytales.MyTalesCharacter;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author JTEODORO
 */
public class LoadCharacteresJUnitTest {

    public LoadCharacteresJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void load() {
        MyTalesCharacter.loadCharacters();
        assertTrue(MyTalesCharacter.containsCharacters());
    }
}
