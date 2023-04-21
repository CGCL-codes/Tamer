package jeco.kernel.algorithm.mopso;

import java.util.ArrayList;
import java.util.Collections;
import jeco.kernel.algorithm.Algorithm;
import jeco.kernel.problem.Problem;
import jeco.kernel.problem.Solution;
import jeco.kernel.problem.Solutions;
import jeco.kernel.util.RandomGenerator;
import jeco.kernel.operator.assigner.CrowdingDistance;
import jeco.kernel.operator.assigner.FrontsExtractor;
import jeco.kernel.operator.comparator.PropertyComparator;
import jeco.kernel.operator.comparator.SolutionDominance;
import jeco.kernel.operator.mutator.NonUniformMutation;
import jeco.kernel.operator.mutator.UniformMutation;
import jeco.kernel.problem.Variable;

/**
 * 
 * Input parameters:
 * - C10: HNSPSO C1(0) factor
 * - C1T: HNSPSO C1(T) factor
 * - C20: HNSPSO C2(0) factor
 * - C2T: HNSPSO C2(T) factor
 * - CHI: PSO chi factor
 * - NUM_PARTICLES
 * - T: maxIterations
 * - TOP_PART_PERCENTAGE: 0.05 usually
 * - W0: HNSPSO W(0) factor
 * - WT: HNSPSO W(T) factor
 * 
 * @author José L. Risco-Martín
 *
 */
public class HNSPSO<V extends Variable<Double>> extends Algorithm<V> {

    public static final double DEFAULT_W0 = 0.7;

    public static final double DEFAULT_WT = 0.4;

    public static final double DEFAULT_C10 = 2.5;

    public static final double DEFAULT_C1T = 0.5;

    public static final double DEFAULT_C20 = 0.5;

    public static final double DEFAULT_C2T = 2.5;

    public static final double DEFAULT_CHI = 1.0;

    /** PSO c1 factor */
    private Double c10;

    private Double c1T;

    /** PSO c2 factor */
    private Double c20;

    private Double c2T;

    /** PSO chi factor */
    private double chi;

    /** max iterations */
    protected int maxT;

    /** PSOList size */
    private int swarmSize;

    /** PSO w factor */
    private Double w0;

    private Double wT;

    /** Iterations */
    protected int t;

    /** PSOList */
    protected Solutions<V> swarm;

    protected Solutions<V> leaders;

    /** Dominance operator */
    private SolutionDominance<V> objectivesComparator;

    /** Sorting assigner */
    private CrowdingDistance<V> crowdingDistanceAssigner;

    /** Sorting comparator */
    private PropertyComparator<V> crowdingDistanceComparator;

    /** Extractor */
    private FrontsExtractor<V> frontsExtractor;

    private ArrayList<Solution<V>> personalBests;

    private double[][] speeds;

    private UniformMutation<V> uniformMutation;

    private NonUniformMutation<V> nonUniformMutation;

    /** Dynamic parameters */
    protected boolean dynamicParameters = true;

    protected boolean dynamicVelocity = false;

    private double[] delta;

    public HNSPSO(Problem<V> problem, int swarmSize, int maxT, double w0, double wT, double c10, double c1T, double c20, double c2T, double chi) {
        super("HNSPSO", problem);
        this.swarmSize = swarmSize;
        this.maxT = maxT;
        this.w0 = w0;
        this.wT = wT;
        this.c10 = c10;
        this.c1T = c1T;
        this.c20 = c20;
        this.c2T = c2T;
        this.chi = chi;
    }

    public HNSPSO(Problem<V> problem, int swarmSize, int maxT, double w0, double wT, double c10, double c1T, double c20, double c2T) {
        this(problem, swarmSize, maxT, w0, wT, c10, c1T, c20, c2T, DEFAULT_CHI);
    }

    public HNSPSO(Problem<V> problem, int swarmSize, int maxT) {
        this(problem, swarmSize, maxT, DEFAULT_W0, DEFAULT_WT, DEFAULT_C10, DEFAULT_C1T, DEFAULT_C20, DEFAULT_C2T, DEFAULT_CHI);
    }

    public HNSPSO(Problem<V> problem, int swarmSize, int maxT, boolean dynamicParameters, boolean dynamicVelocity) {
        this(problem, swarmSize, maxT);
        this.dynamicParameters = dynamicParameters;
        this.dynamicVelocity = dynamicVelocity;
    }

    public void initialize() {
        swarm = new Solutions<V>();
        for (int i = 0; i < swarmSize; ++i) {
            Solution<V> sol = new Solution<V>(problem);
            swarm.add(sol);
        }
        problem.newRandomSetOfSolutions(swarm);
        personalBests = new ArrayList<Solution<V>>();
        for (int i = 0; i < swarmSize; ++i) personalBests.add(new Solution<V>(problem));
        leaders = new Solutions<V>();
        objectivesComparator = new SolutionDominance<V>();
        crowdingDistanceComparator = new PropertyComparator<V>(CrowdingDistance.propertyCrowdingDistance);
        crowdingDistanceAssigner = new CrowdingDistance<V>(problem.getNumberOfObjectives());
        speeds = new double[swarmSize][problem.getNumberOfVariables()];
        if (dynamicVelocity) {
            delta = new double[problem.getNumberOfVariables()];
            for (int j = 0; j < problem.getNumberOfVariables(); j++) {
                delta[j] = (problem.getUpperBound(j) - problem.getLowerBound(j)) / 2;
            }
        }
        uniformMutation = new UniformMutation<V>(problem);
        nonUniformMutation = new NonUniformMutation<V>(problem, maxT);
        frontsExtractor = new FrontsExtractor<V>(objectivesComparator);
        t = 0;
        for (int i = 0; i < swarmSize; ++i) {
            Solution<V> particle = swarm.get(i);
            problem.evaluate(particle);
            for (int j = 0; j < problem.getNumberOfVariables(); j++) {
                speeds[i][j] = 0.0;
            }
            leaders.add(particle.clone());
            personalBests.set(i, particle.clone());
        }
        reduceLeaders(2 * swarmSize);
    }

    public void reduceLeaders(int maxSize) {
        leaders.keepParetoNonDominated(objectivesComparator);
        if (leaders.size() <= maxSize) {
            return;
        }
        crowdingDistanceAssigner.execute(leaders);
        Collections.sort(leaders, crowdingDistanceComparator);
        while (leaders.size() > maxSize) {
            leaders.remove(0);
        }
    }

    private void computeSpeed() {
        double r1, r2, w, c1, c2, cd;
        Solutions<V> referenceFront = null;
        Solution<V> globalBest, personalBest, particle;
        V vPart, pBest, gBest;
        int referenceFrontSize = 0;
        crowdingDistanceAssigner.execute(leaders);
        crowdingDistanceAssigner.execute(swarm);
        ArrayList<Solutions<V>> fronts = frontsExtractor.execute(swarm);
        for (int i = 0; i < swarmSize; i++) {
            particle = swarm.get(i);
            personalBest = personalBests.get(i);
            int referenceFrontIndex = particle.getProperties().get(FrontsExtractor.propertyRank).intValue() - 1 - 1;
            if (referenceFrontIndex < 0 || referenceFrontIndex >= fronts.size()) {
                referenceFront = leaders;
            } else {
                referenceFront = fronts.get(referenceFrontIndex);
            }
            referenceFrontSize = referenceFront.size();
            int pos1 = RandomGenerator.nextInt(0, referenceFrontSize);
            int pos2 = RandomGenerator.nextInt(0, referenceFrontSize);
            Solution<V> one = referenceFront.get(pos1);
            Solution<V> two = referenceFront.get(pos2);
            if (crowdingDistanceComparator.compare(two, one) < 1) {
                globalBest = one;
            } else {
                globalBest = two;
            }
            r1 = RandomGenerator.nextDouble();
            r2 = RandomGenerator.nextDouble();
            cd = particle.getProperties().get(CrowdingDistance.propertyCrowdingDistance).doubleValue();
            if (cd > 1.0) cd = 1.0;
            w = (wT - w0) * ((1.0 * t) / maxT) * Math.exp(-cd) + w0 * Math.exp(-cd);
            c1 = (c1T - c10) * ((1.0 * t) / maxT) + c10;
            c2 = (c2T - c20) * ((1.0 * t) / maxT) + c20;
            if (!dynamicParameters) {
                c1 = RandomGenerator.nextDouble(NSPSO.DEFAULT_C1 - 0.5, NSPSO.DEFAULT_C1);
                c2 = RandomGenerator.nextDouble(NSPSO.DEFAULT_C2 - 0.5, NSPSO.DEFAULT_C2);
                w = RandomGenerator.nextDouble(NSPSO.DEFAULT_W - 0.3, NSPSO.DEFAULT_W);
            }
            for (int j = 0; j < problem.getNumberOfVariables(); ++j) {
                vPart = particle.getVariables().get(j);
                pBest = personalBest.getVariables().get(j);
                gBest = globalBest.getVariables().get(j);
                speeds[i][j] = w * speeds[i][j] + c1 * r1 * (pBest.getValue() - vPart.getValue()) + c2 * r2 * (gBest.getValue() - vPart.getValue());
                if (dynamicVelocity) {
                    if (speeds[i][j] > delta[j]) speeds[i][j] = delta[j];
                    if (speeds[i][j] <= -delta[j]) speeds[i][j] = -delta[j];
                }
            }
        }
    }

    private void computeNewPositions() {
        for (int i = 0; i < swarmSize; i++) {
            Solution<V> particle = swarm.get(i);
            for (int j = 0; j < problem.getNumberOfVariables(); j++) {
                V variable = particle.getVariables().get(j);
                variable.setValue(variable.getValue() + speeds[i][j]);
                if (variable.getValue() < problem.getLowerBound(j)) {
                    variable.setValue(problem.getLowerBound(j));
                    speeds[i][j] = speeds[i][j] * -1.0;
                }
                if (variable.getValue() > problem.getUpperBound(j)) {
                    variable.setValue(problem.getUpperBound(j));
                    speeds[i][j] = speeds[i][j] * -1.0;
                }
            }
        }
    }

    private void mutation() {
        nonUniformMutation.setCurrentIteration(t);
        for (int i = 0; i < swarmSize; i++) {
            if (i % 3 == 0) {
                nonUniformMutation.execute(swarm.get(i));
            } else if (i % 3 == 1) {
                uniformMutation.execute(swarm.get(i));
            } else ;
        }
    }

    public Solutions<V> execute() {
        while (t < maxT) {
            step();
        }
        return leaders;
    }

    public void step() {
        t++;
        computeSpeed();
        computeNewPositions();
        mutation();
        for (int i = 0; i < swarmSize; i++) {
            Solution<V> particle = swarm.get(i);
            problem.evaluate(particle);
            int flag = objectivesComparator.compare(particle, personalBests.get(i));
            if (flag != 1) {
                personalBests.set(i, particle.clone());
            }
            leaders.add(particle.clone());
            leaders.add(personalBests.get(i));
        }
        reduceLeaders(2 * swarmSize);
    }

    public void setMaxT(int maxT) {
        this.maxT = maxT;
    }

    public void setSwarmSize(int swarmSize) {
        this.swarmSize = swarmSize;
    }
}
