package com.mci.comunes.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import com.common.configuration.ConfigurationApplication;
import com.common.configuration.Enviroment;
import com.common.exception.BusinessException;
import com.common.exception.InfraestructureException;
import com.common.exception.InternalApplicationException;
import com.common.to.ServiceRequestTO;
import com.common.to.ServiceResponseTO;
import com.common.to.ServiceTO;
import com.commons.deploy.delegate.ServiceDelegate;
import com.mci.comunes.dtos.EstadoCivilTO;
import com.mci.consolidacion.dtos.DireccionTO;
import com.mci.consolidacion.dtos.DisipuloTO;
import com.mci.consolidacion.dtos.GrupoHomogeneoTO;
import com.mci.consolidacion.dtos.PersonaTO;
import com.mci.consolidacion.dtos.TarjetaPresentacionTO;
import com.mci.consolidacion.dtos.TarjetaReporteTO;
import com.mci.consolidacion.dtos.TarjetaSeguimientoTO;
import com.mci.consolidacion.dtos.TarjetaTO;
import com.mci.consolidacion.dtos.TelefonoTO;
import com.mci.consolidacion.dtos.VisitaTO;

public class TestTotalComunesService extends TestCase {

    public TestTotalComunesService(String testMethod) {
        super(testMethod);
    }

    protected void setUp() throws Exception {
        String pathConfigurationFile = Enviroment.getEnviromentVariable("MCI_CLIENT_HOME") + "/config/config.xml";
        System.out.println(pathConfigurationFile);
        ConfigurationApplication configurationApplication = new ConfigurationApplication();
        boolean result = configurationApplication.startup(pathConfigurationFile);
        System.out.println("result:" + result);
        super.setUp();
    }

    public void testCrearEstadoCivilSolteros() {
        try {
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO("comunes.crearEstadoCivil");
            EstadoCivilTO estadoCivilTO = new EstadoCivilTO();
            estadoCivilTO.setNombre("SOLTERO(A)");
            serviceRequestTO.addParam(estadoCivilTO);
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            serviceDelegate.executeService(serviceRequestTO);
        } catch (InternalApplicationException e) {
            e.printStackTrace();
            System.out.println("exception :" + e.getMessage());
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (InfraestructureException e) {
            e.printStackTrace();
        }
    }

    public void testCrearEstadoCivilDivorciado() {
        try {
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO("comunes.crearEstadoCivil");
            EstadoCivilTO estadoCivilTO = new EstadoCivilTO();
            estadoCivilTO.setNombre("DIVORCIADO(A)");
            serviceRequestTO.addParam(estadoCivilTO);
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            serviceDelegate.executeService(serviceRequestTO);
        } catch (InternalApplicationException e) {
            e.printStackTrace();
            System.out.println("exception :" + e.getMessage());
        } catch (BusinessException e) {
            e.printStackTrace();
        } catch (InfraestructureException e) {
            e.printStackTrace();
        }
    }

    public void testListarEstadosCiviles() {
        try {
            ServiceRequestTO serviceRequestTO = new ServiceRequestTO("comunes.listarEstadosCiviles");
            ServiceDelegate serviceDelegate = new ServiceDelegate();
            List<EstadoCivilTO> estadosCiviles = (List<EstadoCivilTO>) serviceDelegate.executeService(serviceRequestTO);
            assertTrue(estadosCiviles.size() > 0);
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
