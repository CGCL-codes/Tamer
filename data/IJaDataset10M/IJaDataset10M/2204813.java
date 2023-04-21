package uk.ac.ebi.intact.psimitab;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import psidev.psi.mi.tab.PsimitabHeader;
import psidev.psi.mi.tab.converter.tab2xml.XmlConvertionException;
import psidev.psi.mi.tab.converter.txt2tab.MitabLineException;
import psidev.psi.mi.tab.converter.txt2tab.MitabLineParserUtils;
import psidev.psi.mi.tab.converter.xml2tab.ColumnHandler;
import psidev.psi.mi.tab.converter.xml2tab.CrossReferenceConverter;
import psidev.psi.mi.tab.converter.xml2tab.IsExpansionStrategyAware;
import psidev.psi.mi.tab.expansion.ExpansionStrategy;
import psidev.psi.mi.tab.formatter.LineFormatter;
import psidev.psi.mi.tab.formatter.TabulatedLineFormatter;
import psidev.psi.mi.tab.model.*;
import psidev.psi.mi.tab.model.column.Column;
import psidev.psi.mi.xml.model.*;
import psidev.psi.mi.xml.model.InteractionDetectionMethod;
import psidev.psi.mi.xml.model.Interactor;
import psidev.psi.mi.xml.model.Organism;
import uk.ac.ebi.intact.psimitab.exception.NameNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Handles intact specific columns in the MITAB data import/export.
 *
 * @author Nadin Neuhauser (nneuhaus@ebi.ac.uk)
 * @version $Id$
 * @since 2.0.0
 */
public class IntactColumnHandler implements ColumnHandler<IntactBinaryInteraction, PsimitabHeader>, IsExpansionStrategyAware<IntactBinaryInteraction> {

    public static final Log logger = LogFactory.getLog(IntactColumnHandler.class);

    private static final Pattern EXPERIMENT_LABEL_PATTERN = Pattern.compile("[a-z-_]+-\\d{4}[a-z]?-\\d+");

    private static final String IDENTITY_MI_REF = "MI:0356";

    private Boolean goTermNameAutoCompletion = Boolean.FALSE;

    private Boolean interproNameAutoCompletion = Boolean.FALSE;

    /**
     * Query is used for GOTerm Name AutoCompletion
     */
    private GoTermHandler goHandler;

    /**
     * Query is used for Interpro Name AutoCompletion
     */
    private InterproNameHandler interproHandler;

    /**
     * CrossReference Converter
     */
    private final CrossReferenceConverter xConverter = new CrossReferenceConverter();

    public IntactColumnHandler() {
    }

    public IntactColumnHandler(boolean goTerm_Name_AutoCompletion, boolean interpro_Name_AutoCompletion) {
        this.goTermNameAutoCompletion = goTerm_Name_AutoCompletion;
        this.interproNameAutoCompletion = interpro_Name_AutoCompletion;
    }

    public void setExpansionMethod(String method) {
        throw new UnsupportedOperationException("Expansion method is not supported");
    }

    public void setGoTermNameAutoCompletion(boolean autocompletion) {
        this.goTermNameAutoCompletion = autocompletion;
    }

    public void setInterproNameAutoCompletion(boolean autocompletion) {
        this.interproNameAutoCompletion = autocompletion;
    }

    /**
     * Process additional Column information from PSI-MI XML 2.5 to IntactBinaryInteraction.
     *
     * @param bi
     * @param interaction
     */
    public void process(IntactBinaryInteraction bi, Interaction interaction) {
        if (interaction.getParticipants().size() != 2) {
            if (logger.isDebugEnabled()) logger.debug("interaction (id:" + interaction.getId() + ") could not be converted to MITAB25 as it does not have exactly 2 participants.");
        }
        Iterator<Participant> pi = interaction.getParticipants().iterator();
        Participant pA = pi.next();
        Participant pB = pi.next();
        if (pA.getExperimentalRoles() != null && pA.getExperimentalRoles().size() != 1) {
            if (logger.isDebugEnabled()) logger.debug("interaction (id:" + interaction.getId() + ") could not be converted to MITAB25 as it does not have exactly 1 experimentalRole.");
        } else {
            CrossReference experimentalRoleA = extractExperimentalRole(pA);
            if (bi.hasExperimentalRolesInteractorA()) {
                bi.getExperimentalRolesInteractorA().add(experimentalRoleA);
            } else {
                List<CrossReference> xrefs = new ArrayList<CrossReference>();
                xrefs.add(experimentalRoleA);
                bi.setExperimentalRolesInteractorA(xrefs);
            }
        }
        if (pB.getExperimentalRoles() != null && pB.getExperimentalRoles().size() != 1) {
            if (logger.isDebugEnabled()) logger.debug("interaction (id:" + interaction.getId() + ") could not be converted to MITAB25 as it does not have exactly 1 experimentalRole.");
        } else {
            CrossReference experimentalRoleB = extractExperimentalRole(pB);
            if (bi.hasExperimentalRolesInteractorB()) {
                bi.getExperimentalRolesInteractorB().add(experimentalRoleB);
            } else {
                List<CrossReference> xrefs = new ArrayList<CrossReference>();
                xrefs.add(experimentalRoleB);
                bi.setExperimentalRolesInteractorB(xrefs);
            }
        }
        if (pA.getBiologicalRole() == null) {
            if (logger.isDebugEnabled()) logger.debug("interaction (id:" + interaction.getId() + ") could not be converted to MITAB25 as it does not have a biologicalRole.");
        } else {
            CrossReference biologicalRoleA = extractBiologicalRole(pA);
            if (bi.hasBiologicalRolesInteractorA()) {
                bi.getBiologicalRolesInteractorA().add(biologicalRoleA);
            } else {
                List<CrossReference> xrefs = new ArrayList<CrossReference>();
                xrefs.add(biologicalRoleA);
                bi.setBiologicalRolesInteractorA(xrefs);
            }
        }
        if (pB.getBiologicalRole() == null) {
            if (logger.isDebugEnabled()) logger.debug("interaction (id:" + interaction.getId() + ") could not be converted to MITAB25 as it does not have a biologicalRole.");
        } else {
            CrossReference biologicalRoleB = extractBiologicalRole(pB);
            if (bi.hasBiologicalRolesInteractorB()) {
                bi.getBiologicalRolesInteractorB().add(biologicalRoleB);
            } else {
                List<CrossReference> xrefs = new ArrayList<CrossReference>();
                xrefs.add(biologicalRoleB);
                bi.setBiologicalRolesInteractorB(xrefs);
            }
        }
        if (pA.getInteractor().getInteractorType() == null) {
            if (logger.isDebugEnabled()) logger.debug("interaction (id:" + interaction.getId() + ") could not be converted to MITAB25 as it does not have exactly 1 interactorType.");
        } else {
            CrossReference typeA = extractInteractorType(pA);
            if (bi.hasInteractorTypeA()) {
                bi.getInteractorTypeA().add(typeA);
            } else {
                List<CrossReference> xrefs = new ArrayList<CrossReference>();
                xrefs.add(typeA);
                bi.setInteractorTypeA(xrefs);
            }
        }
        if (pB.getInteractor().getInteractorType() == null) {
            if (logger.isDebugEnabled()) logger.debug("interaction (id:" + interaction.getId() + ") could not be converted to MITAB25 as it does not have exactly 1 interactorType.");
        } else {
            CrossReference typeB = extractInteractorType(pB);
            if (bi.hasInteractorTypeB()) {
                bi.getInteractorTypeB().add(typeB);
            } else {
                List<CrossReference> xrefs = new ArrayList<CrossReference>();
                xrefs.add(typeB);
                bi.setInteractorTypeB(xrefs);
            }
        }
        if (pA.getInteractor().getXref().getSecondaryRef() != null && !pA.getInteractor().getXref().getSecondaryRef().isEmpty()) {
            List<CrossReference> propertiesA = extractProperties(pA);
            if (!bi.hasPropertiesA()) bi.setPropertiesA(new ArrayList<CrossReference>());
            bi.getPropertiesA().addAll(propertiesA);
        }
        if (pB.getInteractor().getXref().getSecondaryRef() != null && !pB.getInteractor().getXref().getSecondaryRef().isEmpty()) {
            List<CrossReference> propertiesB = extractProperties(pB);
            if (!bi.hasPropertiesB()) bi.setPropertiesB(new ArrayList<CrossReference>());
            bi.getPropertiesB().addAll(propertiesB);
        }
        if (interaction.getExperiments() != null && !interaction.getExperiments().isEmpty()) {
            for (ExperimentDescription description : interaction.getExperiments()) {
                if (description.hasHostOrganisms()) {
                    Organism hostOrganism = description.getHostOrganisms().iterator().next();
                    String id = Integer.toString(hostOrganism.getNcbiTaxId());
                    String db = hostOrganism.getNames().getShortLabel();
                    CrossReference organismRef = new CrossReferenceImpl(db, id);
                    if (bi.hasHostOrganism()) {
                        bi.getHostOrganism().add(organismRef);
                    } else {
                        List<CrossReference> hos = new ArrayList<CrossReference>();
                        hos.add(organismRef);
                        bi.setHostOrganism(hos);
                    }
                }
            }
        }
        for (ExperimentDescription experiment : interaction.getExperiments()) {
            for (Attribute attribute : experiment.getAttributes()) {
                if (attribute.getName().equals("dataset")) {
                    String dataset = attribute.getValue();
                    if (bi.hasDatasetName()) {
                        bi.getDataset().add(dataset);
                    } else {
                        List<String> datasets = new ArrayList<String>();
                        datasets.add(dataset);
                        bi.setDataset(datasets);
                    }
                }
            }
        }
        List<Author> authors = new ArrayList<Author>();
        for (ExperimentDescription experiment : interaction.getExperiments()) {
            final String label = experiment.getNames().getShortLabel();
            if (isWellFormattedExperimentShortlabel(label)) {
                final StringBuilder sb = new StringBuilder();
                final String[] values = label.split("-");
                sb.append(StringUtils.capitalize(values[0]));
                sb.append(" et al. ");
                sb.append('(');
                sb.append(values[1]);
                sb.append(')');
                authors.add(new AuthorImpl(sb.toString()));
                bi.setAuthors(authors);
            } else {
                InteractionDetectionMethod method = experiment.getInteractionDetectionMethod();
                if (method != null && method.getNames() != null) {
                    String shortLabel = method.getNames().getShortLabel();
                    if (shortLabel != null && shortLabel.equals("inferred by curator")) {
                        String experimentFullName;
                        if (experiment.getNames() != null && (experimentFullName = experiment.getNames().getFullName()) != null) {
                            authors.add(new AuthorImpl(experimentFullName));
                            bi.setAuthors(authors);
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if the ExperimentLabel is how expected.
     *
     * @param label
     * @return
     */
    private boolean isWellFormattedExperimentShortlabel(String label) {
        if (label == null) {
            return false;
        }
        return EXPERIMENT_LABEL_PATTERN.matcher(label).matches();
    }

    /**
     * Extracts the relevant information for the psimitab experimental role.
     *
     * @param participant
     * @return experimental role
     */
    private CrossReference extractExperimentalRole(Participant participant) {
        ExperimentalRole role = participant.getExperimentalRoles().iterator().next();
        String id = role.getXref().getPrimaryRef().getId().split(":")[1];
        String db = role.getXref().getPrimaryRef().getId().split(":")[0];
        String text = role.getNames().getShortLabel();
        return new CrossReferenceImpl(db, id, text);
    }

    /**
     * Extracts the relevant information for the psimitab biological role.
     *
     * @param participant
     * @return experimental role
     */
    private CrossReference extractBiologicalRole(Participant participant) {
        BiologicalRole role = participant.getBiologicalRole();
        String id = role.getXref().getPrimaryRef().getId().split(":")[1];
        String db = role.getXref().getPrimaryRef().getId().split(":")[0];
        String text = role.getNames().getShortLabel();
        return new CrossReferenceImpl(db, id, text);
    }

    /**
     * Extracts the relevant informations for the psimitab interactor type.
     *
     * @param participant
     * @return interactor type
     */
    private CrossReference extractInteractorType(Participant participant) {
        String id = participant.getInteractor().getInteractorType().getXref().getPrimaryRef().getId().split(":")[1];
        String db = participant.getInteractor().getInteractorType().getXref().getPrimaryRef().getId().split(":")[0];
        String text = participant.getInteractor().getInteractorType().getNames().getShortLabel();
        return new CrossReferenceImpl(db, id, text);
    }

    /**
     * Extracts the relevant informations for the psimitab propterties.
     *
     * @param participant
     * @return list of properties
     */
    private List<CrossReference> extractProperties(Participant participant) {
        List<CrossReference> properties = new ArrayList<CrossReference>();
        for (DbReference dbref : participant.getInteractor().getXref().getSecondaryRef()) {
            String id, db, text = null;
            id = dbref.getId();
            db = dbref.getDb();
            if (goTermNameAutoCompletion && dbref.getDb().equalsIgnoreCase("GO")) {
                text = fetchGoNameFromWebservice(id);
            }
            if (interproNameAutoCompletion && dbref.getDb().equalsIgnoreCase("Interpro")) {
                text = fetchInterproNameFromInterproNameHandler(id);
            }
            if (dbref.getRefTypeAc() == null) {
                properties.add(new CrossReferenceImpl(db, id, text));
            } else {
                if (!dbref.getRefTypeAc().equals(IDENTITY_MI_REF)) {
                    properties.add(new CrossReferenceImpl(db, id, text));
                }
            }
        }
        return properties;
    }

    /**
     * Fetch the GoTerm of a specific GO identifier from Intact OLS-Webservice.
     *
     * @param id GO identifier
     * @return GOTerm
     */
    private String fetchGoNameFromWebservice(String id) {
        try {
            if (goHandler == null) {
                goHandler = new GoTermHandler();
            }
            return goHandler.getNameById(id);
        } catch (NameNotFoundException e) {
            logger.info("No Description for " + id + " found.");
            return null;
        }
    }

    /**
     * Fetch the Interpro name of a specific Interpro Id from a interpro-entryFile
     *
     * @param id Interpro identifier
     * @return Interpro name
     */
    private String fetchInterproNameFromInterproNameHandler(String id) {
        try {
            if (interproHandler == null) {
                InputStream stream = null;
                try {
                    URL url = new URL("ftp://ftp.ebi.ac.uk/pub/databases/interpro/entry.list");
                    stream = url.openStream();
                    interproHandler = new InterproNameHandler(stream);
                } catch (IOException e) {
                    interproHandler = new InterproNameHandler(getFileByResources("/interpro-entry-local.txt", InterproNameHandler.class));
                }
            }
            return interproHandler.getNameById(id);
        } catch (NameNotFoundException e) {
            if (logger.isDebugEnabled()) {
                logger.info("No Description for " + id + " found.");
            }
            return null;
        }
    }

    /**
     * Gets the additional information of the BinaryInteraction.
     */
    public void updateHeader(PsimitabHeader header) {
        header.appendColumnName("Experimental role(s) interactor A");
        header.appendColumnName("Experimental role(s) interactor B");
        header.appendColumnName("Biological role(s) interactor A");
        header.appendColumnName("Biological role(s) interactor B");
        header.appendColumnName("Properties interactor A");
        header.appendColumnName("Properties interactor B");
        header.appendColumnName("Type(s) interactor A");
        header.appendColumnName("Type(s) interactor B");
        header.appendColumnName("HostOrganism(s)");
        header.appendColumnName("Expansion method(s)");
        header.appendColumnName("Dataset name(s)");
    }

    /**
     * Sets the additional colums for the BinaryInteraction.
     */
    public void formatAdditionalColumns(IntactBinaryInteraction bi, StringBuffer sb) {
        if (bi.hasExperimentalRolesInteractorA()) {
            sb.append(TabulatedLineFormatter.formatCv(bi.getExperimentalRolesInteractorA()));
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasExperimentalRolesInteractorB()) {
            sb.append(TabulatedLineFormatter.formatCv(bi.getExperimentalRolesInteractorB()));
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasBiologicalRolesInteractorA()) {
            sb.append(TabulatedLineFormatter.formatCv(bi.getBiologicalRolesInteractorA()));
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasBiologicalRolesInteractorB()) {
            sb.append(TabulatedLineFormatter.formatCv(bi.getBiologicalRolesInteractorB()));
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasPropertiesA()) {
            String formatedText = TabulatedLineFormatter.formatCv(bi.getPropertiesA());
            sb.append(formatedText);
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasPropertiesB()) {
            sb.append(TabulatedLineFormatter.formatCv(bi.getPropertiesB()));
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasInteractorTypeA()) {
            sb.append(TabulatedLineFormatter.formatCv(bi.getInteractorTypeA()));
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasInteractorTypeB()) {
            sb.append(TabulatedLineFormatter.formatCv(bi.getInteractorTypeB()));
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasHostOrganism()) {
            sb.append(TabulatedLineFormatter.formatCv(bi.getHostOrganism()));
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasExpansionMethods()) {
            for (Iterator<String> iterator = bi.getExpansionMethods().iterator(); iterator.hasNext(); ) {
                final String method = iterator.next();
                sb.append(method);
                if (iterator.hasNext()) {
                    sb.append(TabulatedLineFormatter.PIPE);
                }
            }
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
        if (bi.hasDatasetName()) {
            sb.append(formatStringList(bi.getDataset()));
        } else {
            sb.append(LineFormatter.NONE);
        }
        sb.append(TabulatedLineFormatter.TAB);
    }

    private String formatStringList(List<String> field) {
        StringBuffer sb = new StringBuffer(64);
        if (field != null && !field.isEmpty()) {
            for (Iterator<String> iterator = field.iterator(); iterator.hasNext(); ) {
                sb.append(iterator.next());
                if (iterator.hasNext()) {
                    sb.append(LineFormatter.PIPE);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Gets the additional information of the BinaryInteraction.
     */
    public void parseColumn(IntactBinaryInteraction bi, Iterator<Column> columnIterator) throws MitabLineException {
        if (columnIterator.hasNext()) {
            String field16 = columnIterator.next().getData();
            bi.setExperimentalRolesInteractorA(MitabLineParserUtils.parseCrossReference(field16));
        }
        if (columnIterator.hasNext()) {
            String field17 = columnIterator.next().getData();
            bi.setExperimentalRolesInteractorB(MitabLineParserUtils.parseCrossReference(field17));
        }
        if (columnIterator.hasNext()) {
            String field18 = columnIterator.next().getData();
            bi.setBiologicalRolesInteractorA(MitabLineParserUtils.parseCrossReference(field18));
        }
        if (columnIterator.hasNext()) {
            String field19 = columnIterator.next().getData();
            bi.setBiologicalRolesInteractorB(MitabLineParserUtils.parseCrossReference(field19));
        }
        if (columnIterator.hasNext()) {
            String field20 = columnIterator.next().getData();
            bi.setPropertiesA(MitabLineParserUtils.parseCrossReference(field20));
        }
        if (columnIterator.hasNext()) {
            String field21 = columnIterator.next().getData();
            bi.setPropertiesB(MitabLineParserUtils.parseCrossReference(field21));
        }
        if (columnIterator.hasNext()) {
            String field22 = columnIterator.next().getData();
            bi.setInteractorTypeA(MitabLineParserUtils.parseCrossReference(field22));
        }
        if (columnIterator.hasNext()) {
            String field23 = columnIterator.next().getData();
            bi.setInteractorTypeB(MitabLineParserUtils.parseCrossReference(field23));
        }
        if (columnIterator.hasNext()) {
            String field24 = columnIterator.next().getData();
            bi.setHostOrganism(MitabLineParserUtils.parseCrossReference(field24));
        }
        if (columnIterator.hasNext()) {
            String field25 = columnIterator.next().getData();
            bi.setExpansionMethod(field25);
        }
        if (columnIterator.hasNext()) {
            String field26 = columnIterator.next().getData();
            bi.setDataset(parseStringList(field26));
        }
    }

    /**
     * This method parse the information from BinaryInteraction
     */
    public void parseColumn(IntactBinaryInteraction bi, StringTokenizer st) throws MitabLineException {
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 16 + " must not be empty.");
        String field16 = st.nextToken();
        bi.setExperimentalRolesInteractorA(MitabLineParserUtils.parseCrossReference(field16));
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 17 + " must not be empty.");
        String field17 = st.nextToken();
        bi.setExperimentalRolesInteractorB(MitabLineParserUtils.parseCrossReference(field17));
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 18 + " must not be empty.");
        String field18 = st.nextToken();
        bi.setBiologicalRolesInteractorA(MitabLineParserUtils.parseCrossReference(field18));
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 19 + " must not be empty.");
        String field19 = st.nextToken();
        bi.setBiologicalRolesInteractorB(MitabLineParserUtils.parseCrossReference(field19));
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 20 + " must not be empty.");
        String field20 = st.nextToken();
        bi.setPropertiesA(MitabLineParserUtils.parseCrossReference(field20));
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 21 + " must not be empty.");
        String field21 = st.nextToken();
        bi.setPropertiesB(MitabLineParserUtils.parseCrossReference(field21));
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 22 + " must not be empty.");
        String field22 = st.nextToken();
        bi.setInteractorTypeA(MitabLineParserUtils.parseCrossReference(field22));
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 23 + " must not be empty.");
        String field23 = st.nextToken();
        bi.setInteractorTypeB(MitabLineParserUtils.parseCrossReference(field23));
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 24 + " must not be empty.");
        String field24 = st.nextToken();
        bi.setHostOrganism(MitabLineParserUtils.parseCrossReference(field24));
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 25 + " must not be empty.");
        String field25 = st.nextToken();
        bi.setExpansionMethod(field25);
        if (!st.hasMoreTokens()) throw new MitabLineException("Column " + 26 + " must not be empty.");
        String field26 = st.nextToken();
        bi.setDataset(parseStringList(field26));
    }

    private List<String> parseStringList(String field) throws MitabLineException {
        if (!MitabLineParserUtils.isFieldEmpty(field)) {
            String[] strings = field.split(MitabLineParserUtils.PIPE);
            return Arrays.asList(strings);
        }
        return null;
    }

    /**
     * This methode creates valid Participants and Interactors for the xml.
     */
    public void updateParticipants(IntactBinaryInteraction binaryInteraction, Participant pA, Participant pB, int index) {
        Interactor iA = pA.getInteractor();
        Interactor iB = pB.getInteractor();
        IntactBinaryInteraction dbi = binaryInteraction;
        if (dbi.hasExperimentalRolesInteractorA()) {
            pA.getExperimentalRoles().clear();
            ExperimentalRole experimentalRole = updateExperimentalRoles(dbi.getExperimentalRolesInteractorA(), index);
            if (!pA.getExperimentalRoles().add(experimentalRole)) {
                if (logger.isDebugEnabled()) logger.debug("ExperimentalRole couldn't add to the participant");
            }
        }
        if (dbi.hasExperimentalRolesInteractorB()) {
            pB.getExperimentalRoles().clear();
            ExperimentalRole experimentalRole = updateExperimentalRoles(dbi.getExperimentalRolesInteractorB(), index);
            if (!pB.getExperimentalRoles().add(experimentalRole)) {
                if (logger.isDebugEnabled()) logger.debug("ExperimentalRole couldn't add to the participant");
            }
        }
        try {
            if (dbi.hasInteractorTypeA()) {
                InteractorType typeA = (InteractorType) xConverter.fromMitab(dbi.getInteractorTypeA().get(0), InteractorType.class);
                iA.setInteractorType(typeA);
            }
            if (dbi.hasInteractorTypeB()) {
                InteractorType typeB = (InteractorType) xConverter.fromMitab(dbi.getInteractorTypeB().get(0), InteractorType.class);
                iB.setInteractorType(typeB);
            }
            if (dbi.hasPropertiesA()) {
                Collection<DbReference> secDbRef = getSecondaryRefs(dbi.getPropertiesA());
                iA.getXref().getSecondaryRef().addAll(secDbRef);
            }
            if (dbi.hasPropertiesB()) {
                Collection<DbReference> secDbRef = getSecondaryRefs(dbi.getPropertiesB());
                iB.getXref().getSecondaryRef().addAll(secDbRef);
            }
        } catch (XmlConvertionException e) {
            e.printStackTrace();
        }
    }

    private ExperimentalRole updateExperimentalRoles(List<CrossReference> experimentalRoles, int index) {
        String roleA = experimentalRoles.get(index).getText();
        String dbA = experimentalRoles.get(index).getDatabase().concat(":".concat(experimentalRoles.get(0).getIdentifier()));
        Names names = new Names();
        if (roleA == null) {
            names.setShortLabel("unspecified role");
            names.setFullName("unspecified role");
        } else {
            names.setShortLabel(roleA);
            names.setFullName(roleA);
        }
        DbReference dbRef = new DbReference();
        dbRef.setDb("psi-mi");
        if (dbA == null) {
            dbRef.setId("MI:0499");
        } else {
            dbRef.setId(dbA);
        }
        dbRef.setDbAc("MI:0488");
        dbRef.setRefType("identity");
        dbRef.setRefTypeAc("MI:0356");
        Xref experimentalXref = new Xref(dbRef);
        return new ExperimentalRole(names, experimentalXref);
    }

    private Collection<DbReference> getSecondaryRefs(List<CrossReference> properties) {
        Collection<DbReference> refs = new ArrayList<DbReference>();
        for (CrossReference property : properties) {
            DbReference secDbRef = new DbReference();
            secDbRef.setDb(property.getDatabase());
            if (property.getDatabase().equalsIgnoreCase("GO")) {
                secDbRef.setId(property.getDatabase().concat(":".concat(property.getIdentifier())));
                secDbRef.setDbAc("MI:0448");
            } else {
                secDbRef.setId(property.getIdentifier());
                if (property.getDatabase().equals("interpro")) {
                    secDbRef.setDbAc("MI:0449");
                }
                if (property.getDatabase().equals("intact")) {
                    secDbRef.setDbAc("MI:0469");
                }
                if (property.getDatabase().equals("uniprotkb")) {
                    secDbRef.setDbAc("MI:0486");
                }
            }
            if (property.hasText()) {
                secDbRef.setSecondary(property.getText());
            }
            refs.add(secDbRef);
        }
        return refs;
    }

    /**
     * This method updates the default hostOrganism for the xml.
     */
    public void updateHostOrganism(IntactBinaryInteraction bi, Organism hostOrganism, int index) {
        if (bi.hasHostOrganism()) {
            CrossReference o = bi.getHostOrganism().get(index);
            int taxid = Integer.parseInt(o.getIdentifier());
            hostOrganism.setNcbiTaxId(taxid);
            Names organismNames = new Names();
            organismNames.setShortLabel(o.getDatabase());
            if (o.hasText()) organismNames.setFullName(o.getText());
            hostOrganism.setNames(organismNames);
        }
    }

    /**
     * Merge a Collection
     *
     * @param source
     * @param target
     */
    public void mergeCollection(IntactBinaryInteraction source, IntactBinaryInteraction target) {
        target.setExperimentalRolesInteractorA(mergeCollections(source.getExperimentalRolesInteractorA(), target.getExperimentalRolesInteractorA()));
        target.setExperimentalRolesInteractorB(mergeCollections(source.getExperimentalRolesInteractorB(), target.getExperimentalRolesInteractorB()));
        target.setBiologicalRolesInteractorA(mergeCollections(source.getBiologicalRolesInteractorA(), target.getBiologicalRolesInteractorA()));
        target.setBiologicalRolesInteractorB(mergeCollections(source.getBiologicalRolesInteractorB(), target.getBiologicalRolesInteractorB()));
        target.setHostOrganism(mergeCollections(source.getHostOrganism(), target.getHostOrganism()));
        target.setDataset(mergeCollections(source.getDataset(), target.getDataset()));
    }

    private <T> List<T> mergeCollections(Collection<T> colA, Collection<T> colB) {
        List<T> list = new ArrayList<T>();
        if (colA != null) list.addAll(colA);
        if (colB != null) list.addAll(colB);
        return list;
    }

    public void process(IntactBinaryInteraction bi, Interaction interaction, ExpansionStrategy expansionStrategy) {
        process(bi, interaction);
        if (bi instanceof IntactBinaryInteraction) {
            ((IntactBinaryInteraction) bi).setExpansionMethod(expansionStrategy.getName());
        }
    }

    /**
     * Decodes URL before getting File (usefull if path contains whitespaces).
     *
     * @param fileName
     * @param clazz
     * @return
     */
    private static File getFileByResources(String fileName, Class clazz) {
        URL url = clazz.getResource(fileName);
        String strFile = url.getFile();
        try {
            return new File(URLDecoder.decode(strFile, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
