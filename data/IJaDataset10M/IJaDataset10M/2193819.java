package uk.ac.ebi.intact.application.graph2MIF.conversion;

import org.apache.log4j.Logger;
import org.apache.xerces.dom.DOMImplementationImpl;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException;
import uk.ac.ebi.intact.application.graph2MIF.exception.GraphNotConvertableException;
import uk.ac.ebi.intact.model.*;
import uk.ac.ebi.intact.simpleGraph.BasicGraph;
import uk.ac.ebi.intact.simpleGraph.Edge;
import uk.ac.ebi.intact.simpleGraph.Graph;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;

/**
 * This class gives IntAct the possibillity to give out a graph to MIF-PSI-Format in XML.
 * see http://psidev.sf.net for details
 *
 * @author Henning Mersch (hmersch@ebi.ac.uk)
 * @version $Id: Graph2UnfoldedMIF.java 4404 2005-10-26 12:39:18Z catherineleroy $
 */
public class Graph2UnfoldedMIF {

    /**
     *  logger for proper information
     *  see config/log4j.properties for more informtation
     */
    static Logger logger = Logger.getLogger("graph2MIF");

    /**
     * The general DOM-Object accessible for all methods is required for creating Elements
     */
    private Document doc;

    /**
     * This Boolean indicates if an exception will be thrown if a required tag of MIF is missing
     * at the IntAct graph
     */
    private boolean STRICT_MIF;

    /**
     * Contructor without parameter - strict MIF will be produced or Exception will be thrown
     */
    public Graph2UnfoldedMIF() {
        this(new Boolean(true));
    }

    /**
     * Contructor with parameter
     * @param strictmif defines wether MIF should be strict produced or not.
     */
    public Graph2UnfoldedMIF(Boolean strictmif) {
        STRICT_MIF = strictmif.booleanValue();
    }

    /**
     * The only public function.
     * Calling this with a Graph object will give you a
     * complete DOM object according to PSI MIF specification, which you can easily can print out.
     * @param graph to convert to PSI-Format
     * @return DOM-Object, representing a XML Document in PSI-Format
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.GraphNotConvertableException if PSIrequired Elements are missing within graph
     */
    public Document getMIF(Graph graph) throws GraphNotConvertableException {
        DOMImplementationImpl impl = new DOMImplementationImpl();
        doc = impl.createDocument("net:sf:psidev:mi", "entrySet", null);
        Element psiEntrySet = doc.getDocumentElement();
        psiEntrySet.setAttribute("xmlns", "net:sf:psidev:mi");
        psiEntrySet.setAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        psiEntrySet.setAttribute("xsi:schemaLocation", "net:sf:psidev:mi http://psidev.sourceforge.net/mi/xml/src/MIF.xsd");
        psiEntrySet.setAttribute("level", "1");
        psiEntrySet.setAttribute("version", "1");
        try {
            Element psiEntry = procGraph(graph);
            psiEntrySet.appendChild(psiEntry);
        } catch (ElementNotParseableException e) {
            logger.warn("could not proceed graph: " + e.getMessage());
            if (STRICT_MIF) {
                throw new GraphNotConvertableException(e.getMessage());
            }
        }
        if (!doc.hasChildNodes()) {
            logger.warn("graph failed, no child elements.");
            throw new GraphNotConvertableException("doc has no Child Elements");
        }
        return doc;
    }

    /**
     * Start of processing the graph
     * @param graph to convert to PSI-Format
     * @return DOM-Object, representing a <entry>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.simpleGraph.Graph
     *
     */
    private Element procGraph(Graph graph) throws ElementNotParseableException {
        Element psiEntry = doc.createElement("entry");
        Element psiSource = doc.createElement("source");
        psiEntry.appendChild(psiSource);
        try {
            Element psiNames = getPsiNamesOfBasicGraph(graph);
            psiSource.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.info("source/names failed (not required):" + e.getMessage());
        }
        Calendar cal = Calendar.getInstance();
        String dayString;
        if (cal.get(Calendar.DAY_OF_MONTH) <= 9) {
            dayString = "0" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        } else {
            dayString = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
        }
        String monthString;
        int month = cal.get(Calendar.MONTH) + 1;
        if (month <= 9) {
            monthString = "0" + month;
        } else {
            monthString = Integer.toString(month);
        }
        String date = cal.get(Calendar.YEAR) + "-" + monthString + "-" + dayString;
        psiSource.setAttribute("releaseDate", date);
        try {
            Element psiInteractionList = procEdges(graph.getEdges());
            psiEntry.appendChild(psiInteractionList);
        } catch (ElementNotParseableException e) {
            logger.warn("edges failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("Interaction without Interactions:" + e.getMessage());
            }
        }
        if (!psiEntry.hasChildNodes()) {
            logger.warn("graph failed, no child elements.");
            throw new ElementNotParseableException("graph has no Child Elements");
        }
        return psiEntry;
    }

    /**
     * process edges of graph
     * @param edges to convert to PSI-Format
     * @return DOM-Object, representing a <interaction>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.simpleGraph.Edge
     */
    private Element procEdges(Collection edges) throws ElementNotParseableException {
        Element psiInteractionList = doc.createElement("interactionList");
        Iterator iterator = edges.iterator();
        while (iterator.hasNext()) {
            try {
                Element psiInteraction = procEdge((Edge) iterator.next());
                psiInteractionList.appendChild(psiInteraction);
            } catch (ElementNotParseableException e) {
                logger.info("edge failed (not required):" + e.getMessage());
            }
        }
        if (!psiInteractionList.hasChildNodes()) {
            logger.warn("edges failed, no child elements.");
            throw new ElementNotParseableException("InteractionList has no Child Elements");
        }
        return psiInteractionList;
    }

    /**
     * process edge
     * @param edge to convert to PSI-Format
     * @return DOM-Object, representing a <proteinInteractor>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.simpleGraph.Edge
     */
    private Element procEdge(Edge edge) throws ElementNotParseableException {
        Element psiInteraction = doc.createElement("interaction");
        try {
            Element psiNames = getPsiNamesOfBasicGraph(edge);
            psiInteraction.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.info("names failed (not required):" + e.getMessage());
        }
        Collection relExperiments = edge.getComponent1().getInteraction().getExperiments();
        Element psiExperimentList = null;
        try {
            psiExperimentList = procExperiments(relExperiments);
            psiInteraction.appendChild(psiExperimentList);
        } catch (ElementNotParseableException e) {
            logger.warn("experiments failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("Interactor without experiment:" + e.getMessage());
            }
        }
        Element psiParticipantList = doc.createElement("participantList");
        psiInteraction.appendChild(psiParticipantList);
        try {
            Element psiProteinParticipant1 = procComponent(edge.getComponent1());
            psiParticipantList.appendChild(psiProteinParticipant1);
            Element psiProteinParticipant2 = procComponent(edge.getComponent2());
            psiParticipantList.appendChild(psiProteinParticipant2);
        } catch (ElementNotParseableException e) {
            logger.warn("compartment failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no 2 proteinParticipants in interaction:" + e.getMessage());
            }
        }
        Element psiInteractionType = null;
        try {
            psiInteractionType = procCvInteractionType(edge.getComponent1().getInteraction().getCvInteractionType());
            psiInteraction.appendChild(psiInteractionType);
        } catch (ElementNotParseableException e) {
            logger.info("interactionType failed (not required):" + e.getMessage());
        }
        try {
            Collection xrefs = edge.getComponent1().getInteraction().getXrefs();
            Element psiXref = procXrefCollection(xrefs);
            psiInteraction.appendChild(psiXref);
        } catch (ElementNotParseableException e) {
            logger.info("xref failed (not required):" + e.getMessage());
        } catch (NullPointerException e) {
            logger.info("xref failed (not required):" + e.getMessage());
        }
        if (!psiInteraction.hasChildNodes()) {
            logger.warn("edge failed, no child elements.");
            throw new ElementNotParseableException("Interaction has no Child Elements");
        }
        return psiInteraction;
    }

    /**
     * process experiments
     * @param experiments to convert to PSI-Format
     * @return DOM-Object, representing a <experimentList>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.Experiment
     */
    private Element procExperiments(Collection experiments) throws ElementNotParseableException {
        Element psiExperimentList = doc.createElement("experimentList");
        Iterator experimentList = experiments.iterator();
        if (!experimentList.hasNext()) {
            logger.warn("experimentlist without one experiment -  failed (required):");
            if (STRICT_MIF) {
                throw new ElementNotParseableException("not one experiment");
            }
        }
        while (experimentList.hasNext()) {
            Experiment experiment = (Experiment) experimentList.next();
            try {
                Element psiExperimentDescription = procExperiment(experiment);
                psiExperimentList.appendChild(psiExperimentDescription);
            } catch (ElementNotParseableException e) {
                logger.info("experiment failed (not required):" + e.getMessage());
            }
        }
        if (!psiExperimentList.hasChildNodes()) {
            logger.warn("experiments failed, no child elements.");
            throw new ElementNotParseableException("ExperimentList has no Child Elements");
        }
        return psiExperimentList;
    }

    /**
     * process experiment
     * @param experiment to convert to PSI-Format
     * @return DOM-Object, representing a <exteriment>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.Experiment
     */
    private Element procExperiment(Experiment experiment) throws ElementNotParseableException {
        Element psiExperimentDescription = doc.createElement("experimentDescription");
        try {
            Element psiNames = getPsiNamesOfAnnotatedObject(experiment);
            psiExperimentDescription.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.info("names failed (not required):" + e.getMessage());
        }
        try {
            Element psiXrefPubMed = procXrefCollectionSelectingPubMed(experiment.getXrefs());
            Element psiBibref = doc.createElement("bibref");
            psiBibref.appendChild(psiXrefPubMed);
            psiExperimentDescription.appendChild(psiBibref);
        } catch (ElementNotParseableException e) {
            logger.info("xref(pubmed) failed (not required):" + e.getMessage());
        }
        try {
            Element psiXrefNotPubMed = procXrefCollectionSelectingNotPubMed(experiment.getXrefs());
            psiExperimentDescription.appendChild(psiXrefNotPubMed);
        } catch (ElementNotParseableException e) {
            logger.info("xref(not pubmed) failed (not required):" + e.getMessage());
        }
        try {
            Element psiHostOrganism = procBioSourceAsHost(experiment.getBioSource());
            psiExperimentDescription.appendChild(psiHostOrganism);
        } catch (ElementNotParseableException e) {
            logger.info("biosource failed (not required):" + e.getMessage());
        }
        try {
            Element psiInteractionDetection = procCvInteractionDetection(experiment.getCvInteraction());
            psiExperimentDescription.appendChild(psiInteractionDetection);
        } catch (ElementNotParseableException e) {
            logger.warn("cvInteraction failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no interactionDetection created::" + e.getMessage());
            }
        }
        try {
            Element psiIdentification = procCvIdentification(experiment.getCvIdentification());
            psiExperimentDescription.appendChild(psiIdentification);
        } catch (ElementNotParseableException e) {
            logger.info("cvIdentification failed (not required):" + e.getMessage());
        }
        try {
            psiExperimentDescription.setAttribute("id", experiment.getAc());
        } catch (NullPointerException e) {
            logger.warn("Ac of Experiment failed - required !");
            if (STRICT_MIF) {
                throw new ElementNotParseableException("Experiment without Ac:" + e.getMessage());
            }
        }
        if (!psiExperimentDescription.hasChildNodes()) {
            logger.warn("Experiment failed, no child elements.");
            throw new ElementNotParseableException("ExperimentDescription has no Child Elements");
        }
        return psiExperimentDescription;
    }

    /**
     * process cvInteractionDetection
     * @param cvInteractionDetection to convert to PSI-Format
     * @return DOM-Object, representing a <interactionDetection>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.CvInteraction
     */
    private Element procCvInteractionDetection(CvInteraction cvInteractionDetection) throws ElementNotParseableException {
        Element psiInteractionDetection = doc.createElement("interactionDetection");
        try {
            Element psiNames = getPsiNamesOfAnnotatedObject(cvInteractionDetection);
            psiInteractionDetection.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.warn("names failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvInteractionDetection ShortLabel");
            }
        }
        try {
            Element psiXref = procXrefCollectionOfControlledVocabulary(cvInteractionDetection.getXrefs());
            psiInteractionDetection.appendChild(psiXref);
        } catch (ElementNotParseableException e) {
            logger.warn("xref failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvInteractionDetection Xref");
            }
        } catch (NullPointerException e) {
            if (STRICT_MIF) {
                logger.warn("xref failed (required):" + e.getMessage());
                throw new ElementNotParseableException("no cvInteractionDetection Xref");
            }
        }
        if (!psiInteractionDetection.hasChildNodes()) {
            logger.warn("cvInteractionDetection failed, no child elements.");
            throw new ElementNotParseableException("interactionDetection has no Child Elements");
        }
        return psiInteractionDetection;
    }

    /**
     * process cvInteractionType
     * @param cvInteractionType to convert to PSI-Format
     * @return DOM-Object, representing a <interactionDetection>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.CvInteraction
     */
    private Element procCvInteractionType(CvInteractionType cvInteractionType) throws ElementNotParseableException {
        Element psiInteractionDetection = doc.createElement("interactionType");
        try {
            Element psiNames = getPsiNamesOfAnnotatedObject(cvInteractionType);
            psiInteractionDetection.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.warn("names failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvInteractionType ShortLabel");
            }
        }
        try {
            Element psiXref = procXrefCollectionOfControlledVocabulary(cvInteractionType.getXrefs());
            psiInteractionDetection.appendChild(psiXref);
        } catch (ElementNotParseableException e) {
            logger.warn("xref failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvInteractionType Xref");
            }
        } catch (NullPointerException e) {
            if (STRICT_MIF) {
                logger.warn("xref failed (required):" + e.getMessage());
                throw new ElementNotParseableException("no cvInteractionType Xref");
            }
        }
        if (!psiInteractionDetection.hasChildNodes()) {
            logger.warn("cvInteractionType failed, no child elements.");
            throw new ElementNotParseableException("interactionDetection has no Child Elements");
        }
        return psiInteractionDetection;
    }

    /**
     * process cvIdentification
     * @param cvIdentification to convert to PSI-Format
     * @return DOM-Object, representing a <participantDetection>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.CvIdentification
     */
    private Element procCvIdentification(CvIdentification cvIdentification) throws ElementNotParseableException {
        Element psiParticipantDetection = doc.createElement("participantDetection");
        try {
            Element psiNames = getPsiNamesOfAnnotatedObject(cvIdentification);
            psiParticipantDetection.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.warn("names failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvInteraction ShortLabel");
            }
        }
        try {
            Element psiXref = procXrefCollectionOfControlledVocabulary(cvIdentification.getXrefs());
            psiParticipantDetection.appendChild(psiXref);
        } catch (ElementNotParseableException e) {
            logger.warn("xref failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvInteraction Xref");
            }
        } catch (NullPointerException e) {
            logger.warn("xref failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvInteraction Xref");
            }
        }
        if (!psiParticipantDetection.hasChildNodes()) {
            logger.warn("cvIdentification failed, no child elements.");
            throw new ElementNotParseableException("ParticipantDetection has no Child Elements");
        }
        return psiParticipantDetection;
    }

    /**
     * process component
     * @param component to convert to PSI-Format
     * @return DOM-Object, representing a <proteinParticipant>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.Component
     */
    private Element procComponent(Component component) throws ElementNotParseableException {
        Element psiProteinParticipant = doc.createElement("proteinParticipant");
        try {
            Element psiProteinInteractor = procInteractor(component.getInteractor());
            psiProteinParticipant.appendChild(psiProteinInteractor);
        } catch (ElementNotParseableException e) {
            logger.warn("interactor failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no interactor:" + e.getMessage());
            }
        }
        try {
            Element psiFeatureList = procFeatureList(component.getBindingDomains());
            psiProteinParticipant.appendChild(psiFeatureList);
        } catch (ElementNotParseableException e) {
            logger.info("featureList failed (not required):" + e.getMessage());
        }
        try {
            Element psiRole = procRole(component.getCvComponentRole());
            psiProteinParticipant.appendChild(psiRole);
        } catch (NullPointerException e) {
            logger.info("cvComponentRole failed (not required):" + e.getMessage());
        }
        if (!psiProteinParticipant.hasChildNodes()) {
            logger.warn("component failed, no child elements.");
            throw new ElementNotParseableException("proteinParticipant has no Child Elements");
        }
        return psiProteinParticipant;
    }

    /**
     * process role
     * @param cvComponentRole to convert to PSI-Format
     * @return DOM-Object, representing a <featureList>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.CvComponentRole
     */
    private Element procRole(CvComponentRole cvComponentRole) throws ElementNotParseableException {
        Element psiRole = doc.createElement("role");
        try {
            psiRole.appendChild(doc.createTextNode(cvComponentRole.getShortLabel()));
        } catch (NullPointerException e) {
            logger.warn("shortLabel failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("role without shortLabel");
            }
        }
        if (!psiRole.hasChildNodes()) {
            logger.warn("cvComponentRole failed, no child elements.");
            throw new ElementNotParseableException("role has no Child Elements");
        }
        return psiRole;
    }

    /**
     * process FeatureList
     * @param features to convert to PSI-Format
     * @return DOM-Object, representing a <featureList>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.Feature
     */
    private Element procFeatureList(Collection features) throws ElementNotParseableException {
        Element psiFeatureList = doc.createElement("featureList");
        Iterator featureList = features.iterator();
        if (!featureList.hasNext()) {
            logger.warn("empty FeatureList failed (required):");
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no features found");
            }
        }
        while (featureList.hasNext()) {
            Feature feature = (Feature) featureList.next();
            try {
                Element psiFeature = procFeature(feature);
                psiFeatureList.appendChild(psiFeature);
            } catch (ElementNotParseableException e) {
                logger.info("feature failed (not required):" + e.getMessage());
            }
        }
        if (!psiFeatureList.hasChildNodes()) {
            logger.warn("featurelist failed, no child elements.");
            throw new ElementNotParseableException("FeatureList has no Child Elements");
        }
        return psiFeatureList;
    }

    /**
     * process Feature
     * @param feature to convert to PSI-Format
     * @return DOM-Object, representing a <featureList>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.Feature
     */
    private Element procFeature(Feature feature) throws ElementNotParseableException {
        Element psiFeature = doc.createElement("feature");
        try {
            Element psiXref = procXrefCollection(feature.getXrefs());
            psiFeature.appendChild(psiXref);
        } catch (ElementNotParseableException e) {
            logger.info("xref failed (not required):" + e.getMessage());
        }
        try {
            Element psiFeatureDescription = procCvFeatureType(feature.getCvFeatureType());
            psiFeature.appendChild(psiFeatureDescription);
        } catch (ElementNotParseableException e) {
            logger.info("cvFeatureTyoe failed (not required):" + e.getMessage());
        }
        try {
            Element psiFeatureDetection = procCvFeatureIdentification(feature.getCvFeatureIdentification());
            psiFeature.appendChild(psiFeatureDetection);
        } catch (ElementNotParseableException e) {
            logger.info("cvFeatureIdentification failed (not required):" + e.getMessage());
        }
        try {
            Element psiLocation = procLocation();
            psiFeature.appendChild(psiLocation);
        } catch (ElementNotParseableException e) {
            logger.warn("location failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("Feature-Location cant created:" + e.getMessage());
            }
        }
        if (!psiFeature.hasChildNodes()) {
            logger.warn("feature failed, no child elements.");
            throw new ElementNotParseableException("Feature has no Child Elements");
        }
        return psiFeature;
    }

    /**
     * process Location - which is not yet implemented in IntAct
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.Feature (Binding Domain)
     */
    private Element procLocation() throws ElementNotParseableException {
        logger.warn("location failed (required): NOT IMPLEMENTED");
        throw new ElementNotParseableException("not implemented in IntAct");
    }

    /**
     * process cvFeatureType
     * @param cvFeatureType to convert to PSI-Format
     * @return DOM-Object, representing a <featureDescription>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.CvFeatureType
     */
    private Element procCvFeatureType(CvFeatureType cvFeatureType) throws ElementNotParseableException {
        Element psiFeatureDescription = doc.createElement("featureDescription");
        try {
            Element psiNames = getPsiNamesOfAnnotatedObject(cvFeatureType);
            psiFeatureDescription.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.warn("names failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvFeatureType ShortLabel");
            }
        }
        try {
            Element psiXref = procXrefCollectionOfControlledVocabulary(cvFeatureType.getXrefs());
            psiFeatureDescription.appendChild(psiXref);
        } catch (ElementNotParseableException e) {
            logger.warn("xref failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvFeatureType Xref");
            }
        } catch (NullPointerException e) {
            logger.warn("xref failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvFeatureType Xref");
            }
        }
        if (!psiFeatureDescription.hasChildNodes()) {
            logger.warn("cvFeatureType failed, no child elements.");
            throw new ElementNotParseableException("FeatureDescription has no Child Elements");
        }
        return psiFeatureDescription;
    }

    /**
     * process cvFeatureIdentification
     * @param cvFeatureIdentification to convert to PSI-Format
     * @return DOM-Object, representing a <featureDescription>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.CvFeatureIdentification
     */
    private Element procCvFeatureIdentification(CvFeatureIdentification cvFeatureIdentification) throws ElementNotParseableException {
        Element psiFeatureDetection = doc.createElement("featureDetection");
        try {
            Element psiNames = getPsiNamesOfAnnotatedObject(cvFeatureIdentification);
            psiFeatureDetection.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.warn("cvFeatureIdentification failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvFeatureIdentification ShortLabel");
            }
        }
        try {
            Element psiXref = procXrefCollectionOfControlledVocabulary(cvFeatureIdentification.getXrefs());
            psiFeatureDetection.appendChild(psiXref);
        } catch (ElementNotParseableException e) {
            logger.warn("xref failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvFeatureIdentification Xref");
            }
        } catch (NullPointerException e) {
            logger.warn("xref failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no cvFeatureIdentification Xref");
            }
        }
        if (!psiFeatureDetection.hasChildNodes()) {
            logger.warn("cvFeatureIdentification failed, no child elements.");
            throw new ElementNotParseableException("FeatureDetection has no Child Elements");
        }
        return psiFeatureDetection;
    }

    /**
     * process interactor
     * @param interactor to convert to PSI-Format
     * @return DOM-Object, representing a <proteinInteractor>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if PSIrequired Elements are missing within graph
     * @see uk.ac.ebi.intact.model.Interactor
     */
    private Element procInteractor(Interactor interactor) throws ElementNotParseableException {
        Element psiProteinInteractor = doc.createElement("proteinInteractor");
        try {
            Element psiNames = getPsiNamesOfAnnotatedObject(interactor);
            psiProteinInteractor.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.warn("names failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("Interactor without name" + e.getMessage());
            }
        }
        try {
            Element psiXref = procXrefCollectionOfProtein(interactor.getXrefs());
            psiProteinInteractor.appendChild(psiXref);
        } catch (ElementNotParseableException e) {
            logger.info("xref failed (not required):" + e.getMessage());
        }
        try {
            psiProteinInteractor.setAttribute("id", interactor.getAc());
        } catch (NullPointerException e) {
            logger.warn("Ac of interactor failed - required !");
            if (STRICT_MIF) {
                throw new ElementNotParseableException("Interactor without Ac:" + e.getMessage());
            }
        }
        try {
            Element psiOrganism = procBioSource(interactor.getBioSource());
            psiProteinInteractor.appendChild(psiOrganism);
        } catch (ElementNotParseableException e) {
            logger.info("BioSource failed (not required):" + e.getMessage());
        }
        try {
            Element psiSequence = procSequence((Protein) interactor);
            psiProteinInteractor.appendChild(psiSequence);
        } catch (ElementNotParseableException e) {
            logger.info("sequence failed (not required):" + e.getMessage());
        }
        if (!psiProteinInteractor.hasChildNodes()) {
            logger.warn("Interactor failed, no child elements.");
            throw new ElementNotParseableException("ProteinInteractor has no Child Elements");
        }
        return psiProteinInteractor;
    }

    /**
     * processing protein
     * @param protein - Object  to convert to PSI-Format
     * @return DOM-Object, representing a <sequence>
     * @exception  uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.Protein
     */
    private Element procSequence(Protein protein) throws ElementNotParseableException {
        Element psiSequence = doc.createElement("sequence");
        try {
            psiSequence.appendChild(doc.createTextNode(protein.getSequence()));
        } catch (NullPointerException e) {
            logger.warn("sequence failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("no sequence in protein");
            }
        }
        ;
        if (!psiSequence.hasChildNodes()) {
            logger.warn("protein failed, no child elements.");
            throw new ElementNotParseableException("Sequence has no Child Elements");
        }
        return psiSequence;
    }

    /**
     * Selection of the primaryRef if the collection belongs to a Protein.
     *
     * @param xrefs xrefs from which we'll try to find a primaryRef.
     * @return DOM-Object, representing a <xref>
     * @throws ElementNotParseableException
     */
    private Element procXrefCollectionOfProtein(final Collection xrefs) throws ElementNotParseableException {
        Xref primaryXref = null;
        for (Iterator iterator = xrefs.iterator(); iterator.hasNext(); ) {
            final Xref xref = (Xref) iterator.next();
            final CvDatabase db = xref.getCvDatabase();
            if (db != null) {
                if (CvDatabase.UNIPROT.equals(db.getShortLabel())) {
                    final CvXrefQualifier cvXrefQualifier = xref.getCvXrefQualifier();
                    if (cvXrefQualifier != null) {
                        if ("identity".equals(cvXrefQualifier.getShortLabel())) {
                            primaryXref = xref;
                            break;
                        } else {
                            if (primaryXref == null) {
                                primaryXref = xref;
                            }
                        }
                    } else {
                        if (primaryXref == null) {
                            primaryXref = xref;
                        }
                    }
                }
            }
        }
        return procXrefCollection(xrefs, primaryXref);
    }

    /**
     * Selection of the primaryRef if the collection belongs to a CvObject.
     *
     * @param xrefs xrefs from which we'll try to find a primaryRef.
     * @return DOM-Object, representing a <xref>
     * @throws ElementNotParseableException
     */
    private Element procXrefCollectionOfControlledVocabulary(final Collection xrefs) throws ElementNotParseableException {
        Xref primaryXref = null;
        for (Iterator iterator = xrefs.iterator(); iterator.hasNext(); ) {
            final Xref xref = (Xref) iterator.next();
            final CvDatabase db = xref.getCvDatabase();
            if ("psi-mi".equals(db.getShortLabel())) {
                primaryXref = xref;
                break;
            } else {
                primaryXref = xref;
            }
        }
        return procXrefCollection(xrefs, primaryXref);
    }

    /**
     * Create the DOM representation for a Collection of Xref without particular criteria about the
     * selection of the primaryRef
     *
     * @param xrefs xrefs from which we'll try to find a primaryRef.
     * @return DOM-Object, representing a <xref>
     * @throws ElementNotParseableException
     */
    private Element procXrefCollection(final Collection xrefs) throws ElementNotParseableException {
        return procXrefCollection(xrefs, null);
    }

    /**
     * This method gets an Collection of Xref and, beacause PSI often does not allow a list of xrefs,
     * takes the 1st primary ref as psiPrimaryRef and all others as secondary Refs.
     * if no UNIPROT xref is found, make the first one the primary Xref.
     * This UNIPROT Xref is only valid for Interactor, however that function is used also with :
     * CvInteraction
     * CvIdentification
     * CvFeature
     * cvFeatureIdentification
     * Interactor
     * Interaction
     *
     * @param xrefs - Object  to convert to PSI-Format
     * @return DOM-Object, representing a <xref>
     * @throws uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException
     *          if it is not defined
     * @see uk.ac.ebi.intact.model.Xref
     */
    private Element procXrefCollection(final Collection xrefs, Xref primaryXref) throws ElementNotParseableException {
        Element psiXref = doc.createElement("xref");
        if (primaryXref != null) {
            try {
                Element psiPrimaryRef = procPrimaryRef(primaryXref);
                psiXref.appendChild(psiPrimaryRef);
            } catch (NullPointerException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            } catch (ElementNotParseableException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            }
        }
        for (Iterator iterator = xrefs.iterator(); iterator.hasNext(); ) {
            final Xref xref = (Xref) iterator.next();
            try {
                Element psiRef = null;
                if (primaryXref == null) {
                    primaryXref = xref;
                    psiRef = procPrimaryRef(xref);
                } else {
                    if (primaryXref == xref) {
                        continue;
                    }
                    psiRef = procSecondaryRef(xref);
                }
                psiXref.appendChild(psiRef);
            } catch (NullPointerException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            } catch (ElementNotParseableException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            }
        }
        if (!psiXref.hasChildNodes()) {
            logger.warn("xrefcollection failed, no child elements.");
            throw new ElementNotParseableException("Xref has no Child Elements");
        }
        return psiXref;
    }

    /**
     * This method gets a Xref and convert it to xref with one primaryRef
     * @param xref - Object  to convert to PSI-Format
     * @return DOM-Object, representing a <xref>
     * @exception  uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.Xref
     */
    private Element procXref(Xref xref) throws ElementNotParseableException {
        Element psiXref = doc.createElement("xref");
        try {
            Element psiPrimaryRef = procPrimaryRef(xref);
            psiXref.appendChild(psiPrimaryRef);
        } catch (ElementNotParseableException e) {
            logger.warn("Xref without primary db or id found ! Ignoring !");
            if (STRICT_MIF) {
                throw new ElementNotParseableException("");
            }
        }
        if (!psiXref.hasChildNodes()) {
            logger.warn("xref failed, no child elements.");
            throw new ElementNotParseableException("Xref has no Child Elements");
        }
        return psiXref;
    }

    /**
     * This method gets a collection of Xfres and, beacause PSI often does not allow a list of xrefs,
     * takes the 1st primary ref as psiPrimaryRef,  and all others as secondary Refs.
     * But only from PubMed-DB will be processed.
     * @param xrefs - Object  to convert to PSI-Format
     * @return DOM-Object, representing a <xref>
     * @exception  uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.Xref
     */
    private Element procXrefCollectionSelectingPubMed(Collection xrefs) throws ElementNotParseableException {
        Element psiXref = doc.createElement("xref");
        Iterator iteratorPrim = xrefs.iterator();
        while (iteratorPrim.hasNext()) {
            Xref xref = (Xref) iteratorPrim.next();
            try {
                if ((xref.getCvDatabase().getShortLabel().equalsIgnoreCase("PubMed") && xref.getPrimaryId() != null)) {
                    Element psiPrimaryRef = procPrimaryRef(xref);
                    psiXref.appendChild(psiPrimaryRef);
                    xrefs.remove(xref);
                    break;
                }
            } catch (NullPointerException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            } catch (ElementNotParseableException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            }
        }
        if (!psiXref.hasChildNodes()) {
            logger.warn("xrefcollection failed, no child elements.");
            throw new ElementNotParseableException("couldn't generate primaryID - Xref not parseabel");
        }
        Iterator iteratorSnd = xrefs.iterator();
        while (iteratorSnd.hasNext()) {
            Xref xref = (Xref) iteratorSnd.next();
            try {
                if ((xref.getCvDatabase().getShortLabel().equalsIgnoreCase("PubMed") && xref.getPrimaryId() != null)) {
                    Element psiSecondaryRef = procSecondaryRef(xref);
                    psiXref.appendChild(psiSecondaryRef);
                }
            } catch (NullPointerException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            } catch (ElementNotParseableException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            }
        }
        if (!psiXref.hasChildNodes()) {
            logger.warn("xrefcol failed, no child elements.");
            throw new ElementNotParseableException("Xref has no Child Elements");
        }
        return psiXref;
    }

    /**
     * This method gets an Collection of Xfres and, beacause PSI often does not allow a list of xrefs,
     * takes the 1st primary ref as psiPrimaryRef,  and all others as secondary Refs.
     * But only from NOT PubMed-DB will be processed.
     * @param xrefs - Object  to convert to PSI-Format
     * @return DOM-Object, representing a <xref>
     * @exception  uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.Xref
     */
    private Element procXrefCollectionSelectingNotPubMed(Collection xrefs) throws ElementNotParseableException {
        Element psiXref = doc.createElement("xref");
        Iterator iteratorPrim = xrefs.iterator();
        while (iteratorPrim.hasNext()) {
            Xref xref = (Xref) iteratorPrim.next();
            try {
                if (!(xref.getCvDatabase().getShortLabel().equalsIgnoreCase("PubMed") && xref.getPrimaryId() != null)) {
                    Element psiPrimaryRef = procPrimaryRef(xref);
                    psiXref.appendChild(psiPrimaryRef);
                    xrefs.remove(xref);
                    break;
                }
            } catch (NullPointerException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            } catch (ElementNotParseableException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            }
        }
        if (!psiXref.hasChildNodes()) {
            logger.warn("xrefcol failed, no child elements.");
            throw new ElementNotParseableException("couldn't generate primaryID - Xref not parseabel");
        }
        Iterator iteratorSnd = xrefs.iterator();
        while (iteratorSnd.hasNext()) {
            Xref xref = (Xref) iteratorSnd.next();
            try {
                if (!(xref.getCvDatabase().getShortLabel().equalsIgnoreCase("PubMed") && xref.getPrimaryId() != null)) {
                    Element psiSecondaryRef = procSecondaryRef(xref);
                    psiXref.appendChild(psiSecondaryRef);
                }
            } catch (NullPointerException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            } catch (ElementNotParseableException e) {
                logger.warn("Xref without primary db or id found ! Ignoring !");
            }
        }
        if (!psiXref.hasChildNodes()) {
            logger.warn("xrefcol failed, no child elements.");
            throw new ElementNotParseableException("Xref has no Child Elements");
        }
        return psiXref;
    }

    /**
     * This method gets a Xref and returns a PSI PrimaryRef.
     * @param xref - Object  to convert to PSI-Format
     * @return DOM-Object, representing a <primaryRef>
     * @see uk.ac.ebi.intact.model.Xref
     */
    private Element procPrimaryRef(Xref xref) throws ElementNotParseableException {
        Element psiPrimaryRef = doc.createElement("primaryRef");
        try {
            psiPrimaryRef.setAttribute("db", xref.getCvDatabase().getShortLabel());
            psiPrimaryRef.setAttribute("id", xref.getPrimaryId());
        } catch (NullPointerException e) {
            logger.warn("xref without db or id - failed (required):");
            throw new ElementNotParseableException("PrimaryRef without id or db");
        }
        try {
            psiPrimaryRef.setAttribute("secondary", xref.getSecondaryId());
        } catch (NullPointerException e) {
            logger.info("no secondaryRef");
        }
        try {
            psiPrimaryRef.setAttribute("version", xref.getDbRelease());
        } catch (NullPointerException e) {
            logger.info("no dbRelease");
        }
        return psiPrimaryRef;
    }

    /**
     * This method gets a Xref and return a PSI SecondaryRef.
     * @param xref - Object  to convert to PSI-Format
     * @return DOM-Object, representing a <secondaryRef>
     * @see uk.ac.ebi.intact.model.Xref
     */
    private Element procSecondaryRef(Xref xref) throws ElementNotParseableException {
        Element psiSecondaryRef = doc.createElement("secondaryRef");
        try {
            psiSecondaryRef.setAttribute("db", xref.getCvDatabase().getShortLabel());
            psiSecondaryRef.setAttribute("id", xref.getPrimaryId());
        } catch (NullPointerException e) {
            logger.warn("xref without db or id - failed (required):");
            throw new ElementNotParseableException("SecondaryRef without id or db");
        }
        try {
            psiSecondaryRef.setAttribute("secondary", xref.getSecondaryId());
        } catch (NullPointerException e) {
            logger.info("no secondaryId");
        }
        try {
            psiSecondaryRef.setAttribute("version", xref.getDbRelease());
        } catch (NullPointerException e) {
            logger.info("no dbRelease");
        }
        return psiSecondaryRef;
    }

    /**
     * process bioSource
     * @param bioSource to convert to PSI-Format
     * @return DOM-Object, representing a <hostOrganism>
     * @exception  uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.BioSource
     */
    private Element procBioSourceAsHost(BioSource bioSource) throws ElementNotParseableException {
        Element psiOrganism = doc.createElement("hostOrganism");
        try {
            Element psiNames = getPsiNamesOfAnnotatedObject(bioSource);
            psiOrganism.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.info("names failed (not required):" + e.getMessage());
        }
        try {
            Element psiCellType = procCvCelltype(bioSource.getCvCellType());
            psiOrganism.appendChild(psiCellType);
        } catch (ElementNotParseableException e) {
            logger.info("cvCellType failed (not required):" + e.getMessage());
        } catch (NullPointerException e) {
            logger.info("cvCellType failed (not required):" + e.getMessage());
        }
        try {
            Element psiCompartment = procCvCompartment(bioSource.getCvCompartment());
            psiOrganism.appendChild(psiCompartment);
        } catch (ElementNotParseableException e) {
            logger.info("cvCompartment failed (not required):" + e.getMessage());
        } catch (NullPointerException e) {
            logger.info("cvCompartment failed (not required):" + e.getMessage());
        }
        try {
            Element psiTissue = procCvTissue(bioSource.getCvTissue());
            psiOrganism.appendChild(psiTissue);
        } catch (ElementNotParseableException e) {
            logger.info("cvTissue failed (not required):" + e.getMessage());
        } catch (NullPointerException e) {
            logger.info(" failed (not required):" + e.getMessage());
        }
        try {
            psiOrganism.setAttribute("ncbiTaxId", bioSource.getTaxId());
        } catch (NullPointerException e) {
            logger.warn("ncbiTaxID failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("organism without Taxid found");
            }
        }
        if (!psiOrganism.hasChildNodes()) {
            logger.warn("bioSource failed, no child elements.");
            throw new ElementNotParseableException("Organism has no Child Elements");
        }
        return psiOrganism;
    }

    /**
     * process bioSource
     * @param bioSource to convert to PSI-Format
     * @return DOM-Object, representing a <organism>
     * @exception  uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.Xref
     */
    private Element procBioSource(BioSource bioSource) throws ElementNotParseableException {
        Element psiOrganism = doc.createElement("organism");
        Element psiNames = null;
        try {
            psiNames = getPsiNamesOfAnnotatedObject(bioSource);
            psiOrganism.appendChild(psiNames);
        } catch (ElementNotParseableException e) {
            logger.info("names failed (not required):" + e.getMessage());
        }
        try {
            Element psiCellType = procCvCelltype(bioSource.getCvCellType());
            psiOrganism.appendChild(psiCellType);
        } catch (ElementNotParseableException e) {
            logger.info("cvCelltype failed (not required):" + e.getMessage());
        } catch (NullPointerException e) {
            logger.info("cvCelltype failed (not required):" + e.getMessage());
        }
        try {
            Element psiCompartment = procCvCompartment(bioSource.getCvCompartment());
            psiOrganism.appendChild(psiCompartment);
        } catch (ElementNotParseableException e) {
            logger.info("cvCompartment failed (not required):" + e.getMessage());
        } catch (NullPointerException e) {
            logger.info("cvCompartment failed (not required):" + e.getMessage());
        }
        try {
            Element psiTissue = procCvTissue(bioSource.getCvTissue());
            psiOrganism.appendChild(psiTissue);
        } catch (ElementNotParseableException e) {
            logger.info("cvTissue failed (not required):" + e.getMessage());
        } catch (NullPointerException e) {
            logger.info("cvTissue failed (not required):" + e.getMessage());
        }
        try {
            psiOrganism.setAttribute("ncbiTaxId", bioSource.getTaxId());
        } catch (NullPointerException e) {
            logger.warn("ncbiTaxID failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("organism without Taxid found");
            }
        }
        if (!psiOrganism.hasChildNodes()) {
            logger.warn("organism failed, no child elements.");
            throw new ElementNotParseableException("Organism has no Child Elements");
        }
        return psiOrganism;
    }

    /**
     * process cvCellType
     * @param cvCellType to convert to PSI-Format
     * @return DOM-Object, representing a <cellType>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.CvCellType
     */
    private Element procCvCelltype(CvCellType cvCellType) throws ElementNotParseableException {
        Element psiCellType = doc.createElement("cellType");
        try {
            psiCellType.appendChild(doc.createTextNode(cvCellType.getShortLabel()));
        } catch (NullPointerException e) {
            logger.warn("cvCelltype failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("cellType not defined");
            }
        }
        if (!psiCellType.hasChildNodes()) {
            logger.warn("cvCellType failed, no child elements.");
            throw new ElementNotParseableException("CellType has no Child Elements");
        }
        return psiCellType;
    }

    /**
     * process cvCompartment
     * @param cvCompartment to convert to PSI-Format
     * @return DOM-Object, representing a <compartment>
     * @exception  uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.CvCompartment
     */
    private Element procCvCompartment(CvCompartment cvCompartment) throws ElementNotParseableException {
        Element psiCompartment = doc.createElement("compartment");
        try {
            psiCompartment.appendChild(doc.createTextNode(cvCompartment.getShortLabel()));
        } catch (NullPointerException e) {
            logger.warn("cvCompartment failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("Tissue not defined");
            }
        }
        if (!psiCompartment.hasChildNodes()) {
            logger.warn("cvCompartment failed, no child elements.");
            throw new ElementNotParseableException("Compartment has no Child Elements");
        }
        return psiCompartment;
    }

    /**
     * process cvTissue
     * @param cvTissue to convert to PSI-Format
     * @return DOM-Object, representing a <tissue>
     * @exception  uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.CvTissue
     */
    private Element procCvTissue(CvTissue cvTissue) throws ElementNotParseableException {
        Element psiTissue = doc.createElement("tissue");
        try {
            psiTissue.appendChild(doc.createTextNode(cvTissue.getShortLabel()));
        } catch (NullPointerException e) {
            logger.warn("cvTissue failed (required):" + e.getMessage());
            if (STRICT_MIF) {
                throw new ElementNotParseableException("Tissue not defined");
            }
        }
        if (!psiTissue.hasChildNodes()) {
            logger.warn("cvTissue failed, no child elements.");
            throw new ElementNotParseableException("Tissue has no Child Elements");
        }
        return psiTissue;
    }

    /**
     * This method gets an BasicGraph Object (like node,edge ...) and will return a Element names,
     * while getting shortLabel as getID() and fullName as getLabel()
     * @param basicgraph - Object  to convert to PSI-Format
     * @return DOM-Object, representing a <names>
     * @exception  uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.BasicObject
     */
    private Element getPsiNamesOfBasicGraph(BasicGraph basicgraph) throws ElementNotParseableException {
        Element psiNames = doc.createElement("names");
        try {
            if (basicgraph.getLabel() != null) {
                Element psiShortLabel = doc.createElement("shortLabel");
                psiShortLabel.appendChild(doc.createTextNode(basicgraph.getLabel()));
                psiNames.appendChild(psiShortLabel);
            } else {
                logger.warn("names failed (required): no shortLabel");
                if (STRICT_MIF) {
                    throw new ElementNotParseableException("No shortlabel for Name found");
                }
            }
        } catch (NullPointerException e) {
            if (STRICT_MIF) {
                logger.warn("names failed (required): no Shortlabel");
                throw new ElementNotParseableException("No shortlabel for Name found");
            }
        }
        if (!psiNames.hasChildNodes()) {
            logger.warn("names failed, no child elements.");
            throw new ElementNotParseableException("Names has no Child Elements");
        }
        return psiNames;
    }

    /**
     * This method gets an AnnotatedObject and will return a Element names,
     * while getting shortLabel as getShortLabel() and fullName as getFullName()
     * @param annotatedObject - Object  to convert to PSI-Format
     * @return DOM-Object, representing a <names>
     * @exception uk.ac.ebi.intact.application.graph2MIF.exception.ElementNotParseableException if it is not defined
     * @see uk.ac.ebi.intact.model.AnnotatedObject
     */
    private Element getPsiNamesOfAnnotatedObject(AnnotatedObject annotatedObject) throws ElementNotParseableException {
        Element psiNames = doc.createElement("names");
        try {
            if (annotatedObject.getShortLabel() != null) {
                Element psiShortLabel = doc.createElement("shortLabel");
                psiShortLabel.appendChild(doc.createTextNode(annotatedObject.getShortLabel()));
                psiNames.appendChild(psiShortLabel);
            } else {
                logger.warn("names failed (required): no shortLabel");
                if (STRICT_MIF) {
                    throw new ElementNotParseableException("No shortlabel for Name found");
                }
            }
        } catch (NullPointerException e) {
            logger.warn("names failed (required): no shortLabel");
            if (STRICT_MIF) {
                throw new ElementNotParseableException("No shortlabel for Name found");
            }
        }
        try {
            if (annotatedObject.getFullName() != null) {
                Element psiFullName = doc.createElement("fullName");
                psiFullName.appendChild(doc.createTextNode(annotatedObject.getFullName()));
                psiNames.appendChild(psiFullName);
            }
        } catch (NullPointerException e) {
        }
        if (!psiNames.hasChildNodes()) {
            logger.warn("names failed, no child elements.");
            throw new ElementNotParseableException("Names has no Child Elements");
        }
        return psiNames;
    }
}
