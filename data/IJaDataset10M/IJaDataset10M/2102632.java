package org.deved.antlride.generator;

public class AntlrContext310 implements AntlrContext {

    private String resourceName;

    private String templateName;

    public AntlrContext310(String templateName, String resourceName) {
        this.resourceName = resourceName;
        this.templateName = templateName;
    }

    public String getPackageName() {
        return "org.deved.antlride.antlr.internal.runtime310";
    }

    public String getVersion() {
        return "310";
    }

    public String getOutputDir() {
        return "../org.deved.antlride.antlrv3.1/src/main/java/";
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getTemplateName() {
        return templateName;
    }
}
