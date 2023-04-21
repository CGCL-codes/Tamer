package frsf.cidisi.faia.solver;

import frsf.cidisi.faia.agent.Action;

/**
 * @author Jorge M. Roa
 * @version 1.0
 * @created 08-Mar-2007 13:16:05
 */
public abstract class Solve {

    public Solve() {
    }

    /**
     * 
     * @param problem
     */
    public abstract Action solve(Object[] params) throws Exception;

    public abstract void showSolution();
}
