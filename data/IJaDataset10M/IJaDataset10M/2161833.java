package org.lindenb.lib.ga;

import java.util.Collections;
import java.util.Vector;

/**
 * @author pierre
 *
 * Implementation of a Genetic Algorithm
 */
public abstract class GAlgorithm {

    Vector solutions;

    int nGeneration;

    public GAlgorithm() {
        this.solutions = new Vector(getPopulationSize() * getPopulationSize(), getPopulationSize());
        this.nGeneration = 0;
        for (int i = 0; i < getPopulationSize(); ++i) {
            this.solutions.addElement(createNewIndividual());
        }
    }

    public int getGenerationCount() {
        return this.nGeneration;
    }

    public int getIndividualCount() {
        return this.solutions.size();
    }

    public GAIndividual getIndividualAt(int i) {
        return (GAIndividual) this.solutions.elementAt(i);
    }

    public int getPopulationSize() {
        return 2;
    }

    public double getProbaCrossOver() {
        return 0.3;
    }

    public double getProbaMutation() {
        return 0.3;
    }

    public double getExtraParentCount() {
        return 10;
    }

    public void doNGenerations(int n) {
        while (n > 0) {
            doOneGeneration();
            --n;
        }
    }

    public boolean doOneGeneration() {
        int nParent = this.solutions.size();
        GAIndividual best = getIndividualAt(0);
        ++this.nGeneration;
        for (int i = 0; i < getExtraParentCount(); ++i) {
            this.solutions.addElement(createNewIndividual());
        }
        for (int i = 0; i < nParent; ++i) {
            GAIndividual p1 = getIndividualAt(i);
            for (int j = 0; j < nParent; ++j) {
                GAIndividual p2 = getIndividualAt(j);
                GAIndividual c[] = p1.crossOver(p2, getProbaCrossOver());
                for (int k = 0; k < c.length; ++k) {
                    c[k].mute(getProbaMutation());
                    this.solutions.addElement(c[k]);
                }
            }
        }
        for (int i = 0; i < getExtraParentCount(); ++i) {
            this.solutions.addElement(createNewIndividual());
        }
        Collections.sort(this.solutions);
        int k = 0;
        while (k < this.solutions.size()) {
            if (k + 1 == this.solutions.size()) break;
            if (getIndividualAt(k).equals(getIndividualAt(k + 1))) {
                this.solutions.removeElementAt(k + 1);
            } else {
                ++k;
            }
        }
        this.solutions.setSize(Math.min(nParent, this.solutions.size()));
        return (!best.equals(getIndividualAt(0)));
    }

    public abstract GAIndividual createNewIndividual();
}
