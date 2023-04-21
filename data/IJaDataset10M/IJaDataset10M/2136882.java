package it.unipr.ce.dsg.deus.core;

import it.unipr.ce.dsg.deus.schema.Automator;
import it.unipr.ce.dsg.deus.schema.Param;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.Random;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;

/**
 * <p>
 * This class is responsible for handling the simulation configuration file
 * according to the DEUS xml schema.
 * </p>
 * 
 * <p>
 * The configuration can be seen as a set of nodes, resources, events, processes
 * and engine parameters. This class handles the configuration of each
 * simulation object and stores them in a set of array data structures.
 * </p>
 * 
 * <p>
 * Each simulation object have a set of base features plus references to other
 * simulation objects: nodes can have a set of resources, events can have a set
 * of referenced events, processes can have references both to nodes and events.
 * </p>
 * 
 * <p>
 * At the end of the configuration file parsing process, this class will
 * initialize an Engine object enabling the simulation execution.
 * </p>
 * 
 * @author Matteo Agosti
 * @author Michele Amoretti (michele.amoretti@unipr.it)
 * 
 * @see it.unipr.ce.dsg.deus.core.Engine
 */
public class AutomatorParser {

    private ArrayList<Node> nodes = null;

    private ArrayList<Event> events = null;

    private ArrayList<Process> processes = null;

    private Engine engine = null;

    /**
	 * Class constructor responsible for configuration file handling and engine
	 * initialization.
	 * 
	 * @param fileName
	 *            the configuration file to open.
	 * @throws JAXBException
	 *             if the configuration file format is wrong.
	 * @throws ClassNotFoundException
	 *             if the configuration file contains handlers (fully qualified
	 *             class names) not existing in the classpath.
	 * @throws IllegalArgumentException
	 *             if the handler class cannot be instantiated.
	 * @throws SecurityException
	 *             if the handler class cannot be instantiated.
	 * @throws InstantiationException
	 *             if the handler class cannot be instantiated.
	 * @throws IllegalAccessException
	 *             if the handler class cannot be instantiated.
	 * @throws InvocationTargetException
	 *             if the handler class cannot be instantiated.
	 * @throws NoSuchMethodException
	 *             if the handler class cannot be instantiated.
	 * @throws SAXException
	 *             if handler class cannot be instantiated.
	 */
    @SuppressWarnings("unchecked")
    public AutomatorParser(String fileName) throws JAXBException, ClassNotFoundException, IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SAXException {
        nodes = new ArrayList<Node>();
        events = new ArrayList<Event>();
        processes = new ArrayList<Process>();
        JAXBContext jc = JAXBContext.newInstance("it.unipr.ce.dsg.deus.schema");
        SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
        Schema schema = schemaFactory.newSchema(new File("schema/automator.xsd"));
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        unmarshaller.setSchema(schema);
        unmarshaller.setEventHandler(new ValidationEventHandler() {

            public boolean handleEvent(ValidationEvent ve) {
                if (ve.getSeverity() == ValidationEvent.FATAL_ERROR || ve.getSeverity() == ValidationEvent.ERROR || ve.getSeverity() == ValidationEvent.WARNING) {
                    ValidationEventLocator locator = ve.getLocator();
                    System.out.println("Invalid configuration file: " + locator.getURL());
                    System.out.println("Error at column " + locator.getColumnNumber() + ", line " + locator.getLineNumber());
                    System.out.println("Error: " + ve.getMessage());
                    return false;
                }
                return true;
            }
        });
        Automator automator = (Automator) ((JAXBElement) unmarshaller.unmarshal(new File(fileName))).getValue();
        for (Iterator<it.unipr.ce.dsg.deus.schema.Node> it = automator.getNode().iterator(); it.hasNext(); ) {
            it.unipr.ce.dsg.deus.schema.Node node = it.next();
            Class<Node> nodeHandler = (Class<Node>) this.getClass().getClassLoader().loadClass(node.getHandler());
            Properties params = new Properties();
            if (node.getParams() != null) params = parseParams(node.getParams().getParam().iterator());
            ArrayList<Resource> resources = new ArrayList<Resource>();
            if (node.getResources() != null) {
                for (Iterator<it.unipr.ce.dsg.deus.schema.Resource> it2 = node.getResources().getResource().iterator(); it2.hasNext(); ) {
                    it.unipr.ce.dsg.deus.schema.Resource resource = it2.next();
                    Class<Resource> resourceHandler = (Class<Resource>) this.getClass().getClassLoader().loadClass(resource.getHandler());
                    Properties resourceParams = new Properties();
                    if (resource.getParams() != null) resourceParams = parseParams(resource.getParams().getParam().iterator());
                    Resource configResource = resourceHandler.getConstructor(new Class[] { Properties.class }).newInstance(new Object[] { resourceParams });
                    resources.add(configResource);
                }
            }
            Node configNode = nodeHandler.getConstructor(new Class[] { String.class, Properties.class, ArrayList.class }).newInstance(new Object[] { node.getId(), params, resources });
            if (node.getLogger() != null) {
                configNode.setLoggerLevel(node.getLogger().getLevel());
                configNode.setLoggerPathPrefix(node.getLogger().getPathPrefix());
            }
            nodes.add(configNode);
        }
        Random simulationRandom = new Random(automator.getEngine().getSeed());
        for (Iterator<it.unipr.ce.dsg.deus.schema.Event> it = automator.getEvent().iterator(); it.hasNext(); ) {
            it.unipr.ce.dsg.deus.schema.Event event = it.next();
            Class<Event> eventHandler = (Class<Event>) this.getClass().getClassLoader().loadClass(event.getHandler());
            Properties params = new Properties();
            if (event.getParams() != null) params = parseParams(event.getParams().getParam().iterator());
            Event configEvent = eventHandler.getConstructor(new Class[] { String.class, Properties.class, Process.class }).newInstance(new Object[] { event.getId(), params, null });
            configEvent.setEventSeed(simulationRandom.nextInt(1000000000));
            if (event.isOneShot() != null) configEvent.setOneShot(event.isOneShot().booleanValue());
            if (event.getLogger() != null) {
                configEvent.setLoggerLevel(event.getLogger().getLevel());
                configEvent.setLoggerPathPrefix(event.getLogger().getPathPrefix());
            }
            if (event.getSchedulerListener() != null) {
                SchedulerListener schedulerListener = (SchedulerListener) this.getClass().getClassLoader().loadClass(event.getSchedulerListener()).newInstance();
                configEvent.setSchedulerListener(schedulerListener);
            }
            events.add(configEvent);
        }
        for (Iterator<it.unipr.ce.dsg.deus.schema.Event> it = automator.getEvent().iterator(); it.hasNext(); ) {
            it.unipr.ce.dsg.deus.schema.Event event = it.next();
            Event realEvent = getEventById(event.getId());
            if (event.getEvents() != null) {
                for (Iterator<it.unipr.ce.dsg.deus.schema.Reference> it2 = event.getEvents().getReference().iterator(); it2.hasNext(); ) {
                    realEvent.getReferencedEvents().add(getEventById(it2.next().getId()));
                }
            }
        }
        for (Iterator<it.unipr.ce.dsg.deus.schema.Process> it = automator.getProcess().iterator(); it.hasNext(); ) {
            it.unipr.ce.dsg.deus.schema.Process process = it.next();
            ArrayList<Node> referencedNodes = new ArrayList<Node>();
            if (process.getNodes() != null) for (Iterator<it.unipr.ce.dsg.deus.schema.Reference> it2 = process.getNodes().getReference().iterator(); it2.hasNext(); ) referencedNodes.add(getNodeById(it2.next().getId()));
            ArrayList<Event> referencedEvents = new ArrayList<Event>();
            if (process.getEvents() != null) for (Iterator<it.unipr.ce.dsg.deus.schema.Reference> it2 = process.getEvents().getReference().iterator(); it2.hasNext(); ) referencedEvents.add(getEventById(it2.next().getId()));
            Properties params = new Properties();
            if (process.getParams() != null) params = parseParams(process.getParams().getParam().iterator());
            Class<Process> processHandler = (Class<Process>) this.getClass().getClassLoader().loadClass(process.getHandler());
            Process configProcess = processHandler.getConstructor(new Class[] { String.class, Properties.class, ArrayList.class, ArrayList.class }).newInstance(new Object[] { process.getId(), params, referencedNodes, referencedEvents });
            if (process.getLogger() != null) {
                configProcess.setLoggerLevel(process.getLogger().getLevel());
                configProcess.setLoggerPathPrefix(process.getLogger().getPathPrefix());
            }
            processes.add(configProcess);
            for (Iterator<it.unipr.ce.dsg.deus.schema.Reference> it2 = process.getEvents().getReference().iterator(); it2.hasNext(); ) {
                Event e = getEventById(it2.next().getId());
                if (e.getParentProcess() == null) e.setParentProcess(configProcess); else throw new JAXBException("Event " + e.getId() + " is already associated to the process " + e.getParentProcess().getId());
            }
        }
        ArrayList<Process> referencedProcesses = new ArrayList<Process>();
        for (Iterator<it.unipr.ce.dsg.deus.schema.Reference> it = automator.getEngine().getProcesses().getReference().iterator(); it.hasNext(); ) {
            referencedProcesses.add(getProcessById(it.next().getId()));
        }
        engine = new Engine(automator.getEngine().getMaxvt(), automator.getEngine().getSeed(), automator.getEngine().getKeyspacesize(), nodes, events, processes, referencedProcesses);
        if (automator.getEngine().getLogger() != null) {
            engine.setLoggerLevel(automator.getEngine().getLogger().getLevel());
            engine.setLoggerPathPrefix(automator.getEngine().getLogger().getPathPrefix());
        }
    }

    /**
	 * Returns the configured node with the given id.
	 * 
	 * @param id
	 *            the id of the node to extract.
	 * @return the configured node with the given id.
	 */
    private Node getNodeById(String id) {
        for (Iterator<Node> it = nodes.iterator(); it.hasNext(); ) {
            Node n = it.next();
            if (n.getId().equals(id)) return n;
        }
        return null;
    }

    /**
	 * Returns the configured event with the given id.
	 * 
	 * @param id
	 *            the id of the event to extract.
	 * @return the configured event with the given id.
	 */
    private Event getEventById(String id) {
        for (Iterator<Event> it = events.iterator(); it.hasNext(); ) {
            Event e = it.next();
            if (e.getId().equals(id)) return e;
        }
        return null;
    }

    /**
	 * Returns the configured process with the given id.
	 * 
	 * @param id
	 *            the id of the process to extract.
	 * @return the configured process with the given id.
	 */
    private Process getProcessById(String id) {
        for (Iterator<Process> it = processes.iterator(); it.hasNext(); ) {
            Process p = it.next();
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    /**
	 * Returns a Properties object
	 * 
	 * @param id
	 *            the id of the node to extract.
	 * @return the configured node with the given id.
	 */
    private Properties parseParams(Iterator<Param> it) {
        Properties params = new Properties();
        while (it.hasNext()) {
            Param param = it.next();
            params.setProperty(param.getName(), param.getValue());
        }
        return params;
    }

    /**
	 * Returns the instance of the simulation engine, initialized by the class
	 * constructor.
	 * 
	 * @return the instance of the simulation engine.
	 */
    public Engine getEngine() {
        return engine;
    }
}
