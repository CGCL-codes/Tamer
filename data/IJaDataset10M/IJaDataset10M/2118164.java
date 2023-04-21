package org.gbif.portal.webservices.rest;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.gbif.portal.util.path.PathMapping;
import org.gbif.portal.util.request.TemplateUtils;
import org.gbif.portal.webservices.actions.Action;
import org.gbif.portal.webservices.actions.DensityAction;
import org.gbif.portal.webservices.actions.DensityParameters;
import org.gbif.portal.webservices.actions.NetworkAction;
import org.gbif.portal.webservices.actions.NetworkParameters;
import org.gbif.portal.webservices.actions.OccurrenceAction;
import org.gbif.portal.webservices.actions.OccurrenceParameters;
import org.gbif.portal.webservices.actions.ProviderAction;
import org.gbif.portal.webservices.actions.ProviderParameters;
import org.gbif.portal.webservices.actions.ResourceAction;
import org.gbif.portal.webservices.actions.ResourceParameters;
import org.gbif.portal.webservices.actions.TaxonAction;
import org.gbif.portal.webservices.actions.TaxonParameters;
import org.gbif.portal.webservices.util.GbifWebServiceException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * @author 
 * 
 * This class provide switching for REST requests. It reads the request value,
 * create parameter map and dispatch it to respective command object for
 * processing, get the result, preprocess if need be then send it back as
 * response
 * 
 * It uses naming conventions for request preprocessing, data access and
 * response preprocessing.
 * 
 * On receiving a request, it approves the request else returns an error doc
 * 
 */
public class Dispatcher implements Controller {

    public static Log log = LogFactory.getLog(Dispatcher.class);

    /** Suffix for KVP array values*/
    public static final String ARRAY_SUFFIX = "_array";

    /** Delimiter for outer value lists in the KVPs */
    protected static final String OUTER_DELIMITER = "()";

    /** Delimiter for inner value lists in the KVPs */
    protected static final String INNER_DELIMITER = ",";

    protected static final int OCCURRENCE_SERVICE = 1;

    protected static final int TAXON_SERVICE = 2;

    protected static final int NETWORK_SERVICE = 3;

    protected static final int WFS_SERVICE = 4;

    protected static final int PROVIDER_SERVICE = 5;

    protected static final int RESOURCE_SERVICE = 6;

    protected static final int DENSITY_SERVICE = 7;

    public static final int UNKNOWN_SERVICE = -1;

    /**
	 * Action service beans
	 */
    protected OccurrenceAction occurrenceAction = null;

    protected ProviderAction providerAction = null;

    protected ResourceAction resourceAction = null;

    protected TaxonAction taxonAction = null;

    protected NetworkAction networkAction = null;

    protected DensityAction densityAction = null;

    protected TemplateUtils templateUtils;

    protected PathMapping pathMapping;

    /**
	 * @param networkAction
	 *            the networkAction to set
	 */
    public void setNetworkAction(NetworkAction networkAction) {
        this.networkAction = networkAction;
    }

    /**
	 * @param occurrenceAction
	 *            the occurrenceAction to set
	 */
    public void setOccurrenceAction(OccurrenceAction occurrenceAction) {
        this.occurrenceAction = occurrenceAction;
    }

    /**
	 * @param taxonAction
	 *            the taxonAction to set
	 */
    public void setTaxonAction(TaxonAction taxonAction) {
        this.taxonAction = taxonAction;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        Map<String, Object> results = new HashMap<String, Object>();
        String template = "";
        int countRecords = 0;
        int requestType = 0;
        VelocityContext velocity = new VelocityContext();
        String contentType = "text/xml; charset=UTF-8";
        response.setContentType(contentType);
        try {
            if (request.getMethod().equals("POST")) params = processPost(request); else params = processGet(request);
            if (params.get("service") == null) throw new GbifWebServiceException("SERVICE (Occurrence/Taxon/Network/WFS) should be provided");
            String format = (String) params.get("format");
            switch(getRequestType((String) params.get("service"))) {
                case OCCURRENCE_SERVICE:
                    OccurrenceParameters occurrenceParameters = new OccurrenceParameters(params, pathMapping);
                    requestType = occurrenceParameters.getRequestType();
                    template = occurrenceAction.getTemplate(params);
                    if (requestType == Action.LIST) results = occurrenceAction.findOccurrenceRecords(occurrenceParameters); else if (requestType == Action.GET) results = occurrenceAction.getOccurrenceRecord(occurrenceParameters); else if (requestType == Action.COUNT) results = occurrenceAction.countOccurrenceRecords(occurrenceParameters); else if (requestType == Action.HELP) results = occurrenceAction.returnHelpPage(occurrenceParameters); else if (requestType == Action.STYLESHEET) {
                        response.getWriter().write(occurrenceParameters.getStylesheetResource(false));
                        return null;
                    } else if (requestType == Action.SCHEMA) {
                        response.getWriter().write(occurrenceParameters.getSchemaResource(false));
                        return null;
                    }
                    break;
                case TAXON_SERVICE:
                    TaxonParameters taxonParameters = new TaxonParameters(params, pathMapping);
                    requestType = taxonParameters.getRequestType();
                    template = taxonAction.getTemplate(params);
                    if (requestType == Action.LIST) results = taxonAction.findTaxonRecords(taxonParameters); else if (requestType == Action.GET) results = taxonAction.getTaxonRecord(taxonParameters); else if (requestType == Action.COUNT) results = taxonAction.countTaxonRecords(taxonParameters); else if (requestType == Action.HELP) results = taxonAction.returnHelpPage(taxonParameters); else if (requestType == Action.STYLESHEET) {
                        response.getWriter().write(taxonParameters.getStylesheetResource(false));
                        return null;
                    } else if (requestType == Action.SCHEMA) {
                        response.getWriter().write(taxonParameters.getSchemaResource(false));
                        return null;
                    }
                    break;
                case DENSITY_SERVICE:
                    DensityParameters densityParameters = new DensityParameters(params, pathMapping);
                    requestType = densityParameters.getRequestType();
                    template = densityAction.getTemplate(params);
                    if (requestType == Action.LIST) results = densityAction.findDensityRecords(densityParameters); else if (requestType == Action.HELP) results = densityAction.returnHelpPage(densityParameters); else if (requestType == Action.STYLESHEET) {
                        response.getWriter().write(densityParameters.getStylesheetResource(false));
                        return null;
                    } else if (requestType == Action.SCHEMA) {
                        response.getWriter().write(densityParameters.getSchemaResource(false));
                        return null;
                    }
                    break;
                case NETWORK_SERVICE:
                    NetworkParameters networkParameters = new NetworkParameters(params, pathMapping);
                    requestType = networkParameters.getRequestType();
                    template = networkAction.getTemplate(params);
                    if (requestType == Action.LIST) results = networkAction.findNetworkRecords(networkParameters); else if (requestType == Action.GET) results = networkAction.getNetworkRecord(networkParameters); else if (requestType == Action.COUNT) results = networkAction.countNetworkRecords(networkParameters); else if (requestType == Action.HELP) results = networkAction.returnHelpPage(networkParameters); else if (requestType == Action.STYLESHEET) {
                        response.getWriter().write(networkParameters.getStylesheetResource(false));
                        return null;
                    } else if (requestType == Action.SCHEMA) {
                        response.getWriter().write(networkParameters.getSchemaResource(false));
                        return null;
                    }
                    break;
                case PROVIDER_SERVICE:
                    ProviderParameters providerParameters = new ProviderParameters(params, pathMapping);
                    requestType = providerParameters.getRequestType();
                    template = providerAction.getTemplate(params);
                    if (requestType == Action.LIST) results = providerAction.findProviderRecords(providerParameters); else if (requestType == Action.GET) results = providerAction.getProviderRecord(providerParameters); else if (requestType == Action.COUNT) results = providerAction.countProviderRecords(providerParameters); else if (requestType == Action.HELP) results = providerAction.returnHelpPage(providerParameters); else if (requestType == Action.STYLESHEET) {
                        response.getWriter().write(providerParameters.getStylesheetResource(false));
                        return null;
                    } else if (requestType == Action.SCHEMA) {
                        response.getWriter().write(providerParameters.getSchemaResource(false));
                        return null;
                    }
                    break;
                case RESOURCE_SERVICE:
                    ResourceParameters resourceParameters = new ResourceParameters(params, pathMapping);
                    requestType = resourceParameters.getRequestType();
                    template = resourceAction.getTemplate(params);
                    if (requestType == Action.LIST) results = resourceAction.findResourceRecords(resourceParameters); else if (requestType == Action.GET) results = resourceAction.getResourceRecord(resourceParameters); else if (requestType == Action.COUNT) results = resourceAction.countResourceRecords(resourceParameters); else if (requestType == Action.HELP) results = resourceAction.returnHelpPage(resourceParameters); else if (requestType == Action.STYLESHEET) {
                        response.getWriter().write(resourceParameters.getStylesheetResource(false));
                        return null;
                    } else if (requestType == Action.SCHEMA) {
                        response.getWriter().write(resourceParameters.getSchemaResource(false));
                        return null;
                    }
                    break;
                default:
                    throw new GbifWebServiceException("Unknown service parameter should be Occurrence/Taxonomy/Network/WFS");
            }
            if (format != null) {
                if (format.equalsIgnoreCase("kml")) {
                    contentType = "application/vnd.google-earth.kml+xml";
                    response.setContentType(contentType);
                }
            }
        } catch (GbifWebServiceException gwsex) {
            template = "org/gbif/portal/ws/error.vm";
            velocity.put("errorMessage", gwsex.getMessage());
            velocity.put("service", params.get("service"));
            velocity.put("request", params.get("request"));
        } catch (Exception gwsex) {
            template = "org/gbif/portal/ws/error.vm";
            velocity.put("errorMessage", gwsex.toString());
            velocity.put("service", params.get("service"));
            velocity.put("request", params.get("request"));
        }
        String url = request.getRequestURL().toString();
        String servletPath = request.getServletPath();
        String urlBase = url.substring(0, url.indexOf(servletPath));
        velocity.put("portalroot", urlBase);
        velocity.put("wsroot", urlBase + servletPath);
        velocity.put("response", results);
        velocity.put("count", results.get("count"));
        Template templateObject = templateUtils.getTemplate(template);
        templateUtils.merge(templateObject, velocity, response.getWriter());
        return null;
    }

    public void setPathMapping(PathMapping pathMapping) {
        this.pathMapping = pathMapping;
    }

    public TemplateUtils getTemplateUtils() {
        return templateUtils;
    }

    public void setTemplateUtils(TemplateUtils templateUtils) {
        this.templateUtils = templateUtils;
    }

    /**
	 * Generate a cleaned parameters map from a get request, including any attributes (from URL rewriting)
	 * 
	 * @param request
	 * @return parameters map
	 * @throws GbifWebServiceException
	 */
    protected Map<String, Object> processGet(HttpServletRequest request) throws GbifWebServiceException {
        Map<String, Object> kvps = new HashMap<String, Object>();
        Enumeration enumeration = request.getParameterNames();
        if (enumeration != null) {
            Map parameterMap = request.getParameterMap();
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                String[] values = (String[]) parameterMap.get(name);
                kvps.put(name.toLowerCase(), values[0]);
                if (values.length > 1) {
                    kvps.put(name + ARRAY_SUFFIX, values);
                }
            }
        }
        enumeration = request.getAttributeNames();
        if (enumeration != null) {
            while (enumeration.hasMoreElements()) {
                String name = (String) enumeration.nextElement();
                Object value = request.getAttribute(name);
                if (value instanceof String) {
                    kvps.put(name.toLowerCase(), (String) value);
                }
            }
        }
        String url = request.getRequestURL().toString();
        String servletPath = request.getServletPath();
        String urlBase = url.substring(0, url.indexOf(servletPath));
        kvps.put("portalroot", urlBase);
        kvps.put("wsroot", urlBase + servletPath);
        log.debug("Parameter map: " + kvps.toString());
        return kvps;
    }

    /**
	 * Generate a cleaned parameters map from a POST request (mainly for WFS XML
	 * requests)
	 * 
	 * @param request
	 * @return parameters map
	 */
    protected Map<String, Object> processPost(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<String, Object>();
        return params;
    }

    /**
	 * Returns the request type for a given KVP set.
	 */
    private int getRequestType(String request) {
        log.debug("dispatcher got request " + request);
        if (request != null) {
            request = request.toUpperCase();
            if (request.equals("OCCURRENCE")) return Dispatcher.OCCURRENCE_SERVICE; else if (request.equals("PROVIDER")) return Dispatcher.PROVIDER_SERVICE; else if (request.equals("RESOURCE")) return Dispatcher.RESOURCE_SERVICE; else if (request.equals("TAXON")) return Dispatcher.TAXON_SERVICE; else if (request.equals("NETWORK")) return Dispatcher.NETWORK_SERVICE; else if (request.equals("DENSITY")) return Dispatcher.DENSITY_SERVICE; else if (request.equals("WFS")) return Dispatcher.NETWORK_SERVICE; else return Dispatcher.UNKNOWN_SERVICE;
        } else return Dispatcher.UNKNOWN_SERVICE;
    }

    /**
	 * @param providerAction the providerAction to set
	 */
    public void setProviderAction(ProviderAction providerAction) {
        this.providerAction = providerAction;
    }

    /**
	 * @param resourceAction the resourceAction to set
	 */
    public void setResourceAction(ResourceAction resourceAction) {
        this.resourceAction = resourceAction;
    }

    /**
	 * @param densityAction the densityAction to set
	 */
    public void setDensityAction(DensityAction densityAction) {
        this.densityAction = densityAction;
    }
}
