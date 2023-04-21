package jm.music.tools.ga;

import jm.music.data.Phrase;

/**
 * @author    Adam Kirby
 * @version   0.1.1, 11th December 2000
 */
public class SimpleParentSelector extends ParentSelector {

    public SimpleParentSelector() {
    }

    public Phrase[] selectParents(Phrase[] population, double[] fitness) {
        Phrase[] parents = new Phrase[population.length];
        for (int i = 0; i < population.length; i++) {
            parents[i] = population[i].copy();
        }
        return parents;
    }
}
