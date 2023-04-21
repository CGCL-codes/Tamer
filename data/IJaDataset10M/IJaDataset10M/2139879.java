package jeco.kernel.operator.crossover;

import jeco.kernel.problem.Solution;
import jeco.kernel.problem.Solutions;
import jeco.kernel.problem.Variable;

/**
  * El cruce sí necesita plantillas porque se accede continuamente a las variables
  * @author jlrisco
  * @param <T>
  */
public abstract class CrossoverOperator<T extends Variable> {

    public abstract Solutions<T> execute(Solution<T> parent1, Solution<T> parent2);
}
