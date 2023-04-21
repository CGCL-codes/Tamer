package net.peelmeagrape.hibernate.config;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import net.peelmeagrape.hibernate.xmlgen.XmlUtils;
import net.peelmeagrape.hibernate.xmlgen.GenerateMappingRun;

/**
 * Process java classes using reflection and generate a hibernate mapping xml document.
 */
public class HibernateAnnotationsProcessor {

    private boolean debugMode = false;

    public String getMappingAsXml(Class aClass) throws DocumentException {
        Document mappingDocument = getMappingAsDom4j(aClass);
        if (mappingDocument == null) return null;
        return XmlUtils.prettyPrint(mappingDocument);
    }

    public Document getMappingAsDom4j(Class aClass) throws DocumentException {
        GenerateMappingRun generateMappingRun = new GenerateMappingRun(aClass);
        generateMappingRun.setDebugMode(debugMode);
        generateMappingRun.generate();
        return generateMappingRun.getMappingDocument();
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }
}
