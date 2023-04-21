package fi.foyt.cs.oauth;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.openid4java.OpenIDException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.Discovery;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.AuthSuccess;
import org.openid4java.message.MessageException;
import org.openid4java.message.MessageExtension;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.AxMessage;
import org.openid4java.message.ax.FetchRequest;
import org.openid4java.message.ax.FetchResponse;
import org.restlet.Request;
import org.restlet.data.CookieSetting;
import org.restlet.data.Form;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Reference;
import org.restlet.data.Status;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.EmptyRepresentation;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.restlet.routing.Redirector;

public class OpenIdLogin extends ServerResource {

    private static final String DESCRIPTOR_COOKIE = "openid-disc";

    static final ConcurrentHashMap<String, String> ax = new ConcurrentHashMap<String, String>(11);

    static {
        ax.put("nickname", "http://axschema.org/namePerson/friendly");
        ax.put("email", "http://axschema.org/contact/email");
        ax.put("fullname", "http://axschema.org/namePerson");
        ax.put("dob", "http://axschema.org/birthDate");
        ax.put("gender", "http://axschema.org/person/gender");
        ax.put("postcode", "http://axschema.org/contact/postalCode/home");
        ax.put("country", "http://axschema.org/contact/country/home");
        ax.put("language", "http://axschema.org/pref/language");
        ax.put("timezone", "http://axschema.org/pref/timezone");
        ax.put("firstname", "http://axschema.org/namePerson/first");
        ax.put("lastname", "http://axschema.org/namePerson/last");
    }

    private Logger log;

    private static ConcurrentHashMap<String, ConsumerManager> managers = new ConcurrentHashMap<String, ConsumerManager>();

    private static ConcurrentHashMap<String, Object> session = new ConcurrentHashMap<String, Object>();

    private static Discovery discovery = new Discovery();

    @Override
    protected void doInit() throws ResourceException {
        super.doInit();
        log = getLogger();
    }

    @Override
    protected Representation head() throws ResourceException {
        getLogger().info("IN head() OpenIDResource");
        setXRDSHeader();
        getLogger().info("Sending empty representation.");
        return new EmptyRepresentation();
    }

    @Get("html")
    public Representation represent() {
        Form params = getQuery();
        log.info("OpenIDResource : " + params);
        String rc = params.getFirstValue("return");
        if (rc != null && rc.length() > 0) {
            Map<String, String> axRequired = new HashMap<String, String>();
            Map<String, String> axOptional = new HashMap<String, String>();
            Identifier i = verifyResponse(axRequired, axOptional);
            if (i == null) {
                log.info("Authentication Failed");
                getResponse().setStatus(Status.CLIENT_ERROR_NOT_FOUND);
                return new StringRepresentation("Authentication Failed");
            }
            log.info("Identifier = " + i.getIdentifier());
            String id = i.getIdentifier();
            if (id != null) {
                JSONObject obj = new JSONObject();
                try {
                    obj.put("id", i.getIdentifier());
                    for (String s : axRequired.keySet()) {
                        obj.put(s, axRequired.get(s));
                    }
                    for (String s : axOptional.keySet()) {
                        obj.put(s, axOptional.get(s));
                    }
                } catch (JSONException e) {
                    log.log(Level.WARNING, "Failed to get the ID!", e);
                }
                getResponse().setEntity(new JsonRepresentation(obj));
            }
            getResponse().getCookieSettings().remove(DESCRIPTOR_COOKIE);
            CookieSetting disc = new CookieSetting(DESCRIPTOR_COOKIE, "");
            disc.setMaxAge(0);
            getResponse().getCookieSettings().add(disc);
            return getResponse().getEntity();
        }
        String target = params.getFirstValue("openid_identifier");
        if (target == null || target.length() == 0) {
            String location = setXRDSHeader();
            StringBuilder html = new StringBuilder();
            html.append("<html><head><meta http-equiv=\"X-XRDS-Location\" content=\"");
            html.append(location);
            html.append("\"/></head></html>");
            return new StringRepresentation(html.toString(), MediaType.TEXT_HTML);
        }
        try {
            StringBuilder returnToUrl = new StringBuilder();
            returnToUrl.append(getReference().getBaseRef());
            returnToUrl.append("?return=true");
            List<?> discoveries = null;
            discoveries = discovery.discover(target);
            for (Object o : discoveries) {
                if (o instanceof DiscoveryInformation) {
                    DiscoveryInformation di = (DiscoveryInformation) o;
                    log.info("Found - " + di.getOPEndpoint());
                    target = di.getOPEndpoint().toString();
                }
            }
            ConsumerManager manager = getManager(target);
            DiscoveryInformation discovered = manager.associate(discoveries);
            String sessionId = String.valueOf(System.identityHashCode(discovered));
            session.put(sessionId, discovered);
            getResponse().getCookieSettings().add(new CookieSetting(DESCRIPTOR_COOKIE, sessionId));
            AuthRequest authReq = manager.authenticate(discovered, returnToUrl.toString());
            log.info("OpenID - REALM = " + getReference().getHostIdentifier());
            authReq.setRealm(getReference().getHostIdentifier().toString());
            FetchRequest fetch = FetchRequest.createFetchRequest();
            String[] optional = params.getValuesArray("ax_optional", true);
            for (String o : optional) {
                if (!ax.containsKey(o)) {
                    log.warning("Not supported AX extension : " + o);
                    continue;
                }
                fetch.addAttribute(o, ax.get(o), false);
            }
            String[] required = params.getValuesArray("ax_required", true);
            for (String r : required) {
                if (!ax.containsKey(r)) {
                    log.warning("Not supported AX extension : " + r);
                    continue;
                }
                fetch.addAttribute(r, ax.get(r), true);
            }
            authReq.addExtension(fetch);
            if (!discovered.isVersion2()) {
                redirectTemporary(authReq.getDestinationUrl(true));
                return null;
            } else {
                Form msg = new Form();
                for (Object key : authReq.getParameterMap().keySet()) {
                    msg.add(key.toString(), authReq.getParameterValue(key.toString()));
                    log.info("Adding to form - key " + key.toString() + " : value" + authReq.getParameterValue(key.toString()));
                }
                Redirector dispatcher = new Redirector(getContext(), authReq.getOPEndpoint(), Redirector.MODE_SERVER_OUTBOUND);
                Request req = getRequest();
                req.setEntity(msg.getWebRepresentation());
                req.setMethod(Method.POST);
                dispatcher.handle(getRequest(), getResponse());
            }
        } catch (DiscoveryException e) {
            e.printStackTrace();
        } catch (MessageException e) {
            e.printStackTrace();
        } catch (ConsumerException e) {
            e.printStackTrace();
        }
        return getResponse().getEntity();
    }

    public Identifier verifyResponse(Map<String, String> axRequired, Map<String, String> axOptional) {
        try {
            log.setLevel(Level.FINEST);
            Logger.getLogger("").setLevel(Level.FINEST);
            ParameterList response = new ParameterList(getQuery().getValuesMap());
            log.info("response = " + response);
            String openidDisc = getCookies().getFirstValue(DESCRIPTOR_COOKIE);
            DiscoveryInformation discovered = (DiscoveryInformation) session.get(openidDisc);
            log.info("discovered = " + discovered);
            log.info("getOriginalRef = " + getOriginalRef());
            log.info("OpenID disc : " + discovered.getOPEndpoint());
            log.info("OpenID orig ref : " + getOriginalRef());
            ConsumerManager manager = getManager(discovered.getOPEndpoint().toString());
            VerificationResult verification = manager.verify(getOriginalRef().toString(), response, discovered);
            log.info("verification = " + verification);
            Identifier verified = verification.getVerifiedId();
            log.info("verified = " + verified);
            if (verified != null) {
                AuthSuccess authSuccess = (AuthSuccess) verification.getAuthResponse();
                if (authSuccess.hasExtension(AxMessage.OPENID_NS_AX)) {
                    FetchResponse fetchResp = (FetchResponse) authSuccess.getExtension(AxMessage.OPENID_NS_AX);
                    MessageExtension ext = authSuccess.getExtension(AxMessage.OPENID_NS_AX);
                    if (ext instanceof FetchResponse) {
                        for (Object attributeAliasesObject : fetchResp.getAttributeAliases()) {
                            String attributeAlias = (String) attributeAliasesObject;
                            @SuppressWarnings("rawtypes") List attributeValues = fetchResp.getAttributeValues(attributeAlias);
                            if (attributeValues.size() == 1) {
                                axRequired.put(attributeAlias, (String) attributeValues.get(0));
                            }
                        }
                    }
                }
                return verified;
            }
        } catch (OpenIDException e) {
            log.log(Level.INFO, "", e);
        }
        log.setLevel(Level.INFO);
        return null;
    }

    private ConsumerManager getManager(String OPUri) {
        log.info("Getting consumer manager for - " + OPUri);
        if (!managers.containsKey(OPUri)) {
            log.info("Creating new consumer manager for - " + OPUri);
            try {
                ConsumerManager cm = new ConsumerManager();
                cm.setConnectTimeout(30000);
                cm.setSocketTimeout(30000);
                cm.setFailedAssocExpire(0);
                managers.put(OPUri, cm);
                return cm;
            } catch (ConsumerException e) {
                log.warning("Failed to create ConsumerManager for - " + OPUri);
            }
            return null;
        } else {
            return managers.get(OPUri);
        }
    }

    private String setXRDSHeader() {
        ConcurrentMap<String, Object> attribs = getContext().getAttributes();
        Reference xrds = new Reference(attribs.get("xrds").toString());
        if ("localhost".equals(xrds.getHostDomain())) {
            xrds.setHostDomain(getReference().getBaseRef().getHostDomain());
            xrds.setHostPort(getReference().getBaseRef().getHostPort());
        }
        String returnTo = getReference().getBaseRef().toString();
        String location = (returnTo != null) ? xrds.toString() + "?returnTo=" + returnTo : xrds.toString();
        getLogger().info("XRDS endpoint = " + xrds);
        Form headers = (Form) getResponse().getAttributes().get("org.restlet.http.headers");
        if (headers == null) {
            headers = new Form();
            headers.add("X-XRDS-Location", location);
            getResponse().getAttributes().put("org.restlet.http.headers", headers);
        } else {
            headers.add("X-XRDS-Location", location);
        }
        return location;
    }
}
