package net.sf.simplecq.loadgen;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Holds several {@link LoadGenerator} and the probability of each of being
 * executed.
 * 
 * @author Sherif Behna
 */
public class LoadGeneratorDistribution implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean initialized = false;

    private LoadGenerator[] opsArray = new LoadGenerator[100];

    private List<LoadGeneratorEntry> entries = new ArrayList<LoadGeneratorEntry>();

    private Random random = new Random();

    public LoadGeneratorDistribution() {
        super();
    }

    public void add(int probability, LoadGenerator op) {
        entries.add(new LoadGeneratorEntry(probability, op));
    }

    public void init() {
        int total = 0;
        for (LoadGeneratorEntry entry : entries) {
            total += entry.getProbability();
        }
        if (total > 100) {
            throw new IllegalStateException("Probability is greater than 100. ");
        }
        int i = 0;
        for (LoadGeneratorEntry entry : entries) {
            for (int j = 0; j < entry.getProbability(); j++) {
                opsArray[i++] = entry.getOpration();
            }
        }
        initialized = true;
    }

    public LoadGenerator next() {
        if (!initialized) {
            throw new IllegalStateException("Not initialized. ");
        }
        int randomInt = random.nextInt(100);
        return opsArray[randomInt];
    }

    private class LoadGeneratorEntry implements Serializable {

        private static final long serialVersionUID = 1L;

        private int probability;

        private LoadGenerator opration;

        public int getProbability() {
            return probability;
        }

        public LoadGenerator getOpration() {
            return opration;
        }

        LoadGeneratorEntry(int probability, LoadGenerator opration) {
            super();
            this.probability = probability;
            this.opration = opration;
        }
    }
}
