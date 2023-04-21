package org.slasoi.infrastructure.servicemanager.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import org.apache.log4j.Logger;
import org.slasoi.infrastructure.servicemanager.exceptions.DescriptorException;
import org.slasoi.infrastructure.servicemanager.exceptions.ProvisionException;
import org.slasoi.infrastructure.servicemanager.exceptions.StopException;
import org.slasoi.infrastructure.servicemanager.exceptions.UnknownIdException;
import org.slasoi.infrastructure.servicemanager.occi.OcciClient;
import org.slasoi.infrastructure.servicemanager.occi.types.Schemas;
import org.slasoi.infrastructure.servicemanager.occi.types.Terms;
import org.slasoi.infrastructure.servicemanager.prediction.PredictionQuery;
import org.slasoi.infrastructure.servicemanager.registry.Registry;
import org.slasoi.infrastructure.servicemanager.tx.impl.OCCIPostTransaction;
import org.slasoi.infrastructure.servicemanager.types.Appliance;
import org.slasoi.infrastructure.servicemanager.types.EndPoint;
import org.slasoi.infrastructure.servicemanager.types.ProvisionRequestType;
import org.slasoi.infrastructure.servicemanager.types.ProvisionResponseType;
import org.slasoi.infrastructure.servicemanager.types.ReservationResponseType;
import eu.slasoi.infrastructure.model.Category;
import eu.slasoi.infrastructure.model.Kind;
import eu.slasoi.infrastructure.model.infrastructure.Compute;

/**
 * Prototype implementation of a Infrastructure Service Manager.
 * 
 * @author sla
 *
 */
public class InfrastructureUtil {

    private static final String EQUALS = " = ";

    private static final String OPEN_HYPHEN = "'";

    private static final String COMMA = ",";

    private static final String ATTRIBUTE = "Attribute";

    private static final String CATEGORY = "Category";

    private static final String UNKNOWN = "UNKNOWN";

    private static final String CLOSE_HYPHEN = OPEN_HYPHEN;

    /**
     * The logger.
     */
    private static Logger logger = Logger.getLogger(InfrastructureImpl.class.getName());

    /**
     * The OCCI client. Used for accessing OCCI implementations of infrastructural service providers
     */
    private org.slasoi.infrastructure.servicemanager.occi.OcciClient occiclient;

    /**
     * Prediction Instrumentation.
     */
    private PredictionQuery predictionQuery;

    /**
     * The Image Registry.
     */
    private Registry<?, ?> imageregistry;

    /**
     * The SLA Type Registry.
     */
    private Registry<?, ?> slatyperegistry;

    /**
     * Location Register.
     */
    private Registry<?, ?> locationregistry;

    /**
     * Prepare Requests @Map. Used for keeping track of <<prepare>>.
     */
    private Map<String, ReservationResponseType> prepareRequests = new HashMap<String, ReservationResponseType>();

    /**
     * Provision Request @Map. Used for keeping track of <<manage>> provision.
     */
    private Map<String, ProvisionRequestType> provisionRequests = new HashMap<String, ProvisionRequestType>();

    /**
     * Provision Response @Map. Used for keeping track <<Manage>> successful provision(s).
     */
    private Map<String, ProvisionResponseType> provisionResponses = new HashMap<String, ProvisionResponseType>();

    /**
     * Random.
     */
    private Random rnd = new Random(System.nanoTime());

    /**
	 * Platform specific temporary directory 
	 */
    private static String tmpdir = System.getProperty("java.io.tmpdir");

    private static String path = tmpdir + File.separator + "ProvisionResponses" + File.separator;

    /**
     * Gets the @OcciClient.
     * @return
     * the @OcciClient
     */
    public ReservationResponseType commit(String infrastructureID) throws UnknownIdException {
        if (prepareRequests.containsKey(infrastructureID)) {
            return prepareRequests.get(infrastructureID);
        } else {
            throw new UnknownIdException();
        }
    }

    public ProvisionResponseType getDetails(String infrastructureID) throws UnknownIdException {
        ProvisionResponseType provisionResponseType = null;
        try {
            provisionResponseType = ProvisionResponseType.fromFile(path + File.separator + infrastructureID + ".xml");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return provisionResponseType;
    }

    /**
     * Gets @Map resource details.
     * 
     * @param resourceId
     * The resource Id identifying the compute resource.
     * 
     * @return
     * The @Map
     * 
     * @throws UnknownIdException
     * Exception thrown if the resource Id does not exist.
     */
    public Compute getComputeDetails(String resourceId) throws UnknownIdException {
        OCCIHeadersParser.parseHeaders(occiclient.getHeaders(resourceId));
        return null;
    }

    public ProvisionResponseType provisionTX(ProvisionRequestType provisionRequestType) throws DescriptorException, ProvisionException {
        Map<String, Compute> computes = new HashMap<String, Compute>();
        if (provisionRequestType == null) {
            logger.error("ProvisionRequestType cannot be null");
            throw new DescriptorException("ProvisionRequestType cannot be null");
        }
        Set<Kind> kinds = provisionRequestType.getKinds();
        List<EndPoint> endPoints = new ArrayList<EndPoint>();
        for (Iterator<Kind> iterator = kinds.iterator(); iterator.hasNext(); ) {
            Kind kind = iterator.next();
            if (kind instanceof Compute) {
                Map<String, String> headers = new HashMap<String, String>();
                logger.info("Compute - kind");
                Compute compute = (Compute) kind;
                computes.put(compute.getHostname(), compute);
            }
        }
        provisionRequestType.setId(rnd.nextLong());
        String infrastructureID = UUID.randomUUID().toString();
        provisionRequestType.setProvId(infrastructureID);
        OCCIPostTransaction occiPutTransaction = new OCCIPostTransaction();
        List<Map<String, String>> headersList = null;
        try {
            headersList = extractHeaders(provisionRequestType);
        } catch (DescriptorException e) {
            logger.error(e);
            e.printStackTrace();
        }
        Map<String, Map<String, String>> resources = occiPutTransaction.post(occiclient, headersList);
        for (Iterator<String> iterator = resources.keySet().iterator(); iterator.hasNext(); ) {
            String hostName = iterator.next();
            EndPoint endPoint = new EndPoint();
            Map<String, String> headers = resources.get(hostName);
            Appliance<String> appliance = new Appliance<String>();
            appliance.setImage(headers.get(Terms.RESOURCE_ID));
            endPoint.setAppliance(appliance);
            endPoint.setHostName(headers.get(Terms.HOSTNAME));
            URL resourceUrl = null;
            try {
                resourceUrl = new URL("http://" + occiclient.getHostname() + ":" + occiclient.getPort() + occiclient.getResource() + "/" + headers.get(Terms.RESOURCE_ID));
                logger.info("The management endpoint for this VM (Resource) is " + resourceUrl.getPath());
                endPoint.setResourceUrl(resourceUrl);
                Compute compute = computes.get(headers.get(Terms.HOSTNAME));
                compute.setResourceId(endPoint.getResourceUrl().toExternalForm());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                logger.error(e);
            }
            endPoints.add(endPoint);
        }
        provisionRequests.put(infrastructureID, provisionRequestType);
        ProvisionResponseType provisionResponseType = new ProvisionResponseType();
        provisionResponseType.setInfrastructureID(infrastructureID);
        provisionResponseType.setExpiry(new Date(Long.MAX_VALUE));
        provisionResponseType.setEndPoints(endPoints);
        provisionResponses.put(infrastructureID, provisionResponseType);
        provisionResponseType.setDescription(provisionRequestType.getDescription());
        provisionResponseType.setName(provisionRequestType.getName());
        provisionResponseType.setSla(provisionRequestType.getSla());
        logger.info("Saving session");
        logger.info(provisionResponseType);
        try {
            create();
            save(provisionResponseType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provisionResponseType;
    }

    public List<Map<String, String>> extractHeaders(ProvisionRequestType provisionRequestType) throws DescriptorException {
        List<Map<String, String>> headersList = new ArrayList<Map<String, String>>();
        Set<Kind> kinds = provisionRequestType.getKinds();
        List<EndPoint> endPoints = new ArrayList<EndPoint>();
        for (Iterator<Kind> iterator = kinds.iterator(); iterator.hasNext(); ) {
            Kind kind = iterator.next();
            if (kind instanceof Compute) {
                Compute compute = (Compute) kind;
                Map<String, String> headers = new HashMap<String, String>();
                if (((compute.getCpu_cores()) == 0) || ((int) compute.getMemory_size() == 0) || ((int) compute.getCpu_speed()) == 0) {
                    logger.error("ProvisionRequestType must specify valid compute resource [cores,speed,memory]");
                    throw new DescriptorException("ProvisionRequestType must specify valid compute resource [cores,speed,memory]");
                }
                headers.put(Terms.PROVISION_ID, provisionRequestType.getProvId());
                headers.put(Terms.NOTIFICATION_URI, provisionRequestType.getNotificationUri());
                headers.put(Terms.COMPUTE_CORES, Integer.toString(compute.getCpu_cores()));
                headers.put(Terms.COMPUTE_MEMORY, Integer.toString((int) compute.getMemory_size()));
                headers.put(Terms.COMPUTE_SPEED, Integer.toString((int) (1000.0f * compute.getCpu_speed())));
                ;
                headers.put(Terms.HOSTNAME, compute.getHostname());
                Set<Category> categories = compute.getCategories();
                String categoryHeader = "";
                for (Iterator<Category> iterator2 = categories.iterator(); iterator2.hasNext(); ) {
                    Category category = (Category) iterator2.next();
                    categoryHeader = categoryHeader + occiclient.createHeader(category.getTerm(), category.getScheme(), category.getLabel()) + ", ";
                }
                headers.put("Category", categoryHeader);
                logger.info("Header - " + categoryHeader);
                headersList.add(headers);
            }
        }
        return headersList;
    }

    public String reprovision(Compute compute, String provID, String notificationURI, String monitoringRequest) throws DescriptorException, ProvisionException {
        Map<String, String> headers = new HashMap<String, String>();
        logger.info("Compute - kind");
        if (((compute.getCpu_cores()) == 0) || ((int) compute.getMemory_size() == 0) || ((int) compute.getCpu_speed()) == 0) {
            logger.error("ProvisionRequestType must specify valid compute resource [cores,speed,memory]");
            throw new DescriptorException("Reprovision. ProvisionRequestType must specify valid compute resource [cores,speed,memory]");
        }
        headers.put(Terms.PROVISION_ID, provID);
        headers.put(Terms.NOTIFICATION_URI, notificationURI);
        headers.put(Terms.COMPUTE_CORES, Integer.toString(compute.getCpu_cores()));
        headers.put(Terms.COMPUTE_MEMORY, Integer.toString((int) compute.getMemory_size()));
        headers.put(Terms.COMPUTE_SPEED, Integer.toString((int) (1000.0f * compute.getCpu_speed())));
        headers.put(Terms.HOSTNAME, compute.getHostname());
        Set<Category> categories = compute.getCategories();
        String categoryHeader = "";
        for (Iterator<Category> iterator2 = categories.iterator(); iterator2.hasNext(); ) {
            Category category = (Category) iterator2.next();
            categoryHeader = categoryHeader + occiclient.createHeader(category.getTerm(), category.getScheme(), category.getLabel()) + ", ";
        }
        headers.put("Category", categoryHeader);
        logger.info("Header - " + categoryHeader);
        logger.info("Calling OCCI Server for Single Provisioning - PUT");
        String resourceID = compute.getResourceId();
        resourceID = occiclient.put(resourceID, headers);
        logger.info("This is the response from the ISM Server");
        logger.info("resourceID -  " + resourceID);
        if (resourceID == null) {
            logger.error("Provision was not sucessful");
            throw new ProvisionException("Reprovision. Provision was not successful");
        }
        return resourceID;
    }

    public ProvisionRequestType query(ProvisionRequestType provisioningRequest) throws DescriptorException {
        return provisioningRequest;
    }

    public void release(String infrastructureID) throws UnknownIdException {
        if (prepareRequests.containsKey(infrastructureID)) {
            prepareRequests.remove(infrastructureID);
        } else {
            throw new UnknownIdException();
        }
    }

    public ProvisionResponseType reprovision(String infrastructureID, ProvisionRequestType newProvisioningRequest) throws DescriptorException, UnknownIdException, ProvisionException {
        if (provisionResponses.get(infrastructureID) == null) {
            logger.error("We didnt find a provision with ID - " + infrastructureID);
            throw new UnknownIdException();
        } else {
            Set<Kind> kinds = newProvisioningRequest.getKinds();
            List<EndPoint> endPoints = new ArrayList<EndPoint>();
            for (Iterator<Kind> iterator = kinds.iterator(); iterator.hasNext(); ) {
                Kind kind = iterator.next();
                if (kind instanceof Compute) {
                    String resourceID = reprovision((Compute) kind, newProvisioningRequest.getProvId(), newProvisioningRequest.getNotificationUri(), newProvisioningRequest.getMonitoringRequest());
                    EndPoint endPoint = new EndPoint();
                    Appliance<String> appliance = new Appliance<String>();
                    appliance.setImage(resourceID);
                    endPoint.setAppliance(appliance);
                    endPoint.setHostName(kind.getHostname());
                    URL resourceUrl = null;
                    try {
                        resourceUrl = new URL("http://" + occiclient.getHostname() + ":" + occiclient.getPort() + occiclient.getResource() + "/" + resourceID);
                        logger.info("The management endpoint for this VM (Resource) is " + resourceUrl.getPath());
                        endPoint.setResourceUrl(resourceUrl);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        logger.error(e);
                    }
                    logger.info("Endpoint - " + endPoint);
                    endPoints.add(endPoint);
                }
            }
            return provisionResponses.get(infrastructureID);
        }
    }

    public ReservationResponseType reserve(ProvisionRequestType provisioningRequest) throws DescriptorException {
        ReservationResponseType reservationResponseType = new ReservationResponseType();
        String infrastructureID = java.util.UUID.randomUUID().toString();
        reservationResponseType.setInfrastructureID(infrastructureID);
        prepareRequests.put(infrastructureID, reservationResponseType);
        return reservationResponseType;
    }

    public void startResource(String resourceID) throws UnknownIdException {
        logger.info("Not implemented yet");
    }

    public void stop(String infrastructureID) throws UnknownIdException, StopException {
        String servicePath = "http://" + occiclient.getHostname() + ":" + occiclient.getPort() + occiclient.getServiceResource() + "/" + infrastructureID;
        boolean errorDeleting = false;
        if (provisionRequests.containsKey(infrastructureID)) {
            delete(infrastructureID);
            String result = occiclient.delete(servicePath);
            if (result == null) {
                errorDeleting = true;
            }
            if (errorDeleting) throw new StopException();
        } else {
            logger.error("infrastructureID - does not exist - " + infrastructureID);
            logger.info("We are going to  delete (stop) - Service -" + servicePath);
            String result = occiclient.delete(servicePath);
            if (result == null) throw new StopException();
        }
    }

    public void stopResource(String resourceID) throws UnknownIdException {
        logger.info("Not Implemented yet");
    }

    @Deprecated
    public ProvisionRequestType createProvisionRequestType(String imageID, String slaTypeID, String locationID, int cores, int memory, String hostName, String notificationURI) {
        logger.info("This method has been deprecated");
        return createProvisionRequestType(imageID, slaTypeID, locationID, cores, 1, memory, "PI", hostName, notificationURI);
    }

    public Compute createComputeConfiguration(String imageID, String slaTypeID, String locationID, int cores, float speed, int memory, String monitoringRequest, String hostName, String notificationURI) {
        return createComputeConfiguration(imageID, slaTypeID, locationID, cores, speed, memory, monitoringRequest, hostName, notificationURI, new Hashtable<String, String>());
    }

    public Compute createComputeConfiguration(String imageID, String slaTypeID, String locationID, int cores, float speed, int memory, String monitoringRequest, String hostName, String notificationURI, Hashtable<String, String> extras) {
        Category computeCategory = new Category();
        computeCategory.setTerm(Terms.COMPUTE);
        computeCategory.setScheme(Schemas.OCCI);
        computeCategory.setTitle("\"" + "\"");
        logger.debug("imageregistry - " + imageregistry);
        Category applianceCategory = imageregistry.getCategoryByID(imageID);
        if (applianceCategory == null) {
            logger.error("Could not find an imageID matching a Category - " + imageID);
            return null;
        }
        logger.debug("locationregistry - " + locationregistry);
        Category locationCategory = locationregistry.getCategoryByID(locationID);
        if (locationCategory == null) {
            logger.error("Could not find an locationID matching a Category - " + locationID);
            return null;
        }
        Compute vmComputeConfiguration = new Compute();
        vmComputeConfiguration.setCpu_speed(speed);
        vmComputeConfiguration.setCpu_cores(cores);
        vmComputeConfiguration.setCpu_speed(speed * 1.0f);
        vmComputeConfiguration.setMemory_size(memory * 1.0f);
        logger.info("Cores - " + vmComputeConfiguration.getCpu_cores());
        logger.info("Speed - " + vmComputeConfiguration.getCpu_speed());
        logger.info("Memory - " + vmComputeConfiguration.getMemory_size());
        vmComputeConfiguration.setHostname(hostName);
        Set<Category> categories = new HashSet<Category>();
        categories.add(applianceCategory);
        categories.add(computeCategory);
        categories.add(locationCategory);
        vmComputeConfiguration.setCategories(categories);
        Hashtable<String, String> extrasCopy = new Hashtable<String, String>();
        extrasCopy.putAll(extras);
        vmComputeConfiguration.setExtras(extrasCopy);
        return vmComputeConfiguration;
    }

    public ProvisionRequestType createProvisionRequestType(Set<Compute> computeConfigurations, String notificationURI) {
        ProvisionRequestType provisionRequestType = new ProvisionRequestType();
        Date startTime = new Date(System.currentTimeMillis());
        Date stopTime = startTime;
        provisionRequestType.setProvStartTime(startTime);
        provisionRequestType.setProvStopTime(stopTime);
        provisionRequestType.setNotificationUri(notificationURI);
        provisionRequestType.setKinds(computeConfigurations);
        return provisionRequestType;
    }

    public ProvisionRequestType createProvisionRequestType(String imageID, String slaTypeID, String locationID, int cores, float speed, int memory, String monitoringRequest, String hostName, String notificationURI) {
        ProvisionRequestType provisionRequestType = new ProvisionRequestType();
        Date startTime = new Date(System.currentTimeMillis());
        Date stopTime = startTime;
        provisionRequestType.setProvStartTime(startTime);
        provisionRequestType.setProvStopTime(stopTime);
        provisionRequestType.setNotificationUri(notificationURI);
        Compute vmComputeConfiguration = createComputeConfiguration(imageID, slaTypeID, locationID, cores, speed, memory, monitoringRequest, hostName, notificationURI);
        Set<Kind> kinds = new HashSet<Kind>();
        kinds.add(vmComputeConfiguration);
        provisionRequestType.setKinds(kinds);
        String path = System.currentTimeMillis() + "-" + imageID + slaTypeID + Integer.toString(cores) + Integer.toString(memory) + hostName + ".xml";
        logger.info("Saving request " + path);
        try {
            provisionRequestType.toFile(System.getProperty("java.io.tmpdir") + File.separator + path);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Request could not be saved");
            logger.error(e);
        }
        provisionRequestType.setMonitoringRequest(monitoringRequest);
        return provisionRequestType;
    }

    public String predictionQuery(String id, String metricName, int minutes) throws UnknownIdException {
        return predictionQuery.query(id, metricName, minutes);
    }

    public Compute createComputeConfiguration(String imageID, String slaTypeID, String locationID, int cores, float speed, int memory, String monitoringRequest, String hostName, String notificationURI, Set<Category> categories) {
        return createComputeConfiguration(imageID, slaTypeID, locationID, cores, speed, memory, monitoringRequest, hostName, notificationURI);
    }

    public Compute getCompute(Map<String, String> headers) {
        Compute compute = new Compute();
        compute.setCpu_cores(Integer.parseInt(headers.get(Terms.COMPUTE_CORES)));
        compute.setCpu_speed(Float.parseFloat(headers.get(Terms.COMPUTE_SPEED)));
        compute.setMemory_size(Float.parseFloat(headers.get(Terms.COMPUTE_MEMORY)));
        compute.setHostname(headers.get(Terms.HOSTNAME));
        compute.setStatus(headers.get(Terms.OCCI_COMPUTE_STATUS));
        logger.info("Compute - " + compute);
        return compute;
    }

    public static String create() {
        String sessionID = UUID.randomUUID().toString();
        File file = new File(path);
        boolean sessionDirCreated = file.mkdir();
        logger.info("Create session Dir - ? " + sessionDirCreated);
        return sessionID;
    }

    public static void save(ProvisionResponseType provisionResponseType) throws IOException {
        provisionResponseType.toFile(path + File.separator + provisionResponseType.getInfrastructureID() + ".xml");
    }

    public static void delete(String infrastructureID) throws UnknownIdException {
        logger.info("Deleting session - " + infrastructureID);
        File file = new File(path + File.separator + infrastructureID + ".xml");
        if (!file.exists()) {
            throw new UnknownIdException();
        }
        logger.info("Session deleted - " + file.delete());
    }

    public static List<ProvisionResponseType> getList() {
        File dir = new File(path + File.separator);
        List<ProvisionResponseType> list = new ArrayList<ProvisionResponseType>();
        String[] files = dir.list();
        for (int i = 0; i < files.length; i++) {
            ProvisionResponseType provisionResponseType = null;
            try {
                provisionResponseType = ProvisionResponseType.fromFile(path + File.separator + files[i]);
                list.add(provisionResponseType);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public ProvisionResponseType provision(ProvisionRequestType provisionRequestType) throws DescriptorException, ProvisionException {
        ArrayList<Map<String, String>> multiHeaders = new ArrayList<Map<String, String>>();
        if (provisionRequestType == null) {
            logger.error("ProvisionRequestType cannot be null");
            throw new DescriptorException("ProvisionRequestType cannot be null");
        }
        if (!Validate.HostNames(provisionRequestType)) {
            String message = "Two Compute resources or more with identical names host names";
            logger.error(message);
            throw new DescriptorException(message);
        }
        provisionRequestType.setId(rnd.nextLong());
        String infrastructureID = UUID.randomUUID().toString();
        provisionRequestType.setProvId(infrastructureID);
        Set<Kind> kinds = provisionRequestType.getKinds();
        List<EndPoint> endPoints = new ArrayList<EndPoint>();
        for (Iterator<Kind> iterator = kinds.iterator(); iterator.hasNext(); ) {
            Kind kind = iterator.next();
            if (kind instanceof Compute) {
                Map<String, String> headers = new HashMap<String, String>();
                logger.info("Compute - kind");
                Compute computeKind = (Compute) kind;
                if (((computeKind.getCpu_cores()) == 0) || ((int) computeKind.getMemory_size() == 0) || ((int) computeKind.getCpu_speed()) == 0) {
                    logger.error("ProvisionRequestType must specify valid compute resource [cores,speed,memory]");
                    throw new DescriptorException("ProvisionRequestType must specify valid compute resource [cores,speed,memory]");
                }
                headers.put(Terms.NOTIFICATION_URI, "\"" + provisionRequestType.getNotificationUri() + "\"");
                headers.put(Terms.COMPUTE_CORES, Integer.toString(computeKind.getCpu_cores()));
                headers.put(Terms.COMPUTE_MEMORY, Integer.toString((int) computeKind.getMemory_size()));
                headers.put(Terms.COMPUTE_SPEED, Integer.toString((int) (1000.0f * computeKind.getCpu_speed())));
                ;
                headers.put(Terms.HOSTNAME, "\"" + computeKind.getHostname() + "\"");
                String attributeHeader = "";
                for (Iterator<String> iterator2 = headers.keySet().iterator(); iterator2.hasNext(); ) {
                    String key = iterator2.next();
                    attributeHeader = attributeHeader + key + "=" + headers.get(key) + ", ";
                }
                attributeHeader = attributeHeader.substring(0, attributeHeader.length() - 2);
                logger.info("Attribute Header - " + attributeHeader);
                Set<Category> categories = kind.getCategories();
                String categoryHeader = "";
                for (Iterator<Category> iterator2 = categories.iterator(); iterator2.hasNext(); ) {
                    Category category = (Category) iterator2.next();
                    categoryHeader = categoryHeader + occiclient.createHeader(category.getTerm(), category.getScheme(), category.getTitle()) + ", ";
                }
                headers.clear();
                headers.put("Category", categoryHeader);
                headers.put("Attribute", attributeHeader);
                logger.info("Calling OCCI Server for Single Provisioning");
                multiHeaders.add(headers);
                EndPoint endPoint = new EndPoint();
                Appliance<String> appliance = new Appliance<String>();
                computeKind.setMacAddress(computeKind.getHostname());
                appliance.setImage("image");
                appliance.setCompute(computeKind);
                endPoint.setAppliance(appliance);
                endPoint.setHostName(computeKind.getHostname());
                URL resourceUrl = null;
                try {
                    resourceUrl = new URL("http://" + occiclient.getHostname() + ":" + occiclient.getPort() + occiclient.getServiceResource() + "/");
                    logger.info("The management endpoint for this VM (Resource) is " + resourceUrl.getPath());
                    endPoint.setResourceUrl(resourceUrl);
                    computeKind.setResourceId(endPoint.getResourceUrl().toExternalForm());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    logger.error(e);
                }
                endPoints.add(endPoint);
            }
        }
        String serviceName = provisionRequestType.getName();
        String serviceDescription = provisionRequestType.getDescription();
        String serviceSlaType = provisionRequestType.getSla();
        infrastructureID = occiclient.post(multiHeaders, serviceName, serviceDescription, serviceSlaType, "andy@home.edmonds.be");
        logger.info("Got infrastructureID ? - " + infrastructureID);
        provisionRequestType.setProvId(infrastructureID);
        provisionRequests.put(infrastructureID, provisionRequestType);
        ProvisionResponseType provisionResponseType = new ProvisionResponseType();
        provisionResponseType.setInfrastructureID(infrastructureID);
        for (Iterator<EndPoint> iterator = endPoints.iterator(); iterator.hasNext(); ) {
            EndPoint endPoint = iterator.next();
            logger.info("	EndPoint - getHostName - " + endPoint.getHostName());
            logger.info("	EndPoint - getResourceUrl - " + endPoint.getResourceUrl());
            try {
                endPoint.setResourceUrl(new URL("http://" + occiclient.getHostname() + ":" + occiclient.getPort() + occiclient.getServiceResource() + "/" + infrastructureID));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        provisionResponseType.setEndPoints(endPoints);
        provisionResponseType.setExpiry(new Date(Long.MAX_VALUE));
        provisionResponseType.setEndPoints(endPoints);
        provisionResponses.put(infrastructureID, provisionResponseType);
        provisionResponseType.setDescription(provisionRequestType.getDescription());
        provisionResponseType.setName(provisionRequestType.getName());
        provisionResponseType.setSla(provisionRequestType.getSla());
        logger.info("Saving session");
        logger.info(provisionResponseType);
        try {
            create();
            save(provisionResponseType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provisionResponseType;
    }

    private EndPoint createEndPoint(Compute computeKind) {
        EndPoint endPoint = new EndPoint();
        Appliance<String> appliance = new Appliance<String>();
        computeKind.setMacAddress(computeKind.getHostname());
        appliance.setImage("image");
        appliance.setCompute(computeKind);
        endPoint.setAppliance(appliance);
        endPoint.setHostName(computeKind.getHostname());
        URL resourceUrl = null;
        try {
            resourceUrl = new URL("http://" + occiclient.getHostname() + ":" + occiclient.getPort() + occiclient.getServiceResource() + "/");
            logger.info("The management endpoint for this VM (Resource) is " + resourceUrl.getPath());
            endPoint.setResourceUrl(resourceUrl);
            computeKind.setResourceId(endPoint.getResourceUrl().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error(e);
        }
        return endPoint;
    }

    public ProvisionResponseType createProvisionResponse(List<EndPoint> endPoints, String groupingResponse, ProvisionRequestType provisionRequestType, String infrastructureID) {
        ProvisionResponseType provisionResponseType = new ProvisionResponseType();
        logger.info("Got infrastructureID ? - " + infrastructureID);
        provisionRequestType.setProvId(infrastructureID);
        provisionRequests.put(infrastructureID, provisionRequestType);
        provisionResponseType.setInfrastructureID(infrastructureID);
        for (Iterator<EndPoint> iterator = endPoints.iterator(); iterator.hasNext(); ) {
            EndPoint endPoint = iterator.next();
            logger.info("   EndPoint - getHostName - " + endPoint.getHostName());
            logger.info("   EndPoint - getResourceUrl - " + endPoint.getResourceUrl());
            try {
                endPoint.setResourceUrl(new URL("http://" + occiclient.getHostname() + ":" + occiclient.getPort() + occiclient.getServiceResource() + "/" + infrastructureID));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        provisionResponseType.setEndPoints(endPoints);
        provisionResponseType.setExpiry(new Date(Long.MAX_VALUE));
        provisionResponseType.setEndPoints(endPoints);
        provisionResponses.put(infrastructureID, provisionResponseType);
        provisionResponseType.setDescription(provisionRequestType.getDescription());
        provisionResponseType.setName(provisionRequestType.getName());
        provisionResponseType.setSla(provisionRequestType.getSla());
        logger.info("Saving session");
        logger.info(provisionResponseType);
        try {
            create();
            save(provisionResponseType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provisionResponseType;
    }

    public void validateResponse(String groupingResponse, ProvisionRequestType provisionRequestType, String ID) throws ProvisionException {
        logger.info("This is the response from the ISM Server");
        logger.info("resourceID -  " + groupingResponse);
        if (groupingResponse == null) {
            logger.error("Provision was not sucessful");
            throw new ProvisionException("Provision was not successful");
        }
        provisionRequests.put(ID, provisionRequestType);
    }

    public static String createHeader(String term, String schema, String title) {
        String category = term + ";";
        category = category + " scheme=" + schema + ";";
        category = category + " title=" + title;
        return category;
    }

    public ProvisionResponseType provisionOld(ProvisionRequestType provisionRequestType) throws DescriptorException, ProvisionException {
        if (provisionRequestType == null) {
            logger.error("ProvisionRequestType cannot be null");
            throw new DescriptorException("ProvisionRequestType cannot be null");
        }
        if (!Validate.HostNames(provisionRequestType)) {
            String message = "Two Compute resources or more with identical names host names";
            logger.error(message);
            throw new DescriptorException(message);
        }
        provisionRequestType.setId(rnd.nextLong());
        String infrastructureID = UUID.randomUUID().toString();
        provisionRequestType.setProvId(infrastructureID);
        Set<Kind> kinds = provisionRequestType.getKinds();
        List<EndPoint> endPoints = new ArrayList<EndPoint>();
        for (Iterator<Kind> iterator = kinds.iterator(); iterator.hasNext(); ) {
            Kind kind = iterator.next();
            if (kind instanceof Compute) {
                Map<String, String> headers = new HashMap<String, String>();
                logger.info("Compute - kind");
                Compute computeKind = (Compute) kind;
                if (((computeKind.getCpu_cores()) == 0) || ((int) computeKind.getMemory_size() == 0) || ((int) computeKind.getCpu_speed()) == 0) {
                    logger.error("ProvisionRequestType must specify valid compute resource [cores,speed,memory]");
                    throw new DescriptorException("ProvisionRequestType must specify valid compute resource [cores,speed,memory]");
                }
                headers.put(Terms.NOTIFICATION_URI, "\"" + provisionRequestType.getNotificationUri() + "\"");
                headers.put(Terms.COMPUTE_CORES, Integer.toString(computeKind.getCpu_cores()));
                headers.put(Terms.COMPUTE_MEMORY, Integer.toString((int) computeKind.getMemory_size()));
                headers.put(Terms.COMPUTE_SPEED, Integer.toString((int) (1000.0f * computeKind.getCpu_speed())));
                ;
                headers.put(Terms.HOSTNAME, "\"" + computeKind.getHostname() + "\"");
                String attributeHeader = "";
                for (Iterator<String> iterator2 = headers.keySet().iterator(); iterator2.hasNext(); ) {
                    String key = iterator2.next();
                    attributeHeader = attributeHeader + key + "=" + headers.get(key) + ", ";
                }
                attributeHeader = attributeHeader.substring(0, attributeHeader.length() - 2);
                logger.info("Attribute Header - " + attributeHeader);
                Set<Category> categories = kind.getCategories();
                String categoryHeader = "";
                for (Iterator<Category> iterator2 = categories.iterator(); iterator2.hasNext(); ) {
                    Category category = (Category) iterator2.next();
                    categoryHeader = categoryHeader + occiclient.createHeader(category.getTerm(), category.getScheme(), category.getTitle()) + ", ";
                }
                headers.clear();
                headers.put("Category", categoryHeader);
                headers.put("Attribute", attributeHeader);
                logger.info("Calling OCCI Server for Single Provisioning");
                String resourceID = occiclient.post(headers, provisionRequestType.getMonitoringRequest());
                logger.info("This is the response from the ISM Server");
                logger.info("resourceID -  " + resourceID);
                if (resourceID == null) {
                    logger.error("Provision was not sucessful");
                    throw new ProvisionException("Provision was not successful");
                }
                EndPoint endPoint = new EndPoint();
                Appliance<String> appliance = new Appliance<String>();
                computeKind.setMacAddress(computeKind.getHostname());
                appliance.setImage(resourceID);
                appliance.setCompute(computeKind);
                endPoint.setAppliance(appliance);
                endPoint.setHostName(computeKind.getHostname());
                URL resourceUrl = null;
                try {
                    resourceUrl = new URL("http://" + occiclient.getHostname() + ":" + occiclient.getPort() + occiclient.getResource() + "/" + resourceID);
                    logger.info("The management endpoint for this VM (Resource) is " + resourceUrl.getPath());
                    endPoint.setResourceUrl(resourceUrl);
                    computeKind.setResourceId(endPoint.getResourceUrl().toExternalForm());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    logger.error(e);
                }
                endPoints.add(endPoint);
            }
        }
        provisionRequests.put(infrastructureID, provisionRequestType);
        ProvisionResponseType provisionResponseType = new ProvisionResponseType();
        provisionResponseType.setInfrastructureID(infrastructureID);
        provisionResponseType.setExpiry(new Date(Long.MAX_VALUE));
        provisionResponseType.setEndPoints(endPoints);
        provisionResponses.put(infrastructureID, provisionResponseType);
        provisionResponseType.setDescription(provisionRequestType.getDescription());
        provisionResponseType.setName(provisionRequestType.getName());
        provisionResponseType.setSla(provisionRequestType.getSla());
        logger.info("Saving session");
        logger.info(provisionResponseType);
        try {
            create();
            save(provisionResponseType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return provisionResponseType;
    }

    public void setImageregistry(org.slasoi.infrastructure.servicemanager.registry.Registry imageregistry) {
        this.imageregistry = imageregistry;
    }

    public void setSlatyperegistry(Registry slatyperegistry) {
        this.slatyperegistry = slatyperegistry;
    }

    public void setLocationregistry(Registry locationregistry) {
        this.locationregistry = locationregistry;
    }

    public OcciClient getOcciclient() {
        return occiclient;
    }

    /**
	     * Sets the @OcciClient.
	     * @param occiclient
	     * The @OcciClient.
	     */
    public void setOcciclient(OcciClient occiclient) {
        this.occiclient = occiclient;
    }

    /**
	     * Sets the @PredictionQuery.
	     * 
	     * @param predictionQuery
	     * The @PredictionQuery
	     */
    public void setPredictionQuery(PredictionQuery predictionQuery) {
        this.predictionQuery = predictionQuery;
    }
}
