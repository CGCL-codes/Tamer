package com.jcompressor.faces.config;

import java.util.Map;
import javax.faces.context.FacesContext;

public interface Resource {

    javax.faces.application.Resource getJsfResource(final FacesContext facesContext);

    String getId();

    String getFile();

    String getTarget();

    String getLibrary();

    Map<String, Object> getAttributes();

    void setId(final String id);

    void setFile(final String file);

    void setTarget(final String target);

    void setLibrary(final String library);

    void addAttribute(final String key, final Object value);
}
