package com.liveims.webims;

import com.nextenso.proxylet.ProxyletConfig;
import com.nextenso.proxylet.ProxyletException;
import com.nextenso.proxylet.http.BufferedHttpResponseProxylet;
import com.nextenso.proxylet.http.HttpHeaders;
import com.nextenso.proxylet.http.HttpResponse;
import com.nextenso.proxylet.http.HttpResponseProlog;

public class ResponseProxylet implements BufferedHttpResponseProxylet {

    public int doResponse(HttpResponse response) throws ProxyletException {
        return LAST_PROXYLET;
    }

    public int accept(HttpResponseProlog prolog, HttpHeaders headers) {
        return ACCEPT;
    }

    public void destroy() {
    }

    public String getProxyletInfo() {
        return null;
    }

    public void init(ProxyletConfig cnf) throws ProxyletException {
    }
}
