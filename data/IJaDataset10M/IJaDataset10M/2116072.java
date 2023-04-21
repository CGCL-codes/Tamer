package physis.core;

import physis.core.event.*;
import physis.core.nursing.Nurse;
import physis.core.nursing.NurseFactory;
import physis.core.lifespace.LifeSpace;
import physis.core.iterator.DigitalOrganismIterator;
import physis.core.genotype.GeneBank;
import physis.core.task.TaskLibrary;
import physis.core.random.Randomness;
import physis.core.random.RandomnessFactory;
import physis.log.Log;
import java.util.*;

/**
 * The default implementation for the Environment
 */
public class EnvironmentImpl implements Environment {

    private static String TASK_FILE = "task_filename";

    private static String COPY_MUTATION_RATE = "copy_mutation_rate";

    private static String DIVIDE_MUTATION_RATE = "divide_mutation_rate";

    private static String INSERT_RATE = "divide_insert_rate";

    private static String DELETE_RATE = "divide_delete_rate";

    private static String INPUT_DATA_HIGHER_BOUND = "input_data_higher_bound";

    private Randomness rnd;

    private Nurse nurse;

    private TaskLibrary tasklibrary;

    private LifeSpace lifespace;

    private double copy_mutation_rate;

    private double divide_mutation_rate;

    private double insert_rate;

    private double delete_rate;

    private int input_data_higher_bound;

    /**
     * The constructor is private because it's a singleton. Can be instantiated by calling getInstance().
     */
    public EnvironmentImpl() {
        rnd = RandomnessFactory.createRandomness();
        tasklibrary = new TaskLibrary();
        tasklibrary.buildLibrary(Configuration.getPhysisHome() + Configuration.getProperty(TASK_FILE));
        copy_mutation_rate = Double.parseDouble(Configuration.getProperty(COPY_MUTATION_RATE));
        divide_mutation_rate = Double.parseDouble(Configuration.getProperty(DIVIDE_MUTATION_RATE));
        insert_rate = Double.parseDouble(Configuration.getProperty(INSERT_RATE));
        delete_rate = Double.parseDouble(Configuration.getProperty(DELETE_RATE));
        input_data_higher_bound = Integer.parseInt(Configuration.getProperty(INPUT_DATA_HIGHER_BOUND));
        nurse = NurseFactory.getNurse();
    }

    /** Returns the tasklibrary. */
    public TaskLibrary getTaskLibrary() {
        return tasklibrary;
    }

    public void setLifeSpace(LifeSpace lifespace_) {
        lifespace = lifespace_;
    }

    public LifeSpace getLifeSpace() {
        return lifespace;
    }

    public void interactionOccured(InteractionEvent ie) {
        try {
            DigitalOrganism actor = ie.getOrganism();
            actor.recalculateBonus(tasklibrary.checkIOActivity(actor.getMetabolism(), actor.getPerformedTasks()));
        } catch (Exception e) {
            Log.error(e);
        }
    }

    public void proliferationPerformed(ProliferationEvent pe) {
        try {
            DigitalOrganism parent = pe.getOrganism();
            if (Configuration.isGeneBankEnabled()) {
                GeneBank.register(parent.getGenome(), pe.getNewSeed().getGenome());
            }
            parent.recalculateEffectiveLength();
            parent.recalculateFitness();
            parent.setFertile(true);
            nurse.placeNewBorn(pe.getNewSeed(), lifespace.getNeighbours(parent), parent.getMerit());
            parent.getVM().restart();
        } catch (Exception e) {
            Log.error(e);
        }
    }

    /**
     * This provides the input from the environment (~food).
     */
    public int getInputData() {
        return rnd.nextInt(input_data_higher_bound);
    }

    /**
     * Rotates the organism's facing forward.
     */
    public void rotateForward(DigitalOrganism digorg) {
        try {
            DigitalOrganism current = digorg.getNeighbour();
            DigitalOrganismIterator neighbours = lifespace.getNeighbours(digorg);
            while (!current.equals(neighbours.next()) && neighbours.hasNext()) {
            }
            if (neighbours.hasNext()) {
                digorg.setNeighbour(neighbours.next());
            } else {
                neighbours.reset();
                digorg.setNeighbour(neighbours.next());
            }
        } catch (Exception e) {
            Log.error(e);
        }
    }

    /**
     * Answers the question: Should the current copied instruction be mutated?
     */
    public boolean copyShouldBeMutated() {
        return rnd.nextDouble() < copy_mutation_rate;
    }

    /**
     * Answers the question: Should the current divided codetape be mutated?
     */
    public boolean divideShouldBeMutated() {
        return rnd.nextDouble() < divide_mutation_rate;
    }

    /**
     * Answers the question: Should the current divided codetape be inserted extra instructions?
     */
    public boolean shouldBeInserted() {
        return rnd.nextDouble() < insert_rate;
    }

    /**
     * Answers the question: Should the current divided codetape be deleted one instruction?
     */
    public boolean shouldBeDeleted() {
        return rnd.nextDouble() < delete_rate;
    }

    public DigitalOrganism getNeighbourRandomly(DigitalOrganism digorg) {
        return lifespace.getNeighbours(digorg).randomly();
    }

    public Randomness getRandomness() {
        return rnd;
    }
}
