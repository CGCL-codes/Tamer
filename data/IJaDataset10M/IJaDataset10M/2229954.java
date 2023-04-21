package org.geometerplus.fbreader.network.authentication.litres;

import org.geometerplus.zlibrary.core.xml.ZLXMLReaderAdapter;
import org.geometerplus.zlibrary.core.network.ZLNetworkException;

class LitResAuthenticationXMLReader extends ZLXMLReaderAdapter {

    public final String HostName;

    public LitResAuthenticationXMLReader(String hostName) {
        HostName = hostName;
    }

    private ZLNetworkException myException;

    protected void setException(ZLNetworkException e) {
        myException = e;
    }

    protected void setErrorMessage(String errorMessage) {
        myException = new ZLNetworkException(true, errorMessage);
    }

    public ZLNetworkException getException() {
        return myException;
    }
}
