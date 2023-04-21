package uk.ac.ebi.pride.tools.converter.dao.handler.impl;

import org.apache.log4j.Logger;
import uk.ac.ebi.pride.tools.converter.dao.handler.HandlerFactory;
import uk.ac.ebi.pride.tools.converter.report.model.Identification;
import uk.ac.ebi.pride.tools.converter.report.model.Sequence;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: rcote
 * Date: 11/02/11
 * Time: 11:29
 * <p/>
 * <p>This FASTA Handler implementation deals specifically with Mascot-indexed Uniprot FASTA files.
 * <p/>
 * <p>The IDLine of such files is in this form:
 * <pre>
 * sw|P61981|1433G_HUMAN RecName: Full=14-3-3 protein gamma;AltName: Full=Protein
 * </pre>
 * <p>and mascot uses 1433G_HUMAN as the identification accession. This is a problem because
 * the base FastaHandlerImpl assumes that the IDLine and the submitted accession are the same.
 * This implementation of the FastaHandler interface will be able to to deal with this situation
 * by mapping the Uniprot identifier (1433G_HUMAN) to the full IDLine and will also put the primary
 * uniprot accession (P61981) as the curated accession of the Identification object returned by calling
 * updateFastaSequenceInformation().
 */
public class FastaHandlerImpl extends AbstractFastaHandlerImpl {

    private static final Pattern FULL_UNIPROT_LINE_PATTERN = Pattern.compile("[a-zA-Z]*\\|([A-Z0-9]*)\\|([A-Z0-9_]*) .*");

    private static final Pattern FIRST_WORD_LINE_PATTERN = Pattern.compile("([\\w.|-]*)\\s?.*");

    private static final Logger logger = Logger.getLogger(FastaHandlerImpl.class);

    private Map<String, String> submittedAcToFastaIdLine = new LinkedHashMap<String, String>();

    private Map<String, String> submittedAcToDesiredAc = new LinkedHashMap<String, String>();

    private Map<String, String> fastaIdLineToDesiredAc = new LinkedHashMap<String, String>();

    private HandlerFactory.FASTA_FORMAT format;

    public FastaHandlerImpl(String fastaFilePath, HandlerFactory.FASTA_FORMAT format) {
        super(fastaFilePath);
        this.format = format;
        initIndexedOffsets();
    }

    private void initIndexedOffsets() {
        Map<String, Long> originalOffsets = getOffsets();
        for (String idLine : originalOffsets.keySet()) {
            switch(format) {
                case UNIPROT_MATCH_AC:
                    parseUniprotAcLine(idLine);
                    break;
                case UNIPROT_MATCH_ID:
                    parseUniprotIdLine(idLine);
                    break;
                case FIRST_WORD:
                    parseFirstWordLine(idLine);
                    break;
                case FULL:
                    submittedAcToFastaIdLine.put(idLine, idLine);
                    submittedAcToDesiredAc.put(idLine, idLine);
                    fastaIdLineToDesiredAc.put(idLine, idLine);
                    break;
            }
        }
    }

    private void parseFirstWordLine(String idLine) {
        Matcher matcher = FIRST_WORD_LINE_PATTERN.matcher(idLine);
        if (matcher.matches()) {
            String ac = matcher.group(1);
            submittedAcToFastaIdLine.put(ac, idLine);
            submittedAcToDesiredAc.put(ac, ac);
            fastaIdLineToDesiredAc.put(idLine, ac);
        } else {
            logger.warn("Invalid ID Line for indexed uniprot FASTA file: " + idLine);
        }
    }

    private void parseUniprotIdLine(String idLine) {
        Matcher matcher = FULL_UNIPROT_LINE_PATTERN.matcher(idLine);
        if (matcher.matches()) {
            String ac = matcher.group(1);
            String id = matcher.group(2);
            submittedAcToFastaIdLine.put(id, idLine);
            submittedAcToDesiredAc.put(id, ac);
            fastaIdLineToDesiredAc.put(idLine, ac);
        } else {
            logger.warn("Invalid ID Line for indexed uniprot FASTA file: " + idLine);
        }
    }

    private void parseUniprotAcLine(String idLine) {
        Matcher matcher = FULL_UNIPROT_LINE_PATTERN.matcher(idLine);
        if (matcher.matches()) {
            String ac = matcher.group(1);
            String id = matcher.group(2);
            submittedAcToFastaIdLine.put(ac, idLine);
            submittedAcToDesiredAc.put(ac, ac);
            fastaIdLineToDesiredAc.put(idLine, ac);
        } else {
            logger.warn("Invalid ID Line for indexed uniprot FASTA file: " + idLine);
        }
    }

    @Override
    public Identification updateFastaSequenceInformation(Identification identification) {
        String idLine = submittedAcToFastaIdLine.get(identification.getAccession());
        String ac = submittedAcToDesiredAc.get(identification.getAccession());
        Long seqId = getSequenceRefForAccession(idLine);
        if (seqId != null) {
            identification.setFastaSequenceReference(seqId);
            if (!ac.equals(identification.getAccession())) {
                identification.setCuratedAccession(ac);
            }
        } else {
            System.err.println("Could not find sequence matching accession: " + identification.getAccession());
            logger.error("Could not find sequence matching accession: " + identification.getAccession());
        }
        return identification;
    }

    @Override
    public Sequence next() {
        Sequence s = getNextSequence();
        s.setAccession(fastaIdLineToDesiredAc.get(s.getAccession()));
        return s;
    }
}
