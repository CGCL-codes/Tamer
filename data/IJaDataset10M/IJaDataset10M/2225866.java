package game.trainers.ant.caco;

import game.trainers.gradient.Newton.Uncmin_methods;
import java.util.Random;
import java.text.DecimalFormat;

public class Colony {

    int directionsCount;

    private int maxIterations;

    private int maxStagnation;

    double searchRadius;

    private double radiusMultiplier;

    private int radiusGeneration;

    private double startingPheromone;

    private double minimumPheromone;

    private double addPheromone;

    private double evaporation;

    private double gradientWeight;

    private boolean debugOn;

    private DecimalFormat df;

    private int iteration;

    private int stagnation;

    private Uncmin_methods trainer;

    int dimensions;

    private double[] nest;

    Direction directions[];

    private Random generator;

    private double[] gBestVector;

    private double gBestError;

    public Colony(Uncmin_methods train, int dimensions, int maxIterations, int maxStagnation, double searchRadius, int directionsCount, double radiusMultiplier, int radiusGeneration, double startingPheromone, double minimumPheromone, double addPheromone, double evaporation, double gradientWeight, boolean debugOn) {
        generator = new Random();
        this.searchRadius = searchRadius;
        this.directionsCount = directionsCount;
        this.maxIterations = maxIterations;
        this.maxStagnation = maxStagnation;
        this.radiusMultiplier = radiusMultiplier;
        this.radiusGeneration = radiusGeneration;
        this.startingPheromone = startingPheromone;
        this.minimumPheromone = minimumPheromone;
        this.addPheromone = addPheromone;
        this.evaporation = evaporation;
        this.dimensions = dimensions;
        this.gradientWeight = gradientWeight;
        this.debugOn = debugOn;
        trainer = train;
        df = new DecimalFormat();
        df.setMaximumFractionDigits(4);
        df.setMinimumFractionDigits(4);
        nest = new double[directionsCount];
        directions = new Direction[directionsCount];
        gBestError = Double.POSITIVE_INFINITY;
    }

    /** main purpose for this is destoying window */
    public void destroy() {
        df = null;
        nest = null;
        directions = null;
        generator = null;
    }

    /** directions  */
    void colonyDump() {
        for (int i = 0; i < directionsCount; i++) {
            System.out.print(df.format(directions[i].getPheromone()) + " ");
        }
        System.out.println();
    }

    /** main body of algorithm */
    public void run() {
        for (int i = 0; i < directionsCount; i++) nest[i] = (Math.random() * 20.0) - 10.0;
        for (int i = 0; i < directionsCount; i++) directions[i] = new Direction(startingPheromone, minimumPheromone, addPheromone, evaporation, dimensions, trainer, gradientWeight);
        iteration = 1;
        stagnation = 0;
        do {
            int vecnum;
            double rand;
            double sum = 0;
            for (int d = 0; d < directionsCount; d++) sum += directions[d].getPheromone();
            rand = generator.nextDouble() * sum;
            vecnum = 0;
            sum = 0;
            while (sum < rand) {
                sum += directions[vecnum].getPheromone();
                vecnum++;
            }
            if (vecnum > 0) vecnum--;
            directions[vecnum].explore(searchRadius);
            if (iteration % radiusGeneration == 0) {
                searchRadius = searchRadius * radiusMultiplier;
                for (int i = 0; i < directionsCount; i++) directions[i].evaporatePheromone();
                if (debugOn) System.out.println("Iteration: " + iteration + "; Radius: " + df.format(searchRadius) + "; error: " + df.format(gBestError));
                if (debugOn) colonyDump();
            }
            for (int d = 0; d < directionsCount; d++) {
                if (directions[d].getgBestError() < gBestError) {
                    gBestError = directions[d].getgBestError();
                    stagnation = 0;
                }
            }
            for (int d = 0; d < directionsCount; d++) {
                directions[d].setgBestError(gBestError);
            }
        } while ((stagnation++ < maxStagnation) && (iteration++ < maxIterations));
    }

    /** returns global best solution
     * @param i
     * @return
     * @return
     * @return*/
    public double getBest(int i) {
        return gBestVector[i];
    }

    /** returns global best error
     * @return
     * @return
     * @return*/
    public double getError() {
        return gBestError;
    }
}
