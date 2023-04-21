package com.googlecode.groovyworks.grails;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.mapper.DefaultActionMapper;
import org.codehaus.groovy.grails.web.servlet.GrailsUrlPathHelper;
import org.springframework.web.util.UrlPathHelper;
import com.opensymphony.xwork2.config.ConfigurationManager;

public class GrailsActionMapper extends DefaultActionMapper {

    private UrlPathHelper urlHelper = new GrailsUrlPathHelper();

    public ActionMapping getMapping(HttpServletRequest request, ConfigurationManager configManager) {
        GrailsActionMapping mapping = new GrailsActionMapping();
        mapping.setRequest(request);
        mapping.setUri(urlHelper.getPathWithinApplication(request));
        return mapping;
    }

    public String getUriFromActionMapping(ActionMapping mapping) {
        if (mapping instanceof GrailsActionMapping) {
            return ((GrailsActionMapping) mapping).getUri();
        }
        return super.getUriFromActionMapping(mapping);
    }
}
