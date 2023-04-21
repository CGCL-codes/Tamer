package gov.lanl.SSLTools.adiron;

import com.adiron.SecurityLevel3.SecurityCurrent;
import com.adiron.SecurityLevel3.Principal;
import com.adiron.SecurityLevel3.PT_Simple;
import com.adiron.SecurityLevel3.PT_Quoting;
import com.adiron.SecurityLevel3.QuotingPrincipal;
import gov.lanl.Utility.NamedValues;
import gov.lanl.SSLTools.AbsAccessDecision;
import gov.lanl.SSLTools.ServiceFactory;
import org.apache.log4j.Logger;
import java.util.Properties;

/**
 * Implements a sample accesses decision object for adiron's
 * implementation of Corba Security CSIv2
 * @author Jim George
 * @version $Revision: 3336 $ $Date: 2006-03-27 18:45:32 -0500 (Mon, 27 Mar 2006) $
 */
public class AccessDecisionShow extends AbsAccessDecision {

    private static Logger cat = Logger.getLogger(AccessDecisionShow.class.getName());

    /**The constructor.
     */
    public AccessDecisionShow() {
    }

    public boolean access_allowed(String operation) {
        cat.debug("Access check for operation : " + operation);
        Properties p = ServiceFactory.getSSLService().getClientData();
        NamedValues nval = new NamedValues(p);
        cat.debug("Client is :" + nval.toString());
        return true;
    }
}
