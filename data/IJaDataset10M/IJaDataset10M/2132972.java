package hermes.ext.arjuna;

import hermes.Hermes;
import hermes.HermesAdmin;
import hermes.HermesAdminFactory;
import hermes.HermesException;
import hermes.JNDIConnectionFactory;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 * Administration plugin for ArjunaMS.
 * 
 * @author arnaud.simon@arjuna.com, colincrist@hermesjms.com  last changed by: $Author: colincrist $
 * @version $Id: ArjunaMSAdminFactory.java,v 1.5 2005/05/01 11:23:53 colincrist Exp $
 */
public class ArjunaMSAdminFactory implements HermesAdminFactory {

    private static final Logger log = Logger.getLogger(ArjunaMSAdminFactory.class);

    private String adminBinding = "Admin";

    public ArjunaMSAdminFactory() {
        super();
    }

    public HermesAdmin createSession(Hermes hermes, ConnectionFactory connectionFactory) throws JMSException, NamingException {
        if (connectionFactory instanceof JNDIConnectionFactory) {
            JNDIConnectionFactory jndiCF = (JNDIConnectionFactory) connectionFactory;
            return new ArjunaMSAdmin(hermes, this, jndiCF.createContext());
        } else {
            throw new HermesException("Provider is not ArjunaMS");
        }
    }

    /**
     * Gets the name of the binding in JNDI to find the Admin interface.
     * 
     * @return
     */
    public String getAdminBinding() {
        return adminBinding;
    }

    /**
     * Sets the name of the binding in JNDI to find the Admin interface.
     * @param adminBinding
     */
    public void setAdminBinding(String adminBinding) {
        this.adminBinding = adminBinding;
    }
}
