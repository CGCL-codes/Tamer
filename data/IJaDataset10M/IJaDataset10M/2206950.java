package qlearning;

import environment.IState;

/** Every problem where the set of states  can be enumerated might implement this interface.<br>

It provides methods to compute V*(state), print the values on the display, allow the programmer to access an individual V*() value.

*/
public interface ValueIteration {

    /** Computes V* for each state, stores the result in a n-dimensionnal array<br>

    n is the number of State dimensions. */
    public void computeVStar(double gamma);

    /** Display all the values on the screen : it is up to the user to define the
	output format corresponding to its needs (gnuplot...)

	Warning : computeVStar must have been called before !
    */
    public void displayVStar();

    /** Read V*(State) <br>
	Warning : computeVStar must have been called before !
    */
    public double getVStar(IState e);

    /** Use V* to extract an optimal policy : you must define the output format.
   
    */
    public void extractPolicy(double gamma);
}
