package net.sourceforge.ondex.parser.psimi2;

import java.io.File;
import java.util.Vector;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import net.sourceforge.ondex.parser.psimi2.datastructures.Attribute;
import net.sourceforge.ondex.parser.psimi2.datastructures.Availability;
import net.sourceforge.ondex.parser.psimi2.datastructures.BaseLocation;
import net.sourceforge.ondex.parser.psimi2.datastructures.BibRef;
import net.sourceforge.ondex.parser.psimi2.datastructures.BioSource;
import net.sourceforge.ondex.parser.psimi2.datastructures.Confidence;
import net.sourceforge.ondex.parser.psimi2.datastructures.Experiment;
import net.sourceforge.ondex.parser.psimi2.datastructures.ExperimentalInteractor;
import net.sourceforge.ondex.parser.psimi2.datastructures.Feature;
import net.sourceforge.ondex.parser.psimi2.datastructures.InferredInteraction;
import net.sourceforge.ondex.parser.psimi2.datastructures.Interaction;
import net.sourceforge.ondex.parser.psimi2.datastructures.Interactor;
import net.sourceforge.ondex.parser.psimi2.datastructures.MethodReference;
import net.sourceforge.ondex.parser.psimi2.datastructures.Names;
import net.sourceforge.ondex.parser.psimi2.datastructures.OpenCV;
import net.sourceforge.ondex.parser.psimi2.datastructures.Parameter;
import net.sourceforge.ondex.parser.psimi2.datastructures.Participant;
import net.sourceforge.ondex.parser.psimi2.datastructures.Ref;
import net.sourceforge.ondex.parser.psimi2.datastructures.Source;
import net.sourceforge.ondex.parser.psimi2.datastructures.XRef;
import org.codehaus.stax2.XMLInputFactory2;

public class Reader extends WorkerThread {

    private Pipe pipe;

    private XMLStreamReader r;

    private boolean more = true;

    private File file;

    Reader(Pipe p, File file) {
        super("PSIMI Reader");
        this.file = file;
        pipe = p;
    }

    public void failableRun() throws Exception {
        parse();
    }

    void closeFileHandle() throws Exception {
        r.close();
    }

    void parse() {
        XMLInputFactory2 factory = (XMLInputFactory2) XMLInputFactory2.newInstance();
        try {
            r = factory.createXMLStreamReader(file);
            for (int event = r.next(); more; event = r.next()) {
                switch(event) {
                    case XMLStreamConstants.END_DOCUMENT:
                        r.close();
                        more = false;
                        break;
                    case XMLStreamConstants.START_ELEMENT:
                        String elementName = r.getLocalName();
                        if (elementName.equals("entry")) {
                            pipe.reset();
                            parseEntry();
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        if (r.getLocalName().equals("entrySet")) {
                            r.close();
                            more = false;
                            pipe.signalTermination();
                            break;
                        }
                }
            }
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }
    }

    private void parseEntry() throws XMLStreamException {
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("source")) parseSource(); else if (elementName.equals("availabilityList")) parseAvailabilityList(); else if (elementName.equals("experimentList")) parseExperimentList(); else if (elementName.equals("interactorList")) parseInteractorList(); else if (elementName.equals("interactionList")) parseInteractionList();
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("entry")) return;
            }
        }
    }

    private void parseSource() throws XMLStreamException {
        Source source = new Source();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("names")) {
                        Names names = parseNames();
                        source.names = names;
                    }
                    if (elementName.equals("bibref")) {
                        BibRef bibref = parseBibref();
                        source.bibref = bibref;
                    }
                    if (elementName.equals("xref")) {
                        XRef xref = parseXRef();
                        source.xref = xref;
                    }
                    if (elementName.equals("attributeList")) {
                        Vector<Attribute> al = parseAttributeList();
                        source.attributeList = al;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("source")) {
                        pipe.setSource(source);
                        return;
                    }
            }
        }
    }

    private void parseAvailabilityList() throws XMLStreamException {
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("availability")) {
                        Availability a = new Availability();
                        String id = r.getAttributeValue(0);
                        try {
                            a.id = Integer.parseInt(id);
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid availability id: " + id);
                        }
                        String text = parseText();
                        a.value = text;
                        pipe.offerAvailability(a);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("availabilityList")) {
                        pipe.closeAvailabilities();
                        return;
                    }
            }
        }
    }

    private void parseExperimentList() throws XMLStreamException {
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("experimentDescription")) {
                        parseExperimentDescription();
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("experimentList")) {
                        pipe.closeExperiments();
                        return;
                    }
            }
        }
    }

    private void parseExperimentDescription() throws XMLStreamException {
        Experiment exp = parseExperimentDescriptionInternal();
        pipe.offerExperiment(exp);
    }

    private Experiment parseExperimentDescriptionInternal() throws XMLStreamException {
        Experiment exp = new Experiment();
        String strId = r.getAttributeValue(0);
        try {
            exp.id = Integer.parseInt(strId);
        } catch (NumberFormatException nfe) {
            throw new XMLStreamException("invalid experiment id");
        }
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("names")) {
                        Names names = parseNames();
                        exp.descriptionNames = names;
                    } else if (elementName.equals("bibref")) {
                        BibRef bibref = parseBibref();
                        exp.descriptionBibref = bibref;
                    } else if (elementName.equals("xref")) {
                        XRef xref = parseXRef();
                        exp.descriptionXRef = xref;
                    } else if (elementName.equals("hostOrganismList")) {
                        Vector<BioSource> hol = parseHostOrganismList();
                        exp.hostOrganismList = hol;
                    } else if (elementName.equals("interactionDetectionMethod")) {
                        Names names = parseNames();
                        XRef xref = parseXRef();
                        exp.interactionDetectionName = names;
                        exp.interactionDetectionXRef = xref;
                    } else if (elementName.equals("participantDetectionMethod")) {
                        Names names = parseNames();
                        XRef xref = parseXRef();
                        exp.participantDetectionName = names;
                        exp.participantDetectionXRef = xref;
                    } else if (elementName.equals("featureDetectionMethod")) {
                        Names names = parseNames();
                        XRef xref = parseXRef();
                        exp.featureDetectionName = names;
                        exp.featureDetectionXRef = xref;
                    } else if (elementName.equals("confidenceList")) {
                        Vector<Confidence> col = parseConfidenceList();
                        exp.confidenceList = col;
                    } else if (elementName.equals("attributeList")) {
                        Vector<Attribute> al = parseAttributeList();
                        exp.attributeList = al;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("experimentDescription")) {
                        return exp;
                    }
            }
        }
        throw new XMLStreamException("no experimentDescription end tag.");
    }

    private Vector<BioSource> parseHostOrganismList() throws XMLStreamException {
        Vector<BioSource> bs = new Vector<BioSource>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("hostOrganism")) {
                        bs.add(parseBioSource("hostOrganism"));
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("hostOrganismList")) {
                        return bs;
                    }
            }
        }
        throw new XMLStreamException("no hostOrganismList end tag.");
    }

    private void parseInteractorList() throws XMLStreamException {
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("interactor")) parseInteractor();
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("interactorList")) {
                        pipe.closeInteractors();
                        return;
                    }
            }
        }
    }

    private void parseInteractor() throws XMLStreamException {
        Interactor ir = parseInteractorInternal();
        pipe.offerInteractor(ir);
    }

    private Interactor parseInteractorInternal() throws XMLStreamException {
        Interactor ir = new Interactor();
        String strId = r.getAttributeValue(0);
        try {
            int id = Integer.parseInt(strId);
            ir.id = id;
        } catch (NumberFormatException nfe) {
            throw new XMLStreamException("non integer interactor id");
        }
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("names")) {
                        Names names = parseNames();
                        ir.names = names;
                    } else if (elementName.equals("xref")) {
                        XRef xref = parseXRef();
                        ir.xref = xref;
                    } else if (elementName.equals("interactorType")) {
                        Names names = parseNames();
                        XRef xref = parseXRef();
                        ir.interactorTypeNames = names;
                        ir.interactorTypeXRef = xref;
                    } else if (elementName.equals("organism")) {
                        BioSource bs = parseBioSource("organism");
                        ir.organism = bs;
                    } else if (elementName.equals("sequence")) {
                        ir.sequence = parseText();
                    } else if (elementName.equals("attributeList")) {
                        ir.attributeList = parseAttributeList();
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().endsWith("Interactor")) {
                        return ir;
                    }
            }
        }
        throw new XMLStreamException("missing interactor end tag.");
    }

    private void parseInteractionList() throws XMLStreamException {
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("interaction")) parseInteraction();
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("interactionList")) {
                        pipe.closeInteractions();
                        return;
                    }
            }
        }
    }

    private void parseInteraction() throws XMLStreamException {
        Interaction in = new Interaction();
        for (int i = 0; i < r.getAttributeCount(); i++) {
            String aname = r.getAttributeLocalName(i);
            if (aname.equals("imexId")) {
                in.imexId = r.getAttributeValue(i);
            } else if (aname.equals("id")) {
                String strId = r.getAttributeValue(i);
                try {
                    int id = Integer.parseInt(strId);
                    in.id = id;
                } catch (NumberFormatException nfe) {
                    throw new XMLStreamException("non integer interactor id");
                }
            }
        }
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("names")) {
                        in.names = parseNames();
                    } else if (elementName.equals("xref")) {
                        in.xref = parseXRef();
                    } else if (elementName.equals("experimentList")) {
                        Vector<Object> expL = parseExperimentRefList("experimentList");
                        in.experimentList = expL;
                    } else if (elementName.equals("participantList")) {
                        Vector<Participant> parL = parseParticipantList();
                        in.participantList = parL;
                    } else if (elementName.equals("inferredInteractionList")) {
                        Vector<InferredInteraction> iiL = parseInferredInteractionList();
                        in.inferredInteractionList = iiL;
                    } else if (elementName.equals("interactionType")) {
                        in.interactionTypeNames = parseNames();
                        in.interactionTypeXRef = parseXRef();
                    } else if (elementName.equals("modelled")) {
                        in.modelled = Boolean.parseBoolean(parseText());
                    } else if (elementName.equals("intraMolecular")) {
                        in.intraMolecular = Boolean.parseBoolean(parseText());
                    } else if (elementName.equals("negative")) {
                        in.negative = Boolean.parseBoolean(parseText());
                    } else if (elementName.equals("confidenceList")) {
                        in.confidenceList = parseConfidenceList();
                    } else if (elementName.equals("parameterList")) {
                        in.parameterList = parseParameterList();
                    } else if (elementName.equals("attributeList")) {
                        in.attributeList = parseAttributeList();
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("interaction")) {
                        pipe.offerInteraction(in);
                        return;
                    }
            }
        }
    }

    private Vector<Object> parseExperimentRefList(String name) throws XMLStreamException {
        Vector<Object> vec = new Vector<Object>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("experimentRef")) {
                        String strId = parseText();
                        try {
                            int id = Integer.parseInt(strId);
                            vec.add(new Integer(id));
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid experiment reference: " + strId);
                        }
                    } else if (elementName.equals("experimentDescription")) {
                        Experiment exp = parseExperimentDescriptionInternal();
                        vec.add(exp);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals(name)) {
                        return vec;
                    }
            }
        }
        throw new XMLStreamException("no experimentList end tag");
    }

    private Vector<Participant> parseParticipantList() throws XMLStreamException {
        Vector<Participant> vec = new Vector<Participant>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("participant")) vec.add(parseParticipant());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("participantList")) {
                        return vec;
                    }
            }
        }
        return vec;
    }

    private Participant parseParticipant() throws XMLStreamException {
        Participant p = new Participant();
        String strId = r.getAttributeValue(0);
        try {
            int id = Integer.parseInt(strId);
            p.id = id;
        } catch (NumberFormatException nfe) {
            throw new XMLStreamException("invalid participant id: " + strId);
        }
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("names")) {
                        p.names = parseNames();
                    } else if (elementName.equals("xref")) {
                        p.xref = parseXRef();
                    } else if (elementName.equals("interactorRef")) {
                        p.interactorRefUsed = true;
                        strId = parseText();
                        try {
                            int id = Integer.parseInt(strId);
                            p.elementRef = id;
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid participant id: " + strId);
                        }
                    } else if (elementName.equals("interactionRef")) {
                        p.interactorRefUsed = false;
                        strId = parseText();
                        try {
                            int id = Integer.parseInt(strId);
                            p.elementRef = id;
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid participant id: " + strId);
                        }
                    } else if (elementName.equals("interactor")) {
                        Interactor interactor = parseInteractorInternal();
                        p.elementRef = interactor;
                    } else if (elementName.equals("participantIdentificationMethodList")) {
                        Vector<MethodReference> mrs = parseMethodReferenceList("participantIdentificationMethod");
                        p.participantIdentificationMethodList = mrs;
                    } else if (elementName.equals("biologicalRole")) {
                        Names names = parseNames();
                        XRef xref = parseXRef();
                        p.biologicalRoleNames = names;
                        p.biologicalRoleXRef = xref;
                    } else if (elementName.equals("experimentalRoleList")) {
                        Vector<MethodReference> ers = parseMethodReferenceList("experimentalRole");
                        p.experimentalRoleList = ers;
                    } else if (elementName.equals("experimentalPreparationList")) {
                        Vector<MethodReference> eps = parseMethodReferenceList("experimentalPreparation");
                        p.experimentalRoleList = eps;
                    } else if (elementName.equals("experimentalInteractorList")) {
                        Vector<ExperimentalInteractor> eis = parseExperimentalInteractorList();
                        p.experimentalInteractorList = eis;
                    } else if (elementName.equals("featureList")) {
                        Vector<Feature> fs = parseFeatureList();
                        p.featureList = fs;
                    } else if (elementName.equals("hostOrganismList")) {
                        Vector<BioSource> hos = parseHostOrganismList();
                        p.hostOrganismList = hos;
                    } else if (elementName.equals("confidenceList")) {
                        Vector<Confidence> cs = parseConfidenceList();
                        p.confidenceList = cs;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("participant")) {
                        return p;
                    }
            }
        }
        return p;
    }

    private Vector<MethodReference> parseMethodReferenceList(String name) throws XMLStreamException {
        Vector<MethodReference> vec = new Vector<MethodReference>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals(name)) {
                        MethodReference mr = parseMethodReference(name);
                        vec.add(mr);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals(name + "List")) {
                        return vec;
                    }
            }
        }
        throw new XMLStreamException("no " + name + "List end tag.");
    }

    private MethodReference parseMethodReference(String name) throws XMLStreamException {
        MethodReference mr = new MethodReference();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("experimentRefList")) {
                        Vector<Object> refs = parseExperimentRefList("experimentRefList");
                        Vector<Integer> intrefs = new Vector<Integer>();
                        for (Object ref : refs) intrefs.add((Integer) ref);
                        mr.experimentRefList = intrefs;
                    } else if (elementName.equals("names")) {
                        Names names = parseNames();
                        mr.names = names;
                    } else if (elementName.equals("xref")) {
                        XRef xref = parseXRef();
                        mr.xref = xref;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals(name)) {
                        return mr;
                    }
            }
        }
        throw new XMLStreamException("no " + name + "List end tag.");
    }

    private Vector<ExperimentalInteractor> parseExperimentalInteractorList() throws XMLStreamException {
        Vector<ExperimentalInteractor> vec = new Vector<ExperimentalInteractor>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("experimentalInteractor")) {
                        ExperimentalInteractor ei = parseExperimentalInteractor();
                        vec.add(ei);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("experimentalInteractorList")) {
                        return vec;
                    }
            }
        }
        throw new XMLStreamException("no experimentalInteractorList end tag.");
    }

    private ExperimentalInteractor parseExperimentalInteractor() throws XMLStreamException {
        ExperimentalInteractor ei = new ExperimentalInteractor();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("interactorRef")) {
                        String strId = parseText();
                        try {
                            int id = Integer.parseInt(strId);
                            ei.interactor = new Integer(id);
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid interactor id: " + strId);
                        }
                    } else if (elementName.equals("interactor")) {
                        Interactor interactor = parseInteractorInternal();
                        ei.interactor = interactor;
                    } else if (elementName.equals("experimentalRefList")) {
                        Vector<Object> refs = parseExperimentRefList("experimentRefList");
                        Vector<Integer> intrefs = new Vector<Integer>();
                        for (Object ref : refs) intrefs.add((Integer) ref);
                        ei.experimentRefList = intrefs;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("experimentalInteractor")) {
                        return ei;
                    }
            }
        }
        throw new XMLStreamException("no experimentalInteractorList end tag.");
    }

    private Vector<Feature> parseFeatureList() throws XMLStreamException {
        Vector<Feature> vec = new Vector<Feature>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("feature")) {
                        Feature f = parseFeature();
                        vec.add(f);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("featureList")) {
                        return vec;
                    }
            }
        }
        throw new XMLStreamException("no featureList end tag.");
    }

    private Feature parseFeature() throws XMLStreamException {
        Feature f = new Feature();
        String strId = r.getAttributeValue(0);
        try {
            int id = Integer.parseInt(strId);
            f.id = id;
        } catch (NumberFormatException nfe) {
            throw new XMLStreamException("invalid feature id: " + strId);
        }
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("names")) {
                        f.names = parseNames();
                    } else if (elementName.equals("xref")) {
                        f.xref = parseXRef();
                    } else if (elementName.equals("featureType")) {
                        f.featureTypeNames = parseNames();
                        f.featureTypeXRef = parseXRef();
                    } else if (elementName.equals("featureDetectionMethod")) {
                        f.featureDetectionMethodNames = parseNames();
                        f.featureDetectionMethodXRef = parseXRef();
                    } else if (elementName.equals("experimentRefList")) {
                        Vector<Object> refs = parseExperimentRefList("experimentRefList");
                        Vector<Integer> intrefs = new Vector<Integer>();
                        for (Object ref : refs) intrefs.add((Integer) ref);
                        f.experimentRefList = intrefs;
                    } else if (elementName.equals("featureRangeList")) {
                        Vector<BaseLocation> frl = parseFeatureRangeList();
                        f.featureRangeList = frl;
                    } else if (elementName.equals("attributeList")) {
                        f.attributeList = parseAttributeList();
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("feature")) {
                        return f;
                    }
            }
        }
        throw new XMLStreamException("no featureList end tag.");
    }

    private Vector<BaseLocation> parseFeatureRangeList() throws XMLStreamException {
        Vector<BaseLocation> vec = new Vector<BaseLocation>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("featureRange")) {
                        BaseLocation f = parseFeatureRange();
                        vec.add(f);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("featureRangeList")) {
                        return vec;
                    }
            }
        }
        throw new XMLStreamException("no featureRangeList end tag.");
    }

    private BaseLocation parseFeatureRange() throws XMLStreamException {
        BaseLocation bl = new BaseLocation();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("startStatus")) {
                        bl.startStatusNames = parseNames();
                        bl.startStatusXRef = parseXRef();
                    } else if (elementName.equals("begin")) {
                        String strId = r.getAttributeValue(0);
                        try {
                            int id = Integer.parseInt(strId);
                            bl.startBeginOrPosition = id;
                            bl.startEnd = -1L;
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid feature position: " + strId);
                        }
                    } else if (elementName.equals("beginInterval")) {
                        String strId = r.getAttributeValue(0);
                        try {
                            int id = Integer.parseInt(strId);
                            bl.startBeginOrPosition = id;
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid feature position: " + strId);
                        }
                        strId = r.getAttributeValue(1);
                        try {
                            int id = Integer.parseInt(strId);
                            bl.startEnd = id;
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid feature position: " + strId);
                        }
                    } else if (elementName.equals("endStatus")) {
                        bl.endStatusNames = parseNames();
                        bl.endStatusXRef = parseXRef();
                    } else if (elementName.equals("end")) {
                        String strId = r.getAttributeValue(0);
                        try {
                            int id = Integer.parseInt(strId);
                            bl.endBeginOrPosition = id;
                            bl.endEnd = -1L;
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid feature position: " + strId);
                        }
                    } else if (elementName.equals("endInterval")) {
                        String strId = r.getAttributeValue(0);
                        try {
                            int id = Integer.parseInt(strId);
                            bl.endBeginOrPosition = id;
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid feature position: " + strId);
                        }
                        strId = r.getAttributeValue(1);
                        try {
                            int id = Integer.parseInt(strId);
                            bl.endEnd = id;
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid feature position: " + strId);
                        }
                    } else if (elementName.equals("isLink")) {
                        String strId = parseText();
                        boolean id = Boolean.parseBoolean(strId);
                        bl.isLink = id;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("featureRange")) {
                        return bl;
                    }
            }
        }
        throw new XMLStreamException("no featureRange end tag.");
    }

    private Vector<InferredInteraction> parseInferredInteractionList() throws XMLStreamException {
        Vector<InferredInteraction> vec = new Vector<InferredInteraction>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("featureRange")) {
                        InferredInteraction f = parseInferredInteraction();
                        vec.add(f);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("inferredInteractionList")) {
                        return vec;
                    }
            }
        }
        throw new XMLStreamException("no featureRangeList end tag.");
    }

    private InferredInteraction parseInferredInteraction() throws XMLStreamException {
        InferredInteraction ii = new InferredInteraction();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("participant")) {
                        for (int event2 = r.next(); more; event2 = r.next()) {
                            switch(event2) {
                                case XMLStreamConstants.START_ELEMENT:
                                    String elementName2 = r.getLocalName();
                                    if (elementName2.equals("participantRef")) {
                                        ii.participantUsed = true;
                                        String strId = parseText();
                                        try {
                                            int id = Integer.parseInt(strId);
                                            ii.participantOrFeature = id;
                                        } catch (NumberFormatException nfe) {
                                            throw new XMLStreamException("invalid participant reference: " + strId);
                                        }
                                    } else if (elementName2.equals("participantFeatureRef")) {
                                        ii.participantUsed = false;
                                        String strId = parseText();
                                        try {
                                            int id = Integer.parseInt(strId);
                                            ii.participantOrFeature = id;
                                        } catch (NumberFormatException nfe) {
                                            throw new XMLStreamException("invalid participant reference: " + strId);
                                        }
                                    }
                                    break;
                                case XMLStreamConstants.END_ELEMENT:
                                    if (r.getLocalName().equals("participant")) {
                                        return ii;
                                    }
                            }
                        }
                    } else if (elementName.equals("experimentRefList")) {
                        Vector<Object> refs = parseExperimentRefList("experimentRefList");
                        Vector<Integer> intrefs = new Vector<Integer>();
                        for (Object ref : refs) intrefs.add((Integer) ref);
                        ii.experimentRefList = intrefs;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("inferredInteraction")) {
                        return ii;
                    }
            }
        }
        throw new XMLStreamException("no featureRange end tag.");
    }

    private Vector<Parameter> parseParameterList() throws XMLStreamException {
        Vector<Parameter> vec = new Vector<Parameter>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("parameter")) {
                        Parameter p = parseParameter();
                        vec.add(p);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("parameterList")) {
                        return vec;
                    }
            }
        }
        throw new XMLStreamException("no featureRangeList end tag.");
    }

    private Parameter parseParameter() throws XMLStreamException {
        Parameter p = new Parameter();
        for (int i = 0; i < r.getAttributeCount(); i++) {
            String aname = r.getAttributeLocalName(i);
            if (aname.equals("term")) {
                p.term = r.getAttributeValue(i);
            } else if (aname.equals("termAc")) {
                p.termAc = r.getAttributeValue(i);
            } else if (aname.equals("unit")) {
                p.unit = r.getAttributeValue(i);
            } else if (aname.equals("unitAc")) {
                p.unitAc = r.getAttributeValue(i);
            } else if (aname.equals("base")) {
                String val = r.getAttributeValue(i);
                try {
                    p.base = Integer.parseInt(val);
                } catch (NumberFormatException nfe) {
                    throw new XMLStreamException("invalid base: " + val);
                }
            } else if (aname.equals("exponent")) {
                String val = r.getAttributeValue(i);
                try {
                    p.exponent = Integer.parseInt(val);
                } catch (NumberFormatException nfe) {
                    throw new XMLStreamException("invalid exponent: " + val);
                }
            } else if (aname.equals("factor")) {
                String val = r.getAttributeValue(i);
                try {
                    p.factor = Integer.parseInt(val);
                } catch (NumberFormatException nfe) {
                    throw new XMLStreamException("invalid factor: " + val);
                }
            }
        }
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("experimentRef")) {
                        String strId = parseText();
                        try {
                            int id = Integer.parseInt(strId);
                            p.experimentRef = id;
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("invalid participant reference: " + strId);
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("parameter")) {
                        return p;
                    }
            }
        }
        throw new XMLStreamException("no featureRange end tag.");
    }

    private Names parseNames() throws XMLStreamException {
        Names names = new Names();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("shortLabel")) {
                        names.label = parseText();
                    } else if (elementName.equals("fullName")) {
                        names.fullName = parseText();
                    } else if (elementName.equals("alias")) {
                        names.aliases.add(parseText());
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("names")) return names;
            }
        }
        throw new XMLStreamException("missing names end tag");
    }

    private BibRef parseBibref() throws XMLStreamException {
        BibRef b = new BibRef();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("xref")) {
                        XRef xref = parseXRef();
                        b.setXRef(xref);
                    } else if (elementName.equals("attributeList")) {
                        Vector<Attribute> atts = parseAttributeList();
                        b.setAttributeList(atts);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("bibref")) return b;
            }
        }
        throw new XMLStreamException("missing bibref end tag");
    }

    private XRef parseXRef() throws XMLStreamException {
        XRef xref = new XRef();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    Ref ref;
                    if (elementName.equals("primaryRef")) {
                        ref = parseRef();
                        xref.primaryRef = ref;
                    } else if (elementName.equals("secondaryRef")) {
                        ref = parseRef();
                        xref.secondaryRefs.add(ref);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("xref")) return xref;
            }
        }
        throw new XMLStreamException("missing xref end tag");
    }

    private Ref parseRef() throws XMLStreamException {
        Ref ref = new Ref();
        for (int i = 0; i < r.getAttributeCount(); i++) {
            String aname = r.getAttributeLocalName(i);
            if (aname.equals("db")) {
                ref.db = r.getAttributeValue(i);
            } else if (aname.equals("dbAc")) {
                ref.dbAc = r.getAttributeValue(i);
            } else if (aname.equals("id")) {
                ref.id = r.getAttributeValue(i);
            } else if (aname.equals("secondary")) {
                ref.secondary = r.getAttributeValue(i);
            } else if (aname.equals("version")) {
                ref.version = r.getAttributeValue(i);
            } else if (aname.equals("refType")) {
                ref.refType = r.getAttributeValue(i);
            } else if (aname.equals("refTypeAc")) {
                ref.refTypeAc = r.getAttributeValue(i);
            }
        }
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("attributeList")) {
                        Vector<Attribute> atts = parseAttributeList();
                        ref.attributeList = atts;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().endsWith("aryRef")) return ref;
            }
        }
        throw new XMLStreamException("missing reference end tag");
    }

    private Vector<Attribute> parseAttributeList() throws XMLStreamException {
        Vector<Attribute> atts = new Vector<Attribute>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("attribute")) {
                        Attribute att = new Attribute();
                        for (int i = 0; i < r.getAttributeCount(); i++) {
                            String aname = r.getAttributeLocalName(i);
                            if (aname.equals("name")) {
                                att.name = r.getAttributeValue(i);
                            } else if (aname.equals("nameAc")) {
                                att.nameAc = r.getAttributeValue(i);
                            }
                        }
                        att.value = parseText();
                        atts.add(att);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("attributeList")) return atts;
            }
        }
        throw new XMLStreamException("missing attributeList end tag");
    }

    private Vector<Confidence> parseConfidenceList() throws XMLStreamException {
        Vector<Confidence> cs = new Vector<Confidence>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("confidence")) {
                        Confidence c = parseConfidence();
                        cs.add(c);
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("confidenceList")) return cs;
            }
        }
        throw new XMLStreamException("missing attributeList end tag");
    }

    private Confidence parseConfidence() throws XMLStreamException {
        Confidence c = new Confidence();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("experiementRefList")) {
                        Vector<Integer> erefs = parseReferenceList("experimentRef");
                        c.experimentRefList = erefs;
                    } else if (elementName.equals("unit")) {
                        OpenCV unit = parseOpenCV();
                        c.unit = unit;
                    } else if (elementName.equals("value")) {
                        String value = parseText();
                        c.value = value;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("confidence")) return c;
            }
        }
        throw new XMLStreamException("missing attributeList end tag");
    }

    private Vector<Integer> parseReferenceList(String name) throws XMLStreamException {
        Vector<Integer> c = new Vector<Integer>();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals(name)) {
                        String reftext = parseText();
                        try {
                            int ref = Integer.parseInt(reftext);
                            c.add(ref);
                        } catch (NumberFormatException nfe) {
                            throw new XMLStreamException("Non-integer reference");
                        }
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals(name + "List")) return c;
            }
        }
        throw new XMLStreamException("missing attributeList end tag");
    }

    private OpenCV parseOpenCV() throws XMLStreamException {
        OpenCV c = new OpenCV();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("names")) {
                        c.names = parseNames();
                    } else if (elementName.equals("xref")) {
                        c.xref = parseXRef();
                    } else if (elementName.equals("attributeList")) {
                        c.attributeList = parseAttributeList();
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals("attributeList")) return c;
            }
        }
        throw new XMLStreamException("missing attributeList end tag");
    }

    private BioSource parseBioSource(String name) throws XMLStreamException {
        BioSource bs = new BioSource();
        int taxid = Integer.parseInt(r.getAttributeValue(0));
        bs.ncbiTaxId = taxid;
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.START_ELEMENT:
                    String elementName = r.getLocalName();
                    if (elementName.equals("names")) {
                        bs.names = parseNames();
                    } else if (elementName.equals("cellType")) {
                        bs.cellType = parseOpenCV();
                    } else if (elementName.equals("compartment")) {
                        bs.compartment = parseOpenCV();
                    } else if (elementName.equals("tissue")) {
                        bs.tissue = parseOpenCV();
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (r.getLocalName().equals(name)) {
                        return bs;
                    }
            }
        }
        throw new XMLStreamException("no hostOrganism end tag");
    }

    private String parseText() throws XMLStreamException {
        StringBuilder b = new StringBuilder();
        for (int event = r.next(); more; event = r.next()) {
            switch(event) {
                case XMLStreamConstants.END_DOCUMENT:
                    r.close();
                    more = false;
                    break;
                case XMLStreamConstants.CHARACTERS:
                    b.append(r.getText());
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    return b.toString();
            }
        }
        return b.toString();
    }
}
