package clickandlearn.conquis.minijuegos.relaciones;

import java.util.Timer;
import java.util.TimerTask;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import clickandlearn.conquis.singleplayer.GameStates;

public class PuntuacionState extends BasicGameState {

    private int Id = -1;

    @SuppressWarnings("unused")
    private GameContainer contenedor;

    private StateBasedGame estados;

    private Image fondo;

    private static AngelCodeFont font;

    private static int puntuacion;

    static Message message2;

    private class Message {

        public Message(String text, float pos) {
            this.text = text;
            this.pos = pos;
            width = font.getWidth(text);
        }

        public void setText(String t) {
            this.text = t;
        }

        public String text;

        public float pos;

        public float width;
    }

    public PuntuacionState(int id) {
        this.Id = id;
    }

    @Override
    public int getID() {
        return Id;
    }

    @Override
    public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
        this.contenedor = arg0;
        this.estados = arg1;
        this.cargarRecursos();
    }

    public void enter(GameContainer container, StateBasedGame game) {
        int delay = 5000;
        Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {

            public void run() {
                salir();
            }
        }, delay);
    }

    private void salir() {
        estados.enterState(GameStates.TABLERO);
    }

    static void setPuntuacion(int p) {
        puntuacion = p;
        message2.setText(((Integer) puntuacion).toString());
    }

    private void cargarRecursos() throws SlickException {
        fondo = new Image("recursos/minijuegos/Puntuacion.jpg");
        font = new AngelCodeFont("recursos/fonts/scorefnt.fnt", "recursos/fonts/scorefnt.png");
        message2 = new Message("0", font.getWidth(""));
    }

    @Override
    public void render(GameContainer arg0, StateBasedGame arg1, Graphics g) throws SlickException {
        fondo.draw(0, 0);
        font.drawString(290, 250, "Has conseguido\n\n   " + message2.text + " puntos.");
    }

    @Override
    public void update(GameContainer arg0, StateBasedGame arg1, int delta) throws SlickException {
    }

    public void leave(GameContainer container, StateBasedGame game) {
        liberarRecursos();
    }

    private void liberarRecursos() {
        fondo = null;
        font = null;
        message2 = null;
    }

    public void keyPressed(int key, char c) {
    }
}
