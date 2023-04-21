package com.ericsson.xsmp.common.template;

import java.util.Locale;
import java.util.Map;

public interface TemplateResolver {

    public String resolveTemplate(String msgName, Map<String, Object> parameters);

    public String resolveTemplate(String msgName, Map<String, Object> parameters, Locale locale);
}
