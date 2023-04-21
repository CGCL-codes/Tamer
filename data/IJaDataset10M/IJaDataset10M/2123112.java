package org.openkonnect.interceptor.openbravo;

import org.openkonnect.api.OpenKonnectException;
import org.openkonnect.api.model.OpenKonnectEvent;

public abstract interface IInterceptorService {

    abstract String getOKMonitorTopic();

    abstract void postOpenKonnectEvent(OpenKonnectEvent oke) throws OpenKonnectException;

    abstract void monitor(String mmesg);
}
