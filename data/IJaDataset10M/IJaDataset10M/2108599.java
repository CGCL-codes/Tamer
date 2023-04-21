package org.embedded.tomcat.config;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.embedded.tomcat.utils.CustomToStringStyle;

public class Webapp {

    String contextPath;

    String webApplicationHome;

    public Webapp() {
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getWebApplicationHome() {
        return webApplicationHome;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, new CustomToStringStyle(3));
    }
}
