package com.tirsen.hanoi.engine;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import java.net.URL;
import java.util.*;
import java.io.IOException;

/**
 * 
 * 
 * <!-- $Id: ProcessDefinitionRepository.java,v 1.2 2002/09/03 07:55:24 tirsen Exp $ -->
 * <!-- $Author: tirsen $ -->
 *
 * @author Jon Tirs&eacute;n (tirsen@users.sourceforge.net)
 * @version $Revision: 1.2 $
 */
public class ProcessDefinitionRepository {

    private static final Log logger = LogFactory.getLog(ProcessDefinitionRepository.class);

    private static ProcessDefinitionRepository instance;

    public static final String LOADER_CLASS_NAME = "_HanoiProcessDefinitionLoader";

    private Map packages = new HashMap();

    private static String HANOI_CONFIG_FILE = "hanoi.xml";

    /**
     * Loads the definition with the specified ID. The package is determined from the id and the loader located in that
     * package is used to load the definition.
     * @param definitionID the definition to be loaded.
     * @return the loaded definition.
     */
    public ProcessDefinition loadDefinition(String definitionID) {
        if (definitionID == null) {
            logger.warn("Definition ID is null.");
            return null;
        }
        ProcessDefinition definition = null;
        if (definition == null) {
            int lastSlashIndex = definitionID.lastIndexOf('/');
            String workflowPackage = lastSlashIndex == -1 ? "" : definitionID.substring(0, lastSlashIndex);
            ProcessDefinitionLoader loader = getLoader(workflowPackage);
            definition = loader.load(definitionID);
        }
        return definition;
    }

    /**
     * Retrieves loader for package. Uses a cached instance if it has been cached otherwise tries to load it and puts
     * the instance into the cache, the cached loaders can later be used to browse the repository.
     *
     * @param workflowPackage package to retrieve loader for.
     * @return the loader for the specified package.
     */
    private ProcessDefinitionLoader getLoader(String workflowPackage) {
        ProcessDefinitionLoader loader = (ProcessDefinitionLoader) packages.get(workflowPackage);
        if (loader == null) {
            String javaPackage = workflowPackage.replace('/', '.');
            String className = javaPackage + "." + LOADER_CLASS_NAME;
            logger.debug("loading and instantiating loader " + className);
            try {
                Class loaderClass = Class.forName(className);
                loader = (ProcessDefinitionLoader) loaderClass.newInstance();
            } catch (Exception e) {
                logger.error("Could not find or instantiate loader-class for package: " + workflowPackage, e);
                throw new RuntimeException("Could not find or instantiate loader-class for package: " + workflowPackage, e);
            }
            packages.put(workflowPackage, loader);
        }
        return loader;
    }

    /**
     * Loads the configuration located in the file "config.xml" in the classpath.
     */
    public static void loadConfig() {
        URL resource = ProcessDefinitionRepository.class.getClassLoader().getResource(HANOI_CONFIG_FILE);
        if (resource == null) {
            logger.warn("could not find configuration file \"" + HANOI_CONFIG_FILE + "\", " + "please include it in \"hanoi_config.jar\" in the ear-file.");
            instance = new ProcessDefinitionRepository();
        } else {
            instance = new ProcessDefinitionRepository();
            Document document = null;
            try {
                logger.debug("loading configuration from " + resource);
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                document = builder.parse(resource.openStream());
            } catch (Exception e) {
                throw new RuntimeException("Failed to load configuration.", e);
            }
            logger.debug("document = " + document);
            Element element = document.getDocumentElement();
            logger.debug("element = " + element);
            NodeList nodeList = element.getElementsByTagName("package");
            logger.debug("nodeList = " + nodeList);
            logger.debug("nodeList.getLength() = " + nodeList.getLength());
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                logger.debug("node = " + node);
                logger.debug("node.getFirstChild().getNodeValue() = " + node.getFirstChild().getNodeValue());
                instance.addPackage(node.getFirstChild().getNodeValue());
            }
        }
    }

    /**
     * Retrieves the default system-wide repository.
     * @return default repository.
     */
    public static ProcessDefinitionRepository getInstance() {
        if (instance == null) {
            loadConfig();
        }
        return instance;
    }

    /**
     * Adds package to this repository, tries to load and instantiate the loader from this package.
     *
     * @param workflowPackage package to add, basically a Java-package with dots replaced with slashes.
     */
    public void addPackage(String workflowPackage) {
        getLoader(workflowPackage);
    }

    public ProcessDefinition[] getDefinitions() {
        Collection result = new LinkedList();
        for (Iterator iterator = packages.values().iterator(); iterator.hasNext(); ) {
            ProcessDefinitionLoader loader = (ProcessDefinitionLoader) iterator.next();
            result.addAll(Arrays.asList(loader.getDefinitions()));
        }
        return (ProcessDefinition[]) result.toArray(new ProcessDefinition[0]);
    }
}
