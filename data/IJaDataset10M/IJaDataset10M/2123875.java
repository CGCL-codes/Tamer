package com.ark.fix.model.fixml;

import com.ark.fix.model.*;

public class BidDescList extends FIXMLAggregate {

    private NoBidDesc _NoBidDesc;

    private FIXMLObjSeq _BidDescGroup;

    public NoBidDesc getNoBidDesc() {
        return _NoBidDesc;
    }

    public void setNoBidDesc(NoBidDesc obj) {
        _NoBidDesc = obj;
    }

    public void initNoBidDesc(Object obj) throws ModelException {
        if (_NoBidDesc != null) throw new ModelException("Value has already been initialized for NoBidDesc.");
        setNoBidDesc((NoBidDesc) obj);
    }

    public FIXMLObjSeq getBidDescGroup() {
        return _BidDescGroup;
    }

    public void setBidDescGroup(FIXMLObjSeq objs) {
        _BidDescGroup = objs;
    }

    public void initBidDescGroup(Object obj) {
        if (_BidDescGroup == null) {
            _BidDescGroup = new FIXMLObjSeq(BidDescGroup.class);
        }
        _BidDescGroup.add((BidDescGroup) obj);
    }

    public String[] getProperties() {
        String[] properties = { "NoBidDesc", "BidDescGroup" };
        return properties;
    }

    public String[] getRequiredProperties() {
        String[] properties = {};
        return properties;
    }

    public String toFIXMessage() {
        StringBuffer sb = new StringBuffer("");
        if (_NoBidDesc != null) sb.append(_NoBidDesc.toFIXMessage());
        if (_BidDescGroup != null) sb.append(_BidDescGroup.toFIXMessage());
        return sb.toString();
    }

    public String toFIXML(String ident) {
        StringBuffer sb = new StringBuffer("");
        sb.append(ident + "<BidDescList>\n");
        if (_NoBidDesc != null) sb.append(_NoBidDesc.toFIXML(ident + "\t") + "\n");
        if (_BidDescGroup != null) sb.append(_BidDescGroup.toFIXML(ident + "\t") + "\n");
        sb.append(ident + "</BidDescList>");
        return sb.toString();
    }
}
