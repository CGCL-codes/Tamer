package org.webthree.cocoon.components.modules.input;

import java.util.Iterator;
import java.util.Map;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.modules.input.InputModule;
import org.apache.regexp.RE;

/**
 * @author michael.gerzabek@gmx.net
 * 
 */
public class FormModule extends AbstractLogEnabled implements Serviceable, InputModule, ThreadSafe {

    private ServiceManager manager;

    public void service(ServiceManager manager) throws ServiceException {
        this.manager = manager;
    }

    public Object getAttribute(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
        RE re = new RE("(.*):([0-9]{12}):(.*)");
        if (re.match(name)) {
            String key = "urn:auftrag" + re.getParen(2);
            Parameters parameters = new Parameters();
            parameters.setParameter("orderid", re.getParen(2));
            try {
            } catch (IllegalArgumentException iae) {
                throw new ConfigurationException(iae.getMessage(), iae);
            }
        }
        return null;
    }

    public Iterator getAttributeNames(Configuration modeConf, Map objectModel) throws ConfigurationException {
        throw new ConfigurationException("This function is not yet implemented!");
    }

    public Object[] getAttributeValues(String name, Configuration modeConf, Map objectModel) throws ConfigurationException {
        throw new ConfigurationException("This function is not yet implemented!");
    }
}
