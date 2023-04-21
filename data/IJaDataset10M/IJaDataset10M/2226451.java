package org.javagenerate.generator.script;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;

public class TemplateDef {

    private final Properties properties;

    public Properties getProperties() {
        return properties;
    }

    public String getResourceName() {
        return resourceName;
    }

    private final String resourceName;

    public TemplateDef(String resourceName, Properties properties) {
        this.properties = properties;
        this.resourceName = resourceName;
    }

    @Override
    public String toString() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            TemplateReader.parse(ClassLoader.getSystemResourceAsStream(resourceName), out, properties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }

    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("package", "com.someweirdarseshit");
        TemplateDef templateDef = new TemplateDef("gwt/BaseEdit.java.template", properties);
        System.out.println(templateDef);
    }
}
