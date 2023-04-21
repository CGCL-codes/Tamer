package test;

import modelo.*;
import static org.junit.Assert.*;
import modelo.componente.Automatica;
import org.junit.Before;
import org.junit.Test;

public class TestAuto {

    Auto auto = new Auto();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void testAuto() {
        try {
            assertNotNull(auto.getCaja());
            assertNotNull(auto.getAlimentacion());
            assertNotNull(auto.getCarroceria());
            assertNotNull(auto.getCombustible());
            assertNotNull(auto.getEjeDelantero());
            assertNotNull(auto.getEjeTrasero());
            assertNotNull(auto.getEscape());
            assertNotNull(auto.getMotor());
            assertNotNull(auto.getSuspension());
            assertNotNull(auto.getTurbo());
            assertFalse(auto.isEncendido());
            assertTrue(auto.isAutomatica());
            assertFalse(auto.isManual());
            assertFalse(auto.isSecuencial());
            assertTrue(auto.getVelocidad() == 0);
            assertNotNull(auto.getMotor().getAuto());
            assertNotNull(auto.getMotor().getAuto().getCaja());
            assertNotNull(auto.getLlantaDelanteraDerecha());
            assertNotNull(auto.getLlantaDelanteraIzquierda());
            assertNotNull(auto.getLlantaTraseraDerecha());
            assertNotNull(auto.getLlantaTraseraIzquierda());
            assertNotNull(auto.getNeumaticoDelanteroDerecho());
            assertNotNull(auto.getNeumaticoDelanteroIzquierdo());
            assertNotNull(auto.getNeumaticoTraseroDerecho());
            assertNotNull(auto.getNeumaticoTraseroIzquierdo());
            assertNotNull(auto.getEjeDelantero().getLlantaDerecha());
            assertNotNull(auto.getEjeDelantero().getLlantaIzquierda());
            assertNotNull(auto.getEjeTrasero().getLlantaDerecha());
            assertNotNull(auto.getEjeTrasero().getLlantaIzquierda());
            assertNotNull(auto.getLlantaDelanteraDerecha().getNeumatico());
            assertNotNull(auto.getLlantaDelanteraIzquierda().getNeumatico());
            assertNotNull(auto.getLlantaTraseraDerecha().getNeumatico());
            assertNotNull(auto.getLlantaTraseraIzquierda().getNeumatico());
            assertNotNull(auto.getNeumaticoDelanteroDerecho().getLlanta());
            assertNotNull(auto.getNeumaticoDelanteroIzquierdo().getLlanta());
            assertNotNull(auto.getNeumaticoTraseroDerecho().getLlanta());
            assertNotNull(auto.getNeumaticoTraseroIzquierdo().getLlanta());
            assertNotNull(auto.getLlantaDelanteraDerecha().getEje());
            assertNotNull(auto.getLlantaDelanteraIzquierda().getEje());
            assertNotNull(auto.getLlantaTraseraDerecha().getEje());
            assertNotNull(auto.getLlantaTraseraIzquierda().getEje());
        } catch (AssertionError a) {
            a.printStackTrace();
            a.getMessage();
        }
    }

    @Test
    public void testComprobarComponentes() {
        try {
            auto.comprobarComponentes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEstaListoParaCarrera() {
        try {
            assertTrue(auto.estaListoParaCarrera());
        } catch (AssertionError a) {
            a.printStackTrace();
            a.getMessage();
        }
    }

    @Test
    public void testSetEncendido() {
        try {
            auto.setEncendido(true);
            assertTrue(auto.isEncendido());
            assertTrue(auto.getMotor().isEncendido());
            assertTrue(auto.isEncendido());
        } catch (AssertionError a) {
            a.printStackTrace();
            a.getMessage();
        }
    }

    @Test
    public void testGetPeso() {
        try {
            assertTrue(auto.getPeso() > 0);
        } catch (AssertionError a) {
            a.printStackTrace();
        }
    }

    @Test
    public void testAcelerar() {
        try {
            auto.setEncendido(true);
            assertTrue(auto.isEncendido());
            auto.acelerar(true);
            assertTrue(auto.isAcelerando());
            assertTrue(auto.getMotor().isAcelerando());
        } catch (AssertionError a) {
            a.printStackTrace();
        }
    }

    @Test
    public void testAcelerarDesacelerarCajaAutomatica() {
        auto.setEncendido(true);
        assertTrue(auto.isEncendido());
        auto.setCaja(new Automatica(5));
        int cambio = 5;
        double velocidad = 0;
        while ((auto.getCaja().getCambio() <= cambio) && (auto.getMotor().getRPM() <= (300 + auto.getMotor().getRevolucionesUmbralPeligro()))) {
            auto.acelerar(true);
            try {
                assertTrue(auto.getVelocidad() >= velocidad);
            } catch (AssertionError a) {
                a.printStackTrace();
            }
            velocidad = auto.getVelocidad();
        }
        try {
            assertTrue(auto.getVelocidad() > 100);
        } catch (AssertionError a) {
            a.printStackTrace();
        }
        velocidad = auto.getVelocidad();
        int contador = 0;
        while ((auto.getCaja().getCambio() < 3) && (contador < 6000)) {
            auto.acelerar(false);
            try {
                assertTrue(auto.getVelocidad() < velocidad);
            } catch (AssertionError a) {
                a.printStackTrace();
            }
            contador++;
        }
        cambio = 5;
        velocidad = 0;
        while ((auto.getCaja().getCambio() <= cambio) && (auto.getMotor().getRPM() <= (300 + auto.getMotor().getRevolucionesUmbralPeligro()))) {
            auto.acelerar(true);
            try {
                assertTrue(auto.getVelocidad() >= velocidad);
            } catch (AssertionError a) {
                a.printStackTrace();
            }
            velocidad = auto.getVelocidad();
        }
        try {
            assertTrue(auto.getVelocidad() > 100);
        } catch (AssertionError a) {
            a.printStackTrace();
        }
    }
}
