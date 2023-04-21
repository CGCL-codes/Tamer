package modelo;

import javax.swing.*;
import vista.ventanas.*;
import modelo.exceptions.*;

public class Carrera implements Runnable {

    private Pista pista;

    private JFrame ventanaAnterior;

    private VentanaCarrera vista;

    private AlgoPesos apuesta;

    private modelo.Usuario usuario;

    private modelo.Virtual virtual;

    public Carrera(Usuario usuario, Virtual virtual, Pista pista, AlgoPesos apuesta, JFrame ventanaAnterior, VentanaCarrera vistaCarrera) {
        this.ventanaAnterior = ventanaAnterior;
        this.usuario = usuario;
        this.virtual = virtual;
        this.pista = pista;
        this.apuesta = apuesta;
        this.vista = vistaCarrera;
        this.vista.addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent e) {
                cerrarVentana();
            }
        });
    }

    /**
	 * Metodo que se encarga de inicializar los atributos para la carrera
	 */
    private void incializar() {
        this.usuario.getAuto().setPosicion(0);
        this.virtual.getAuto().setPosicion(0);
        this.vista.addKeyListener(new control.Usuario(usuario.getAuto()));
        this.vista.addKeyListener(this.virtual.getControl());
        this.usuario.getAuto().agregarObservador(this.vista);
        this.virtual.getAuto().agregarObservador(this.vista);
        this.usuario.getAuto().ActualizarObservadores();
        this.virtual.getAuto().ActualizarObservadores();
        this.vista.setVisible(true);
    }

    /**
	 * Metodo que se encarga de aumentar / decrementar el dinero del usuario segun
	 * el resultado de la carrera, setea las posiciones de los autos nuevamente en 0,
	 * cierra la vista de la carrera y muestra la vista anterior a la carrera
	 */
    private void finalizar() {
        if (this.vista.isEnabled()) {
            String ganador = null;
            if (this.usuario.getAuto().getPosicion() < this.virtual.getAuto().getPosicion()) {
                this.usuario.getDinero().restar(this.apuesta);
                ganador = this.virtual.getNombre();
            } else {
                this.usuario.getDinero().sumar(this.apuesta);
                ganador = this.usuario.getNombre();
            }
            JOptionPane.showMessageDialog(this.vista, " Fin de la carrera.\n Ganador: " + ganador + "\n Ud tiene " + usuario.getDinero().toStringConUnidades());
        }
        this.virtual.getAuto().setPosicion(0);
        this.usuario.getAuto().setPosicion(0);
        cerrarVentana();
    }

    public void cerrarVentana() {
        this.vista.removeAll();
        this.usuario.getAuto().setEncendido(false);
        this.virtual.getAuto().setEncendido(false);
        this.usuario.getAuto().deleteObservers();
        this.virtual.getAuto().deleteObservers();
        this.virtual.getAuto().setPosicion(0);
        this.usuario.getAuto().setPosicion(0);
        pista.deleteObservers();
        this.vista.setEnabled(false);
        this.vista.setVisible(false);
        vista = null;
        try {
            this.vista.dispose();
        } catch (NullPointerException e) {
        }
        ;
        this.ventanaAnterior.setVisible(true);
    }

    public void run() {
        this.incializar();
        boolean enCarrera = true;
        this.virtual.getAuto().setEncendido(true);
        this.virtual.jugar(true);
        long tiempo = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < tiempo) ;
        this.vista.getPanelInfoUsuario().setCorriendo(true);
        this.usuario.getAuto().setEncendido(true);
        while (enCarrera) {
            try {
                synchronized (this.usuario.getAuto()) {
                    synchronized (this.virtual.getAuto()) {
                        this.virtual.manejar();
                        this.usuario.getAuto().ActualizarObservadores();
                        this.virtual.getAuto().ActualizarObservadores();
                        this.usuario.getAuto().wait(25);
                        this.virtual.getAuto().wait(25);
                        this.pista.actualizarPosiciones();
                        this.usuario.getAuto().Desgastar();
                        this.virtual.getAuto().Desgastar();
                        this.virtual.getAuto().notifyAll();
                    }
                    this.usuario.getAuto().notifyAll();
                }
            } catch (ExceptionFinPista e) {
                enCarrera = false;
            } catch (InterruptedException e) {
                throw new IllegalStateException("algo interrumpido", e);
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            try {
                if (!this.vista.isEnabled()) {
                    enCarrera = false;
                }
            } catch (NullPointerException e) {
            }
            ;
        }
        this.virtual.jugar(false);
        this.finalizar();
    }
}
