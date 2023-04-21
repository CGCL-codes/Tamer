package de.dgrid.bisgrid.secure.proy.workflow.factory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.security.auth.x500.X500Principal;
import javax.xml.namespace.QName;
import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlObject;
import org.w3.x2005.x08.addressing.EndpointReferenceType;
import x0PolicySchemaOs.oasisNamesTcXacml2.PolicyType;
import de.dgrid.bisgrid.common.BISGridProperties;
import de.dgrid.bisgrid.common.bpel.strategies.GetBpelEngineStrategy;
import de.dgrid.bisgrid.common.bpel.strategies.GetBpelEngineStrategyFactory;
import de.dgrid.bisgrid.services.workflow.IWorkflowService;
import de.dgrid.bisgrid.services.workflow.WorkflowServiceHome;
import de.dgrid.bisgrid.services.workflow.messages.workflowServiceFactory.CreateWorkflowReqDocument;
import de.dgrid.bisgrid.services.workflow.messages.workflowServiceFactory.CreateWorkflowRespDocument;
import de.dgrid.bisgrid.services.workflow.messages.workflowServiceFactory.SearchReqDocument;
import de.dgrid.bisgrid.services.workflow.messages.workflowServiceFactory.SearchRespDocument;
import de.dgrid.bisgrid.services.workflow.messages.workflowServiceFactory.SearchRespDocument.SearchResp;
import de.dgrid.bisgrid.services.workflow.messages.workflowServiceFactory.SearchRespDocument.SearchResp.EndpointReferenceSet;
import de.dgrid.bisgrid.services.workflow.properties.workflowFactory.WorkflowFactoryPropertiesDocument;
import de.dgrid.bisgrid.services.workflow.properties.workflowFactory.WorkflowServiceNameDocument;
import de.dgrid.bisgrid.services.workflow.properties.workflowService.CredentialDescriptionType;
import de.dgrid.bisgrid.services.workflow.properties.workflowService.ServiceDescriptionType;
import de.dgrid.bisgrid.services.workflow.properties.workflowService.WorkflowEnpointURLDocument;
import de.dgrid.bisgrid.services.workflow.properties.workflowService.WorkflowNameDocument;
import de.fzj.unicore.uas.util.AddressingUtil;
import de.fzj.unicore.wsrflite.Home;
import de.fzj.unicore.wsrflite.Kernel;
import de.fzj.unicore.wsrflite.utils.Utilities;
import de.fzj.unicore.wsrflite.xmlbeans.BaseFault;
import de.fzj.unicore.wsrflite.xmlbeans.impl.WSResourceImpl;
import de.fzj.unicore.wsrflite.xmlbeans.rp.ImmutableResourceProperty;
import eu.unicore.security.xfireutil.SecurityTokens;

/**
 * The Workflow Factory Service is used to create instances of a Workflow Service. This service itself is a WSRF Service and each
 * instance of this service is responsible to create service instances for one Workflow Service.
 * 
 * The Property of the WorkflowFactoryService is the name of the WorkflowService it creates instances for. Note, this is an
 * immutable property! 
 * 
 * @see WorkflowFactoryServiceHome 
 * @author Andre Hoeing - 06.03.2008
 *
 */
public class WorkflowFactoryService extends WSResourceImpl implements IWorkflowFactoryService {

    /**
	 * generated serial id
	 */
    private static final long serialVersionUID = -7506920127068722737L;

    private static final Logger log = Logger.getLogger(WorkflowFactoryService.class);

    private static GetBpelEngineStrategy getBpelEngineStrategy;

    public WorkflowFactoryService() {
        super();
        getBpelEngineStrategy = GetBpelEngineStrategyFactory.getStrategyInstance(BISGridProperties.getInstance().getAdapterForEngineType(BISGridProperties.BPEL_WORKFLOW_ENGINE_SELECTION_STRATEGY));
        log.debug("create new workflowfactoryservice instance");
    }

    /**
	 * called by the Service Home, when a factory instance (this) will be created
	 */
    public void initialise(String serviceName, Map<java.lang.String, java.lang.Object> initobjs) throws Exception {
        super.initialise(serviceName, initobjs);
        WorkflowNameDocument bwnd = WorkflowNameDocument.Factory.newInstance();
        bwnd.setWorkflowName((String) initobjs.get(WORKFLOW_NAME_PROPERTY));
        properties.put(RPworkflowName, new ImmutableResourceProperty(bwnd));
        WorkflowServiceNameDocument w_serviceName = WorkflowServiceNameDocument.Factory.newInstance();
        w_serviceName.setWorkflowServiceName((String) initobjs.get(WORKFLOW_SERVICE_NAME_PROPERTY));
        properties.put(RPworkflowServiceName, new ImmutableResourceProperty(w_serviceName));
        WorkflowEnpointURLDocument w_endpointURL = WorkflowEnpointURLDocument.Factory.newInstance();
        w_endpointURL.setWorkflowEnpointURL((String) initobjs.get(WORKFLOW_URL_PROPERTY));
        properties.put(RPWorkflowEnpointURL, new ImmutableResourceProperty(w_endpointURL));
        ServiceDescriptionType[] serviceCallDesctiptions = (ServiceDescriptionType[]) initobjs.get(SERVICE_DESCRIPTIONS_PROPERTY);
        if (serviceCallDesctiptions != null) {
            log.debug("Factory received " + serviceCallDesctiptions.length + " Service Descriptions");
            properties.put(RPServiceCallDescriptions, new ImmutableResourceProperty(serviceCallDesctiptions));
        }
        CredentialDescriptionType[] credentialDescriptions = (CredentialDescriptionType[]) initobjs.get(CREDENTIAL_DESCRIPTIONS_PROPERTY);
        if (credentialDescriptions != null) {
            log.debug("Factory received " + credentialDescriptions.length + " Credential Descriptions");
            properties.put(RPCredentialsDescriptions, new ImmutableResourceProperty(credentialDescriptions));
        }
        PolicyType policy = (PolicyType) initobjs.get(POLICY_PROPERTY);
        if (policy != null) {
            log.debug("security policy found in init objects");
            properties.put(RPAccessPolicy, new ImmutableResourceProperty(policy));
        }
    }

    /**
	 * creates a new workflow
	 */
    public CreateWorkflowRespDocument createWorkflow(CreateWorkflowReqDocument resp) throws BaseFault {
        CreateWorkflowRespDocument response = CreateWorkflowRespDocument.Factory.newInstance();
        response.addNewCreateWorkflowResp();
        try {
            Map<String, Object> initParams = new HashMap<String, Object>();
            if (getSecurityContext() != null) {
                SecurityTokens stokens = (SecurityTokens) getSecurityContext().get(SecurityTokens.KEY);
                if (stokens != null) {
                    X500Principal userName = stokens.getUserName();
                    initParams.put(IWorkflowService.OWNER, userName);
                } else {
                    log.warn("no security tokens available. at least a SECURITYINHandler to set the owner");
                }
            }
            String workflowServiceName;
            {
                ImmutableResourceProperty rp = (ImmutableResourceProperty) properties.get(RPworkflowServiceName);
                WorkflowServiceNameDocument doc = (WorkflowServiceNameDocument) rp.getXml()[0];
                workflowServiceName = doc.getWorkflowServiceName();
            }
            initParams.put(IWorkflowService.BPEL_ENGINE, getBpelEngineStrategy.getBpelWorkflowEngine());
            String bpelWorkflowName;
            {
                ImmutableResourceProperty rp = (ImmutableResourceProperty) properties.get(RPworkflowName);
                WorkflowNameDocument doc = (WorkflowNameDocument) rp.getXml()[0];
                bpelWorkflowName = doc.getWorkflowName();
            }
            initParams.put(IWorkflowService.WORKFLOW_NAME, bpelWorkflowName);
            String bpelEndpointUrl;
            {
                ImmutableResourceProperty rp = (ImmutableResourceProperty) properties.get(RPWorkflowEnpointURL);
                WorkflowEnpointURLDocument doc = (WorkflowEnpointURLDocument) rp.getXml()[0];
                bpelEndpointUrl = doc.getWorkflowEnpointURL();
            }
            initParams.put(IWorkflowService.WORKFLOW_ENDPOINT_URL, bpelEndpointUrl);
            ImmutableResourceProperty scdrp = (ImmutableResourceProperty) properties.get(RPServiceCallDescriptions);
            if (scdrp != null) {
                ServiceDescriptionType[] scds = (ServiceDescriptionType[]) scdrp.getXml();
                ServiceDescriptionType[] deepCopyServices = new XMLBeanHelper<ServiceDescriptionType>().deepCopyArray(scds);
                log.info("adding " + deepCopyServices.length + " service descriptions to init params");
                initParams.put(IWorkflowService.EXTERNAL_SERVICE_DESCRIPTIONS, deepCopyServices);
            }
            ImmutableResourceProperty cdd = (ImmutableResourceProperty) properties.get(RPCredentialsDescriptions);
            if (cdd != null) {
                CredentialDescriptionType[] cdds = (CredentialDescriptionType[]) cdd.getXml();
                CredentialDescriptionType[] deepCopyCredentials = new XMLBeanHelper<CredentialDescriptionType>().deepCopyArray(cdds);
                log.info("adding " + deepCopyCredentials.length + " credential descriptions to init params");
                initParams.put(IWorkflowService.CREDENTIAL_DESCRIPTIONS, deepCopyCredentials);
            }
            ImmutableResourceProperty accessPolicyRP = (ImmutableResourceProperty) properties.get(RPAccessPolicy);
            if (accessPolicyRP != null) {
                PolicyType policy = (PolicyType) accessPolicyRP.getXml()[0];
                log.info("adding access poliy to workflow instance init params");
                initParams.put(IWorkflowService.ACCESS_POLICY, policy);
            }
            Kernel k = Kernel.getKernel();
            Home h = k.getServiceHome(workflowServiceName);
            String id = h.createWSRFServiceInstance(initParams);
            EndpointReferenceType epr = AddressingUtil.newEPR();
            epr.addNewAddress().setStringValue(Utilities.makeAddress(workflowServiceName, id));
            response.getCreateWorkflowResp().setEndpointReference(epr);
            return response;
        } catch (Exception e) {
            log.error("cannot create new worklfow instance", e);
            e.printStackTrace();
            throw new BaseFault(e.getMessage());
        }
    }

    @Override
    public QName getResourcePropertyDocumentQName() {
        return WorkflowFactoryPropertiesDocument.type.getDocumentElementName();
    }

    private class XMLBeanHelper<BeanDoc extends XmlObject> {

        @SuppressWarnings("unchecked")
        public BeanDoc[] deepCopyArray(BeanDoc... docs) {
            if (docs == null) return null;
            BeanDoc[] copyOf = Arrays.copyOf(docs, docs.length);
            for (int i = 0; i < docs.length; i++) {
                copyOf[i] = (BeanDoc) docs[i].copy();
            }
            return (BeanDoc[]) copyOf;
        }
    }

    @Override
    public SearchRespDocument search(SearchReqDocument req) throws BaseFault {
        String workflowServiceName;
        {
            ImmutableResourceProperty rp = (ImmutableResourceProperty) properties.get(RPworkflowServiceName);
            WorkflowServiceNameDocument doc = (WorkflowServiceNameDocument) rp.getXml()[0];
            workflowServiceName = doc.getWorkflowServiceName();
        }
        WorkflowServiceHome home = (WorkflowServiceHome) Kernel.getKernel().getServiceHome(workflowServiceName);
        String[] uniqueIds = home.getInstanceIds();
        List<EndpointReferenceType> endpointReferences = new LinkedList<EndpointReferenceType>();
        for (String id : uniqueIds) {
            EndpointReferenceType epr = AddressingUtil.newEPR();
            epr.addNewAddress().setStringValue(Utilities.makeAddress(workflowServiceName, id));
            endpointReferences.add(epr);
        }
        SearchRespDocument response = SearchRespDocument.Factory.newInstance();
        SearchResp respType = response.addNewSearchResp();
        EndpointReferenceSet eprSet = respType.addNewEndpointReferenceSet();
        eprSet.setEndpointReferenceArray(endpointReferences.toArray(new EndpointReferenceType[0]));
        return response;
    }
}
