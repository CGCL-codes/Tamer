package uk.ac.ebi.intact.confidence.psimi;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.xml.PsimiXmlReader;
import psidev.psi.mi.xml.PsimiXmlReaderException;
import psidev.psi.mi.xml.PsimiXmlWriter;
import psidev.psi.mi.xml.PsimiXmlWriterException;
import psidev.psi.mi.xml.model.Confidence;
import psidev.psi.mi.xml.model.*;
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
import uk.ac.ebi.intact.confidence.model.*;
import uk.ac.ebi.intact.confidence.util.AttributeGetter;
import uk.ac.ebi.intact.confidence.util.AttributeGetterException;
import uk.ac.ebi.intact.confidence.util.AttributeGetterImpl;
import uk.ac.ebi.intact.confidence.utils.ParserUtils;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Given the classifier, or the necessary data to train the model, this class assigns confidence values to
 * protein-protein interactions in a PSI-MI XML file.
 *
 * @author Irina Armean (iarmean@ebi.ac.uk)
 * @version $Id$
 * @since 1.6.0
 *        <pre> 03-Dec-2007 </pre>
 */
public class PsiMiXmlConfidence {

    /**
     * Sets up a logger for that class.
     */
    public static final Log log = LogFactory.getLog(PsiMiXmlConfidence.class);

    private File workDir;

    private OpenNLPMaxEntClassifier classifier;

    private BlastConfig blastConfig;

    private Set<UniprotAc> againstProteins;

    private DecimalFormat df = new DecimalFormat("0.00");

    private File goaFilterFile;

    private static GOAFilter goFilter;

    private AnnotationRetrieverStrategy annoDb;

    private Xref xref;

    /**
     * Constructor for the PSI-MI XML plugin to add scores to a PSI-MI XML file.
     *
     * @param gisModel : the classifying model persisted to file
     * @param config  : the configuration needed for intact-blast bridge
     * @param againstProteins : the list of proteins contained in the high confidence set
     * @param goaFile : the fail with the GOA annotations fo uniprot, for filtering the IEA tagged annotations
     * @param workDir : working directory for the plugin
     * @throws IOException
     */
    public PsiMiXmlConfidence(File gisModel, BlastConfig config, Set<UniprotAc> againstProteins, File goaFile, File workDir) throws PsiMiException {
        this.blastConfig = config;
        this.againstProteins = againstProteins;
        try {
            classifier = new OpenNLPMaxEntClassifier(gisModel);
        } catch (IOException e) {
            throw new PsiMiException(e);
        }
        this.workDir = workDir;
        this.goaFilterFile = goaFile;
        goFilter = new GOAFilterMapImpl();
        annoDb = new IntactAnnotationRetrieverImpl();
        initUnitXref();
    }

    public PsiMiXmlConfidence(String hcSetPath, String lcSetPath, File goaFile, File workDir, BlastConfig config) throws PsiMiException {
        this.workDir = workDir;
        this.blastConfig = config;
        try {
            this.classifier = new OpenNLPMaxEntClassifier(hcSetPath, lcSetPath, workDir);
        } catch (IOException e) {
            throw new PsiMiException(e);
        }
        this.againstProteins = ParserUtils.parseProteins(new File(hcSetPath));
        this.goaFilterFile = goaFile;
        initUnitXref();
    }

    public void appendConfidence(File inPsiMiXmlFile, File outPsiMiFile, Set<ConfidenceType> type) throws PsiMiException {
        PsimiXmlReader reader = new PsimiXmlReader();
        try {
            EntrySet entry = reader.read(inPsiMiXmlFile);
            saveScores(entry.getEntries(), type);
            writeScores(entry, outPsiMiFile);
        } catch (PsimiXmlReaderException e) {
            throw new PsiMiException(e);
        } catch (AttributeGetterException e) {
            throw new PsiMiException(e);
        }
    }

    public void writeScores(EntrySet entry, File outPsiMiFile) throws PsiMiException {
        PsimiXmlWriter writer = new PsimiXmlWriter();
        try {
            writer.write(entry, outPsiMiFile);
        } catch (PsimiXmlWriterException e) {
            System.out.println(entry);
            throw new PsiMiException(e);
        }
    }

    public static void setGoFilter(GOAFilter goFilter) {
        PsiMiXmlConfidence.goFilter = goFilter;
    }

    private void initUnitXref() throws PsiMiException {
        try {
            xref = Xref.class.newInstance();
            DbReference dbXref = new DbReference();
            xref.setPrimaryRef(dbXref);
        } catch (InstantiationException e) {
            throw new PsiMiException(e);
        } catch (IllegalAccessException e) {
            throw new PsiMiException(e);
        }
    }

    private void saveScores(Collection<Entry> entries, Set<ConfidenceType> type) throws PsiMiException, AttributeGetterException {
        AttributeGetter ag = new AttributeGetterImpl(this.workDir, annoDb, goFilter);
        for (Iterator<Entry> iterator = entries.iterator(); iterator.hasNext(); ) {
            Entry entry = iterator.next();
            Collection<Interaction> interactions = entry.getInteractions();
            for (Iterator<Interaction> interactionIterator = interactions.iterator(); interactionIterator.hasNext(); ) {
                Interaction interaction = interactionIterator.next();
                Collection<Participant> participants = interaction.getParticipants();
                if (participants.size() != 2) {
                    log.warn("Interaction: " + interaction.getId() + " has more than 2 participants => skipped!");
                } else {
                    BinaryInteraction binaryInteraction = retrieveBinaryInteraction(participants);
                    if (binaryInteraction != null) {
                        List<Attribute> attribs = getAttributes(ag, binaryInteraction, type);
                        if (log.isInfoEnabled()) {
                            log.info("interaction: " + binaryInteraction.toString() + " attribs: " + attribs);
                        }
                        save(interaction, classifier.evaluate(attribs));
                    } else {
                        save(interaction, classifier.evaluate(new ArrayList<Attribute>(0)));
                    }
                }
            }
        }
    }

    private BinaryInteraction retrieveBinaryInteraction(Collection<Participant> participants) {
        Iterator<Participant> iterPart = participants.iterator();
        Interactor intA = iterPart.next().getInteractor();
        Interactor intB = iterPart.next().getInteractor();
        Identifier uniprotA = retrieveUniprotId(intA);
        Identifier uniprotB = retrieveUniprotId(intB);
        BinaryInteraction bi = null;
        if (uniprotA != null && uniprotB != null) {
            bi = new BinaryInteraction(uniprotA, uniprotB);
        }
        return bi;
    }

    private Identifier retrieveUniprotId(Interactor interactor) {
        Xref xref = interactor.getXref();
        DbReference refA = xref.getPrimaryRef();
        if (refA.getDb().equalsIgnoreCase("uniprotkb") && refA.getRefType().equalsIgnoreCase("identity")) {
            return new UniprotIdentifierImpl(refA.getId());
        }
        return null;
    }

    private List<Attribute> getAttributes(AttributeGetter ag, BinaryInteraction binaryInteraction, Set<ConfidenceType> type) throws PsiMiException, AttributeGetterException {
        List<Attribute> attributes = new ArrayList<Attribute>();
        for (Iterator<ConfidenceType> confTypeIter = type.iterator(); confTypeIter.hasNext(); ) {
            ConfidenceType confidenceType = confTypeIter.next();
            if (confidenceType.equals(ConfidenceType.GO)) {
                try {
                    goFilter.initialize(goaFilterFile);
                } catch (FilterException e) {
                    throw new PsiMiException(e);
                }
            }
            attributes.addAll(getAttributes(ag, binaryInteraction, confidenceType));
        }
        return attributes;
    }

    private List<Attribute> getAttributes(AttributeGetter ag, BinaryInteraction binaryInteraction, ConfidenceType type) throws AttributeGetterException {
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

    private void save(Interaction interaction, double[] scores) throws PsiMiException {
        Unit u = null;
        try {
            u = Unit.class.newInstance();
            Names names = new Names();
            names.setFullName("interaction confidence score");
            names.setShortLabel("intact confidence");
            u.setNames(names);
            u.setXref(xref);
        } catch (InstantiationException e) {
            throw new PsiMiException(e);
        } catch (IllegalAccessException e) {
            throw new PsiMiException(e);
        }
        Confidence conf = new Confidence(u, df.format(scores[classifier.getIndex("high")]));
        Collection<Confidence> confs = interaction.getConfidences();
        confs.add(conf);
    }
}
