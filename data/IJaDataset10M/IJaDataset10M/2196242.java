package examples.gp.tictactoe;

import org.jgap.gp.*;
import org.jgap.gp.impl.*;
import junit.framework.*;

public class TicTacToeTest extends GPTestCase {

    /** String containing the CVS revision. Read out via reflection!*/
    private static final String CVS_REVISION = "$Revision: 1.2 $";

    public static Test suite() {
        TestSuite suite = new TestSuite(TicTacToeTest.class);
        return suite;
    }

    public void setUp() {
        super.setUp();
    }

    /**
   * @throws Exception
   *
   * @author Klaus Meffert
   * @since 3.2
   */
    public void testConstruct_0() throws Exception {
        GPConfiguration config = new GPConfiguration();
        config.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
        config.setMaxInitDepth(8);
        config.setPopulationSize(40);
        config.setStrictProgramCreation(false);
        config.setProgramCreationMaxTries(5);
        config.setMaxCrossoverDepth(12);
        TicTacToeMain game1 = new TicTacToeMain(config);
        GPGenotype player1 = game1.create(config, 1, null, 2);
        GPConfiguration config2 = new GPConfiguration();
        config2.setGPFitnessEvaluator(new DeltaGPFitnessEvaluator());
        config2.setMaxInitDepth(8);
        config2.setPopulationSize(40);
        config2.setStrictProgramCreation(false);
        config2.setProgramCreationMaxTries(5);
        config2.setMaxCrossoverDepth(12);
        TicTacToeMain game2 = new TicTacToeMain(config2);
        GPGenotype player2 = game2.create(config2, 2, null, 1);
    }
}
