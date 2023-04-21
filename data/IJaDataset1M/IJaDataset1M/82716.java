package test;

import junit.framework.*;
import modelo.componente.*;
import modelo.*;

public class TestTurbo extends TestCase {

    public void testDesgastar() {
        Turbo turbo = new Turbo();
        turbo.setEstado(100);
        turbo.desgastar();
        assertEquals(99.999999999982, turbo.getEstado());
    }

    public void testObtenerPotencia() {
        Turbo turbo = new Turbo();
        assertEquals(5.0, turbo.obtenerPotencia());
    }

    public void testAfectar() {
        Turbo turbo = new Turbo();
        Clima clima = new Clima(25.0, 50.0, 1013.0);
        turbo.afectar(clima);
        assertEquals(1.0, turbo.getCoeficienteDeObtencionDePotencia());
    }

    public static Test suite() {
        return new TestSuite(TestTurbo.class);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}
