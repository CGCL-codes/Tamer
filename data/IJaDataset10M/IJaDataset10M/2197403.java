package org.dasein.cloud.cloudstack;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.OperationNotSupportedException;
import org.dasein.cloud.services.dc.DataCenter;
import org.dasein.cloud.services.lb.LbAlgorithm;
import org.dasein.cloud.services.lb.LbListener;
import org.dasein.cloud.services.lb.LbProtocol;
import org.dasein.cloud.services.lb.LoadBalancer;
import org.dasein.cloud.services.lb.LoadBalancerAddressType;
import org.dasein.cloud.services.lb.LoadBalancerServices;
import org.dasein.cloud.services.lb.LoadBalancerState;
import org.dasein.cloud.services.server.Server;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class LoadBalancers implements LoadBalancerServices {

    public static final String ASSIGN_TO_LOAD_BALANCER_RULE = "assignToLoadBalancerRule";

    public static final String CREATE_LOAD_BALANCER_RULE = "createLoadBalancerRule";

    public static final String DELETE_LOAD_BALANCER_RULE = "deleteLoadBalancerRule";

    public static final String LIST_LOAD_BALANCER_RULES = "listLoadBalancerRules";

    public static final String LIST_LOAD_BALANCER_RULE_INSTANCES = "listLoadBalancerRuleInstances";

    public static final String REMOVE_FROM_LOAD_BALANCER_RULE = "removeFromLoadBalancerRule";

    private CloudstackProvider provider;

    LoadBalancers(CloudstackProvider provider) {
        this.provider = provider;
    }

    public void addDataCenters(String toLoadBalancerId, String... dataCenterIds) throws CloudException, InternalException {
    }

    public void addServers(String toLoadBalancerId, String... serverIds) throws CloudException, InternalException {
        try {
            LoadBalancer lb = getLoadBalancer(toLoadBalancerId);
            if (lb == null) {
                throw new CloudException("No such load balancer: " + toLoadBalancerId);
            }
            if (serverIds == null || serverIds.length < 1) {
                return;
            }
            for (LbListener listener : lb.getListeners()) {
                String ruleId = getVmOpsRuleId(listener.getAlgorithm(), toLoadBalancerId, listener.getPublicPort(), listener.getPrivatePort());
                StringBuilder str = new StringBuilder();
                for (int i = 0; i < serverIds.length; i++) {
                    str.append(serverIds[i]);
                    if (i < serverIds.length - 1) {
                        str.append(",");
                    }
                }
                HttpClient client = new HttpClient();
                GetMethod get = new GetMethod();
                Document doc;
                int code;
                try {
                    get.setPath(provider.buildUrl(ASSIGN_TO_LOAD_BALANCER_RULE, new Param[] { new Param("id", ruleId), new Param("virtualMachineIds", str.toString()) }));
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
                    } else if (code == 431 || code == 436) {
                        throw new InternalException("Invalid parameters");
                    } else if (code == 530 || code == 567) {
                        throw new CloudException("Server error in cloud (" + code + ")");
                    }
                    throw new CloudException("Received error code from server: " + code);
                }
                try {
                    doc = provider.parseResponse(get.getResponseBodyAsStream());
                } catch (IOException e) {
                    throw new CloudException("IOException getting stream: " + e.getMessage());
                }
                provider.waitForJob(doc, "Add Server");
            }
        } catch (RuntimeException e) {
            throw new InternalException(e);
        } catch (Error e) {
            throw new InternalException(e);
        }
    }

    public String create(String name, String description, String addressId, String[] dcIds, LbListener[] listeners, String[] servers) throws CloudException, InternalException {
        try {
            org.dasein.cloud.services.address.IpAddress publicAddress = provider.getAddressServices().getIpAddress(addressId);
            if (publicAddress == null) {
                throw new CloudException("You must specify the IP address for your load balancer.");
            }
            for (LbListener listener : listeners) {
                createVmOpsRule(listener.getAlgorithm(), publicAddress.getAddress(), listener.getPublicPort(), listener.getPrivatePort());
            }
            if (servers != null) {
                this.addServers(publicAddress.getAddress(), servers);
            }
            return publicAddress.getAddress();
        } catch (CloudException e) {
            e.printStackTrace();
            throw e;
        } catch (InternalException e) {
            e.printStackTrace();
            throw e;
        } catch (RuntimeException e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void createVmOpsRule(LbAlgorithm algorithm, String publicIp, int publicPort, int privatePort) throws CloudException, InternalException {
        String id = getVmOpsRuleId(algorithm, publicIp, publicPort, privatePort);
        if (id != null) {
            return;
        }
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        Document doc;
        int code;
        try {
            Param[] params = new Param[6];
            String algor;
            switch(algorithm) {
                case ROUND_ROBIN:
                    algor = "roundrobin";
                    break;
                case LEAST_CONN:
                    algor = "leastconn";
                    break;
                case SOURCE:
                    algor = "source";
                    break;
                default:
                    algor = "roundrobin";
                    break;
            }
            params[0] = new Param("publicIp", publicIp);
            params[1] = new Param("publicPort", String.valueOf(publicPort));
            params[2] = new Param("privatePort", String.valueOf(privatePort));
            params[3] = new Param("algorithm", algor);
            params[4] = new Param("name", "dsnlb_" + publicIp + "_" + publicPort + "_" + privatePort);
            params[5] = new Param("description", "dsnlb_" + publicIp + "_" + publicPort + "_" + privatePort);
            get.setPath(provider.buildUrl(CREATE_LOAD_BALANCER_RULE, params));
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
            } else if (code == 431 || code == 436) {
                throw new InternalException("Invalid parameters");
            } else if (code == 530 || code == 567) {
                throw new CloudException("Server error in cloud (" + code + ")");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        NodeList matches = doc.getElementsByTagName("loadbalancerrule");
        for (int i = 0; i < matches.getLength(); i++) {
            HashMap<String, LoadBalancer> current = new HashMap<String, LoadBalancer>();
            Collection<LoadBalancer> lbs;
            Node node = matches.item(i);
            toRule(node, current);
            lbs = current.values();
            if (lbs.size() > 0) {
                return;
            }
        }
        throw new CloudException("Failed to add load balancer rule (2).");
    }

    public LoadBalancerAddressType getAddressType() throws CloudException, InternalException {
        return LoadBalancerAddressType.IP;
    }

    public LoadBalancer getLoadBalancer(String loadBalancerId) throws CloudException, InternalException {
        for (LoadBalancer lb : list()) {
            if (lb.getProviderLoadBalancerId().equals(loadBalancerId)) {
                return lb;
            }
        }
        return null;
    }

    public int getMaxPublicPorts() throws CloudException, InternalException {
        return 0;
    }

    public String getProviderTermForLoadBalancer(Locale locale) {
        return "load balancer";
    }

    private static volatile List<LbAlgorithm> algorithms = null;

    public Iterable<LbAlgorithm> getSupportedAlgorithms() {
        List<LbAlgorithm> list = algorithms;
        if (list == null) {
            list = new ArrayList<LbAlgorithm>();
            list.add(LbAlgorithm.ROUND_ROBIN);
            list.add(LbAlgorithm.LEAST_CONN);
            list.add(LbAlgorithm.SOURCE);
            algorithms = Collections.unmodifiableList(list);
        }
        return algorithms;
    }

    private static volatile List<LbProtocol> protocols = null;

    public Iterable<LbProtocol> getSupportedProtocols() {
        List<LbProtocol> list = protocols;
        if (protocols == null) {
            list = new ArrayList<LbProtocol>();
            list.add(LbProtocol.RAW_TCP);
            protocols = Collections.unmodifiableList(list);
        }
        return protocols;
    }

    private Collection<String> getServersAt(String ruleId) throws InternalException, CloudException {
        ArrayList<String> ids = new ArrayList<String>();
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        Document doc;
        int code;
        try {
            get.setPath(provider.buildUrl(LIST_LOAD_BALANCER_RULE_INSTANCES, new Param[] { new Param("id", ruleId) }));
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
            } else if (code == 530 || code == 570) {
                throw new CloudException("Server error in cloud (" + code + ")");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        NodeList instances = doc.getElementsByTagName("loadbalancerruleinstance");
        for (int i = 0; i < instances.getLength(); i++) {
            Node node = instances.item(i);
            NodeList attributes = node.getChildNodes();
            for (int j = 0; j < attributes.getLength(); j++) {
                Node n = attributes.item(j);
                if (n.getNodeName().equals("id")) {
                    ids.add(n.getFirstChild().getNodeValue());
                }
            }
        }
        return ids;
    }

    private String getVmOpsRuleId(LbAlgorithm lbAlgorithm, String publicIp, int publicPort, int privatePort) throws CloudException, InternalException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        String ruleId = null;
        String algorithm;
        Document doc;
        int code;
        switch(lbAlgorithm) {
            case ROUND_ROBIN:
                algorithm = "roundrobin";
                break;
            case LEAST_CONN:
                algorithm = "leastconn";
                break;
            case SOURCE:
                algorithm = "source";
                break;
            default:
                algorithm = "roundrobin";
                break;
        }
        try {
            get.setPath(provider.buildUrl(LIST_LOAD_BALANCER_RULES, new Param[] { new Param("publicIp", publicIp) }));
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
            } else if (code == 530 || code == 570) {
                throw new CloudException("Server error in cloud (" + code + ")");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        NodeList rules = doc.getElementsByTagName("loadbalancerrule");
        for (int i = 0; i < rules.getLength(); i++) {
            Node node = rules.item(i);
            NodeList attributes = node.getChildNodes();
            boolean isIt = true;
            String id = null;
            for (int j = 0; j < attributes.getLength(); j++) {
                Node n = attributes.item(j);
                String name = n.getNodeName().toLowerCase();
                String value;
                if (n.getChildNodes().getLength() > 0) {
                    value = n.getFirstChild().getNodeValue();
                } else {
                    value = null;
                }
                if (name.equals("publicip")) {
                    if (!value.equals(publicIp)) {
                        isIt = false;
                        break;
                    }
                } else if (name.equals("publicport")) {
                    if (value == null || publicPort != Integer.parseInt(value)) {
                        isIt = false;
                        break;
                    }
                } else if (name.equals("privateport")) {
                    if (value == null || privatePort != Integer.parseInt(value)) {
                        isIt = false;
                        break;
                    }
                } else if (name.equals("algorithm")) {
                    if (value == null || !value.equals(algorithm)) {
                        isIt = false;
                        break;
                    }
                } else if (name.equals("id")) {
                    id = value;
                }
            }
            if (isIt) {
                ruleId = id;
                break;
            }
        }
        return ruleId;
    }

    public boolean isAddressAssignedByProvider() throws CloudException, InternalException {
        return false;
    }

    public boolean isDataCenterLimited() {
        return false;
    }

    public boolean isRequiresListenerOnCreate() throws CloudException, InternalException {
        return true;
    }

    public boolean isRequiresServerOnCreate() throws CloudException, InternalException {
        return false;
    }

    public boolean isSubscribed() throws CloudException, InternalException {
        return true;
    }

    public boolean isSupportsMonitoring() {
        return false;
    }

    public Collection<LoadBalancer> list() throws CloudException, InternalException {
        HashMap<String, LoadBalancer> matches = new HashMap<String, LoadBalancer>();
        for (org.dasein.cloud.services.address.IpAddress ipAddress : provider.getAddressServices().listPublicIpPool(false)) {
            HttpClient client = new HttpClient();
            GetMethod get = new GetMethod();
            Document doc;
            int code;
            try {
                get.setPath(provider.buildUrl(LIST_LOAD_BALANCER_RULES, new Param[] { new Param("publicIp", ipAddress.getAddress()) }));
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
                } else if (code == 530 || code == 570) {
                    throw new CloudException("Server error in cloud (" + code + ")");
                }
                throw new CloudException("Received error code from server: " + code);
            }
            try {
                doc = provider.parseResponse(get.getResponseBodyAsStream());
            } catch (IOException e) {
                throw new CloudException("IOException getting stream: " + e.getMessage());
            }
            NodeList rules = doc.getElementsByTagName("loadbalancerrule");
            for (int i = 0; i < rules.getLength(); i++) {
                Node node = rules.item(i);
                toRule(node, matches);
            }
        }
        return matches.values();
    }

    public void remove(String loadBalancerId) throws CloudException, InternalException {
        LoadBalancer lb = getLoadBalancer(loadBalancerId);
        for (String serverId : lb.getProviderServerIds()) {
            Server server = provider.getServerServices().getServer(serverId);
            if (server != null) {
                for (LbListener listener : lb.getListeners()) {
                    String ruleId = getVmOpsRuleId(listener.getAlgorithm(), lb.getAddress(), listener.getPublicPort(), listener.getPrivatePort());
                    if (ruleId != null) {
                        removeVmOpsRule(ruleId);
                    }
                }
            }
        }
    }

    public void removeDataCenters(String fromLoadBalancerId, String... dataCenterIds) throws CloudException, InternalException {
        throw new OperationNotSupportedException("These load balancers are not data center based.");
    }

    public void removeServers(String toLoadBalancerId, String... serverIds) throws CloudException, InternalException {
        try {
            LoadBalancer lb = getLoadBalancer(toLoadBalancerId);
            if (lb == null) {
                throw new CloudException("No such load balancer: " + toLoadBalancerId);
            }
            StringBuilder ids = new StringBuilder();
            for (int i = 0; i < serverIds.length; i++) {
                ids.append(serverIds[i]);
                if (i < serverIds.length - 1) {
                    ids.append(",");
                }
            }
            for (LbListener listener : lb.getListeners()) {
                String ruleId = getVmOpsRuleId(listener.getAlgorithm(), toLoadBalancerId, listener.getPublicPort(), listener.getPrivatePort());
                HttpClient client = new HttpClient();
                GetMethod get = new GetMethod();
                Document doc;
                int code;
                try {
                    get.setPath(provider.buildUrl(REMOVE_FROM_LOAD_BALANCER_RULE, new Param[] { new Param("id", ruleId), new Param("virtualMachineIds", ids.toString()) }));
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
                    } else if (code == 530 || code == 567) {
                        throw new CloudException("Server error in cloud (" + code + ")");
                    }
                    throw new CloudException("Received error code from server: " + code);
                }
                try {
                    doc = provider.parseResponse(get.getResponseBodyAsStream());
                } catch (IOException e) {
                    throw new CloudException("IOException getting stream: " + e.getMessage());
                }
                provider.waitForJob(doc, "Remove Server");
            }
        } catch (RuntimeException e) {
            throw new InternalException(e);
        } catch (Error e) {
            throw new InternalException(e);
        }
    }

    private void removeVmOpsRule(String ruleId) throws CloudException, InternalException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        Document doc;
        int code;
        try {
            get.setPath(provider.buildUrl(DELETE_LOAD_BALANCER_RULE, new Param[] { new Param("id", ruleId) }));
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
            } else if (code == 530 || code == 567) {
                throw new CloudException("Server error in cloud (" + code + ")");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        provider.waitForJob(doc, "Remove Load Balancer Rule");
    }

    private void toRule(Node node, Map<String, LoadBalancer> current) throws InternalException, CloudException {
        NodeList attributes = node.getChildNodes();
        int publicPort = -1, privatePort = -1;
        LbAlgorithm algorithm = null;
        String publicIp = null;
        String ruleId = null;
        for (int i = 0; i < attributes.getLength(); i++) {
            Node n = attributes.item(i);
            String name = n.getNodeName().toLowerCase();
            String value;
            if (n.getChildNodes().getLength() > 0) {
                value = n.getFirstChild().getNodeValue();
            } else {
                value = null;
            }
            if (name.equals("publicip")) {
                publicIp = value;
            } else if (name.equals("id")) {
                ruleId = value;
            } else if (name.equals("publicport") && value != null) {
                publicPort = Integer.parseInt(value);
            } else if (name.equals("privateport") && value != null) {
                privatePort = Integer.parseInt(value);
            } else if (name.equals("algorithm")) {
                if (algorithm == null || algorithm.equals("roundrobin")) {
                    algorithm = LbAlgorithm.ROUND_ROBIN;
                } else if (algorithm.equals("leastconn")) {
                    algorithm = LbAlgorithm.LEAST_CONN;
                } else if (algorithm.equals("")) {
                    algorithm = LbAlgorithm.SOURCE;
                } else {
                    algorithm = LbAlgorithm.ROUND_ROBIN;
                }
            }
        }
        LbListener listener = new LbListener();
        listener.setAlgorithm(algorithm);
        listener.setNetworkProtocol(LbProtocol.RAW_TCP);
        listener.setPrivatePort(privatePort);
        listener.setPublicPort(publicPort);
        Collection<String> serverIds = getServersAt(ruleId);
        if (current.containsKey(publicIp)) {
            LoadBalancer lb = current.get(publicIp);
            LbListener[] listeners = lb.getListeners();
            String[] currentIds = lb.getProviderServerIds();
            TreeSet<Integer> ports = new TreeSet<Integer>();
            for (int port : lb.getPublicPorts()) {
                ports.add(port);
            }
            ports.add(publicPort);
            int[] portList = new int[ports.size()];
            int i = 0;
            for (Integer p : ports) {
                portList[i++] = p.intValue();
            }
            lb.setPublicPorts(portList);
            i = 0;
            boolean there = false;
            LbListener[] newList = new LbListener[listeners.length];
            for (LbListener l : listeners) {
                if (l.getAlgorithm().equals(listener.getAlgorithm())) {
                    if (l.getNetworkProtocol().equals(listener.getNetworkProtocol())) {
                        if (l.getPublicPort() == listener.getPublicPort()) {
                            if (l.getPrivatePort() == listener.getPrivatePort()) {
                                there = true;
                                break;
                            }
                        }
                    }
                }
                newList[i++] = l;
            }
            if (!there) {
                LbListener[] tmp = new LbListener[newList.length + 1];
                tmp[i++] = listener;
                lb.setListeners(tmp);
            }
            TreeSet<String> newIds = new TreeSet<String>();
            for (String id : currentIds) {
                newIds.add(id);
            }
            for (String id : serverIds) {
                newIds.add(id);
            }
            lb.setProviderServerIds(newIds.toArray(new String[newIds.size()]));
        } else {
            LoadBalancer lb = new LoadBalancer();
            lb.setCurrentState(LoadBalancerState.ACTIVE);
            lb.setAddress(publicIp);
            lb.setAddressType(LoadBalancerAddressType.IP);
            lb.setDescription(publicIp + ":" + publicPort + " -> RAW_TCP:" + privatePort);
            lb.setName(publicIp);
            lb.setProviderOwnerId(provider.getContext().getAccountNumber());
            lb.setCreationTimestamp(0L);
            lb.setPublicPorts(new int[] { publicPort });
            Collection<DataCenter> dcs = provider.getDataCenterServices().listDataCenters(provider.getContext().getRegionId());
            String[] ids = new String[dcs.size()];
            int i = 0;
            for (DataCenter dc : dcs) {
                ids[i++] = dc.getProviderDataCenterId();
            }
            lb.setProviderDataCenterIds(ids);
            lb.setProviderLoadBalancerId(publicIp);
            lb.setProviderRegionId(provider.getContext().getRegionId());
            lb.setProviderServerIds(serverIds.toArray(new String[serverIds.size()]));
            lb.setListeners(new LbListener[] { listener });
            current.put(publicIp, lb);
        }
    }

    public String getLoadBalancerForAddress(String address) throws InternalException, CloudException {
        HttpClient client = new HttpClient();
        GetMethod get = new GetMethod();
        Document doc;
        int code;
        try {
            get.setPath(provider.buildUrl(LIST_LOAD_BALANCER_RULES, new Param[] { new Param("publicIp", address) }));
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
            } else if (code == 530 || code == 570) {
                throw new CloudException("Server error in cloud (" + code + ")");
            }
            throw new CloudException("Received error code from server: " + code);
        }
        try {
            doc = provider.parseResponse(get.getResponseBodyAsStream());
        } catch (IOException e) {
            throw new CloudException("IOException getting stream: " + e.getMessage());
        }
        NodeList rules = doc.getElementsByTagName("loadbalancerrule");
        if (rules.getLength() > 0) {
            return address;
        }
        return null;
    }
}
