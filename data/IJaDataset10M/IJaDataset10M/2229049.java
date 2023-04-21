package net.emotivecloud.virtmonitor;

import java.util.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.StringReader;
import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;
import net.emotivecloud.commons.Network;

public class ParserNetwork {

    private static final long serialVersionUID = -2655260253745183203L;

    private String RootName = null;

    private String name = null;

    private String id = null;

    private String address = null;

    private String netmask = null;

    private String gateway = null;

    private String dev = null;

    private String mode = null;

    private String bridge = null;

    private String ip_start = null;

    private String ip_end = null;

    private String stp = null;

    private String delay = null;

    Network net = new Network();

    public String getRootName() {
        return RootName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDev() {
        return dev;
    }

    public void setDev(String dev) {
        this.dev = dev;
    }

    public String getBridge() {
        return bridge;
    }

    public void setBridge(String bridge) {
        this.bridge = bridge;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setRandomId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getIp_start() {
        return ip_start;
    }

    public void setIp_start(String ip_start) {
        this.ip_start = ip_start;
    }

    public String getIp_end() {
        return ip_end;
    }

    public void setIp_end(String ip_end) {
        this.ip_end = ip_end;
    }

    public String getStp() {
        return stp;
    }

    public void setStp(String stp) {
        this.stp = stp;
    }

    public String getDelay() {
        return delay;
    }

    public void setDelay(String delay) {
        this.delay = delay;
    }

    public ParserNetwork() {
    }

    public ParserNetwork(String XML) throws IOException, FileNotFoundException {
        SAXBuilder builder = new SAXBuilder();
        Document doc;
        try {
            Reader in = new StringReader(XML);
            doc = builder.build(in);
            Element root = doc.getRootElement();
            RootName = root.getName();
            setName(root.getChildText("name"));
            setId(root.getChildText("uuid"));
            Element bridge = root.getChild("bridge");
            setBridge(bridge.getAttributeValue("name"));
            setIp_start("pepito");
            Element ip = root.getChild("ip");
            setAddress(ip.getAttributeValue("address"));
            setNetmask(ip.getAttributeValue("netmask"));
            Element dhcp = ip.getChild("dhcp");
            Element range = dhcp.getChild("range");
            setIp_start(range.getAttributeValue("start"));
            setIp_end(range.getAttributeValue("end"));
            Element forward = root.getChild("forward");
            if (forward != null) {
                setMode(forward.getAttributeValue("mode"));
                setDev(forward.getAttributeValue("dev"));
            } else {
                setMode("");
                setDev("");
            }
        } catch (JDOMException e) {
            System.out.println(" File doesn't exist or file has bad XML structure ");
        } catch (NullPointerException e) {
            System.out.println(" File doesn't exist or file has bad XML structure ");
        }
    }

    public String CreateXML(String name, String uuid, String bridge, String address, String netmask, String dev, String mode, String start, String end) {
        String xml = "";
        try {
            Element root = new Element("network");
            Element nameX = new Element("name");
            nameX.setText(name);
            root.addContent(nameX);
            if (uuid != null) {
                Element uuidX = new Element("uuid");
                uuidX.setText(uuid);
                root.addContent(uuidX);
            }
            Element bridgeX = new Element("bridge");
            bridgeX.setAttribute("name", bridge);
            root.addContent(bridgeX);
            if ((mode.contains("route")) || (mode.contains("nat"))) {
                Element forwardX = new Element("forward");
                forwardX.setAttribute("mode", mode);
                if ((dev != null) && (mode.contains("route"))) {
                    forwardX.setAttribute("dev", dev);
                }
                root.addContent(forwardX);
            }
            Element ipX = new Element("ip");
            ipX.setAttribute("address", address);
            ipX.setAttribute("netmask", netmask);
            Element dhcpX = new Element("dhcp");
            Element rangeX = new Element("range");
            rangeX.setAttribute("start", start);
            rangeX.setAttribute("end", end);
            dhcpX.addContent(rangeX);
            ipX.addContent(dhcpX);
            root.addContent(ipX);
            Document document = new Document(root);
            XMLOutputter outputter = new XMLOutputter();
            xml = outputter.outputString(document);
            return xml;
        } catch (Exception e) {
            System.out.println("Error");
        }
        return xml;
    }

    public static void main(String args[]) throws IOException, FileNotFoundException {
    }
}
