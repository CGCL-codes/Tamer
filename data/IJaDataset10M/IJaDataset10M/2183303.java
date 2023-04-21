package hermes.ext;

import hermes.HermesAdminFactory;
import hermes.config.ProviderExtConfig;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.NamingException;

/**
 * @author colincrist@hermesjms.com
 * @version $Id: ExtensionFinder.java,v 1.7 2004/09/16 20:30:49 colincrist Exp $
 */
public interface ExtensionFinder {

    public HermesAdminFactory createExtension(String classPathId, ProviderExtConfig extConfig, ConnectionFactory cf) throws InstantiationException, IllegalAccessException, ClassNotFoundException, NamingException, JMSException;
}
