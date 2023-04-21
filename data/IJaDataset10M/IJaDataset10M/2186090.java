package issrg.gt4Plus.pdp;

import issrg.gt4.util.AuthzException;
import issrg.gt4.util.XMLParser;
import org.globus.security.authorization.*;
import org.globus.gsi.jaas.GlobusPrincipal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.*;
import java.util.*;
import java.net.*;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.*;
import javax.xml.namespace.QName;
import javax.security.auth.Subject;
import com.sun.xacml.ConfigurationStore;
import com.sun.xacml.Indenter;
import com.sun.xacml.ParsingException;
import com.sun.xacml.UnknownIdentifierException;
import com.sun.xacml.attr.AttributeFactory;
import com.sun.xacml.attr.AttributeValue;
import com.sun.xacml.attr.StringAttribute;
import com.sun.xacml.attr.IntegerAttribute;
import com.sun.xacml.attr.DoubleAttribute;
import com.sun.xacml.cond.FunctionFactory;
import com.sun.xacml.cond.FunctionFactoryProxy;
import com.sun.xacml.cond.StandardFunctionFactory;
import com.sun.xacml.ctx.RequestCtx;
import com.sun.xacml.ctx.ResponseCtx;
import com.sun.xacml.ctx.Attribute;
import com.sun.xacml.finder.AttributeFinder;
import com.sun.xacml.finder.PolicyFinder;
import com.sun.xacml.finder.impl.CurrentEnvModule;
import com.sun.xacml.finder.impl.FilePolicyModule;
import com.sun.xacml.finder.impl.SelectorModule;
import com.sun.xacml.simple.*;
import issrg.gt4.util.BasicDom;
import issrg.utils.handler.EncodeXML;
import issrg.utils.handler.Merge;
import issrg.utils.handler.HandlerServiceException;

/**
 *
 * @author Linying Su
 */
public class XacmlPDP implements PDPInterceptor {

    private static Log logger = LogFactory.getLog(XacmlPDP.class.getName());

    private long serial;

    private String id;

    private Document doc;

    private Decision authzDecision;

    private com.sun.xacml.PDP xacmlPDP = null;

    private String[] files = null;

    private Element[] policies = null;

    private Element response = null;

    private Subject peerSubject = null;

    private String defaultSubject = null;

    private String defaultResource = null;

    private String defaultAction = null;

    /** Creates a new instance of XacmlPDP */
    public XacmlPDP() {
    }

    /**
     * this method is used to decide whether the requestor can access the resource.
     * @return a Decision object
     * @param requestEntities represents the request attributes.
     * @param nonReqEntities represents none request attributes.
     */
    public Decision canAccess(RequestEntities requestEntities, NonRequestEntities nonReqEntities) throws AuthorizationException {
        Date date = new Date();
        long start = date.getTime();
        if (logger.isDebugEnabled()) logger.debug("to make an authz decision");
        this.serial++;
        String trans = this.id + this.serial;
        logger.debug("tansaction id: " + trans);
        EntityAttributes actEntity = requestEntities.getAction();
        EntityAttributes subEntity = requestEntities.getRequestor();
        EntityAttributes envEntity = requestEntities.getEnvironment();
        EntityAttributes resEntity = requestEntities.getResource();
        Collection attrs = subEntity.getIdentityAttributes().getAttributes();
        for (Iterator i = attrs.iterator(); i.hasNext(); ) {
            org.globus.security.authorization.Attribute attr = (org.globus.security.authorization.Attribute) i.next();
            Set vals = attr.getAttributeValueSet();
            for (Iterator j = vals.iterator(); j.hasNext(); ) {
                Object val = j.next();
                if (val.getClass().getName().equals("org.globus.gsi.jaas.GlobusPrincipal")) {
                    GlobusPrincipal pricipal = (GlobusPrincipal) val;
                    defaultSubject = pricipal.getName();
                } else if (val.getClass().getName().equals("javax.security.auth.Subject")) {
                    this.peerSubject = (Subject) val;
                }
            }
        }
        attrs = resEntity.getIdentityAttributes().getAttributes();
        for (Iterator i = attrs.iterator(); i.hasNext(); ) {
            org.globus.security.authorization.Attribute attr = (org.globus.security.authorization.Attribute) i.next();
            Set vals = attr.getAttributeValueSet();
            for (Iterator j = vals.iterator(); j.hasNext(); ) {
                Object val = j.next();
                if (val.getClass().getName().equals("java.lang.String")) {
                    defaultResource = (String) val;
                }
            }
        }
        attrs = actEntity.getIdentityAttributes().getAttributes();
        for (Iterator i = attrs.iterator(); i.hasNext(); ) {
            org.globus.security.authorization.Attribute attr = (org.globus.security.authorization.Attribute) i.next();
            Set vals = attr.getAttributeValueSet();
            for (Iterator j = vals.iterator(); j.hasNext(); ) {
                Object val = j.next();
                if (val.getClass().getName().equals("javax.xml.namespace.QName")) {
                    QName name = (QName) val;
                    defaultAction = name.getLocalPart();
                }
            }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("default");
            logger.debug("subject : " + defaultSubject);
            logger.debug("resource : " + defaultResource);
            logger.debug("action : " + defaultAction);
        }
        Element requestCtx = null;
        IdentityAttributeCollection identityAttr = envEntity.getIdentityAttributes();
        for (Iterator i = identityAttr.getAttributes().iterator(); i.hasNext(); ) {
            org.globus.security.authorization.Attribute attr = (org.globus.security.authorization.Attribute) i.next();
            AttributeIdentifier iden = attr.getAttributeIdentifier();
            Set vals = attr.getAttributeValueSet();
            String id = iden.getAttributeId().toString();
            if (id.startsWith("issrg:cs:kent:ac:uk:coordination:request:context")) {
                for (Iterator j = vals.iterator(); j.hasNext(); ) {
                    Element v = (Element) j.next();
                    requestCtx = v;
                }
            }
        }
        if (requestCtx == null) {
            Element subject_attrs = null;
            Element resource_attrs = null;
            Element action_attrs = null;
            Element environment_attrs = null;
            identityAttr = subEntity.getIdentityAttributes();
            for (Iterator i = identityAttr.getAttributes().iterator(); i.hasNext(); ) {
                org.globus.security.authorization.Attribute attr = (org.globus.security.authorization.Attribute) i.next();
                AttributeIdentifier iden = attr.getAttributeIdentifier();
                Set vals = attr.getAttributeValueSet();
                String id = iden.getAttributeId().toString();
                if (id.startsWith("issrg:cs:kent:ac:uk:coordination:subject:attributes")) {
                    for (Iterator j = vals.iterator(); j.hasNext(); ) {
                        Element v = (Element) j.next();
                        subject_attrs = v;
                    }
                }
            }
            identityAttr = actEntity.getIdentityAttributes();
            for (Iterator i = identityAttr.getAttributes().iterator(); i.hasNext(); ) {
                org.globus.security.authorization.Attribute attr = (org.globus.security.authorization.Attribute) i.next();
                AttributeIdentifier iden = attr.getAttributeIdentifier();
                Set vals = attr.getAttributeValueSet();
                String id = iden.getAttributeId().toString();
                if (id.startsWith("issrg:cs:kent:ac:uk:coordination:action:attributes")) {
                    for (Iterator j = vals.iterator(); j.hasNext(); ) {
                        Element v = (Element) j.next();
                        action_attrs = v;
                    }
                }
            }
            identityAttr = envEntity.getIdentityAttributes();
            for (Iterator i = identityAttr.getAttributes().iterator(); i.hasNext(); ) {
                org.globus.security.authorization.Attribute attr = (org.globus.security.authorization.Attribute) i.next();
                AttributeIdentifier iden = attr.getAttributeIdentifier();
                Set vals = attr.getAttributeValueSet();
                String id = iden.getAttributeId().toString();
                if (id.startsWith("issrg:cs:kent:ac:uk:coordination:environment:attributes")) {
                    for (Iterator j = vals.iterator(); j.hasNext(); ) {
                        Element v = (Element) j.next();
                        environment_attrs = v;
                    }
                }
            }
            identityAttr = resEntity.getIdentityAttributes();
            for (Iterator i = identityAttr.getAttributes().iterator(); i.hasNext(); ) {
                org.globus.security.authorization.Attribute attr = (org.globus.security.authorization.Attribute) i.next();
                AttributeIdentifier iden = attr.getAttributeIdentifier();
                Set vals = attr.getAttributeValueSet();
                String id = iden.getAttributeId().toString();
                if (id.startsWith("issrg:cs:kent:ac:uk:coordination:resource:attributes")) {
                    for (Iterator j = vals.iterator(); j.hasNext(); ) {
                        Element v = (Element) j.next();
                        resource_attrs = v;
                    }
                }
            }
            Element request = null;
            Merge merge = new Merge();
            try {
                request = merge.merge(subject_attrs, resource_attrs);
                request = merge.merge(request, action_attrs);
                request = merge.merge(request, environment_attrs);
            } catch (HandlerServiceException ae) {
                throw new AuthorizationException("error when merging two set of attributes");
            }
            if (logger.isDebugEnabled()) {
                logger.debug("the request context is ");
                logger.debug(new EncodeXML().encode(request, 0));
            }
            requestCtx = request;
        }
        HashSet subjects = new HashSet();
        HashSet resources = new HashSet();
        HashSet actions = new HashSet();
        HashSet environment = new HashSet();
        HashSet attributes = new HashSet();
        Calendar now = Calendar.getInstance();
        Calendar later = Calendar.getInstance();
        later.add(Calendar.HOUR, 1);
        EntityAttributes reqEntity = requestEntities.getRequestor();
        EntityAttributes issuerEntity = requestEntities.getResource();
        Collection issuer_attrs = issuerEntity.getIdentityAttributes().getAttributes();
        for (Iterator i = issuer_attrs.iterator(); i.hasNext(); ) {
            org.globus.security.authorization.Attribute attr = (org.globus.security.authorization.Attribute) i.next();
            issuerEntity = attr.getIssuer();
        }
        try {
            URI ISSUER_URI = new URI("urn:globus:4.0:subject");
            URI DATE_DATATYPE_URI = new URI("urn:globus:4.0:datatype:java:subject");
            AttributeIdentifier issuerIden = new AttributeIdentifier(ISSUER_URI, DATE_DATATYPE_URI, true);
            org.globus.security.authorization.Attribute issuerAttribute = new org.globus.security.authorization.Attribute(issuerIden, issuerEntity, now, later);
            IdentityAttributeCollection attrCol = new IdentityAttributeCollection();
            issuerAttribute.addAttributeValue(trans);
            attrCol.add(issuerAttribute);
            issuerEntity = new EntityAttributes(attrCol);
        } catch (URISyntaxException ue) {
            throw new AttributeException("XACML PDP error:" + ue);
        }
        NodeList list = requestCtx.getElementsByTagName("Subject");
        subjects = this.getSubject(list);
        if (!subjects.isEmpty()) logger.debug("subject is extarcted");
        list = requestCtx.getElementsByTagName("Resource");
        resources = this.getResource(list);
        if (!resources.isEmpty()) logger.debug("resource is extarcted");
        list = requestCtx.getElementsByTagName("Action");
        actions = this.getAction(list);
        if (!actions.isEmpty()) logger.debug("action is extarcted");
        list = requestCtx.getElementsByTagName("Environment");
        environment = this.getEnvironment(list);
        if (!environment.isEmpty()) logger.debug("environment is extarcted");
        try {
            if (subjects.isEmpty()) {
                if (logger.isDebugEnabled()) logger.debug("to get default subject attributes");
                String subjectStr = this.defaultSubject;
                Attribute subjectAttribute = createAttribute("urn:oasis:names:tc:xacml:1.0:subject:subject-id", "http://www.w3.org/2001/XMLSchema#string", subjectStr);
                attributes.add(subjectAttribute);
                com.sun.xacml.ctx.Subject subject = new com.sun.xacml.ctx.Subject(attributes);
                subjects.add(subject);
            }
            if (resources.isEmpty()) {
                if (logger.isDebugEnabled()) logger.debug("to get default resource attributes");
                Attribute resourceAttribute = createAttribute("urn:oasis:names:tc:xacml:1.0:resource:resource-id", "http://www.w3.org/2001/XMLSchema#string", this.defaultResource);
                resources.add(resourceAttribute);
            }
            if (actions.isEmpty()) {
                if (logger.isDebugEnabled()) logger.debug("to get default action attributes");
                Attribute actionAttribute = createAttribute("urn:oasis:names:tc:xacml:1.0:action:action-id", "http://www.w3.org/2001/XMLSchema#string", this.defaultAction);
                actions.add(actionAttribute);
            }
            if (environment.isEmpty()) {
                if (logger.isDebugEnabled()) logger.debug("to get default environment attributes");
                environment.addAll(peerSubject.getPublicCredentials(Attribute.class));
            }
            RequestCtx xacml_request = new RequestCtx(subjects, resources, actions, environment);
            java.io.ByteArrayOutputStream byteStream = null;
            byteStream = new java.io.ByteArrayOutputStream();
            xacml_request.encode(byteStream, new com.sun.xacml.Indenter());
            XMLParser parser = new XMLParser(byteStream.toString());
            if (logger.isDebugEnabled()) logger.debug("the constructed request is");
            if (logger.isDebugEnabled()) logger.debug(new EncodeXML().encode(parser.getXmlElement(), 0));
            ResponseCtx res = null;
            res = this.xacmlPDP.evaluate(xacml_request);
            byteStream = new java.io.ByteArrayOutputStream();
            res.encode(byteStream, new com.sun.xacml.Indenter());
            parser = new XMLParser(byteStream.toString());
            String result = this.getDecision(res);
            this.response = parser.getXmlElement();
            if (logger.isInfoEnabled()) logger.debug(new EncodeXML().encode(this.response, 0));
            if (result.equals("Permit")) {
                int decision = Decision.PERMIT;
                this.authzDecision = new Decision(issuerEntity, reqEntity, decision, now, later);
                date = new Date();
                long end = date.getTime();
                end = end - start;
                if (logger.isInfoEnabled()) logger.info("this decision is made in " + end + " milliseconds");
                return this.authzDecision;
            } else if (result.equals("Deny")) {
                int decision = Decision.DENY;
                this.authzDecision = new Decision(issuerEntity, reqEntity, decision, now, later);
                date = new Date();
                long end = date.getTime();
                end = end - start;
                if (logger.isInfoEnabled()) logger.info("this decision is made in " + end + " milliseconds");
                return this.authzDecision;
            } else if (result.equals("Indeterminate")) {
                int decision = Decision.INDETERMINATE;
                this.authzDecision = new Decision(issuerEntity, reqEntity, decision, now, later);
                date = new Date();
                long end = date.getTime();
                end = end - start;
                if (logger.isInfoEnabled()) logger.info("this decision is made in " + end + " milliseconds");
                return this.authzDecision;
            } else if (result.equals("NotApplicable")) {
                int decision = Decision.NOT_APPLICABLE;
                this.authzDecision = new Decision(issuerEntity, reqEntity, decision, now, later);
                date = new Date();
                long end = date.getTime();
                end = end - start;
                if (logger.isInfoEnabled()) logger.info("this decision is made in " + end + " milliseconds");
                return this.authzDecision;
            } else throw new AuthorizationException("unknown decision result");
        } catch (Exception ae) {
            int decision = Decision.INDETERMINATE;
            this.authzDecision = new Decision(issuerEntity, reqEntity, decision, now, later);
            if (logger.isDebugEnabled()) logger.debug("failed to make an authz decision:" + ae);
            date = new Date();
            long end = date.getTime();
            end = end - start;
            if (logger.isInfoEnabled()) logger.info("this process takes " + end + " milliseconds");
            return this.authzDecision;
        }
    }

    public Decision canAdminister(RequestEntities requestEntities, NonRequestEntities nonReqEntities) throws AuthorizationException {
        return this.authzDecision;
    }

    /**
     * this method is used to initialise the Xacml PDP.
     * @param chainName denotes the service name, which is protected by the PDP.
     * @param prefix denotes the scope of the Xacml PDP.
     * @param config passes the properties which are configured in the security descriptor
     */
    public void initialize(String chainName, String prefix, ChainConfig config) throws InitializeException {
        if (logger.isDebugEnabled()) logger.debug("XACML PDP is initialised (scope: " + prefix + ") in the chain of " + chainName);
        Element authzConfig = (Element) config.getProperty(prefix, "parameterObject");
        if (authzConfig == null) {
            logger.debug("the configuration is null");
            return;
        } else if (logger.isDebugEnabled()) {
            logger.debug("the configuration of the XACML PDP is ");
            logger.debug(new EncodeXML().encode(authzConfig, 0));
        }
        this.serial = 0;
        String policy = null;
        this.id = null;
        NodeList list = authzConfig.getElementsByTagName("parameter");
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            Element node1 = (Element) node;
            String name = node1.getAttribute("name");
            String value = node1.getAttribute("value");
            if (name.equals("access-control-policy")) policy = value;
            if (name.equals("xacml-pdp-identity")) this.id = value;
        }
        if (policy == null) throw new InitializeException("no access control policy");
        if (logger.isDebugEnabled()) {
            if (policy != null) logger.debug("the access control policy is " + policy);
            if (this.id != null) logger.debug("PDP identity is " + this.id);
        }
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        try {
            this.doc = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException pe) {
            throw new InitializeException("failed to create DOM : " + pe);
        }
        StringTokenizer tokenizer = new StringTokenizer(policy);
        this.files = new String[tokenizer.countTokens()];
        int c = 0;
        while (tokenizer.hasMoreTokens()) {
            String op = tokenizer.nextToken();
            this.files[c] = op.trim();
            logger.debug(files[c]);
            c++;
        }
        try {
            SimplePDP pdp = new SimplePDP(files);
            this.xacmlPDP = pdp.getPDP();
            if (logger.isDebugEnabled()) logger.debug("The XACML PDP is built successfully");
        } catch (Exception e) {
            throw new InitializeException("failed to construct an XACML PDP : " + e);
        }
    }

    public void close() throws CloseException {
        if (logger.isDebugEnabled()) logger.debug("the XACML PDP is closed");
    }

    private HashSet getAttributes(NodeList list) throws AuthorizationException {
        HashSet attributes = new HashSet();
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (Text.class.isAssignableFrom(node.getClass())) continue;
            NodeList list0 = node.getChildNodes();
            for (int k = 0; k < list0.getLength(); k++) {
                Node node0 = list0.item(k);
                if (Text.class.isAssignableFrom(node0.getClass())) continue;
                if (node0.getNodeName().equals("Attribute")) {
                    Element ele = (Element) node0;
                    String id = ele.getAttribute("AttributeId");
                    logger.debug("get " + id);
                    String type = ele.getAttribute("DataType");
                    NodeList list1 = node0.getChildNodes();
                    for (int j = 0; j < list1.getLength(); j++) {
                        Node node1 = list1.item(j);
                        if (Text.class.isAssignableFrom(node1.getClass())) continue;
                        if (!node1.getNodeName().equals("AttributeValue")) continue;
                        NodeList list2 = node1.getChildNodes();
                        if (list2.getLength() != 1) continue;
                        Node node2 = list2.item(0);
                        if (!Text.class.isAssignableFrom(node2.getClass())) continue;
                        String value = node2.getNodeValue();
                        value = value.trim();
                        try {
                            logger.debug(id + "-" + value + "-" + type);
                            Attribute subjectAttribute = createAttribute(id, type, value);
                            attributes.add(subjectAttribute);
                        } catch (Exception e) {
                            logger.debug("failed to create XACML context:" + e);
                            throw new AuthorizationException("failed to create XACML context");
                        }
                    }
                }
            }
        }
        return attributes;
    }

    /**
     * this method is used to get a set of com.sun.xacml.ctx.Subject objects from a given list.
     * this list contains a set of XACML subject attributes.
     * @param list is the given list.
     * @return a set of com.sun.xacml.ctx.Subject objects.
     */
    public HashSet getSubject(NodeList list) throws AuthorizationException {
        HashSet subjects = new HashSet();
        HashSet attributes = this.getAttributes(list);
        if (!attributes.isEmpty()) {
            com.sun.xacml.ctx.Subject subject = new com.sun.xacml.ctx.Subject(attributes);
            subjects.add(subject);
        }
        return subjects;
    }

    /**
     * this method is used to get a set of Attribute objects from a given list.
     * this list contains a set of XACML resource attributes.
     * @param list is the given list.
     * @return a set of Attribute objects.
     */
    public HashSet getResource(NodeList list) throws AuthorizationException {
        HashSet attributes = this.getAttributes(list);
        return attributes;
    }

    /**
     * this method is used to get a set of Attribute objects from a given list.
     * this list contains a set of XACML action attributes.
     * @param list is the given list.
     * @return a set of Attribute objects.
     */
    public HashSet getAction(NodeList list) throws AuthorizationException {
        return this.getResource(list);
    }

    /**
     * this method is used to get a set of Attribute objects from a given list.
     * this list contains a set of XACML environment attributes.
     * @param list is the given list.
     * @return a set of Attribute objects.
     */
    public HashSet getEnvironment(NodeList list) throws AuthorizationException {
        return this.getResource(list);
    }

    /**
     * return the response of a decision making.
     */
    public Element getResponse() {
        return this.response;
    }

    /**
     * this method is used to get all of the coordination attributes in the policy.
     * @return an Element object, which contains all of the attributes in the format of
     * <Attributes>
     *   <Attribute AttributeId=<attribute name>, DataType=<xacml data type>, Type="Subject/Resource/Action/Environment">
     * </Attributes>
     */
    public Element getAttributes() throws InitializeException {
        logger.debug("to catch attributes in these policies");
        Element attrs = null;
        ArrayList attributes = new ArrayList();
        this.policies = new Element[this.files.length];
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        Document doc = null;
        try {
            doc = factory.newDocumentBuilder().newDocument();
        } catch (ParserConfigurationException pe) {
            throw new InitializeException("XML parser error:" + pe);
        }
        for (int j = 0; j < this.files.length; j++) {
            BasicDom dom = new BasicDom(this.files[j]);
            Document doc1 = dom.getXMLDocument();
            this.policies[j] = doc1.getDocumentElement();
            Node ele = this.policies[j];
            this.extract(ele, attributes);
        }
        ArrayList apps = this.getAssignmentApplys();
        for (Iterator i = apps.iterator(); i.hasNext(); ) {
            Element app = (Element) i.next();
            this.extract(app, attributes);
        }
        attrs = doc.createElement("Attributes");
        for (Iterator i = attributes.iterator(); i.hasNext(); ) {
            issrg.gt4.util.Attribute attribute = (issrg.gt4.util.Attribute) i.next();
            Element attr = doc.createElement("Attribute");
            attr.setAttribute("AttributeId", attribute.getName());
            attr.setAttribute("DataType", attribute.getDataType());
            if (attribute.getType() == issrg.gt4.util.Attribute.SUBJECT) attr.setAttribute("Type", "Subject"); else if (attribute.getType() == issrg.gt4.util.Attribute.RESOURCE) attr.setAttribute("Type", "Resource"); else if (attribute.getType() == issrg.gt4.util.Attribute.ACTION) attr.setAttribute("Type", "Action"); else if (attribute.getType() == issrg.gt4.util.Attribute.ENVIRONMENT) attr.setAttribute("Type", "Environment"); else throw new InitializeException("invalid attribute type");
            attrs.appendChild(attr);
        }
        return attrs;
    }

    /**
     * Given a Node object, which represent a policy, this method collect all of the attributes into a list. Each attribute in the list 
     * has the name in the format of x[...].
     * @param ele is the given node.
     * @param atttributes is the list.
     */
    private void extract(Node ele, ArrayList attributes) {
        if (ele == null) return;
        if (!Text.class.isAssignableFrom(ele.getClass())) {
            if (ele.getNodeName().equals("SubjectAttributeDesignator")) {
                String name = ((Element) ele).getAttribute("AttributeId");
                if (this.isCoordinationAttribute(name)) {
                    String dataType = ((Element) ele).getAttribute("DataType");
                    int type = issrg.gt4.util.Attribute.SUBJECT;
                    if (!this.exist(attributes, name, type)) {
                        issrg.gt4.util.Attribute attr = new issrg.gt4.util.Attribute(name, dataType, type);
                        attributes.add(attr);
                    }
                }
            } else if (ele.getNodeName().equals("ActionAttributeDesignator")) {
                String name = ((Element) ele).getAttribute("AttributeId");
                if (this.isCoordinationAttribute(name)) {
                    String dataType = ((Element) ele).getAttribute("DataType");
                    int type = issrg.gt4.util.Attribute.ACTION;
                    if (!this.exist(attributes, name, type)) {
                        issrg.gt4.util.Attribute attr = new issrg.gt4.util.Attribute(name, dataType, type);
                        attributes.add(attr);
                    }
                }
            } else if (ele.getNodeName().equals("ResourceAttributeDesignator")) {
                String name = ((Element) ele).getAttribute("AttributeId");
                if (this.isCoordinationAttribute(name)) {
                    String dataType = ((Element) ele).getAttribute("DataType");
                    int type = issrg.gt4.util.Attribute.RESOURCE;
                    if (!this.exist(attributes, name, type)) {
                        issrg.gt4.util.Attribute attr = new issrg.gt4.util.Attribute(name, dataType, type);
                        attributes.add(attr);
                    }
                }
            } else if (ele.getNodeName().equals("EnvironmentAttributeDesignator")) {
                String name = ((Element) ele).getAttribute("AttributeId");
                if (this.isCoordinationAttribute(name)) {
                    String dataType = ((Element) ele).getAttribute("DataType");
                    int type = issrg.gt4.util.Attribute.ENVIRONMENT;
                    if (!this.exist(attributes, name, type)) {
                        issrg.gt4.util.Attribute attr = new issrg.gt4.util.Attribute(name, dataType, type);
                        attributes.add(attr);
                    }
                }
            }
        }
        NodeList children = ele.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            this.extract(node, attributes);
        }
    }

    /**
     * this method is used to check whether or not an attribute exists in a given list of attributes.
     * @param list is the given list of attributes.
     * @param name is the attribute name.
     * @param type is the attribute type, i.e. Subject/Resource/Action/Environment
     * @return true, if it exists; otherwise returns false.
     */
    private boolean exist(ArrayList list, String name, int type) {
        for (Iterator i = list.iterator(); i.hasNext(); ) {
            issrg.gt4.util.Attribute attr = (issrg.gt4.util.Attribute) i.next();
            if (attr.getName().equals(name) && attr.getType() == type) return true;
        }
        return false;
    }

    /**
     * This method is used to return a list of elements, which are children of <AttributeAssignment>, from obligations of 
     * the current policies.
     */
    private ArrayList getAssignmentApplys() throws InitializeException {
        Element[] obls = this.getObligations();
        ArrayList applys = new ArrayList();
        for (int k = 0; k < obls.length; k++) {
            Element obligations = obls[k];
            if (obligations == null) continue;
            NodeList list = obligations.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                Node node = list.item(i);
                if (node.getNodeName().equals("Obligation")) {
                    NodeList assigns = node.getChildNodes();
                    for (int j = 0; j < assigns.getLength(); j++) {
                        Node assign = assigns.item(j);
                        if (assign.getNodeName().equals("AttributeAssignment")) {
                            NodeList texts = assign.getChildNodes();
                            if (texts.getLength() != 1) throw new InitializeException("invalid AttributeAssigment");
                            Node text = texts.item(0);
                            if (Text.class.isAssignableFrom(text.getClass())) {
                                Text t = (Text) text;
                                String str = t.getNodeValue();
                                XMLParser parser = new XMLParser(str);
                                try {
                                    Element e = parser.getXmlElement();
                                    applys.add(e);
                                } catch (AuthzException ae) {
                                    throw new InitializeException("error:" + ae);
                                }
                            }
                        }
                    }
                }
            }
        }
        return applys;
    }

    /**
     * this method is used to get an array of Elements. Each Element represents the obligations of a policy.
     */
    private Element[] getObligations() {
        Element[] obls = new Element[this.files.length];
        for (int i = 0; i < this.files.length; i++) {
            obls[i] = null;
            Element root = this.policies[i];
            NodeList children = root.getChildNodes();
            for (int j = 0; j < children.getLength(); j++) {
                Node node = children.item(j);
                if (node.getNodeName().equals("Obligations")) obls[i] = (Element) node;
            }
        }
        return obls;
    }

    /**
     * This method is used to create an Attribute object.
     * @param id is the attribute ID.
     * @param type is the attribute data type
     * @param value is the value of the attribute.
     */
    private Attribute createAttribute(String id, String type, Object value) throws URISyntaxException, UnknownIdentifierException, ParsingException {
        URI idURI = new URI(id);
        URI typeURI = new URI(type);
        AttributeValue attributeValue = AttributeFactory.createAttribute(typeURI, value.toString());
        return new Attribute(idURI, null, null, attributeValue);
    }

    /**
     * this method is used to get decision result from the XACML response context.
     * @param response is the XACML response context.
     * @return a string to indicate the result, such as "Permit", "Deny", "Indeterminate" and "NotApplicable"
     */
    private String getDecision(com.sun.xacml.ctx.ResponseCtx response) throws AuthorizationException {
        Set results = response.getResults();
        if (results.size() != 1) throw new AuthorizationException("invalid decision result");
        for (Iterator i = results.iterator(); i.hasNext(); ) {
            com.sun.xacml.ctx.Result result = (com.sun.xacml.ctx.Result) i.next();
            if (result.getDecision() == 0) return new String("Permit"); else if (result.getDecision() == 1) return new String("Deny"); else if (result.getDecision() == 2) return new String("Indeterminate"); else if (result.getDecision() == 3) return new String("NotApplicable"); else throw new AuthorizationException("unknown decision");
        }
        return null;
    }

    /**
     * Given an attribute name, this method checks whether or not the attribute is a coordination atribute
     * according to the syntax of coordination attribute naming.
     * @param name is the given attribute name.
     * @return true, if the name complies with the syntax; otherwise return false.
     */
    private boolean isCoordinationAttribute(String name) {
        int index1 = name.indexOf("[");
        int index2 = name.indexOf("]");
        if (index1 < 0 || index2 < 0) return false;
        if (index1 >= index2) return false;
        String tmp = name.substring(index1 + 1, index2);
        if (tmp != null) {
            index1 = tmp.indexOf("[");
            index2 = tmp.indexOf("]");
            if (index1 >= 0 || index2 >= 0) return false;
        }
        return true;
    }
}
