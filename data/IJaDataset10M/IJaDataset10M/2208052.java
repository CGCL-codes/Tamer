package uk.ac.ebi.pride.data.core;

import java.util.List;

/**
 * Gel free identification.
 * <p/>
 * User: rwang
 * Date: 24-Mar-2010
 * Time: 16:38:43
 */
public class GelFreeIdentification extends Identification {

    /**
     * Constructor
     *
     * @param id                    required.
     * @param accession             required.
     * @param accessionVersion      optional.
     * @param peptides              required and non empty.
     * @param score                 optional.
     * @param searchDatabase        required.
     * @param searchDatabaseVersion optional.
     * @param searchEngine          required.
     * @param sequenceCoverage      optional ?
     * @param spectrum              optional ?
     * @param spliceIsoform         optional.
     * @param threshold             optional.
     * @param params                optional.
     */
    public GelFreeIdentification(Comparable id, String accession, String accessionVersion, List<Peptide> peptides, double score, String searchDatabase, String searchDatabaseVersion, String searchEngine, double sequenceCoverage, Spectrum spectrum, String spliceIsoform, double threshold, ParamGroup params) {
        super(id, accession, accessionVersion, peptides, score, searchDatabase, searchDatabaseVersion, searchEngine, sequenceCoverage, spectrum, spliceIsoform, threshold, params);
    }
}
