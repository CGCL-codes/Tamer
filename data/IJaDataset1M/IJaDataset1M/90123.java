package org.proteored.miapeapi.xml.xtandem.msi;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;
import org.proteored.miapeapi.cv.msi.Score;
import org.proteored.miapeapi.interfaces.msi.IdentifiedPeptide;
import org.proteored.miapeapi.interfaces.msi.IdentifiedProtein;
import org.proteored.miapeapi.interfaces.msi.ProteinScore;
import org.proteored.miapeapi.xml.util.MiapeXmlUtil;
import de.proteinms.xtandemparser.xtandem.Domain;
import de.proteinms.xtandemparser.xtandem.ModificationMap;
import de.proteinms.xtandemparser.xtandem.Peptide;
import de.proteinms.xtandemparser.xtandem.Protein;

public class IdentifiedProteinImpl implements IdentifiedProtein {

    private final Protein xmlProtein;

    private final Set<ProteinScore> proteinScores = new HashSet<ProteinScore>();

    private final Integer identifier;

    private final List<Peptide> peptideList;

    private final ModificationMap modificationsMap;

    private final ControlVocabularyManager cvManager;

    public IdentifiedProteinImpl(Protein xTandemProtein, List<Peptide> list, Integer identifier, ModificationMap modificationsMap, ControlVocabularyManager cvManager) {
        this.xmlProtein = xTandemProtein;
        this.identifier = identifier;
        this.peptideList = list;
        this.modificationsMap = modificationsMap;
        this.cvManager = cvManager;
        processScores();
    }

    private void processScores() {
        if (xmlProtein != null) {
            final ControlVocabularyTerm xTandemExpectValueTerm = Score.getXTandemExpectValueTerm(cvManager);
            String name = "XTandem e-value";
            if (xTandemExpectValueTerm != null) {
                name = xTandemExpectValueTerm.getPreferredName();
            }
            proteinScores.add(new ProteinScoreImpl(name, xmlProtein.getExpectValue()));
            name = "XTandem summed score";
            proteinScores.add(new ProteinScoreImpl(name, xmlProtein.getSummedScore()));
        }
    }

    @Override
    public int getId() {
        if (identifier != null) return identifier;
        return -1;
    }

    @Override
    public String getAccession() {
        if (xmlProtein != null) return xmlProtein.getLabel();
        return null;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public Set<ProteinScore> getScores() {
        if (!this.proteinScores.isEmpty()) return this.proteinScores;
        return null;
    }

    @Override
    public String getPeptideNumber() {
        return null;
    }

    @Override
    public String getCoverage() {
        return null;
    }

    @Override
    public String getPeaksMatchedNumber() {
        return null;
    }

    @Override
    public String getUnmatchedSignals() {
        return null;
    }

    @Override
    public String getAdditionalInformation() {
        return null;
    }

    @Override
    public Boolean getValidationStatus() {
        return null;
    }

    @Override
    public String getValidationType() {
        return null;
    }

    @Override
    public String getValidationValue() {
        return null;
    }

    @Override
    public List<IdentifiedPeptide> getIdentifiedPeptides() {
        if (peptideList != null && !peptideList.isEmpty()) {
            List<IdentifiedPeptide> peptides = new ArrayList<IdentifiedPeptide>();
            for (Peptide peptide : peptideList) {
                for (Domain domain : peptide.getDomains()) {
                    peptides.add(new IdentifiedPeptideImpl(domain, modificationsMap, peptide.getSequence().trim(), this, MiapeXmlUtil.PeptideCounter.increaseCounter(), cvManager));
                }
            }
            return peptides;
        }
        return null;
    }
}
