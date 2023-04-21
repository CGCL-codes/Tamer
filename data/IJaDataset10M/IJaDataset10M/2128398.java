package examples.wslauncher3;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import de.fhg.igd.logging.Logger;
import de.fhg.igd.logging.LoggerFactory;
import de.fhg.igd.semoa.security.AgentStructure;
import de.fhg.igd.semoa.server.AgentLauncher;
import de.fhg.igd.semoa.server.Environment;
import de.fhg.igd.semoa.service.AbstractService;
import de.fhg.igd.util.MemoryResource;
import de.fhg.igd.util.Resource;
import de.fhg.igd.util.WhatIs;

/** 
 * This service launches an agent via webservice.
 * Publish -class examples.wslauncher3.DelegationServiceImpl -key ${WhatIs:DELEGATION_SERVICE}
 * 
 * @author C. Nickel
 * @version $Id: DelegationServiceImpl.java 1913 2007-08-08 02:41:53Z jpeters $
 */
public class DelegationServiceImpl extends AbstractService implements DelegationService {

    /**
     * The <code>Logger</code> instance for this class
     */
    private static Logger log_ = LoggerFactory.getLogger("webservice");

    /**
	 * Length of the token which is used to synchronize agent and 
	 * webservice. The token is a random string.
	 */
    private static int TOKEN_LENGTH = 10;

    /**
	 * The class of the agent which shall be launched
	 */
    private static String AGENT_ = "examples.wslauncher3.Agent2";

    /**
	 * Additional package prefixes which shall not be imported into the agent
	 */
    private static String ADDITIONAL_EXCLUDES = "org.apache.*";

    /**
	 * Timeout for each thread to wait for the agent's return.
	 */
    private long TIMEOUT = 100000;

    /**
	 * The syncmap_ is needed to coordinate the webservices and the agents. 
	 * When several webservices are running it ensures that the agent gives 
	 * his response to the right one. 
	 * The keys in the syncmap_ are the different tokens which are also stored
	 * in the properties of the agent. The values are the answers reported by
	 * the agents.
	 */
    private Map syncmap_;

    /** 
	 * Constructor
	 */
    public DelegationServiceImpl() {
        syncmap_ = new HashMap();
    }

    /**
	 * This method generates a token and stores it in the agent's properties
	 * as well as in the syncmap_. 
	 * It launches the agent named in AGENT_.
	 * DelegationService and agent are synchronized via the token.
	 * 
	 * @param wsurl 
	 * 			the url of the webservice
	 * @param method
	 * 			the method to invoke at the webservice
	 * @param param
	 * 			an Object[] with the parameters which are necessary to invoke  
	 * 			this method at the webservice 
	 * 
	 * @return answ
	 * 			the answer given by the AGENT_
	 * 
	 * @see examples.wslauncher3.DelegationService#delegate(String, String, Object[])
	 */
    public String delegate(String wsurl, String method, Object[] params) {
        PrivilegedLaunchAction action;
        String token;
        String answ;
        int i;
        token = "";
        for (i = 0; i < TOKEN_LENGTH; i++) {
            token = token + (char) (Math.random() * 26 + 65);
        }
        syncmap_.put(token, null);
        System.out.println("----- Token for synchronization of DelegationService and Agent2 = " + token + "------");
        log_.info("Token for synchronization of DelegationService and Agent2 = " + token);
        action = new PrivilegedLaunchAction(wsurl, method, params, token);
        try {
            AccessController.doPrivileged(action);
        } catch (Exception ex) {
            System.out.println("Could not launch agent: " + ex);
            log_.error("Could not launch agent: " + ex);
        }
        answ = "----- Did not get an answer from the agent2! -----";
        try {
            synchronized (token) {
                token.wait(TIMEOUT);
                answ = (String) syncmap_.get(token);
                syncmap_.remove(token);
                System.out.println("----- DelegationService got the answer: " + answ + " -----");
                log_.info("DelegationService got the answer: " + answ);
            }
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
        return answ;
    }

    /** 
	 * Checks if the token is a key in the syncmap. The method then stores 
	 * the answer under this key and notifies the waiting delegation service.
	 *
	 * @param answ
	 * 			answer from the agent2
	 * @param token
	 * 			token for the synchronization of Agent2 and the
	 * 			delegation service
	 * 
	 * @see examples.wslauncher3.DelegationService#response(String, String)
	 */
    public void response(String answ, String token) {
        Iterator i;
        String key;
        if (!syncmap_.containsKey(token) || token == "") {
            System.out.println("Can't find the token in the map!");
            log_.error("Can't find the token in the map!");
            return;
        }
        if (answ == "") {
            System.out.println("I do not have an answer");
            log_.error("I do not have an answer");
            return;
        }
        i = syncmap_.keySet().iterator();
        while (i.hasNext()) {
            key = (String) i.next();
            if (key.equals(token)) {
                syncmap_.put(key, answ);
                synchronized (key) {
                    key.notify();
                    System.out.println("----- Agent2 has notified the delegation service (" + key + ")! -----");
                    log_.info("Agent2 has notified the delegation service (" + key + ")!");
                }
                return;
            }
        }
        System.out.println("Sorry, could not find a matching token.");
        log_.error("Sorry, could not find a matching token.");
    }

    public String info() {
        return "This service launches an agent via webservice.";
    }

    public String author() {
        return "Claudia Nickel";
    }

    public String revision() {
        return "$id$";
    }

    /**
	 * This action launches the agent. Due to security reasons it 
	 * is called above within a doPrivileged() statement.
	 * 
	 */
    private class PrivilegedLaunchAction implements PrivilegedExceptionAction {

        Object[] param_;

        String method_;

        String token_;

        String wsurl_;

        public PrivilegedLaunchAction(String wsurl, String method, Object[] param, String token) {
            method_ = method;
            wsurl_ = wsurl;
            param_ = param;
            token_ = token;
        }

        public Object run() throws Exception {
            AgentLauncher l;
            Environment env;
            Properties prop;
            Resource res;
            String deflt;
            String key;
            int i;
            prop = AgentStructure.defaults();
            prop.setProperty(AgentStructure.PROP_AGENT_CLASS, AGENT_);
            prop.setProperty("wsurl", wsurl_);
            prop.setProperty("token", token_);
            prop.setProperty("method", method_);
            deflt = prop.getProperty(AgentStructure.PROP_AGENT_EXCLUDE);
            prop.setProperty(AgentStructure.PROP_AGENT_EXCLUDE, deflt + ":" + ADDITIONAL_EXCLUDES);
            for (i = 0; i < param_.length; i++) {
                prop.setProperty("param" + i, param_[i].toString());
            }
            res = new MemoryResource();
            env = Environment.getEnvironment();
            key = WhatIs.stringValue(AgentLauncher.WHATIS);
            l = (AgentLauncher) env.lookup(key);
            if (l == null) {
                log_.severe("Can't find the agent launcher");
                throw new PrivilegedActionException(new NullPointerException("Can't find the agent launcher"));
            }
            try {
                l.launchAgent(res, prop);
            } catch (Exception ex) {
                throw new PrivilegedActionException(ex);
            }
            return null;
        }
    }
}
