package com.okrasz.elvis.elvisnet;

import org.uddi4j.*;
import org.uddi4j.client.*;
import org.uddi4j.datatype.*;
import org.uddi4j.datatype.assertion.*;
import org.uddi4j.datatype.binding.*;
import org.uddi4j.datatype.business.*;
import org.uddi4j.datatype.service.*;
import org.uddi4j.datatype.tmodel.*;
import org.uddi4j.request.*;
import org.uddi4j.response.*;
import org.uddi4j.util.*;
import org.uddi4j.transport.TransportException;
import org.w3c.dom.Element;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.util.Vector;
import java.util.Properties;
import java.io.*;
import java.net.*;
import com.okrasz.elvis.db.sql.*;
import java.sql.*;
import javax.security.auth.login.FailedLoginException;
import com.okrasz.common.ContextKeeper;

/**
 *
 * @author  okrasz
 */
public class UDDI {

    UDDIProxy proxy = new UDDIProxy();

    String user = null;

    String pass = null;

    AuthToken token = null;

    /** Creates a new instance of UDDI */
    public UDDI(String inquiry, String publish, String user, String pass) throws MalformedURLException {
        if (inquiry == null && publish == null) throw new MalformedURLException("Musi być podany adres URL do wyszukiwania lub publikowania");
        if (inquiry != null) proxy.setInquiryURL(inquiry);
        if (publish != null) proxy.setPublishURL(publish);
        this.user = user;
        this.pass = pass;
    }

    public UDDI(String user, String pass) throws UnknownHostException, SQLException, MalformedURLException {
        ResultSet rs = DbUddi.getInstance().getDefault();
        if (rs.next()) {
            System.out.println(rs.getString("inquiry"));
            proxy.setInquiryURL(rs.getString("inquiry"));
            proxy.setPublishURL(rs.getString("publish"));
        } else {
            rs = DbUddi.getInstance().doSelect(null);
            if (rs.next()) {
                System.out.println(rs.getString("inquiry"));
                proxy.setInquiryURL(rs.getString("inquiry"));
                proxy.setPublishURL(rs.getString("publish"));
            } else {
                throw new UnknownHostException("Nie informacji o węzłach UDDI w bazie danych");
            }
        }
        this.user = user;
        this.pass = pass;
    }

    public UDDI() throws UnknownHostException, SQLException, MalformedURLException {
        this(null, null);
    }

    public Vector findElpSearch() throws UDDIException, TransportException {
        TModelBag tModels = new TModelBag();
        tModels.add(new TModelKey(ContextKeeper.getContext().getInitParameter("ElpSearchTModel")));
        ServiceList services = proxy.find_service("", null, null, tModels, null, 0);
        ServiceInfos serviceInfos = services.getServiceInfos();
        Vector url = new Vector(serviceInfos.size());
        for (int i = 0; i < serviceInfos.size(); i++) {
            ServiceInfo service = serviceInfos.get(i);
            if (DbUddiEntries.getInstance().hasKey(service.getBusinessKey())) continue;
            BindingDetail bindingDetails = proxy.find_binding(null, service.getServiceKey(), tModels, 0);
            Vector bindings = bindingDetails.getBindingTemplateVector();
            AccessPoint endpoint = null;
            for (int j = 0; j < bindings.size(); j++) {
                AccessPoint point = ((BindingTemplate) bindings.get(j)).getAccessPoint();
                if (point == null) continue;
                url.add(point.getText());
            }
        }
        return url;
    }

    /**
     * Publishes library in UDDI service
     * @param library - the name of the library
     * @param url - url to the ELP services
     */
    public void publish(String library, String url) throws UDDIException, MalformedURLException, FailedLoginException, TransportException {
        String businessKey = publishBusiness(library);
        System.out.println("Library business key: " + businessKey);
        Vector namesSearch = new Vector();
        namesSearch.add(new Name("Usługa wyszukująca w bibliotece", "pl"));
        namesSearch.add(new Name("Library query service", "en"));
        String serviceKeySearch = publishService(businessKey, namesSearch);
        System.out.println("Library search service key: " + serviceKeySearch);
        String bindingKeySearch = publishBinding(serviceKeySearch, ContextKeeper.getContext().getInitParameter("ElpSearchTModel"), url);
        System.out.println("Library search binding key: " + bindingKeySearch);
        Vector namesInfo = new Vector();
        namesInfo.add(new Name("Usługa informacyjna o bibliotece", "pl"));
        namesInfo.add(new Name("Library information service", "en"));
        String serviceKeyInfo = publishService(businessKey, namesInfo);
        System.out.println("Library info service key: " + serviceKeyInfo);
        String bindingKeyInfo = publishBinding(serviceKeyInfo, ContextKeeper.getContext().getInitParameter("ElpInfoTModel"), url);
        System.out.println("Library info service key: " + bindingKeyInfo);
    }

    /**
     * Publishes business in UDDI
     * @param businessName - a name of the service being published
     * @return businessKey
     */
    public String publishBusiness(String businessName) throws UDDIException, FailedLoginException, TransportException {
        if (token == null) authorize();
        Vector entities = new Vector();
        BusinessEntity be = new BusinessEntity("", businessName, "pl");
        entities.addElement(be);
        BusinessDetail bd = proxy.save_business(token.getAuthInfoString(), entities);
        Vector businessEntities = bd.getBusinessEntityVector();
        BusinessEntity returnedBusinessEntity = (BusinessEntity) (businessEntities.elementAt(0));
        String key = ((BusinessEntity) businessEntities.elementAt(0)).getBusinessKey();
        DbUddiEntries.getInstance().insertUddiEntry(key, null, DbUddiEntries.BUSINESS_ENTRY);
        return key;
    }

    /**
     * Publishes a service within specified business
     * @param businessKey - the UDDI key that identifies the business
     * @param name - name of business that is going to be published
     * @param tModelKey - the UDDI key that identifies technical model which is 
     *                    implemented by the service
     * @param url - url where the service is bound
     */
    public String publishService(String businessKey, Vector names) throws UDDIException, MalformedURLException, FailedLoginException, TransportException {
        if (token == null) authorize();
        BusinessService businessService = new BusinessService("");
        businessService.setNameVector(names);
        businessService.setBusinessKey(businessKey);
        Vector services = new Vector();
        services.addElement(businessService);
        ServiceDetail serviceDetail = proxy.save_service(token.getAuthInfoString(), services);
        Vector businessServices = serviceDetail.getBusinessServiceVector();
        BusinessService businessServiceReturned = (BusinessService) (businessServices.elementAt(0));
        String key = businessServiceReturned.getServiceKey();
        DbUddiEntries.getInstance().insertUddiEntry(key, businessKey, DbUddiEntries.SERVICE_ENTRY);
        return key;
    }

    public String publishBinding(String serviceKey, String tModelKey, String url) throws UDDIException, MalformedURLException, FailedLoginException, TransportException {
        if (token == null) authorize();
        String[] urlParts = url.split(":", 2);
        String protocol = url.substring(0, url.indexOf(':'));
        Vector tModelInstanceInfoVector = new Vector();
        TModelInstanceInfo tModelInstanceInfo = new TModelInstanceInfo(tModelKey);
        tModelInstanceInfoVector.add(tModelInstanceInfo);
        TModelInstanceDetails tModelInstanceDetails = new TModelInstanceDetails();
        tModelInstanceDetails.setTModelInstanceInfoVector(tModelInstanceInfoVector);
        Vector bindingTemplatesVector = new Vector();
        BindingTemplate bindingTemplate = new BindingTemplate("", tModelInstanceDetails, new AccessPoint(url, protocol));
        bindingTemplate.setServiceKey(serviceKey);
        bindingTemplatesVector.addElement(bindingTemplate);
        BindingDetail bindingDetail = proxy.save_binding(token.getAuthInfoString(), bindingTemplatesVector);
        Vector bindingTemplateVector = bindingDetail.getBindingTemplateVector();
        BindingTemplate bindingTemplateReturned = (BindingTemplate) (bindingTemplateVector.elementAt(0));
        String key = bindingTemplateReturned.getBindingKey();
        DbUddiEntries.getInstance().insertUddiEntry(key, serviceKey, DbUddiEntries.BINDING_ENTRY);
        return key;
    }

    public AuthToken authorize() throws FailedLoginException, TransportException, UDDIException {
        if (user == null || pass == null) throw new javax.security.auth.login.FailedLoginException("Login i hasło muszą być podane");
        try {
            return token = proxy.get_authToken(user, pass);
        } catch (UDDIException e) {
            throw new javax.security.auth.login.FailedLoginException("Proces autoryzacji nie powiódł się!");
        }
    }
}
