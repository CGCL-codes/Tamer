package com.atosorigin.nl.saml.cas.controller;

import java.util.HashMap;
import java.util.Map;
import org.jasig.cas.authentication.principal.AbstractWebApplicationService;
import org.jasig.cas.authentication.principal.Response;
import org.opensaml.SAMLSubject;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

/**
 * @author a108600
 *
 */
public class SamlProxyWebApplicationService extends AbstractWebApplicationService {

    /** Constant representing service. */
    private static final String CONST_PARAM_SERVICE = "TARGET";

    /** Constant representing artifact. */
    private static final String CONST_PARAM_TICKET = "SAMLart";

    /**
	 * 
	 */
    private static final long serialVersionUID = -7263360200760541351L;

    /**
	 * @param id
	 * @param originalUrl
	 * @param artifactId
	 * @param httpClient
	 */
    public SamlProxyWebApplicationService(String id, String originalUrl, String artifactId) {
        super(id, originalUrl, artifactId, null);
    }

    /**
	 * @param subject
	 * @return
	 */
    public static SamlProxyWebApplicationService createServiceFrom(SAMLSubject subject) {
        Element confData = subject.getConfirmationData();
        Element ticketNode = (Element) confData.getChildNodes().item(0);
        Text ticketData = (Text) ticketNode.getChildNodes().item(0);
        final String ticket = ticketData.getTextContent();
        Element serviceNode = (Element) confData.getChildNodes().item(1);
        Text serviceText = (Text) serviceNode.getChildNodes().item(0);
        final String serviceToUse = serviceText.getTextContent();
        return new SamlProxyWebApplicationService(cleanupUrl(serviceToUse), serviceToUse, ticket);
    }

    public Response getResponse(String ticketId) {
        final Map<String, String> parameters = new HashMap<String, String>();
        parameters.put(CONST_PARAM_TICKET, ticketId);
        parameters.put(CONST_PARAM_SERVICE, getOriginalUrl());
        return Response.getRedirectResponse(getOriginalUrl(), parameters);
    }
}
