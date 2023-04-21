package org.proteored.miapeapi.factories.msi;

import java.util.List;
import java.util.Set;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;

public class IdentifiedProteinImpl implements IdentifiedProtein {

    private final String accession;

    private final String description;

    private final Set<ProteinScore> score;

    private final String peptideNumber;

    private final String coverage;

    private final String matchedNumber;

    private final String unmatchedNumber;

    private final Boolean validationStatus;

    private final String additionalInformation;

    private final String validationType;

    private final String validationValue;

    private final List<IdentifiedPeptide> identifiedPeptides;

    private final int id;

    public IdentifiedProteinImpl(IdentifiedProteinBuilder identifiedProteinBuilder) {
        this.accession = identifiedProteinBuilder.accession;
        this.description = identifiedProteinBuilder.description;
        this.score = identifiedProteinBuilder.scores;
        this.peptideNumber = identifiedProteinBuilder.peptideNumber;
        this.coverage = identifiedProteinBuilder.coverage;
        this.matchedNumber = identifiedProteinBuilder.matchedNumber;
        this.unmatchedNumber = identifiedProteinBuilder.unmatchedNumber;
        this.additionalInformation = identifiedProteinBuilder.additionalInformation;
        this.validationStatus = identifiedProteinBuilder.validationStatus;
        this.validationType = identifiedProteinBuilder.validationType;
        this.validationValue = identifiedProteinBuilder.validationValue;
        this.identifiedPeptides = identifiedProteinBuilder.identifiedPeptides;
        this.id = identifiedProteinBuilder.id;
    }

    @Override
    public String getAccession() {
        return accession;
    }

    @Override
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    @Override
    public String getCoverage() {
        return coverage;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getPeptideNumber() {
        return peptideNumber;
    }

    @Override
    public Set<ProteinScore> getScores() {
        return score;
    }

    @Override
    public Boolean getValidationStatus() {
        return validationStatus;
    }

    @Override
    public String getPeaksMatchedNumber() {
        return matchedNumber;
    }

    @Override
    public String getUnmatchedSignals() {
        return unmatchedNumber;
    }

    @Override
    public String getValidationType() {
        return validationType;
    }

    @Override
    public String getValidationValue() {
        return validationValue;
    }

    @Override
    public List<IdentifiedPeptide> getIdentifiedPeptides() {
        return identifiedPeptides;
    }

    @Override
    public int getId() {
        return id;
    }
}
