package org.cilogon.service.config.models;

import edu.uiuc.ncsa.security.core.exceptions.ConfigurationException;
import edu.uiuc.ncsa.security.core.exceptions.NotImplementedException;
import edu.uiuc.ncsa.security.rdf.ConfigurationThing;
import edu.uiuc.ncsa.security.rdf.MyThing;
import edu.uiuc.ncsa.security.rdf.MyThingSession;
import org.cilogon.service.util.Namespaces;
import org.cilogon.util.exceptions.CILogonException;
import org.tupeloproject.kernel.OperatorException;
import org.tupeloproject.kernel.Thing;
import org.tupeloproject.rdf.Resource;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import static org.cilogon.service.config.rdf.ServiceVocabulary.*;
import static org.cilogon.util.CILogonUriRefFactory.uriRef;

/**
 * <p>Created by Jeff Gaynor<br>
 * on Jun 21, 2010 at  11:01:10 AM
 */
public class TokenFactoryModel extends ConfigurationThing {

    public TokenFactoryModel(MyThingSession thingSession, Resource resource) throws OperatorException {
        super(thingSession, resource);
    }

    public void inject(Namespaces namespaces) {
        try {
            namespaces.setAccessToken(getAccessToken());
            namespaces.setAccessTokenSS(getAccessTokenSS());
            namespaces.setTempCred(getTempCred());
            namespaces.setTempCredSS(getTempCredSS());
            namespaces.setTransactionToken(getTransactionToken());
            namespaces.setUserString(getUserToken());
            namespaces.setVerifier(getVerifier());
            namespaces.setDomainString(getDomain());
            namespaces.setServerString(getServer());
            namespaces.setPrefixToNS(getXMLNS());
        } catch (OperatorException e) {
            throw new ConfigurationException("Error: could net set configuration", e);
        }
    }

    public void inject(Object bean) {
        throw new NotImplementedException();
    }

    public void remove() {
        try {
            for (Resource t : getTypes()) {
                removeType(t);
            }
            if (getLabel() != null) {
                removeLabel(getLabel());
            }
            removeValue(TEMP_CRED, getTempCred());
            removeValue(TEMP_CRED_SHARED_SECRET, getTempCredSS());
            removeValue(ACCESS_TOKEN, getAccessToken());
            removeValue(ACCESS_TOKEN_SHARED_SECRET, getAccessTokenSS());
            removeValue(VERIFIER, getVerifier());
            removeValue(USER_TOKEN, getUserToken());
            removeValue(TRANSACTION_TOKEN, getTransactionToken());
            removeValue(DOMAIN_TOKEN, getDomain());
            removeValue(SERVER_TOKEN, getServer());
            ArrayList<XMLNSToken> tokens = new ArrayList<XMLNSToken>();
            fetchThingsByPredicate(HAS_XMLNS_TOKEN, XMLNSToken.class, tokens);
            for (XMLNSToken token : tokens) {
                token.remove();
                removeValue(HAS_XMLNS_TOKEN, token.getSubject());
            }
        } catch (OperatorException ox) {
            throw new CILogonException("Unable to complete remove of values", ox);
        }
    }

    public String getTempCred() throws OperatorException {
        return getString(TEMP_CRED);
    }

    public void setTempCred(String tempCred) throws OperatorException {
        setValue(TEMP_CRED, tempCred);
    }

    public String getTempCredSS() throws OperatorException {
        return getString(TEMP_CRED_SHARED_SECRET);
    }

    public void setTempCredSS(String tempCredSS) throws OperatorException {
        setValue(TEMP_CRED_SHARED_SECRET, tempCredSS);
    }

    public String getAccessToken() throws OperatorException {
        return getString(ACCESS_TOKEN);
    }

    public void setAccessToken(String accessToken) throws OperatorException {
        setValue(ACCESS_TOKEN, accessToken);
    }

    public String getAccessTokenSS() throws OperatorException {
        return getString(ACCESS_TOKEN_SHARED_SECRET);
    }

    public void setAccessTokenSS(String accessTokenSS) throws OperatorException {
        setValue(ACCESS_TOKEN_SHARED_SECRET, accessTokenSS);
    }

    public String getVerifier() throws OperatorException {
        return getString(VERIFIER);
    }

    public void setVerfier(String verfier) throws OperatorException {
        setValue(VERIFIER, verfier);
    }

    public String getUserToken() throws OperatorException {
        return getString(USER_TOKEN);
    }

    public void setUserToken(String userToken) throws OperatorException {
        setValue(USER_TOKEN, userToken);
    }

    public String getTransactionToken() throws OperatorException {
        return getString(TRANSACTION_TOKEN);
    }

    public void setTransactionToken(String transactionToken) throws OperatorException {
        setValue(TRANSACTION_TOKEN, transactionToken);
    }

    public String getDomain() throws OperatorException {
        return getString(DOMAIN_TOKEN);
    }

    public void setDomain(String domain) throws OperatorException {
        setValue(DOMAIN_TOKEN, domain);
    }

    public String getServer() throws OperatorException {
        return getString(SERVER_TOKEN);
    }

    public void setServer(String server) throws OperatorException {
        setValue(SERVER_TOKEN, server);
    }

    public HashMap<String, URI> getXMLNS() throws OperatorException {
        HashMap<String, URI> hashMap = new HashMap<String, URI>();
        Collection<Thing> tokens = getThings(HAS_XMLNS_TOKEN);
        for (Thing t : tokens) {
            hashMap.put(t.getString(XMLNS_PREFIX), t.getUri(XMLNS_NAMESPACE));
        }
        return hashMap;
    }

    public class XMLNSToken extends MyThing {

        public XMLNSToken(Thing t) {
            super(t);
        }

        public XMLNSToken(MyThingSession thingSession, Resource resource) {
            super(thingSession, resource);
        }

        public void setPrefix(String prefix) throws OperatorException {
            setValue(XMLNS_PREFIX, prefix);
        }

        public String getPrefix() throws OperatorException {
            return getString(XMLNS_PREFIX);
        }

        public void setURI(URI uri) throws OperatorException {
            setValue(XMLNS_NAMESPACE, uri);
        }

        public URI getURI() throws OperatorException {
            return getUri(XMLNS_NAMESPACE);
        }

        public void remove() {
            try {
                for (Resource t : getTypes()) {
                    removeType(t);
                }
                removeValue(XMLNS_NAMESPACE, getURI());
                removeValue(XMLNS_PREFIX, getPrefix());
            } catch (OperatorException x) {
                throw new CILogonException("Error removing XML namespace value", x);
            }
        }
    }

    /**
     * There may be many namespace mappings. In computing the serial string all prefixes are replaced by their xmlns prefix,
     * so http://cilogon.org/serverA/12345 ---> A:12345 and here "A" is the prefix, "http://cilogon.org.serverA/" is the
     * associated value. The tokens are uniquely determined by their prefixes -- if another token with the
     * same prefix is found, it will be replaced.
     *
     * @param prefix
     * @param value
     * @throws OperatorException
     */
    public void addXMLNSToken(String prefix, URI value) throws OperatorException {
        ArrayList<XMLNSToken> tokens = new ArrayList<XMLNSToken>();
        fetchThingsByPredicate(HAS_XMLNS_TOKEN, XMLNSToken.class, tokens);
        if (tokens.isEmpty()) {
            tokens.add(newXmlnsToken(prefix, value));
        } else {
            for (XMLNSToken t : tokens) {
                if (t.getPrefix().equals(prefix)) {
                    t.setURI(value);
                    return;
                }
                tokens.add(newXmlnsToken(prefix, value));
            }
        }
        setValues(HAS_XMLNS_TOKEN, tokens);
    }

    protected XMLNSToken newXmlnsToken(String prefix, URI value) throws OperatorException {
        XMLNSToken x = new XMLNSToken(getMyThingSession(), uriRef());
        x.setPrefix(prefix);
        x.setURI(value);
        x.addType(XMLNS);
        return x;
    }
}
