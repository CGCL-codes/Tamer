package verinec.netsim;

import java.io.File;
import java.io.IOException;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import verinec.VerinecException;
import verinec.data.repository.IVerinecRepository;
import verinec.netsim.loggers.events.Events;
import verinec.netsim.loggers.events.EventsJDOMFactory;
import verinec.util.LocalSAXBuilder;
import verinec.util.SchemaValidator;

/**
 * The static simulator does not simulate anything. 
 *
 * It just returns the file
 * httpoutput.xml from the examples folder as Elements.
 * 
 * @author Dominik Jungo
 * @version $Revision: 47 $
 */
public class StaticSimulation implements ISimulation {

    private IVerinecRepository repository;

    private Events inputevents;

    private double stoptime;

    /**
	 * creates a static simulation. 
	 *
	 * The parameters here are just used to be
	 * compatible with the Simulation class. They are not used.
	 * 
	 * @param repository
	 *            a repository
	 * @param inputevents
	 *            inputevents
	 * @param stoptime
	 *            a stoptime
	 */
    public StaticSimulation(IVerinecRepository repository, Events inputevents, double stoptime) {
        super();
        setRepository(repository);
        setInputEvents(inputevents);
        setStopTime(stoptime);
    }

    /**
	 * creates a static simulation. The parameters here are just used to be
	 * compatible with the Simulation class. They are not used.
	 * 
	 * @param repository
	 *            a repository
	 * @param inputevents
	 *            inputevents
	 *  
	 */
    public StaticSimulation(IVerinecRepository repository, Events inputevents) {
        super();
        setRepository(repository);
        setInputEvents(inputevents);
    }

    /**
	 * creates an static simulation.
	 */
    public StaticSimulation() {
        super();
    }

    /**
	 * @throws NetSimException
	 * @see verinec.netsim.ISimulation#start()
	 */
    public Events start() throws NetSimException {
        String name = "httpoutput.xml";
        Events result = null;
        try {
            SAXBuilder xmlbuilder = LocalSAXBuilder.instance();
            xmlbuilder.setFactory(new EventsJDOMFactory());
            result = (Events) xmlbuilder.build(new File(name)).getRootElement();
            result.detach();
        } catch (JDOMException je) {
            je.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try {
            SchemaValidator validator = new SchemaValidator();
            if (!validator.validate(result.getDocument())) {
                throw new NetSimException("Invalid Result Events Document");
            }
        } catch (VerinecException e) {
            e.printStackTrace();
            throw new NetSimException("Validation of the Result Events Document failed", e);
        }
        try {
            new XMLOutputter(Format.getPrettyFormat()).output(result.getDocument(), System.out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
	 * @see verinec.netsim.ISimulation#setRepository(verinec.data.repository.IVerinecRepository)
	 */
    public void setRepository(IVerinecRepository repository) {
        this.repository = repository;
    }

    /**
	 * @see verinec.netsim.ISimulation#setInputEvents(verinec.netsim.loggers.events.Events)
	 */
    public void setInputEvents(Events events) {
        this.inputevents = events;
    }

    /**
	 * @see verinec.netsim.ISimulation#setStopTime(double)
	 */
    public void setStopTime(double time) {
        this.stoptime = time;
    }

    /**
	 * @see verinec.netsim.ISimulation#getStopTime()
	 */
    public double getStopTime() {
        return stoptime;
    }

    /**
	 * @see verinec.netsim.ISimulation#getRepository()
	 */
    public IVerinecRepository getRepository() {
        return repository;
    }

    /**
	 * @see verinec.netsim.ISimulation#getInputEvents()
	 */
    public Events getInputEvents() {
        return inputevents;
    }
}
