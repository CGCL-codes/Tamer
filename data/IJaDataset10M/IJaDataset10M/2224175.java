package com.googlecode.kipler.container.dl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.googlecode.kipler.common.Copyable;
import com.googlecode.kipler.syntax.concept.Concept;

/**
 * 
 * @author İnanç Seylan
 * 
 */
public class ABox implements Copyable<ABox>, Iterable<Individual> {

    private IndividualNameManager nameMgr = new IndividualNameManager();

    private Map<String, Individual> individuals = new HashMap<String, Individual>();

    public Individual getIndividual(String name) {
        return individuals.get(name);
    }

    public Individual createIndividual() {
        String name = nameMgr.generateIndividualName();
        return createIndividual(name);
    }

    public Individual createIndividual(String name) {
        Individual indv = new Individual(name);
        individuals.put(name, indv);
        return indv;
    }

    public boolean contains(String individual) {
        return individuals.containsKey(individual);
    }

    public ABox copy() {
        ABox abox = new ABox();
        abox.nameMgr = nameMgr.copy();
        for (Individual i : individuals.values()) {
            Individual j = i.copy();
            abox.individuals.put(j.getName(), j);
        }
        return abox;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && (obj.getClass().equals(this.getClass()))) {
            ABox other = (ABox) obj;
            return individuals.equals(other.individuals);
        }
        return false;
    }

    public int size() {
        return individuals.size();
    }

    @Override
    public Iterator<Individual> iterator() {
        return individuals.values().iterator();
    }

    public void add(ABox abox) {
        for (Individual i : abox) {
            Individual j;
            if (contains(i.getName())) {
                j = getIndividual(i.getName());
            } else {
                j = createIndividual(i.getName());
            }
            for (Concept c : i.getConcepts()) {
                j.add(c);
            }
        }
    }
}
