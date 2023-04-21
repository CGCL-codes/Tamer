package constants;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Marco Celesti
 */
public class DNAConstants {

    private static Map<Integer, String> dnaInstructions = new HashMap<Integer, String>();

    public static final String DO_NOTHING = "do_nothing";

    public static final String MOVE_NN = "move_nn";

    public static final String MOVE_NE = "move_ne";

    public static final String MOVE_EE = "move_ee";

    public static final String MOVE_SE = "move_se";

    public static final String MOVE_SS = "move_ss";

    public static final String MOVE_SW = "move_sw";

    public static final String MOVE_WW = "move_ww";

    public static final String MOVE_NW = "move_nw";

    public static final String EAT = "eat";

    public static final String GOTO = "goto";

    public static final String DIE = "die";

    public static Map<Integer, String> getMapInstructions() {
        dnaInstructions.put(0, DO_NOTHING);
        dnaInstructions.put(1, MOVE_NN);
        dnaInstructions.put(2, MOVE_NE);
        dnaInstructions.put(3, MOVE_EE);
        dnaInstructions.put(4, MOVE_SE);
        dnaInstructions.put(5, MOVE_SS);
        dnaInstructions.put(6, MOVE_SW);
        dnaInstructions.put(7, MOVE_WW);
        dnaInstructions.put(8, MOVE_NW);
        dnaInstructions.put(9, GOTO);
        dnaInstructions.put(10, EAT);
        return dnaInstructions;
    }
}
