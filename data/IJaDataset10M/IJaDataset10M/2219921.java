package tp2.modelo.test;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import org.junit.*;
import tp2.modelo.*;
import tp2.modelo.excepciones.*;
import tp2.auxiliares.*;

public class VueloCompuestoTest {

    private VueloCompuesto vueloCompuesto;

    private Vuelo vueloParcial;

    private ObjetoVolador objetoVolador;

    private Escenario escenario;

    @Before
    public void arrange() {
        escenario = new Escenario(new Rectangle(new Dimension(1, 1)));
        objetoVolador = new Nave(new Point(0, 0), 1, escenario, 5, 10);
        vueloCompuesto = new VueloCompuesto(objetoVolador);
    }

    @Test
    public void testAgregarVueloConTrayectoria1() {
        vueloParcial = new VueloEnLineaRecta(new Nave(new Point(0, 0), 1, escenario, 5, 10), new Point(1, 0));
        try {
            vueloCompuesto.agregarVuelo(vueloParcial, 1);
            Assert.fail("Se pudo agregar un vuelo inv�lido al compuesto");
        } catch (ObjetoDesconocido e) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Falla con una excepccion invalida");
        }
    }

    @Test
    public void testAgregarVueloConTrayectoria2() {
        vueloParcial = new VueloEnLineaRecta(objetoVolador, new Point(1, 0));
        try {
            vueloCompuesto.agregarVuelo(vueloParcial, 1);
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Falla con una excepccion invalida");
        }
    }

    @Test
    public void testAgregarVueloConTrayectoria3() {
        try {
            vueloCompuesto.agregarVuelo(vueloParcial, 0);
            Assert.fail("Se pudo agregar un vuelo con trayectoria inv�lida");
        } catch (ValorInvalido e) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Falla con una excepccion invalida");
        }
    }

    @Test
    public void testAvanzarDurante1() {
        Point posicionActual = new Point(0, 0);
        double velocidad = Math.PI / 2;
        objetoVolador = new Nave(posicionActual, 1, escenario, velocidad, 10);
        vueloCompuesto = new VueloCompuesto(objetoVolador);
        vueloParcial = new VueloEnCirculos(objetoVolador, new Point(0, 1), true);
        vueloCompuesto.agregarVuelo(vueloParcial, Math.PI);
        vueloParcial = new VueloEnCirculos(objetoVolador, new Point(0, 1), false);
        vueloCompuesto.agregarVuelo(vueloParcial, Math.PI);
        vueloCompuesto.iniciar();
        ArrayList<Point> posiciones = new ArrayList<Point>();
        posicionActual = posicionActual.sumarCon(vueloCompuesto.avanzarDurante(1));
        posiciones.add(posicionActual);
        posicionActual = posicionActual.sumarCon(vueloCompuesto.avanzarDurante(2));
        posiciones.add(posicionActual);
        posicionActual = posicionActual.sumarCon(vueloCompuesto.avanzarDurante(2));
        posiciones.add(posicionActual);
        Assert.assertTrue(((Point) (posiciones.get(0))).distance(new Point(1, 1)) <= 1E-10);
        Assert.assertTrue(((Point) (posiciones.get(1))).distance(new Point(-1, 3)) <= 1E-10);
        Assert.assertTrue(((Point) (posiciones.get(2))).distance(new Point(1, 3)) <= 1E-10);
    }

    @Test
    public void testAvanzarDurante2() {
        Point posicionActual = new Point(0, 0);
        double velocidad = Math.PI / 2;
        objetoVolador = new Nave(posicionActual, 1, escenario, velocidad, 10);
        vueloCompuesto = new VueloCompuesto(objetoVolador);
        vueloParcial = new VueloEnCirculos(objetoVolador, new Point(0, 1), true);
        vueloCompuesto.agregarVuelo(vueloParcial, Math.PI);
        vueloParcial = new VueloEnCirculos(objetoVolador, new Point(0, 1), false);
        vueloCompuesto.agregarVuelo(vueloParcial, Math.PI);
        try {
            vueloCompuesto.avanzarDurante(1);
            Assert.fail("El vuelo avanz� sin estar iniciado");
        } catch (VueloNoIniciado e) {
            Assert.assertTrue(true);
        } catch (Exception e) {
            Assert.fail("Falla con una excepccion invalida");
        }
    }
}
