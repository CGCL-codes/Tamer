package de.unikoeln.genetik.popgen.jfms.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Population {

    private final ArrayList<SubPopulation> subPopulations = new ArrayList<SubPopulation>();

    public void changeGeneration(int t) {
        for (SubPopulation subPopulation : subPopulations) {
            subPopulation.changeGeneration(t);
        }
    }

    public Individual[] createSample() {
        Individual[] sample = new Individual[getSampleSize()];
        int begin = 0;
        for (SubPopulation subPopulation : subPopulations) {
            System.arraycopy(subPopulation.getSample(), 0, sample, begin, subPopulation.getSampleSize());
            begin += subPopulation.getSampleSize();
        }
        return sample;
    }

    public void evaluateFitnesses() {
        for (SubPopulation subPopulation : subPopulations) {
            subPopulation.evaluateFitnesses();
        }
    }

    public Map<Genome, Set<Individual>> getAlleleMap() {
        Map<Genome, Set<Individual>> map = new TreeMap<Genome, Set<Individual>>();
        for (SubPopulation subPopulation : subPopulations) {
            for (Individual individual : subPopulation) {
                Set<Individual> individuals = map.get(individual.getGenome());
                if (individuals == null) {
                    individuals = new HashSet<Individual>();
                    map.put(individual.getGenome(), individuals);
                }
                individuals.add(individual);
            }
        }
        return map;
    }

    public int getDiploidSize() {
        int size = 0;
        for (SubPopulation subPopulation : subPopulations) {
            size += subPopulation.getDiploidSize();
        }
        return size;
    }

    public Set<Number> getFixedMutations() {
        Set<Number> fixedMutations = new HashSet<Number>();
        fixedMutations.addAll(subPopulations.get(0).getFixedMutations());
        for (int i = 1; i < subPopulations.size(); i++) {
            fixedMutations.retainAll(subPopulations.get(i).getFixedMutations());
        }
        return fixedMutations;
    }

    public Individual getIndividual(int k) {
        int i = 0;
        while (subPopulations.get(i).size() < k) {
            k -= subPopulations.get(i).size();
            i++;
        }
        return subPopulations.get(i).get(k);
    }

    public int getSampleSize() {
        int sampleSize = 0;
        for (SubPopulation subPopulation : subPopulations) {
            sampleSize += subPopulation.getSampleSize();
        }
        return sampleSize;
    }

    public ArrayList<SubPopulation> getSubPopulations() {
        return subPopulations;
    }

    public void migrate(double m) {
        for (int i = 0; i < subPopulations.size(); i++) {
            int migrants = (int) (m * subPopulations.get(i).size());
            Individual[] replacedLocals = subPopulations.get(i).getSample(migrants);
            for (int j = 0; j < subPopulations.size(); j++) {
                if (j != i) {
                    Individual[] emmigrants = subPopulations.get(j).getSample(migrants);
                    for (int k = 0; k < migrants; k++) {
                        replacedLocals[k].setGenome(emmigrants[k].getGenome());
                    }
                }
            }
        }
    }

    public void mutate() {
        for (SubPopulation subPopulation : subPopulations) {
            subPopulation.mutate();
        }
    }

    public void recombine(double r) {
        for (SubPopulation subPopulation : subPopulations) {
            subPopulation.recombine(r);
        }
    }

    public void removeMutations(Set<Number> mutations) {
        for (SubPopulation subPopulation : subPopulations) {
            subPopulation.removeMutations(mutations);
        }
    }
}
