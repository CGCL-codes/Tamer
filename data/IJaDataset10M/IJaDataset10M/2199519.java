package com.aelitis.net.upnp.impl.services;

import com.aelitis.net.upnp.*;

public class UPnPActionArgumentImpl implements UPnPActionArgument {

    protected String name;

    protected String value;

    protected UPnPActionArgumentImpl(String _name, String _value) {
        name = _name;
        value = _value;
    }

    public String getName() {
        return (name);
    }

    public String getValue() {
        return (value);
    }
}
