package jeco.kernel.algorithm.mopso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;
import jeco.kernel.algorithm.Algorithm;
import jeco.kernel.problem.Problem;
import jeco.kernel.problem.Solution;
import jeco.kernel.problem.Solutions;
import jeco.kernel.util.RandomGenerator;
import jeco.kernel.operator.assigner.CrowdingDistance;
import jeco.kernel.operator.assigner.NicheCount;
import jeco.kernel.operator.comparator.PropertyComparator;
import jeco.kernel.operator.comparator.SolutionDominance;
import jeco.kernel.problem.Variable;

/**
 * Xiaodong Li
 * A Non-dominated Sorting Particle Swarm Optimizer for Multiobjective 
 * Optimization
 * 
 * GECCO-2003, Springer-Verlag, 2003, 2723, 37-48
 * 
 * Input parameters:
 * - C1: PSO c1 factor
 * - C2: PSO c2 factor
 * - CHI: PSO chi factor
 * - MAX_ITERATIONS
 * - NUM_PARTICLES
 * - SORTING_METHOD: CROWDING_DISTANCE, CROWDING_DISTANCE_REPLACE, NICHE_COUNT, NICHE_COUNT_REPLACE
 * - TOP_PART_PERCENTAGE: 0.05 usually, but I am using 0.25
 * - W: PSO w factor
 * 
 * @author José L. Risco-Martín
 *
 */
public class NSPSO<V extends Variable<Double>> extends Algorithm<V> {

    private static final Logger logger = Logger.getLogger(NSPSO.class.getName());

    public static final double DEFAULT_W = 0.4;

    public static final double DEFAULT_C1 = 2.0;

    public static final double DEFAULT_C2 = 2.0;

    public static final double DEFAULT_CHI = 1.0;

    /** PSO c1 factor */
    private double c1;

    /** PSO c2 factor */
    private double c2;

    /** PSO chi factor */
    protected double chi;

    /** max iterations */
    protected int maxT;

    /** PSOList size */
    private int swarmSize;

    /** Sorting method */
    private String sortingMethod;

    /** number of non dominated particles (percentage) */
    private double topPartPercentage;

    /** PSO w factor */
    private double w;

    /** Iterations */
    protected int t;

    /** PSOList */
    protected Solutions<V> swarm;

    protected Solutions<V> leaders;

    /** Dominance operator */
    private SolutionDominance<V> dominance;

    private ArrayList<Solution<V>> personalBests;

    private double[][] speeds;

    /** Dynamic parameters */
    protected boolean dynamicParameters = false;

    protected boolean dynamicVelocity = false;

    private double[] delta;

    public NSPSO(Problem<V> problem, int numParticles, int maxIterations, double w, double c1, double c2, double chi, double topPartPercentage, String sortingMethod) {
        super("NSPSO", problem);
        this.swarmSize = numParticles;
        this.maxT = maxIterations;
        this.w = w;
        this.c1 = c1;
        this.c2 = c2;
        this.chi = chi;
        this.topPartPercentage = topPartPercentage;
        this.sortingMethod = sortingMethod;
    }

    public NSPSO(Problem<V> problem, int numParticles, int maxIterations, double w, double c1, double c2, double chi) {
        this(problem, numParticles, maxIterations, w, c1, c2, chi, 0.25, "CROWDING_DISTANCE");
    }

    public NSPSO(Problem<V> problem, int numParticles, int maxIterations, double w, double c1, double c2) {
        this(problem, numParticles, maxIterations, w, c1, c2, DEFAULT_CHI, 0.25, "CROWDING_DISTANCE");
    }

    public NSPSO(Problem<V> problem, int numParticles, int maxIterations) {
        this(problem, numParticles, maxIterations, DEFAULT_W, DEFAULT_C1, DEFAULT_C2);
    }

    public NSPSO(Problem<V> problem, int numParticles, int maxIterations, boolean dynamicParameters, boolean dynamicVelocity) {
        this(problem, numParticles, maxIterations);
        this.dynamicParameters = dynamicParameters;
        this.dynamicVelocity = dynamicVelocity;
    }

    @Override
    public void initialize() {
        swarm = new Solutions<V>();
        for (int i = 0; i < swarmSize; ++i) {
            Solution<V> sol = new Solution<V>(problem);
            swarm.add(sol);
        }
        problem.newRandomSetOfSolutions(swarm);
        leaders = new Solutions<V>();
        dominance = new SolutionDominance<V>();
        for (Solution<V> particle : swarm) {
            problem.evaluate(particle);
            leaders.add(particle.clone());
        }
        reduceExternalArchive(2 * swarmSize);
        speeds = new double[swarmSize][problem.getNumberOfVariables()];
        if (dynamicVelocity) {
            delta = new double[problem.getNumberOfVariables()];
            for (int j = 0; j < problem.getNumberOfVariables(); j++) {
                delta[j] = (problem.getUpperBound(j) - problem.getLowerBound(j)) / 2;
            }
        }
        personalBests = new ArrayList<Solution<V>>();
        for (int i = 0; i < swarmSize; ++i) {
            Solution<V> particle = swarm.get(i);
            for (int j = 0; j < problem.getNumberOfVariables(); ++j) {
                double value = RandomGenerator.nextDouble(problem.getLowerBound(j), problem.getUpperBound(j));
                if (RandomGenerator.nextDouble() < 0.5) {
                    speeds[i][j] = -value;
                } else {
                    speeds[i][j] = value;
                }
            }
            personalBests.add(particle.clone());
        }
        t = 0;
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
        for (int i = 0; i < swarmSize; i++) {
            Solution<V> particle = swarm.get(i);
            problem.evaluate(particle);
        }
        for (int i = 0; i < swarmSize; i++) {
            Solution<V> particle = swarm.get(i);
            int flag = dominance.compare(particle, personalBests.get(i));
            if (flag < 0) {
                personalBests.set(i, particle.clone());
            }
        }
        for (int i = 0; i < swarmSize; i++) {
            Solution<V> particle = swarm.get(i);
            leaders.add(particle.clone());
        }
        for (int i = 0; i < swarmSize; ++i) {
            leaders.add(personalBests.get(i));
        }
        reduceExternalArchive(2 * swarmSize);
    }

    private void computeSpeed() {
        if (leaders.size() <= 0) {
            System.out.println("External archive is zero.");
            Solution<V> particle = new Solution<V>(problem);
            problem.evaluate(particle);
            leaders.add(particle);
        }
        if (sortingMethod.indexOf("CROWDING_DISTANCE") == 0) {
            CrowdingDistance<V> assigner = new CrowdingDistance<V>(problem.getNumberOfObjectives());
            assigner.execute(leaders);
            Collections.sort(leaders, new PropertyComparator<V>(CrowdingDistance.propertyCrowdingDistance));
            if (dynamicParameters) {
                assigner.execute(swarm);
            }
        } else if (sortingMethod.indexOf("NICHE_COUNT") == 0) {
            NicheCount<V> assigner = new NicheCount<V>(problem.getNumberOfObjectives());
            assigner.execute(leaders);
            Collections.sort(leaders, new PropertyComparator<V>(NicheCount.propertyNicheCount));
        } else {
            logger.severe("Sorting method not propertly defined: " + sortingMethod);
        }
        double r1, r2, W, C1, C2, cd;
        Solution<V> particle, personalBest, globalBest;
        V vPart, pBest, gBest;
        for (int i = 0; i < swarmSize; i++) {
            particle = swarm.get(i);
            personalBest = personalBests.get(i);
            int randomIndex = (int) (leaders.size() * topPartPercentage * RandomGenerator.nextDouble());
            if (sortingMethod.indexOf("CROWDING_DISTANCE") == 0) {
                randomIndex = (leaders.size() - 1) - randomIndex;
            }
            globalBest = leaders.get(randomIndex);
            r1 = RandomGenerator.nextDouble();
            r2 = RandomGenerator.nextDouble();
            if (dynamicParameters) {
                cd = particle.getProperties().get(CrowdingDistance.propertyCrowdingDistance).doubleValue();
                if (cd > 1.0) {
                    cd = 1.0;
                }
                W = (HNSPSO.DEFAULT_WT - HNSPSO.DEFAULT_W0) * ((1.0 * t) / maxT) * Math.exp(-cd) + HNSPSO.DEFAULT_W0 * Math.exp(-cd);
                C1 = (HNSPSO.DEFAULT_C1T - HNSPSO.DEFAULT_C10) * ((1.0 * t) / maxT) + HNSPSO.DEFAULT_C10;
                C2 = (HNSPSO.DEFAULT_C2T - HNSPSO.DEFAULT_C20) * ((1.0 * t) / maxT) + HNSPSO.DEFAULT_C20;
            } else {
                C1 = RandomGenerator.nextDouble(Math.min(1.5, c1), Math.max(1.5, c1));
                C2 = RandomGenerator.nextDouble(Math.min(1.5, c2), Math.max(1.5, c2));
                W = RandomGenerator.nextDouble(Math.min(0.1, w), Math.max(0.1, w));
            }
            for (int j = 0; j < problem.getNumberOfVariables(); j++) {
                vPart = particle.getVariables().get(j);
                pBest = personalBest.getVariables().get(j);
                gBest = globalBest.getVariables().get(j);
                speeds[i][j] = W * speeds[i][j] + C1 * r1 * (pBest.getValue() - vPart.getValue()) + C2 * r2 * (gBest.getValue() - vPart.getValue());
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
                variable.setValue(variable.getValue() + chi * speeds[i][j]);
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

    public void reduceExternalArchive(int maxSize) {
        leaders.keepParetoNonDominated(dominance);
        if (leaders.size() <= maxSize) {
            return;
        }
        if (sortingMethod.indexOf("CROWDING_DISTANCE") == 0) {
            CrowdingDistance<V> assigner = new CrowdingDistance<V>(problem.getNumberOfObjectives());
            assigner.execute(leaders);
            Collections.sort(leaders, new PropertyComparator<V>(CrowdingDistance.propertyCrowdingDistance));
        } else if (sortingMethod.indexOf("NICHE_COUNT") == 0) {
            NicheCount<V> assigner = new NicheCount<V>(problem.getNumberOfObjectives());
            assigner.execute(leaders);
            Collections.sort(leaders, new PropertyComparator<V>(NicheCount.propertyNicheCount));
        } else {
            logger.severe("Sorting method not propertly defined: " + sortingMethod);
        }
        while (leaders.size() > maxSize) {
            if (sortingMethod.indexOf("CROWDING_DISTANCE") == 0) {
                leaders.remove(0);
            } else {
                leaders.remove(leaders.size() - 1);
            }
        }
        if (sortingMethod.indexOf("_REPLACE") > 0) {
            Solution<V> pI = new Solution<V>(problem);
            problem.evaluate(pI);
            if (sortingMethod.indexOf("CROWDING_DISTANCE") == 0) {
                leaders.set(0, pI);
            } else {
                leaders.set(leaders.size() - 1, pI);
            }
        }
    }

    public void setMaxT(int maxT) {
        this.maxT = maxT;
    }

    public void setSwarmSize(int swarmSize) {
        this.swarmSize = swarmSize;
    }
}
