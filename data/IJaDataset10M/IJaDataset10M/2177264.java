package pdp.scrabble.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import pdp.scrabble.Game;
import pdp.scrabble.Language;
import pdp.scrabble.test.board.BoardAccess;
import static pdp.scrabble.Factory.FACTORY;

@RunWith(Suite.class)
@Suite.SuiteClasses({ BoardAccess.class })
public class BoardTester {

    public static final Game GAME = FACTORY.createGame(Language.init("English"), null);

    @BeforeClass
    public static void setUpClass() throws Exception {
        GAME.create();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }
}
