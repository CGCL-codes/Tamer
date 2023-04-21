package GBP;

import java.util.*;

/**
 * This program solves the Graph-Bipartitioning-Problem 
 *   through KLFM algorithm experiment.
 *
 * @author T.S.Yo
**/
public class RunEXP {

    public static void main(String[] args) {
        String expOpt = args[0];
        String iFile = args[1];
        int opt = Integer.parseInt(expOpt);
        RunGBP exp1 = new RunGBP(iFile);
        switch(opt) {
            case 1:
                System.out.println("Performing fixed-number Multi-Start FM experiement");
                exp1.runFixedMS();
                break;
            case 2:
                System.out.println("Performing Multi-Start FM till optimum found");
                exp1.runContMS();
                break;
            case 3:
                System.out.println("Performing GA-FM with ini-population are local optimums");
                exp1.runGA(true);
                break;
            case 4:
                System.out.println("Performing GA-FM  with random ini-population");
                exp1.runGA(false);
                break;
            default:
                System.out.println("Option " + expOpt + " is not supported.");
                break;
        }
    }
}
