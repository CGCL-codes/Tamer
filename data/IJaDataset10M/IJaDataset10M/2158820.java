package de.tud.android.helpers;

import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import de.tudresden.inf.rn.mobilis.mxa.parcelable.XMPPIQ;
import de.tudresden.inf.rn.mobilis.xmpp.beans.ConditionInfo;
import de.tudresden.inf.rn.mobilis.xmpp.beans.XMPPBean;
import de.tudresden.inf.rn.mobilis.xmpp.beans.media.RepositoryItemInfo;

/**
 * 
 * @author Benjamin S�llner
 *
 */
public class Parceller {

    private static Parceller instance;

    private Map<String, Map<String, XMPPBean>> prototypes = Collections.synchronizedMap(new HashMap<String, Map<String, XMPPBean>>());

    public static Parceller getInstance() {
        if (Parceller.instance == null) Parceller.instance = new Parceller();
        return Parceller.instance;
    }

    private Parceller() {
    }

    public void registerXMPPBean(XMPPBean prototype) {
        String namespace = prototype.getNamespace();
        String childElement = prototype.getChildElement();
        synchronized (this.prototypes) {
            if (!this.prototypes.keySet().contains(namespace)) this.prototypes.put(namespace, Collections.synchronizedMap(new HashMap<String, XMPPBean>()));
            this.prototypes.get(namespace).put(childElement, prototype);
        }
    }

    public void unregisterXMPPBean(XMPPBean prototype) {
        String namespace = prototype.getNamespace();
        String childElement = prototype.getChildElement();
        synchronized (this.prototypes) {
            if (this.prototypes.containsKey(namespace)) {
                this.prototypes.get(namespace).remove(childElement);
                if (this.prototypes.get(namespace).size() > 0) this.prototypes.remove(namespace);
            }
        }
    }

    public XMPPIQ convertXMPPBeanToIQ(XMPPBean bean, boolean mergePayload) {
        int type = XMPPIQ.TYPE_GET;
        switch(bean.getType()) {
            case XMPPBean.TYPE_GET:
                type = XMPPIQ.TYPE_GET;
                break;
            case XMPPBean.TYPE_SET:
                type = XMPPIQ.TYPE_SET;
                break;
            case XMPPBean.TYPE_RESULT:
                type = XMPPIQ.TYPE_RESULT;
                break;
            case XMPPBean.TYPE_ERROR:
                type = XMPPIQ.TYPE_ERROR;
                break;
        }
        XMPPIQ iq;
        if (mergePayload) iq = new XMPPIQ(bean.getFrom(), bean.getTo(), type, null, null, bean.toXML()); else iq = new XMPPIQ(bean.getFrom(), bean.getTo(), type, bean.getChildElement(), bean.getNamespace(), bean.payloadToXML());
        iq.packetID = bean.getId();
        return iq;
    }

    public XMPPBean convertXMPPIQToBean(XMPPIQ iq) {
        final Map<String, Map<String, XMPPBean>> prototypes = this.prototypes;
        try {
            String childElement = iq.element;
            String namespace = iq.namespace;
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new StringReader(iq.payload));
            XMPPBean bean = null;
            synchronized (prototypes) {
                if (namespace != null && prototypes.containsKey(namespace) && prototypes.get(namespace).containsKey(childElement)) {
                    bean = prototypes.get(namespace).get(childElement).clone();
                    bean.fromXML(parser);
                    bean.setId(iq.packetID);
                    bean.setFrom(iq.from);
                    bean.setTo(iq.to);
                    switch(iq.type) {
                        case XMPPIQ.TYPE_GET:
                            bean.setType(XMPPBean.TYPE_GET);
                            break;
                        case XMPPIQ.TYPE_SET:
                            bean.setType(XMPPBean.TYPE_SET);
                            break;
                        case XMPPIQ.TYPE_RESULT:
                            bean.setType(XMPPBean.TYPE_RESULT);
                            break;
                        case XMPPIQ.TYPE_ERROR:
                            bean.setType(XMPPBean.TYPE_ERROR);
                            break;
                    }
                    return bean;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
