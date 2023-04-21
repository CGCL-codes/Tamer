package uk.ac.ebi.intact.confidence.psimi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.tab.PsimiTabReader;
import psidev.psi.mi.tab.PsimiTabWriter;
import psidev.psi.mi.tab.model.BinaryInteraction;
import psidev.psi.mi.tab.model.Confidence;
import psidev.psi.mi.tab.model.ConfidenceImpl;
import psidev.psi.mi.tab.model.CrossReference;
import psidev.psi.mi.xml.converter.ConverterException;
import uk.ac.ebi.intact.bridges.blast.BlastConfig;
import uk.ac.ebi.intact.bridges.blast.BlastServiceException;
import uk.ac.ebi.intact.bridges.blast.model.UniprotAc;
import uk.ac.ebi.intact.confidence.dataRetriever.AnnotationRetrieverStrategy;
import uk.ac.ebi.intact.confidence.dataRetriever.IntactAnnotationRetrieverImpl;
import uk.ac.ebi.intact.confidence.filter.FilterException;
import uk.ac.ebi.intact.confidence.filter.GOAFilter;
import uk.ac.ebi.intact.confidence.filter.GOAFilterMapImpl;
import uk.ac.ebi.intact.confidence.maxent.OpenNLPMaxEntClassifier;
import uk.ac.ebi.intact.confidence.model.Attribute;
import uk.ac.ebi.intact.confidence.model.ConfidenceType;
import uk.ac.ebi.intact.confidence.model.Identifier;
import uk.ac.ebi.intact.confidence.model.UniprotIdentifierImpl;
import uk.ac.ebi.intact.confidence.util.AttributeGetter;
import uk.ac.ebi.intact.confidence.util.AttributeGetterException;
import uk.ac.ebi.intact.confidence.util.AttributeGetterImpl;
import uk.ac.ebi.intact.confidence.utils.ParserUtils;
import uk.ac.ebi.intact.psimitab.IntActBinaryInteraction;
import uk.ac.ebi.intact.psimitab.IntActColumnHandler;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * writes to the psimi-tab file the score values
 *
 * @author Irina Armean (iarmean@ebi.ac.uk)
 * @since 1.6.0
 *        <pre>28 Aug 2007</pre>
 */
public class PsiMiTabConfidence {

    /**
     * Sets up a logger for that class.
     */
    public static final Log log = LogFactory.getLog(PsiMiTabConfidence.class);

    private File workDir;

    private OpenNLPMaxEntClassifier classifier;

    private BlastConfig blastConfig;

    private Set<UniprotAc> againstProteins;

    private File goaFilterFile;

    private GOAFilter goaFilter;

    private DecimalFormat df = new DecimalFormat("0.00");

    private PsimiTabWriter psimiTabWriter;

    private AnnotationRetrieverStrategy annoDb;

    public PsiMiTabConfidence(File gisModel, BlastConfig config, Set<UniprotAc> againstProteins, File goaFile, File workDir) throws IOException {
        if (!(gisModel.exists() && workDir.exists())) {
            throw new NullPointerException("GisModel File or workDir does not exist!");
        }
        if (config == null || againstProteins == null) {
            throw new NullPointerException("BlastConfig or againstProteins-Set is null, please make sure they are not null!");
        }
        this.blastConfig = config;
        this.againstProteins = againstProteins;
        classifier = new OpenNLPMaxEntClassifier(gisModel);
        this.workDir = workDir;
        this.goaFilterFile = goaFile;
        annoDb = new IntactAnnotationRetrieverImpl();
    }

    public PsiMiTabConfidence(String hcSetPath, String lcSetPath, File goaFile, File workDir, BlastConfig config) throws IOException {
        this.workDir = workDir;
        this.blastConfig = config;
        this.classifier = new OpenNLPMaxEntClassifier(hcSetPath, lcSetPath, workDir);
        this.againstProteins = fetchAgainstProteins(new File(hcSetPath));
        this.goaFilterFile = goaFile;
        annoDb = new IntactAnnotationRetrieverImpl();
    }

    public void appendConfidence(File inPsiMiFile, boolean hasHeaderLine, File outPsiMiFile, Set<ConfidenceType> type, boolean fromStratch) throws PsiMiException {
        if (outPsiMiFile.exists()) {
            if (!fromStratch) {
            }
        }
        PsimiTabReader reader = new PsimiTabReader(hasHeaderLine);
        reader.setBinaryInteractionClass(IntActBinaryInteraction.class);
        reader.setColumnHandler(new IntActColumnHandler());
        try {
            Iterator<BinaryInteraction> psiMiIterator = reader.iterate(inPsiMiFile);
            psimiTabWriter = new PsimiTabWriter();
            psimiTabWriter.setHeaderEnabled(hasHeaderLine);
            psimiTabWriter.setBinaryInteractionClass(IntActBinaryInteraction.class);
            psimiTabWriter.setColumnHandler(new IntActColumnHandler());
            saveScores(psiMiIterator, type, outPsiMiFile, true);
        } catch (IOException e) {
            throw new PsiMiException(e);
        } catch (ConverterException e) {
            throw new PsiMiException(e);
        } catch (FilterException e) {
            throw new PsiMiException(e);
        } catch (AttributeGetterException e) {
            throw new PsiMiException(e);
        }
    }

    public void writeScores(Collection<BinaryInteraction> interactions, boolean hasHeaderLine, File outPsiMiFile) throws PsiMiException {
        PsimiTabWriter writer = new PsimiTabWriter();
        writer.setHeaderEnabled(hasHeaderLine);
        writer.setBinaryInteractionClass(IntActBinaryInteraction.class);
        writer.setColumnHandler(new IntActColumnHandler());
        try {
            writer.write(interactions, outPsiMiFile);
        } catch (IOException e) {
            throw new PsiMiException(e);
        } catch (ConverterException e) {
            throw new PsiMiException(e);
        }
    }

    protected void saveScores(Iterator<BinaryInteraction> psiMiIterator, Set<ConfidenceType> type, File outFile, boolean firstLine) throws IOException, FilterException, AttributeGetterException {
        while (psiMiIterator.hasNext()) {
            BinaryInteraction interaction = psiMiIterator.next();
            if (interaction.getConfidenceValues().size() == 0) {
                if (interactionValid(interaction)) {
                    List<Attribute> attribs = getAttributes(interaction, type);
                    if (log.isDebugEnabled()) {
                        log.debug("interaction: " + interaction.getInteractorA() + ";" + interaction.getInteractorB() + " attribs: " + attribs);
                    }
                    save(interaction, classifier.evaluate(attribs));
                } else {
                    save(interaction, classifier.evaluate(new ArrayList<Attribute>(0)));
                }
            }
            psimiTabWriter.writeOrAppend(interaction, outFile, firstLine);
            if (firstLine) {
                firstLine = false;
            }
        }
    }

    protected List<Attribute> getAttributes(BinaryInteraction interaction, Set<ConfidenceType> type) throws IOException, FilterException, AttributeGetterException {
        List<Attribute> attributes = new ArrayList<Attribute>();
        for (Iterator<ConfidenceType> confTypeIter = type.iterator(); confTypeIter.hasNext(); ) {
            ConfidenceType confidenceType = confTypeIter.next();
            if (confidenceType.equals(ConfidenceType.GO)) {
                if (goaFilter == null) {
                    goaFilter = new GOAFilterMapImpl();
                    goaFilter.initialize(goaFilterFile);
                }
            }
            List<Attribute> attribs = getAttributes(interaction, confidenceType);
            if (attribs != null) {
                attributes.addAll(attribs);
            }
        }
        return attributes;
    }

    private Set<UniprotAc> fetchAgainstProteins(File hcSet) throws IOException {
        return ParserUtils.parseProteins(hcSet);
    }

    private boolean interactionValid(BinaryInteraction interaction) {
        String[] acs = getUniprotAcs(interaction);
        return (acs.length == 2 ? true : false);
    }

    private List<Attribute> getAttributes(BinaryInteraction interaction, ConfidenceType type) throws AttributeGetterException {
        uk.ac.ebi.intact.confidence.model.BinaryInteraction binaryInteraction = convertBinary(interaction);
        String[] acs = getUniprotAcs(interaction);
        AttributeGetter ag = new AttributeGetterImpl(this.workDir, annoDb, goaFilter);
        switch(type) {
            case GO:
                return ag.fetchGoAttributes(binaryInteraction);
            case InterPRO:
                return ag.fetchIpAttributes(binaryInteraction);
            case Alignment:
                try {
                    return ag.fetchAlignAttributes(binaryInteraction, this.againstProteins, this.blastConfig);
                } catch (BlastServiceException e) {
                    throw new AttributeGetterException(e);
                }
            case ALL:
                return ag.fetchAllAttributes(binaryInteraction, this.againstProteins, this.blastConfig);
            default:
                return ag.fetchAllAttributes(binaryInteraction, this.againstProteins, this.blastConfig);
        }
    }

    private uk.ac.ebi.intact.confidence.model.BinaryInteraction convertBinary(BinaryInteraction interaction) {
        String[] acs = getUniprotAcs(interaction);
        Identifier idA = new UniprotIdentifierImpl(acs[0]);
        Identifier idB = new UniprotIdentifierImpl(acs[1]);
        return new uk.ac.ebi.intact.confidence.model.BinaryInteraction(idA, idB);
    }

    private String[] getUniprotAcs(BinaryInteraction interaction) {
        String uniprotA = "";
        Collection<CrossReference> xrefsA = interaction.getInteractorA().getIdentifiers();
        for (Iterator<CrossReference> crossReferenceIterator = xrefsA.iterator(); crossReferenceIterator.hasNext(); ) {
            CrossReference crossReference = crossReferenceIterator.next();
            if (crossReference.getDatabase().equalsIgnoreCase("uniprotkb")) {
                uniprotA = crossReference.getIdentifier();
            }
        }
        String uniprotB = "";
        Collection<CrossReference> xrefsB = interaction.getInteractorB().getIdentifiers();
        for (Iterator<CrossReference> crossReferenceIterator = xrefsB.iterator(); crossReferenceIterator.hasNext(); ) {
            CrossReference crossReference = crossReferenceIterator.next();
            if (crossReference.getDatabase().equalsIgnoreCase("uniprotkb")) {
                uniprotB = crossReference.getIdentifier();
            }
        }
        if (uniprotA.equalsIgnoreCase("") || uniprotB.equalsIgnoreCase("")) {
            return new String[0];
        } else {
            String[] result = { uniprotA, uniprotB };
            return result;
        }
    }

    private void save(BinaryInteraction interaction, double[] scores) {
        List<Confidence> confVals = new ArrayList<Confidence>(1);
        Confidence conf1 = new ConfidenceImpl("intact confidence", df.format(scores[classifier.getIndex("high")]));
        confVals.add(conf1);
        interaction.setConfidenceValues(confVals);
    }
}
