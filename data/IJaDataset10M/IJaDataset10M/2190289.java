package com.htwg.routingengine.rpc;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.AbstractElement;
import com.htwg.routingengine.basis.Basis;
import com.htwg.routingengine.enginesmagic.ParameterSelector;
import com.htwg.routingengine.framework.RoutingEngine;

public class CallWrapper extends Basis {

    public CallWrapper(RoutingEngine re, String configFilename) {
        try {
            routingEngine = re;
            String filename = new String("./config/");
            filename = filename + configFilename;
            SAXReader reader = new SAXReader();
            serverConfigDoc = reader.read(new FileReader(filename));
            List<Node> nodelist = serverConfigDoc.selectNodes("/CallWrapper/RpcConfig");
            ParameterSelector ps = new ParameterSelector();
            String portFromXML = new String();
            portFromXML = ps.selectParameterValue(nodelist, "port");
            port = ((Integer) Integer.parseInt(portFromXML)).intValue();
            webServer = new WebServer(port);
            xmlRpcServer = webServer.getXmlRpcServer();
            phm = new PropertyHandlerMapping();
            xmlRpcServer.setHandlerMapping(phm);
            XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
            serverConfig.setEnabledForExtensions(false);
            serverConfig.setContentLengthOptional(false);
            Node node = serverConfigDoc.selectSingleNode("/CallWrapper/InterfaceConfig");
            registerExternalInterfaces(node.asXML());
            initalized = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void serverStart() throws Exception {
        if (initalized) {
            webServer.start();
        } else throw new Exception("Sorry, but RPC Server was not successfully initialized");
    }

    public void serverStop() throws Exception {
        webServer.shutdown();
    }

    private void registerExternalInterfaces(String xmlConfig) {
        try {
            Document xmldoc = DocumentHelper.parseText(xmlConfig);
            String xmlPath = new String("/InterfaceConfig/Param");
            List<Node> nodelist = xmldoc.selectNodes(xmlPath);
            AbstractElement currentElement = null;
            Iterator<Node> nodeIterator = nodelist.iterator();
            while (nodeIterator.hasNext()) {
                currentElement = (AbstractElement) nodeIterator.next();
                if (null != currentElement) {
                    try {
                        String externalInterfaceName = (String) currentElement.valueOf("@name");
                        String className = (String) currentElement.valueOf("@value");
                        Class c = Class.forName(className);
                        phm.addHandler(externalInterfaceName, c);
                    } catch (XmlRpcException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private PropertyHandlerMapping phm;

    private XmlRpcServer xmlRpcServer;

    private WebServer webServer;

    private int port = 8080;

    private boolean initalized;

    private RoutingEngine routingEngine;

    private Document serverConfigDoc;
}
