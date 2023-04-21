package fr.jussieu.gla.wasa.core;

import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Hashtable;
import com.lc.util.ImplementationError;

/**
 * The heart of the WASA framework, containing the Adaptative Search
 * Algorithm implementation.
 *
 * <p>
 * <table width="80%" cellpadding="5" border="1"><tr><td>
 * <p>Start from a random assignment of variables in V</p>
 * <blockquote>
 * <p> Repeat</p>
 * <ol>
 *   <li>
 *     Compute errors of all constraints in C and combine errors
 *     on each variable by considering for a given variable only the
 *     constraints on which it appears
 *   </li><li>
 *     select the variable X (not marked as Tabu) with highest error
 *     and evaluate costs of possible moves from X
 *   </li><li>
 *     if no improving move exists <br>
 *     then mark X tabu for a given number of iterations<br>
 *     else select the best move (min&nbsp;- conflict) and change the
 *     value of X accordingly
 *   </li>
 * </ol>
 * <p>
 *   until a solution is found or a maximal number of iterations is reached
 * </p>
 * </blockquote>
 * </td></tr></table>
 * @author Laurent Caillette
 * @version $Revision: 1.14 $ $Date: 2002/04/12 11:41:34 $
 */
public class Algorithm {

    private final Engine engine;

    private final ErrorTable errorTable;

    public Algorithm(Engine engine) {
        this.engine = engine;
        engine.setAlgorithm(this);
        errorTable = new ErrorTable(engine.getProblem());
    }

    protected Engine getEngine() {
        return engine;
    }

    protected Problem getProblem() {
        return engine.getProblem();
    }

    protected TabuList getTabuList() {
        return engine.getTabuList();
    }

    protected ErrorTable getErrorTable() {
        return errorTable;
    }

    private AlgorithmState state = AlgorithmState.UNINITIALIZED;

    public AlgorithmState getState() {
        return state;
    }

    protected void setState(AlgorithmState newState) {
        if (newState == null) {
            throw new NullPointerException("newState");
        }
        state = newState;
    }

    /**
 * The very best Evaluation found since the Algorithm started to run.
 */
    private Evaluation absoluteBest = null;

    protected Evaluation getAbsoluteBest() {
        return absoluteBest;
    }

    /**
 * The Evaluation to compute Neighbourhood from. This value is reassigned
 * at the end of {@link #step()}.
 */
    private Evaluation stepStartPoint = null;

    protected Evaluation getStepStartPoint() {
        return stepStartPoint;
    }

    /**
 * Set to the best Evaluation after {@link #exploreAround()}.
 * The Configuratino should have been kept.
 */
    private Evaluation bestInLastNeighbourhood = null;

    protected Evaluation getBestInLastNeighbourhood() {
        return bestInLastNeighbourhood;
    }

    /**
 * Set to the result of {@link #evaluateConstraints()}.
 */
    private Evaluation lastEvaluation = null;

    protected Evaluation getLastEvaluation() {
        return lastEvaluation;
    }

    private boolean constraintApplied = false;

    private Constraint currentConstraint;

    protected Constraint getCurrentConstraint() {
        return currentConstraint;
    }

    private boolean errorAssigned = false;

    protected boolean getErrorAssigned() {
        return errorAssigned;
    }

    void notifyErrorFor(Var var) {
        float[] errors = errorTable.getErrors(var);
        if (errors == null) {
            errors = new float[getProblem().getConstraints().length];
            for (int i = 0; i < errors.length; i++) {
                errors[i] = Float.NaN;
            }
            errorTable.put(var, errors);
        }
        int constraintRank = currentConstraint.getRank();
        if (Float.isNaN(errors[constraintRank])) {
            errors[constraintRank] = var.getError();
        } else {
            errors[constraintRank] += var.getError();
        }
        constraintApplied = true;
    }

    /**
 * Evaluates Constraints on current Buisness Object state.
 *
 * <p>
 * Instance variables access&nbsp;:
 * <ul>
 *   <li>
 *     {@link #problem} : errors are unset
 *   </li><li>
 *     {@link #errorTable} : cleared, then updated with errors set by each
 *     constraint's evaluation
 *   </li><li>
 *     {@link #lastEvaluation} : assigned
 *   </li><li>
 *     {@link #absoluteBest} : re-assigned if better than best was found
 *   </li>
 * </ul>
 *
 * <p>
 * Method calls
 * <ul>
 *   <li>
 *     {@link Problem#unsetErrors()}
 *   </li><li>
 *     {@link Constraint#evaluate()}
 *   </li><li>
 *     {@link #notifyErrorFor( Var )} from {@link Constraint#evaluate()}
 *   </li>
 * </ul>
 * </p>
 */
    protected void evaluateConstraints() throws EngineException, CloneNotSupportedException {
        getProblem().unsetErrors();
        errorTable.clear();
        Constraint[] constraints = getProblem().getConstraintArray();
        for (int i = 0; i < constraints.length; i++) {
            currentConstraint = constraints[i];
            currentConstraint.evaluate();
        }
        errorAssigned = errorTable.countAffectedVars() != 0;
        lastEvaluation = new Evaluation(getProblem(), errorTable.getVarsSortedByErrorMix(), errorTable.computeOverallError());
        if (absoluteBest == null) {
            absoluteBest = lastEvaluation;
        } else {
            Evaluation maybeBest = absoluteBest.retainIfBetter(lastEvaluation);
            if (maybeBest != absoluteBest) {
                absoluteBest = maybeBest;
                absoluteBest.createConfiguration();
            }
        }
    }

    private Var selectedVar = null;

    protected Var getSelectedVar() {
        return selectedVar;
    }

    /**
 * Evaluates Constraints on current Buisness Object state.
 *
 * <p>
 * Instance variables access&nbsp;:
 * <ul>
 *   <li>
 *     {@link #stepStartPoint}
 *   </li><li>
 *     {@link #lastEvaluation} : assigned
 *   </li><li>
 *     {@link #absoluteBest} : re-assigned if better than best was found
 *   </li>
 * </ul>
 *
 * <p>
 * Method calls
 * <ul>
 *   <li>
 *     {@link Problem#restore( Configuration )}
 *   </li><li>
 *     {@link Explorer#reset()}
 *   </li><li>
 *     {@link Explorer#apply()}
 *   </li>
 * </ul>
 * </p>
 *
 * @return <tt>false</tt> if no <tt>Var</tt> to run an <tt>Explorer</tt> on
 *     was found, <tt>true</tt> otherwise.
 */
    protected AlgorithmState explore() throws EngineException, CloneNotSupportedException {
        selectedVar = stepStartPoint.getMostViolatingVar(getTabuList());
        setState(AlgorithmState.EXPLORING);
        if (selectedVar == null) {
            return AlgorithmState.NO_VAR_FOUND;
        } else {
            Explorer explorer = selectedVar.getExplorer();
            explorer.reset(selectedVar);
            int exploreIndex = 0;
            boolean processExplorer = true;
            while (processExplorer) {
                getProblem().restore(stepStartPoint.getConfiguration());
                processExplorer = explorer.hasNeighbours(exploreIndex);
                if (processExplorer) {
                    if (explorer.explore(exploreIndex)) {
                        evaluateConstraints();
                        if (lastEvaluation == bestInLastNeighbourhood.retainIfBetter(lastEvaluation)) {
                            bestInLastNeighbourhood = lastEvaluation;
                            bestInLastNeighbourhood.createConfiguration();
                        }
                    }
                }
                if (!errorAssigned) {
                    return AlgorithmState.NO_ERROR_ASSIGNED;
                }
                exploreIndex++;
            }
            if (bestInLastNeighbourhood == stepStartPoint) {
                return AlgorithmState.NO_IMPROVEMENT_FOUND;
            } else {
                return AlgorithmState.IMPROVEMENT_FOUND;
            }
        }
    }

    protected void customize() throws EngineException, CloneNotSupportedException {
        AlgorithmState currentState = getState();
        if (currentState == AlgorithmState.UNINITIALIZED) {
            getEngine().getCustomizer().processUninitialized(getProblem());
        } else if (currentState == AlgorithmState.NO_VAR_FOUND) {
            getEngine().getCustomizer().processNoVarFound(getProblem());
        } else if (currentState == AlgorithmState.NO_IMPROVEMENT_FOUND) {
            getEngine().getCustomizer().processNoImprovementFound(getProblem());
        } else if (currentState == AlgorithmState.IMPROVEMENT_FOUND) {
            getEngine().getCustomizer().processImprovementFound(getProblem());
        } else if (currentState == AlgorithmState.NO_ERROR_ASSIGNED) {
            getEngine().getCustomizer().processNoErrorAssigned(getProblem());
        } else {
            throw new ImplementationError("getState() : " + currentState + " value not handled");
        }
        evaluateConstraints();
        lastEvaluation.createConfiguration();
    }

    private static int stepIndex = 0;

    public AlgorithmState step() throws EngineException, CloneNotSupportedException {
        constraintApplied = false;
        customize();
        stepStartPoint = lastEvaluation;
        bestInLastNeighbourhood = stepStartPoint;
        AlgorithmState currentState = explore();
        setState(currentState);
        getTabuList().step();
        if (currentState == AlgorithmState.NO_VAR_FOUND) {
        } else if (currentState == AlgorithmState.NO_IMPROVEMENT_FOUND) {
            getTabuList().put(selectedVar);
        } else if (currentState == AlgorithmState.IMPROVEMENT_FOUND) {
            stepStartPoint = bestInLastNeighbourhood;
        } else if (currentState == AlgorithmState.NO_ERROR_ASSIGNED) {
            stepStartPoint = bestInLastNeighbourhood;
        } else {
            throw new ImplementationError("getState() : " + currentState + " value not handled");
        }
        getProblem().restore(stepStartPoint.getConfiguration());
        if (!getEngine().getAllowNoConstraintApplied() && !constraintApplied) {
            throw new NoConstraintAppliedException();
        }
        return currentState;
    }
}
