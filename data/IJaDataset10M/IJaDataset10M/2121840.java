package alice.tuprolog;

import java.io.Serializable;

/**
 * This class defines runtime information about the goal to be demonstrated.
 *
 *
 *
 */
final class GoalInfo implements Serializable {

    /** demonstrating goal */
    public Term goal;

    /** runtime context of the demo */
    public RTContext context;

    /** time and mark index from which beginning next rename and unification operation */
    public int time, mark;

    GoalInfo(Term g) {
        goal = g;
    }

    public String toString() {
        return "[ goal info - goal: " + goal + " - time: " + time + " - mark: " + mark + " ]";
    }
}
