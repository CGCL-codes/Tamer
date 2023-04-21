package com.mci.consolidacion.services;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.AssertionFailedError;
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
import com.mci.consolidacion.business.Disipulo;
import com.mci.consolidacion.business.Tarjeta;
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

public class TestTotalConsolidacionService extends TestCase {
	public static DateFormat getDefaultDateFormat() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		dateFormat.setLenient(false);
		return dateFormat;
	}

	public TestTotalConsolidacionService(String testMethod) {
		super(testMethod);
	}

	// static DocumentosDelegate documentosDelegate;
	protected void setUp() throws Exception {
		String pathConfigurationFile = Enviroment
				.getEnviromentVariable("MCI_CLIENT_HOME")
				+ "/config/config.xml";
		System.out.println(pathConfigurationFile);

		ConfigurationApplication configurationApplication = new ConfigurationApplication();
		boolean result = configurationApplication
				.startup(pathConfigurationFile);
		System.out.println("result:" + result);

		super.setUp();
	}

	public static void main(String[] args) {
		// TestConsolidacionService.documentosDelegate=new DocumentosDelegate();
		// System.out.println("do1 :"+documentosDelegate);

		// junit.textui.TestRunner.run(TestConsolidacionService.class);
		junit.textui.TestRunner.run(TestTotalConsolidacionService.class);
		// new TestConsolidacionService().testCrearIdioma();
	}

	public void testCrearGrupoHomogeneoAdultos() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.crearGrupoHomogeneo");

			GrupoHomogeneoTO grupoHomogeneoTO = new GrupoHomogeneoTO();
			grupoHomogeneoTO.setNombre("ADULTOS");
			serviceRequestTO.addParam(grupoHomogeneoTO);
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			serviceDelegate.executeService(serviceRequestTO);

		} catch (InternalApplicationException e) {
			e.printStackTrace();
			System.out.println("exception :" + e.getMessage());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testCrearGrupoHomogeneoJovenes() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.crearGrupoHomogeneo");

			GrupoHomogeneoTO grupoHomogeneoTO = new GrupoHomogeneoTO();
			grupoHomogeneoTO.setNombre("JOVENES");
			serviceRequestTO.addParam(grupoHomogeneoTO);
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			serviceDelegate.executeService(serviceRequestTO);

		} catch (InternalApplicationException e) {
			e.printStackTrace();
			System.out.println("exception :" + e.getMessage());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testCrearGrupoHomogeneoNiños() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.crearGrupoHomogeneo");

			GrupoHomogeneoTO grupoHomogeneoTO = new GrupoHomogeneoTO();
			grupoHomogeneoTO.setNombre("NIÑOS");
			serviceRequestTO.addParam(grupoHomogeneoTO);
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			serviceDelegate.executeService(serviceRequestTO);

		} catch (InternalApplicationException e) {
			e.printStackTrace();
			System.out.println("exception :" + e.getMessage());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testCrearTarjeta() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.crearTarjeta");

			DireccionTO direccion = new DireccionTO();
			direccion.setCalle("Oriental y Rufino Marin");
			direccion.setBarrio("El Dorado");
			DireccionTO direccionTarjeta = new DireccionTO();
			direccionTarjeta.setCalle("Upiales y Paez");
			direccionTarjeta.setBarrio("El Guambra");

			TarjetaTO t = new TarjetaTO();
			t.setFecha(getDefaultDateFormat().parse("20/10/2006"));
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
			p.setCodigo("0201");
			p.setTelefono(telefonoTO);
			p.setCedula("1103255152");
			p.setNombre("Alex");
			p.setApellido("Pozo");
			p.setOcupacion("Redes");
			p.setEdad(new Integer(15));
			p.setDireccion(direccion);
			t.setPersona(p);
			Integer liderInicialId = new Integer(1);
			Integer consolidadorId = new Integer(1);
			Integer encargadoId = new Integer(1);
			Integer grupoHomogeneoId = 2;
			Integer estadoCivilId = 1;
			serviceRequestTO.addParam(t);
			serviceRequestTO.addParam(consolidadorId);
			serviceRequestTO.addParam(encargadoId);
			serviceRequestTO.addParam(liderInicialId);
			serviceRequestTO.addParam(grupoHomogeneoId);
			// agregamos el estado civil casado
			serviceRequestTO.addParam(estadoCivilId);
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			ServiceResponseTO serviceResponseTO = (ServiceResponseTO) serviceDelegate
					.executeService(serviceRequestTO);

		} catch (InternalApplicationException e) {
			e.printStackTrace();
			System.out.println("exception :" + e.getMessage());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testCrearTarjetaPastora() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.crearTarjeta");

			DireccionTO direccion = new DireccionTO();
			direccion.setCalle("Oriental y Rufino Marin");
			direccion.setBarrio("El Dorado");
			DireccionTO direccionTarjeta = new DireccionTO();
			direccionTarjeta.setCalle("Upiales y Paez");
			direccionTarjeta.setBarrio("El Guambra");

			TarjetaTO t = new TarjetaTO();
			t.setFecha(getDefaultDateFormat().parse("20/10/2006"));
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
			p.setCodigo("01");
			p.setTelefono(telefonoTO);
			p.setCedula("1103255152");
			p.setNombre("Enma");
			p.setApellido("Diaz");
			p.setOcupacion("Pastora");
			p.setEdad(new Integer(45));

			p.setDireccion(direccion);
			t.setPersona(p);
			Integer consolidadorId = new Integer(1);
			Integer encargadoId = new Integer(1);
			Integer grupoHomogeneoId = 1;
			// agregamos el estado civil casado
			Integer estadoCivilId = 1;

			serviceRequestTO.addParam(t);
			serviceRequestTO.addParam(consolidadorId);
			serviceRequestTO.addParam(encargadoId);
			serviceRequestTO.addParam(null, Integer.class);
			serviceRequestTO.addParam(grupoHomogeneoId);
			serviceRequestTO.addParam(estadoCivilId);

			ServiceDelegate serviceDelegate = new ServiceDelegate();
			ServiceResponseTO serviceResponseTO = (ServiceResponseTO) serviceDelegate
					.executeService(serviceRequestTO);

		} catch (InternalApplicationException e) {
			e.printStackTrace();
			System.out.println("exception :" + e.getMessage());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// obtenerDisipulo
	public void testObtenerDisipulo() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.obtenerDisipulo");
			serviceRequestTO.addParam(new Integer(2));
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			PersonaTO persona = (PersonaTO) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(persona);
			assertNotNull(persona.getNombre());
			assertNotNull(persona.getApellido());
			assertNotNull(persona.getId());

			assertEquals("Alex", persona.getNombre());
			assertEquals("Pozo", persona.getApellido());
			assertEquals(new Integer(2), persona.getId());
			// assertEquals("JOVENES", persona.get);
			assertEquals("CASADO(A)", persona.getEstadoCivilTO().getNombre());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// buscarLiderPorNombre
	public void testBuscarLiderPorNombre() {
		try {
			List contents = new ArrayList();
			contents.add("a");
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(contents,
					new ServiceTO("consolidacion.buscarLiderPorNombre"));
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			ServiceResponseTO serviceResponseTO = (ServiceResponseTO) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(serviceResponseTO);
			List listaConsolidadores = serviceResponseTO.getContents();
			Iterator it = listaConsolidadores.iterator();
			while (it.hasNext()) {
				PersonaTO personaTO = (PersonaTO) it.next();
				assertNotNull(personaTO.getNombre());
				assertNotNull(personaTO.getCodigo());
				System.out.println("nombre:" + personaTO.getNombre());

			}

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// crearConsolidador
	public void testCrearConsolidador() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.crearConsolidador");
			serviceRequestTO.addParam(new Integer(2));
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			serviceDelegate.executeService(serviceRequestTO);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// buscarConsolidadorPorNombre
	public void testBuscarConsolidadorPorNombre() {
		try {
			List contents = new ArrayList();
			contents.add("m");
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(contents,
					new ServiceTO("consolidacion.buscarConsolidadorPorNombre"));
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			ServiceResponseTO serviceResponseTO = (ServiceResponseTO) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(serviceResponseTO);
			List listaConsolidadores = serviceResponseTO.getContents();
			Iterator it = listaConsolidadores.iterator();
			while (it.hasNext()) {
				PersonaTO personaTO = (PersonaTO) it.next();
				assertNotNull(personaTO.getNombre());
				System.out.println("nombre:" + personaTO.getNombre());

			}

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// crearConsolidador
	public void testEliminarConsolidador() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.eliminarConsolidador");
			serviceRequestTO.addParam(new Integer(2));
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			serviceDelegate.executeService(serviceRequestTO);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// crearConsolidador
	public void testCrearConsolidador1() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.crearConsolidador");
			serviceRequestTO.addParam(new Integer(2));
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			serviceDelegate.executeService(serviceRequestTO);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// crearConsolidador
	public void testBuscarDisipuloPorNombre() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.buscarDisipuloPorNombre");
			serviceRequestTO.addParam("A");
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			ServiceResponseTO serviceResponseTO = (ServiceResponseTO) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(serviceResponseTO);
			List listDisipulos = serviceResponseTO.getContents();
			assertTrue(listDisipulos.size() > 0);
			Iterator disipulosIterator = listDisipulos.iterator();
			while (disipulosIterator.hasNext()) {
				PersonaTO disipulo = (PersonaTO) disipulosIterator.next();
				System.out.println("nombre:" + disipulo.getNombre());
				assertEquals("0201", disipulo.getCodigo());
				assertEquals("Alex", disipulo.getNombre());
				assertEquals("Pozo", disipulo.getApellido());

			}
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//
	// buscarConsolidadorPorNombre
	public void testBuscarTarjetasPorLider() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.buscarTarjetasPorLider");
			serviceRequestTO.addParam(new Integer(1));
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			List tarjetas = (List) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(tarjetas);
			assertTrue(tarjetas.size() == 2);

			Tarjeta tarjetaReporteTO = (Tarjeta) tarjetas.get(1);
			assertNotNull(tarjetaReporteTO.getPersona());
			assertNotNull(tarjetaReporteTO.getPersona().getNombre());
			assertNotNull(tarjetaReporteTO.getPersona().getApellido());
			assertNotNull(tarjetaReporteTO.getFecha());
			assertEquals("Alex", tarjetaReporteTO.getPersona().getNombre());
			assertEquals("Pozo", tarjetaReporteTO.getPersona().getApellido());

			System.out.println("Fecha:" + tarjetaReporteTO.getFecha());
			System.out.println("apellido:"
					+ tarjetaReporteTO.getPersona().getApellido());
			System.out.println("nombre:"
					+ tarjetaReporteTO.getPersona().getNombre());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// buscarTarjetasNombrePersona
	public void testBuscarTarjetasNombrePersona() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.buscarTarjetasNombrePersona");
			serviceRequestTO.addParam("a");
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			List tarjetas = (List) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(tarjetas);
			assertTrue(tarjetas.size() > 0);
			Iterator it = tarjetas.iterator();
			while (it.hasNext()) {
				TarjetaReporteTO tarjetaReporteTO = (TarjetaReporteTO) it
						.next();
				assertNotNull(tarjetaReporteTO.getPersona());
				assertEquals("Alex", tarjetaReporteTO.getPersona().getNombre());
				assertEquals("Pozo", tarjetaReporteTO.getPersona()
						.getApellido());

				System.out.println("nombre:"
						+ tarjetaReporteTO.getPersona().getNombre());

			}

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// buscarTarjetasNombrePersona
	public void testBuscarTarjeta() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.buscarTarjeta");
			serviceRequestTO.addParam(new Integer(1));
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			TarjetaPresentacionTO tarjeta = (TarjetaPresentacionTO) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(tarjeta);
			assertNotNull(tarjeta.getId());
			assertNotNull(tarjeta.getFecha());
			assertNotNull(tarjeta.getPersona());
			assertEquals("Alex", tarjeta.getPersona().getNombre());
			assertEquals("Pozo", tarjeta.getPersona().getApellido());
			assertEquals("Lenin", tarjeta.getPersona().getLiderPersona()
					.getNombre());
			assertEquals("Diaz", tarjeta.getPersona().getLiderPersona()
					.getApellido());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// buscarTarjetasPorFechas
	public void testBuscarTarjetasSeguimiento() {
		try {
			long ti = System.currentTimeMillis();
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.buscarTarjetasSeguimiento");

			serviceRequestTO.addParam(getDefaultDateFormat()
					.parse("20/10/2006"));
			serviceRequestTO.addParam(new Integer(1));
			serviceRequestTO.addParam(null, Integer.class);

			ServiceDelegate serviceDelegate = new ServiceDelegate();
			List tarjetas = (List) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(tarjetas);
			assertTrue("numero de tarjetas:", tarjetas.size() == 1);
			long tf = System.currentTimeMillis();
			System.out.println("tiempo de demora:" + (tf - ti));
			tiempoTotal += (tf - ti);
			veces++;
			System.out.println("porcentaje:" + (tf - ti) / veces);
			// serviceDelegate.clean();
		} catch (AssertionFailedError e) {
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();
			fail();
		}

	}

	// buscarTarjetasPorFechas
	public void testBuscarTarjetasPorFechaYLider() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.buscarTarjetas");

			serviceRequestTO.addParam(getDefaultDateFormat()
					.parse("20/10/2006"));
			serviceRequestTO.addParam(new Integer(2));
			serviceRequestTO.addParam(new Integer(1));

			ServiceDelegate serviceDelegate = new ServiceDelegate();
			List tarjetas = (List) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(tarjetas);
			assertEquals("numero de tarjetas:", 1, tarjetas.size());

		} catch (AssertionFailedError e) {
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();
			fail();
		}

	}

	// buscarTarjetasPorFechas
	public void testBuscarTarjetasPorFechaYLider1() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.buscarTarjetas");

			serviceRequestTO.addParam(getDefaultDateFormat()
					.parse("20/10/2006"));
			serviceRequestTO.addParam(new Integer(1));
			serviceRequestTO.addParam(new Integer(1));

			ServiceDelegate serviceDelegate = new ServiceDelegate();
			List tarjetas = (List) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull(tarjetas);
			assertEquals("numero de tarjetas:", 1, tarjetas.size());

		} catch (AssertionFailedError e) {
			throw e;
		} catch (Throwable e) {
			e.printStackTrace();
			fail();
		}

	}

	// buscarTarjetasPorFechas
	public void testModificarTarjetasConsolidacion() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.modificarTarjetasConsolidacion");
			List<TarjetaSeguimientoTO> tarjetasModificadas = new ArrayList<TarjetaSeguimientoTO>();

			TarjetaSeguimientoTO tarjetaSeguimiento1 = new TarjetaSeguimientoTO();
			tarjetaSeguimiento1.setId(1);
			tarjetaSeguimiento1.setModificado(true);
			VisitaTO visitaTO1 = new VisitaTO();
			visitaTO1.setFonoVisita1(true);
			visitaTO1.setFonoVisita2(true);
			visitaTO1.setVerificacion("Si pero no");
			visitaTO1.setVisita1(true);
			visitaTO1.setVisita2(true);
			visitaTO1.setId(1);

			tarjetaSeguimiento1.setVisita(visitaTO1);
			tarjetasModificadas.add(tarjetaSeguimiento1);

			TarjetaSeguimientoTO tarjetaSeguimiento2 = new TarjetaSeguimientoTO();
			tarjetaSeguimiento2.setId(2);
			tarjetaSeguimiento2.setModificado(true);
			VisitaTO visitaTO2 = new VisitaTO();
			visitaTO2.setFonoVisita1(true);
			visitaTO2.setFonoVisita2(false);
			visitaTO2.setVerificacion("Ya ya");
			visitaTO2.setVisita1(true);
			visitaTO2.setVisita2(false);
			visitaTO2.setId(1);

			tarjetaSeguimiento2.setVisita(visitaTO2);
			tarjetasModificadas.add(tarjetaSeguimiento2);

			serviceRequestTO.addParam(tarjetasModificadas, List.class);

			ServiceDelegate serviceDelegate = new ServiceDelegate();
			serviceDelegate.executeService(serviceRequestTO);

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testListarGruposHomogeneos() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"comunes.listarGruposHomogeneos");
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			List<GrupoHomogeneoTO> estadosCiviles = (List<GrupoHomogeneoTO>) serviceDelegate
					.executeService(serviceRequestTO);
			assertTrue(estadosCiviles.size() > 0);

		} catch (InternalApplicationException e) {
			e.printStackTrace();
			System.out.println("exception :" + e.getMessage());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// crearLider
	public void testCrearLider() {
		try {
			Integer idLider = new Integer(2);
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"celulas.crearLider");
			serviceRequestTO.addParam(idLider);
			ServiceDelegate serviceDelegate = new ServiceDelegate();
			serviceDelegate.executeService(serviceRequestTO);

			ServiceRequestTO serviceRequestTO1 = new ServiceRequestTO(
					"celulas.obtenerLider");
			serviceRequestTO1.addParam(idLider);
			ServiceDelegate serviceDelegate1 = new ServiceDelegate();
			PersonaTO persona = (PersonaTO) serviceDelegate1
					.executeService(serviceRequestTO1);
			assertNotNull(persona);
			assertNotNull(persona.getNombre());
			assertNotNull(persona.getApellido());
			assertNotNull(persona.getId());

			assertEquals("Alex", persona.getNombre());
			assertEquals("Pozo", persona.getApellido());
			assertEquals(idLider, persona.getId());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void testSembrarTarjetaAsiMismo() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.sembrarTarjeta");
			serviceRequestTO.addParam(new Integer(1));
			serviceRequestTO.addParam(new Integer(2));
			ServiceDelegate serviceDelegate = new ServiceDelegate();

			serviceDelegate.executeService(serviceRequestTO);

		} catch (BusinessException e) {
			assertTrue("genera excepcion", true);

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("excepcion en metodo");
		}

	}

	public void testSembrarTarjeta() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.sembrarTarjeta");
			serviceRequestTO.addParam(new Integer(2));
			serviceRequestTO.addParam(new Integer(2));
			ServiceDelegate serviceDelegate = new ServiceDelegate();

			serviceDelegate.executeService(serviceRequestTO);

		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail("excepcion en metodo");
		}

	}

	// buscarTarjetaPorPersona
	public void testBuscarTarjetaPorPersonaYActualizar() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.buscarTarjetaPorPersona");
			serviceRequestTO.addParam(new Integer(2));
			ServiceDelegate serviceDelegate = new ServiceDelegate();

			Tarjeta tarjeta = (Tarjeta) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull("tarjeta", tarjeta);
			assertNotNull(tarjeta.getPersona().getNombre());
			assertNotNull(tarjeta.getDireccion().getBarrio());

			tarjeta.setNecesidad("Esta pensandolo");
			tarjeta.getDireccion().setCalle("9 de Octubre");

			// utilizacion para metodo para actualizar
			serviceRequestTO = new ServiceRequestTO(
					"consolidacion.modificarTarjeta");
			serviceRequestTO.addParam(tarjeta);

			serviceDelegate.executeService(serviceRequestTO);

		} catch (InternalApplicationException e) {
			e.printStackTrace();
			System.out.println("exception :" + e.getMessage());

		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InfraestructureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	// consolidacion.buscarPersonaPorCodigo
	public void testBuscarPersonaPorCodigo() {
		try {
			ServiceRequestTO serviceRequestTO = new ServiceRequestTO(
					"consolidacion.buscarPersonaPorCodigo");
			serviceRequestTO.addParam("0201");
			ServiceDelegate serviceDelegate = new ServiceDelegate();

			Disipulo disipulo = (Disipulo) serviceDelegate
					.executeService(serviceRequestTO);
			assertNotNull("disipulo", disipulo);
			assertNotNull("Nombre", disipulo.getNombre());

		} catch (Throwable e) {
			e.printStackTrace();
			fail("Excepcion");
		}

	}

	static long tiempoTotal = 0;

	static long veces = 0;
}
