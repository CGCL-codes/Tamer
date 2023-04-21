package ddss.web;

import org.apache.http.ParseException;
import org.ksoap2.serialization.SoapObject;
import ddss.common.webservice.WSAccessDefinitions;
import ddss.loader.Container;
import ddss.loader.ContainerType;

public class GetActiveContainersRequest extends WSRequest {

    private static final String METHOD_NAME = "GetActiveBreedingSites";

    private SoapObject m_Parameters;

    public GetActiveContainersRequest() {
        super();
        Initialize();
    }

    public GetActiveContainersRequest(int arg0) {
        super(arg0);
        Initialize();
    }

    private void Initialize() {
        setMethodName(METHOD_NAME);
        m_Parameters = new SoapObject(WSAccessDefinitions.getNamespace(), METHOD_NAME);
        setOutputSoapObject(m_Parameters);
        addMapping(WSAccessDefinitions.getNamespace(), "Container", Container.class);
    }

    @Override
    protected Object BuildResponse(Object _soapResponse) {
        if (_soapResponse == null) throw new NullPointerException("No respose from server.");
        if (_soapResponse instanceof SoapObject) {
            SoapObject resultSOAP = (SoapObject) _soapResponse;
            Container[] containers = new Container[resultSOAP.getPropertyCount()];
            for (int i = 0; i < containers.length; i++) {
                SoapObject containerSOAP = (SoapObject) resultSOAP.getProperty(i);
                int ID = Integer.parseInt(containerSOAP.getProperty("ID").toString());
                String Description = containerSOAP.getProperty("Description").toString();
                String Name = containerSOAP.getProperty("Name").toString();
                boolean bActive = Boolean.parseBoolean(containerSOAP.getProperty("bactive").toString());
                int bExterior = Integer.parseInt(containerSOAP.getProperty("bexterior").toString());
                Container container = new Container();
                container.ID = ID;
                container.Name = Name;
                container.Description = Description;
                container.bactive = bActive;
                container.bexterior = ContainerType.fromInt(bExterior);
                containers[i] = container;
            }
            return containers;
        }
        throw new ParseException("Unexpected response from server.");
    }
}
