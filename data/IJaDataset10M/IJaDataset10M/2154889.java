package clickandlearn.conquis.minijuegos.relaciones.red;

import java.util.Timer;
import java.util.TimerTask;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import clickandlearn.conquis.comun.RecursosComunes;
import clickandlearn.conquis.minijuegos.relaciones.EstadoJuegoRelaciones;
import clickandlearn.conquis.red.Estado;
import clickandlearn.conquis.red.GameStateRed;
import clickandlearn.conquis.red.InfoRed;
import clickandlearn.conquis.tablero.InfoJuego;

public class InGameState extends clickandlearn.conquis.minijuegos.relaciones.InGameState implements GameStateRed {

    private boolean esperando = true;

    private Timer timer;

    private boolean mostrarLetras = false;

    private boolean mostrarUnMomento = false;

    private boolean habilitaMouse = false;

    public InGameState(int iD) {
        super(iD);
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        System.out.println("-- Juego: " + InfoJuego.getInstance().getJuego_aleatorio());
        super.init(container, game);
        timer = new Timer();
    }

    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        if (!esperando) {
            super.render(container, game, g);
            if (!habilitaMouse) {
            }
            if ((InfoRed.getInstance().getId() != (InfoJuego.getInstance().getJugadorMinijuego1() + 1)) && mostrarLetras && !mostrarUnMomento) {
                RecursosComunes.getInstance().getFuente().drawString(300, 300, "Espectador");
            } else if (mostrarUnMomento) {
                RecursosComunes.getInstance().getFuente().drawString(300, 300, "Un momento...");
            }
        } else RecursosComunes.getInstance().getFuente().drawString(230, 300, "Esperando Jugadores...");
    }

    @Override
    public void update(GameContainer container, StateBasedGame game, int arg2) throws SlickException {
        if (!esperando) super.update(container, game, arg2);
    }

    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            if (!RecursosComunes.getInstance().mostrarConfirmacion()) System.exit(0);
        } else {
            if (!esperando) {
                if ((InfoJuego.getInstance().getJugadorMinijuego1() + 1) == InfoRed.getInstance().getId()) {
                    InfoRed.getInstance().getCliente().keyPressed(key, c);
                }
            }
        }
    }

    public void mousePressed(int button, int x, int y) {
        if (!esperando) {
            if ((InfoJuego.getInstance().getJugadorMinijuego1() + 1) == InfoRed.getInstance().getId()) {
                InfoRed.getInstance().getCliente().mousePressed(button, x, y);
            }
        }
    }

    public void enter(GameContainer container, StateBasedGame game) {
        esperando = true;
        InfoRed.getInstance().getCliente().setGameStateActual(this);
        InfoRed.getInstance().getCliente().jugadorEsperando();
        if ((InfoRed.getInstance().getId() != (InfoJuego.getInstance().getJugadorMinijuego1() + 1))) {
            timer.scheduleAtFixedRate(new TimerTask() {

                public void run() {
                    mostrarLetras = !mostrarLetras;
                }
            }, 1000, 1000);
        }
        super.enter(container, game);
    }

    public void leave(GameContainer container, StateBasedGame game) {
        InfoRed.getInstance().getCliente().setGameStateActual(null);
        timer.cancel();
        super.leave(container, game);
    }

    @Override
    public void empezar() {
        esperando = false;
        habilitaMouse = false;
        int delay = 3000;
        Timer timer3 = new Timer();
        timer3.schedule(new TimerTask() {

            public void run() {
                habilitaMouse = true;
            }
        }, delay);
    }

    @Override
    public synchronized Estado getEstado() {
        return estadoJuego;
    }

    @Override
    public boolean isEsperando() {
        return esperando;
    }

    @Override
    public void procesaBotonMousePulsado(int button, int x, int y) {
        if (habilitaMouse) {
            super.mousePressed(button, x, y);
        }
    }

    @Override
    public void procesaBotonMouseSoltado(int button, int x, int y) {
    }

    @Override
    public synchronized void procesaSyncEstado(Estado estado) {
        if (InfoRed.getInstance().getId() != 1) estadoJuego = (EstadoJuegoRelaciones) estado;
    }

    @Override
    public void procesaTeclaPulsada(int key, char c) {
        super.keyPressed(key, c);
    }

    @Override
    public void procesaTeclaSoltada(int key, char c) {
    }

    @Override
    public void cambiarEstado(final int estado) {
        InfoRed.getInstance().getCliente().setGameStateActual(null);
        InfoRed.getInstance().getCliente().flush();
        mostrarUnMomento = true;
        int delay = 3000;
        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {

            public void run() {
                procesaCambiarEstado(estado);
            }
        }, delay);
    }

    private void procesaCambiarEstado(int estado) {
        mostrarUnMomento = false;
        super.cambiarEstado(estado);
    }
}
