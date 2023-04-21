package org.dasein.cloud.cloudstack;

import java.io.IOException;
import java.security.SignatureException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.OperationNotSupportedException;
import org.dasein.cloud.cloudstack.Volumes.DiskOffering;
import org.dasein.cloud.services.dc.DataCenter;
import org.dasein.cloud.services.dc.Region;
import org.dasein.cloud.services.server.Architecture;
import org.dasein.cloud.services.server.Platform;
import org.dasein.cloud.services.server.Server;
import org.dasein.cloud.services.server.ServerPersistence;
import org.dasein.cloud.services.server.ServerServices;
import org.dasein.cloud.services.server.ServerSize;
import org.dasein.cloud.services.server.ServerState;
import org.dasein.cloud.services.server.ServerStatistics;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class VirtualMachines implements ServerServices {

    public static final Logger logger = Logger.getLogger(VirtualMachines.class);

    private static final String DEPLOY_VIRTUAL_MACHINE = "deployVirtualMachine";

    private static final String DESTROY_VIRTUAL_MACHINE = "destroyVirtualMachine";

    private static final String LIST_VIRTUAL_MACHINES = "listVirtualMachines";

    private static final String LIST_SERVICE_OFFERINGS = "listServiceOfferings";

    private static final String REBOOT_VIRTUAL_MACHINE = "rebootVirtualMachine";

    private static final String START_VIRTUAL_MACHINE = "startVirtualMachine";

    private static final String STOP_VIRTUAL_MACHINE = "stopVirtualMachine";

    private CloudstackProvider provider;

    public VirtualMachines(CloudstackProvider provider) {
        this.provider = provider;
    }

    public void boot(String serverId) throws InternalException, CloudException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        int code;
        try {
            get.setPath(provider.buildUrl(START_VIRTUAL_MACHINE, new Param[] { new Param("id", serverId) }));
            get.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        } catch (SignatureException e) {
            throw new InternalException("Failed to generate a proper signature: " + e.getMessage());
        }
        try {
            code = client.executeMethod(get);
        } catch (HttpException e) {
            throw new InternalException("HttpException during GET: " + e.getMessage());
        } catch (IOException e) {
            throw new CloudException("IOException during GET: " + e.getMessage());
        }
        if (code != HttpStatus.SC_OK) {
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
    }

    public Server clone(String serverId, String intoDcId, String name, String description, boolean powerOn, String... firewallIds) throws InternalException, CloudException {
        throw new OperationNotSupportedException("Instances cannot be cloned.");
    }

    public Server define(String imageId, ServerSize offering, String zoneId, String name, boolean withAnalytics, String... firewalls) throws InternalException, CloudException {
        throw new OperationNotSupportedException("You must launch servers in this cloud.");
    }

    public String getConsoleOutput(String serverId, long sinceTimestamp) throws InternalException, CloudException {
        return "";
    }

    public Collection<String> getFirewalls(String serverId) throws InternalException, CloudException {
        return new ArrayList<String>();
    }

    public String getProviderTermForServer(Locale locale) {
        return "virtual machine";
    }

    public Server getServer(String serverId) throws InternalException, CloudException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        Document doc;
        int code;
        try {
            get.setPath(provider.buildUrl(LIST_VIRTUAL_MACHINES, new Param[0]));
            get.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        } catch (SignatureException e) {
            throw new InternalException("Unable to generate a valid signature: " + e.getMessage());
        }
        try {
            code = client.executeMethod(get);
        } catch (HttpException e) {
            throw new InternalException("HttpException during GET: " + e.getMessage());
        } catch (IOException e) {
            throw new CloudException("IOException during GET: " + e.getMessage());
        }
        if (code != HttpStatus.SC_OK) {
            if (code == 401) {
                throw new CloudException("Unauthorized user");
            } else if (code == 430) {
                throw new InternalException("Malformed parameters");
            } else if (code == 547 || code == 530) {
                throw new CloudException("Server error in cloud (" + code + ")");
            } else if (code == 531) {
                throw new CloudException("Unable to find account");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        NodeList matches = doc.getElementsByTagName("virtualmachine");
        if (matches.getLength() < 1) {
            return null;
        }
        for (int i = 0; i < matches.getLength(); i++) {
            Server s = toServer(matches.item(i));
            if (s.getProviderServerId().equals(serverId)) {
                return s;
            }
        }
        return null;
    }

    public ServerPersistence getServerPersistence() throws InternalException, CloudException {
        return ServerPersistence.ALL;
    }

    public ServerStatistics getServerStatistics(String serverId, long startTimestamp, long endTimestamp) throws InternalException, CloudException {
        return new ServerStatistics();
    }

    public Collection<ServerSize> getSupportedSizes(Architecture forArchitecture) throws InternalException, CloudException {
        Collection<DiskOffering> diskOfferings = ((Volumes) provider.getVolumeServices()).getDiskOfferings();
        ArrayList<ServerSize> sizes = new ArrayList<ServerSize>();
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        Document doc;
        int code;
        try {
            get.setPath(provider.buildUrl(LIST_SERVICE_OFFERINGS, new Param[0]));
            get.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        } catch (SignatureException e) {
            throw new InternalException("Unable to generate a valid signature: " + e.getMessage());
        }
        try {
            code = client.executeMethod(get);
        } catch (HttpException e) {
            throw new InternalException("HttpException during GET: " + e.getMessage());
        } catch (IOException e) {
            throw new CloudException("IOException during GET: " + e.getMessage());
        }
        if (code != HttpStatus.SC_OK) {
            if (code == 401) {
                throw new CloudException("Unauthorized user");
            } else if (code == 430) {
                throw new InternalException("Malformed parameters");
            } else if (code == 547 || code == 530) {
                throw new CloudException("Server error in cloud (" + code + ")");
            } else if (code == 531) {
                throw new CloudException("Unable to find account");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        NodeList matches = doc.getElementsByTagName("serviceoffering");
        for (int i = 0; i < matches.getLength(); i++) {
            String id = null, name = null;
            Node node = matches.item(i);
            NodeList attributes;
            int memory = 0;
            int cpu = 0;
            attributes = node.getChildNodes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node n = attributes.item(j);
                String value;
                if (n.getChildNodes().getLength() > 0) {
                    value = n.getFirstChild().getNodeValue();
                } else {
                    value = null;
                }
                if (n.getNodeName().equals("id")) {
                    id = value;
                } else if (n.getNodeName().equals("name")) {
                    name = value;
                } else if (n.getNodeName().equals("cpunumber")) {
                    cpu = Integer.parseInt(value);
                } else if (n.getNodeName().equals("memory")) {
                    memory = Integer.parseInt(value);
                }
                if (id != null && name != null && cpu > 0 && memory > 0) {
                    break;
                }
            }
            if (id != null) {
                for (DiskOffering size : diskOfferings) {
                    String nid = id + ":" + size.id;
                    String nname = name + " (" + cpu + " CPU/" + memory + "MB RAM/" + (size.diskSize / 1024000000) + "GB Disk)";
                    sizes.add(new ServerSize(nid, nname));
                }
            }
        }
        return sizes;
    }

    public boolean isSupportsCloning() {
        return false;
    }

    public Server launch(String imageId, ServerSize size, String inZoneId, String name, String usingKey, String withVlanId, boolean withMonitoring, String... protectedByFirewalls) throws InternalException, CloudException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        Document doc;
        int code;
        inZoneId = translateZone(inZoneId);
        try {
            String offerings[] = size.getSizeId().split(":");
            get.setPath(provider.buildUrl(DEPLOY_VIRTUAL_MACHINE, new Param("zoneId", inZoneId), new Param("serviceOfferingId", offerings[0]), new Param("diskOfferingId", offerings[1]), new Param("templateId", imageId), new Param("displayName", name)));
            get.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        } catch (SignatureException e) {
            throw new InternalException("Unable to generate a valid signature: " + e.getMessage());
        }
        try {
            code = client.executeMethod(get);
        } catch (HttpException e) {
            throw new InternalException("HttpException during GET: " + e.getMessage());
        } catch (IOException e) {
            throw new CloudException("IOException during GET: " + e.getMessage());
        }
        if (code != HttpStatus.SC_OK) {
            if (code == 401) {
                throw new CloudException("Unauthorized user");
            } else if (code == 430) {
                throw new InternalException("Malformed parameters");
            } else if (code == 431) {
                throw new InternalException("Invalid parameters");
            } else if (code == 540 || code == 530) {
                throw new CloudException("Server error in cloud (" + code + ")");
            } else if (code == 531) {
                throw new CloudException("Unable to find account");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream(), true);
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        NodeList matches = doc.getElementsByTagName("deployvirtualmachineresponse");
        String serverId = null;
        for (int i = 0; i < matches.getLength(); i++) {
            NodeList attrs = matches.item(i).getChildNodes();
            for (int j = 0; j < attrs.getLength(); j++) {
                Node node = attrs.item(j);
                if (node != null && node.getNodeName().equalsIgnoreCase("virtualmachineid")) {
                    serverId = node.getFirstChild().getNodeValue();
                    break;
                }
            }
            if (serverId != null) {
                break;
            }
        }
        if (serverId == null) {
            throw new CloudException("Could not launch server");
        }
        provider.waitForJob(doc, "Launch Server");
        return getServer(serverId);
    }

    public Collection<Server> list() throws InternalException, CloudException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        Document doc;
        int code;
        try {
            get.setPath(provider.buildUrl(LIST_VIRTUAL_MACHINES, new Param[0]));
            get.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        } catch (SignatureException e) {
            throw new InternalException("Unable to generate a valid signature: " + e.getMessage());
        }
        try {
            code = client.executeMethod(get);
        } catch (HttpException e) {
            throw new InternalException("HttpException during GET: " + e.getMessage());
        } catch (IOException e) {
            throw new CloudException("IOException during GET: " + e.getMessage());
        }
        if (code != HttpStatus.SC_OK) {
            if (code == 401) {
                throw new CloudException("Unauthorized user");
            } else if (code == 430) {
                throw new InternalException("Malformed parameters");
            } else if (code == 547 || code == 530) {
                throw new CloudException("Server error in cloud (" + code + ")");
            } else if (code == 531) {
                throw new CloudException("Unable to find account");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        ArrayList<Server> servers = new ArrayList<Server>();
        NodeList matches = doc.getElementsByTagName("virtualmachine");
        for (int i = 0; i < matches.getLength(); i++) {
            Node node = matches.item(i);
            servers.add(toServer(node));
        }
        return servers;
    }

    public void monitor(String serverId) throws InternalException, CloudException {
    }

    public void pause(String serverId) throws InternalException, CloudException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        Document doc;
        int code;
        try {
            get.setPath(provider.buildUrl(STOP_VIRTUAL_MACHINE, new Param[] { new Param("id", serverId) }));
            get.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        } catch (SignatureException e) {
            throw new InternalException("Failed to generate a proper signature: " + e.getMessage());
        }
        try {
            code = client.executeMethod(get);
        } catch (HttpException e) {
            throw new InternalException("HttpException during GET: " + e.getMessage());
        } catch (IOException e) {
            throw new CloudException("IOException during GET: " + e.getMessage());
        }
        if (code != HttpStatus.SC_OK) {
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        provider.waitForJob(doc, "Pause Server");
    }

    public void reboot(String serverId) throws CloudException, InternalException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        int code;
        try {
            get.setPath(provider.buildUrl(REBOOT_VIRTUAL_MACHINE, new Param[] { new Param("id", serverId) }));
            get.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        } catch (SignatureException e) {
            throw new InternalException("Failed to generate a proper signature: " + e.getMessage());
        }
        try {
            code = client.executeMethod(get);
        } catch (HttpException e) {
            throw new InternalException("HttpException during GET: " + e.getMessage());
        } catch (IOException e) {
            throw new CloudException("IOException during GET: " + e.getMessage());
        }
        if (code != HttpStatus.SC_OK) {
            if (code == 401) {
                throw new CloudException("Unauthorized user");
            } else if (code == 430) {
                throw new InternalException("Malformed parameters");
            } else if (code == 431) {
                throw new InternalException("Invalid parameters");
            } else if (code == 542 || code == 530) {
                throw new CloudException("Server error in cloud (" + code + ")");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
    }

    public void stop(String serverId) throws InternalException, CloudException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        int code;
        try {
            get.setPath(provider.buildUrl(DESTROY_VIRTUAL_MACHINE, new Param[] { new Param("id", serverId) }));
            get.getParams().setCookiePolicy(CookiePolicy.IGNORE_COOKIES);
        } catch (SignatureException e) {
            throw new InternalException("Failed to generate a proper signature: " + e.getMessage());
        }
        try {
            code = client.executeMethod(get);
        } catch (HttpException e) {
            throw new InternalException("HttpException during GET: " + e.getMessage());
        } catch (IOException e) {
            throw new CloudException("IOException during GET: " + e.getMessage());
        }
        if (code != HttpStatus.SC_OK) {
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
    }

    private String translateZone(String zoneId) throws InternalException, CloudException {
        if (zoneId == null) {
            for (Region r : provider.getDataCenterServices().listRegions()) {
                zoneId = r.getProviderRegionId();
                break;
            }
        } else {
            boolean found = false;
            for (Region r : provider.getDataCenterServices().listRegions()) {
                for (DataCenter dc : provider.getDataCenterServices().listDataCenters(r.getProviderRegionId())) {
                    if (zoneId.equals(dc.getProviderDataCenterId())) {
                        zoneId = r.getProviderRegionId();
                        found = true;
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
        }
        return zoneId;
    }

    public void unmonitor(String serverId) throws InternalException, CloudException {
    }

    private Server toServer(Node node) throws CloudException {
        NodeList attributes = node.getChildNodes();
        Server server = new Server();
        server.setArchitecture(Architecture.I64);
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            String name = attribute.getNodeName().toLowerCase();
            String value;
            if (attribute.getChildNodes().getLength() > 0) {
                value = attribute.getFirstChild().getNodeValue();
            } else {
                value = null;
            }
            if (name.equals("virtualmachineid") || name.equals("id")) {
                server.setProviderServerId(value);
            } else if (name.equals("name")) {
                server.setDescription(value);
            } else if (name.equals("displayname")) {
                server.setName(value);
            } else if (name.equals("ipaddress")) {
                server.setPrivateIpAddresses(value);
                server.setPrivateDnsAddress(value);
            } else if (name.equals("haenable")) {
                server.setPersistent(value != null && value.equalsIgnoreCase("true"));
            } else if (name.equals("password")) {
                server.setRootPassword(value);
            } else if (name.equals("osarchitecture")) {
                if (value != null && value.equals("32")) {
                    server.setArchitecture(Architecture.I32);
                } else {
                    server.setArchitecture(Architecture.I64);
                }
            } else if (name.equals("created")) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                try {
                    server.setStartTime(df.parse(value).getTime());
                } catch (ParseException e) {
                    logger.warn("Invalid date: " + value);
                    server.setStartTime(0L);
                }
            } else if (name.equals("state")) {
                ServerState state;
                if (value.equalsIgnoreCase("stopped")) {
                    state = ServerState.PAUSED;
                } else if (value.equalsIgnoreCase("running")) {
                    state = ServerState.RUNNING;
                } else if (value.equalsIgnoreCase("stopping")) {
                    state = ServerState.REBOOTING;
                } else if (value.equalsIgnoreCase("starting")) {
                    state = ServerState.PENDING;
                } else if (value.equalsIgnoreCase("creating")) {
                    state = ServerState.PENDING;
                } else if (value.equalsIgnoreCase("migrating")) {
                    state = ServerState.REBOOTING;
                } else if (value.equalsIgnoreCase("destroyed")) {
                    state = ServerState.TERMINATED;
                } else if (value.equalsIgnoreCase("ha")) {
                    state = ServerState.REBOOTING;
                } else {
                    throw new CloudException("Unexpected server state: " + value);
                }
                server.setCurrentState(state);
            } else if (name.equals("zoneid")) {
                server.setRegionId(value);
                server.setDataCenterId(Zones.getDataCenterForRegion(value));
            } else if (name.equals("templateid")) {
                server.setImageId(value);
            } else if (name.equals("templatename")) {
                server.setPlatform(Platform.guess(value));
            } else if (name.equals("serviceofferingid")) {
                server.setSize(value);
            }
        }
        if (server.getName() == null) {
            server.setName(server.getProviderServerId());
        }
        if (server.getDescription() == null) {
            server.setDescription(server.getName());
        }
        server.setReservedAddressId(null);
        if (server.getRegionId() == null) {
            server.setRegionId(provider.getContext().getRegionId());
        }
        if (server.getDataCenterId() == null) {
            server.setDataCenterId(Zones.getDataCenterForRegion(server.getRegionId()));
        }
        return server;
    }

    public Collection<ServerStatistics> getServerStatisticsForPeriod(String arg0, long arg1, long arg2) throws InternalException, CloudException {
        return new ArrayList<ServerStatistics>();
    }
}
