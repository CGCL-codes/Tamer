package com.ark.fix.model.fixml;

import com.ark.fix.model.*;

public class LimitOrder extends FIXMLAggregate {

    private Price _Price;

    public Price getPrice() {
        return _Price;
    }

    public void setPrice(Price obj) {
        _Price = obj;
    }

    public void initPrice(Object obj) throws ModelException {
        if (_Price != null) throw new ModelException("Value has already been initialized for Price.");
        setPrice((Price) obj);
    }

    public String[] getProperties() {
        String[] properties = { "Price" };
        return properties;
    }

    public String[] getRequiredProperties() {
        String[] properties = {};
        return properties;
    }

    public String toFIXMessage() {
        StringBuffer sb = new StringBuffer("");
        if (_Price != null) sb.append(_Price.toFIXMessage());
        return sb.toString();
    }

    public String toFIXML(String ident) {
        StringBuffer sb = new StringBuffer("");
        sb.append(ident + "<LimitOrder>\n");
        if (_Price != null) sb.append(_Price.toFIXML(ident + "\t") + "\n");
        sb.append(ident + "</LimitOrder>");
        return sb.toString();
    }
}
