package olr;

import java.io.IOException;
import olr.rdf.Definitions;
import olr.rdf.OLR3Definitions;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.apache.torque.Torque;
import com.lutris.appserver.server.ApplicationException;
import com.lutris.appserver.server.StandardApplication;
import com.lutris.appserver.server.httpPresentation.HttpPresentationComms;
import com.lutris.util.Config;

/**
 * @version $Id: Olr.java,v 1.11 2004/08/02 19:01:18 roku Exp $
 */
public final class Olr extends StandardApplication {

    private static final String TORQUE_PROPERTIES_FILENAME = "Torque.properties";

    public void startup(Config appConfig) throws ApplicationException {
        super.startup(appConfig);
        Definitions.setValuesFromConfig();
        OLR3Definitions.setValuesFromConfig();
        try {
            Olr.initTorque();
        } catch (Exception e) {
            throw new ApplicationException(e);
        }
    }

    public boolean requestPreprocessor(HttpPresentationComms comms) throws Exception {
        return super.requestPreprocessor(comms);
    }

    public static void initTorque() throws Exception {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration();
            config.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(TORQUE_PROPERTIES_FILENAME));
            Torque.init(config);
        } catch (IOException e) {
            e.fillInStackTrace();
            Logger.getLogger(Olr.class).error("Exception setting up torque: " + e);
            throw new Exception(e.toString());
        }
    }
}
