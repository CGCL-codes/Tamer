package com.tilab.wsig.uddi;

import java.util.Iterator;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.uddi4j.UDDIException;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.OverviewDoc;
import org.uddi4j.datatype.OverviewURL;
import org.uddi4j.datatype.binding.AccessPoint;
import org.uddi4j.datatype.binding.BindingTemplate;
import org.uddi4j.datatype.binding.TModelInstanceDetails;
import org.uddi4j.datatype.binding.TModelInstanceInfo;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.response.AuthToken;
import org.uddi4j.response.BindingDetail;
import org.uddi4j.response.DispositionReport;
import org.uddi4j.response.Result;
import org.uddi4j.response.ServiceDetail;
import org.uddi4j.response.ServiceInfo;
import org.uddi4j.response.ServiceInfos;
import org.uddi4j.response.ServiceList;
import org.uddi4j.response.TModelDetail;
import org.uddi4j.transport.TransportException;
import org.uddi4j.util.CategoryBag;
import org.uddi4j.util.FindQualifiers;
import org.uddi4j.util.KeyedReference;
import org.uddi4j.util.ServiceKey;
import org.uddi4j.util.TModelBag;
import org.uddi4j.util.TModelKey;
import com.tilab.wsig.WSIGConfiguration;
import com.tilab.wsig.store.WSIGService;
import com.tilab.wsig.wsdl.WSDLUtils;

public class UDDIManager {

    private Logger log = Logger.getLogger(UDDIManager.class.getName());

    private UDDIProxy uddiProxy;

    private String businessKey;

    private String userName;

    private String password;

    public UDDIManager() {
        try {
            setupUDDI4j();
        } catch (Exception e) {
            log.error("UDDI setup error", e);
        }
    }

    /**
	 * UDDIRegister
	 * @param wsigService
	 * @throws Exception
	 */
    public synchronized ServiceKey UDDIRegister(WSIGService wsigService) throws Exception {
        ServiceKey serviceKey = null;
        String serviceName = wsigService.getServiceName();
        log.info("Register service " + serviceName + " into UDDI");
        try {
            String tName = "WSIG's tModel for " + serviceName;
            String sName = "WSIG's businessService for " + serviceName;
            TModel tModel;
            tModel = createTModel(WSDLUtils.getWsdlUrl(serviceName).toExternalForm(), tName);
            CategoryBag cb = new CategoryBag();
            KeyedReference kr;
            String operation;
            Iterator it = wsigService.getOperations().iterator();
            while (it.hasNext()) {
                operation = (String) it.next();
                kr = new KeyedReference();
                kr.setTModelKey(WSIGConfiguration.getInstance().getProperty(WSIGConfiguration.KEY_UDDI_TMODEL));
                kr.setKeyName("fipaServiceName");
                kr.setKeyValue(serviceName);
                cb.add(kr);
                kr = new KeyedReference();
                kr.setTModelKey(WSIGConfiguration.getInstance().getProperty(WSIGConfiguration.KEY_UDDI_TMODEL));
                kr.setKeyName(operation);
                kr.setKeyValue(operation);
                cb.add(kr);
            }
            BusinessService businessService = new BusinessService("");
            businessService.setDefaultNameString(sName, null);
            businessService.setBusinessKey(businessKey);
            businessService.setCategoryBag(cb);
            Vector services = new Vector();
            services.addElement(businessService);
            ServiceDetail serviceDetail = uddiProxy.save_service(getAuthToken().getAuthInfoString(), services);
            Vector businessServices = serviceDetail.getBusinessServiceVector();
            BusinessService businessServiceReturned = (BusinessService) (businessServices.elementAt(0));
            serviceKey = new ServiceKey(businessServiceReturned.getServiceKey());
            createBindingTemplate(new AccessPoint(WSIGConfiguration.getInstance().getWsigUri(), "http"), serviceKey, new TModelKey(tModel.getTModelKey()));
        } catch (UDDIException e) {
            log.error("UDDI Registration", e);
        } catch (TransportException e) {
            log.error("UDDI Registration", e);
        }
        return serviceKey;
    }

    /**
	 * UDDIDeregister
	 * @param wsigService
	 * @throws Exception
	 */
    public synchronized void UDDIDeregister(WSIGService wsigService) throws Exception {
        log.info("Deregister service " + wsigService.getServiceName() + " from UDDI");
        ServiceKey k = wsigService.getUddiServiceKey();
        DispositionReport dr;
        try {
            BusinessService bs = takeService(k);
            BindingTemplate bt = bs.getBindingTemplates().get(0);
            TModelKey tModelKey = new TModelKey(bt.getTModelInstanceDetails().get(0).getTModelKey());
            dr = uddiProxy.delete_tModel(getAuthToken().getAuthInfoString(), tModelKey.getText());
            if (!dr.success()) {
                log.error("Error during deletion of TModel\n" + "\n operator:" + dr.getOperator() + "\n generic:" + dr.getGeneric());
            }
            dr = uddiProxy.delete_service(getAuthToken().getAuthInfoString(), k.getText());
            if (!dr.success()) {
                log.error("Error during deletion of Service\n" + "\n operator:" + dr.getOperator() + "\n generic:" + dr.getGeneric());
                Vector results = dr.getResultVector();
                for (int j = 0; j < results.size(); j++) {
                    Result r = (Result) results.elementAt(j);
                    log.error(" errno:" + r.getErrno());
                    if (r.getErrInfo() != null) {
                        log.error("\n errCode:" + r.getErrInfo().getErrCode() + "\n errInfoText:" + r.getErrInfo().getText());
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error during UDDI deregistration", e);
            throw e;
        }
    }

    /**
	 * removes old records in UDDI
	 */
    private void resetUDDI4j() {
        ServiceList sl = new ServiceList();
        try {
            Vector names = new Vector(1);
            names.add(new Name("%WSIG%"));
            CategoryBag cb = new CategoryBag();
            TModelBag tmb = new TModelBag();
            FindQualifiers fq = new FindQualifiers();
            int maxRows = Integer.MAX_VALUE;
            sl = uddiProxy.find_service(businessKey, names, cb, tmb, fq, maxRows);
            ServiceInfo info;
            ServiceInfos infos = sl.getServiceInfos();
            String s;
            Vector sKeys = new Vector();
            int k;
            if (infos.size() < 1) {
                log.debug("Old records do not exist in UDDI.");
                return;
            }
            for (k = 0; k < infos.size(); k++) {
                info = infos.get(k);
                s = info.getServiceKey();
                log.debug(" service to delete: " + s);
                sKeys.add(s);
            }
            DispositionReport dr;
            dr = uddiProxy.delete_service(getAuthToken().getAuthInfoString(), sKeys);
        } catch (UDDIException ue) {
            log.debug(ue);
        } catch (TransportException te) {
            log.debug(te);
        }
    }

    /**
	 * sets up the DFToUDDI4j. It starts components required.
	 * Class fields authToken and uddiProxy are set properly as main result.
	 */
    private void setupUDDI4j() {
        WSIGConfiguration c = WSIGConfiguration.getInstance();
        synchronized (c) {
            System.setProperty(WSIGConfiguration.KEY_UDDI4J_LOG_ENABLED, c.getUDDI4jLogEnabled());
            System.setProperty(WSIGConfiguration.KEY_UDDI4J_TRANSPORT_CLASS, c.getUDDI4jTransportClass());
            businessKey = c.getBusinessKey();
            userName = c.getUserName();
            password = c.getUserPassword();
            uddiProxy = new UDDIProxy();
            try {
                uddiProxy.setInquiryURL(c.getQueryManagerURL());
                uddiProxy.setPublishURL(c.getLifeCycleManagerURL());
            } catch (Exception e) {
                log.error(e);
            }
        }
        resetUDDI4j();
    }

    /**
	 * asks for an authentification.
	 * Configuration's name and password are used.
	 *
	 * @return an authentification
	 * @throws TransportException if transport problems are occured
	 * @throws UDDIException	  if UDDI problems are occured
	 */
    private AuthToken getAuthToken() throws TransportException, UDDIException {
        log.debug("Ask for authToken.");
        AuthToken authToken = uddiProxy.get_authToken(userName, password);
        log.debug("Returned authToken from a UDDI:" + authToken.getAuthInfoString());
        return authToken;
    }

    /**
	 * creates and registers a bindingTemplate
	 *
	 * @param accessPoint an accessPoint for a service
	 * @param serviceKey  service key for a bindingTemplate
	 * @param tModelKey   tModel key to be reffered
	 * @return a new bindingTemplate registered in a UDDI
	 */
    public BindingTemplate createBindingTemplate(AccessPoint accessPoint, ServiceKey serviceKey, TModelKey tModelKey) {
        log.debug("A bindingTemplate is going to be created.");
        BindingTemplate bindingTemplateReturned = null;
        try {
            Vector tModelInstanceInfoVector = new Vector();
            TModelInstanceInfo tModelInstanceInfo = new TModelInstanceInfo(tModelKey.getText());
            tModelInstanceInfoVector.add(tModelInstanceInfo);
            TModelInstanceDetails tModelInstanceDetails = new TModelInstanceDetails();
            tModelInstanceDetails.setTModelInstanceInfoVector(tModelInstanceInfoVector);
            BindingTemplate bindingTemplate = new BindingTemplate("", tModelInstanceDetails, accessPoint);
            bindingTemplate.setServiceKey(serviceKey.getText());
            Vector bindingTemplatesVector = new Vector();
            bindingTemplatesVector.addElement(bindingTemplate);
            BindingDetail bindingDetail = uddiProxy.save_binding(getAuthToken().getAuthInfoString(), bindingTemplatesVector);
            Vector bindingTemplateVector = bindingDetail.getBindingTemplateVector();
            bindingTemplateReturned = (BindingTemplate) (bindingTemplateVector.elementAt(0));
        } catch (UDDIException e) {
            log.error(e);
        } catch (TransportException e) {
            log.error(e);
        }
        log.debug("New BindingKey: " + bindingTemplateReturned.getBindingKey());
        return bindingTemplateReturned;
    }

    /**
	 * creates and registers a new TModel
	 *
	 * @param wsdlURL url for a wsdl's description
	 * @param name	name for a tModel
	 * @return tModel created
	 */
    public TModel createTModel(String wsdlURL, String name) {
        log.debug("A tModel is going to be created.");
        TModel tModelReturned = null;
        try {
            OverviewURL overviewURL = new OverviewURL(wsdlURL);
            OverviewDoc overviewDoc = new OverviewDoc();
            overviewDoc.setOverviewURL(overviewURL);
            TModel tModel = new TModel("", name);
            tModel.setOverviewDoc(overviewDoc);
            Vector tModelsVector = new Vector();
            tModelsVector.addElement(tModel);
            TModelDetail tModelDetail = uddiProxy.save_tModel(getAuthToken().getAuthInfoString(), tModelsVector);
            tModelsVector = tModelDetail.getTModelVector();
            tModelReturned = (TModel) (tModelsVector.elementAt(0));
        } catch (UDDIException e) {
            log.error(e);
        } catch (TransportException e) {
            log.error(e);
        }
        log.debug("New tModelKey: " + tModelReturned.getTModelKey());
        return tModelReturned;
    }

    /**
	 * gets a service identified by a key
	 *
	 * @param serviceKey key of service requested
	 * @return service requested
	 * @throws UDDIException
	 * @throws TransportException
	 */
    public BusinessService takeService(ServiceKey serviceKey) throws UDDIException, TransportException {
        ServiceDetail serviceDetail = uddiProxy.get_serviceDetail(serviceKey.getText());
        Vector businessServices = serviceDetail.getBusinessServiceVector();
        return (BusinessService) (businessServices.elementAt(0));
    }
}
