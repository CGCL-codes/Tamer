package org.deft.repository.integrator;

import org.deft.operation.OperationConfiguration;
import org.deft.repository.datamodel.Artifact;
import org.deft.repository.datamodel.Chapter;
import org.deft.util.XmlUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The IntegratorConfiguration is a wrapper class for the OperationConfiguration of an integrator.
 * It can convert the content of the OperationConfiguration to XML and restore the OperationConfiguration
 * from XML.
 * It is the equivalent of the OperationChainConfiguration class, just for integrators. 
 */
public class IntegratorConfiguration {

    private Integrator integrator;

    private OperationConfiguration config;

    public IntegratorConfiguration(Integrator integrator) {
        this.integrator = integrator;
        this.config = integrator.createConfiguration();
    }

    public OperationConfiguration getConfiguration() {
        return config;
    }

    public Document getConfigurationAsXml(String artifactRefId, Artifact artifact, Chapter chapter) {
        Document document = XmlUtil.makeDocument();
        Element root = document.createElement("reference");
        document.appendChild(root);
        root.setAttribute("artifact", artifact.getId());
        root.setAttribute("chapter", chapter.getId());
        root.setAttribute("id", artifactRefId);
        if (integrator != null) {
            Element eIntegrator = document.createElement("integrator");
            root.appendChild(eIntegrator);
            config.addToXml(eIntegrator);
        }
        return document;
    }

    public void loadConfigurationForIntegrator(Document doc) {
        Element eIntegrator = getIntegratorConfigurationXmlElement(doc);
        config.loadFromXml(eIntegrator);
    }

    private Element getIntegratorConfigurationXmlElement(Document doc) {
        Element root = doc.getDocumentElement();
        Element eIntegrator = (Element) root.getElementsByTagName("integrator").item(0);
        return eIntegrator;
    }
}
