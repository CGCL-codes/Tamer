package jeco.lib.problems.lp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import jeco.kernel.algorithm.moga.NSGAII;
import jeco.kernel.operator.crossover.SBXCrossover;
import jeco.kernel.operator.mutator.PolynomialMutation;
import jeco.kernel.operator.selector.BinaryTournamentNSGAII;
import jeco.kernel.problem.Problem;
import jeco.kernel.problem.Solution;
import jeco.kernel.problem.Solutions;
import jeco.kernel.problem.Variable;
import jeco.kernel.util.RandomGenerator;

/**
 *
 * @author jlrisco
 */
public class LinearProgramming extends Problem<Variable<Double>> {

    protected double[] cT = null;

    protected double[][] A = null;

    protected double[] b = null;

    protected String[] varNames;

    public LinearProgramming(String name, Integer numberOfVariables) {
        super(name, numberOfVariables, 2);
    }

    @Override
    public void evaluate(Solution<Variable<Double>> solution) {
        for (int i = 0; i < numberOfObjectives; i++) {
            solution.getObjectives().set(i, 0.0);
        }
        ArrayList<Variable<Double>> vars = solution.getVariables();
        double fitness = 0.0;
        for (int i = 0; i < numberOfVariables; ++i) {
            fitness += cT[i] * vars.get(i).getValue();
        }
        double violation = 0.0, lhs = 0.0, rhs = 0.0;
        for (int i = 0; i < A.length; ++i) {
            rhs = b[i];
            lhs = 0.0;
            for (int j = 0; j < A[i].length; ++j) {
                lhs += A[i][j] * vars.get(j).getValue();
            }
            if (lhs > rhs) {
                violation += lhs - rhs;
            }
        }
        solution.getObjectives().set(0, violation);
        solution.getObjectives().set(1, fitness);
    }

    @Override
    public void newRandomSetOfSolutions(Solutions<Variable<Double>> solutions) {
        for (Solution<Variable<Double>> solution : solutions) {
            for (int j = 0; j < numberOfVariables; ++j) {
                solution.getVariables().get(j).setValue(RandomGenerator.nextDouble(lowerBound[j], upperBound[j]));
            }
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("Minimize:\n");
        for (int i = 0; i < cT.length; ++i) {
            if (cT[i] > 0) {
                buffer.append("+");
            } else {
                buffer.append("-");
            }
            buffer.append(cT[i]);
            buffer.append("*");
            buffer.append(varNames[i]);
        }
        buffer.append("\nSubject to:\n");
        for (int i = 0; i < A.length; ++i) {
            for (int j = 0; j < A[i].length; ++j) {
                if (A[i][j] >= 0) buffer.append("+");
                buffer.append(A[i][j]);
                buffer.append("*");
                buffer.append(varNames[j]);
            }
            buffer.append("<=");
            buffer.append(b[i]);
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public static LinearProgramming newInstance(String filePath) {
        LinearProgramming problem = null;
        LpReader lpReader = new LpReader(filePath);
        try {
            lpReader.readLP();
            int noOfVariables = lpReader.noOfVariables();
            problem = new LinearProgramming(filePath, noOfVariables);
            problem.varNames = lpReader.varName;
            for (int i = 0; i < noOfVariables; ++i) {
                problem.lowerBound[i] = lpReader.lbound[i];
                if (Double.isInfinite(lpReader.ubound[i])) {
                    problem.upperBound[i] = 1000000;
                } else {
                    problem.upperBound[i] = lpReader.ubound[i];
                }
            }
            double[] cT = lpReader.objectiveVector();
            for (int i = 0; i < noOfVariables; ++i) {
                if (lpReader.objectiveSense() == LpReader.SENSE_MAX) {
                    cT[i] = -cT[i];
                }
            }
            problem.cT = cT;
            double[] b = lpReader.rhsVector();
            double[][] A = lpReader.constraintsMatrix();
            int noOfConstraints = lpReader.noOfConstraints();
            for (int i = 0; i < noOfConstraints; ++i) {
                if (b[i] != 0 && lpReader.sense[i] == LpReader.SENSE_GEQ) {
                    b[i] = -b[i];
                }
                for (int j = 0; j < noOfVariables; ++j) {
                    if (A[i][j] != 0 && lpReader.sense[i] == LpReader.SENSE_GEQ) {
                        A[i][j] = -A[i][j];
                    }
                }
            }
            problem.A = A;
            problem.b = b;
        } catch (ParseException ex) {
            Logger.getLogger(LinearProgramming.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LinearProgramming.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LinearProgramming.class.getName()).log(Level.SEVERE, null, ex);
        }
        return problem;
    }

    public static void main(String[] args) {
        String filePath = "D:\\jlrisco\\Trabajo\\Proyectos\\2010PlanAvanza\\test\\production.lp";
        LinearProgramming problem = LinearProgramming.newInstance(filePath);
        System.out.println(problem.toString());
        NSGAII<Variable<Double>> nsga2 = new NSGAII<Variable<Double>>(problem, 100, 2500, new PolynomialMutation<Variable<Double>>(problem), new SBXCrossover<Variable<Double>>(problem), new BinaryTournamentNSGAII<Variable<Double>>());
        nsga2.initialize();
        Solutions<Variable<Double>> solutions = nsga2.execute();
        for (Solution<Variable<Double>> solution : solutions) {
            if (solution.getObjectives().get(0) > 0) {
                continue;
            }
            ArrayList<Variable<Double>> vars = solution.getVariables();
            for (int i = 0; i < vars.size(); ++i) {
                System.out.print(problem.varNames[i] + "=" + vars.get(i).getValue() + " ");
            }
            System.out.println("-> " + solution.getObjectives().get(1));
        }
    }

    @Override
    public Variable<Double> newVariable() {
        return new Variable<Double>(0.0);
    }

    @Override
    public Problem<Variable<Double>> clone() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
