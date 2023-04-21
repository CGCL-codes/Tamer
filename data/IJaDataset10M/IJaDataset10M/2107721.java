package org.dasein.cloud.flexiscale;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.log4j.Logger;
import org.dasein.cloud.AbstractCloud;
import org.dasein.cloud.CloudException;
import org.dasein.cloud.InternalException;
import org.dasein.cloud.ProviderContext;
import org.dasein.cloud.flexiscale.admin.FlexiscaleAdminServices;
import org.dasein.cloud.flexiscale.compute.FlexiscaleComputeServices;
import org.dasein.cloud.flexiscale.identity.FlexiscaleIdentityServices;
import org.dasein.cloud.flexiscale.network.FlexiscaleFirewall;
import org.dasein.cloud.flexiscale.network.FlexiscaleNetworkServices;
import org.dasein.cloud.network.Protocol;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.flexiant.extility.Customer;
import com.flexiant.extility.CustomerMetadata;
import com.flexiant.extility.FlexiScaleSoapBindingStub;

public class FlexiscaleProvider extends AbstractCloud {

    private static final Logger logger = Logger.getLogger(FlexiscaleProvider.class);

    public String META_DATA_TYPE = "privateMetaData";

    public String Public_MetaData = "publicMetaData";

    public String Private_MetaData = "privateMetaData";

    public String Customer_MetaData_Root = "Customer_MetaData";

    public String IpAddress_WithinGroup_First_Tag = "ipAddressWithinGroup";

    public String IpAddress_WithinGroup_Second_Tag = "item";

    public String IpAddress_Node_Name = "ipAddress";

    public String Flexiscale_Firewall_WithinGroup_First_Tag = "ipAddressWithinGroup";

    public String Flexiscale_Firewall_WithinGroup_Second_Tag = "item";

    public String Flexiscale_Firewall_Node_Name = "firewallId";

    public String ServerId_WithinGroup_Tag = "serverIdWithinGroup";

    public String ServerId_Node_Name = "item";

    public String FirewallRule_WithinIpAddress_Tag = "fireWallRulesWithinIpAddress";

    public String FirewallRule_Node_Name = "item";

    public String SecurityGroupId__Node_Name = "groupId";

    public String IpPermissions__Tag_Name = "ipPermissions";

    public FlexiscaleProvider() {
    }

    public boolean addAuthorizeInCustomerMetaData(Element securityGroupItem, @Nonnull String cidr, @Nonnull Protocol protocol, int startPort, int endPort) throws InternalException, CloudException {
        Document doc = securityGroupItem.getOwnerDocument();
        Element ipPermissions = getElement(doc, IpPermissions__Tag_Name, null);
        Element permissionsItem = doc.createElement("item");
        Element ipProtocol = doc.createElement("ipProtocol");
        ipProtocol.setTextContent(protocol.name());
        Element fromPort = doc.createElement("fromPort");
        fromPort.setTextContent(String.valueOf(startPort));
        Element toPort = doc.createElement("toPort");
        toPort.setTextContent(String.valueOf(endPort));
        Element groups = doc.createElement("groups");
        Element ipRanges = doc.createElement("ipRanges");
        Element ipRangesItem = doc.createElement("item");
        Element cidrIp = doc.createElement("cidrIp");
        cidrIp.setTextContent(cidr);
        ipRangesItem.appendChild(cidrIp);
        ipRanges.appendChild(ipRangesItem);
        permissionsItem.appendChild(ipProtocol);
        permissionsItem.appendChild(fromPort);
        permissionsItem.appendChild(toPort);
        permissionsItem.appendChild(groups);
        permissionsItem.appendChild(ipRanges);
        ipPermissions.appendChild(permissionsItem);
        securityGroupItem.appendChild(ipPermissions);
        return setCustomerMetadata(doc);
    }

    public boolean addFirewallRuleForIpAddresseInSecurityGroup(@Nonnull String securityGroupId, String ipAddress, String firewallRuleId) throws InternalException, CloudException {
        Document doc = this.getDocfromMetaData();
        Element securityGroupItem = (Element) this.getSecurityGroupItem(doc, securityGroupId);
        if (securityGroupItem != null) {
            NodeList ipAddressWithinGroupBlocks = securityGroupItem.getElementsByTagName(this.IpAddress_WithinGroup_First_Tag);
            if (ipAddressWithinGroupBlocks != null && ipAddressWithinGroupBlocks.getLength() > 0) {
                for (int j = 0; j < ipAddressWithinGroupBlocks.getLength(); j++) {
                    NodeList targets = ipAddressWithinGroupBlocks.item(j).getChildNodes();
                    for (int k = 0; k < targets.getLength(); k++) {
                        Node item = targets.item(k);
                        if (item.getNodeName().equals(this.IpAddress_WithinGroup_Second_Tag)) {
                            NodeList secondTargets = item.getChildNodes();
                            for (int l = 0; l < secondTargets.getLength(); l++) {
                                Node seconodItem = secondTargets.item(l);
                                if (seconodItem.getNodeName().equals(this.IpAddress_Node_Name) && seconodItem.getFirstChild().getNodeValue().equals(ipAddress)) {
                                    Element IpAddressWithinGroupElement = (Element) item;
                                    NodeList firewallRuleWithinGroupBlocks = IpAddressWithinGroupElement.getElementsByTagName(this.FirewallRule_WithinIpAddress_Tag);
                                    if (firewallRuleWithinGroupBlocks != null && firewallRuleWithinGroupBlocks.getLength() > 0) {
                                        Element firewallRuleItem = doc.createElement(this.FirewallRule_Node_Name);
                                        firewallRuleItem.setTextContent(firewallRuleId);
                                        firewallRuleWithinGroupBlocks.item(0).appendChild(firewallRuleItem);
                                    } else {
                                        Element fireWallRules = doc.createElement(this.FirewallRule_WithinIpAddress_Tag);
                                        Element fireWallRulesItem = doc.createElement(this.FirewallRule_Node_Name);
                                        fireWallRulesItem.setTextContent(firewallRuleId);
                                        fireWallRules.appendChild(fireWallRulesItem);
                                        seconodItem.getParentNode().appendChild(fireWallRules);
                                    }
                                    return this.setCustomerMetadata(doc);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            throw new CloudException("There is no such security group !!!");
        }
        return false;
    }

    public void addIpAddresseInSecurityGroup(@Nonnull String securityGroupId, String ipAddress) throws InternalException, CloudException {
        Document doc = this.getDocfromMetaData();
        Element securityGroupItem = (Element) this.getSecurityGroupItem(doc, securityGroupId);
        if (securityGroupItem != null) {
            NodeList ipAddressWithinGroupBlocks = securityGroupItem.getElementsByTagName(this.IpAddress_WithinGroup_First_Tag);
            if (ipAddressWithinGroupBlocks == null || ipAddressWithinGroupBlocks.getLength() == 0) {
                Element ipAddressWithinGroupElmtTop = doc.createElement(this.IpAddress_WithinGroup_First_Tag);
                Element ipAddressWithinGroupElmt = doc.createElement(this.IpAddress_WithinGroup_Second_Tag);
                Element ipAddressesItem = doc.createElement(this.IpAddress_Node_Name);
                ipAddressesItem.setTextContent(ipAddress);
                ipAddressWithinGroupElmt.appendChild(ipAddressesItem);
                Long flexiscaleFireWallId = createFlexiscaleFirewall(ipAddress);
                if (flexiscaleFireWallId != null) {
                    Element flexiscalefFrewallIdElmt = doc.createElement(this.Flexiscale_Firewall_Node_Name);
                    flexiscalefFrewallIdElmt.setTextContent(String.valueOf(flexiscaleFireWallId));
                    ipAddressWithinGroupElmt.appendChild(flexiscalefFrewallIdElmt);
                }
                ipAddressWithinGroupElmtTop.appendChild(ipAddressWithinGroupElmt);
                securityGroupItem.appendChild(ipAddressWithinGroupElmtTop);
                setCustomerMetadata(doc);
                authorizeFlexiscaleIpAddressForSecurityGroup(securityGroupId, ipAddress, flexiscaleFireWallId);
            } else {
                for (int j = 0; j < ipAddressWithinGroupBlocks.getLength(); j++) {
                    NodeList targets = ipAddressWithinGroupBlocks.item(j).getChildNodes();
                    for (int k = 0; k < targets.getLength(); k++) {
                        Node item = targets.item(k);
                        if (item.getNodeName().equals(this.IpAddress_WithinGroup_Second_Tag)) {
                            NodeList secondTargets = item.getChildNodes();
                            for (int l = 0; l < secondTargets.getLength(); l++) {
                                Node seconodItem = secondTargets.item(l);
                                if (seconodItem.getNodeName().equals(this.IpAddress_Node_Name) && seconodItem.getFirstChild().getNodeValue().equals(ipAddress)) {
                                    logger.debug("The ip address already in the security group !!!!");
                                    return;
                                }
                            }
                        }
                    }
                    Element ipAddressesItem = doc.createElement(this.IpAddress_WithinGroup_Second_Tag);
                    Element ipAddressElmt = doc.createElement(this.IpAddress_Node_Name);
                    ipAddressElmt.setTextContent(ipAddress);
                    ipAddressesItem.appendChild(ipAddressElmt);
                    Long flexiscaleFireWallId = createFlexiscaleFirewall(ipAddress);
                    if (flexiscaleFireWallId != null) {
                        Element old = this.getFlexiscaleFirewallIdElement(doc, securityGroupId, ipAddress);
                        if (old != null) {
                            Element flexiscalefFrewallIdElmt = doc.createElement(this.Flexiscale_Firewall_Node_Name);
                            flexiscalefFrewallIdElmt.setTextContent(String.valueOf(flexiscaleFireWallId));
                            ipAddressesItem.replaceChild(flexiscalefFrewallIdElmt, old);
                        } else {
                            Element flexiscalefFrewallIdElmt = doc.createElement(this.Flexiscale_Firewall_Node_Name);
                            flexiscalefFrewallIdElmt.setTextContent(String.valueOf(flexiscaleFireWallId));
                            ipAddressesItem.appendChild(flexiscalefFrewallIdElmt);
                        }
                    }
                    ipAddressWithinGroupBlocks.item(j).appendChild(ipAddressesItem);
                    setCustomerMetadata(doc);
                    authorizeFlexiscaleIpAddressForSecurityGroup(securityGroupId, ipAddress, flexiscaleFireWallId);
                }
            }
        } else {
            throw new CloudException("There is no such security group !!!");
        }
    }

    public boolean addServerIdInSecurityGroup(@Nonnull String securityGroupId, String serverId) throws InternalException, CloudException {
        Document doc = this.getDocfromMetaData();
        Element securityGroupItem = (Element) this.getSecurityGroupItem(doc, securityGroupId);
        if (securityGroupItem != null) {
            NodeList serverIdWithinGroupBlocks = securityGroupItem.getElementsByTagName(this.ServerId_WithinGroup_Tag);
            if (serverIdWithinGroupBlocks == null || serverIdWithinGroupBlocks.getLength() == 0) {
                Element serverIdWithinGrouppElmt = doc.createElement(this.ServerId_WithinGroup_Tag);
                Element serverIdWithinGroupItem = doc.createElement(this.ServerId_Node_Name);
                serverIdWithinGroupItem.setTextContent(serverId);
                serverIdWithinGrouppElmt.appendChild(serverIdWithinGroupItem);
                securityGroupItem.appendChild(serverIdWithinGrouppElmt);
                return this.setCustomerMetadata(doc);
            } else {
                for (int j = 0; j < serverIdWithinGroupBlocks.getLength(); j++) {
                    NodeList targets = serverIdWithinGroupBlocks.item(j).getChildNodes();
                    boolean isNewServerId = true;
                    for (int k = 0; k < targets.getLength(); k++) {
                        Node item = targets.item(k);
                        if (item.getNodeName().equals(this.ServerId_Node_Name) && item.getFirstChild().getNodeValue().equals(serverId)) {
                            logger.error("The server already in the security group !!!!");
                            isNewServerId = false;
                            break;
                        }
                    }
                    if (isNewServerId) {
                        Element serverIdItemToBeAdded = doc.createElement(this.ServerId_Node_Name);
                        serverIdItemToBeAdded.setTextContent(serverId);
                        serverIdWithinGroupBlocks.item(0).appendChild(serverIdItemToBeAdded);
                        return this.setCustomerMetadata(doc);
                    }
                }
            }
        } else {
            throw new CloudException("There is no such security group !!!");
        }
        return false;
    }

    public void authorizeFlexiscaleIpAddressForSecurityGroup(@Nonnull String securityGroupId, @Nonnull String ipAddress, Long flexiscaleRuleId) throws CloudException, InternalException {
        FlexiscaleFirewall firewall = new FlexiscaleFirewall(this);
        firewall.authorizeFlexiscaleIpAddressForSecurityGroup(securityGroupId, ipAddress, flexiscaleRuleId);
    }

    public Document buildSecurityGroupInfoNode(Document doc, String name, String description, String providerVlanId) throws InternalException, CloudException {
        Element root = doc.getDocumentElement();
        Element securityGroupInfo = null;
        Element securityGroupInfoUpdate = null;
        Element securityGroupInfoItem = doc.createElement("item");
        NodeList blocks = root.getElementsByTagName("groupName");
        if (blocks != null && blocks.getLength() > 0) {
            for (int i = 0; i < blocks.getLength(); i++) {
                Node node = blocks.item(i);
                if (node.getNodeType() == Node.TEXT_NODE) continue;
                if (node.getNodeType() == Node.COMMENT_NODE) continue;
                Element item = (Element) node;
                if (item.getTextContent().trim().equals(name)) {
                    throw new CloudException("The security group already exits!!!");
                }
            }
            securityGroupInfo = (Element) blocks.item(0).getParentNode().getParentNode();
            securityGroupInfoUpdate = securityGroupInfo;
        } else {
            securityGroupInfoUpdate = doc.createElement("securityGroupInfo");
        }
        Element ownerId = doc.createElement("ownerId");
        ownerId.setTextContent(this.getContext().getAccountNumber());
        Element groupId = doc.createElement("groupId");
        groupId.setTextContent(name);
        Element groupName = doc.createElement("groupName");
        groupName.setTextContent(name);
        Element groupDescription = doc.createElement("groupDescription");
        groupDescription.setTextContent(description);
        Element vpcId = doc.createElement("vpcId");
        if (providerVlanId != null) {
            vpcId.setTextContent(providerVlanId);
        }
        securityGroupInfoItem.appendChild(ownerId);
        securityGroupInfoItem.appendChild(groupId);
        securityGroupInfoItem.appendChild(groupName);
        securityGroupInfoItem.appendChild(groupDescription);
        securityGroupInfoItem.appendChild(vpcId);
        securityGroupInfoUpdate.appendChild(securityGroupInfoItem);
        if (securityGroupInfo != null) {
            root.replaceChild(securityGroupInfoUpdate, securityGroupInfo);
        } else {
            root.appendChild(securityGroupInfoUpdate);
        }
        return doc;
    }

    /**
	 * Flexiscale requires to set up public, private, restricted, system metadata,
	 * which would involve xml operations
	 */
    public Document createDomDocument() throws CloudException, InternalException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.newDocument();
            return doc;
        } catch (ParserConfigurationException e) {
            logger.error(e);
            throw new InternalException(e);
        }
    }

    public Element createElement(@Nonnull Document doc, @Nonnull String nodeName, String nodevalue) {
        Element element = doc.createElement(nodeName);
        if (nodevalue != null) {
            element.setNodeValue(nodevalue);
        }
        return element;
    }

    /**
	 *  Convert a xml document to string
	 */
    public String convertDomToString(Document doc) throws CloudException, InternalException {
        try {
            if (doc == null) return null;
            StringWriter stw = new StringWriter();
            Transformer serializer = TransformerFactory.newInstance().newTransformer();
            serializer.transform(new DOMSource(doc), new StreamResult(stw));
            if (stw != null) {
                return stw.toString();
            }
        } catch (TransformerConfigurationException e) {
            logger.error(e);
            throw new InternalException(e);
        } catch (TransformerFactoryConfigurationError e) {
            logger.error(e);
            throw new InternalException(e);
        } catch (TransformerException e) {
            logger.error(e);
            throw new InternalException(e);
        }
        return null;
    }

    public String getMetaData() throws CloudException, InternalException {
        String metaData = null;
        if (this.META_DATA_TYPE.equals(this.Public_MetaData)) {
            metaData = getCustomerMetadata().getPublic_metadata();
        } else if (this.META_DATA_TYPE.equals(this.Private_MetaData)) {
            metaData = getCustomerMetadata().getPrivate_metadata();
        }
        return metaData;
    }

    public Document getDocfromMetaData() throws InternalException, CloudException {
        String metaData = this.getMetaData();
        if (metaData != null || !metaData.equals("")) {
            return this.parseStringToDom(metaData);
        } else {
            throw new CloudException("The customer metadata is empty !!!!");
        }
    }

    public Element getElement(@Nonnull Element topElement, @Nonnull String nodeName, String nodevalue) {
        NodeList blocks = topElement.getElementsByTagName(nodeName);
        Element element = null;
        if (blocks == null || blocks.getLength() == 0) {
            element = createElement(topElement.getOwnerDocument(), nodeName, null);
        } else if (blocks.getLength() == 1) {
            element = (Element) blocks.item(0);
        }
        return element;
    }

    public Element getElement(@Nonnull Document doc, @Nonnull String nodeName, String nodevalue) {
        NodeList blocks = doc.getElementsByTagName(nodeName);
        Element element = null;
        if (blocks == null || blocks.getLength() == 0) {
            element = createElement(doc, IpPermissions__Tag_Name, null);
        } else if (blocks.getLength() == 1) {
            element = (Element) blocks.item(0);
        }
        return element;
    }

    public Element getFlexiscaleFirewallIdElement(@Nonnull Document doc, String securityGroupId, String ipAddress) throws InternalException, CloudException {
        Element securityGroupItem = (Element) getSecurityGroupItem(doc, securityGroupId);
        if (securityGroupItem != null) {
            NodeList ipAddressBlocks = securityGroupItem.getElementsByTagName(this.IpAddress_Node_Name);
            if (ipAddressBlocks != null || ipAddressBlocks.getLength() > 0) {
                for (int j = 0; j < ipAddressBlocks.getLength(); j++) {
                    Node item = ipAddressBlocks.item(j);
                    if (item.getFirstChild().getNodeValue().equals(ipAddress)) {
                        NodeList items = item.getParentNode().getChildNodes();
                        for (int k = 0; k < items.getLength(); k++) {
                            Node target = items.item(k);
                            if (target.getNodeName().equals(this.Flexiscale_Firewall_Node_Name)) {
                                return (Element) target;
                            }
                        }
                    }
                }
            } else {
                return null;
            }
        }
        return null;
    }

    public Node getSecurityGroupItem(Document doc, String securityGroupId) throws InternalException, CloudException {
        NodeList blocks;
        if (doc == null) return null;
        blocks = doc.getElementsByTagName("securityGroupInfo");
        for (int i = 0; i < blocks.getLength(); i++) {
            NodeList items = blocks.item(i).getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                Node item = items.item(j);
                if (item.getNodeName().equals("item")) {
                    NodeList attrs = item.getChildNodes();
                    for (int k = 0; k < attrs.getLength(); k++) {
                        Node attr = attrs.item(k);
                        if (attr.getNodeName().equals("groupId")) {
                            if (attr.getFirstChild().getNodeValue().trim().equals(securityGroupId)) return item;
                        }
                    }
                }
            }
        }
        return null;
    }

    public Long createFlexiscaleFirewall(@Nonnull String ipAddress) throws CloudException, InternalException {
        FlexiscaleFirewall firewall = new FlexiscaleFirewall(this);
        return firewall.createFileWallInFlexiscale(ipAddress);
    }

    public void deleteInCustomerMetaData(String securityGroupId) throws InternalException, CloudException {
        String metaData = getMetaData();
        Document doc = null;
        Element securityGroupItem = null;
        if (metaData == null || metaData.equals("")) {
            throw new CloudException("There is no such firewall Id in the " + META_DATA_TYPE + ". No need to delete!");
        } else {
            doc = parseStringToDom(metaData);
            Collection<String> ipAddresses = this.getFlexiscaleIpaddressForSecurityGroup(securityGroupId);
            if (ipAddresses != null) {
                logger.debug("Can not delete the firewall Id, because there are VM using this security group rules");
                throw new CloudException("Can not delete the firewall Id, because there are VM using this security group rules");
            }
            NodeList blocks = doc.getElementsByTagName("groupName");
            if (blocks != null) {
                for (int i = 0; i < blocks.getLength(); i++) {
                    Node node = blocks.item(i);
                    if (node.getNodeType() == Node.TEXT_NODE) continue;
                    if (node.getNodeType() == Node.COMMENT_NODE) continue;
                    securityGroupItem = (Element) node;
                    if (securityGroupItem.getTextContent().trim().equals(securityGroupId)) {
                        Node parent = blocks.item(i).getParentNode();
                        Node grandParent = parent.getParentNode();
                        grandParent.removeChild(parent);
                        this.setCustomerMetadata(doc);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public FlexiscaleAdminServices getAdminServices() {
        return new FlexiscaleAdminServices(this);
    }

    @Override
    public FlexiscaleComputeServices getComputeServices() {
        return new FlexiscaleComputeServices(this);
    }

    @Override
    public VirtualDataCenter getDataCenterServices() {
        try {
            return new VirtualDataCenter(this);
        } catch (CloudException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public FlexiscaleIdentityServices getIdentityServices() {
        return new FlexiscaleIdentityServices(this);
    }

    @Override
    public FlexiscaleNetworkServices getNetworkServices() {
        return new FlexiscaleNetworkServices(this);
    }

    @Override
    public String getProviderName() {
        String name = getContext().getProviderName();
        return ((name == null) ? "Flexiscale" : name);
    }

    public Collection<String> getFlexiscaleIpaddressForSecurityGroup(@Nonnull String securityGroupId) throws InternalException, CloudException {
        ArrayList<String> list = new ArrayList<String>();
        Document doc = this.getDocfromMetaData();
        Element securityGroupItem = (Element) this.getSecurityGroupItem(doc, securityGroupId);
        if (securityGroupItem != null) {
            NodeList ipAddressWithinGroupBlocks = securityGroupItem.getElementsByTagName(this.IpAddress_Node_Name);
            if (ipAddressWithinGroupBlocks == null || ipAddressWithinGroupBlocks.getLength() == 0) {
                return null;
            } else {
                for (int j = 0; j < ipAddressWithinGroupBlocks.getLength(); j++) {
                    Node item = ipAddressWithinGroupBlocks.item(j);
                    list.add(item.getFirstChild().getNodeValue());
                }
                return list;
            }
        }
        return null;
    }

    public String getFlexiscaleFirewallIdForSecurityGroup(Document doc, String securityGroupId, String ipAddress) throws InternalException, CloudException {
        if (doc == null) {
            doc = this.getDocfromMetaData();
        }
        Element firewallIdElmt = this.getFlexiscaleFirewallIdElement(doc, securityGroupId, ipAddress);
        if (firewallIdElmt != null) {
            return firewallIdElmt.getTextContent();
        } else {
            return null;
        }
    }

    public Collection<Element> getSecurityGroupNodeFromIpAddress(Document doc, String ipAddress) throws InternalException, CloudException {
        ArrayList<Element> list = new ArrayList<Element>();
        if (doc == null) {
            doc = this.getDocfromMetaData();
        }
        NodeList blocks;
        if (doc == null) return null;
        blocks = doc.getElementsByTagName(this.IpAddress_Node_Name);
        if (blocks != null) {
            for (int i = 0; i < blocks.getLength(); i++) {
                Node item = blocks.item(i);
                if (item.getFirstChild().getNodeValue().equals(ipAddress)) {
                    list.add((Element) item.getParentNode().getParentNode().getParentNode());
                }
            }
        }
        return list;
    }

    public Collection<Element> getSecurityGroupNodeFromServerId(Document doc, String serverId) throws InternalException, CloudException {
        return getSecurityGroupNodeItems(doc, serverId, this.ServerId_WithinGroup_Tag, this.ServerId_Node_Name, false);
    }

    public Collection<String> getSecurityGroupIdFromServerId(Document doc, String serverId) throws InternalException, CloudException {
        ArrayList<String> list = new ArrayList<String>();
        if (doc == null) {
            doc = this.getDocfromMetaData();
        }
        NodeList blocks;
        if (doc == null) return null;
        blocks = doc.getElementsByTagName(this.ServerId_WithinGroup_Tag);
        for (int i = 0; i < blocks.getLength(); i++) {
            NodeList items = blocks.item(i).getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                Node item = items.item(j);
                if (item.getNodeName().equals(this.ServerId_Node_Name) && item.getFirstChild().getNodeValue().equals(serverId)) {
                    Element securityGroupItem = (Element) item.getParentNode().getParentNode();
                    NodeList securityGroupitems = securityGroupItem.getChildNodes();
                    for (int k = 0; k < securityGroupitems.getLength(); k++) {
                        Node node = securityGroupitems.item(k);
                        if (node.getNodeName().equals(this.SecurityGroupId__Node_Name)) {
                            list.add(node.getFirstChild().getNodeValue());
                            break;
                        }
                    }
                }
            }
        }
        return list;
    }

    public Node getSecurityGroupItem(String securityGroupId) throws InternalException, CloudException {
        Document doc = getDocfromMetaData();
        return getSecurityGroupItemFromSecurityGroupId(doc, securityGroupId);
    }

    private Node getSecurityGroupItemFromSecurityGroupId(Document doc, String securityGroupId) throws InternalException, CloudException {
        NodeList blocks;
        if (doc == null) return null;
        blocks = doc.getElementsByTagName("securityGroupInfo");
        for (int i = 0; i < blocks.getLength(); i++) {
            NodeList items = blocks.item(i).getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                Node item = items.item(j);
                if (item.getNodeName().equals("item")) {
                    NodeList attrs = item.getChildNodes();
                    for (int k = 0; k < attrs.getLength(); k++) {
                        Node attr = attrs.item(k);
                        if (attr.getNodeName().equals("groupId")) {
                            if (attr.getFirstChild().getNodeValue().trim().equals(securityGroupId)) return item;
                        }
                    }
                }
            }
        }
        return null;
    }

    public Collection<Element> getSecurityGroupId(Document doc, String basedOnInfo, String tagName, String nodeName, boolean isSecond) throws InternalException, CloudException {
        ArrayList<Element> list = new ArrayList<Element>();
        if (doc == null) {
            doc = this.getDocfromMetaData();
        }
        NodeList blocks;
        if (doc == null) return null;
        blocks = doc.getElementsByTagName(tagName);
        for (int i = 0; i < blocks.getLength(); i++) {
            NodeList items = blocks.item(i).getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                Node item = items.item(j);
                if (item.getNodeName().equals(nodeName) && item.getFirstChild().getNodeValue().equals(basedOnInfo)) {
                    if (isSecond) {
                        list.add((Element) item.getParentNode().getParentNode().getParentNode());
                        break;
                    } else {
                        list.add((Element) item.getParentNode().getParentNode());
                        break;
                    }
                }
            }
        }
        return list;
    }

    public Collection<Element> getSecurityGroupNodeItems(Document doc, String basedOnInfo, String tagName, String nodeName, boolean isSecond) throws InternalException, CloudException {
        ArrayList<Element> list = new ArrayList<Element>();
        if (doc == null) {
            doc = this.getDocfromMetaData();
        }
        NodeList blocks;
        if (doc == null) return null;
        blocks = doc.getElementsByTagName(tagName);
        for (int i = 0; i < blocks.getLength(); i++) {
            NodeList items = blocks.item(i).getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                Node item = items.item(j);
                if (item.getNodeName().equals(nodeName) && item.getFirstChild().getNodeValue().equals(basedOnInfo)) {
                    if (isSecond) {
                        list.add((Element) item.getParentNode().getParentNode().getParentNode());
                        break;
                    } else {
                        list.add((Element) item.getParentNode().getParentNode());
                        break;
                    }
                }
            }
        }
        return list;
    }

    public Collection<String> getSecurityGroupIdsFromIpAddress(String ipAddress) throws InternalException, CloudException {
        ArrayList<String> list = new ArrayList<String>();
        Document doc = this.getDocfromMetaData();
        NodeList blocks;
        if (doc == null) return null;
        blocks = doc.getElementsByTagName("ipAddressWithinGroup");
        for (int i = 0; i < blocks.getLength(); i++) {
            NodeList items = blocks.item(i).getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                Node item = items.item(j);
                if (item.getNodeName().equals("item") && item.getNodeValue().equals(ipAddress)) {
                    Element securityGroupItem = (Element) item.getParentNode().getParentNode();
                    NodeList securityGroupitems = securityGroupItem.getChildNodes();
                    for (int k = 0; k < securityGroupitems.getLength(); k++) {
                        Node node = securityGroupitems.item(k);
                        if (node.getNodeName().equals("groupId")) {
                            list.add(node.getFirstChild().getNodeValue());
                        }
                    }
                }
            }
        }
        return null;
    }

    public String getSecurityGroupIdFromIpAddress(String ipAddress) throws InternalException, CloudException {
        Document doc = this.getDocfromMetaData();
        NodeList blocks;
        if (doc == null) return null;
        blocks = doc.getElementsByTagName("ipAddressWithinGroup");
        for (int i = 0; i < blocks.getLength(); i++) {
            NodeList items = blocks.item(i).getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                Node item = items.item(j);
                if (item.getNodeName().equals("item") && item.getNodeValue().equals(ipAddress)) {
                    Element securityGroupItem = (Element) item.getParentNode().getParentNode();
                    NodeList securityGroupitems = securityGroupItem.getChildNodes();
                    for (int k = 0; k < securityGroupitems.getLength(); k++) {
                        Node node = securityGroupitems.item(k);
                        if (node.getNodeName().equals("groupId")) {
                            return node.getNodeValue();
                        }
                    }
                }
            }
        }
        return null;
    }

    public Collection<String> getFirewallRulesFromIpAddress(String ipAddress) throws InternalException, CloudException {
        ArrayList<String> list = new ArrayList<String>();
        Document doc = this.getDocfromMetaData();
        NodeList blocks;
        if (doc == null) return null;
        blocks = doc.getElementsByTagName(this.IpAddress_WithinGroup_First_Tag);
        for (int i = 0; i < blocks.getLength(); i++) {
            NodeList items = blocks.item(i).getChildNodes();
            for (int j = 0; j < items.getLength(); j++) {
                Node item = items.item(j);
                if (item.getNodeName().equals(this.IpAddress_WithinGroup_Second_Tag)) {
                    NodeList secondTargets = item.getChildNodes();
                    boolean findIp = false;
                    for (int l = 0; l < secondTargets.getLength(); l++) {
                        Node seconodItem = secondTargets.item(l);
                        if (seconodItem.getNodeName().equals(this.IpAddress_Node_Name) && !item.getFirstChild().getNodeValue().equals(ipAddress)) {
                            break;
                        } else if (seconodItem.getNodeName().equals(this.IpAddress_Node_Name) && item.getFirstChild().getNodeValue().equals(ipAddress)) {
                            findIp = true;
                        }
                        if (findIp && seconodItem.getNodeName().equals(this.FirewallRule_WithinIpAddress_Tag)) {
                            NodeList ruleItems = seconodItem.getChildNodes();
                            for (int m = 0; m < secondTargets.getLength(); m++) {
                                Node thirdItem = ruleItems.item(m);
                                if (thirdItem.getNodeName().equals(this.FirewallRule_Node_Name)) {
                                    list.add(thirdItem.getFirstChild().getNodeValue());
                                    return list;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void removeFlexiscaleFirewall(String flexiscaleFirewallId) throws InternalException, CloudException {
        FlexiscaleFirewall firewall = new FlexiscaleFirewall(this);
        firewall.deleteInFlexiscale(flexiscaleFirewallId);
    }

    public boolean removeSingleIpAddressInSecurityGroup(String ipAddress) {
        Document doc = null;
        try {
            doc = this.getDocfromMetaData();
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (CloudException e) {
            e.printStackTrace();
        }
        ArrayList<Element> securityGroupItemList = null;
        try {
            securityGroupItemList = (ArrayList<Element>) getSecurityGroupNodeFromIpAddress(doc, ipAddress);
        } catch (InternalException e) {
            e.printStackTrace();
        } catch (CloudException e) {
            e.printStackTrace();
        }
        if (securityGroupItemList == null) return false;
        for (Element securityGroupItem : securityGroupItemList) {
            String securityGroupId = null;
            NodeList securityGroupIdBlocks = securityGroupItem.getElementsByTagName("groupId");
            if (securityGroupIdBlocks != null && securityGroupIdBlocks.getLength() == 1) {
                securityGroupId = securityGroupIdBlocks.item(0).getFirstChild().getNodeValue();
            }
            if (securityGroupId == null) {
                logger.debug("The ip is not in the security group, which it should be !!!!");
                return false;
            }
            NodeList ipAddressWithinGroupBlocks = securityGroupItem.getElementsByTagName(this.IpAddress_WithinGroup_Second_Tag);
            if (ipAddressWithinGroupBlocks != null) {
                for (int j = 0; j < ipAddressWithinGroupBlocks.getLength(); j++) {
                    NodeList targets = ipAddressWithinGroupBlocks.item(j).getChildNodes();
                    for (int k = 0; k < targets.getLength(); k++) {
                        Node item = targets.item(k);
                        if (item.getNodeName().equals(this.IpAddress_Node_Name) && item.getFirstChild().getNodeValue().equals(ipAddress)) {
                            logger.debug("The ip address is in the security group !!!!");
                            String flexiscaleFirewallId;
                            try {
                                flexiscaleFirewallId = this.getFlexiscaleFirewallIdForSecurityGroup(doc, securityGroupId, ipAddress);
                                this.removeFlexiscaleFirewall(flexiscaleFirewallId);
                            } catch (InternalException e) {
                                e.printStackTrace();
                            } catch (CloudException e) {
                                e.printStackTrace();
                            } finally {
                                ipAddressWithinGroupBlocks.item(j).getParentNode().removeChild(ipAddressWithinGroupBlocks.item(j));
                                return this.setCustomerMetadata(doc);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean removeSingleServerIdInSecurityGroup(String serverId) throws InternalException, CloudException {
        Document doc = this.getDocfromMetaData();
        ArrayList<Element> securityGroupItemList = (ArrayList<Element>) this.getSecurityGroupNodeFromServerId(doc, serverId);
        if (securityGroupItemList == null) return false;
        for (Element securityGroupItem : securityGroupItemList) {
            String securityGroupId = null;
            NodeList securityGroupIdBlocks = securityGroupItem.getElementsByTagName("groupId");
            if (securityGroupIdBlocks != null && securityGroupIdBlocks.getLength() == 1) {
                securityGroupId = securityGroupIdBlocks.item(0).getFirstChild().getNodeValue();
            }
            if (securityGroupId == null) {
                logger.debug("The ip is not in the security group, which it should be !!!!");
                return false;
            }
            NodeList serverIdWithinGroupBlocks = securityGroupItem.getElementsByTagName(this.ServerId_WithinGroup_Tag);
            if (serverIdWithinGroupBlocks != null) {
                for (int j = 0; j < serverIdWithinGroupBlocks.getLength(); j++) {
                    NodeList targets = serverIdWithinGroupBlocks.item(j).getChildNodes();
                    for (int k = 0; k < targets.getLength(); k++) {
                        Node item = targets.item(k);
                        if (item.getNodeName().equals(this.ServerId_Node_Name) && item.getFirstChild().getNodeValue().equals(serverId)) {
                            logger.debug("The Server Id is in the security group !!!!");
                            serverIdWithinGroupBlocks.item(j).removeChild(item);
                            break;
                        }
                    }
                }
            }
        }
        return this.setCustomerMetadata(doc);
    }

    private boolean setCustomerMetadata(String publicMetadata, String privateMetadata, String restrictedMetadata) throws InternalException, CloudException {
        try {
            FlexiscaleStub locator = new FlexiscaleStub(this);
            FlexiScaleSoapBindingStub service = locator.getFlexiscaleStub();
            CustomerMetadata customerMetadata = service.getCustomerMetadata();
            if (publicMetadata != null) {
                customerMetadata.setPublic_metadata(publicMetadata);
            }
            if (privateMetadata != null) {
                customerMetadata.setPrivate_metadata(privateMetadata);
            }
            if (restrictedMetadata != null) {
                customerMetadata.setRestricted_metadata(restrictedMetadata);
            }
            service.setCustomerMetadata(customerMetadata);
            return true;
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw new CloudException(e);
        }
    }

    public boolean setCustomerMetadata(Document doc) {
        String metaData;
        try {
            metaData = this.convertDomToString(doc);
            if (this.META_DATA_TYPE.equals(this.Public_MetaData)) {
                return setCustomerMetadata(metaData, null, null);
            } else if (this.META_DATA_TYPE.equals(this.Private_MetaData)) {
                return setCustomerMetadata(null, metaData, null);
            } else {
                return setCustomerMetadata(null, null, metaData);
            }
        } catch (CloudException e) {
            e.printStackTrace();
        } catch (InternalException e) {
            e.printStackTrace();
        }
        return false;
    }

    private CustomerMetadata getCustomerMetadata() throws InternalException, CloudException {
        CustomerMetadata customerMetadata = new CustomerMetadata();
        try {
            FlexiscaleStub locator = new FlexiscaleStub(this);
            FlexiScaleSoapBindingStub service = locator.getFlexiscaleStub();
            customerMetadata = service.getCustomerMetadata();
            return customerMetadata;
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            throw new InternalException(e);
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw new CloudException(e);
        }
    }

    /**
	 * Convert string to a xml document
	 */
    public Document parseStringToDom(String xmlString) throws CloudException, InternalException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            if (doc != null) return doc;
        } catch (ParserConfigurationException e) {
            throw new InternalException(e);
        } catch (SAXException e) {
            throw new InternalException(e);
        } catch (IOException e) {
            throw new InternalException(e);
        }
        return null;
    }

    /**  EndPoint*/
    private String getURL(ProviderContext ctx) {
        String endpoint = ctx.getEndpoint();
        return endpoint;
    }

    /**  EndPoint*/
    public String getURL() {
        return getURL(getContext());
    }

    /**
	 * Get the unique Flexiscale Customer UUID for each users
	 * @return
	 */
    public String getCustomerUUID() throws InternalException, CloudException {
        FlexiscaleStub locator = new FlexiscaleStub(this);
        FlexiScaleSoapBindingStub service = locator.getFlexiscaleStub();
        try {
            Customer customer = service.getCustomer();
            if (customer != null) {
                return customer.getCustomer_uuid();
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            throw new InternalException(e);
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw new CloudException(e);
        }
        return null;
    }

    /**
	 * Get the unique Flexiscale User UUID for each users
	 * @return
	 */
    public String getUserUUID() throws InternalException, CloudException {
        FlexiscaleStub locator = new FlexiscaleStub(this);
        FlexiScaleSoapBindingStub service = locator.getFlexiscaleStub();
        try {
            Customer customer = service.getCustomer();
            if (customer != null) {
                return customer.getUser_uuid();
            }
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            throw new InternalException(e);
        } catch (RemoteException e) {
            logger.error(e.getMessage());
            throw new CloudException(e);
        }
        return null;
    }

    @Override
    public String getCloudName() {
        String name = getContext().getCloudName();
        return ((name == null) ? "flexiscale" : name);
    }

    public String getRegionId() {
        String regionId = getContext().getRegionId();
        return regionId;
    }

    public String getDataCenterId() {
        String regionId = getRegionId();
        return getDataCenterIdFromRegionId(regionId);
    }

    public String getDataCenterIdFromRegionId(String regionId) {
        return (regionId + "-a");
    }

    public String getRegionIdFromDataCenterId(String datacenterId) {
        String regionId = datacenterId.split("-a")[0];
        return regionId;
    }

    public boolean isFlexiscale() {
        return (getContext().getEndpoint().contains("flexiscale"));
    }
}
