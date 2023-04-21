package tp2.modelo.test;

import java.awt.Dimension;
import java.awt.Rectangle;
import org.junit.*;
import tp2.modelo.*;
import tp2.auxiliares.*;

public class ArmaDirigidaTest {

    private Escenario escenario;

    private ArmaDirigida arma;

    private Flota flota;

    private Proyectil proyectil1;

    private Proyectil proyectil2;

    private NaveMilitar objetivo1;

    private NaveMilitar objetivo2;

    @Before
    public void arrange() {
        escenario = new Escenario(new Rectangle(new Dimension(1, 1)));
        arma = new ArmaDirigida(new Point(10, 1), escenario, 100, 1, new Point(4, 3), "arma");
        arma.setModeloDeProyectil(new Proyectil(new Point(0, 0), 0.1, 0, 10));
        objetivo1 = new NaveMilitar(new Point(40, -234), 1, escenario, 1, 100);
        objetivo2 = new NaveMilitar(new Point(22, 67), 1, escenario, 1, 100);
        objetivo1.setEquipo("aliados");
        objetivo2.setEquipo("aliados");
        flota = new Flota(objetivo1);
        arma.setFlotaObjetivo(flota);
    }

    @Test
    public void testDisparar1() {
        proyectil1 = arma.disparar();
        proyectil1.moverDurante(10000);
        Assert.assertTrue((proyectil1.getPosicion().distance(objetivo1.getPosicion())) <= (1E-10));
    }

    @Test
    public void TestDisparar2() {
        flota.agregarNave(objetivo2);
        objetivo1.destruir();
        proyectil2 = arma.disparar();
        proyectil2.moverDurante(10000);
        Assert.assertTrue((proyectil2.getPosicion().distance(objetivo2.getPosicion())) <= (1E-10));
    }

    @Test
    public void TestDisparar3() {
        objetivo1.destruir();
        objetivo2.destruir();
        proyectil2 = arma.disparar();
        proyectil2.moverDurante(10);
        Assert.assertTrue((proyectil2.getPosicion().distance(new Point(810, 601))) <= (1E-10));
    }
}
