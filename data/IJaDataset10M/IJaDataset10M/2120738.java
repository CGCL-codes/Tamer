package bexee.model.xmltobpel;

import java.util.Hashtable;
import java.util.Map;
import javax.xml.namespace.QName;
import bexee.model.StandardAttributes;
import bexee.model.activity.Assign;
import bexee.model.activity.Invoke;
import bexee.model.activity.Receive;
import bexee.model.activity.Reply;
import bexee.model.activity.Sequence;
import bexee.model.activity.Switch;
import bexee.model.activity.impl.AssignImpl;
import bexee.model.activity.impl.InvokeImpl;
import bexee.model.activity.impl.ReceiveImpl;
import bexee.model.activity.impl.ReplyImpl;
import bexee.model.activity.impl.SequenceImpl;
import bexee.model.activity.impl.SwitchImpl;
import bexee.model.elements.BpelCase;
import bexee.model.elements.Correlation;
import bexee.model.elements.CorrelationPattern;
import bexee.model.elements.From;
import bexee.model.elements.PartnerLink;
import bexee.model.elements.To;
import bexee.model.elements.Variable;
import bexee.model.elements.Variables;
import bexee.model.elements.impl.BpelCaseImpl;
import bexee.model.elements.impl.CorrelationImpl;
import bexee.model.elements.impl.CorrelationPatternImpl;
import bexee.model.elements.impl.FromImpl;
import bexee.model.elements.impl.PartnerLinkImpl;
import bexee.model.elements.impl.RolesImpl;
import bexee.model.elements.impl.ToImpl;
import bexee.model.elements.impl.VariableImpl;
import bexee.model.elements.impl.VariablesImpl;
import bexee.model.expression.impl.BooleanExpressionImpl;
import bexee.model.process.BPELProcess;
import bexee.model.process.Process;
import bexee.model.process.impl.ProcessImpl;
import bexee.util.BooleanUtils;
import bexee.util.StringUtils;

/**
 * This is a factory for the creation of BPEL process elements. The
 * 
 * @author Pawel Kowalski
 * @version $Revision: 1.1 $, $Date: 2004/12/15 14:18:09 $
 */
public class BPELElementFactory {

    private static Map instances = new Hashtable();

    /**
     * Create a new factory instance or if one already exists for this
     * BPELProcessm, retrieve the existing one.
     * 
     * @param process
     * @return
     */
    public static BPELElementFactory getInstance(BPELProcess process) {
        if (instances.get(process) == null) {
            BPELElementFactory elementFactory = new BPELElementFactory();
            instances.put(process, elementFactory);
        }
        return (BPELElementFactory) instances.get(process);
    }

    private Map variables;

    private Map partnerLinks;

    private Map nameSpaces;

    /**
     * Create a BPELElementFactory object and initialize it.
     */
    public BPELElementFactory() {
        variables = new Hashtable();
        partnerLinks = new Hashtable();
        nameSpaces = new Hashtable();
    }

    /**
     * Create a Process instance with all possible attributes. Default values
     * will be set when an attribute is empty or null.
     * 
     * @param name
     * @param targetNamespace
     * @param queryLanguage
     * @param expressionLanguage
     * @param suppressJoinFailure
     * @param enableInstanceCompensation
     * @param abstractProcess
     * @param xmlns
     * @param nameSpaces
     * @return
     */
    public Process createProcess(String name, String targetNamespace, String queryLanguage, String expressionLanguage, String suppressJoinFailure, String enableInstanceCompensation, String abstractProcess, String xmlns, Map nameSpaces) {
        Process process = new ProcessImpl(name, targetNamespace, queryLanguage, expressionLanguage, suppressJoinFailure, enableInstanceCompensation);
        if (nameSpaces != null) {
            this.nameSpaces = nameSpaces;
        }
        return process;
    }

    /**
     * Create a PartnerLink with all possible attributes. Register the created
     * PartnerLink for retrieval by an Activity using PartnerLinks.
     * 
     * @param partnerLinkName
     * @param partnerLinkType
     * @param myRole
     * @param partnerRole
     * @return
     */
    public PartnerLink createPartnerLink(String partnerLinkName, String partnerLinkType, String myRole, String partnerRole) {
        PartnerLink partnerLink = new PartnerLinkImpl();
        partnerLink.setName(partnerLinkName);
        partnerLink.setPartnerLinkType(expandToQName(partnerLinkType));
        partnerLink.setMyRole(myRole);
        partnerLink.setPartnerRole(partnerRole);
        partnerLinks.put(partnerLinkName, partnerLink);
        return partnerLink;
    }

    /**
     * Describe <code>createVariables</code> method here.
     * 
     * @return
     */
    public Variables createVariables() {
        return new VariablesImpl();
    }

    /**
     * Create a Variable with all possible attributes. Register the created
     * Variable for retrieval by an invoke or receive element.
     * 
     * @param name
     * @param messageType
     * @param type
     * @param element
     * @return a <code>Variable</code> value
     */
    public Variable createVariable(String name, String messageType, String type, String element) {
        Variable variable = new VariableImpl();
        variable.setName(name);
        variable.setMessageType(expandToQName(messageType));
        variable.setType(expandToQName(type));
        variable.setElement(expandToQName(element));
        variables.put(name, variable);
        return variable;
    }

    /**
     * Create a Correlation with all parameters. If the pattern parameter is
     * null or empty, no CorrelationPattern object will be set.
     * 
     * @param set
     * @param initiate
     * @param pattern
     * @return
     */
    public Object createCorrelation(String set, String initiate, String pattern) {
        Correlation correlation = new CorrelationImpl();
        correlation.setSet(set);
        correlation.setInitiate(BooleanUtils.yesNoToBoolean(initiate));
        if (StringUtils.isNullOrEmpty(pattern)) {
            correlation.setPattern(createCorrelationPattern(pattern));
        }
        return correlation;
    }

    /**
     * Create a CorrelationPattern with a patternString.
     * 
     * @param patternString
     *            a <code>String</code> value
     * @return a CorrelationPattern object
     */
    public CorrelationPattern createCorrelationPattern(String patternString) {
        CorrelationPattern pattern = new CorrelationPatternImpl();
        pattern.setCorrelationString(patternString);
        return pattern;
    }

    /**
     * Describe <code>createAssign</code> method here.
     * 
     * @param standardAttributes
     * @return assign BPEL activity
     */
    public Assign createAssign(StandardAttributes standardAttributes) {
        return new AssignImpl(standardAttributes);
    }

    /**
     * Create a new Sequence BPEL activity.
     * 
     * @param standardAttributes
     *            a <code>StandardAttributes</code> value
     * @return Sequence activity
     */
    public Sequence createSequence(StandardAttributes standardAttributes) {
        return new SequenceImpl(standardAttributes);
    }

    /**
     * Create a Receive Activity with all possible attributes. A Receive created
     * this way will be linked with an already defined existing Variables.
     * 
     * @param standardAttributes
     *            a <code>StandardAttributes</code> value
     * @param partnerLinkName
     *            a <code>String</code> value
     * @param portTypeString
     *            a <code>String</code> value
     * @param operation
     * @param variableName
     *            a <code>String</code> value
     * @param createInstance
     * @return Receive activity
     */
    public Receive createReceive(StandardAttributes standardAttributes, String partnerLinkName, String portTypeString, String operation, String variableName, String createInstance) {
        PartnerLink partnerLink = retrievePartnerLink(partnerLinkName);
        QName portType = expandToQName(portTypeString);
        Variable variable = retrieveVariable(variableName);
        Receive receive = new ReceiveImpl(standardAttributes, partnerLink, portType, operation, variable, createInstance);
        return receive;
    }

    /**
     * Create an Invoke Activity with all possible attributes. An Invoke created
     * this way will be linked with already defined and existing Variables and
     * PartnerLinks.
     * 
     * @param standardAttributes
     *            a <code>StandardAttributes</code> value
     * @param partnerLinkName
     *            a <code>String</code> value
     * @param portTypeString
     *            a <code>String</code> value
     * @param operation
     * @param inVariableName
     *            a <code>String</code> value
     * @param outVariableName
     *            a <code>String</code> value
     * @return
     */
    public Object createInvoke(StandardAttributes standardAttributes, String partnerLinkName, String portTypeString, String operation, String inVariableName, String outVariableName) {
        PartnerLink partnerLink = retrievePartnerLink(partnerLinkName);
        QName portType = expandToQName(portTypeString);
        Variable inVariable = retrieveVariable(inVariableName);
        Variable outVariable = retrieveVariable(outVariableName);
        Invoke invoke = new InvokeImpl(standardAttributes, partnerLink, portType, operation, inVariable, outVariable);
        return invoke;
    }

    /**
     * Describe <code>createReply</code> method here.
     * 
     * @param attributes
     * @param partnerLink
     * @param portType
     * @param operation
     * @param variable
     * @param faultName
     * @return
     */
    public Reply createReply(StandardAttributes attributes, String partnerLink, String portType, String operation, String variable, String faultName) {
        Reply reply = new ReplyImpl(attributes);
        reply.setPartnerLink(retrievePartnerLink(partnerLink));
        reply.setPortType(expandToQName(portType));
        reply.setOperation(operation);
        reply.setVariable(retrieveVariable(variable));
        reply.setFaultName(expandToQName(faultName));
        return reply;
    }

    /**
     * Describe <code>createSwitch</code> method here.
     * 
     * @param standardAttributes
     * @return
     */
    public Object createSwitch(StandardAttributes standardAttributes) {
        Switch bpelSwitch = new SwitchImpl(standardAttributes);
        return bpelSwitch;
    }

    /**
     * Create a From with all possible attributes. Variables and PartnerLinks
     * will not be created but retrieved from the enclosing process instead.
     * 
     * @param variable
     * @param part
     * @param property
     * @param partnerLink
     * @param endpointReference
     * @param expression
     * @param opaque
     * @return
     */
    public From createFrom(String variable, String part, String property, String partnerLink, String endpointReference, String expression, String opaque) {
        From from = new FromImpl();
        from.setVariable(retrieveVariable(variable));
        from.setPart(part);
        from.setProperty(expandToQName(property));
        from.setPartnerLink(retrievePartnerLink(partnerLink));
        from.setEndpointReference(new RolesImpl(endpointReference));
        from.setExpression(expression);
        from.setOpaque(BooleanUtils.yesNoToBoolean(opaque));
        return from;
    }

    /**
     * Create a To with all possible attributes. Variables and PartnerLinks will
     * not be created but retrieved from the enclosing process instead.
     * 
     * @param variable
     * @param part
     * @param property
     * @param partnerLink
     * @return
     */
    public To createTo(String variable, String part, String property, String partnerLink) {
        To to = new ToImpl();
        to.setVariable(retrieveVariable(variable));
        to.setPart(part);
        to.setProperty(expandToQName(property));
        to.setPartnerLink(retrievePartnerLink(partnerLink));
        return to;
    }

    /**
     * Create a new BpelCase for the use within a Switch activity. Create this
     * BpelCase with a boolean condition used for selecting the BpelCase among
     * all cases within a Switch.
     * 
     * @param condition
     * @return
     */
    public BpelCase createBpelCase(String condition) {
        BpelCase bpelCase = new BpelCaseImpl();
        bpelCase.setBooleanExpression(new BooleanExpressionImpl(condition));
        return bpelCase;
    }

    /**
     * Retrieve an already created PartnerLink for an Activity using
     * partnerLinks. If the partnerLinkName is null, empty or a PartnerLink with
     * this name has not been created, this method will return a null value.
     * 
     * @param partnerLinkName
     *            the name of the PartnerLink to be retrieved.
     * @return a PartnerLink or null.
     */
    public PartnerLink retrievePartnerLink(String partnerLinkName) {
        if (StringUtils.isNullOrEmpty(partnerLinkName)) {
            return null;
        }
        return (PartnerLink) partnerLinks.get(partnerLinkName);
    }

    /**
     * Retrieve an already created Variable for an Activity using variables. If
     * the variableName parameter is null, empty or the Variable has not been
     * created, this method will return null value.
     * 
     * @param variableName
     *            the name of the Variable to be retrieved.
     * @return a Variable or null if the variableName is null, empty or a
     *         Variable with this name has not yet been created.
     */
    public Variable retrieveVariable(String variableName) {
        if (StringUtils.isNullOrEmpty(variableName)) {
            return null;
        }
        return (Variable) variables.get(variableName);
    }

    /**
     * @param qName
     * @return
     */
    private QName expandToQName(String qName) {
        String[] parts = StringUtils.split(qName);
        String namespaceUri;
        if (nameSpaces.get(parts[0]) == null) {
            namespaceUri = "";
        } else {
            namespaceUri = (String) nameSpaces.get(parts[0]);
        }
        return new QName(namespaceUri, parts[1]);
    }
}
