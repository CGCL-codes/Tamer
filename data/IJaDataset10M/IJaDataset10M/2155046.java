package com.google.code.head2head.gwtmodules.client.util;

import com.google.code.head2head.gwtmodules.client.services.FacadeServices;
import com.google.code.head2head.gwtmodules.client.services.FacadeServicesAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class ServiceLocator {

    public static FacadeServicesAsync getFacadeServicesAsync() {
        FacadeServicesAsync svc = (FacadeServicesAsync) GWT.create(FacadeServices.class);
        ServiceDefTarget endpoint = (ServiceDefTarget) svc;
        String moduleRelativeURL = GWT.getModuleBaseURL() + "../userInfoService";
        endpoint.setServiceEntryPoint(moduleRelativeURL);
        return svc;
    }
}
