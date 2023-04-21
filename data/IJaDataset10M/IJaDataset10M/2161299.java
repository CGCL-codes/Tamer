package FitnessEvaluation.ParityProblem;

/**
 * Class implementing the functions called by the Even Five Parity problem
 * @author jonatan
 */
public abstract class EvenFiveParBSF {

    /**
     * Negates the input, 1 becomes 0, 0 becomes 1
     * @param x input
     * @return negated input
     * @throws ArithmeticException if input is not 0 or 1
     */
    public int not(int x) throws ArithmeticException {
        if (x == 0) return 1; else if (x == 1) return 0; else throw new ArithmeticException();
    }

    /**
     * Checks if there is an even number of 1s as input. 
     * @param d0 input
     * @param d1 input
     * @param d2 input
     * @param d3 input
     * @param d4 input
     * @return return 1 if the number of 1s in the input is even else 0
     */
    int fun(int d0, int d1, int d2, int d3, int d4) {
        int d = d0 + d1 + d2 + d3 + d4;
        if ((d % 2) == 0) return 1; else return 0;
    }

    /**
     * Calculates the fitness by comparing the input expression to all possible cases. Minimizing 0 is the best
     * @return the fitness value
     */
    public int getFitness() {
        int fitness = 32;
        for (int d0 = 0; d0 < 2; d0++) {
            for (int d1 = 0; d1 < 2; d1++) {
                for (int d2 = 0; d2 < 2; d2++) {
                    for (int d3 = 0; d3 < 2; d3++) {
                        for (int d4 = 0; d4 < 2; d4++) {
                            if (fun(d0, d1, d2, d3, d4) == expr(d0, d1, d2, d3, d4)) fitness--;
                        }
                    }
                }
            }
        }
        return fitness;
    }

    /** Creates a new instance of EvenFiveParityHelpFile */
    public EvenFiveParBSF() {
    }

    /**
     * Abstract method to override for evaluating input expression.
     * @param d0 input
     * @param d1 input
     * @param d2 input
     * @param d3 input
     * @param d4 input
     * @return value of expression
     */
    public abstract int expr(int d0, int d1, int d2, int d3, int d4);
}
