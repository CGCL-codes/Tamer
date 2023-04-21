package fiuba.algo3.juego.test;

import junit.framework.TestCase;
import org.junit.Test;
import fiuba.algo3.juego.modelo.Algo42;
import fiuba.algo3.juego.modelo.Arma;
import fiuba.algo3.juego.modelo.Civil;
import fiuba.algo3.juego.modelo.Cohete;
import fiuba.algo3.juego.modelo.Laser;
import fiuba.algo3.juego.modelo.NaveNoOperable;
import fiuba.algo3.juego.modelo.Plano;
import fiuba.algo3.juego.modelo.Punto;
import fiuba.algo3.juego.modelo.excepciones.AlgoSeAtacaASiMismoError;
import fiuba.algo3.juego.modelo.excepciones.AreaInvalidaError;
import fiuba.algo3.juego.modelo.excepciones.AtaqueEntreNavesNoOperables;
import fiuba.algo3.juego.modelo.excepciones.NaveDestruidaError;
import fiuba.algo3.juego.modelo.excepciones.SuperposicionNavesError;

public class ArmasTest extends TestCase {

    @Test
    public void testAtaqueInvalidoArmaAlgo42() throws AtaqueEntreNavesNoOperables, AreaInvalidaError {
        Plano plano = new Plano(100, 100);
        Punto posicionAlgo = new Punto(50, 50);
        Punto posicionCohete = new Punto(50, 50);
        Algo42 algo = new Algo42(posicionAlgo, plano);
        Cohete cohete = new Cohete(posicionCohete, true, plano);
        try {
            cohete.intentarChocar(algo);
            fail("Algo no puede atacarse a si mismo");
        } catch (AlgoSeAtacaASiMismoError error) {
        }
    }

    @Test
    public void testAtaqueInvalidoArmaNaveNoOperable() throws AreaInvalidaError, AlgoSeAtacaASiMismoError, SuperposicionNavesError, NaveDestruidaError {
        Plano plano = new Plano(100, 100);
        Punto puntoCohete = new Punto(50, 50);
        Punto puntoCivil = new Punto(50, 50);
        Cohete cohete = new Cohete(puntoCohete, false, plano);
        NaveNoOperable civil = new Civil(puntoCivil, plano);
        try {
            cohete.intentarChocar(civil);
            fail("Deberia fallar porque entre enemigos no se atacan");
        } catch (AtaqueEntreNavesNoOperables error) {
        }
    }

    @Test
    public void testMovimiento() {
        Plano plano = new Plano(100, 100);
        Punto punto = new Punto(50, 90);
        Arma laser = new Laser(punto, false, plano);
        int posicion = 90;
        for (int i = 1; i == 30; i++) {
            laser.intentarMover();
            posicion = (posicion - 2);
            assertTrue("La posicion en X no cambia", laser.devolverPunto().getX() == 50);
            assertTrue("La posicion en Y debe decrementarse en 2 unidades", laser.devolverPunto().getY() == posicion);
        }
    }

    @Test
    public void testMovimientoFueraDeArea() {
        Punto punto = new Punto(50, 80);
        Plano plano = new Plano(100, 100);
        Laser laser = new Laser(punto, false, plano);
        for (int i = 0; i < 42; i++) {
            laser.intentarMover();
        }
        assertEquals(laser.fueUsado(), true);
    }
}
