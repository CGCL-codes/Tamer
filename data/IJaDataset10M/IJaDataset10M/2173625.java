package clickandlearn.conquis.minijuegos.trivial;

import java.util.Calendar;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import clickandlearn.conquis.comun.CargaDinamica;
import clickandlearn.conquis.comun.RecursosComunes;
import clickandlearn.conquis.singleplayer.GameStates;
import clickandlearn.conquis.tablero.InfoJuego;

/**
 * 
 * @author Los Conquis - Click & Learn
 * 
 */
public class MainMenuState extends BasicGameState implements CargaDinamica {

    int stateID = 0;

    private StateBasedGame game;

    @SuppressWarnings("unused")
    private GameContainer container;

    boolean cargando = true;

    Image background = null;

    Image logo = null;

    public static AngelCodeFont font;

    private float scrollPos = 700;

    private float logoScale;

    private int logoDir = 1;

    Message message;

    /**
	 * Constructor de la clase MainMenuState.
	 * 
	 * @param stateID
	 * @throws SlickException
	 */
    public MainMenuState(int stateID) {
        this.stateID = stateID;
    }

    @Override
    public int getID() {
        return stateID;
    }

    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.cargarRecursos();
        this.game = game;
        this.container = container;
    }

    public void render(GameContainer container, StateBasedGame game, Graphics g) {
        if (!cargando) {
            background.draw(0, 0, container.getWidth(), container.getHeight());
            logo.draw(400 - ((logo.getWidth() / 2) * logoScale), 200 - ((logo.getHeight() / 2) * logoScale), logo.getWidth() * logoScale, logo.getHeight() * logoScale);
            g.setColor(new Color(0, 0, 0, 0.2f));
            g.fillRect(-10, 480, 850, 40);
            g.setColor(Color.white);
            float x = scrollPos + message.pos;
            if ((x < 800) && (x + message.width > 0)) {
                font.drawString(x, 480, message.text);
            }
            if ((scrollPos < -450)) {
                scrollPos = 800;
            }
        }
    }

    public void update(GameContainer container, StateBasedGame game, int delta) {
        if (!cargando) {
            logoScale += delta * 0.0003f * logoDir;
            if (logoScale < 0.9) {
                logoScale += delta * 0.002f * logoDir;
            }
            if (logoScale > 1.05f) {
                logoDir = -1;
            }
            if (logoScale < 0.95f) {
                logoDir = 1;
            }
            scrollPos -= delta * 0.1f;
        }
    }

    public void keyPressed(int key, char c) {
        if (key == Input.KEY_ESCAPE) {
            if (!RecursosComunes.getInstance().mostrarConfirmacion()) System.exit(0);
        } else if (key == Input.KEY_SPACE) {
            if (InfoJuego.getInstance().getModoJuego() == InfoJuego.ModoJuego.Local) game.enterState(clickandlearn.conquis.singleplayer.GameStates.TRIVIAL, null, null); else game.enterState(clickandlearn.conquis.multiplayer.GameStates.TRIVIAL, null, null);
        }
    }

    private class Message {

        public Message(String text, float pos) {
            this.text = text;
            this.pos = pos;
            width = font.getWidth(text);
        }

        public String text;

        public float pos;

        public float width;
    }

    public void cargarRecursos() throws SlickException {
        cargando = true;
        Calendar ahora1 = Calendar.getInstance();
        long tiempo1 = ahora1.getTimeInMillis();
        background = new Image("recursos/minijuegos/trivial/fondoIntro3.png");
        logo = new Image("recursos/minijuegos/trivial/IntroTrivial.png");
        font = new AngelCodeFont("recursos/fonts/scorefnt.fnt", "recursos/fonts/scorefnt.png");
        message = new Message("Pulsa ESPACIO para empezar.", font.getWidth(""));
        Calendar ahora2 = Calendar.getInstance();
        long tiempo2 = ahora2.getTimeInMillis();
        System.out.println("Cargando Menu Trivial: " + (tiempo2 - tiempo1) + " milisegundos");
        cargando = false;
    }

    public void liberarRecursos() {
        cargando = true;
        background = null;
        logo = null;
        font = null;
        message = null;
    }

    @Override
    public void enter(GameContainer container, StateBasedGame game) {
    }

    @Override
    public void leave(GameContainer container, StateBasedGame game) {
    }
}
