package org.proteored.miapeapi.cv.msi;

import org.proteored.miapeapi.cv.Accession;
import org.proteored.miapeapi.cv.ControlVocabularyManager;
import org.proteored.miapeapi.cv.ControlVocabularySet;
import org.proteored.miapeapi.cv.ControlVocabularyTerm;

public class Score extends ControlVocabularySet {

    private static Accession XTANDEM_EVALUE_ACC = new Accession("MS:1001330");

    private static Accession XTANDEM_HYPERSWCORE = new Accession("MS:1001331");

    private static Score instance;

    public static Score getInstance(ControlVocabularyManager cvManager) {
        if (instance == null) instance = new Score(cvManager);
        return instance;
    }

    private Score(ControlVocabularyManager cvManager) {
        super(cvManager);
        String[] parentAccessionsTMP = { "MS:1001143", "MS:1001092", "MS:1001198", "MS:1001153", "MS:1001147", "PRIDE:0000013", "PRIDE:0000047" };
        this.parentAccessions = parentAccessionsTMP;
        String[] explicitAccessionsTMP = { "PRIDE:0000052", "PRIDE:0000012", "PRIDE:0000138", "PRIDE:0000069", "PRIDE:0000185", "PRIDE:0000186", "PRIDE:0000099", "PRIDE:0000091", "PRIDE:0000050", "PRIDE:0000053", "PRIDE:0000284", "PRIDE:0000054", "PRIDE:0000177", "PRIDE:0000275", "PRIDE:0000275", "PRIDE:0000150", "PRIDE:0000100", "PRIDE:0000176", "PRIDE:0000062", "PRIDE:0000058", "PRIDE:0000149", "PRIDE:0000147", "PRIDE:0000148", "PRIDE:0000151", "PRIDE:0000150", "PRIDE:0000214", "PRIDE:0000215" };
        this.explicitAccessions = explicitAccessionsTMP;
        this.miapeSection = 308;
    }

    public static ControlVocabularyTerm getXTandemExpectValueTerm(ControlVocabularyManager cvManager) {
        return getInstance(cvManager).getCVTermByAccession(XTANDEM_EVALUE_ACC);
    }

    public static ControlVocabularyTerm getXTandemHyperScoreTerm(ControlVocabularyManager cvManager) {
        return getInstance(cvManager).getCVTermByAccession(XTANDEM_HYPERSWCORE);
    }
}
