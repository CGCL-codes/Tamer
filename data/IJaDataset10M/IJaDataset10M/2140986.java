package gpl.lonelysingleton.sleepwalker.tests.genetic;

import gpl.lonelysingleton.sleepwalker.genetic.AbstractPopulation;
import gpl.lonelysingleton.sleepwalker.genetic.Problem;
import java.util.NoSuchElementException;

public final class AbstractPopulationImplementor extends AbstractPopulation {

    public AbstractPopulationImplementor(final Problem ProblemToSolve_P) throws CloneNotSupportedException {
        super(ProblemToSolve_P);
    }

    public void changeGeneration() throws NoSuchElementException, CloneNotSupportedException {
        setCurrentGenerationNumber(getCurrentGenerationNumber() + 1);
    }
}
