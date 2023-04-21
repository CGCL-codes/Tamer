package es.ulpgc.dis.heuriskein.model.solver.ga.operators;

import java.util.ArrayList;
import es.ulpgc.dis.heuriskein.model.solver.Individual;
import es.ulpgc.dis.heuriskein.model.solver.Population;
import es.ulpgc.dis.heuriskein.model.solver.problems.BoundaryLimits;
import es.ulpgc.dis.heuriskein.model.solver.problems.Variable;
import es.ulpgc.dis.heuriskein.model.solver.representation.FOIndividual;
import es.ulpgc.dis.heuriskein.utils.RandomNumbers;

public class RealArithmeticCrossover extends CrossoverOperator {

    private double a;

    private int k = 3;

    public RealArithmeticCrossover(RealArithmeticCrossover realArithmeticCrossover) {
        super(realArithmeticCrossover);
        this.k = realArithmeticCrossover.k;
    }

    public RealArithmeticCrossover() {
        super();
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
    }

    public Class[] getType() {
        return new Class[] { FOIndividual.class };
    }

    @Override
    public void crossover(Individual parent1, Individual parent2) {
        FOIndividual father = (FOIndividual) parent1;
        FOIndividual mother = (FOIndividual) parent2;
        ArrayList fatherGen = father.getGen();
        double[] P1 = new double[fatherGen.size()];
        int i = 0;
        for (Object j : fatherGen) {
            P1[i++] = ((Double) j).doubleValue();
        }
        ArrayList motherGen = mother.getGen();
        double[] P2 = new double[motherGen.size()];
        i = 0;
        for (Object j : motherGen) {
            P2[i++] = ((Double) j).doubleValue();
        }
        FOIndividual offspring1;
        FOIndividual offspring2;
        double[] H1 = new double[P1.length];
        double[] H2 = new double[P1.length];
        if (k == 0) {
            k = RandomNumbers.nextInt(P1.length);
        }
        for (i = 0; i < P1.length; i++) {
            if (i < k) {
                H1[i] = P1[i];
                H2[i] = P2[i];
            } else {
                BoundaryLimits aLimits = calculate_a(P1, father.getBoundaryLimits(i), P2, mother.getBoundaryLimits(i), i);
                a = aLimits.lower + RandomNumbers.nextDouble() * (aLimits.upper - aLimits.lower);
                H1[i] = P2[i] * a + P1[i] * (1 - a);
                H2[i] = P1[i] * a + P2[i] * (1 - a);
            }
        }
        offspring1 = (FOIndividual) population.newIndividual();
        offspring1.setGen(H1);
        offspring2 = (FOIndividual) population.newIndividual();
        offspring2.setGen(H2);
        population.addIndividual(offspring1);
        population.addIndividual(offspring2);
    }

    private BoundaryLimits calculate_a(double[] P1, BoundaryLimits P1limits, double[] P2, BoundaryLimits P2limits, int i) {
        double upper = 0.0, lower = 0.0;
        if (P1[i] == P2[i]) {
            return new BoundaryLimits("a", 1, lower, upper);
        }
        double alpha = (P2limits.lower - P2[i]) / (P1[i] - P2[i]);
        double betha = (P1limits.upper - P1[i]) / (P2[i] - P1[i]);
        double gamma = (P1limits.lower - P1[i]) / (P2[i] - P1[i]);
        double delta = (P2limits.upper - P2[i]) / (P1[i] - P2[i]);
        if (P1[i] > P2[i]) {
            lower = Math.max(alpha, betha);
            upper = Math.min(gamma, delta);
        } else {
            lower = Math.max(gamma, delta);
            upper = Math.min(alpha, betha);
        }
        return new BoundaryLimits("a", 1, lower, upper);
    }

    public static void main(String args[]) {
        double gen1[] = { 2.3, 4.5, -1.2, 0.8 };
        double gen2[] = { 1.4, -0.2, 6.7, 4.8 };
        Variable variable = new Variable("x", 4);
        BoundaryLimits limit = new BoundaryLimits("x", 1, 4, -2, 7);
        FOIndividual parent1 = new FOIndividual();
        parent1.addVariable(variable);
        parent1.addBoundaryLimits(limit);
        parent1.setGen(gen1);
        FOIndividual parent2 = new FOIndividual();
        parent2.addVariable(variable);
        parent2.addBoundaryLimits(limit);
        parent2.setGen(gen2);
        RealArithmeticCrossover op = new RealArithmeticCrossover();
        BoundaryLimits limits = op.calculate_a(gen1, new BoundaryLimits("x", 1, -2, 7), gen2, new BoundaryLimits("x", 1, -2, 7), 2);
        Population population = new Population();
        population.addIndividual(parent1);
        population.addIndividual(parent2);
        op.setPopulation(population);
        op.crossover(parent1, parent2);
        System.out.println(population.toHumanReadableString());
        for (int i = 0; i < parent1.getGen().size(); i++) {
            System.out.println(parent1.getBoundaryLimits(i).toString());
        }
    }

    public String getName() {
        return "Real Arithmetic Crossover";
    }

    public Object clone() {
        return new RealArithmeticCrossover(this);
    }
}
