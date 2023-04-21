package es.caib.pagos.services;

import java.rmi.RemoteException;
import javax.xml.rpc.ServiceException;
import es.caib.pagos.services.wsdl.DatosRespuesta046;
import es.caib.pagos.services.wsdl.Service_TasaLocator;
import es.caib.pagos.services.wsdl.Service_TasaSoap;
import es.caib.pagos.services.wsdl.Service_TasaSoapStub;
import es.caib.pagos.services.wsdl.UsuariosWebServices;
import es.caib.pagos.util.Constants;

public class InicioPagoService {

    private final String url;

    public InicioPagoService(final String url) {
        this.url = url;
    }

    public DatosRespuesta046 execute(final String value, final UsuariosWebServices usuario) throws ServiceException, RemoteException {
        final Service_TasaLocator sl = new Service_TasaLocator();
        sl.setEndpointAddress(Constants.SERVICE_SOAP, url);
        final Service_TasaSoap port = sl.getService_TasaSoap();
        ((Service_TasaSoapStub) port).setHeader(Constants.NAMESPACE_ATIB, Constants.PARTNAME_USUARIOS_WEBSERVICES, usuario);
        return port.inserta046(value);
    }
}
