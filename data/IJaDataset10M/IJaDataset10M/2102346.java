package infrastructure;

import infrastructure.species_parameters.IDatabase;
import infrastructure.species_parameters.ParamFile;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for Database
 */
public class DatabaseTest extends TestCase {

    IDatabase database;

    @Override
    @Before
    public void setUp() throws Exception {
        database = new ParamFile();
    }

    @Test
    public void testGetDouble() {
        double value = 0;
        value = database.getDouble("Arr_ela", "seedMass_g");
        assertTrue(value > 0);
        value = database.getDouble("Arr_ela", "allomConstDMvsNLeaf");
        assertTrue(value < -2);
    }
}
