package de.iritgo.aktera.base.session;

import de.iritgo.aktera.authentication.UserEnvironment;
import de.iritgo.aktera.authorization.AuthorizationException;
import de.iritgo.aktera.core.exception.NestedException;
import de.iritgo.aktera.crypto.Encryptor;
import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.model.ModelRequest;
import de.iritgo.aktera.model.ModelResponse;
import de.iritgo.aktera.model.StandardLogEnabledModel;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 */
public abstract class LoginBase extends StandardLogEnabledModel {

    private static String DOMAIN_COOKIE_NAME = "keel-domain";

    private static String LOGIN_COOKIE_NAME = "keel-user";

    private static String PASSWORD_COOKIE_NAME = "keel-password";

    protected String decodeWithSeq(String[] seq, String original, ModelRequest req) throws ModelException {
        if (seq == null) {
            return original;
        }
        String current = original;
        for (int i = seq.length - 1; i >= 0; i--) {
            String oneSeq = seq[i];
            if (oneSeq.indexOf(".") <= 0) {
                throw new IllegalArgumentException(oneSeq + " is not a valid encryption configuration");
            }
            String service = oneSeq.substring(0, oneSeq.indexOf("."));
            String method = oneSeq.substring(oneSeq.indexOf(".") + 1);
            try {
                Encryptor oneEncryptor = (Encryptor) req.getService(Encryptor.ROLE, service);
                if (method.equals("encrypt")) {
                    current = new String(oneEncryptor.decrypt(current.getBytes("UTF-8")), "UTF-8");
                } else if (method.equals("hash")) {
                    current = new String(oneEncryptor.hash(current.getBytes("UTF-8")), "UTF-8");
                } else {
                    throw new ModelException("Method '" + method + "' invalid.");
                }
            } catch (UnsupportedEncodingException ue) {
                throw new ModelException(ue);
            } catch (NestedException ne) {
                throw new ModelException(ne);
            }
        }
        return current;
    }

    protected String encodeWithSeq(String[] seq, String original, ModelRequest req) throws ModelException {
        if (seq == null) {
            return original;
        }
        String current = original;
        if (current == null) {
            current = "";
        }
        for (int i = 0; i < seq.length; i++) {
            String oneSeq = seq[i];
            if (oneSeq.indexOf(".") <= 0) {
                throw new IllegalArgumentException(oneSeq + " is not a valid encryption configuration");
            }
            String service = oneSeq.substring(0, oneSeq.indexOf("."));
            String method = oneSeq.substring(oneSeq.indexOf(".") + 1);
            try {
                Encryptor oneEncryptor = (Encryptor) req.getService(Encryptor.ROLE, service);
                if (method.equals("encrypt")) {
                    current = new String(oneEncryptor.encrypt(current.getBytes("UTF-8")), "UTF-8");
                } else if (method.equals("hash")) {
                    current = new String(oneEncryptor.hash(current.getBytes("UTF-8")), "UTF-8");
                } else {
                    throw new ModelException("Method '" + method + "' invalid.");
                }
            } catch (UnsupportedEncodingException ue) {
                throw new ModelException(ue);
            } catch (NestedException ne) {
                throw new ModelException(ne);
            }
        }
        return current;
    }

    protected String getCookieName(Configuration conf, String name) {
        String defaultName = null;
        if (name.equals("domain")) {
            defaultName = DOMAIN_COOKIE_NAME;
        } else if (name.equals("login")) {
            defaultName = LOGIN_COOKIE_NAME;
        } else if (name.equals("password")) {
            defaultName = PASSWORD_COOKIE_NAME;
        } else {
            throw new IllegalArgumentException("'" + name + "' unknown");
        }
        if (conf != null) {
            Configuration cookieNames = conf.getChild("cookie-names");
            if (cookieNames != null) {
                return cookieNames.getAttribute(name, defaultName);
            } else {
                log.debug("No cookie names specified - using defaults");
            }
        }
        log.warn("No configuration for login model - using default cookie names");
        return defaultName;
    }

    /**
	 * Return the sequence of cryptographic protections used for
	 * a given element
	 */
    protected String[] getCryptSeq(Configuration conf, String whichOne) {
        if (conf == null) {
            log.debug("No configuration for login encryption sequence for '" + whichOne + "'");
            return null;
        }
        Configuration seq = conf.getChild(whichOne);
        Configuration[] children = seq.getChildren();
        if (children.length == 0) {
            log.debug("No sequence found for '" + whichOne + "'");
        }
        ArrayList seqList = new ArrayList();
        for (int i = 0; i < children.length; i++) {
            Configuration oneChild = children[i];
            if (oneChild.getName().equals("seq")) {
                seqList.add(oneChild.getValue(""));
            }
        }
        String[] returnList = new String[seqList.size()];
        int j = 0;
        for (Iterator ie = seqList.iterator(); ie.hasNext(); ) {
            returnList[j] = (String) ie.next();
            if (log.isDebugEnabled()) {
                log.debug("Encrypt seq " + j + ", " + returnList[j]);
            }
            j++;
        }
        return returnList;
    }

    protected String getDomainCookieName(Configuration conf) {
        return getCookieName(conf, "domain");
    }

    protected String getLoginCookieName(Configuration conf) {
        return getCookieName(conf, "login");
    }

    protected String getPasswordCookieName(Configuration conf) {
        return getCookieName(conf, "password");
    }

    protected ModelResponse createLoginInfoOutput(ModelRequest req, ModelResponse res) throws ModelException {
        try {
            String loginName = "";
            Context c = req.getContext();
            if (c != null) {
                UserEnvironment ue = (UserEnvironment) c.get(UserEnvironment.CONTEXT_KEY);
                try {
                    loginName = ue.getLoginName();
                } catch (AuthorizationException e) {
                    throw new ModelException("Authorization error", e);
                }
                res.addOutput("logininfo", "Logged in as " + loginName);
            } else {
                throw new ModelException("Unable to get user-info from context, context was null");
            }
        } catch (ContextException ce) {
            log.debug("Unable to access user environment from context ");
        }
        return res;
    }
}
