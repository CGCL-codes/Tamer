package com.wrupple.muba.common.server.bussiness.session;

public interface UserSessionInformationProvider {

    boolean isGranted(String roleCatalogMaster);

    Long getUserDomain();

    Long getUserPersonId();

    Object getAuthenticationPrincipal();
}
