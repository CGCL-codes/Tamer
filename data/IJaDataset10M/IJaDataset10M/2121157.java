package oss.net.pstream.sax;

import org.xml.sax.Attributes;
import oss.net.pstream.PortletStreamParameters;
import oss.net.pstream.PortletStreamParametersMap;

/**
 * Handles the host element in the configuration xml.
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */
public class HostElement extends ElementHandler {

    public HostElement(boolean Debug) {
        super(Debug);
    }

    public PortletStreamParameters handleElementStart(PortletStreamParametersMap PM, String PortletName, Attributes Attrs) throws org.xml.sax.SAXException {
        PortletStreamParameters Params = PM.getParamsForResource(PortletName);
        debug(getName(), "PortletName - " + PortletName);
        debug(getName(), Attrs);
        String temp;
        String protocol = ((temp = Attrs.getValue(protocol_attr)) == null) ? "http" : temp;
        String name = Attrs.getValue(name_attr);
        if (name == null) AttributeException(name_attr, name);
        String port = ((temp = Attrs.getValue(port_attr)) == null) ? "80" : temp;
        String path = ((temp = Attrs.getValue(path_attr)) == null) ? "/" : temp;
        Params.setProtocol(protocol);
        Params.setHost(name);
        Params.setPort(Integer.parseInt(port));
        Params.setPath(path);
        return Params;
    }

    public PortletStreamParameters handleElementEnd(StringBuffer Content, PortletStreamParametersMap PM, String PortletName) {
        return PM.getParamsForResource(PortletName);
    }

    public String getName() {
        return super.host_element;
    }
}
