package com.tilab.wsig.wsdl;

import jade.content.ContentManager;
import jade.content.onto.BasicOntology;
import jade.content.onto.Ontology;
import jade.content.onto.OntologyException;
import jade.content.schema.AgentActionSchema;
import jade.content.schema.AggregateSchema;
import jade.content.schema.ConceptSchema;
import jade.content.schema.ObjectSchema;
import jade.content.schema.PrimitiveSchema;
import jade.core.Agent;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.wsdl.Binding;
import javax.wsdl.BindingInput;
import javax.wsdl.BindingOperation;
import javax.wsdl.BindingOutput;
import javax.wsdl.Definition;
import javax.wsdl.Input;
import javax.wsdl.Message;
import javax.wsdl.Operation;
import javax.wsdl.Output;
import javax.wsdl.Part;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensionRegistry;
import javax.wsdl.factory.WSDLFactory;
import org.apache.log4j.Logger;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDComponent;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDModelGroup;
import org.eclipse.xsd.XSDSchema;
import com.tilab.wsig.WSIGConfiguration;
import com.tilab.wsig.store.ActionBuilder;
import com.tilab.wsig.store.MapperBasedActionBuilder;
import com.tilab.wsig.store.OntologyBasedActionBuilder;
import com.tilab.wsig.store.OperationName;
import com.tilab.wsig.store.SuppressOperation;
import com.tilab.wsig.store.TypedAggregateSchema;
import com.tilab.wsig.store.WSIGService;

public class JadeToWSDL {

    private static Logger log = Logger.getLogger(JadeToWSDL.class.getName());

    public static Definition createWSDLFromSD(Agent agent, ServiceDescription sd, WSIGService wsigService) throws Exception {
        Object mapperObject = null;
        Class mapperClass = wsigService.getMapperClass();
        if (mapperClass != null) {
            try {
                mapperObject = mapperClass.newInstance();
            } catch (Exception e) {
                log.error("Mapper class " + mapperClass.getName() + " not found", e);
                throw e;
            }
        }
        String soapStyle = WSIGConfiguration.getInstance().getWsdlStyle();
        String soapUse;
        if (WSDLConstants.STYLE_RPC.equals(soapStyle)) {
            soapUse = WSDLConstants.USE_ENCODED;
        } else {
            soapUse = WSDLConstants.USE_LITERAL;
        }
        String serviceName = wsigService.getServicePrefix() + sd.getName();
        WSDLFactory factory = WSDLFactory.newInstance();
        String tns = WSDLConstants.URN + ":" + serviceName;
        Definition definition = WSDLUtils.createWSDLDefinition(factory, tns);
        XSDSchema wsdlTypeSchema = WSDLUtils.createSchema(tns);
        ExtensionRegistry registry = null;
        registry = factory.newPopulatedExtensionRegistry();
        definition.setExtensionRegistry(registry);
        PortType portType = WSDLUtils.createPortType(tns);
        definition.addPortType(portType);
        Binding binding = WSDLUtils.createBinding(tns);
        try {
            binding.addExtensibilityElement(WSDLUtils.createSOAPBinding(registry, soapStyle));
        } catch (WSDLException e) {
            throw new Exception("Error in SOAPBinding Handling", e);
        }
        definition.addBinding(binding);
        Port port = WSDLUtils.createPort(tns);
        try {
            port.addExtensibilityElement(WSDLUtils.createSOAPAddress(registry, serviceName));
        } catch (WSDLException e) {
            throw new Exception("Error in SOAPAddress Handling", e);
        }
        Service service = WSDLUtils.createService(tns);
        service.addPort(port);
        definition.addService(service);
        Iterator ontologies = sd.getAllOntologies();
        ContentManager cntManager = agent.getContentManager();
        if (ontologies.hasNext()) {
            String ontoName = (String) ontologies.next();
            log.debug("Elaborate ontology: " + ontoName);
            Ontology onto = cntManager.lookupOntology(ontoName);
            java.util.List actionNames = onto.getActionNames();
            for (int i = 0; i < actionNames.size(); i++) {
                try {
                    String actionName = (String) actionNames.get(i);
                    log.debug("Elaborate operation: " + actionName);
                    if (mapperClass != null && isActionSuppressed(mapperClass, actionName)) {
                        log.debug("--operation " + actionName + " suppressed");
                        continue;
                    }
                    int subOperationNumber = 1;
                    boolean operationDefinitedInMapper = false;
                    Vector<Method> mapperMethodsForAction = getMapperMethodsForAction(mapperClass, actionName);
                    if (mapperMethodsForAction.size() > 0) {
                        subOperationNumber = mapperMethodsForAction.size();
                        operationDefinitedInMapper = true;
                    }
                    for (int j = 0; j < subOperationNumber; j++) {
                        String operationName = actionName;
                        ActionBuilder actionBuilder = null;
                        if (operationDefinitedInMapper) {
                            Method method = mapperMethodsForAction.get(j);
                            OperationName annotationOperationName = method.getAnnotation(OperationName.class);
                            if (annotationOperationName != null) {
                                operationName = annotationOperationName.name();
                            } else {
                                if (subOperationNumber > 1) {
                                    Class[] parameterTypes = method.getParameterTypes();
                                    StringBuffer parameterStrings = new StringBuffer();
                                    for (int paramIndex = 0; paramIndex < parameterTypes.length; paramIndex++) {
                                        parameterStrings.append(parameterTypes[paramIndex].getSimpleName());
                                        if (paramIndex < (parameterTypes.length - 1)) {
                                            parameterStrings.append(WSDLConstants.SEPARATOR);
                                        }
                                    }
                                    operationName = operationName + WSDLConstants.SEPARATOR + parameterStrings.toString();
                                }
                            }
                            actionBuilder = new MapperBasedActionBuilder(mapperObject, method, onto, actionName);
                        } else {
                            actionBuilder = new OntologyBasedActionBuilder(onto, actionName);
                        }
                        wsigService.addActionBuilder(operationName, actionBuilder);
                        Operation operation = WSDLUtils.createOperation(operationName);
                        portType.addOperation(operation);
                        BindingOperation operationBinding = WSDLUtils.createBindingOperation(registry, tns, operationName);
                        binding.addBindingOperation(operationBinding);
                        Message inputMessage = WSDLUtils.createMessage(tns, WSDLUtils.getRequestName(operationName));
                        Input input = WSDLUtils.createInput(inputMessage);
                        operation.setInput(input);
                        definition.addMessage(inputMessage);
                        BindingInput inputBinding = WSDLUtils.createBindingInput(registry, tns, soapUse);
                        operationBinding.setBindingInput(inputBinding);
                        Map<String, ObjectSchema> inputParametersMap;
                        if (operationDefinitedInMapper) {
                            Method mapperMethod = mapperMethodsForAction.get(j);
                            inputParametersMap = manageInputParameters(tns, operationName, soapStyle, wsdlTypeSchema, inputMessage, actionName, onto, mapperMethod);
                        } else {
                            inputParametersMap = manageInputParameters(tns, operationName, soapStyle, wsdlTypeSchema, inputMessage, actionName, onto, null);
                        }
                        actionBuilder.setParametersMap(inputParametersMap);
                        Message outputMessage = WSDLUtils.createMessage(tns, WSDLUtils.getResponseName(operationName));
                        Output output = WSDLUtils.createOutput(outputMessage);
                        operation.setOutput(output);
                        definition.addMessage(outputMessage);
                        BindingOutput outputBinding = WSDLUtils.createBindingOutput(registry, tns, soapUse);
                        operationBinding.setBindingOutput(outputBinding);
                        manageOutputParameter(tns, operationName, soapStyle, wsdlTypeSchema, outputMessage, actionName, onto);
                    }
                } catch (Exception e) {
                    throw new Exception("Error in Agent Action Handling", e);
                }
            }
            try {
                definition.setTypes(WSDLUtils.createTypes(registry, wsdlTypeSchema));
            } catch (WSDLException e) {
                throw new Exception("Error adding type to definition", e);
            }
            wsigService.setWsdlDefinition(definition);
            if (WSIGConfiguration.getInstance().isWsdlWriteEnable()) {
                try {
                    log.info("Write WSDL for service: " + serviceName);
                    WSDLUtils.writeWSDL(factory, definition, serviceName);
                } catch (Exception e) {
                    log.error("Error writing WSDL file", e);
                }
            }
        }
        return definition;
    }

    private static Map<String, ObjectSchema> manageInputParameters(String tns, String operationName, String soapStyle, XSDSchema wsdlTypeSchema, Message inputMessage, String actionName, Ontology onto, Method mapperMethod) throws Exception {
        AgentActionSchema actionSchema = (AgentActionSchema) onto.getSchema(actionName);
        XSDModelGroup elementSequence = null;
        if (WSDLConstants.STYLE_DOCUMENT.equals(soapStyle)) {
            Part partMessage = WSDLUtils.createElementPart(WSDLConstants.PARAMETERS, operationName, tns);
            inputMessage.addPart(partMessage);
            XSDElementDeclaration element = WSDLUtils.addElementToSchema(tns, wsdlTypeSchema, operationName);
            XSDComplexTypeDefinition complexType = WSDLUtils.addComplexTypeToElement(element);
            elementSequence = WSDLUtils.addSequenceToComplexType(complexType);
        }
        Map<String, ObjectSchema> inputParametersMap;
        if (mapperMethod != null) {
            inputParametersMap = manageMapperInputParameters(tns, soapStyle, wsdlTypeSchema, elementSequence, inputMessage, actionSchema, onto, mapperMethod);
        } else {
            inputParametersMap = manageOntologyInputParameters(tns, soapStyle, wsdlTypeSchema, elementSequence, inputMessage, actionSchema);
        }
        return inputParametersMap;
    }

    private static Map<String, ObjectSchema> manageOntologyInputParameters(String tns, String soapStyle, XSDSchema wsdlTypeSchema, XSDModelGroup elementSequence, Message inputMessage, AgentActionSchema actionSchema) throws Exception {
        String[] slotNames = actionSchema.getNames();
        Map<String, ObjectSchema> inputParametersMap = new HashMap<String, ObjectSchema>();
        for (String slotName : slotNames) {
            ObjectSchema slotSchema = actionSchema.getSchema(slotName);
            String slotType = createComplexTypeFromSchema(tns, actionSchema, slotSchema, wsdlTypeSchema, slotName, null, null, null);
            log.debug("--ontology input slot: " + slotName + " (" + slotType + ")");
            if (slotSchema instanceof AggregateSchema) {
                slotSchema = WSDLUtils.getTypedAggregateSchema(actionSchema, slotName);
            }
            inputParametersMap.put(slotName, slotSchema);
            if (WSDLConstants.STYLE_RPC.equals(soapStyle)) {
                Part partMessage = WSDLUtils.createTypePart(slotName, slotType, tns);
                inputMessage.addPart(partMessage);
            } else {
                Integer cardMin = actionSchema.isMandatory(slotName) ? null : 0;
                WSDLUtils.addElementToSequence(tns, wsdlTypeSchema, slotName, slotType, elementSequence, cardMin, null);
            }
        }
        return inputParametersMap;
    }

    private static Map<String, ObjectSchema> manageMapperInputParameters(String tns, String soapStyle, XSDSchema wsdlTypeSchema, XSDModelGroup elementSequence, Message inputMessage, AgentActionSchema actionSchema, Ontology onto, Method mapperMethod) throws Exception {
        Class[] parameterTypes = mapperMethod.getParameterTypes();
        String[] parameterNames = WSDLUtils.getParameterNames(mapperMethod);
        Map<String, ObjectSchema> inputParametersMap = new HashMap<String, ObjectSchema>();
        for (int k = 0; k < parameterTypes.length; k++) {
            Class parameterClass = parameterTypes[k];
            String parameterName;
            if (parameterNames != null) {
                parameterName = parameterNames[k];
            } else {
                parameterName = parameterClass.getSimpleName() + WSDLConstants.SEPARATOR + k;
            }
            String parameterType = createComplexTypeFromClass(tns, onto, actionSchema, parameterClass, wsdlTypeSchema, parameterName, null);
            log.debug("--mapper input parameter: " + parameterName + " (" + parameterType + ")");
            ObjectSchema parameterSchema = getParameterSchema(onto, parameterClass);
            inputParametersMap.put(parameterName, parameterSchema);
            if (WSDLConstants.STYLE_RPC.equals(soapStyle)) {
                Part partMessage = WSDLUtils.createTypePart(parameterName, parameterType, tns);
                inputMessage.addPart(partMessage);
            } else {
                WSDLUtils.addElementToSequence(tns, wsdlTypeSchema, parameterName, parameterType, elementSequence);
            }
        }
        return inputParametersMap;
    }

    private static void manageOutputParameter(String tns, String operationName, String soapStyle, XSDSchema wsdlTypeSchema, Message outputMessage, String actionName, Ontology onto) throws Exception {
        AgentActionSchema actionSchema = (AgentActionSchema) onto.getSchema(actionName);
        ObjectSchema resultSchema = actionSchema.getResultSchema();
        XSDModelGroup elementSequence = null;
        if (WSDLConstants.STYLE_DOCUMENT.equals(soapStyle)) {
            String responseName = WSDLUtils.getResponseName(operationName);
            Part partMessage = WSDLUtils.createElementPart(WSDLConstants.PARAMETERS, responseName, tns);
            outputMessage.addPart(partMessage);
            String elementName = WSDLUtils.getResponseName(operationName);
            XSDElementDeclaration element = WSDLUtils.addElementToSchema(tns, wsdlTypeSchema, elementName);
            XSDComplexTypeDefinition complexType = WSDLUtils.addComplexTypeToElement(element);
            elementSequence = WSDLUtils.addSequenceToComplexType(complexType);
        }
        if (resultSchema != null) {
            String resultName = WSDLUtils.getResultName(operationName);
            String resultType = createComplexTypeFromSchema(tns, actionSchema, resultSchema, wsdlTypeSchema, resultSchema.getTypeName(), null, null, null);
            log.debug("--ontology output result: " + resultName + " (" + resultType + ")");
            Part partMessage;
            if (WSDLConstants.STYLE_RPC.equals(soapStyle)) {
                partMessage = WSDLUtils.createTypePart(resultName, resultType, tns);
                outputMessage.addPart(partMessage);
            } else {
                WSDLUtils.addElementToSequence(tns, wsdlTypeSchema, resultName, resultType, elementSequence);
            }
        }
    }

    public static ObjectSchema getParameterSchema(Ontology onto, Class parameterClass) throws OntologyException {
        ObjectSchema parameterSchema;
        if (parameterClass.isPrimitive() || WSDLConstants.java2xsd.get(parameterClass) != null) {
            String typeName;
            if (parameterClass.isPrimitive()) {
                typeName = parameterClass.getName();
            } else {
                typeName = (String) WSDLConstants.java2xsd.get(parameterClass);
            }
            parameterSchema = new PrimitiveSchema(typeName);
        } else if (parameterClass.isArray()) {
            ObjectSchema elementSchema = getParameterSchema(onto, parameterClass.getComponentType());
            parameterSchema = new TypedAggregateSchema(BasicOntology.SEQUENCE, elementSchema);
        } else if (Collection.class.isAssignableFrom(parameterClass) || jade.util.leap.Collection.class.isAssignableFrom(parameterClass)) {
            parameterSchema = null;
        } else {
            String conceptSchemaName = null;
            List conceptNames = onto.getConceptNames();
            for (int i = 0; i < conceptNames.size(); i++) {
                String conceptName = (String) conceptNames.get(i);
                if (parameterClass.equals(onto.getClassForElement(conceptName))) {
                    conceptSchemaName = conceptName;
                    break;
                }
            }
            parameterSchema = onto.getSchema(conceptSchemaName);
        }
        return parameterSchema;
    }

    private static Vector<Method> getMapperMethodsForAction(Class mapperClass, String actionName) {
        Vector<Method> methodsAction = new Vector<Method>();
        if (mapperClass != null) {
            Method[] methods = mapperClass.getDeclaredMethods();
            Method method = null;
            String methodNameToCheck = WSDLConstants.MAPPER_METHOD_PREFIX + actionName;
            for (int j = 0; j < methods.length; j++) {
                method = methods[j];
                if (method.getName().equalsIgnoreCase(methodNameToCheck)) {
                    methodsAction.add(method);
                }
            }
        }
        return methodsAction;
    }

    private static boolean isActionSuppressed(Class mapperClass, String actionName) {
        boolean isSuppressed = false;
        if (mapperClass != null) {
            Method[] methods = mapperClass.getDeclaredMethods();
            Method method = null;
            String methodNameToCheck = WSDLConstants.MAPPER_METHOD_PREFIX + actionName;
            for (int j = 0; j < methods.length; j++) {
                method = methods[j];
                SuppressOperation annotationSuppressOperation = method.getAnnotation(SuppressOperation.class);
                if ((method.getName().equalsIgnoreCase(methodNameToCheck) && annotationSuppressOperation != null)) {
                    isSuppressed = true;
                    break;
                }
            }
        }
        return isSuppressed;
    }

    private static String createComplexTypeFromClass(String tns, Ontology onto, ConceptSchema containerSchema, Class parameterClass, XSDSchema wsdlTypeSchema, String paramName, XSDComponent parentComponent) throws Exception {
        String slotType = null;
        if (parameterClass.isPrimitive() || WSDLConstants.java2xsd.get(parameterClass) != null) {
            if (parameterClass.isPrimitive()) {
                slotType = parameterClass.getName();
            } else {
                slotType = (String) WSDLConstants.java2xsd.get(parameterClass);
            }
            if (parentComponent != null) {
                log.debug("------add primitive-type " + paramName + " (" + slotType + ")");
                WSDLUtils.addElementToSequence(tns, wsdlTypeSchema, paramName, slotType, (XSDModelGroup) parentComponent, null, null);
            }
        } else if (parameterClass.isArray()) {
            Class aggrType = parameterClass.getComponentType();
            paramName = aggrType.getSimpleName().toLowerCase();
            slotType = WSDLUtils.getAggregateType(paramName, BasicOntology.SEQUENCE);
            if (WSDLUtils.getComplexType(wsdlTypeSchema, wsdlTypeSchema.getTargetNamespace(), slotType) == null) {
                log.debug("----create array-type " + slotType);
                XSDComplexTypeDefinition complexType = WSDLUtils.addComplexTypeToSchema(tns, wsdlTypeSchema, slotType);
                XSDModelGroup sequence = WSDLUtils.addSequenceToComplexType(complexType);
                createComplexTypeFromClass(tns, onto, containerSchema, aggrType, wsdlTypeSchema, paramName, sequence);
            }
        } else if (Collection.class.isAssignableFrom(parameterClass) || jade.util.leap.Collection.class.isAssignableFrom(parameterClass)) {
            throw new Exception("Collection NOT supported");
        } else {
            String[] conceptSchemaNames = containerSchema.getNames();
            String conceptSchemaName = null;
            for (String name : conceptSchemaNames) {
                if (parameterClass.equals(onto.getClassForElement(name))) {
                    conceptSchemaName = name;
                    break;
                }
            }
            if (conceptSchemaName == null) {
                throw new Exception("ConceptSchema of type " + parameterClass.getSimpleName() + " doesn't exist in " + onto.getName());
            }
            ObjectSchema conceptSchema = containerSchema.getSchema(conceptSchemaName);
            slotType = createComplexTypeFromSchema(tns, containerSchema, conceptSchema, wsdlTypeSchema, conceptSchemaName, parentComponent, null, null);
        }
        return slotType;
    }

    private static String createComplexTypeFromSchema(String tns, ConceptSchema containerSchema, ObjectSchema objSchema, XSDSchema wsdlTypeSchema, String slotName, XSDComponent parentComponent, Integer cardMin, Integer cardMax) throws Exception {
        String slotType = null;
        if (objSchema instanceof PrimitiveSchema) {
            slotType = WSDLConstants.jade2xsd.get(objSchema.getTypeName());
            if (parentComponent != null) {
                if (cardMin == null && !containerSchema.isMandatory(slotName)) {
                    cardMin = new Integer(0);
                }
                log.debug("------add primitive-type " + slotName + " (" + slotType + ") " + ((cardMin != null && cardMin == 0) ? "OPTIONAL" : ""));
                WSDLUtils.addElementToSequence(tns, wsdlTypeSchema, slotName, slotType, (XSDModelGroup) parentComponent, cardMin, cardMax);
            }
        } else if (objSchema instanceof ConceptSchema) {
            slotType = objSchema.getTypeName();
            if (parentComponent != null) {
                if (cardMin == null && !containerSchema.isMandatory(slotName)) {
                    cardMin = new Integer(0);
                }
                log.debug("------add complex-type " + slotName + " (" + slotType + ") " + ((cardMin != null && cardMin == 0) ? "OPTIONAL" : ""));
                WSDLUtils.addElementToSequence(tns, wsdlTypeSchema, slotName, slotType, (XSDModelGroup) parentComponent, cardMin, cardMax);
            }
            if (WSDLUtils.getComplexType(wsdlTypeSchema, wsdlTypeSchema.getTargetNamespace(), slotType) == null) {
                log.debug("----create complex-type " + slotType);
                XSDComplexTypeDefinition complexType = WSDLUtils.addComplexTypeToSchema(tns, wsdlTypeSchema, slotType);
                XSDModelGroup sequence = WSDLUtils.addSequenceToComplexType(complexType);
                for (String conceptSlotName : objSchema.getNames()) {
                    ObjectSchema slotSchema = objSchema.getSchema(conceptSlotName);
                    createComplexTypeFromSchema(tns, (ConceptSchema) objSchema, slotSchema, wsdlTypeSchema, conceptSlotName, sequence, null, null);
                }
            }
        } else if (objSchema instanceof AggregateSchema) {
            cardMax = WSDLUtils.getAggregateCardMax(containerSchema, slotName);
            cardMin = WSDLUtils.getAggregateCardMin(containerSchema, slotName);
            ObjectSchema aggregateSchema = WSDLUtils.getAggregateElementSchema(containerSchema, slotName);
            slotType = aggregateSchema.getTypeName();
            if (aggregateSchema instanceof PrimitiveSchema) {
                slotType = WSDLConstants.jade2xsd.get(slotType);
            }
            String itemName = slotType;
            String aggregateType = objSchema.getTypeName();
            slotType = WSDLUtils.getAggregateType(slotType, aggregateType);
            if (WSDLUtils.getComplexType(wsdlTypeSchema, wsdlTypeSchema.getTargetNamespace(), slotType) == null) {
                log.debug("----create array-type " + slotType);
                XSDComplexTypeDefinition complexType = WSDLUtils.addComplexTypeToSchema(tns, wsdlTypeSchema, slotType);
                XSDModelGroup sequence = WSDLUtils.addSequenceToComplexType(complexType);
                if (parentComponent != null) {
                    log.debug("------add array-type " + slotName + " (" + slotType + ") [" + cardMin + "," + cardMax + "]");
                    WSDLUtils.addElementToSequence(tns, wsdlTypeSchema, slotName, slotType, (XSDModelGroup) parentComponent);
                }
                createComplexTypeFromSchema(tns, containerSchema, aggregateSchema, wsdlTypeSchema, itemName, sequence, cardMin, cardMax);
            }
        }
        return slotType;
    }
}
