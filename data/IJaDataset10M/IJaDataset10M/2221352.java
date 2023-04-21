package net.jankenpoi.sudokuki.solver;

public interface GridSolver {

    int GRID_LENGTH = 81;

    /**
	 * Method to resolve a grid (usually passed as parameter to the solver's
	 * constructor)
	 * 
	 * @return a <b>GridSolution</b> object containing the result of the solving
	 *         process,<br/>
	 *         <b>null</b> if the solving process was canceled before
	 *         completion.
	 * 
	 */
    GridSolution resolve();
}
