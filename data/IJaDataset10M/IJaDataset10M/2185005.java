package com.vlee.webservice.maybank2u.databinding;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import com.vlee.webservice.maybank2u.XmlQuery;
import com.vlee.webservice.maybank2u.XmlQueryLocator;
import com.vlee.webservice.maybank2u.XmlQuerySoap;

public class XmlQueryClient {

    private static XmlQueryClient instance = null;

    private XmlQuerySoap port = null;

    private XmlQueryClient() throws ServiceException {
        XmlQuery service = new XmlQueryLocator();
        port = service.getXmlQuerySoap();
    }

    public static XmlQueryClient getInstance() throws ServiceException {
        if (instance == null) {
            instance = new XmlQueryClient();
        }
        return instance;
    }

    public String requestPayeeInfo(String userId, String password, String payeeCode, String billNo, String amount) throws RemoteException {
        return port.requestPayeeInfo(userId, password, payeeCode, billNo, amount);
    }
}
