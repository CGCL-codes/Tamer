package org.fao.fenix.web.client.service;

import com.google.gwt.user.client.rpc.RemoteService;

public interface REService extends RemoteService {

    public String doubleValue(int value);
}
