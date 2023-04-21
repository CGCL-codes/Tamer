package net.ar.guia.plugins;

import java.io.*;
import net.ar.guia.render.templates.*;

public class DefaultGuiaTemplateManagerEntry implements Serializable, Comparable {

    protected String alias;

    protected String resourcePath;

    protected transient Template template;

    public DefaultGuiaTemplateManagerEntry() {
    }

    public DefaultGuiaTemplateManagerEntry(String aTemplateName) {
        alias = aTemplateName;
    }

    public DefaultGuiaTemplateManagerEntry(String aTemplateName, String aResourcePath, Template aTemplate) {
        alias = aTemplateName;
        resourcePath = aResourcePath;
        template = aTemplate;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public String getTemplateName() {
        return alias;
    }

    public void setResourcePath(String unString) {
        resourcePath = unString;
    }

    public void setTemplateName(String unString) {
        alias = unString;
    }

    public Template getTemplate() {
        return template;
    }

    public synchronized void setTemplate(Template unTemplate) {
        template = unTemplate;
    }

    public boolean equals(Object obj) {
        if (obj instanceof DefaultGuiaTemplateManagerEntry) return getTemplateName().equals(((DefaultGuiaTemplateManagerEntry) obj).getTemplateName()); else return getTemplateName().equals(obj.toString());
    }

    public int hashCode() {
        return getTemplateName().hashCode();
    }

    public int compareTo(Object o) {
        if (o instanceof DefaultGuiaTemplateManagerEntry) return getTemplateName().compareTo(((DefaultGuiaTemplateManagerEntry) o).getTemplateName()); else return getTemplateName().compareTo(o.toString());
    }
}
