package engine.distribution.serialization;

import java.io.Serializable;
import engine.Population;

/**
 * Class representing an evaluation task sent to the slave.
 * @author Karol Stosiek (karol.stosiek@gmail.com)
 * @author Michal Anglart (anglart.michal@gmail.com)
 * 
 * @param <T> Type of the individual in the task.
 */
public class EvaluationTask<T> implements Serializable {

    /** Generated serial version UID. */
    private static final long serialVersionUID = 7335573552212749764L;

    /** State of the task. */
    private enum State {

        NOT_EVALUATED, EVALUATED
    }

    ;

    /** Population that has to be evaluated. */
    private final Population<T> population;

    /** State of the evaluation task. */
    private State state;

    /**
   * Constructor.
   * @param newPopulation Population to create task from.
   * @param newState State of the task.
   */
    EvaluationTask(final Population<T> newPopulation, final State newState) {
        this.population = newPopulation;
        this.state = newState;
    }

    /**
   * Constructor of inevaluated task.
   * @param newPopulation Population to create task from.
   */
    public EvaluationTask(final Population<T> newPopulation) {
        this(newPopulation, State.NOT_EVALUATED);
    }

    /**
   * Gets the population to be evaluated.
   * @return Population to be evaluated.
   */
    public Population<T> getPopulation() {
        return population;
    }

    /**
   * Gets the state of the task.
   * @return True iff the task is evaluated, false otherwise.
   */
    public boolean isEvaluated() {
        return state == State.EVALUATED;
    }

    /** Marks the task as already evaluated. */
    public void markAsEvaluated() {
        state = State.EVALUATED;
    }

    /**
   * Returns size of the task.
   * @return Size of the task.
   */
    public int size() {
        return population.size();
    }
}
