package evolution;

import simbad.gui.Simbatch;
import contribs.maploader.MapLoaderBatDemo;

public class AlgoBat extends Algo1p1ES {

    static final int STEPS_BY_ROBOT = 100000;

    MapLoaderBatDemo env;

    Simbatch simbatch;

    double bestFitness;

    public AlgoBat(int dim, int range, int n) {
        super(dim, range, n);
        env = new MapLoaderBatDemo("resources/map1.png");
        simbatch = new Simbatch(env, true);
    }

    @Override
    double fitness(double[] v) {
        simbatch.reset();
        env.bat.setPerceptatorPoids(v);
        for (int i = 0; i < STEPS_BY_ROBOT; i++) {
            simbatch.step();
        }
        return env.bat.fitness();
    }

    void run(boolean adapt) {
        init();
        bestFitness = fitness(best);
        if (!adapt) sigma = range / 3; else sigma = 0.000001;
        int every = 1;
        StringBuffer sb = new StringBuffer();
        System.out.println("Debut");
        for (int i = 0; i < N; i++) {
            child = mutate(best, sigma);
            double childFitness = fitness(child);
            if (childFitness >= bestFitness) {
                best = child;
                bestFitness = childFitness;
                System.out.println("best = " + bestFitness);
                if (adapt) sigma *= 2;
            } else {
                if (adapt) sigma *= Math.pow(2.0, -0.25);
            }
        }
        System.out.println("Best : " + bestFitness);
    }

    public static void main(String[] args) {
        AlgoBat algoBat = new AlgoBat((Bat.NB_ENTREES + 1 + 2) * Bat.NB_COUCHES, 2, 10);
        algoBat.run(true);
    }
}
