package uk.ac.ox.oucs.trecx.reporting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import uk.ac.ox.oucs.trecx.core.jaxb.Event;
import uk.ac.ox.oucs.trecx.core.jaxb.EventList;
import java.util.Properties;
import java.util.Collections;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;

/**
 * Class intended for reporting from tracking stores. Instances of this class
 * should be capable of querying one or more configured tracking stores. This
 * class is intended to be used as a singleton instance.
 * @author Alexis O'Connor
 */
public abstract class Reporter {

    private static Reporter _INSTANCE = null;

    private static Logger loggerConfig = Logger.getLogger("TReCX.Configuration");

    protected List<String> hosts;

    protected JAXBContext jc;

    /**
     * Get a singleton instance of this class.
     * @return a singleton instance of this class.
     */
    public static Reporter getReporter() {
        if (_INSTANCE == null) {
            _INSTANCE = new uk.ac.ox.oucs.trecx.reporting.impl.ReporterImpl();
            _INSTANCE.configure();
        }
        return _INSTANCE;
    }

    /**
     * Configure the implicit instance. At the moment this method expects to
     * find a file called <code>reporting.properties</code> present on the
     * <code>CLASSPATH</code> from which to configure the instance.
     * @param reporter the instance to be configured.
     */
    protected void configure() {
        Properties properties = new Properties();
        try {
            properties.load(getClass().getResourceAsStream("/reporting.properties"));
        } catch (IOException e) {
            loggerConfig.error(e.getMessage());
            return;
        }
        ArrayList<String> list = new ArrayList<String>();
        Enumeration e = properties.propertyNames();
        while (e.hasMoreElements()) {
            String s = (String) e.nextElement();
            if (s.startsWith("trecx.store")) {
                list.add(properties.getProperty(s));
            }
        }
        hosts = Collections.unmodifiableList(list);
        try {
            jc = JAXBContext.newInstance(Event.class.getPackage().getName());
        } catch (JAXBException e1) {
            loggerConfig.error("Could not create the JAXB context.");
        }
    }

    /**
     * Get the list of hosts that this instance reports from.
     * @return the list of hosts that this instance reports from.
     */
    public List<String> getHosts() {
        return hosts;
    }

    /**
     * Get the JAXB context object for this object. This object is used for
     * marshalling and unmarshalling between XML and JAXB instances. (This 
     * method is present in this class because if it can not be instantiated 
     * this implies a fundamental configuration error. The choice to use JAXB
     * is a fundamental design decision and requirement of this class).
     * @return the JAXB context object for this object.
     */
    protected JAXBContext getJAXBContext() {
        return jc;
    }

    /**
     * Query tracking store(s) for events within a specified date range.
     * @param begin the start date (inclusive) (can be <code>null</code>).
     * @param end the end date (exclusive) (can be <code>null</code>).
     * @return an event list corresponding to the query.
     */
    public abstract EventList queryDates(Date begin, Date end);

    /**
     * Query tracking store(s) for events matching a specified user.
     * @param userID the ID of the user of interest.
     * @param begin the start date (inclusive) (can be <code>null</code>).
     * @param end the end date (exclusive) (can be <code>null</code>).
     * @return an event list corresponding to the query.
     */
    public abstract EventList queryUser(String userID, Date begin, Date end);

    /**
     * Query tracking store(s) for events matching a specified application.
     * @param applicatioID the ID of the application of interest.
     * @param begin the start date (inclusive) (can be <code>null</code>).
     * @param end the end date (exclusive) (can be <code>null</code>).
     * @return an event list corresponding to the query.
     */
    public abstract EventList queryApplication(String applicationID, Date begin, Date end);
}
