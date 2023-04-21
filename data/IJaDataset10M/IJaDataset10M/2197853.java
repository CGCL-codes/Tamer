package com.induslogic.uddi.server.service;

import java.io.*;
import java.sql.*;
import java.net.*;
import org.w3c.dom.*;
import com.induslogic.uddi.server.inquiry.*;
import com.induslogic.uddi.server.publish.*;
import com.induslogic.uddi.server.inquiry.*;
import com.induslogic.uddi.server.replication.*;
import com.induslogic.uddi.*;

/**
 * UddiService.java
 * Created:
 *
 * @author rohit makin
 * @version
 */
public class UddiService {

    Connection con;

    public UddiService(Connection c) {
        con = c;
    }

    public UddiObject invokeAppropriateApi(String name, UddiObject param) throws SQLException, UDDIXmlException, MalformedURLException, IOException, ClassNotFoundException {
        if (name.equals("do_ping")) {
            DoPing doping = new DoPing(new Do_Ping(param), con);
            return doping.getData();
        }
        if (name.equals("find_binding")) {
            FindBinding findBinding = new FindBinding(new Find_Binding(param), con);
            return findBinding.getData();
        }
        if (name.equals("find_business")) {
            FindBusiness findBusiness = new FindBusiness(new Find_Business(param), con);
            return findBusiness.getData();
        }
        if (name.equals("find_relatedBusinesses")) {
            FindRelatedBusinesses findRelatedBusinesses = new FindRelatedBusinesses(new Find_RelatedBusinesses(param), con);
            return findRelatedBusinesses.getData();
        }
        if (name.equals("find_service")) {
            FindService findService = new FindService(new Find_Service(param), con);
            return findService.getData();
        }
        if (name.equals("find_tModel")) {
            FindTModel findTModel = new FindTModel(new Find_TModel(param), con);
            return findTModel.getData();
        }
        if (name.equals("get_bindingDetail")) {
            BindingDetails bindingDetails = new BindingDetails(new Get_BindingDetail(param), con);
            return bindingDetails.getData();
        }
        if (name.equals("get_businessDetail")) {
            BusinessDetails businessDetails = new BusinessDetails(new Get_BusinessDetail(param), con);
            return businessDetails.getData();
        }
        if (name.equals("get_changeRecords")) {
            GetChangeRecords getChangeRecords = new GetChangeRecords(new Get_ChangeRecords(param), con);
            return getChangeRecords.getData();
        }
        if (name.equals("get_serviceDetail")) {
            ServiceDetails serviceDetails = new ServiceDetails(new Get_ServiceDetail(param), con);
            return serviceDetails.getData();
        }
        if (name.equals("get_tModelDetail")) {
            TModelDetails tmodelDetails = new TModelDetails(new Get_TModelDetail(param), con);
            return tmodelDetails.getData();
        }
        if (name.equals("delete_binding")) {
            DeleteBinding deleteBinding = new DeleteBinding(con);
            return deleteBinding.delete(new Delete_Binding(param));
        }
        if (name.equals("delete_business")) {
            DeleteBusiness deleteBusiness = new DeleteBusiness(con);
            return deleteBusiness.delete(new Delete_Business(param));
        }
        if (name.equals("delete_service")) {
            DeleteService deleteService = new DeleteService(con);
            return deleteService.delete(new Delete_Service(param));
        }
        if (name.equals("delete_tModel")) {
            DeleteTModel deleteTModel = new DeleteTModel(con);
            return deleteTModel.delete(new Delete_TModel(param));
        }
        if (name.equals("discard_authToken")) {
            DiscardAuthToken discardAuthToken = new DiscardAuthToken(con);
            return discardAuthToken.discardToken(new Discard_AuthToken(param));
        }
        if (name.equals("get_authToken")) {
            GetAuthToken getAuthToken = new GetAuthToken(con);
            return getAuthToken.getToken(new Get_AuthToken(param));
        }
        if (name.equals("get_registeredInfo")) {
            GetRegisteredInfo getRegisteredInfo = new GetRegisteredInfo(new Get_RegisteredInfo(param), con);
            return getRegisteredInfo.getData();
        }
        if (name.equals("notify_changeRecordsAvailable")) {
            NotifyChangeRecordsAvailable notifyChangeRecordsAvaliable = new NotifyChangeRecordsAvailable(new Notify_ChangeRecordsAvailable(param), con);
            return notifyChangeRecordsAvaliable.getData();
        }
        if (name.equals("save_binding")) {
            SaveBinding saveBinding = new SaveBinding(con);
            return saveBinding.save(new Save_Binding(param));
        }
        if (name.equals("save_business")) {
            SaveBusiness saveBusiness = new SaveBusiness(con);
            return saveBusiness.getData(new Save_Business(param));
        }
        if (name.equals("save_service")) {
            SaveService saveService = new SaveService(con);
            return saveService.save(new Save_Service(param));
        }
        if (name.equals("save_tModel")) {
            SaveTModel saveTModel = new SaveTModel(con);
            return saveTModel.getData(new Save_TModel(param));
        }
        DispositionReport dr = new DispositionReport();
        dr.setResult(Result.getResult(ErrorCodes.E_UNSUPPORTED, "api not supported"));
        return dr;
    }
}
