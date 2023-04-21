package com.mci.consolidacion.richclient;

import java.util.ArrayList;
import java.util.List;
import com.common.configuration.ConfigurationApplication;
import com.common.exception.BusinessException;
import com.common.exception.InfraestructureException;
import com.common.exception.InternalApplicationException;
import com.common.to.ServiceRequestTO;
import com.common.to.ServiceResponseTO;
import com.common.to.ServiceTO;
import com.commons.deploy.delegate.ServiceDelegate;
import com.mci.consolidacion.dtos.DireccionTO;
import com.mci.consolidacion.dtos.DisipuloTO;
import com.mci.consolidacion.dtos.TarjetaTO;
import com.mci.consolidacion.dtos.TelefonoTO;

public class TestClient {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        try {
            ConfigurationApplication configurationApplication = new ConfigurationApplication();
            boolean result = configurationApplication.startup("G:/workspaces/workspacemci/mcirichclient/config/config.xml");
            DireccionTO direccion = new DireccionTO();
            direccion.setCalle("Oriental y Rufino Marin");
            direccion.setBarrio("El Dorado");
            DireccionTO direccionTarjeta = new DireccionTO();
            direccionTarjeta.setCalle("Upiales y Paez");
            direccionTarjeta.setBarrio("El Guambra");
            TarjetaTO t = new TarjetaTO();
            t.setFecha(new java.util.Date());
            t.setAsisteCelula(true);
            t.setNecesidad("Necesito encontrar a Dios");
            t.setDireccion(direccionTarjeta);
            TelefonoTO telefonoTO = new TelefonoTO();
            telefonoTO.setTelefonoCasa("098336403");
            telefonoTO.setTelefonoCelular("098336403");
            telefonoTO.setTelefonoOficina("098336403");
            telefonoTO.setTelefonoOtro("098336403");
            DisipuloTO p = new DisipuloTO();
            p.setCedula("1103255152");
            p.setTelefono(telefonoTO);
            p.setCedula("1103255152");
            p.setNombre("Edison");
            p.setApellido("Cuenca");
            p.setOcupacion("Redes");
            p.setEdad(new Integer(15));
            p.setDireccion(direccion);
            t.setPersona(p);
            Integer liderId = new Integer(1);
            Integer consolidadorId = new Integer(1);
            List contents = new ArrayList();
            contents.add(t);
            contents.add(consolidadorId);
            contents.add(liderId);
            contents.add(1);
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO(contents, new ServiceTO("consolidacion.crearTarjeta"));
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            ServiceResponseTO serviceResponseTO = (ServiceResponseTO) serviceDelegate.executeService(serviceRequestTO);
        } catch (InternalApplicationException e) {
            e.printStackTrace();
            System.out.println("exception :" + e.getMessage());
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (InfraestructureException e) {
            e.printStackTrace();
        }
    }
}
