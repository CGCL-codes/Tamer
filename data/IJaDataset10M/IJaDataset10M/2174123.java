package insertPointWS.ws;

import myflit.Positions;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.xpath.XPath;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.AbstractJDomPayloadEndpoint;

public class InsertPointEndpoint extends AbstractJDomPayloadEndpoint implements InitializingBean {

    private Namespace namespace;

    private XPath latXPath;

    private XPath lonXPath;

    private XPath fleetidXPath;

    @Autowired
    private Positions positions;

    private float lat;

    private float lon;

    private long fleetid;

    protected Element invokeInternal(Element element) throws Exception {
        String response = "OKEY";
        extractPositionFromRequest(element);
        if (!CheckErrorCondition()) {
            boolean ok = positions.insertPosition(lat, lon, fleetid);
            if (!ok) response = "ERROR";
        } else {
            response = "ERROR";
        }
        return createResponse(response);
    }

    private void extractPositionFromRequest(Element element) throws JDOMException {
        this.lat = Float.parseFloat(latXPath.valueOf(element).length() == 0 ? "999" : latXPath.valueOf(element));
        this.lon = Float.parseFloat(lonXPath.valueOf(element).length() == 0 ? "999" : lonXPath.valueOf(element));
        this.fleetid = Long.parseLong(fleetidXPath.valueOf(element).length() == 0 ? "999" : fleetidXPath.valueOf(element));
    }

    public void afterPropertiesSet() throws Exception {
        namespace = Namespace.getNamespace("ip", "http://myflit.com/ip");
        latXPath = XPath.newInstance("//ip:Lat");
        latXPath.addNamespace(namespace);
        lonXPath = XPath.newInstance("//ip:Lon");
        lonXPath.addNamespace(namespace);
        fleetidXPath = XPath.newInstance("//ip:FleetId");
        fleetidXPath.addNamespace(namespace);
    }

    private Element createResponse(String response) {
        Element responseElement = new Element("InsertPointResponse", namespace);
        responseElement.addContent(new Element("Response", namespace).setText(response));
        return responseElement;
    }

    private boolean CheckErrorCondition() {
        boolean error = false;
        if (this.lat > 90 || this.lat < -90 || this.lon > 180 || this.lon < -180) error = true;
        return error;
    }
}
