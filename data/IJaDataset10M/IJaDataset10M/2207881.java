package de.tudresden.inf.rn.mobilis.android.xhunt.clientstub;

import org.xmlpull.v1.XmlPullParser;

public class TransferTicketResponse extends XMPPBean {

    public TransferTicketResponse() {
        this.setType(XMPPBean.TYPE_RESULT);
    }

    @Override
    public void fromXML(XmlPullParser parser) throws Exception {
    }

    public static final String CHILD_ELEMENT = "TransferTicketResponse";

    @Override
    public String getChildElement() {
        return CHILD_ELEMENT;
    }

    public static final String NAMESPACE = "mobilisxhunt:iq:transferticket";

    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public XMPPBean clone() {
        TransferTicketResponse clone = new TransferTicketResponse();
        clone.cloneBasicAttributes(clone);
        return clone;
    }

    @Override
    public String payloadToXML() {
        return "";
    }
}
