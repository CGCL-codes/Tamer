package uchicago.src.sim.games;

/**
 *
 * @version $Revision: 1.4 $ $Date: 2004/11/03 19:51:03 $
 */
public class AlwaysCooperate implements Strategy {

    private static AlwaysCooperate allC = new AlwaysCooperate();

    public static AlwaysCooperate getInstance() {
        return allC;
    }

    private AlwaysCooperate() {
    }

    public GameChoice calculateMove(GameChoice opponentMove) {
        return GameChoice.COOPERATE;
    }

    public String toString() {
        return "ALWAYS COOPERATE";
    }
}
