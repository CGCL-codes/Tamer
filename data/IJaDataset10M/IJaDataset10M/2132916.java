package org.opensourcedea.model;

import lpsolve.LpSolve;
import java.util.ArrayList;
import org.opensourcedea.dea.*;
import org.opensourcedea.exception.DEASolverException;
import org.opensourcedea.exception.MissingDataException;
import org.opensourcedea.exception.ProblemNotSolvedProperlyException;
import org.opensourcedea.linearSolver.*;

/**
 * The class implementing the SBM Output Oriented model (implementing both CRTS and VRTS).
 * <p>
 * Because the objective 'MIN po = 1 / (1 + (1/s) SUM(Sr+/Yro))' is not linear the 
 * objective used in the calculation is 'MAX 1/p0 = (1 + (1/s) SUM(Sr+/Yro))' which is linear.</p>
 * <p>
 * As a consequence the output slack weight is 1 / (s*Yro) and the model objective is deduced from
 * the solver objective value as follows: Model Objective = 1 / (1 + SolverObjectiveValue).
 * </p>
 * @author Hubert Virtos
 *
 */
public class SBM_O extends Model {

    /**
	 * The method solving the DEA Problem.
	 * @param deaP An instance of DEAProblem.
	 * @throws Exception 
	 */
    public DEAPSolution solve(DEAProblem deaP) throws MissingDataException, DEASolverException, ProblemNotSolvedProperlyException {
        Integer dmuIndex = 0;
        try {
            int nbDMUs = deaP.getNumberOfDMUs();
            int nbVariables = deaP.getNumberOfVariables();
            double[][] transposedMatrix = deaP.getTranspose(false);
            ArrayList<double[]> constraints = new ArrayList<double[]>();
            DEAPSolution returnSol = new DEAPSolution(nbDMUs, nbVariables);
            createConstraintMatrix(deaP, nbDMUs, nbVariables, transposedMatrix, constraints);
            for (int i = 0; i < nbDMUs; i++) {
                createAndSolve(deaP, nbDMUs, nbVariables, constraints, transposedMatrix, returnSol, i);
            }
            return returnSol;
        } catch (ProblemNotSolvedProperlyException e1) {
            throw new ProblemNotSolvedProperlyException("The problem could not be solved properly at DMU Index: " + dmuIndex.toString() + ". The error was: " + e1.getMessage());
        } catch (DEASolverException e2) {
            throw new DEASolverException("The problem could not be solved properly at DMU Index: " + dmuIndex.toString() + ". The error was: " + e2.getMessage());
        } catch (MissingDataException e3) {
            throw new MissingDataException("Some model data is missing.");
        }
    }

    private static void createConstraintMatrix(DEAProblem deaP, int nbDMUs, int nbVariables, double[][] transposedMatrix, ArrayList<double[]> constraints) {
        double[] constraintRow;
        for (int varIndex = 0; varIndex < nbVariables; varIndex++) {
            constraintRow = new double[nbDMUs + nbVariables];
            System.arraycopy(transposedMatrix[varIndex], 0, constraintRow, 0, nbDMUs);
            if (deaP.getVariableOrientation(varIndex) == VariableOrientation.INPUT) {
                constraintRow[nbDMUs + varIndex] = 1;
            } else {
                constraintRow[nbDMUs + varIndex] = -1;
            }
            constraints.add(constraintRow);
        }
        if (deaP.getModelType() == ModelType.SBM_O_V) {
            constraintRow = new double[nbDMUs + nbVariables];
            for (int k = 0; k < nbDMUs; k++) {
                constraintRow[k] = 1;
            }
            constraints.add(constraintRow);
        }
        if (deaP.getModelType() == ModelType.SBM_O_GRS) {
            constraintRow = new double[nbDMUs + nbVariables];
            for (int k = 0; k < nbDMUs; k++) {
                constraintRow[k] = 1;
            }
            constraints.add(constraintRow);
            constraints.add(constraintRow);
        }
    }

    private static void createAndSolve(DEAProblem deaP, int nbDMUs, int nbVariables, ArrayList<double[]> constraints, double[][] transposedMatrix, DEAPSolution returnSol, Integer dmuIndex) throws DEASolverException, ProblemNotSolvedProperlyException, MissingDataException {
        double[] objF = new double[nbDMUs + nbVariables];
        double[] rhs;
        int[] solverEqualityType;
        int nbOutputs = deaP.getNumberOfOutputs();
        if (deaP.getModelType() == ModelType.SBM_O) {
            rhs = new double[nbVariables];
            solverEqualityType = new int[nbVariables];
        } else if (deaP.getModelType() == ModelType.SBM_O_V) {
            rhs = new double[nbVariables + 1];
            solverEqualityType = new int[nbVariables + 1];
            rhs[nbVariables] = 1;
            solverEqualityType[nbVariables] = LpSolve.EQ;
        } else {
            rhs = new double[nbVariables + 2];
            solverEqualityType = new int[nbVariables + 2];
            rhs[nbVariables] = deaP.getRTSLowerBound();
            solverEqualityType[nbVariables] = LpSolve.GE;
            rhs[nbVariables + 1] = deaP.getRTSUpperBound();
            solverEqualityType[nbVariables + 1] = LpSolve.LE;
        }
        for (int varIndex = 0; varIndex < nbVariables; varIndex++) {
            rhs[varIndex] = transposedMatrix[varIndex][dmuIndex];
            if (deaP.getVariableOrientation(varIndex) == VariableOrientation.OUTPUT) {
                if (transposedMatrix[varIndex][dmuIndex] * nbOutputs != 0) {
                    objF[nbDMUs + varIndex] = 1 / (transposedMatrix[varIndex][dmuIndex] * nbOutputs);
                } else {
                    objF[nbDMUs + varIndex] = 1;
                }
            }
            solverEqualityType[varIndex] = LpSolve.EQ;
        }
        SolverResults sol = new SolverResults();
        sol = Lpsolve.solveLPProblem(constraints, objF, rhs, SolverObjDirection.MAX, solverEqualityType);
        storeSolution(deaP, nbDMUs, nbVariables, returnSol, dmuIndex, sol);
    }

    private static void storeSolution(DEAProblem deaP, int nbDMUs, int nbVariables, DEAPSolution returnSol, int dmuIndex, SolverResults sol) {
        returnSol.setObjective(dmuIndex, 1 / (1 + sol.Objective));
        for (int varPos = 0; varPos < nbVariables; varPos++) {
            returnSol.setSlack(dmuIndex, varPos, sol.VariableResult[nbDMUs + varPos]);
            if (deaP.getVariableOrientation(varPos) == VariableOrientation.OUTPUT) {
                returnSol.setWeight(dmuIndex, varPos, sol.DualResult[varPos + 1] * -1);
                returnSol.setProjection(dmuIndex, varPos, deaP.getDataMatrix(dmuIndex, varPos) + returnSol.getSlack(dmuIndex, varPos));
            } else {
                returnSol.setWeight(dmuIndex, varPos, sol.DualResult[varPos + 1]);
                returnSol.setProjection(dmuIndex, varPos, deaP.getDataMatrix(dmuIndex, varPos) - returnSol.getSlack(dmuIndex, varPos));
            }
        }
        ArrayList<NonZeroLambda> refSet = new ArrayList<NonZeroLambda>();
        for (int lambdaPos = 0; lambdaPos < nbDMUs; lambdaPos++) {
            if (sol.VariableResult[lambdaPos] != 0) {
                refSet.add(new NonZeroLambda(lambdaPos, sol.VariableResult[lambdaPos]));
            }
        }
        returnSol.setReferenceSet(dmuIndex, refSet);
        SolverStatus.checkSolverStatus(returnSol, sol);
    }
}
