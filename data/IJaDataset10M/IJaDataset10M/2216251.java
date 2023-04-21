package logica;

import java.awt.Color;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Iterator;
import logica.entidades.Fantasma;
import logica.entidades.FantasmaNaranja;
import logica.entidades.FantasmaRojo;
import logica.entidades.FantasmaRosa;
import logica.entidades.FantasmaVerde;
import logica.entidades.Pacman;
import logica.escenario.Escenario;
import logica.escenario.Fruta;
import logica.escenario.ListaDeEscenarios;
import controlador.ManejoPorTeclado;
import vista.*;
import ar.uba.fi.algo3.titiritero.ControladorJuego;
import ar.uba.fi.algo3.titiritero.ObjetoVivo;
import ar.uba.fi.algo3.titiritero.vista.TextoDinamico;
import ar.uba.fi.algo3.titiritero.vista.Ventana;

public class Juego implements ObjetoVivo {

    private static final int resolucionX = 700;

    private static final int resolucionY = 600;

    private static final double escala = 1.85;

    private static final int margenSinEscalarX = 0;

    private static final int margenSinEscalarY = 0;

    private Pacman pacman;

    private Fantasma[] arrayDeFantasmas;

    private ListaDeEscenarios listaDeEscenarios;

    private Escenario escenarioActual;

    private long puntaje;

    private int vidas;

    private int nivelActual;

    private ControladorJuego controladorJuego;

    private Ventana superficieDeDibujo;

    private TextoInfomativo textoPuntaje;

    private TextoInfomativo textoVidas;

    private int posicionHorizontalTextosInformativos;

    private Fruta fruta;

    public Juego(String archivoTextoConEscenarios) throws IOException {
        this.puntaje = 0;
        this.vidas = 3;
        this.nivelActual = 1;
        this.arrayDeFantasmas = new Fantasma[4];
        this.listaDeEscenarios = new ListaDeEscenarios(archivoTextoConEscenarios, this);
        this.pacman = null;
        this.escenarioActual = null;
        this.superficieDeDibujo = new Ventana(resolucionX, resolucionY, this.controladorJuego);
    }

    private void inicializarFantasmas(Escenario escenario) {
        float velocidadActual = (float) (1 + (0.2 * (this.nivelActual - 1)));
        int duracionModoAzulActual = (40 - (this.nivelActual - 1));
        int puntosAlSerComidoActual = (100 + (10 * (this.nivelActual - 1)));
        Iterator<Posicion> iteradorPuntosDeSeparacion = escenario.iteradorPuntosDeSeparacion();
        arrayDeFantasmas[0] = new FantasmaRosa(escenario, iteradorPuntosDeSeparacion.next(), duracionModoAzulActual, velocidadActual, puntosAlSerComidoActual);
        arrayDeFantasmas[1] = new FantasmaNaranja(escenario, iteradorPuntosDeSeparacion.next(), duracionModoAzulActual, velocidadActual, puntosAlSerComidoActual);
        arrayDeFantasmas[2] = new FantasmaVerde(escenario, iteradorPuntosDeSeparacion.next(), duracionModoAzulActual, velocidadActual, puntosAlSerComidoActual);
        arrayDeFantasmas[3] = new FantasmaRojo(escenario, iteradorPuntosDeSeparacion.next(), duracionModoAzulActual, velocidadActual, puntosAlSerComidoActual);
    }

    private void agregarObjetosVivosAlControlador() {
        VistaFruta vistaFruta = new VistaFruta(this.fruta, this.getEscalaYPosicion());
        this.controladorJuego.agregarDibujable(vistaFruta);
        this.controladorJuego.agregarObjetoVivo(pacman);
        VistaPacman vistaPacman = new VistaPacman(pacman, this.getEscalaYPosicion());
        vistaPacman.setPosicionable(this.pacman);
        this.controladorJuego.agregarDibujable(vistaPacman);
        VistaFantasma vistasFantasma[] = new VistaFantasma[arrayDeFantasmas.length];
        final Color coloresFantasma[] = { Color.RED, Color.ORANGE, Color.GREEN, Color.PINK };
        for (int i = 0; i < arrayDeFantasmas.length; i++) {
            this.controladorJuego.agregarObjetoVivo(arrayDeFantasmas[i]);
            vistasFantasma[i] = new VistaFantasma(coloresFantasma[i], arrayDeFantasmas[i], this.getEscalaYPosicion());
            this.controladorJuego.agregarDibujable(vistasFantasma[i]);
        }
    }

    private void inicializarNivel(int nivel) throws IOException {
        this.superficieDeDibujo.setVisible(true);
        this.superficieDeDibujo.limpiar();
        this.controladorJuego = new ControladorJuego();
        this.controladorJuego.setSuperficieDeDibujo(this.superficieDeDibujo);
        Escenario escenario = listaDeEscenarios.getEscenario(nivel);
        this.pacman = new Pacman(escenario, escenario.getPosicionInicialPacman());
        this.pacman.setPuntajeAcumulado(this.puntaje);
        escenario.colocarPacman(this.pacman);
        this.controladorJuego.setSuperficieDeDibujo(this.superficieDeDibujo);
        this.escenarioActual = escenario;
        this.fruta = new Fruta(this.escenarioActual, this.escenarioActual.getPosicionInicialPacman(), 50);
        this.escenarioActual.getUeb(this.escenarioActual.getPosicionInicialPacman()).addNoJugador(this.fruta);
        this.controladorJuego.agregarObjetoVivo(this.fruta);
        this.inicializarFantasmas(escenario);
        this.agregarObjetosVivosAlControlador();
        this.controladorJuego.agregarObjetoVivo(this);
        this.controladorJuego.setIntervaloSimulacion(120);
        this.superficieDeDibujo.addKeyListener((KeyListener) new ManejoPorTeclado(this.pacman));
        Posicion posicionPuntaje = new Posicion(1, this.getPosicionHorizontalTextosInformativos());
        Posicion posicionVidas = new Posicion(15, this.getPosicionHorizontalTextosInformativos());
        this.textoPuntaje = new TextoInfomativo("Puntaje: ", this.puntaje, posicionPuntaje, getEscalaYPosicion());
        this.textoVidas = new TextoInfomativo("Vidas: ", this.puntaje, posicionVidas, getEscalaYPosicion());
        TextoDinamico textoDinamicoPuntaje = new TextoDinamico(this.textoPuntaje);
        TextoDinamico textoDinamicoVidas = new TextoDinamico(this.textoVidas);
        textoDinamicoPuntaje.setPosicionable(this.textoPuntaje);
        textoDinamicoVidas.setPosicionable(this.textoVidas);
        this.controladorJuego.agregarDibujable(textoDinamicoVidas);
        this.controladorJuego.agregarDibujable(textoDinamicoPuntaje);
    }

    public void jugar() throws IOException {
        while ((listaDeEscenarios.tieneSiguiente()) && (this.vidas != 0)) {
            this.inicializarNivel(this.nivelActual);
            this.controladorJuego.comenzarJuego();
        }
    }

    public void vivir() {
        if ((this.escenarioActual.getPuntosRestantes() != 0) && (this.vidas > 0)) {
            this.ActualizarPuntajeJugador();
            this.textoPuntaje.actualizarValorDelTexto(this.puntaje);
            this.textoVidas.actualizarValorDelTexto(this.vidas);
            if (!this.pacman.estaVivo()) {
                this.RutinaCuandoElPacmanMuere();
            }
        } else {
            this.controladorJuego.detenerJuego();
            if (this.vidas > 0) {
                this.nivelActual++;
            } else {
                this.superficieDeDibujo.setVisible(false);
            }
        }
    }

    private void RutinaCuandoElPacmanMuere() {
        this.vidas = this.vidas - 1;
        for (int i = 0; i < 4; i++) {
            this.arrayDeFantasmas[i].reset();
        }
        this.pacman.setPosicion(this.escenarioActual.getPosicionInicialPacman());
        this.pacman.revivir();
    }

    public void ActualizarPuntajeJugador() {
        this.puntaje = this.pacman.getPuntajeAcumulado();
    }

    public void ponerFantasmasAzules() {
        for (int i = 0; i < 4; i++) {
            this.arrayDeFantasmas[i].volverAzul();
        }
    }

    public ControladorJuego getControlador() {
        return this.controladorJuego;
    }

    public EscalaYPosicion getEscalaYPosicion() {
        return new EscalaYPosicion(escala, margenSinEscalarX, margenSinEscalarY);
    }

    public void setPosicionHorizontalTextosInformativos(int posicionHorizontalTextosInformativos) {
        this.posicionHorizontalTextosInformativos = posicionHorizontalTextosInformativos;
    }

    public int getPosicionHorizontalTextosInformativos() {
        return posicionHorizontalTextosInformativos;
    }
}
