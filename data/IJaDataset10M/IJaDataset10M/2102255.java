package pdp.scrabble.test;

import pdp.scrabble.test.bag.BagAccess;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pdp.scrabble.Game;
import static pdp.scrabble.Factory.FACTORY;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BagAccess.class })
public class BagTester {

    public static final Game GAME = FACTORY.createGame("English", null);

    @BeforeClass
    public static void setUpClass() throws Exception {
        GAME.create();
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
}
