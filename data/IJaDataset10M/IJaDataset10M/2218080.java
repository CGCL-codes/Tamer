package com.mindbox.viajes.test;

import java.util.Calendar;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import com.mindbox.viajes.dto.Ciudad;
import com.mindbox.viajes.dto.Evento;
import com.mindbox.viajes.dto.Pais;
import com.mindbox.viajes.dto.TipoEvento;
import com.mindbox.viajes.exception.ExceptionCampo;
import com.mindbox.viajes.exception.ExceptionLogica;
import com.mindbox.viajes.logica.ControladorEvento;

/**
 * Test para el Controlador de Eventos MindBox 2009
 * 
 * @author Julian
 * 
 */
public class ControladorEventoTest1 {

    private ControladorEvento controlador;

    /**
	 * Inicializar Controlador
	 * 
	 * @throws java.lang.Exception
	 * @author Julian
	 */
    @Before
    public void setUp() throws Exception {
        this.controlador = new ControladorEvento();
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba del evento nulo.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento() throws ExceptionCampo, ExceptionLogica {
        this.controlador.guardarEvento(null);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba del nombre nulo.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento2() throws ExceptionCampo, ExceptionLogica {
        this.controlador.guardarEvento(new Evento());
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba de la ciudad nula.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento3() throws ExceptionCampo, ExceptionLogica {
        Evento ev = new Evento();
        ev.setNombre("nombre");
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba del pais nulo.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento4() throws ExceptionCampo, ExceptionLogica {
        Evento ev = new Evento();
        ev.setNombre("nombre");
        ev.setCiudad(new Ciudad());
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba de la descripcion nula.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento5() throws ExceptionCampo, ExceptionLogica {
        Evento ev = new Evento();
        ev.setNombre("nombre");
        Ciudad ciudad = new Ciudad();
        ciudad.setPais(new Pais());
        ev.setCiudad(ciudad);
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba del tipo de evento nulo.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento6() throws ExceptionCampo, ExceptionLogica {
        Evento ev = new Evento();
        ev.setNombre("nombre");
        Ciudad ciudad = new Ciudad();
        ciudad.setPais(new Pais());
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba de la fecha/hora inicio nula.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento7() throws ExceptionCampo, ExceptionLogica {
        Evento ev = new Evento();
        ev.setNombre("nombre");
        Ciudad ciudad = new Ciudad();
        ciudad.setPais(new Pais());
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        ev.setTipoEvento(new TipoEvento());
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba de la fecha/hora fin nula.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento8() throws ExceptionCampo, ExceptionLogica {
        Evento ev = new Evento();
        ev.setNombre("nombre");
        Ciudad ciudad = new Ciudad();
        ciudad.setPais(new Pais());
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        ev.setTipoEvento(new TipoEvento());
        ev.setFechaHoraInicio(Calendar.getInstance().getTime());
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba de la fecha/hora inicio posterior a la fecha/hora fin.
	 */
    @Test(expected = ExceptionLogica.class)
    public void testGuardarEvento9() throws ExceptionCampo, ExceptionLogica {
        Evento ev = new Evento();
        ev.setNombre("nombre");
        Ciudad ciudad = new Ciudad();
        ciudad.setPais(new Pais());
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        ev.setTipoEvento(new TipoEvento());
        ev.setFechaHoraFin(Calendar.getInstance().getTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        ev.setFechaHoraInicio(Calendar.getInstance().getTime());
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba de los IDs de las Entidades Ciudad y TipoEvento, no son numero
	 */
    @Test(expected = ExceptionLogica.class)
    public void testGuardarEvento10() throws ExceptionCampo, ExceptionLogica {
        Evento ev = new Evento();
        ev.setNombre("nombre");
        Ciudad ciudad = new Ciudad();
        ciudad.setPais(new Pais());
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        ev.setTipoEvento(new TipoEvento());
        ev.setFechaHoraInicio(Calendar.getInstance().getTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        ev.setFechaHoraFin(Calendar.getInstance().getTime());
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba de datos truncados, los cuales no son guardados en la base de
	 * datos.
	 */
    @Test(expected = ExceptionLogica.class)
    public void testGuardarEvento11() throws ExceptionCampo, ExceptionLogica, Exception {
        Evento ev = new Evento();
        ev.setNombre("1234567890123456789012345678901234567890");
        Ciudad ciudad = new Ciudad("1", "Ciu", null, "");
        ciudad.setPais(new Pais("1", "Comoros"));
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        ev.setTipoEvento(new TipoEvento("1", "Nom", ""));
        ev.setFechaHoraInicio(Calendar.getInstance().getTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        ev.setFechaHoraFin(Calendar.getInstance().getTime());
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba en la cual se espera violacion de llave foranea.
	 */
    @Test(expected = ExceptionLogica.class)
    public void testGuardarEvento12() throws ExceptionCampo, ExceptionLogica, Exception {
        Evento ev = new Evento();
        ev.setNombre("123456789012345678901234567890");
        Ciudad ciudad = new Ciudad("1", "Ciu", null, "");
        ciudad.setPais(new Pais("1", "Comoros"));
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        ev.setTipoEvento(new TipoEvento("1", "Nom", ""));
        ev.setFechaHoraInicio(Calendar.getInstance().getTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        ev.setFechaHoraFin(Calendar.getInstance().getTime());
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba en la cual se espera violacion de llave foranea.
	 */
    @Test(expected = Exception.class)
    public void testGuardarEvento13() throws ExceptionCampo, ExceptionLogica, Exception {
        Evento ev = new Evento();
        ev.setNombre("123456789012345678901234567890");
        Ciudad ciudad = new Ciudad("1", "Ciu", null, "");
        ciudad.setPais(new Pais("1", "Comoros"));
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        ev.setTipoEvento(new TipoEvento("1", "Nom", ""));
        ev.setFechaHoraInicio(Calendar.getInstance().getTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        ev.setFechaHoraFin(Calendar.getInstance().getTime());
        ev.setDireccion("dire");
        ev.setCoordenadas("coor");
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba en la cual se espera violacion de llave foranea.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento14() throws ExceptionCampo, ExceptionLogica, Exception {
        Evento ev = new Evento();
        ev.setNombre("123456789012345678901234567890");
        Ciudad ciudad = new Ciudad("1", "Ciu", null, "");
        ciudad.setPais(new Pais("1", "Comoros"));
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        ev.setTipoEvento(new TipoEvento("1", "Nom", ""));
        ev.setFechaHoraInicio(Calendar.getInstance().getTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        ev.setFechaHoraFin(Calendar.getInstance().getTime());
        ev.setDireccion("dire");
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba en la cual se espera violacion de llave foranea.
	 */
    @Test(expected = ExceptionLogica.class)
    public void testGuardarEvento15() throws ExceptionCampo, ExceptionLogica, Exception {
        Evento ev = new Evento();
        ev.setNombre("123456789012345678901234567890dsadsadasd");
        Ciudad ciudad = new Ciudad("1", "Ciu", null, "");
        ciudad.setPais(new Pais("1", ""));
        ev.setCiudad(ciudad);
        ev.setDescripcion("descripcion");
        ev.setTipoEvento(new TipoEvento("1", "Nom", ""));
        ev.setFechaHoraInicio(Calendar.getInstance().getTime());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        ev.setFechaHoraFin(Calendar.getInstance().getTime());
        ev.setDireccion("dire");
        ev.setCoordenadas("coor");
        this.controlador.guardarEvento(ev);
    }

    /**
	 * Test method for
	 * {@link com.mindbox.viajes.logica.ControladorEvento#guardarEvento(com.mindbox.viajes.dto.Evento)}.
	 * Prueba del tipo de evento nulo.
	 */
    @Test(expected = ExceptionCampo.class)
    public void testGuardarEvento16() throws ExceptionCampo, ExceptionLogica {
        Evento ev = new Evento();
        ev.setNombre("nombre");
        Ciudad ciudad = new Ciudad();
        ciudad.setPais(null);
        ev.setCiudad(ciudad);
        this.controlador.guardarEvento(ev);
    }
}
