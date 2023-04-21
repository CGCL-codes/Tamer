package org.neuroph.contrib.neat.gen.impl;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.neuroph.contrib.neat.gen.FitnessScores;
import org.neuroph.contrib.neat.gen.NeatParameters;
import org.neuroph.contrib.neat.gen.Organism;
import org.neuroph.contrib.neat.gen.Specie;
import org.neuroph.contrib.neat.gen.operations.Speciator;

public class SimpleSpeciator implements Speciator {

    private static Logger s_log = Logger.getLogger(SimpleSpeciator.class.getName());

    public static final double DEFAULT_DISJOINT_MULTIPLIER = 1;

    public static final double DEFAULT_EXCESS_MULTIPLIER = 1;

    public static final double DEFAULT_MATCHED_MULTIPLIER = 0.4;

    public static final double DEFAULT_COMPATABILITY_THRESHOLD = 0.26;

    private double disjointMultiplier = DEFAULT_DISJOINT_MULTIPLIER;

    private double excessMultiplier = DEFAULT_EXCESS_MULTIPLIER;

    private double matchedMultiplier = DEFAULT_MATCHED_MULTIPLIER;

    private double compatabilityThreshold = DEFAULT_COMPATABILITY_THRESHOLD;

    public void speciate(NeatParameters params, List<Specie> species, FitnessScores fitnessScores, List<Organism> chromosomes) {
        int newSpeciesCreated = 0;
        for (Organism o : chromosomes) {
            Specie parentSpecie = null;
            for (int i = 0; i < species.size() && parentSpecie == null; i++) {
                Specie s = species.get(i);
                if (o.equals(s.getRepresentativeOrganism())) {
                    parentSpecie = s;
                } else {
                    double score = compare(s.getRepresentativeOrganism(), o);
                    if (score < compatabilityThreshold) {
                        parentSpecie = s;
                    }
                }
            }
            if (parentSpecie == null) {
                parentSpecie = new Specie(params);
                species.add(parentSpecie);
                newSpeciesCreated++;
            }
            parentSpecie.addOrganism(o);
        }
    }

    public double compare(Organism one, Organism two) {
        double NumDisjoint = 0;
        double NumExcess = 0;
        double NumMatched = 0;
        double weightDifference = 0;
        int g1 = 0;
        int g2 = 0;
        while ((g1 < one.getConnections().size() - 1) || (g2 < two.getConnections().size() - 1)) {
            if (g1 == one.getConnections().size() - 1) {
                ++g2;
                ++NumExcess;
                continue;
            } else if (g2 == two.getConnections().size() - 1) {
                ++g1;
                ++NumExcess;
                continue;
            } else {
                long id1 = one.getConnections().get(g1).getInnovationId();
                long id2 = two.getConnections().get(g2).getInnovationId();
                if (id1 == id2) {
                    ++g1;
                    ++g2;
                    ++NumMatched;
                    weightDifference += Math.abs(one.getConnections().get(g1).getWeight() - two.getConnections().get(g2).getWeight());
                }
                if (id1 < id2) {
                    ++NumDisjoint;
                    ++g1;
                }
                if (id1 > id2) {
                    ++NumDisjoint;
                    ++g2;
                }
            }
        }
        int longest = (int) Math.max(one.getGenes().size(), two.getGenes().size());
        double excessScore = NumExcess / (double) longest;
        double disjointScore = NumDisjoint / (double) longest;
        double matchedScore = weightDifference / NumMatched;
        if (Double.isNaN(matchedScore)) {
            throw new IllegalStateException("Invalid Organism comparison: [excessScore = " + excessScore + " disjointScore = " + disjointScore + " matchedScore = " + matchedScore + "].");
        }
        return (excessMultiplier * excessScore) + (disjointMultiplier * disjointScore) + (matchedMultiplier * matchedScore);
    }

    public double getDisjointMultiplier() {
        return disjointMultiplier;
    }

    public double getExcessMultiplier() {
        return excessMultiplier;
    }

    public double getMatchedMultiplier() {
        return matchedMultiplier;
    }

    public double getCompatabilityThreshold() {
        return compatabilityThreshold;
    }

    public void setDisjointMultiplier(double disjointMultiplier) {
        this.disjointMultiplier = disjointMultiplier;
    }

    public void setExcessMultiplier(double excessMultiplier) {
        this.excessMultiplier = excessMultiplier;
    }

    public void setMatchedMultiplier(double matchedMultiplier) {
        this.matchedMultiplier = matchedMultiplier;
    }

    public void setCompatabilityThreshold(double compatabilityThreshold) {
        this.compatabilityThreshold = compatabilityThreshold;
    }
}
