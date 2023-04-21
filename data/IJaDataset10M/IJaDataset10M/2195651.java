package backend.logging;

import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import backend.event.GraphEvent;
import backend.event.GraphListener;
import backend.event.MappingEvent;
import backend.event.MappingListener;
import backend.event.ONDEXEvent;
import backend.event.ONDEXListener;
import backend.event.ParserEvent;
import backend.event.ParserListener;
import backend.event.ValidatorEvent;
import backend.event.ValidatorListener;

public class ONDEXLogger implements ONDEXListener, GraphListener, ParserListener, MappingListener, ValidatorListener {

    /**
	 * This class outputs logging messages using Log4j.
	 *
	 */
    public ONDEXLogger() {
        PropertyConfigurator.configure(System.getProperty("ondex.dir") + File.separator + "log4j.properties");
        Logger logger = Logger.getLogger(this.getClass());
        logger.info("ONDEX logger initialized");
    }

    /**
	 * Captures all events and writes them to Log4j.
	 * 
	 */
    public void eventOccurred(ONDEXEvent e) {
        Logger logger = Logger.getLogger(e.getSource().getClass());
        String mesg = e.getEventType().getDescription() + " " + e.getEventType().getMessage();
        logger.log(e.getEventType().getLog4jLevel(), mesg);
    }

    public void eventOccurred(GraphEvent e) {
        eventOccurred((ONDEXEvent) e);
    }

    public void eventOccurred(ParserEvent e) {
        eventOccurred((ONDEXEvent) e);
    }

    public void eventOccurred(MappingEvent e) {
        eventOccurred((ONDEXEvent) e);
    }

    public void eventOccurred(ValidatorEvent e) {
        eventOccurred((ONDEXEvent) e);
    }
}
