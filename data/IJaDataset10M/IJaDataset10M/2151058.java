package test;

import modelo.*;
import modelo.exceptions.ExceptionFinPista;
import control.*;
import junit.framework.TestCase;

public class TestVirtual extends TestCase {

    public static void main(String[] args) {
        Auto autoVirtual = new Auto();
        Habilidad habilidad = new Intermedio(autoVirtual);
        control.Virtual ctrlVirtual = new control.Virtual(habilidad, autoVirtual);
        modelo.Virtual virtual = new modelo.Virtual(ctrlVirtual, autoVirtual);
        Pista pista = new Pista(virtual.getAuto(), virtual.getAuto(), 600);
        TestVentanaManejarVirtual vista = new TestVentanaManejarVirtual(virtual, pista);
        virtual.getAuto().setPosicion(0);
        vista.addKeyListener(new control.Usuario(virtual.getAuto()));
        virtual.getAuto().agregarObservador(vista);
        virtual.getAuto().ActualizarObservadores();
        vista.setVisible(true);
        boolean enCarrera = true;
        virtual.jugar(true);
        while (enCarrera) {
            System.out.println("while- encendido: " + virtual.getAuto().isEncendido() + " acelerando: " + virtual.getAuto().isAcelerando() + " cambio: " + virtual.getAuto().getCaja().getCambio() + " rpm: " + virtual.getAuto().getMotor().getRPM());
            try {
                synchronized (virtual.getAuto()) {
                    virtual.getAuto().wait(25);
                    pista.actualizarPosiciones();
                    virtual.getAuto().Desgastar();
                    virtual.getAuto().notifyAll();
                }
            } catch (ExceptionFinPista e) {
                enCarrera = false;
                System.out.println("FIN PRUEBA en testVirtual");
            } catch (InterruptedException e) {
                throw new IllegalStateException("algo interrumpido en testVirtual", e);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
        }
        virtual.jugar(false);
        vista.dispose();
    }
}
