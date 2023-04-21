package clickandlearn.conquis.minijuegos.barcos;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import clickandlearn.conquis.comun.CargaDinamica;
import clickandlearn.conquis.comun.RecursosComunes;
import clickandlearn.conquis.comun.TextoMaquina;
import clickandlearn.conquis.minijuegos.barcos.entity.AbstractEntity;
import clickandlearn.conquis.minijuegos.barcos.entity.Entity;
import clickandlearn.conquis.minijuegos.barcos.entity.EntityManager;
import clickandlearn.conquis.minijuegos.barcos.entity.Player;
import clickandlearn.conquis.singleplayer.GameStates;
import clickandlearn.conquis.tablero.InfoJuego;

public class InGameState extends BasicGameState implements EntityManager, CargaDinamica {

    protected final int VIDA_MAX = 100;

    protected final int PENALIZACION = 10;

    protected final int[] player1Keys = { Input.KEY_A, Input.KEY_W, Input.KEY_D, Input.KEY_F, Input.KEY_G };

    protected final int[] player2Keys = { Input.KEY_LEFT, Input.KEY_UP, Input.KEY_RIGHT, Input.KEY_N, Input.KEY_M };

    private int stateID = 0;

    private StateBasedGame game;

    private GameContainer container;

    private Image background = null;

    private boolean fondoAbajo;

    private boolean fondoDerecha;

    private float fondoX;

    private float fondoY;

    public static AngelCodeFont font;

    private ArrayList<AbstractEntity> entities = new ArrayList<AbstractEntity>();

    private ArrayList<AbstractEntity> addList = new ArrayList<AbstractEntity>();

    private ArrayList<Entity> removeList = new ArrayList<Entity>();

    protected Player player1;

    protected Player player2;

    private ParticleSystem hit;

    private float hitCount = 1;

    boolean renderHit = false;

    protected int lifePlayer1;

    protected int lifePlayer2;

    private Sound shotFx = null;

    private Sound explosionFx = null;

    private Sound choqueFx = null;

    protected boolean gameOver;

    private boolean player1win, player2win;

    private int[] teclasPulsadas;

    private Music musica;

    private boolean verInstrucciones;

    private TextoMaquina instrucciones;

    public InGameState(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        verInstrucciones = true;
        this.player1win = true;
        this.player2win = true;
        this.game = game;
        this.container = container;
        this.cargarInstrucciones();
        this.cargarRecursos();
        fondoX = 0;
        fondoY = 0;
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        if (!verInstrucciones) {
            background.draw(fondoX - 40, fondoY - 50, container.getScreenWidth() + 50, container.getScreenHeight() + 50);
            if (fondoAbajo) {
                fondoY += 0.15;
                if (fondoY >= 50) fondoAbajo = false;
            } else {
                fondoY -= 0.15;
                if (fondoY <= 0) fondoAbajo = true;
            }
            if (fondoDerecha) {
                fondoX += 0.12;
                if (fondoX >= 40) fondoDerecha = false;
            } else {
                fondoX -= 0.12;
                if (fondoY <= 40) fondoDerecha = true;
            }
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.render(container);
            }
            if (renderHit) {
                hit.render();
            }
            drawGUI(g);
            if (gameOver) {
                gameOver();
            }
            if (container.isPaused()) pause();
        } else {
            this.instrucciones.setEscribete(true);
            this.instrucciones.pinta(g, 50, 70);
        }
    }

    public void mousePressed(int button, int x, int y) {
        if (!gameOver && this.verInstrucciones) {
            this.instrucciones.setMsEntreLetras(15);
        }
    }

    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (!gameOver && this.verInstrucciones) {
            this.instrucciones.avanza();
        }
        if (!gameOver) {
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                for (int j = i + 1; j < entities.size(); j++) {
                    Entity other = (Entity) entities.get(j);
                    if (entity.collides(other)) {
                        entity.collide(this, other);
                        other.collide(this, entity);
                    }
                }
            }
            entities.removeAll(removeList);
            entities.addAll(addList);
            removeList.clear();
            addList.clear();
            for (int i = 0; i < entities.size(); i++) {
                Entity entity = (Entity) entities.get(i);
                entity.update(container, this, delta);
            }
            if (renderHit) {
                if (hitCount <= 0) {
                    renderHit = false;
                    hitCount = 1;
                }
                hitCount -= delta * 0.0005;
                hit.update(delta);
            }
        }
        if ((lifePlayer1 <= 0) || (lifePlayer2 <= 0)) {
            gameOver = true;
        }
        if (teclasPulsadas != null) {
            player1.actualiza(teclasPulsadas, delta);
            player2.actualiza(teclasPulsadas, delta);
        }
    }

    private void drawGUI(Graphics g) {
        String cadena1 = InfoJuego.getInstance().getJugador(InfoJuego.getInstance().getJugadorMinijuego1()).getNombre() + " : " + (lifePlayer1 >= 0 ? lifePlayer1 : 0);
        String cadena2 = InfoJuego.getInstance().getJugador(InfoJuego.getInstance().getJugadorMinijuego2()).getNombre() + " : " + (lifePlayer2 >= 0 ? lifePlayer2 : 0);
        font.drawString(10, 0, cadena1, Color.red);
        font.drawString(10, 40, cadena2, Color.blue);
    }

    public void removeEntity(Entity entity) {
        removeList.add(entity);
    }

    public void addEntity(Entity entity) {
        addList.add((AbstractEntity) entity);
    }

    public void playerHit(int player, float x, float y) {
        ((ConfigurableEmitter) hit.getEmitter(0)).setPosition(x, y);
        ((ConfigurableEmitter) hit.getEmitter(1)).setPosition(x, y);
        ((ConfigurableEmitter) hit.getEmitter(2)).setPosition(x, y);
        ((ConfigurableEmitter) hit.getEmitter(3)).setPosition(x, y);
        hit.reset();
        hitCount = 1;
        renderHit = true;
        if (player == 1) lifePlayer1 -= 2; else lifePlayer2 -= 2;
        if (explosionFx.playing()) explosionFx.stop();
        explosionFx.play(1.0f, 0.5f);
    }

    public void shotFired() {
        shotFx.play();
    }

    private void gameOver() {
        int ancho = 0;
        if (lifePlayer1 <= 0) {
            ancho = font.getWidth(InfoJuego.getInstance().getJugador(InfoJuego.getInstance().getJugadorMinijuego1()).getNombre() + " ha perdido su barco");
            font.drawString((container.getWidth() - ancho) / 2, 230, InfoJuego.getInstance().getJugador(InfoJuego.getInstance().getJugadorMinijuego1()).getNombre() + " ha perdido su barco", Color.green);
        } else if (lifePlayer2 <= 0) {
            ancho = font.getWidth(InfoJuego.getInstance().getJugador(InfoJuego.getInstance().getJugadorMinijuego2()).getNombre() + " ha perdido su barco");
            font.drawString((container.getWidth() - ancho) / 2, 230, InfoJuego.getInstance().getJugador(InfoJuego.getInstance().getJugadorMinijuego2()).getNombre() + " ha perdido su barco", Color.green);
        }
        String contenido = "Pulse espacio para continuar...";
        ancho = font.getWidth(contenido);
        font.drawString((container.getWidth() - ancho) / 2, 300, contenido, Color.gray);
    }

    public void choque() {
        choqueFx.play(1.0f, 0.4f);
    }

    public void reset() throws SlickException {
        entities.clear();
        System.gc();
        lifePlayer1 = 50;
        lifePlayer2 = 50;
        this.hit.reset();
        renderHit = false;
        gameOver = false;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
        this.player1win = true;
        this.player2win = true;
        musica.loop();
    }

    public void leave(GameContainer container, StateBasedGame game) {
        musica.stop();
        try {
            reset();
        } catch (SlickException e) {
            System.err.println(e.getMessage());
        }
        liberarRecursos();
    }

    public void pause() {
        font.drawString(350, 270, "Pause ", Color.pink);
    }

    public void keyPressed(int key, char c) {
        if (this.verInstrucciones) {
            if (key == Input.KEY_SPACE) {
                if (this.instrucciones.textoTotalmenteEscrito()) {
                    this.instrucciones.setEscribete(false);
                    verInstrucciones = false;
                } else this.instrucciones.setMsEntreLetras(10);
            }
        } else {
            if (key == Input.KEY_SPACE && (!container.isPaused()) && (gameOver)) {
                if (this.lifePlayer1 <= 0) this.player1win = false;
                if (this.lifePlayer2 <= 0) this.player2win = false;
                if (player1win) {
                    int OroP1 = InfoJuego.getInstance().getOroDelJugador(InfoJuego.getInstance().getJugadorMinijuego1());
                    OroP1 += lifePlayer1;
                    InfoJuego.getInstance().setOroJugador(InfoJuego.getInstance().getJugadorMinijuego1(), OroP1);
                } else {
                    int OroP2 = InfoJuego.getInstance().getOroDelJugador(InfoJuego.getInstance().getJugadorMinijuego2());
                    OroP2 += lifePlayer2;
                    InfoJuego.getInstance().setOroJugador(InfoJuego.getInstance().getJugadorMinijuego2(), OroP2);
                }
                cambiarEstado(GameStates.TABLERO);
            } else if (key == Input.KEY_ENTER) {
                if (!container.isPaused()) container.pause(); else container.resume();
            } else if (key == Input.KEY_ESCAPE) {
                if (!RecursosComunes.getInstance().mostrarConfirmacion()) System.exit(0);
            } else {
                for (int j = 0; j < this.player1Keys.length; j++) if ((player1Keys[j] == key) || (player2Keys[j] == key)) {
                    boolean encontrada = false;
                    int i = 0;
                    while (!encontrada && i < teclasPulsadas.length) {
                        if (this.teclasPulsadas[i] == key) {
                            encontrada = true;
                        }
                        i++;
                    }
                    if (!encontrada) {
                        int k = 0;
                        boolean hecha = false;
                        while (k < teclasPulsadas.length && !hecha) {
                            if (teclasPulsadas[k] == 0) {
                                teclasPulsadas[k] = key;
                                hecha = true;
                            }
                            k++;
                        }
                    }
                }
            }
        }
    }

    public void keyReleased(int key, char c) {
        if (this.verInstrucciones) {
            if (key == Input.KEY_SPACE) {
                if (this.instrucciones.textoTotalmenteEscrito()) {
                    this.instrucciones.setEscribete(false);
                    verInstrucciones = false;
                } else this.instrucciones.setMsEntreLetras(10);
            }
        } else {
            if (key == Input.KEY_SPACE && (!container.isPaused()) && (gameOver)) {
            } else if (key == Input.KEY_ENTER) {
            } else if (key == Input.KEY_ESCAPE) {
                if (!RecursosComunes.getInstance().mostrarConfirmacion()) System.exit(0);
            } else {
                for (int j = 0; j < this.player1Keys.length; j++) if ((player1Keys[j] == key) || (player2Keys[j] == key)) {
                    for (int i = 0; i < teclasPulsadas.length; i++) if (this.teclasPulsadas[i] == key) {
                        teclasPulsadas[i] = 0;
                    }
                }
            }
        }
    }

    public void cargarRecursos() throws SlickException {
        Calendar ahora1 = Calendar.getInstance();
        long tiempo1 = ahora1.getTimeInMillis();
        musica = new Music("recursos/minijuegos/barcos/BatallaBarcos.ogg", true);
        background = new Image("recursos/minijuegos/barcos/sea.png");
        font = new AngelCodeFont("recursos/fonts/scorefnt.fnt", "recursos/fonts/scorefnt.png");
        int[] player1Keys = { Input.KEY_A, Input.KEY_W, Input.KEY_D, Input.KEY_F, Input.KEY_G };
        player1 = new Player(1, 200, 300, "recursos/minijuegos/barcos/barcorojo.png", player1Keys);
        entities.add(player1);
        int[] player2Keys = { Input.KEY_LEFT, Input.KEY_UP, Input.KEY_RIGHT, Input.KEY_N, Input.KEY_M };
        player2 = new Player(2, 600, 300, "recursos/minijuegos/barcos/barcoazul.png", player2Keys);
        entities.add(player2);
        shotFx = new Sound("recursos/minijuegos/barcos/fire.ogg");
        explosionFx = new Sound("recursos/minijuegos/barcos/explosion_2.ogg");
        choqueFx = new Sound("recursos/minijuegos/barcos/choque.ogg");
        teclasPulsadas = new int[10];
        for (int i = 0; i < 10; i++) {
            teclasPulsadas[i] = 0;
        }
        try {
            hit = ParticleIO.loadConfiguredSystem("recursos/minijuegos/barcos/explosion.xml");
        } catch (IOException e) {
            System.err.println("Error al cargar el xml");
            e.printStackTrace();
        }
        gameOver = false;
        this.lifePlayer1 = VIDA_MAX;
        this.lifePlayer2 = VIDA_MAX;
        Calendar ahora2 = Calendar.getInstance();
        long tiempo2 = ahora2.getTimeInMillis();
        System.out.println("Cargando Juego Barcos: " + (tiempo2 - tiempo1) + " milisegundos");
    }

    public void liberarRecursos() {
        background = null;
        font = null;
        player1 = null;
        player2 = null;
        shotFx = null;
        explosionFx = null;
        choqueFx = null;
        hit = null;
        this.teclasPulsadas = null;
    }

    public void cambiarEstado(final int estado) {
        game.enterState(estado);
    }

    private void cargarInstrucciones() {
        FileReader fichIns = null;
        this.instrucciones = new TextoMaquina();
        try {
            fichIns = new FileReader("recursos//minijuegos//barcos//barcosinstrucciones.txt");
        } catch (FileNotFoundException e) {
            System.err.println("Error al cargar las instrucciones del juego Mecanograf�a");
            e.printStackTrace();
        }
        StringBuffer s = new StringBuffer();
        int character = 0;
        while (character != -1) {
            try {
                character = fichIns.read();
            } catch (IOException e) {
                System.err.println("Error al leer las instrucciones del juego Mecanograf�a");
                e.printStackTrace();
            }
            s.append((char) character);
        }
        this.instrucciones.setText(s.toString());
        this.instrucciones.reinicia();
    }
}
