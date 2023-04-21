package org.itsnat.impl.core.listener.trans;

import java.util.HashMap;
import java.util.Map;
import org.itsnat.core.event.ParamTransport;
import org.itsnat.impl.core.ItsNatServletRequestImpl;
import org.itsnat.impl.core.browser.Browser;
import org.itsnat.impl.core.browser.BrowserOpera;
import org.itsnat.impl.core.domutil.ItsNatDOMUtilInternal;
import org.itsnat.impl.core.event.fromclient.ClientNormalEventImpl;
import org.itsnat.impl.core.request.RequestNormalEventImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author jmarranz
 */
public class NodeAllAttribTransportUtil extends ParamTransportUtil {

    public static final NodeAllAttribTransportUtil SINGLETON = new NodeAllAttribTransportUtil();

    /**
     * Creates a new instance of ParamTransportUtil
     */
    public NodeAllAttribTransportUtil() {
    }

    public String getCodeToSend(ParamTransport param) {
        return "    this.getUtil().transpAllAttrs(this);\n";
    }

    public void syncWithServer(ParamTransport param, RequestNormalEventImpl request, ClientNormalEventImpl event) {
        syncWithServer(request, event);
    }

    public static void syncWithServer(RequestNormalEventImpl request, ClientNormalEventImpl event) {
        Element elem = (Element) event.getCurrentTarget();
        ItsNatServletRequestImpl itsNatRequest = request.getItsNatServletRequest();
        int attrNum = Integer.parseInt(itsNatRequest.getParameterOrAttribute("itsnat_attr_num"));
        if (attrNum > 0) {
            Browser browser = request.getBrowser();
            boolean toLowerCase = (browser instanceof BrowserOpera) && request.getItsNatDocument().isHTML();
            Map remoteAttribs = new HashMap();
            for (int i = 0; i < attrNum; i++) {
                String name = itsNatRequest.getParameterOrAttribute("itsnat_attr_" + i);
                String value = itsNatRequest.getParameterOrAttribute(name);
                ItsNatDOMUtilInternal.setAttribute(elem, name, value);
                if (toLowerCase) name = name.toLowerCase();
                remoteAttribs.put(name, value);
            }
            if (elem.hasAttributes()) {
                NamedNodeMap attribs = elem.getAttributes();
                for (int i = 0; i < attribs.getLength(); i++) {
                    Attr attr = (Attr) attribs.item(i);
                    String name = attr.getName();
                    if (toLowerCase) name = name.toLowerCase();
                    if (!remoteAttribs.containsKey(name)) attribs.removeNamedItem(name);
                }
            }
        }
    }
}
