package com.ppcbullet.jobmanager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * The standard implementation of a Service Manager Factory.
 * Contains collections of managed service factories and the service managers currently in use.
 * <p>
 * The factories are loaded dynamically from plugin modules stored in separate JARs on the classpath.
 * They are defined via an XML file called 'servicemanager.xml' located in the META-INF directory.
 * On initialization the class looks for these files and loads the specified factories.
 * <p>
 * The collection of service managers is used to ensure that only one is in operation
 * for a given managed service at a time.  Many services can manage multiple jobs concurrently
 * and their factory might provide the same instance multiple times.
 * This could lead to unpredictable behavior if the same one was being managed by
 * multiple different service managers.
 *
 * @author Ewan Heming
 * @version     1.0.0   2010-02-19
 * @since       1.0.0
 */
class ServiceManagerFactoryImpl extends ServiceManagerFactory {

    /**
     * The list of factories that the class knows about.
     */
    private List<ManagedServiceFactory> factories;

    /**
     * The service managers currently in use.  Indexed by their id.
     */
    private Map<String, ServiceManager> serviceManagers;

    /**
     * Sole constructor.  Populates the list of know factories by searching the classpath for
     * 'servicemanager.xml' files located in the META-INF directory.
     */
    ServiceManagerFactoryImpl() {
        factories = new ArrayList<ManagedServiceFactory>();
        serviceManagers = new HashMap<String, ServiceManager>();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        try {
            Enumeration<URL> urls = cl.getResources("META-INF/servicemanager.xml");
            while (urls.hasMoreElements()) {
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document document = db.parse(urls.nextElement().getPath());
                String factoryClass = document.getElementsByTagName("factory").item(0).getFirstChild().getNodeValue();
                int maxJobs = Integer.parseInt(document.getElementsByTagName("maxjobs").item(0).getFirstChild().getNodeValue());
                int checkInterval = Integer.parseInt(document.getElementsByTagName("checkinterval").item(0).getFirstChild().getNodeValue());
                long timeout = Long.parseLong(document.getElementsByTagName("timeout").item(0).getFirstChild().getNodeValue());
                ManagedServiceFactory fact = (ManagedServiceFactory) Class.forName(factoryClass).newInstance();
                fact.setMaxJobs(maxJobs);
                fact.setCheckInterval(checkInterval);
                fact.setTimeout(timeout);
                Collection<Class<? extends Job>> supportedJobs = new HashSet<Class<? extends Job>>();
                NodeList supportedJobNodes = document.getElementsByTagName("supportedjob");
                for (int i = 0; i < supportedJobNodes.getLength(); i++) {
                    String supportedJob = supportedJobNodes.item(i).getFirstChild().getNodeValue();
                    supportedJobs.add((Class<? extends Job>) Class.forName(supportedJob));
                }
                fact.setSupportedJobs(supportedJobs);
                factories.add(fact);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ServiceManagerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(ServiceManagerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(ServiceManagerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(ServiceManagerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(ServiceManagerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServiceManagerFactoryImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    ServiceManager getService(Job job) throws NoSuchServiceException {
        for (ManagedServiceFactory factory : factories) {
            ManagedService service = factory.getService(job);
            if (service != null) {
                ServiceManager serviceManager = serviceManagers.get(service.getId());
                if (serviceManager == null) {
                    serviceManager = new ServiceManagerImpl();
                    serviceManager.setServiceManagerFactory(this);
                    serviceManager.setManagedService(service);
                    serviceManagers.put(service.getId(), serviceManager);
                }
                return serviceManager;
            }
        }
        throw new NoSuchServiceException("There are no resistered Managed Services that can process the input job.");
    }

    @Override
    void addManagedServiceFactory(ManagedServiceFactory fact) {
        factories.add(fact);
    }

    @Override
    void removeManagedServiceFactory(ManagedServiceFactory fact) {
        factories.remove(fact);
    }

    @Override
    void removeServiceManager(ManagedService managedService) {
        serviceManagers.remove(managedService.getId());
        for (ManagedServiceFactory managedServiceFactory : factories) {
            managedServiceFactory.removeService(managedService);
        }
    }
}
