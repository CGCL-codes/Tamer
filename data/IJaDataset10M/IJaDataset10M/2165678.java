package es.ulpgc.dis.heuriskein.model.solver.problems;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Vector;
import es.ulpgc.dis.heuriskein.model.solver.Execution;
import es.ulpgc.dis.heuriskein.model.solver.Fitness;
import es.ulpgc.dis.heuriskein.model.solver.Individual;
import es.ulpgc.dis.heuriskein.model.solver.Population;
import es.ulpgc.dis.heuriskein.model.solver.representation.FOIndividual;
import org.nfunk.jep.*;
import org.lsmp.djep.xjep.*;

public class FOFitness extends Fitness {

    private String function;

    XJep jep;

    private double c = 0.5;

    private int a = 1;

    private double lower;

    private double upper;

    private ArrayList<Constraint> lessEqualConstrains;

    private ArrayList<Node> lessEqualNodes;

    private ArrayList<Constraint> equalConstrains;

    private ArrayList<Node> equalNodes;

    private ArrayList<Constraint> greaterEqualConstrains;

    private ArrayList<Node> greaterEqualNodes;

    private Node functionNode;

    private boolean firstTime = true;

    private boolean maximize = false;

    public FOFitness(String n) {
        this.function = n;
        initializateParser();
        jep = new XJep();
        jep.addStandardConstants();
        jep.addStandardFunctions();
        jep.setAllowUndeclared(true);
        jep.setAllowAssignment(true);
        jep.setImplicitMul(true);
        lessEqualConstrains = new ArrayList<Constraint>();
        equalConstrains = new ArrayList<Constraint>();
        greaterEqualConstrains = new ArrayList<Constraint>();
    }

    public FOFitness(FOFitness fitness) {
        this.function = fitness.function;
        jep = new XJep();
        jep.addStandardConstants();
        jep.addStandardFunctions();
        jep.setAllowUndeclared(true);
        jep.setAllowAssignment(true);
        jep.setImplicitMul(true);
        lessEqualConstrains = (ArrayList<Constraint>) fitness.lessEqualConstrains.clone();
        equalConstrains = (ArrayList<Constraint>) fitness.equalConstrains.clone();
        greaterEqualConstrains = (ArrayList<Constraint>) fitness.greaterEqualConstrains.clone();
        lower = fitness.lower;
        upper = fitness.upper;
        maximize = fitness.maximize;
    }

    private void initializateParser() {
    }

    private void firstTime() {
        greaterEqualNodes = new ArrayList<Node>();
        equalNodes = new ArrayList<Node>();
        lessEqualNodes = new ArrayList<Node>();
        try {
            for (Constraint constraint : lessEqualConstrains) {
                Node node = jep.parse(constraint.function);
                lessEqualNodes.add(node);
            }
            for (Constraint constraint : equalConstrains) {
                Node node = jep.parse(constraint.function);
                equalNodes.add(node);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public double evaluateConstrains(int power) throws ParseException {
        double total = 0;
        if (firstTime) {
            firstTime();
            firstTime = false;
        }
        for (Node node : lessEqualNodes) {
            double result = ((Double) jep.evaluate(node)).doubleValue();
            if (result > 0) {
                if (maximize) total -= Math.pow(result, power); else total += Math.pow(result, power);
            }
        }
        for (Node node : equalNodes) {
            double result = ((Double) jep.evaluate(node)).doubleValue();
            if (result != 0) {
                if (result < 0) result *= -1;
                if (maximize) total -= Math.pow(result, power); else total += Math.pow(result, power);
            }
        }
        Execution execution = getExecution();
        if (execution != null) {
            int t = execution.getCurrentIteration();
            total *= Math.pow(c * t, a);
        } else {
            total *= Math.pow(c, a);
        }
        return total;
    }

    public void addLessEqualConstrain(String leftPart, String rightPart) {
        Constraint constraint = new Constraint();
        constraint.originalFunction = leftPart + " <= " + rightPart;
        constraint.function = leftPart + "-1*(" + rightPart + ")";
        lessEqualConstrains.add(constraint);
    }

    public void addGreaterEqualConstrain(String leftPart, String rightPart) {
        Constraint constraint = new Constraint();
        constraint.originalFunction = leftPart + " >= " + rightPart;
        constraint.function = "-1(" + leftPart + ") + (" + rightPart + ")";
        lessEqualConstrains.add(constraint);
    }

    public void addEqualConstrain(String leftPart, String rightPart) {
        Constraint constraint = new Constraint();
        constraint.originalFunction = leftPart + " == " + rightPart;
        constraint.function = leftPart + " -1*(" + rightPart + ")";
        equalConstrains.add(constraint);
    }

    public void addConstraint(Constraint constraint) {
        if (constraint.originalFunction.indexOf("<=") != -1) {
            lessEqualConstrains.add(constraint);
            return;
        }
        if (constraint.originalFunction.indexOf(">=") != -1) {
            greaterEqualConstrains.add(constraint);
            return;
        }
        if (constraint.originalFunction.indexOf("==") != -1) {
            equalConstrains.add(constraint);
            return;
        }
        return;
    }

    @Override
    public double fitness(Individual ind) {
        if (!(ind instanceof FOIndividual)) {
            throw new RuntimeException("Invalid Individual class, get " + ind.getClass().toString() + ", expected FOIndividual");
        }
        double result = evaluation(ind);
        double constrains = 0;
        try {
            constrains = evaluateConstrains(1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        result += constrains;
        double fitness = -1 * (result - lower) / (lower - upper);
        if (fitness < 0) fitness = 0;
        if (fitness > 1) fitness = 1;
        if (!maximize) fitness = 1 - fitness;
        return fitness;
    }

    public double evaluation(Individual ind) {
        decodeValues(ind);
        double result = evaluateindividual();
        ind.setValue(result);
        return result;
    }

    private double evaluateindividual() {
        String result = "NaN";
        try {
            if (functionNode == null) functionNode = jep.parse(function);
            result = (jep.evaluate(functionNode)).toString();
        } catch (ParseException e) {
            return 0.0;
        }
        return (Double.parseDouble(result));
    }

    private void decodeValues(Individual ind) {
        TreeMap<String, Object> gen = ind.getDetailedGen();
        int i = 0;
        for (String key : gen.keySet()) {
            Object alell = gen.get(key);
            if (alell instanceof Double) {
                Node node;
                try {
                    node = jep.parse(key + " = " + alell.toString());
                    jep.evaluate(node);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                continue;
            }
            if (alell instanceof ArrayList) {
                Double values[];
                try {
                    ArrayList<Double> valuesArray = ((ArrayList<Double>) alell);
                    values = new Double[valuesArray.size()];
                    for (int j = 0; j < values.length; j++) {
                        values[j] = valuesArray.get(j);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("Critical error, can't continue");
                }
                String expression = key + "=[";
                for (int j = 0; j < values.length; j++) {
                    if (j == values.length - 1) {
                        expression += values[j];
                    } else {
                        expression += values[j].toString() + ",";
                    }
                }
                expression += "]";
                Node node;
                try {
                    node = jep.parse(expression);
                    jep.evaluate(node);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                continue;
            }
            throw new RuntimeException("Bad Coded Individual");
        }
    }

    public void fitness(Population pop) {
        double min = 1.0;
        for (Individual ind : pop.getIndividuals()) {
            if (ind.getFitnessValue() <= 0) {
                double fitness = fitness(ind);
                ind.setFitnessValue(fitness);
                if (fitness != 0 && fitness < min) {
                    min = fitness;
                }
            } else {
                double fitness = ind.getFitnessValue();
                if (fitness != 0 && fitness < min) {
                    min = fitness;
                }
            }
        }
        if (min <= 0 || min == 1.0) min = 0.00001;
        for (Individual ind : pop.getIndividuals()) {
            if (ind.getFitnessValue() <= 0) {
                ind.setFitnessValue(min);
            }
        }
    }

    public void setMaximize() {
        maximize = true;
    }

    public void setMinimize() {
        maximize = false;
    }

    public ArrayList<Constraint> getConstraints() {
        ArrayList<Constraint> constraintList = new ArrayList<Constraint>();
        constraintList.addAll(lessEqualConstrains);
        constraintList.addAll(greaterEqualConstrains);
        constraintList.addAll(equalConstrains);
        return constraintList;
    }

    public static void main(String args[]) {
        FOFitness fitness = new FOFitness("5x[1]+5x[2]+5x[3]+5x[4]-5*Sum(x[i]^2,i,1,4)-Sum(x[i],i,5,13)");
        FOIndividual ind = new FOIndividual();
        double gen[] = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 3, 3, 3, 1 };
        ind.setGen(gen);
        Variable variable = new Variable("x", 13);
        Vector<Variable> variableList = new Vector<Variable>();
        variableList.add(variable);
        ind.setVariables(variableList);
        System.out.println(fitness.evaluation(ind));
    }

    public double getLower() {
        return lower;
    }

    public void setLower(double lower) {
        this.lower = lower;
    }

    public double getUpper() {
        return upper;
    }

    public void setUpper(double upper) {
        this.upper = upper;
    }

    public boolean isMaximize() {
        return maximize;
    }

    public void setMaximize(boolean maximize) {
        this.maximize = maximize;
    }

    public Object clone() {
        return new FOFitness(this);
    }
}
