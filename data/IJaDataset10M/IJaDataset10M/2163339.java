package de.KW4FT.TestRepository.TXT;

import static org.junit.Assert.*;
import java.util.HashMap;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import de.KW4FT.TestRepository.TXT.Excel_TestData;
import de.KW4FT.TestRepository.TXT.RepositoryPfade;

public class Excel_TestDataTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testReadData() {
        HashMap<String, String> my_HashMap = new HashMap<String, String>();
        HashMap<String, String> my_HashMap_Actuell = new HashMap<String, String>();
        my_HashMap.put("[Login]Benutzer(1)", "Zoltan");
        my_HashMap.put("[Login]Benutzer(2)", "Hanna");
        my_HashMap.put("[Login]Passwort(1)", "Geheim1");
        my_HashMap.put("[Login]Passwort(2)", "Geheim2");
        my_HashMap.put("[Carkonfig]Modell(1)", "Rolo");
        my_HashMap.put("[Carkonfig]Modell(2)", "Rasant");
        my_HashMap.put("[Carkonfig]Ausstattung(1)", "ABS<trenner>Lederlenkrad");
        my_HashMap.put("[Carkonfig]Ausstattung(2)", "ABS<trenner>Lederlenkrad");
        my_HashMap.put("[Carkonfig]Sondermodell(1)", "Jazz");
        my_HashMap.put("[Carkonfig]Sondermodell(2)", "Gomera");
        my_HashMap.put("[Carkonfig]Rabatt(1)", "10");
        my_HashMap.put("[Carkonfig]Rabatt(2)", "[Carkonfig]Rabatt(1)");
        my_HashMap.put("[Carkonfig]Preis(1)", "24500");
        my_HashMap.put("[Carkonfig]Preis(2)", "23400");
        @SuppressWarnings("unused") RepositoryPfade lv_RepositoryPfade = RepositoryPfade.getInstance("C:\\TXT_Testrepository");
        Excel_TestData my_TestData = new Excel_TestData();
        my_HashMap_Actuell = my_TestData.ReadData("Testrepository.xls");
        assertEquals(my_HashMap, my_HashMap_Actuell);
    }
}
