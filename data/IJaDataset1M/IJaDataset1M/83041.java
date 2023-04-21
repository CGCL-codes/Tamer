package org.obe.xpdl.parser.dom4j;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.obe.XMLException;
import org.obe.xpdl.XPDLMessages;
import org.obe.xpdl.model.activity.*;
import org.obe.xpdl.model.application.Application;
import org.obe.xpdl.model.condition.Condition;
import org.obe.xpdl.model.condition.ConditionType;
import org.obe.xpdl.model.condition.Xpression;
import org.obe.xpdl.model.data.*;
import org.obe.xpdl.model.ext.*;
import org.obe.xpdl.model.ext.Event;
import org.obe.xpdl.model.misc.*;
import org.obe.xpdl.model.participant.Participant;
import org.obe.xpdl.model.participant.ParticipantType;
import org.obe.xpdl.model.pkg.ExternalPackage;
import org.obe.xpdl.model.pkg.PackageHeader;
import org.obe.xpdl.model.pkg.XPDLPackage;
import org.obe.xpdl.model.transition.*;
import org.obe.xpdl.model.workflow.ProcessHeader;
import org.obe.xpdl.model.workflow.WorkflowProcess;
import org.obe.xpdl.parser.ElementRequiredException;
import org.obe.xpdl.parser.XPDLParser;
import org.obe.xpdl.parser.XPDLParserException;
import org.xml.sax.EntityResolver;
import java.awt.*;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the XPDLParser interface, using DOM4J.
 *
 * @author Anthony Eden
 * @author Adrian Price
 */
public class Dom4JXPDLParser implements XPDLParser, DOM4JNames {

    private static Log _logger = LogFactory.getLog(Dom4JXPDLParser.class);

    private static final DurationUnit defaultDurationUnit = DurationUnit.MINUTE;

    private static final InstantiationType defaultInstantiationType = InstantiationType.ONCE;

    private static final int DEFAULT_WIDTH = 140;

    private static final int DEFAULT_HEIGHT = 50;

    private EntityResolver entityResolver;

    private static final String MSG_CREATED = "Created ";

    private static final String APPLICATIONS_LOADED = " applications loaded";

    private static final String IMPL_ROUTE_MUTEX = "Implementation and route element cannot be ";

    /**
     * Construct a new Dom4JXPDLParser.
     */
    public Dom4JXPDLParser() {
    }

    public Dom4JXPDLParser(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public XPDLPackage parse(InputStream in) throws IOException, XPDLParserException {
        return parse(new InputStreamReader(in));
    }

    /**
     * Parse the XPDL document which is provided by the given InputStream.
     *
     * @param in The XPDL character stream
     * @return The Workflow Package
     * @throws IOException         Any I/O Exception
     * @throws XPDLParserException Any parser exception
     */
    public XPDLPackage parse(Reader in) throws IOException, XPDLParserException {
        try {
            DocumentFactory docFactory = DocumentFactory.getInstance();
            SAXReader reader = new SAXReader(docFactory);
            if (entityResolver != null) {
                reader.setEntityResolver(entityResolver);
                reader.setValidation(true);
            }
            Document document = reader.read(in);
            Element packageElement = document.getRootElement();
            PackageHeader packageHeader = createPackageHeader(Util.child(packageElement, PACKAGE_HEADER));
            XPDLPackage pkg = new XPDLPackage(packageElement.attributeValue(ID), packageElement.attributeValue(NAME), packageHeader);
            Map pkgNamespaces = pkg.getNamespaces();
            List namespaces = packageElement.declaredNamespaces();
            for (int i = 0; i < namespaces.size(); i++) {
                Namespace ns = (Namespace) namespaces.get(i);
                String prefix = ns.getPrefix();
                if (prefix != null && prefix.length() != 0) pkgNamespaces.put(prefix, ns.getURI());
            }
            pkg.setRedefinableHeader(createRedefinableHeader(Util.child(packageElement, REDEFINABLE_HEADER)));
            pkg.setConformanceClass(createConformanceClass(Util.child(packageElement, CONFORMANCE_CLASS)));
            pkg.setScript(createScript(Util.child(packageElement, SCRIPT)));
            loadExternalPackages(pkg, Util.child(packageElement, EXTERNAL_PACKAGES));
            loadTypeDeclarations(pkg, Util.child(packageElement, TYPE_DECLARATIONS));
            loadParticipants(pkg, Util.child(packageElement, PARTICIPANTS));
            loadApplications(pkg, Util.child(packageElement, APPLICATIONS));
            loadDataFields(pkg, Util.child(packageElement, DATA_FIELDS));
            loadWorkflowProcesses(pkg, Util.child(packageElement, WORKFLOW_PROCESSES));
            Map extAttrs = new HashMap();
            pkg.setExtendedAttributes(loadExtendedAttributes(packageElement, extAttrs));
            pkg.setCalendar((String) extAttrs.get(OBE_CALENDAR));
            pkg.setAssignmentStrategy((AssignmentStrategyDef) extAttrs.get(OBE_ASSIGNMENT_STRATEGY));
            pkg.setCompletionStrategy((String) extAttrs.get(OBE_COMPLETION_STRATEGY));
            return pkg;
        } catch (DocumentException e) {
            throw new XPDLParserException(XPDLMessages.ERROR_PARSING_DOCUMENT, e);
        } catch (PropertyVetoException e) {
            throw new XPDLParserException(XPDLMessages.ERROR_PARSING_DOCUMENT, e);
        }
    }

    /**
     * Create the package header from the given element.  This method will throw
     * an XPDLParserException if the XPDLPackage header element is null.
     *
     * @param element The package header element
     * @throws XPDLParserException Thrown if the element is null
     */
    protected PackageHeader createPackageHeader(Element element) throws XPDLParserException {
        if (element == null) {
            throw new ElementRequiredException(PACKAGE_HEADER, "Package header required but not found");
        }
        PackageHeader packageHeader = new PackageHeader();
        packageHeader.setXPDLVersion(Util.elementAsString(element, XPDL_VERSION));
        packageHeader.setVendor(Util.elementAsString(element, VENDOR));
        packageHeader.setCreated(Util.elementAsDate(element, CREATED));
        packageHeader.setDescription(Util.elementAsString(element, DESCRIPTION));
        packageHeader.setDocumentation(Util.elementAsURL(element, DOCUMENTATION));
        packageHeader.setPriorityUnit(Util.elementAsString(element, PRIORITY_UNIT));
        packageHeader.setCostUnit(Util.elementAsString(element, COST_UNIT));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + packageHeader);
        return packageHeader;
    }

    protected RedefinableHeader createRedefinableHeader(Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) {
            _logger.debug("Redefinable header not found");
            return null;
        }
        RedefinableHeader redefinableHeader = new RedefinableHeader();
        redefinableHeader.setAuthor(Util.elementAsString(element, AUTHOR));
        redefinableHeader.setVersion(Util.elementAsString(element, VERSION));
        redefinableHeader.setCodepage(Util.elementAsString(element, CODEPAGE));
        redefinableHeader.setCountrykey(Util.elementAsString(element, COUNTRYKEY));
        String pubStatusString = element.attributeValue(PUBLICATION_STATUS);
        if (pubStatusString != null) {
            PublicationStatus publicationStatus = PublicationStatus.valueOf(pubStatusString);
            redefinableHeader.setPublicationStatus(publicationStatus);
        }
        loadResponsibles(redefinableHeader, Util.child(element, RESPONSIBLES));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + redefinableHeader);
        return redefinableHeader;
    }

    /**
     * Create a ConformanceClass object from the given element.
     *
     * @param element The ConformanceClass element
     * @throws XPDLParserException Any parser exception
     */
    protected ConformanceClass createConformanceClass(Element element) throws XPDLParserException {
        if (element == null) {
            _logger.debug("Conformance class header not found");
            return null;
        }
        ConformanceClass cc = new ConformanceClass();
        GraphConformance gc = GraphConformance.valueOf(element.attributeValue(GRAPH_CONFORMANCE));
        cc.setGraphConformance(gc);
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + cc);
        return cc;
    }

    /**
     * Create a Script object from the given Element.
     *
     * @param element The Element
     * @return the Script object
     * @throws XPDLParserException
     */
    protected Script createScript(Element element) throws XPDLParserException {
        if (element == null) {
            _logger.debug("Script not found");
            return null;
        }
        Script script = new Script(element.attributeValue(TYPE));
        script.setVersion(element.attributeValue(VERSION));
        script.setGrammar(element.attributeValue(GRAMMAR));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + script);
        return script;
    }

    protected void loadExternalPackages(XPDLPackage pkg, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadExternalPackages(pkg, Util.children(element, EXTERNAL_PACKAGE));
    }

    protected void loadExternalPackages(XPDLPackage pkg, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            try {
                Element extPkgElem = (Element) iter.next();
                createExternalPackage(pkg, extPkgElem);
            } catch (XPDLParserException e) {
                _logger.error("Error loading external package", e);
            }
        }
        if (pkg.getExternalPackage().length == 0) {
            _logger.debug("External packages element present but no external packages found");
        }
    }

    protected void createExternalPackage(XPDLPackage pkg, Element element) throws XPDLParserException, PropertyVetoException {
        String href = element.attributeValue(HREF);
        _logger.debug("Loading external package from " + href);
        XPDLPackage p;
        try {
            p = parse(new URL(href).openStream());
            _logger.debug("Loaded external package " + p.getId());
        } catch (IOException e) {
            throw new XPDLParserException("Error loading external package " + href, e);
        }
        ExternalPackage extPkg = new ExternalPackage(href, p);
        extPkg.setExtendedAttributes(loadExtendedAttributes(null, element));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + extPkg);
        pkg.add(extPkg);
    }

    protected void loadResponsibles(RedefinableHeader header, Element element) throws PropertyVetoException {
        if (element == null) return;
        loadResponsibles(header, Util.children(element, RESPONSIBLE));
    }

    protected void loadResponsibles(RedefinableHeader header, List elements) throws PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element responsibleElement = (Element) iter.next();
            header.add(responsibleElement.getTextTrim());
        }
        if (header.getResponsible().length == 0) {
            _logger.debug("Responsibles element present but no responsibles found");
        }
    }

    /**
     * Load participants from the given Element.  The element should represent a
     * collection of participants (which is identified by the "Participants"
     * tag).
     *
     * @param resCntr
     * @param element The "Participants" element
     * @throws XPDLParserException
     */
    protected void loadParticipants(ResourceContainer resCntr, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadParticipants(resCntr, Util.children(element, PARTICIPANT));
    }

    /**
     * Load the participant objects from the given list of "Participant"
     * elements.
     *
     * @param resCntr
     * @param elements The list of "Participant" elements
     * @throws XPDLParserException
     */
    protected void loadParticipants(ResourceContainer resCntr, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element participantElement = (Element) iter.next();
            resCntr.add(createParticipant(participantElement));
        }
    }

    protected Participant createParticipant(Element element) throws XPDLParserException, PropertyVetoException {
        Element typeElement = Util.child(element, PARTICIPANT_TYPE);
        if (typeElement == null) {
            throw new ElementRequiredException(PARTICIPANT_TYPE, "Participant type required");
        }
        String typeString = typeElement.attributeValue(TYPE);
        if (typeString == null) {
            throw new ElementRequiredException(TYPE, "Participant type identifier required");
        }
        ParticipantType participantType = ParticipantType.valueOf(typeString);
        if (participantType == null) {
            throw new XPDLParserException("Unknown participant type: " + typeString);
        }
        ExternalReference extRef = null;
        Element extRefElement = Util.child(element, EXTERNAL_REFERENCE);
        if (extRefElement != null) extRef = createExternalReference(extRefElement);
        Participant participant = new Participant(element.attributeValue(ID), element.attributeValue(NAME), participantType, extRef);
        participant.setDescription(Util.elementAsString(element, DESCRIPTION));
        Map extAttrs = new HashMap();
        participant.setExtendedAttributes(loadExtendedAttributes(element, extAttrs));
        participant.setCalendar((String) extAttrs.get(OBE_CALENDAR));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + participant);
        return participant;
    }

    /**
     * Load applications from the given Element.  The element should represent a
     * collection of applications (which is identified by the "Applications"
     * tag).
     *
     * @param resCntr
     * @param element The "Applications" element
     * @throws XPDLParserException
     */
    protected void loadApplications(ResourceContainer resCntr, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadApplications(resCntr, Util.children(element, APPLICATION));
    }

    /**
     * Load the applications objects from the given list of "Application"
     * elements.
     *
     * @param resCntr
     * @param elements The list of "Application" elements
     * @throws XPDLParserException
     */
    protected void loadApplications(ResourceContainer resCntr, List elements) throws XPDLParserException, PropertyVetoException {
        _logger.debug("Loading applications in container " + resCntr.getId() + "...");
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element applicationElement = (Element) iter.next();
            resCntr.add(createApplication(resCntr, applicationElement));
        }
        _logger.debug(resCntr.getApplication().length + APPLICATIONS_LOADED);
    }

    protected Application createApplication(ResourceContainer resCntr, Element element) throws XPDLParserException, PropertyVetoException {
        Application application = new Application(element.attributeValue(ID), element.attributeValue(NAME));
        application.setDescription(Util.elementAsString(element, DESCRIPTION));
        if (Util.child(element, FORMAL_PARAMETERS) != null) {
            loadFormalParameters(resCntr, application, Util.child(element, FORMAL_PARAMETERS));
        } else if (Util.child(element, EXTERNAL_REFERENCE) != null) {
            application.setExternalReference(createExternalReference(Util.child(element, EXTERNAL_REFERENCE)));
        }
        application.setExtendedAttributes(loadExtendedAttributes(resCntr, element));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + application);
        return application;
    }

    protected AssignmentStrategyDef createAssignmentStrategy(Element element) throws XPDLParserException {
        boolean expandGroups = Boolean.valueOf(element.attributeValue(EXPAND_GROUPS)).booleanValue();
        AssignmentStrategyDef strategy = new AssignmentStrategyDef(element.attributeValue(ID), expandGroups);
        _logger.debug("Created assignment strategy " + strategy.getId());
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + strategy);
        return strategy;
    }

    /**
     * Load workflow processes from the given Element.  The element should
     * represent a collection of workflow process (which is identified by the
     * "WorkflowProcesses" tag).
     *
     * @param element The "WorkflowProcesses" element
     * @throws XPDLParserException
     */
    protected void loadWorkflowProcesses(XPDLPackage pkg, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadWorkflowProcesses(pkg, Util.children(element, WORKFLOW_PROCESS));
    }

    /**
     * Load the workflow process objects from the given list of
     * "WorkflowProcess" elements.
     *
     * @param elements The list of "WorkflowProcess" elements
     * @throws XPDLParserException
     */
    protected void loadWorkflowProcesses(XPDLPackage pkg, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element workflowProcessElement = (Element) iter.next();
            pkg.add(createWorkflowProcess(pkg, workflowProcessElement));
        }
    }

    protected WorkflowProcess createWorkflowProcess(XPDLPackage pkg, Element element) throws XPDLParserException, PropertyVetoException {
        ProcessHeader processHeader = createProcessHeader(Util.child(element, PROCESS_HEADER));
        WorkflowProcess wp = new WorkflowProcess(element.attributeValue(ID), element.attributeValue(NAME), pkg, processHeader);
        wp.setRedefinableHeader(createRedefinableHeader(Util.child(element, REDEFINABLE_HEADER)));
        wp.setAccessLevel(AccessLevel.valueOf(element.attributeValue(ACCESS_LEVEL)));
        loadFormalParameters(pkg, wp, Util.child(element, FORMAL_PARAMETERS));
        loadDataFields(wp, Util.child(element, DATA_FIELDS));
        loadParticipants(wp, Util.child(element, PARTICIPANTS));
        _logger.debug("Loading applications in process " + wp.getId() + "...");
        loadApplications(wp, Util.child(element, APPLICATIONS));
        _logger.debug(wp.getApplication().length + APPLICATIONS_LOADED);
        _logger.debug("Loading activity sets in process " + wp.getId() + "...");
        loadActivitySets(pkg, wp, Util.child(element, ACTIVITY_SETS));
        _logger.debug(wp.getActivitySet().length + " activity sets loaded");
        _logger.debug("Loading activities in process " + wp.getId() + "...");
        loadActivities(pkg, wp, wp, Util.child(element, ACTIVITIES));
        _logger.debug(wp.getActivity().length + " activities loaded");
        _logger.debug("Loading transitions in process " + wp.getId() + "...");
        loadTransitions(pkg, wp, Util.child(element, TRANSITIONS));
        _logger.debug(wp.getTransition().length + " transitions loaded");
        Map extAttrs = new HashMap();
        wp.setExtendedAttributes(loadExtendedAttributes(element, extAttrs));
        Event event = (Event) extAttrs.get(OBE_EVENT);
        Timer timer = (Timer) extAttrs.get(OBE_TIMER);
        if (event != null && timer != null) {
            throw new XPDLParserException("WorkflowProcess: event and timer are mutually exclusive");
        }
        if (event != null) wp.setTrigger(event); else if (timer != null) wp.setTrigger(timer);
        wp.setAssignmentStrategy((AssignmentStrategyDef) extAttrs.get(OBE_ASSIGNMENT_STRATEGY));
        wp.setCalendar((String) extAttrs.get(OBE_CALENDAR));
        wp.setCompletionStrategy((String) extAttrs.get(OBE_COMPLETION_STRATEGY));
        _logger.debug("Resolving references in process " + wp.getId() + "...");
        wp.resolveReferences();
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + wp);
        return wp;
    }

    protected ProcessHeader createProcessHeader(Element element) throws XPDLParserException {
        if (element == null) {
            throw new ElementRequiredException(PROCESS_HEADER, XPDLMessages.PROCESS_HEADER_REQUIRED);
        }
        ProcessHeader processHeader = new ProcessHeader();
        DurationUnit durationUnit = defaultDurationUnit;
        String durationUnitString = element.attributeValue(DURATION_UNIT);
        if (durationUnitString != null) durationUnit = DurationUnit.valueOf(durationUnitString);
        processHeader.setDurationUnit(durationUnit);
        processHeader.setCreated(Util.elementAsDate(element, CREATED));
        processHeader.setPriority(Util.elementAsString(element, PRIORITY));
        processHeader.setLimit(Util.elementAsDuration(element, LIMIT));
        processHeader.setValidFrom(Util.elementAsDate(element, VALID_FROM));
        processHeader.setValidTo(Util.elementAsDate(element, VALID_TO));
        processHeader.setDescription(Util.elementAsString(element, DESCRIPTION));
        processHeader.setTimeEstimation(createTimeEstimation(Util.child(element, TIME_ESTIMATION)));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + processHeader);
        return processHeader;
    }

    /**
     * Load activity sets from the given Element.  The element should represent
     * a collection of activity sets (which is identified by the "ActivitySets"
     * tag).
     *
     * @param pkg
     * @param element The "ActivitySets" element
     * @throws XPDLParserException
     */
    protected void loadActivitySets(XPDLPackage pkg, WorkflowProcess wp, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) {
            _logger.debug("ActivitySets element was null");
            return;
        }
        loadActivitySets(pkg, wp, Util.children(element, ACTIVITY_SET));
    }

    /**
     * Load the activity set objects from the given list of "ActivitySet"
     * elements.
     *
     * @param pkg
     * @param elements The list of "Activity" elements
     * @throws XPDLParserException
     */
    protected void loadActivitySets(XPDLPackage pkg, WorkflowProcess wp, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element activitySetElement = (Element) iter.next();
            wp.add(createActivitySet(pkg, wp, activitySetElement));
        }
    }

    protected ActivitySet createActivitySet(XPDLPackage pkg, WorkflowProcess wp, Element element) throws XPDLParserException, PropertyVetoException {
        ActivitySet activitySet = new ActivitySet(element.attributeValue(ID));
        loadActivities(pkg, wp, activitySet, Util.child(element, ACTIVITIES));
        loadTransitions(pkg, activitySet, Util.child(element, TRANSITIONS));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + activitySet);
        return activitySet;
    }

    /**
     * Load actvities from the given Element.  The element should represent a
     * collection of activities (which is identified by the "Activities" tag).
     *
     * @param pkg
     * @param graph   The list of activities
     * @param element The "Activities" element
     * @throws XPDLParserException
     */
    protected void loadActivities(XPDLPackage pkg, WorkflowProcess wp, Graph graph, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) {
            _logger.debug("Activities element was null");
            return;
        }
        loadActivities(pkg, wp, graph, Util.children(element, ACTIVITY));
    }

    /**
     * Load the activity objects from the given list of "Activity" elements.
     *
     * @param pkg
     * @param graph    The list of activites
     * @param elements The list of "Activity" elements
     * @throws XPDLParserException
     */
    protected void loadActivities(XPDLPackage pkg, WorkflowProcess wp, Graph graph, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element activityElement = (Element) iter.next();
            graph.add(createActivity(pkg, wp, activityElement));
        }
    }

    protected Activity createActivity(XPDLPackage pkg, WorkflowProcess wp, Element element) throws XPDLParserException, PropertyVetoException {
        Activity activity = new Activity(element.attributeValue(ID), element.attributeValue(NAME), wp);
        activity.setDescription(Util.elementAsString(element, DESCRIPTION));
        activity.setLimit(Util.elementAsDuration(element, LIMIT));
        activity.setPerformer(Util.elementAsString(element, PERFORMER));
        activity.setPriority(Util.elementAsString(element, PRIORITY));
        activity.setDocumentation(Util.elementAsURL(element, DOCUMENTATION));
        activity.setIcon(Util.elementAsURL(element, ICON));
        loadDeadlines(activity, Util.children(element, DEADLINE));
        activity.setSimulationInformation(createSimulationInformation(Util.child(element, SIMULATION_INFORMATION)));
        Element implementationElement = Util.child(element, IMPLEMENTATION);
        if (implementationElement != null) {
            activity.setImplementation(createImplementation(pkg, implementationElement));
        }
        Element routeElement = Util.child(element, ROUTE);
        if (routeElement != null) {
            if (implementationElement == null) {
                activity.setRoute(createRoute());
            } else {
                _logger.warn(IMPL_ROUTE_MUTEX + "defined together.  Ignoring route.");
            }
        }
        Element blockActivityElement = Util.child(element, BLOCKACTIVITY);
        if (blockActivityElement != null) {
            if (implementationElement != null || routeElement != null) {
                _logger.warn(IMPL_ROUTE_MUTEX + "defined together.  Ignoring BlockActivity.");
            } else {
                activity.setBlockActivity(createBlockActivity(blockActivityElement));
            }
        }
        if (implementationElement == null && routeElement == null && blockActivityElement == null) {
            throw new XPDLParserException("Activity '" + activity.getId() + "' requires Implementation, Route or BlockActivity");
        }
        Element startModeElement = Util.child(element, START_MODE);
        if (startModeElement != null) {
            activity.setStartMode(Util.child(startModeElement, MANUAL) != null ? AutomationMode.MANUAL : AutomationMode.AUTOMATIC);
        }
        Element finishModeElement = Util.child(element, FINISH_MODE);
        if (finishModeElement != null) {
            activity.setFinishMode(Util.child(finishModeElement, MANUAL) != null ? AutomationMode.MANUAL : AutomationMode.AUTOMATIC);
        }
        loadTransitionRestrictions(activity, Util.child(element, TRANSITION_RESTRICTIONS));
        Map extAttrs = new HashMap();
        activity.setExtendedAttributes(loadExtendedAttributes(element, extAttrs));
        activity.setAssignmentStrategy((AssignmentStrategyDef) extAttrs.get(OBE_ASSIGNMENT_STRATEGY));
        activity.setBounds((Rectangle) extAttrs.get(OBE_BOUNDS));
        activity.setCalendar((String) extAttrs.get(OBE_CALENDAR));
        activity.setCompletionStrategy((String) extAttrs.get(OBE_COMPLETION_STRATEGY));
        BlockActivity ba = activity.getBlockActivity();
        if (ba != null) ba.setLoop((Loop) extAttrs.get(OBE_LOOP));
        activity.setToolMode((ToolMode) extAttrs.get(OBE_TOOL_MODE));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + activity);
        return activity;
    }

    protected void loadDeadlines(Activity activity, List elements) throws XPDLParserException, PropertyVetoException {
        if (elements != null) {
            for (Iterator iter = elements.iterator(); iter.hasNext(); ) {
                Deadline deadline = createDeadline((Element) iter.next());
                if (deadline != null) activity.add(deadline);
            }
        }
    }

    /**
     * Create a Deadline object from the given Element.
     *
     * @param element The Element
     * @return the Deadline object
     * @throws XPDLParserException
     */
    protected Deadline createDeadline(Element element) throws XPDLParserException {
        if (element == null) {
            _logger.debug("Deadline not found");
            return null;
        }
        _logger.debug("Creating Deadline object");
        Deadline deadline = new Deadline(Util.elementAsString(element, DEADLINE_CONDITION), Util.elementAsString(element, EXCEPTION_NAME));
        deadline.setExecutionType(ExecutionType.valueOf(element.attributeValue(EXECUTION)));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + deadline);
        return deadline;
    }

    protected BlockActivity createBlockActivity(Element element) {
        BlockActivity blockActivity = new BlockActivity();
        blockActivity.setBlockId(element.attributeValue(BLOCKID));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + blockActivity);
        return blockActivity;
    }

    protected Implementation createImplementation(XPDLPackage pkg, Element element) throws XPDLParserException, PropertyVetoException {
        ToolSet toolSet = new ToolSet();
        Iterator elements = element.elements().iterator();
        while (elements.hasNext()) {
            Element implElement = (Element) elements.next();
            ImplementationType implType = ImplementationType.valueOf(implElement.getName());
            if (implType == null) {
                throw new XPDLParserException("Illegal implementation type: " + implElement.getName());
            }
            switch(implType.value()) {
                case ImplementationType.NO_INT:
                    return new NoImplementation();
                case ImplementationType.SUBFLOW_INT:
                    return createSubFlow(implElement);
                case ImplementationType.TOOLS_INT:
                    toolSet.add(createTool(pkg, implElement));
                    break;
                default:
                    throw new XPDLParserException("Unsupported implementation type: " + implType);
            }
        }
        return toolSet;
    }

    protected Route createRoute() {
        Route route = new Route();
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + route);
        return route;
    }

    protected Loop createLoop(Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) throw new XPDLParserException("obe:Loop element missing");
        Loop loop = new Loop();
        Element whileElement = element.element(WHILE_QNAME);
        if (whileElement == null) {
            Element untilElement = element.element(UNTIL_QNAME);
            if (untilElement == null) {
                Element foreachElement = element.element(FOREACH_QNAME);
                if (foreachElement != null) {
                    loop.setBody(createForEach(foreachElement));
                }
            } else {
                loop.setBody(createUntil(untilElement));
            }
        } else {
            loop.setBody(createWhile(whileElement));
        }
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + loop);
        return loop;
    }

    protected While createWhile(Element element) throws XPDLParserException, PropertyVetoException {
        Element condElement = element.element(CONDITION_QNAME);
        if (condElement == null) throw new XPDLParserException("Missing Condition element in While");
        While w = new While(createCondition(condElement));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + w);
        return w;
    }

    protected Until createUntil(Element element) throws XPDLParserException, PropertyVetoException {
        Element condElement = element.element(CONDITION_QNAME);
        if (condElement == null) throw new XPDLParserException("Missing Condition element in Until");
        Until u = new Until(createCondition(condElement));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + u);
        return u;
    }

    protected ForEach createForEach(Element element) throws XPDLParserException {
        String dataField = element.attributeValue(DATA_FIELD);
        Element exprElement = element.element(IN_QNAME);
        if (exprElement == null) throw new XPDLParserException("Missing In element in ForEach");
        ForEach fe = new ForEach(dataField, exprElement.getTextTrim());
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + fe);
        return fe;
    }

    protected SubFlow createSubFlow(Element element) throws XPDLParserException, PropertyVetoException {
        SubFlow subFlow = new SubFlow(element.attributeValue(ID));
        String executionString = element.attributeValue(EXECUTION);
        if (executionString != null) subFlow.setExecution(ExecutionType.valueOf(executionString));
        loadActualParameters(subFlow, Util.child(element, ACTUAL_PARAMETERS));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + subFlow);
        return subFlow;
    }

    protected Tool createTool(XPDLPackage pkg, Element element) throws XPDLParserException, PropertyVetoException {
        Tool tool = new Tool(element.attributeValue(ID));
        tool.setDescription(Util.elementAsString(element, DESCRIPTION));
        String typeString = element.attributeValue(TYPE);
        if (typeString != null) tool.setToolType(ToolType.valueOf(typeString));
        loadActualParameters(tool, Util.child(element, ACTUAL_PARAMETERS));
        tool.setExtendedAttributes(loadExtendedAttributes(pkg, element));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + tool);
        return tool;
    }

    protected SimulationInformation createSimulationInformation(Element element) throws XPDLParserException {
        if (element == null) return null;
        SimulationInformation simInfo = new SimulationInformation();
        InstantiationType instantiationType = defaultInstantiationType;
        String instantiationTypeString = element.attributeValue(INSTANTIATION_TYPE);
        if (instantiationTypeString != null) {
            instantiationType = InstantiationType.valueOf(instantiationTypeString);
        }
        simInfo.setInstantiationType(instantiationType);
        simInfo.setCost(Util.elementAsString(element, COST));
        simInfo.setTimeEstimation(createTimeEstimation(Util.child(element, TIME_ESTIMATION)));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + simInfo);
        return simInfo;
    }

    /**
     * Create a TimeEstimation object.
     *
     * @param element The TimeEstimation Element
     * @return The TimeEstimation object
     * @throws XPDLParserException Parse error
     */
    protected TimeEstimation createTimeEstimation(Element element) throws XPDLParserException {
        if (element == null) return null;
        TimeEstimation timeEstimation = new TimeEstimation();
        timeEstimation.setWaitingTime(Util.elementAsDuration(element, WAITING_TIME));
        timeEstimation.setWorkingTime(Util.elementAsDuration(element, WORKING_TIME));
        timeEstimation.setDuration(Util.elementAsDuration(element, DURATION));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + timeEstimation);
        return timeEstimation;
    }

    public Condition createCondition(Element element) throws PropertyVetoException {
        if (element == null) return null;
        Condition condition = new Condition();
        Iterator xpressionElements = Util.children(element, XPRESSION).iterator();
        while (xpressionElements.hasNext()) {
            Element xpressionElement = (Element) xpressionElements.next();
            condition.add(new Xpression(xpressionElement.getText()));
        }
        String typeString = element.attributeValue(TYPE);
        if (typeString != null) condition.setType(ConditionType.valueOf(typeString));
        condition.setValue(element.getText());
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + condition);
        return condition;
    }

    /**
     * Load transitions from the given Element.  The element should represent a
     * collection of transitions (which is identified by the "Transitions"
     * tag).
     *
     * @param pkg
     * @param graph   The list of transitions
     * @param element The "Transitions" element
     * @throws XPDLParserException
     */
    protected void loadTransitions(XPDLPackage pkg, Graph graph, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadTransitions(pkg, graph, Util.children(element, TRANSITION));
    }

    /**
     * Load the transition objects from the given list of "Transition"
     * elements.
     *
     * @param pkg
     * @param graph    The list of transitions
     * @param elements The list of "Transition" elements
     * @throws XPDLParserException
     */
    protected void loadTransitions(XPDLPackage pkg, Graph graph, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element transitionElement = (Element) iter.next();
            graph.add(createTransition(pkg, transitionElement));
        }
    }

    protected Transition createTransition(XPDLPackage pkg, Element element) throws XPDLParserException, PropertyVetoException {
        Transition transition = new Transition(element.attributeValue(ID), element.attributeValue(NAME), element.attributeValue(FROM), element.attributeValue(TO));
        transition.setDescription(Util.elementAsString(element, DESCRIPTION));
        transition.setCondition(createCondition(Util.child(element, CONDITION)));
        Map extAttrs = new HashMap();
        transition.setExtendedAttributes(loadExtendedAttributes(element, extAttrs));
        transition.setEvent((Event) extAttrs.get(OBE_EVENT));
        transition.setExecutionType((ExecutionType) extAttrs.get(EXECUTION));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + transition);
        return transition;
    }

    protected Event createEvent(Element eventElement) throws XPDLParserException, PropertyVetoException {
        if (eventElement == null) throw new XPDLParserException("obe:Event element missing");
        Event event = new Event(eventElement.attributeValue(ID), eventElement.attributeValue(COUNT), eventElement.attributeValue(PREDICATE));
        Element actualParametersElem = eventElement.element(ACTUAL_PARAMETERS_QNAME);
        if (actualParametersElem != null) loadActualParameters(event, actualParametersElem);
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + event);
        return event;
    }

    protected Timer createTimer(Element timerElement) throws XPDLParserException, PropertyVetoException {
        if (timerElement == null) throw new XPDLParserException("obe:Timer element missing");
        Timer timer = new Timer(timerElement.attributeValue(ID), timerElement.attributeValue(COUNT), timerElement.attributeValue(INTERVAL), timerElement.attributeValue(CALENDAR), timerElement.attributeValue(RECOVERABLE));
        Element actualParametersElem = timerElement.element(ACTUAL_PARAMETERS_QNAME);
        if (actualParametersElem != null) loadActualParameters(timer, actualParametersElem);
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + timer);
        return timer;
    }

    /**
     * Load transition restrictions from the given Element.  The element should
     * represent a collection of transition restrictions (which is identified by
     * the "TransitionRestrictions" tag).
     *
     * @param activity The list of transition restrictions
     * @param element  The "TransitionRestrictions" element
     * @throws XPDLParserException
     */
    protected void loadTransitionRestrictions(Activity activity, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadTransitionRestrictions(activity, Util.children(element, TRANSITION_RESTRICTION));
    }

    /**
     * Load the transition restriction objects from the given list of
     * "TransitionRestriction"  elements.
     *
     * @param activity The list of transition restriction
     * @param elements The list of "TransitionRestriction" elements
     * @throws XPDLParserException
     */
    protected void loadTransitionRestrictions(Activity activity, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element transitionRestrictionElement = (Element) iter.next();
            activity.add(createTransitionRestriction(transitionRestrictionElement));
        }
    }

    protected TransitionRestriction createTransitionRestriction(Element element) throws XPDLParserException, PropertyVetoException {
        _logger.debug("Loading transition restriction");
        TransitionRestriction transitionRestriction = new TransitionRestriction();
        transitionRestriction.setJoin(createJoin(Util.child(element, JOIN)));
        transitionRestriction.setSplit(createSplit(Util.child(element, SPLIT)));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + transitionRestriction);
        return transitionRestriction;
    }

    public Join createJoin(Element element) {
        if (element == null) return null;
        Join join = new Join();
        String typeString = element.attributeValue(TYPE);
        if (typeString != null) join.setType(JoinType.valueOf(typeString));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + join);
        return join;
    }

    public Split createSplit(Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return null;
        Split split = new Split();
        String typeString = element.attributeValue(TYPE);
        if (typeString == null) {
            throw new XPDLParserException("Unsupported split type: " + typeString);
        }
        split.setType(SplitType.valueOf(typeString));
        loadTransitionReferences(split, Util.child(element, TRANSITION_REFERENCES));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + split);
        return split;
    }

    /**
     * Load transition references from the given Element.  The element should
     * represent a collection of transition references (which is identified by
     * the "TransitionRefs" tag).
     *
     * @param split   The list of transition references
     * @param element The "TransitionRefs" element
     * @throws XPDLParserException
     */
    protected void loadTransitionReferences(Split split, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadTransitionReferences(split, Util.children(element, TRANSITION_REFERENCE));
    }

    /**
     * Load the transition references objects from the given list of
     * "TransitionRef" elements.
     *
     * @param split    The list of transition references
     * @param elements The list of "TransitionRef" elements
     * @throws XPDLParserException
     */
    protected void loadTransitionReferences(Split split, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element transitionRefElement = (Element) iter.next();
            split.add(transitionRefElement.attributeValue(ID));
        }
    }

    /**
     * Load formal parameters from the given Element.  The element should
     * represent a collection of formal parameters (which is identified by the
     * "FormalParameters" tag).
     *
     * @param resCntr
     * @param invokable The list of formal parameters
     * @param element   The "FormalParameters" element
     * @throws XPDLParserException
     */
    protected void loadFormalParameters(ResourceContainer resCntr, Invokable invokable, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadFormalParameters(resCntr, invokable, Util.children(element, FORMAL_PARAMETER));
    }

    /**
     * Load the formal parameters objects from the given list of
     * "FormalParameter" elements.
     *
     * @param resCntr
     * @param invokable The list of formal parameters
     * @param elements  The list of "FormalParameter" elements
     * @throws XPDLParserException
     */
    protected void loadFormalParameters(ResourceContainer resCntr, Invokable invokable, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element fpElem = (Element) iter.next();
            invokable.add(createFormalParameter(resCntr, fpElem));
        }
    }

    protected FormalParameter createFormalParameter(ResourceContainer resCntr, Element element) throws XPDLParserException, PropertyVetoException {
        DataType dataType = createDataType(resCntr, Util.child(element, DATA_TYPE));
        if (dataType == null) {
            throw new ElementRequiredException(DATA_TYPE, XPDLMessages.DATA_TYPE_REQUIRED);
        }
        FormalParameter param = new FormalParameter(element.attributeValue(ID), element.attributeValue(INDEX), element.attributeValue(MODE), dataType, Util.elementAsString(element, DESCRIPTION));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + param);
        return param;
    }

    /**
     * Load actual parameters from the given Element.  The element should
     * represent a collection of actual parameters (which is identified by the
     * "ActualParameters" tag).
     *
     * @param invocation The list of actual parameters
     * @param element    The "ActualParameters" element
     * @throws XPDLParserException
     */
    protected void loadActualParameters(Invocation invocation, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadActualParameters(invocation, Util.children(element, ACTUAL_PARAMETER));
    }

    /**
     * Load the actual parameters objects from the given list of
     * "ActualParameter" elements.
     *
     * @param invocation The list of actual parameters
     * @param elements   The list of "ActualParameter" elements
     * @throws XPDLParserException
     */
    protected void loadActualParameters(Invocation invocation, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element actualParameterElement = (Element) iter.next();
            invocation.add(createActualParameter(actualParameterElement));
        }
    }

    protected ActualParameter createActualParameter(Element element) throws XPDLParserException {
        ActualParameter actualParm = new ActualParameter(element.getText());
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + actualParm);
        return actualParm;
    }

    protected DataType createDataType(ResourceContainer resCntr, Element element) throws XPDLParserException, PropertyVetoException {
        DataType dataType = new DataType(createType(resCntr, element));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + dataType);
        return dataType;
    }

    protected Type createType(ResourceContainer resCntr, Element parent) throws XPDLParserException, PropertyVetoException {
        List list = parent.elements();
        if (list.size() < 1) throw new ElementRequiredException(TYPE, XPDLMessages.TYPE_REQUIRED);
        Type type = null;
        Element typeElement = (Element) list.get(0);
        String typeName = typeElement.getName();
        if (typeName.equals(BASIC_TYPE)) {
            type = BasicType.valueOf(typeElement.attributeValue(TYPE));
        } else if (typeName.equals(DECLARED_TYPE)) {
            XPDLPackage pkg = resCntr instanceof XPDLPackage ? (XPDLPackage) resCntr : ((WorkflowProcess) resCntr).getPackage();
            String refid = typeElement.attributeValue(ID);
            for (int i = 0, n = pkg.getTypeDeclaration().length; i < n; i++) {
                TypeDeclaration typeDecl = pkg.getTypeDeclaration(i);
                if (typeDecl.getId().equals(refid)) {
                    type = new DeclaredType(typeDecl);
                    break;
                }
            }
            if (type == null) {
                throw new XPDLParserException("Undefined TypeDeclaration reference: " + refid);
            }
        } else if (typeName.equals(SCHEMA_TYPE)) {
            type = createSchemaType(typeElement);
        } else if (typeName.equals(EXTERNAL_REFERENCE)) {
            return createExternalReference(typeElement);
        } else if (typeName.equals(RECORD_TYPE)) {
            RecordType recType = new RecordType();
            loadMembers(resCntr, Util.children(typeElement, MEMBER), recType);
            type = recType;
        } else if (typeName.equals(UNION_TYPE)) {
            UnionType unionType = new UnionType();
            loadMembers(resCntr, Util.children(typeElement, MEMBER), unionType);
            type = unionType;
        } else if (typeName.equals(ENUMERATION_TYPE)) {
            EnumerationType enumType = new EnumerationType();
            loadEnumerationValues(Util.children(typeElement, ENUMERATION_VALUE), enumType);
            type = enumType;
        } else if (typeName.equals(ARRAY_TYPE)) {
            type = new ArrayType(createType(resCntr, typeElement), typeElement.attributeValue(LOWER_INDEX), typeElement.attributeValue(UPPER_INDEX));
        } else if (typeName.equals(LIST_TYPE)) {
            type = new ListType(createType(resCntr, typeElement));
        }
        if (type == null) {
            throw new XPDLParserException("Illegal type name: " + typeName);
        }
        if (_logger.isDebugEnabled()) _logger.debug("Created Type: " + type);
        return type;
    }

    protected SchemaType createSchemaType(Element typeElement) throws XPDLParserException {
        SchemaType schemaType = new SchemaType();
        List list = typeElement.elements();
        if (!list.isEmpty()) {
            if (list.size() > 1) _logger.warn("Additional SchemaType child elements ignored");
            try {
                schemaType.setText(Util.exportToText((Element) list.get(0)));
            } catch (XMLException e) {
                throw new XPDLParserException(e);
            }
        }
        return schemaType;
    }

    protected void loadMembers(ResourceContainer resCntr, List elements, ComplexType complexType) throws XPDLParserException, PropertyVetoException {
        Iterator memberElements = elements.iterator();
        while (memberElements.hasNext()) {
            Element memberElement = (Element) memberElements.next();
            Type type = createType(resCntr, memberElement);
            complexType.add(type);
        }
    }

    protected void loadEnumerationValues(List elements, EnumerationType type) throws XPDLParserException, PropertyVetoException {
        Iterator valueElements = elements.iterator();
        while (valueElements.hasNext()) {
            Element valueElement = (Element) valueElements.next();
            type.add(new EnumerationValue(valueElement.attributeValue(NAME)));
        }
    }

    /**
     * Load type declarations from the given Element.  The element should
     * represent a collection of type declarations (which is identified by the
     * "TypeDeclarations" tag).
     *
     * @param pkg
     * @param element The "TypeDeclarations" element
     * @throws XPDLParserException
     */
    protected void loadTypeDeclarations(XPDLPackage pkg, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadTypeDeclarations(pkg, Util.children(element, TYPE_DECLARATION));
    }

    /**
     * Load the type declaration objects from the given list of
     * "TypeDeclaration" elements.
     *
     * @param pkg
     * @param elements The list of "TypeDeclaration" elements
     * @throws XPDLParserException
     */
    protected void loadTypeDeclarations(XPDLPackage pkg, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element typeDeclElem = (Element) iter.next();
            pkg.add(createTypeDeclaration(pkg, typeDeclElem));
        }
    }

    protected TypeDeclaration createTypeDeclaration(XPDLPackage pkg, Element element) throws XPDLParserException, PropertyVetoException {
        Type type = createType(pkg, element);
        TypeDeclaration typeDeclaration = new TypeDeclaration(element.attributeValue(ID), element.attributeValue(NAME), type);
        typeDeclaration.setDescription(Util.elementAsString(element, DESCRIPTION));
        typeDeclaration.setExtendedAttributes(loadExtendedAttributes(pkg, element));
        return typeDeclaration;
    }

    /**
     * Load data fields from the given Element.  The element should represent a
     * collection of data fields (which is identified by the "DataFields" tag).
     *
     * @param resCntr
     * @param element The "DataFields" element
     * @throws XPDLParserException
     */
    protected void loadDataFields(ResourceContainer resCntr, Element element) throws XPDLParserException, PropertyVetoException {
        if (element == null) return;
        loadDataFields(resCntr, Util.children(element, DATA_FIELD));
    }

    /**
     * Load the data field objects from the given list of "DataField" elements.
     *
     * @param resCntr
     * @param elements The list of "DataField" elements
     * @throws XPDLParserException
     */
    protected void loadDataFields(ResourceContainer resCntr, List elements) throws XPDLParserException, PropertyVetoException {
        Iterator iter = elements.iterator();
        while (iter.hasNext()) {
            Element dataFieldElement = (Element) iter.next();
            resCntr.add(createDataField(resCntr, dataFieldElement));
        }
        _logger.debug("Data fields size after load: " + resCntr.getDataField().length);
    }

    protected DataField createDataField(ResourceContainer resCntr, Element element) throws XPDLParserException, PropertyVetoException {
        DataType dataType = createDataType(resCntr, Util.child(element, DATA_TYPE));
        if (dataType == null) throw new ElementRequiredException(DATA_TYPE, XPDLMessages.DATA_TYPE_REQUIRED);
        DataField dataField = new DataField(element.attributeValue(ID), element.attributeValue(NAME), dataType);
        String isArrayString = element.attributeValue(IS_ARRAY);
        dataField.setArray(Boolean.valueOf(isArrayString).booleanValue());
        dataField.setDescription(Util.elementAsString(element, DESCRIPTION));
        dataField.setInitialValue(Util.elementAsString(element, INITIAL_VALUE));
        dataField.setLength(Util.elementAsInteger(element, LENGTH));
        dataField.setExtendedAttributes(loadExtendedAttributes(resCntr, element));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + dataField);
        return dataField;
    }

    /**
     * Create an ExternalReference object from the given Element.
     *
     * @param element The "ExternalReference" element
     * @return The ExternalReference object
     * @throws XPDLParserException
     */
    protected ExternalReference createExternalReference(Element element) throws XPDLParserException {
        ExternalReference externalReference = new ExternalReference(element.attributeValue(LOCATION), element.attributeValue(XREF), element.attributeValue(NAMESPACE));
        if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + externalReference);
        return externalReference;
    }

    /**
     * Load extended attributes from the given parent.  If the element has a
     * child &lt;ExtendedAttributes&gt; element, the latter's child
     * &lt;ExtendedAttribute&gt; elements recognized by OBE are parsed into the
     * <code>extendedAttributes</code> map and removed from their parent. Any
     * remaining 3rd party elements are detached and added to a new document,
     * which becomes the method's return value.
     *
     * @param resCntr
     * @param parent  The parent element.
     * @return A DOM document containing any 3rd party attributes (can be
     *         <code>null</code>).
     * @throws XPDLParserException
     */
    protected ExtendedAttributes loadExtendedAttributes(ResourceContainer resCntr, Element parent) throws XPDLParserException, PropertyVetoException {
        return loadExtendedAttributes(parent, null);
    }

    /**
     * Load extended attributes from the given parent.  If the element has a
     * child &lt;ExtendedAttributes&gt; element, the latter's child
     * &lt;ExtendedAttribute&gt; elements recognized by OBE are parsed into the
     * <code>extendedAttributes</code> map and removed from their parent. Any
     * remaining 3rd party elements are detached and added to a new document,
     * which becomes the method's return value.
     *
     * @param parent        The parent element.
     * @param obeExtAttrMap Map of parsed OBE extended attributes.
     * @return A DOM document containing any 3rd party attributes (can be
     *         <code>null</code>).
     * @throws XPDLParserException
     */
    protected ExtendedAttributes loadExtendedAttributes(Element parent, Map obeExtAttrMap) throws XPDLParserException, PropertyVetoException {
        Element extAttrsElem = Util.child(parent, EXTENDED_ATTRIBUTES);
        if (extAttrsElem == null) return null;
        List elements = Util.children(extAttrsElem, EXTENDED_ATTRIBUTE);
        if (elements.isEmpty()) return null;
        if (_logger.isDebugEnabled()) {
            _logger.debug("Loading extended attributes [size=" + elements.size() + ']');
        }
        for (Iterator iter = elements.iterator(); iter.hasNext(); ) {
            Element element = (Element) iter.next();
            String name = element.attributeValue(NAME);
            Object value;
            if (name.equals(OBE_ASSIGNMENT_STRATEGY)) {
                Element strategyElement = Util.detach(element, ASSIGNMENT_STRATEGY_QNAME);
                value = createAssignmentStrategy(strategyElement);
            } else if (name.equals(OBE_BOUNDS)) {
                Element boundsElement = Util.detach(element, BOUNDS_QNAME);
                value = createBounds(boundsElement);
            } else if (name.equals(OBE_CALENDAR)) {
                element.detach();
                value = element.attributeValue(VALUE);
            } else if (name.equals(OBE_COMPLETION_STRATEGY)) {
                element.detach();
                value = element.attributeValue(VALUE);
            } else if (name.equals(OBE_EVENT)) {
                Element eventElement = Util.detach(element, EVENT_QNAME);
                value = createEvent(eventElement);
            } else if (name.equals(OBE_TIMER)) {
                Element timerElement = Util.detach(element, TIMER_QNAME);
                value = createTimer(timerElement);
            } else if (name.equals(EXECUTION)) {
                element.detach();
                String tag = element.attributeValue(VALUE);
                value = ExecutionType.valueOf(tag);
            } else if (name.equals(OBE_LOOP)) {
                Element loopElement = Util.detach(element, LOOP_QNAME);
                value = createLoop(loopElement);
            } else if (name.equals(OBE_TOOL_MODE)) {
                element.detach();
                value = ToolMode.valueOf(element.attributeValue(VALUE));
            } else {
                continue;
            }
            iter.remove();
            if (obeExtAttrMap != null) {
                obeExtAttrMap.put(name, value);
                if (_logger.isDebugEnabled()) {
                    _logger.debug("Added OBE extended attribute [" + name + '=' + value + ']');
                }
            }
        }
        ExtendedAttributes extAttrs = null;
        try {
            if (!elements.isEmpty()) {
                if ("".equals(extAttrsElem.getNamespaceURI())) {
                    boolean foundDefaultXPDLNS = false;
                    List namespaces = extAttrsElem.declaredNamespaces();
                    for (int i = 0, n = namespaces.size(); i < n; i++) {
                        Namespace ns = (Namespace) namespaces.get(i);
                        if ("".equals(ns.getPrefix()) && ns.getURI().equals(XPDL_NS_URI)) {
                            foundDefaultXPDLNS = true;
                            break;
                        }
                    }
                    if (!foundDefaultXPDLNS) namespaces.add(new Namespace("", XPDL_NS_URI));
                }
                extAttrs = new ExtendedAttributes(Util.exportToText(extAttrsElem));
                if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + extAttrs);
            }
        } catch (XMLException e) {
            throw new XPDLParserException(e);
        }
        return extAttrs;
    }

    protected Rectangle createBounds(Element boundsElement) {
        Rectangle rect = null;
        if (boundsElement != null) {
            Integer x = parseIntAttribute(boundsElement, X);
            Integer y = parseIntAttribute(boundsElement, Y);
            Integer width = parseIntAttribute(boundsElement, WIDTH);
            Integer height = parseIntAttribute(boundsElement, HEIGHT);
            if (x != null && y != null) {
                if (width == null) width = new Integer(DEFAULT_WIDTH);
                if (height == null) height = new Integer(DEFAULT_HEIGHT);
                rect = new Rectangle(x.intValue(), y.intValue(), width.intValue(), height.intValue());
                if (_logger.isDebugEnabled()) _logger.debug(MSG_CREATED + rect);
            } else {
                _logger.warn("Incomplete obe:Bounds specification ignored");
            }
        }
        return rect;
    }

    protected Integer parseIntAttribute(Element element, String attribute) {
        String s = element.attributeValue(attribute);
        return s == null || s.length() == 0 ? null : Integer.valueOf(s);
    }
}
