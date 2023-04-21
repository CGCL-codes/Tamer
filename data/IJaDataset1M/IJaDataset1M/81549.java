package org.opensih.ejb;

import javax.ejb.Stateless;
import org.opensih.ejb.webservices.DarCorrelativo;

@Stateless
public class Invocador implements InterfaceInvocador {

    public int invocarObtenerNumeroCorrelativo(String aFulano) {
        DarCorrelativo service = new DarCorrelativo();
        System.out.println("Antes de invocar el webservice");
        int Correlativo = service.getOpenSIH_0020_0020Prototipo_0020WebService_0020PublisherPort().receiveCorrelativo(aFulano);
        System.out.println("Luego del WebService, me dio el correlativo: " + Correlativo + " para el individuo: " + aFulano);
        return Correlativo;
    }
}
