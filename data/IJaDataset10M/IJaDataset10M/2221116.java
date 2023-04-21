package uchicago.src.sim.analysis;

import uchicago.src.sim.engine.ActionUtilities;
import uchicago.src.sim.util.SimUtilities;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Computes the maximum of a sequence of values, based on a list of 
 * objects and a method to call on them.
 *
 * @author Jerry Vos based on Nick Collier's Average Sequence
 * @version $Revision: 1.4 $ $Date: 2005/08/12 16:13:30 $
 * @see uchicago.src.sim.analysis.AverageSequence
 * @see uchicago.src.sim.analysis.MinSequence
 */
public class MaxSequence implements Sequence {

    private Method m;

    private ArrayList list;

    /**
	* Constructs this MaxSequence using the specified list and method
	* name. Each object in the list should respond to the method named by
	* method name. This method must return some subclass of Number.
	*
	* @param list the list of objects on which to call the method
	* @param methodName the name of the method to call. This method must return
	* some subclass of java.Number.
	* @see java.lang.Number
	*/
    public MaxSequence(ArrayList list, String methodName) {
        try {
            m = ActionUtilities.getNoArgMethod(list.listIterator().next(), methodName);
            this.list = list;
        } catch (NoSuchMethodException ex) {
            SimUtilities.showError("Unable to find method " + methodName + " when creating MaxSequence.\n", ex);
            ex.printStackTrace();
        }
    }

    /**
	 * Compute and return the maximum, if the list this sequence is based
	 * off of is empty this will return Double.MIN_VALUE.
	 */
    public double getSValue() {
        return StatisticUtilities.getMax(list, m);
    }
}
