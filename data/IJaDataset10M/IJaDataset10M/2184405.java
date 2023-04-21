package com.aelitis.net.upnp.impl.services;

import org.gudy.azureus2.plugins.utils.xml.simpleparser.SimpleXMLParserDocumentNode;
import com.aelitis.net.upnp.*;

public class UPnPActionImpl implements UPnPAction {

    protected UPnPServiceImpl service;

    protected String name;

    protected UPnPActionImpl(UPnPServiceImpl _service, SimpleXMLParserDocumentNode node) {
        service = _service;
        name = node.getChild("name").getValue().trim();
    }

    public String getName() {
        return (name);
    }

    public UPnPService getService() {
        return (service);
    }

    public UPnPActionInvocation getInvocation() {
        return (new UPnPActionInvocationImpl(this));
    }
}
