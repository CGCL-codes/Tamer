package matuya.sjm.examples.robot;

import matuya.sjm.parse.*;
import matuya.sjm.parse.tokens.*;

/**
 * Show how to use a parser class that arranges its 
 * subparsers as methods.
 * 
 * @author Steven J. Metsker
 *
 * @version 1.0 
 */
public class ShowRobotRefactored {

    /** 
 * Show how to use a parser class that arranges its subparsers 
 * as methods.
 */
    public static void main(String[] args) {
        Parser p = new RobotRefactored().command();
        String s = "place carrier at WB500_IN";
        System.out.println(p.bestMatch(new TokenAssembly(s)));
    }
}
