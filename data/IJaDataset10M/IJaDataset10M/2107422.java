package edu.sdsc.rtdsm.framework.src;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.util.*;
import java.io.*;
import edu.sdsc.rtdsm.framework.util.*;
import edu.sdsc.rtdsm.framework.sink.*;
import edu.sdsc.rtdsm.drivers.turbine.*;
import edu.sdsc.rtdsm.drivers.turbine.util.*;

public class SrcConfigParser {

    public String fileName = "srcConfig.xml";

    Hashtable<String, SrcConfig> hash = new Hashtable<String, SrcConfig>();

    Hashtable<String, SinkConfig> feedbackHash = null;

    Vector<String> srcList = new Vector<String>();

    public void parse() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document = null;
        try {
            Debugger.debug(Debugger.TRACE, "Node types:");
            Debugger.debug(Debugger.TRACE, "Node.ELEMENT_NODE=" + Node.ELEMENT_NODE);
            Debugger.debug(Debugger.TRACE, "Node.ATTRIBUTE_NODE=" + Node.ATTRIBUTE_NODE);
            Debugger.debug(Debugger.TRACE, "Node.ENTITY_NODE=" + Node.ENTITY_NODE);
            Debugger.debug(Debugger.TRACE, "Node.DOCUMENT_NODE =" + Node.DOCUMENT_NODE);
            Debugger.debug(Debugger.TRACE, "Node.CDATA_SECTION_NODE =" + Node.CDATA_SECTION_NODE);
            Debugger.debug(Debugger.TRACE, "Node.ENTITY_REFERENCE_NODE =" + Node.ENTITY_REFERENCE_NODE);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(new File(fileName));
            Node topNode = document.getDocumentElement();
            Debugger.debug(Debugger.TRACE, " Node type: " + topNode.getNodeType() + " Node name:" + topNode.getNodeName());
            NodeList topList = document.getDocumentElement().getChildNodes();
            for (int i = 0; i < topList.getLength(); i++) {
                Node node_i = topList.item(i);
                if (node_i.getNodeType() == Node.ELEMENT_NODE) {
                    String tagName = ((Element) node_i).getTagName();
                    if (node_i.getNodeType() == Node.ELEMENT_NODE && tagName.equals(Constants.SRCCONFIG_XML_SOURCE_TAG)) {
                        if (hash.containsKey(tagName)) {
                            throw new IllegalStateException("The xml document has repeated " + "source tag");
                        }
                        Element src = (Element) node_i;
                        SrcConfig srcConfig = null;
                        SinkConfig feedbackSinkConfig = null;
                        if (!src.hasAttribute(Constants.SRCCONFIG_XML_SRC_NAME_TAG)) {
                            throw new IllegalStateException("The xml document has a source tag " + "without a name attribute");
                        }
                        String srcName = src.getAttribute(Constants.SRCCONFIG_XML_SRC_NAME_TAG);
                        Debugger.debug(Debugger.TRACE, "srcName=" + srcName);
                        NodeList srcChildList = src.getChildNodes();
                        if (srcChildList.getLength() < 1) {
                            throw new IllegalStateException("The xml document has a source tag " + "without an orbParams tab");
                        }
                        for (int j = 0; j < srcChildList.getLength(); j++) {
                            Node node_j = srcChildList.item(j);
                            if (node_j.getNodeType() == Node.ELEMENT_NODE && ((Element) node_j).getTagName().equals(Constants.SRCCONFIG_XML_MAIN_CHANNEL_TAG)) {
                                Element mainChannelNode = (Element) node_j;
                                srcConfig = parseMainChannel(srcName, mainChannelNode);
                            } else if (node_j.getNodeType() == Node.ELEMENT_NODE && ((Element) node_j).getTagName().equals(Constants.SRCCONFIG_XML_FEEDBACK_CHANNEL_TAG)) {
                                Element feedbackChannelNode = (Element) node_j;
                                if (feedbackHash == null) {
                                    feedbackHash = new Hashtable<String, SinkConfig>();
                                }
                                feedbackSinkConfig = parseFeedbackChannel(srcName, feedbackChannelNode);
                            }
                        }
                        hash.put(srcName, srcConfig);
                        if (feedbackHash != null) {
                            feedbackHash.put(srcName, feedbackSinkConfig);
                        }
                        srcList.addElement(srcName);
                    }
                }
            }
        } catch (ClassCastException cce) {
            cce.printStackTrace();
        } catch (SAXException sxe) {
            Exception x = sxe;
            if (sxe.getException() != null) x = sxe.getException();
            x.printStackTrace();
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public SinkConfig parseFeedbackChannel(String srcName, Element feedbackChannelNode) {
        SinkConfig sinkConfig = null;
        return sinkConfig;
    }

    public SrcConfig parseMainChannel(String srcName, Element mainChannelNode) {
        SrcConfig srcConfig = null;
        NodeList mainChannelChildList = mainChannelNode.getChildNodes();
        int counter = 0;
        for (int j = 0; j < mainChannelChildList.getLength(); j++) {
            Node childNode = mainChannelChildList.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && ((Element) childNode).getTagName().equals(Constants.SRCCONFIG_XML_ORB_PARAMS_TAG)) {
                if (counter != 0) {
                    throw new IllegalStateException("Currently only one ORB is supported " + " Multiple ORBs will be supported in future");
                }
                counter++;
                Element orb = (Element) childNode;
                if (!orb.hasAttribute(Constants.SRCCONFIG_XML_ORB_TYPE_TAG)) {
                    throw new IllegalStateException("The xml document has a orb tag " + "without an orbType attribute");
                }
                String orbType = orb.getAttribute(Constants.SRCCONFIG_XML_ORB_TYPE_TAG);
                if (Constants.SRCCONFIG_XML_DATA_TURBINE_ORBTYPE_STR.equals(orbType)) {
                    srcConfig = new TurbineSrcConfig(srcName);
                } else {
                    throw new IllegalStateException("Currently only data turbine ORB " + "is supported. Multiple ORB types will be supported in " + "future");
                }
                srcConfig.parse(orb);
            }
        }
        return srcConfig;
    }

    public SrcConfig getSourceConfig(String srcName) {
        return hash.get(srcName);
    }

    public Vector<String> getSourceList() {
        return srcList;
    }
}
