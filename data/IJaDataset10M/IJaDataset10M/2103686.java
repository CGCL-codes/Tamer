package hu.uszeged.inf.wlab.netspotter.plugin.connection;

import java.io.FileWriter;
import java.util.List;
import java.util.TreeMap;
import hu.uszeged.inf.wlab.netspotter.common.DiscoveryTask;
import hu.uszeged.inf.wlab.netspotter.plugin.connection.DiscoveryInterface.Status;
import hu.uszeged.inf.wlab.netspotter.topology.Node;
import nts.NTSQuery;
import hu.uszeged.inf.wlab.netspotter.common.device.*;
import hu.uszeged.inf.wlab.netspotter.common.port.*;
import java.util.*;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class NeighborDiscovery implements DiscoveryInterface {

    Status status;

    NTSQuery ntsq;

    public void discoveredLinks() {
    }

    public List<Node> discoveredNodes() {
        return null;
    }

    public AbstractResult getResult() {
        return null;
    }

    private Device searchByPhyAddress(String MAC, DiscoveryTask discoveryTask) {
        Device d = null;
        for (Device currentDevice : discoveryTask.getSelectedDevices()) {
            int actKey = currentDevice.getIpv6Interfaces().firstKey();
            boolean kov = true;
            while (kov) {
                if (MAC.equals(currentDevice.getIpv6Interfaces().get(actKey).getMac())) {
                    d = currentDevice;
                }
                if (actKey != currentDevice.getIpv6Interfaces().lastKey()) {
                    actKey = currentDevice.getIpv6Interfaces().higherKey(actKey);
                } else {
                    kov = false;
                }
            }
        }
        return d;
    }

    @SuppressWarnings("unchecked")
    public void start(DiscoveryTask discoveryTask) {
        status = Status.initializing;
        ntsq = new NTSQuery();
        TreeMap<String, String> resultMap;
        System.out.println("Eszk�z�k beolvas�sa:");
        for (Device currentDevice : discoveryTask.getSelectedDevices()) {
            System.out.println();
            System.out.println(currentDevice.getDeviceName());
            int intCount = 1;
            TreeMap<Integer, IpInterface> ipv6Interfaces = new TreeMap<Integer, IpInterface>();
            String s = ntsq.TelnetExecScript(currentDevice.getIpAddress(), "23", currentDevice.getTelnetUsername(), currentDevice.getTelnetPassword(), "show ipv6 interface brief");
            String[] s2 = s.split("\n");
            for (int i = 0; i < s2.length - 1; i++) {
                if (s2[i].endsWith("[up/up]") && s2[i + 1].contains(":")) {
                    String ifName = s2[i].split(" ")[0];
                    String include_mac = ntsq.TelnetExecScript(currentDevice.getIpAddress(), "23", currentDevice.getTelnetUsername(), currentDevice.getTelnetPassword(), "show interface " + ifName + " | include Hardware");
                    if (include_mac.contains("address is ")) {
                        String mac = include_mac.split("address is ")[1].split(" ")[0];
                        ipv6Interfaces.put(intCount, new IpInterface(ifName, intCount, mac));
                        ntsq.TelnetExecScript(currentDevice.getIpAddress(), "23", currentDevice.getTelnetUsername(), currentDevice.getTelnetPassword(), "ping ipv6 ff02::1 repeat 2 timeout 1\n" + ipv6Interfaces.get(intCount).getDescription());
                        ntsq.TelnetExecScript(currentDevice.getIpAddress(), "23", currentDevice.getTelnetUsername(), currentDevice.getTelnetPassword(), "ping ipv6 ff02::2 repeat 2 timeout 1\n" + ipv6Interfaces.get(intCount).getDescription());
                        intCount++;
                    }
                }
            }
            currentDevice.setIpv6Interfaces(ipv6Interfaces);
        }
        for (Device currentDevice : discoveryTask.getSelectedDevices()) {
            NDTable ndTable = new NDTable();
            String neighbors = ntsq.TelnetExecScript(currentDevice.getIpAddress(), "23", currentDevice.getTelnetUsername(), currentDevice.getTelnetPassword(), "show ipv6 neighbors");
            String routers = ntsq.TelnetExecScript(currentDevice.getIpAddress(), "23", currentDevice.getTelnetUsername(), currentDevice.getTelnetPassword(), "show ipv6 routers | include Router");
            String[] neighborsList = neighbors.split("\n");
            if (neighborsList[0].startsWith("IPv6 Address")) {
                for (int i = 1; i < neighborsList.length; i++) {
                    String[] s6 = neighborsList[i].split(" ");
                    String netAdd = s6[0];
                    String phyAdd = s6[s6.length - 4];
                    String state = s6[s6.length - 2];
                    String ifSortName = s6[s6.length - 1];
                    boolean isRouter = false;
                    String ifName;
                    String ifPhy = "FF:FF:FF:FF:FF:FF";
                    String routersList[] = routers.split("\n");
                    ifName = ntsq.TelnetExecScript(currentDevice.getIpAddress(), "23", currentDevice.getTelnetUsername(), currentDevice.getTelnetPassword(), "show run interface " + ifSortName + " | include interface").split(" ")[1];
                    for (int j = 1; j <= currentDevice.getIpv6Interfaces().size(); j++) {
                        if (ifName.equals(currentDevice.getIpv6Interfaces().get(j).getDescription())) {
                            ifPhy = currentDevice.getIpv6Interfaces().get(j).getMac();
                        }
                    }
                    for (int j = 0; j < routersList.length; j++) {
                        if (routersList[j].contains(netAdd)) {
                            isRouter = true;
                        }
                    }
                    ndTable.addSingleNDEntry(new NDEntry(ifName, ifPhy, phyAdd, netAdd, state, isRouter));
                }
                currentDevice.setNDTable(ndTable);
            }
        }
        try {
            System.out.println("az XML l�trhoz�sa");
            XMLWriter writer = new XMLWriter(new FileWriter("NDoutput.xml"), OutputFormat.createPrettyPrint());
            Document document = DocumentHelper.createDocument();
            Element graph = document.addElement("graph");
            XMLWriter gwriter = new XMLWriter(new FileWriter("NDgraph.xml"), OutputFormat.createPrettyPrint());
            Document gdocument = DocumentHelper.createDocument();
            Element graphXML = gdocument.addElement("graphXML");
            Element ggraph = graphXML.addElement("graph").addAttribute("isDirected", "false");
            int k = 0;
            TreeMap<String, String[]> unknown = new TreeMap<String, String[]>();
            for (Device currentDevice : discoveryTask.getSelectedDevices()) {
                Element node = graph.addElement("node");
                node.addAttribute("deviceid", currentDevice.getDeviceName());
                TreeMap<String, Element> insertedElement = new TreeMap<String, Element>();
                for (int i = 0; i < currentDevice.getNDTable().getTable().size(); i++) {
                    NDEntry ndE = currentDevice.getNDTable().getTable().get(i);
                    Element intface;
                    if (insertedElement.containsKey(ndE.getIfPhyAddress())) {
                        intface = insertedElement.get(ndE.getIfPhyAddress());
                    } else {
                        intface = node.addElement("interface").addAttribute("name", ndE.getIfName()).addAttribute("MACAddress", ndE.getIfPhyAddress());
                        insertedElement.put(ndE.getIfPhyAddress(), intface);
                    }
                    String remoteDeviceName;
                    Device dv;
                    if ((dv = this.searchByPhyAddress(ndE.getPhyAddress(), discoveryTask)) != null) {
                        remoteDeviceName = dv.getDeviceName();
                        String intName = "";
                        int actKey = dv.getIpv6Interfaces().firstKey();
                        boolean kov = true;
                        while (kov) {
                            if (ndE.getPhyAddress().equals(dv.getIpv6Interfaces().get(actKey).getMac())) {
                                intName = dv.getIpv6Interfaces().get(actKey).getDescription();
                            }
                            if (actKey != dv.getIpv6Interfaces().lastKey()) {
                                actKey = dv.getIpv6Interfaces().higherKey(actKey);
                            } else {
                                kov = false;
                            }
                        }
                        intface.addElement("NDNeighbor").addAttribute("deviceid", remoteDeviceName).addAttribute("interface", intName).addAttribute("MACAddress", ndE.getPhyAddress()).addAttribute("IPAddress", ndE.getNetAddress()).addAttribute("state", ndE.getState()).addAttribute("isRouter", ndE.getisRouterString());
                    } else if (unknown.containsKey(ndE.getPhyAddress())) {
                        remoteDeviceName = unknown.get(ndE.getPhyAddress())[0];
                        String remoteDeviceInt = unknown.get(ndE.getPhyAddress())[1];
                        intface.addElement("NDNeighbor").addAttribute("deviceid", remoteDeviceName).addAttribute("interface", remoteDeviceInt).addAttribute("MACAddress", ndE.getPhyAddress()).addAttribute("IPAddress", ndE.getNetAddress()).addAttribute("state", ndE.getState()).addAttribute("isRouter", ndE.getisRouterString());
                    } else {
                        remoteDeviceName = "unknown" + k;
                        intface.addElement("NDNeighbor").addAttribute("deviceid", remoteDeviceName).addAttribute("interface", "unknownINT").addAttribute("MACAddress", ndE.getPhyAddress()).addAttribute("IPAddress", ndE.getNetAddress()).addAttribute("state", ndE.getState()).addAttribute("isRouter", ndE.getisRouterString());
                        String s[] = { "unknown" + k, "unknownINT" };
                        unknown.put(ndE.getPhyAddress(), s);
                        k++;
                    }
                    Element edge = ggraph.addElement("edge").addAttribute("source", currentDevice.getDeviceName()).addAttribute("target", remoteDeviceName);
                    Element style = edge.addElement("style");
                    Element line = style.addElement("line");
                    String szin;
                    switch(ndE.getStateInt()) {
                        case 0:
                            {
                                szin = "blue";
                                break;
                            }
                        case 1:
                            {
                                szin = "yellow";
                                break;
                            }
                        case 2:
                            {
                                szin = "red";
                                break;
                            }
                        case 3:
                            {
                                szin = "green";
                                break;
                            }
                        case 4:
                            {
                                szin = "black";
                                break;
                            }
                        default:
                            {
                                szin = "gray";
                                break;
                            }
                    }
                    line.addAttribute("colour", szin);
                }
            }
            gwriter.write(gdocument);
            gwriter.close();
            writer.write(document);
            writer.close();
        } catch (Exception e) {
            System.out.println("HIBA az XML l�trehoz�s�ban: " + e.getMessage());
        }
    }

    public Status status() {
        return null;
    }

    public void stop() {
    }
}
