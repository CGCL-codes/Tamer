package uk.ac.ncl.neresc.dynasoar.hostprovider;

import org.apache.log4j.Logger;
import org.uddi4j.client.UDDIProxy;
import org.uddi4j.response.*;
import org.uddi4j.datatype.Name;
import org.uddi4j.datatype.tmodel.TModel;
import org.uddi4j.datatype.binding.*;
import org.uddi4j.datatype.service.BusinessService;
import org.uddi4j.util.*;
import org.uddi4j.transport.TransportFactory;
import java.util.Vector;
import java.util.Hashtable;
import uk.ac.ncl.neresc.dynasoar.fault.RegistryException;

/**
 * Created by IntelliJ IDEA.
 * User: nam48
 * Date: 21-Mar-2006
 * Time: 16:06:34
 * To change this template use File | Settings | File Templates.
 */
public class L2RegistryHelper {

    private static Logger mLog = Logger.getLogger(L2RegistryHelper.class.getName());

    private UDDIProxy proxy = null;

    private Hashtable<String, BusinessService> infoTable = null;

    private Hashtable<String, String> tModelStore = null;

    private String transportClassName = null;

    public L2RegistryHelper(String inquire, String publish, String transport) {
        try {
            mLog.debug("Setting transport class to: " + transport);
            transportClassName = transport;
            if (transportClassName != null) {
                System.setProperty(TransportFactory.PROPERTY_NAME, transportClassName);
            }
            mLog.debug("Setting up UDDI Proxy");
            proxy = new UDDIProxy();
            proxy.setInquiryURL(inquire);
            proxy.setPublishURL(publish);
            infoTable = new Hashtable<String, BusinessService>();
            tModelStore = new Hashtable<String, String>();
        } catch (Exception mx) {
            mLog.debug("Exception: " + mx.getMessage());
            mx.printStackTrace();
        }
    }

    private Vector getService(String serviceName) throws RegistryException {
        mLog.debug("Searching for " + serviceName);
        Vector services = new Vector();
        try {
            AuthToken token = proxy.get_authToken("", "");
            Vector names = new Vector();
            mLog.debug("Creating service name");
            names.add(new Name(serviceName));
            FindQualifiers findQualifiers = new FindQualifiers();
            Vector qualifier = new Vector();
            qualifier.add(new FindQualifier("exactNameMatch"));
            findQualifiers.setFindQualifierVector(qualifier);
            ServiceList serviceList = proxy.find_service(null, names, null, null, findQualifiers, 1);
            if (serviceList != null) {
                mLog.debug("Found service list");
            } else {
                mLog.debug("No service found");
            }
            Vector serviceInfoVector = serviceList.getServiceInfos().getServiceInfoVector();
            if (serviceInfoVector == null || serviceInfoVector.isEmpty()) {
                mLog.debug("No service found");
            } else {
                ServiceInfo serviceInfo = (ServiceInfo) serviceInfoVector.elementAt(0);
                String serviceKey = serviceInfo.getServiceKey();
                ServiceDetail svcDetails = proxy.get_serviceDetail(serviceKey);
                Vector businessServices = svcDetails.getBusinessServiceVector();
                if (businessServices.isEmpty()) {
                    mLog.error("Service not known in registry");
                    throw new RegistryException("Service not known in registry");
                } else {
                    BusinessService svc = (BusinessService) businessServices.get(0);
                    services.add(svc);
                    infoTable.put(serviceName, svc);
                    mLog.debug("Number of matching services: " + services.size());
                }
            }
        } catch (Exception ex) {
            mLog.debug("Exception: " + ex.getMessage());
            ex.printStackTrace();
            throw new RegistryException(ex.getMessage(), ex.getCause());
        }
        return services;
    }

    public void getAccessPoints(String serviceName, Vector accessPoints, Vector codePoints) {
        mLog.debug("Trying to get access points");
        try {
            Vector services = getService(serviceName);
            if (services == null || services.isEmpty()) {
                return;
            }
            for (int i = 0; i < services.size(); i++) {
                BusinessService svc = (BusinessService) services.get(i);
                BindingTemplates bTemps = svc.getBindingTemplates();
                if (bTemps != null) {
                    Vector bindingVector = bTemps.getBindingTemplateVector();
                    for (int j = 0; j < bindingVector.size(); j++) {
                        BindingTemplate binding = (BindingTemplate) bindingVector.get(j);
                        AccessPoint ap = binding.getAccessPoint();
                        accessPoints.add(ap.getText());
                    }
                }
                CategoryBag cBag = svc.getCategoryBag();
                if (cBag != null) {
                    Vector keyedRefVector = cBag.getKeyedReferenceVector();
                    for (int j = 0; j < keyedRefVector.size(); j++) {
                        KeyedReference keyRef = (KeyedReference) keyedRefVector.get(j);
                        String keyName = keyRef.getKeyName();
                        String keyValue = keyRef.getKeyValue();
                        if (keyName.equals("CodeStoreURL")) {
                            mLog.debug("Found CodeStore: " + keyValue);
                            codePoints.add(keyValue);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getVMName(String serviceName) {
        mLog.debug("Getting the virtual machine name");
        String VMName = null;
        try {
            BusinessService service = infoTable.get(serviceName);
            if (service != null) {
                mLog.debug("Found service in temporary registry");
                CategoryBag cBag = service.getCategoryBag();
                if (cBag != null) {
                    Vector keyedRefVector = cBag.getKeyedReferenceVector();
                    for (int j = 0; j < keyedRefVector.size(); j++) {
                        KeyedReference keyRef = (KeyedReference) keyedRefVector.get(j);
                        String keyName = keyRef.getKeyName();
                        String keyValue = keyRef.getKeyValue();
                        String tModelKey = keyRef.getTModelKey();
                        if (keyName.equals("VM-NAME")) {
                            mLog.debug("Found Virtual Machine Name: " + keyValue);
                            VMName = keyValue;
                            tModelStore.put(keyValue, tModelKey);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return VMName;
    }

    public String getServiceType(String serviceName) {
        mLog.debug("Getting the service type");
        String serviceType = null;
        try {
            BusinessService service = infoTable.get(serviceName);
            if (service != null) {
                mLog.debug("Found service in temporary registry");
                CategoryBag cBag = service.getCategoryBag();
                if (cBag != null) {
                    Vector keyedRefVector = cBag.getKeyedReferenceVector();
                    for (int j = 0; j < keyedRefVector.size(); j++) {
                        KeyedReference keyRef = (KeyedReference) keyedRefVector.get(j);
                        String keyName = keyRef.getKeyName();
                        String keyValue = keyRef.getKeyValue();
                        if (keyName.equals("ServiceType")) {
                            mLog.debug("Service Type: " + keyValue);
                            serviceType = keyValue;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return serviceType;
    }

    public String getServiceURIForVM(String serviceName) {
        mLog.debug("Getting the service uri in the VM");
        String serviceURI = null;
        try {
            BusinessService service = infoTable.get(serviceName);
            if (service != null) {
                mLog.debug("Found service in temporary registry");
                CategoryBag cBag = service.getCategoryBag();
                if (cBag != null) {
                    Vector keyedRefVector = cBag.getKeyedReferenceVector();
                    for (int j = 0; j < keyedRefVector.size(); j++) {
                        KeyedReference keyRef = (KeyedReference) keyedRefVector.get(j);
                        String keyName = keyRef.getKeyName();
                        String keyValue = keyRef.getKeyValue();
                        if (keyName.equals("ServiceURI")) {
                            mLog.debug("Service URI: " + keyValue);
                            serviceURI = keyValue;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return serviceURI;
    }

    public String getVMPort(String serviceName) {
        mLog.debug("Getting the service uri in the VM");
        String vmPort = null;
        try {
            BusinessService service = infoTable.get(serviceName);
            if (service != null) {
                mLog.debug("Found service in temporary registry");
                CategoryBag cBag = service.getCategoryBag();
                if (cBag != null) {
                    Vector keyedRefVector = cBag.getKeyedReferenceVector();
                    for (int j = 0; j < keyedRefVector.size(); j++) {
                        KeyedReference keyRef = (KeyedReference) keyedRefVector.get(j);
                        String keyName = keyRef.getKeyName();
                        String keyValue = keyRef.getKeyValue();
                        if (keyName.equals("VM-PORT")) {
                            mLog.debug("VM Container Port: " + keyValue);
                            vmPort = keyValue;
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return vmPort;
    }

    public Vector getAllServicesByVMName(String vmName) {
        Vector svcObjects = new Vector();
        mLog.debug("Getting all services for a VM");
        try {
            AuthToken token = proxy.get_authToken("", "");
            String tModelKey = tModelStore.get(vmName);
            System.out.println("TModelKey: " + tModelKey);
            CategoryBag cBagInp = new CategoryBag();
            KeyedReference keyRefInp = new KeyedReference();
            keyRefInp.setKeyName("VM-NAME");
            keyRefInp.setKeyValue(vmName);
            keyRefInp.setTModelKey(tModelKey);
            cBagInp.add(keyRefInp);
            ServiceList serviceList = proxy.find_service(null, null, cBagInp, null, null, 0);
            if (serviceList != null) {
                mLog.debug("Found service list");
            } else {
                mLog.debug("No service found");
            }
            Vector serviceInfoVector = serviceList.getServiceInfos().getServiceInfoVector();
            if (serviceInfoVector == null || serviceInfoVector.isEmpty()) {
                mLog.debug("No service found");
            } else {
                for (int i = 0; i < serviceInfoVector.size(); i++) {
                    ServiceInfo serviceInfo = (ServiceInfo) serviceInfoVector.elementAt(i);
                    String serviceKey = serviceInfo.getServiceKey();
                    ServiceDetail svcDetails = proxy.get_serviceDetail(serviceKey);
                    Vector businessServices = svcDetails.getBusinessServiceVector();
                    if (businessServices.isEmpty()) {
                        mLog.error("Service not known in registry");
                        throw new RegistryException("Service not known in registry");
                    } else {
                        BusinessService svc = (BusinessService) businessServices.get(0);
                        ServiceInfoObject info = new ServiceInfoObject();
                        info.setServiceName(svc.getDefaultName().getText());
                        System.out.println("SERVICE NAME: " + svc.getDefaultName().getText());
                        infoTable.put(svc.getDefaultName().getText(), svc);
                        info.setVmName(vmName);
                        CategoryBag cBag = svc.getCategoryBag();
                        String uri = "http://VM-IP-ADDRESS:PORT/ServiceURI";
                        if (cBag != null) {
                            Vector keyedRefVector = cBag.getKeyedReferenceVector();
                            for (int j = 0; j < keyedRefVector.size(); j++) {
                                KeyedReference keyRef = (KeyedReference) keyedRefVector.get(j);
                                String keyName = keyRef.getKeyName();
                                String keyValue = keyRef.getKeyValue();
                                if (keyName.equals("VM-PORT")) {
                                    mLog.debug("VM Container Port: " + keyValue);
                                    uri = uri.replaceAll("PORT", keyValue);
                                } else if (keyName.equals("ServiceURI")) {
                                    mLog.debug("Service URI: " + keyValue);
                                    uri = uri.replaceAll("ServiceURI", keyValue);
                                }
                            }
                        }
                        mLog.debug("Complete URI: " + uri);
                        info.setServiceURI(uri);
                        info.setUrlValid(false);
                        svcObjects.add(info);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return svcObjects;
    }

    public void updateServiceWithNewAccessPoint(String serviceName, String accessPoint) {
        mLog.debug("Updating the service in the registry with the new access point");
        try {
            AuthToken token = proxy.get_authToken("", "");
            BusinessService service = infoTable.get(serviceName);
            if (service != null) {
                mLog.debug("Found service in temporary registry");
                mLog.debug(service.getBusinessKey());
                mLog.debug(service.getDefaultDescriptionString());
                mLog.debug(service.getDefaultName());
                mLog.debug(service.getServiceKey());
                Vector tModels = new Vector();
                TModel tModel = new TModel("", "ServiceAccessPoint");
                tModels.add(tModel);
                TModelDetail tModelDetail = proxy.save_tModel(token.getAuthInfoString(), tModels);
                Vector tModelVector = tModelDetail.getTModelVector();
                String tModelKey = ((TModel) (tModelVector.elementAt(0))).getTModelKey();
                Vector tModelInstanceInfoVector = new Vector();
                TModelInstanceInfo tModelInstanceInfo = new TModelInstanceInfo(tModelKey);
                tModelInstanceInfoVector.add(tModelInstanceInfo);
                TModelInstanceDetails tModelInstanceDetails = new TModelInstanceDetails();
                tModelInstanceDetails.setTModelInstanceInfoVector(tModelInstanceInfoVector);
                AccessPoint newAP = new AccessPoint();
                newAP.setText(accessPoint);
                newAP.setURLType("http");
                BindingTemplate newBinding = new BindingTemplate("", tModelInstanceDetails, newAP);
                newBinding.setServiceKey(service.getServiceKey());
                Vector bindings = new Vector();
                bindings.add(newBinding);
                BindingDetail bindingDetail = proxy.save_binding(token.getAuthInfoString(), bindings);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        L2RegistryHelper l2Reg = new L2RegistryHelper("http://budle.ncl.ac.uk:8090/grimoires/services/inquire", "http://budle.ncl.ac.uk:8090/grimoires/services/publish", "org.uddi4j.transport.ApacheAxisTransport");
        l2Reg.getAccessPoints("TestService1", new Vector(), new Vector());
        System.out.println("ServiceType - " + l2Reg.getServiceType("TestService1"));
        System.out.println("VMName - " + l2Reg.getVMName("TestService1"));
        Vector svcObj = l2Reg.getAllServicesByVMName("MyTestVM");
        for (int i = 0; i < svcObj.size(); i++) {
            ServiceInfoObject svc = (ServiceInfoObject) svcObj.get(i);
            System.out.println("Service Name: " + svc.getServiceName());
            System.out.println("Service URI: " + svc.getServiceURI());
            System.out.println("VM Name: " + svc.getVmName());
            String IP = svc.getServiceURI().split(":")[1].substring(2).trim();
            System.out.println("IP: " + IP);
        }
    }
}
