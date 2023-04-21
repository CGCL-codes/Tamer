package eu.soa4all.execution.mapping.resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import eu.soa4All.abstractServiceSchema.AbstractServiceDocument;
import eu.soa4All.abstractServiceSchema.AbstractServiceType;
import eu.soa4All.abstractServiceSchema.InputParameterType;
import eu.soa4All.abstractServiceSchema.OutputParameterType;
import eu.soa4All.abstractServiceSchema.RequestResponseOperationType;
import eu.soa4All.abstractServiceSchema.ServiceOperationType;
import eu.soa4All.abstractServiceSchema.ServiceType;
import eu.soa4all.execution.mapping.tools.xml.XmlUtils;

public class AbstractService {

    static Logger log = Logger.getLogger(AbstractService.class);

    private String name;

    private String sawsdlPath;

    private List<ServiceOperation> serviceOperations;

    private String location;

    private String targetNamespace;

    public AbstractService(String name, String sawsdlPath) {
        this.setName(name);
        this.setSawsdlPath(sawsdlPath);
        serviceOperations = new ArrayList<ServiceOperation>();
    }

    public boolean addOperation(ServiceOperation op) {
        return serviceOperations.add(op);
    }

    public boolean removeOperation(ServiceOperation op) {
        return serviceOperations.remove(op);
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param sawsdlPath the sawsdlPath to set
	 */
    public void setSawsdlPath(String sawsdlPath) {
        this.sawsdlPath = sawsdlPath;
    }

    /**
	 * @return the sawsdlPath
	 */
    public String getSawsdlPath() {
        return sawsdlPath;
    }

    /**
	 * @return the serviceOperations
	 */
    public List<ServiceOperation> getServiceOperations() {
        return serviceOperations;
    }

    /**
	 * completare per ora usata solo per test
	 * 
	 */
    public void toXml(String abstractServicePath) {
        Iterator<ServiceOperation> i;
        Iterator<ServiceParameter> j;
        ServiceOperation serviceOperation;
        List<ServiceParameter> serviceParameters;
        ServiceParameter serviceParameter;
        AbstractServiceDocument abstractServiceDocument;
        AbstractServiceType abstractServiceType;
        ServiceType serviceType;
        ServiceOperationType serviceOperationType;
        RequestResponseOperationType requestResponseOperationType;
        InputParameterType inputParameterType;
        OutputParameterType outputParameterType;
        abstractServiceDocument = AbstractServiceDocument.Factory.newInstance();
        abstractServiceType = abstractServiceDocument.addNewAbstractService();
        serviceType = abstractServiceType.addNewService();
        serviceType.setName(name);
        serviceType.setSawsdlPath(sawsdlPath);
        i = this.serviceOperations.iterator();
        while (i.hasNext()) {
            serviceOperation = i.next();
            serviceOperationType = serviceType.addNewServiceOperation();
            serviceOperationType.setName(serviceOperation.getName());
            serviceOperationType.setModelReference(serviceOperation.getModelReference());
            requestResponseOperationType = serviceOperationType.addNewRequestResponseOperation();
            requestResponseOperationType.setInputName(serviceOperation.getInputName());
            requestResponseOperationType.setInputModelReference(serviceOperation.getInputModelReference());
            requestResponseOperationType.setOutputName(serviceOperation.getOutputName());
            requestResponseOperationType.setOutputModelReference(serviceOperation.getOutputModelReference());
            serviceParameters = serviceOperation.getInputParameters();
            j = serviceParameters.iterator();
            while (j.hasNext()) {
                serviceParameter = j.next();
                inputParameterType = requestResponseOperationType.addNewInputParameter();
                if (serviceParameter.getIsLeaf() == null) inputParameterType.setIsLeaf("null"); else {
                    if (serviceParameter.getIsLeaf()) inputParameterType.setIsLeaf("true"); else inputParameterType.setIsLeaf("false");
                }
                if (serviceParameter.getNode() == null) inputParameterType.setNode("null"); else inputParameterType.setNode(serviceParameter.getNode());
                if (serviceParameter.getNameRoot() == null) inputParameterType.setNameRoot("null"); else inputParameterType.setNameRoot(serviceParameter.getNameRoot());
                if (serviceParameter.getModelReferenceRoot() == null) inputParameterType.setModelReferenceRoot("null"); else inputParameterType.setModelReferenceRoot(serviceParameter.getModelReferenceRoot());
                if (serviceParameter.getModelReference() == null) inputParameterType.setModelReference("null"); else inputParameterType.setModelReference(serviceParameter.getModelReference());
                if (serviceParameter.getLiftingSchemaMapping() == null) inputParameterType.setLiftingSchemaMapping("null"); else inputParameterType.setLiftingSchemaMapping(serviceParameter.getLiftingSchemaMapping());
                if (serviceParameter.getLoweringSchemaMapping() == null) inputParameterType.setLoweringSchemaMapping("null"); else inputParameterType.setLoweringSchemaMapping(serviceParameter.getLoweringSchemaMapping());
                if (serviceParameter.getName() == null) inputParameterType.setName("null"); else inputParameterType.setName(serviceParameter.getName());
                if (serviceParameter.getType() == null) inputParameterType.setType("null"); else inputParameterType.setType(serviceParameter.getType());
                if (serviceParameter.getMinOccurs() == null) inputParameterType.setMinOccurs("null"); else inputParameterType.setMinOccurs(serviceParameter.getMinOccurs());
                if (serviceParameter.getMaxOccurs() == null) inputParameterType.setMaxOccurs("null"); else inputParameterType.setMaxOccurs(serviceParameter.getMaxOccurs());
                if (serviceParameter.getIsOptional() == null) inputParameterType.setIsOptional("null"); else {
                    if (serviceParameter.getIsOptional()) inputParameterType.setIsOptional("true"); else inputParameterType.setIsOptional("false");
                }
            }
            serviceParameters = serviceOperation.getOutputParameters();
            j = serviceParameters.iterator();
            while (j.hasNext()) {
                serviceParameter = j.next();
                outputParameterType = requestResponseOperationType.addNewOutputParameter();
                if (serviceParameter.getIsLeaf() == null) outputParameterType.setIsLeaf("null"); else {
                    if (serviceParameter.getIsLeaf()) outputParameterType.setIsLeaf("true"); else outputParameterType.setIsLeaf("false");
                }
                if (serviceParameter.getNode() == null) outputParameterType.setNode("null"); else outputParameterType.setNode(serviceParameter.getNode());
                if (serviceParameter.getNameRoot() == null) outputParameterType.setNameRoot("null"); else outputParameterType.setNameRoot(serviceParameter.getNameRoot());
                if (serviceParameter.getModelReferenceRoot() == null) outputParameterType.setModelReferenceRoot("null"); else outputParameterType.setModelReferenceRoot(serviceParameter.getModelReferenceRoot());
                if (serviceParameter.getModelReference() == null) outputParameterType.setModelReference("null"); else outputParameterType.setModelReference(serviceParameter.getModelReference());
                if (serviceParameter.getLiftingSchemaMapping() == null) outputParameterType.setLiftingSchemaMapping("null"); else outputParameterType.setLiftingSchemaMapping(serviceParameter.getLiftingSchemaMapping());
                if (serviceParameter.getLoweringSchemaMapping() == null) outputParameterType.setLoweringSchemaMapping("null"); else outputParameterType.setLoweringSchemaMapping(serviceParameter.getLoweringSchemaMapping());
                if (serviceParameter.getName() == null) outputParameterType.setName("null"); else outputParameterType.setName(serviceParameter.getName());
                if (serviceParameter.getType() == null) outputParameterType.setType("null"); else outputParameterType.setType(serviceParameter.getType());
                if (serviceParameter.getMinOccurs() == null) outputParameterType.setMinOccurs("null"); else outputParameterType.setMinOccurs(serviceParameter.getMinOccurs());
                if (serviceParameter.getMaxOccurs() == null) outputParameterType.setMaxOccurs("null"); else outputParameterType.setMaxOccurs(serviceParameter.getMaxOccurs());
                if (serviceParameter.getIsOptional() == null) outputParameterType.setIsOptional("null"); else {
                    if (serviceParameter.getIsOptional()) outputParameterType.setIsOptional("true"); else outputParameterType.setIsOptional("false");
                }
            }
        }
        XmlUtils.writeXmlFile(abstractServicePath, (XmlObject) abstractServiceDocument);
    }

    /**
	 * TODO: completare per ora usata solo per test
	 * 
	 */
    public void toXMLTest() {
        List<ServiceOperation> serviceOperationsTest;
        serviceOperationsTest = this.getServiceOperations();
        Iterator<ServiceOperation> i = serviceOperationsTest.iterator();
        List<ServiceParameter> serviceParameters;
        Iterator<ServiceParameter> j;
        ServiceParameter serviceParameter;
        ServiceOperation opTest;
        log.debug("ABSTRACT SERVICE-Name: " + this.getName());
        log.debug("ABSTRACT SERVICE-SawsdlPath: " + this.getSawsdlPath());
        while (i.hasNext()) {
            opTest = i.next();
            log.debug("\n-------------------------");
            log.debug("SERVICE OPERATIONS-Name: " + opTest.getName());
            log.debug("SERVICE OPERATIONS-ModelReference: " + opTest.getModelReference());
            log.debug("\n-------------------------");
            serviceParameters = opTest.getInputParameters();
            j = serviceParameters.iterator();
            while (j.hasNext()) {
                serviceParameter = j.next();
                log.debug("SERVICE PARAMETERS-Name: " + serviceParameter.getName());
                log.debug("SERVICE PARAMETERS-ModelReference: " + serviceParameter.getModelReference());
                log.debug("SERVICE PARAMETERS-Type: " + serviceParameter.getType());
                log.debug("SERVICE PARAMETERS-Node: " + serviceParameter.getNode());
                log.debug("SERVICE PARAMETERS-LiftingSchemaMapping: " + serviceParameter.getLiftingSchemaMapping());
                log.debug("SERVICE PARAMETERS-LoweringSchemaMapping: " + serviceParameter.getLoweringSchemaMapping());
                log.debug("SERVICE PARAMETERS-MinOccurs: " + serviceParameter.getMinOccurs());
                log.debug("SERVICE PARAMETERS-MaxOccurs: " + serviceParameter.getMaxOccurs());
                log.debug("\n-------------------------");
            }
        }
    }

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        BasicConfigurator.configure();
        log.setLevel(Level.DEBUG);
        AbstractService abstractServiceTest = new AbstractService("ServizioTest", "ServizioTestSAWSDLPath");
        ServiceOperation op1;
        ServiceOperation op2;
        ServiceOperation op3;
        ServiceParameter param1;
        ServiceParameter param2;
        param1 = new ServiceParameter("param1", "param1", "param1");
        param2 = new ServiceParameter("param2", "param2", "param2");
        op1 = new ServiceOperation("op1", "concetto1");
        op2 = new ServiceOperation("op2", "concetto2");
        op3 = new ServiceOperation("op3", "concetto3");
        op1.addInputParameters(param1);
        op1.addInputParameters(param1);
        op1.addInputParameters(param2);
        op1.addInputParameters(param2);
        op1.addOutputParameters(param1);
        op1.addOutputParameters(param1);
        op2.addInputParameters(param1);
        op2.addInputParameters(param1);
        op2.addInputParameters(param2);
        op2.addInputParameters(param2);
        abstractServiceTest.addOperation(op1);
        abstractServiceTest.addOperation(op2);
        abstractServiceTest.addOperation(op3);
        log.debug("TO STRING: " + abstractServiceTest.toString());
        log.debug("SAWSDLPATH: " + abstractServiceTest.sawsdlPath);
        log.debug("NAME: " + abstractServiceTest.name);
        abstractServiceTest.toXMLTest();
        abstractServiceTest.toXml("testFiles\\output\\ServizioTest.xml");
    }

    /**
	 * @param serviceOperations the serviceOperations to set
	 */
    public void setServiceOperations(List<ServiceOperation> serviceOperations) {
        this.serviceOperations = serviceOperations;
    }

    /**
	 * @return the location
	 */
    public String getLocation() {
        return location;
    }

    /**
	 * @param location the location to set
	 */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
	 * @return the targetNamespace
	 */
    public String getTargetNamespace() {
        return targetNamespace;
    }

    /**
	 * @param targetNamespace the targetNamespace to set
	 */
    public void setTargetNamespace(String targetNamespace) {
        this.targetNamespace = targetNamespace;
    }
}
