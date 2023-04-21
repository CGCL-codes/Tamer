package br.gov.frameworkdemoiselle.internal.factory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.slf4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import br.gov.frameworkdemoiselle.annotation.Bundle;
import br.gov.frameworkdemoiselle.annotation.PersistenceUnit;
import br.gov.frameworkdemoiselle.exception.ConfigurationException;
import br.gov.frameworkdemoiselle.internal.configuration.EntityManagerConfig;
import br.gov.frameworkdemoiselle.internal.proxy.EntityManagerProxy;
import br.gov.frameworkdemoiselle.stereotype.Configuration;
import br.gov.frameworkdemoiselle.util.ResourceBundle;

/**
 * <p>Factory class responsible to produces instances of EntityManager.
 * Produces instances based on informations defined in persistence.xml, demoiselle.properties or @PersistenceUnit annotation.</p>
 * 
 *  TODO allow users to define EntityManager's scope using demoiselle.properties
 */
@SessionScoped
public class EntityManagerFactory implements Serializable {

    private static final long serialVersionUID = 1L;

    public static String ENTITY_MANAGER_RESOURCE = "META-INF/persistence.xml";

    @Inject
    private Logger logger;

    @Inject
    @Bundle(baseName = "demoiselle-jpa-bundle")
    private ResourceBundle bundle;

    /** Cache of EntityManagers **/
    private final Map<String, EntityManager> entityManagerCache = new HashMap<String, EntityManager>();

    /**
	 * <p>Default EntityManager producer. 
	 * 
	 * Tries two strategies to produces EntityManager instances.
	 * 
	 * <li>The first one is based on informations available on demoiselle properties file ("frameworkdemoiselle.persistenceunit.name" key).</li>
	 * <li>The second one is based on persistence.xml file. If exists only one Persistence Unit defined, this one is used.</li>
	 * 
	 * @param config Suplies informations about EntityManager defined in properties file.
	 * @return Produced EntityManager.
	 */
    @Produces
    @Default
    public EntityManager create(EntityManagerConfig config) {
        String persistenceUnitName = getFromDemoiselleProperties(config);
        if (persistenceUnitName == null) {
            persistenceUnitName = getFromPersistenceXML();
        }
        return getEntityManager(persistenceUnitName);
    }

    /**
	 * <p>Qualified EntityManager producer.
	 * Uses the "name" attribute from @PersistenceUnit annotation as Persistence unit name.</p>  
	 * 
	 * @param injectionPoint Injection Point informations.
	 * @return Produced EntityManager.
	 */
    @Produces
    @PersistenceUnit(name = "")
    public EntityManager create(final InjectionPoint injectionPoint) {
        this.logger.debug(bundle.getString("getting-persistence-unit-from-annotation"));
        return getEntityManager(injectionPoint.getAnnotated().getAnnotation(PersistenceUnit.class).name());
    }

    /**
	 * Creates EntityManager instances using the persistenceUnitName parameter.
	 * Puts the instances into a cache for further use.
	 * 
	 * @param persistenceUnitName Persistence unit name.
	 * @return Produced EntityManager.
	 */
    private EntityManager getEntityManager(String persistenceUnitName) {
        EntityManager entityManager;
        if (entityManagerCache.containsKey(persistenceUnitName)) {
            entityManager = entityManagerCache.get(persistenceUnitName);
        } else {
            entityManager = new EntityManagerProxy(getEntityManagerFactory(persistenceUnitName).createEntityManager());
            entityManagerCache.put(persistenceUnitName, entityManager);
            this.logger.info(bundle.getString("entity-manager-was-created", persistenceUnitName));
        }
        return entityManager;
    }

    public javax.persistence.EntityManagerFactory getEntityManagerFactory(String persistenceUnitName) {
        return Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    /**
	 * Tries to get persistence unit name from demoiselle.properties.
	 * 
	 * @param config Configuration containing persistence unit name.
	 * @return Persistence unit name.
	 */
    private String getFromDemoiselleProperties(EntityManagerConfig config) {
        String persistenceUnitName = config.getPersistenceUnitName();
        if (persistenceUnitName != null) {
            this.logger.debug(bundle.getString("getting-persistence-unit-from-properties", Configuration.DEFAULT_CONFIGURATION_FILE));
        }
        return persistenceUnitName;
    }

    /**
	 * Uses persistence.xml to get informations about which
	 * persistence unit to use. Throws ConfigurationException if more than one Persistence Unit is defined.
	 * 
	 * @return Persistence Unit Name
	 */
    private String getFromPersistenceXML() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(contextClassLoader.getResourceAsStream(ENTITY_MANAGER_RESOURCE));
            NodeList nodes = document.getElementsByTagName("persistence-unit");
            if (nodes.getLength() > 1) {
                throw new ConfigurationException(bundle.getString("more-than-one-persistence-unit-defined"));
            }
            String persistenceUnitName = "";
            for (int index = 0; index < nodes.getLength(); index++) {
                Node node = nodes.item(index);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (node.getNodeName().equals("persistence-unit")) {
                        persistenceUnitName = ((Element) node).getAttribute("name");
                        this.logger.debug(bundle.getString("persistence-unit-name-found", persistenceUnitName));
                    }
                }
            }
            this.logger.debug(bundle.getString("getting-persistence-unit-from-persistence"));
            return persistenceUnitName;
        } catch (ConfigurationException cause) {
            this.logger.error(cause.getMessage(), cause);
            throw cause;
        } catch (Exception cause) {
            String message = bundle.getString("can-not-get-persistence-unit-from-persistence");
            this.logger.error(message, cause);
            throw new ConfigurationException(message, cause);
        }
    }

    public void destroy(@Disposes @Default final EntityManager entityManager) {
        entityManager.close();
        this.logger.debug(bundle.getString("entity-manager-closed"));
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public ResourceBundle getBundle() {
        return bundle;
    }

    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }
}
