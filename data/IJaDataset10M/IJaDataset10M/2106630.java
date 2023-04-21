package com.liferay.portal.tools;

import com.liferay.util.ant.Wsdl2JavaTask;
import java.io.File;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * <a href="PortalClientBuilder.java.html"><b><i>View Source</i></b></a>
 *
 * @author Brian Wing Shun Chan
 *
 */
public class PortalClientBuilder {

    public static void main(String[] args) {
        if (args.length == 4) {
            new PortalClientBuilder(args[0], args[1], args[2], args[3]);
        } else {
            throw new IllegalArgumentException();
        }
    }

    public PortalClientBuilder(String fileName, String outputDir, String mappingFile, String url) {
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(new File(fileName));
            Element root = doc.getRootElement();
            Iterator itr = root.elements("service").iterator();
            while (itr.hasNext()) {
                Element service = (Element) itr.next();
                String name = service.attributeValue("name");
                if (name.startsWith("Portal_") || name.startsWith("Portlet_")) {
                    Wsdl2JavaTask.generateJava(url + "/" + name + "?wsdl", outputDir, mappingFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File testNamespace = new File(outputDir + "/com/liferay/portal");
        if (testNamespace.exists()) {
            throw new RuntimeException("Please update " + mappingFile + " to namespace " + "com.liferay.portal to com.liferay.client.portal");
        }
    }
}
