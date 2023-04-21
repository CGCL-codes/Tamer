package hu.sztaki.lpds.pgportal.services.is.mds2.ldap;

import hu.sztaki.lpds.pgportal.services.is.*;
import hu.sztaki.lpds.pgportal.services.is.mds2.MDSResource;
import hu.sztaki.lpds.pgportal.services.is.mds2.MDSInformationSystem;
import hu.sztaki.lpds.pgportal.services.utils.MiscUtils;
import hu.sztaki.lpds.pgportal.services.pgrade.GridConfiguration;
import javax.naming.*;
import javax.naming.directory.*;

/**
  *
  * @author  boci
  */
public class MDSGiisChecker {

    /** Creates a new instance of MDSRessourceListManager */
    public MDSGiisChecker() {
    }

    public static boolean isMDSGiisAvailable(String aGiisHost, String aGiisPort, String aGiisBaseDn) {
        java.util.Hashtable env = new java.util.Hashtable();
        try {
            env.put(Context.PROVIDER_URL, "ldap://" + aGiisHost + ":" + aGiisPort);
            env.put(Context.SECURITY_AUTHENTICATION, "simple");
            env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
            env.put("com.sun.jndi.ldap.connect.timeout", "30");
            DirContext ctx = new InitialDirContext(env);
            String[] retAttr = new String[] { "Mds-Host-hn" };
            SearchControls controls = new SearchControls();
            controls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
            controls.setReturningAttributes(retAttr);
            MiscUtils.printlnLog(MDSGiisChecker.class.getName() + ".isMDSGiisAvailable(" + aGiisHost + ")", "Check STARTING.");
            DirContext dC = ctx.getSchema(aGiisBaseDn);
            MiscUtils.printlnLog(MDSGiisChecker.class.getName() + ".isMDSGiisAvailable(" + aGiisHost + ")", "Check FINISHED.");
            ctx.close();
            return true;
        } catch (javax.naming.CommunicationException e) {
            MiscUtils.printlnLog(MDSGiisChecker.class.getName() + ".isMDSGiisAvailable(" + aGiisHost + ")", "CommunicationException was occured." + e.getMessage());
            return false;
        } catch (javax.naming.NamingException e) {
            MiscUtils.printlnLog(MDSGiisChecker.class.getName() + ".isMDSGiisAvailable(" + aGiisHost + ")", "-NamingException was occured: " + e.getMessage());
            return false;
        }
    }
}
