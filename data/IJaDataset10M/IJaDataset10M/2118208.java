package org.openscience.nmrshiftdb.util;

public class NmrshiftdbConstants {

    public static String EDITED = "edited";

    public static String TRUE = "true";

    public static String FALSE = "false";

    public static String REJECTED = "rejected";

    public static String CHANGE = "change";

    public static String CALCULATEDONLY = "calculated spectra only";

    public static String MEASUREDONLY = "measured spectra only";

    public static String EXACT = "exact";

    public static String REGEXP = "regular expression";

    public static String FUZZY = "fuzzy";

    public static String FRAGMENT = "fragment";

    public static String SUBSPECTRUM = "subspectrum search";

    public static String TOTALSPECTRUM = "total similarity spectrum search";

    public static String SUBSTRUCTURE_SIMILARITY = "similarity search";

    public static String SUBSTRUCTURE_EXACT = "substructure search";

    public static String TOTALSTRUCTURE = "identity search";

    public static String MOLECULEKEYWORDS_TOTAL = "search by molecule keyword";

    public static String SPECTRUMKEYWORDS_TOTAL = "search by spectrum keyword";

    public static String MOLECULEKEYWORDS_FRAGMENT = "search by molecule keyword fragements";

    public static String SPECTRUMKEYWORDS_FRAGMENT = "search by spectrum keyword fragements";

    public static String LITERATURE_TITLE = "literature/title";

    public static String LITERATURE_AUTHOR = "literature/author";

    public static String COMMENT = "comment";

    public static String CANNAME = "canonical name";

    public static String SPECLINK = "description of spectrum link";

    public static String MOLLINK = "description of molecule link";

    public static String MOLKEY = "molecule keyword";

    public static String SPECKEY = "spectrum keyword";

    public static String CASNUMBER = "CAS number";

    public static String CHEMNAME = "chemical name";

    public static String FORMULA = "chemical formula";

    public static String FORMULA_WITH_OTHER = "chemical formula (with other elements)";

    public static String MULTIPLICITY = "multiplicities";

    public static String POTMULTIPLICITY = "potential multiplicities";

    public static String MYSPECTRA = "my spectra";

    public static String BROWSE = "browse all structures";

    public static String WEIGHT = "molecular weight search";

    public static String CONDITIONS = "condition search";

    public static String MOLECULE_NR = "molecule id";

    public static String SPECTRUM_NR = "spectrum id";

    public static String HOSECODE = "HOSE code";

    public static String DBE = "double bond equivalents";

    public static String SSSR = "number of rings in smallest set of smallest rings";

    public static String DBE_RINGS = "double bond equivalents/smallest set of smallest rings";

    public static String DELETE_SEARCH_HISTORY = "Clear history";

    public static String UPLOAD_JCAMP_SEARCH = "uploadjcampsearch";

    public static String STRUCTURESHISTORY = "structureshistory";

    public static String SEARCHHISTORY = "searchhistory";

    public static String INPUT = "input";

    public static String MOLFILE = "molfile";

    public static String MOLFILEH = "molfileh";

    public static String JOURNAL_ARTICLE = "journal_article";

    public static String MONOGRAPH = "monograph";

    public static String OTHER_ARTICLE = "other_article";

    public static String UNREPORTED = "Unreported";

    public static String UNKNOWN = "Unknown";

    public static String METADATA = "metadata";

    public static String SUBSTANCE = "substance";

    public static String CONDITION = "condition";

    public static String frequency = "jcamp:.OBSERVE FREQUENCY";

    public static String solvent = "jcamp:.SOLVENTNAME";

    public static String temperature = "jcamp:TEMPERATURE";

    public static String assignment = "nmr:assignmentMethod";

    public static String nucleus = "nmr:OBSERVENUCLEUS";

    public static String nmrid = "nmr:nmrshiftdbid";

    public String getSUBSPECTRUM() {
        return (SUBSPECTRUM);
    }

    public String getFUZZY() {
        return (FUZZY);
    }

    public String getEXACT() {
        return (EXACT);
    }

    public String getREGEXP() {
        return (REGEXP);
    }

    public String getFRAGMENT() {
        return (FRAGMENT);
    }

    public String getLITERATURE_TITLE() {
        return (LITERATURE_TITLE);
    }

    public String getLITERATURE_AUTHOR() {
        return (LITERATURE_AUTHOR);
    }

    public String getCOMMENT() {
        return (COMMENT);
    }

    public String getCANNAME() {
        return (CANNAME);
    }

    public String getSPECLINK() {
        return (SPECLINK);
    }

    public String getMOLLINK() {
        return (MOLLINK);
    }

    public String getMOLKEY() {
        return (MOLKEY);
    }

    public String getSPECKEY() {
        return (SPECKEY);
    }

    public String getCASNUMBER() {
        return (CASNUMBER);
    }

    public String getCHEMNAME() {
        return (CHEMNAME);
    }

    public String getFORMULA() {
        return (FORMULA);
    }

    public String getFORMULA_WITH_OTHER() {
        return (FORMULA_WITH_OTHER);
    }

    public String getDELETE_SEARCH_HISTORY() {
        return (DELETE_SEARCH_HISTORY);
    }

    public String getMULTIPLICITY() {
        return (MULTIPLICITY);
    }

    public String getPOTMULTIPLICITY() {
        return (POTMULTIPLICITY);
    }

    public String getMYSPECTRA() {
        return (MYSPECTRA);
    }

    public String getSUBSTRUCTURE_SIMILARITY() {
        return (SUBSTRUCTURE_SIMILARITY);
    }

    public String getSUBSTRUCTURE_EXACT() {
        return (SUBSTRUCTURE_EXACT);
    }

    public String getTOTALSTRUCTURE() {
        return (TOTALSTRUCTURE);
    }

    public String getBROWSE() {
        return (BROWSE);
    }

    public String getSPECTRUM_NR() {
        return (SPECTRUM_NR);
    }

    public String getMOLECULE_NR() {
        return (MOLECULE_NR);
    }

    public String getHOSECODE() {
        return (HOSECODE);
    }

    public String getDBE_RINGS() {
        return DBE_RINGS;
    }

    public String getWEIGHT() {
        return WEIGHT;
    }
}
