package org.proteored.miapeapi.factories.msi;

import java.util.List;
import java.util.Set;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.InputData;
import org.proteored.miapeapi.interfaces.msi.PeptideModification;
import org.proteored.miapeapi.interfaces.msi.PeptideScore;

public class IdentifiedPeptideImpl implements IdentifiedPeptide {

    private final String sequence;

    private final Set<PeptideScore> scores;

    private final Set<PeptideModification> modifications;

    private final String spectrumRef;

    private final String charge;

    private final String massDesviation;

    private final InputData inputData;

    private final int rank;

    private final List<IdentifiedProtein> proteins;

    private final int id;

    public IdentifiedPeptideImpl(IdentifiedPeptideBuilder identifiedPeptideBuilder) {
        this.sequence = identifiedPeptideBuilder.sequence;
        this.scores = identifiedPeptideBuilder.scores;
        this.modifications = identifiedPeptideBuilder.modifications;
        this.spectrumRef = identifiedPeptideBuilder.spectrumRef;
        this.charge = identifiedPeptideBuilder.charge;
        this.massDesviation = identifiedPeptideBuilder.massDesviation;
        this.inputData = identifiedPeptideBuilder.inputData;
        this.rank = identifiedPeptideBuilder.rank;
        this.proteins = identifiedPeptideBuilder.proteins;
        this.id = identifiedPeptideBuilder.id;
    }

    @Override
    public String getCharge() {
        return charge;
    }

    @Override
    public String getMassDesviation() {
        return massDesviation;
    }

    @Override
    public Set<PeptideModification> getModifications() {
        return modifications;
    }

    @Override
    public Set<PeptideScore> getScores() {
        return scores;
    }

    @Override
    public String getSequence() {
        return sequence;
    }

    @Override
    public String getSpectrumRef() {
        return spectrumRef;
    }

    @Override
    public InputData getInputData() {
        return inputData;
    }

    @Override
    public int getRank() {
        return rank;
    }

    @Override
    public List<IdentifiedProtein> getIdentifiedProteins() {
        return proteins;
    }

    @Override
    public int getId() {
        return id;
    }
}
