package fr.jussieu.gla.wasa.samples.queens;

import fr.jussieu.gla.wasa.core.ICustomizer;
import fr.jussieu.gla.wasa.core.Problem;
import fr.jussieu.gla.wasa.core.EngineException;

/**
 * The NQueen customizer, which randomize with a certain rate of {@link Var}s each time there is
 * no improvement found and that the {@link TabuList} is full.
 * @author Florent Selva
 * @author Laurent Caillette
 * @version $Revision: 1.3 $ $Date: 2002/04/17 15:24:08 $
 */
public class QueenCustomizer implements ICustomizer {

    private float randomRatio;

    /**
   * Sets the rate of random.
   */
    public QueenCustomizer(float randomRatio) {
        this.randomRatio = randomRatio;
    }

    /**
   * Random of the whole Vars and clears the {@link TabuList} when
   * <tt>Problem</tt> is uninitialized.
   * @see ICustomizer
   */
    public void processUninitialized(Problem problem) throws EngineException {
        problem.getRandomConfigurator().configure(1f);
        problem.getEngine().getTabuList().clear();
    }

    /**
   * Do nothing.
   * @see ICustomizer
   */
    public void processNoErrorAssigned(Problem problem) throws EngineException {
    }

    /**
   * Random of the whole Vars and clears the {@link TabuList} when
   * <tt>Problem</tt> is uninitialized.
   * @see ICustomizer
   */
    public void processNoVarFound(Problem problem) throws EngineException {
        problem.getRandomConfigurator().configure(1f);
        problem.getEngine().getTabuList().clear();
    }

    /**
   * Random with a certain rate of {@link Var}s each time the
   * {@link TabuList} is full.
   * @see ICustomizer
   */
    public void processNoImprovementFound(Problem problem) throws EngineException {
        if (problem.getEngine().getTabuList().getSize() == problem.getEngine().getTabuList().getCapacity()) {
            problem.getRandomConfigurator().configure(randomRatio);
            problem.getEngine().getTabuList().clear();
        }
    }

    /**
   * Clears the {@link TabuList}.
   * @see ICustomizer
   */
    public void processImprovementFound(Problem problem) throws EngineException {
        problem.getEngine().getTabuList().clear();
    }

    /**
   * Returns <tt>null</tt>
   * @return <tt>null</tt>
   */
    public ICustomizerEditor createCustomizerEditor() {
        return null;
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String toString() {
        return "NQueen Customizer";
    }
}
