package SeviceLocatorNegocio;

/**
 *
 * @author James
 */
public class LocalizadorServicios {

    public Double consultarDeuda(java.lang.Integer idCliente) {
        SeviceLocatorNegocio.ImpuestosWSService service = new SeviceLocatorNegocio.ImpuestosWSService();
        SeviceLocatorNegocio.ImpuestosWS port = service.getImpuestosWSPort();
        return port.consultarDeuda(idCliente);
    }
}
