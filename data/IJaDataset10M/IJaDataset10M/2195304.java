package ddd.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DomainGatewayAsync {

    void invokeDomainService(String serviceId, String actionId, String[] args, AsyncCallback callback);

    void queryBeanInfo(String serviceId, AsyncCallback callback);
}
