package clickandlearn.conquis.menus;

import java.awt.Point;
import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import clickandlearn.conquis.comun.Boton;
import clickandlearn.conquis.comun.Sprite;
import clickandlearn.conquis.red.InfoRed;
import clickandlearn.conquis.singleplayer.GameStates;
import clickandlearn.conquis.tablero.EstadoCargarGuardar;
import clickandlearn.conquis.tablero.InfoJuego;

/**
 * author Click&Learn -Los Conquis
 * 
 * Clase que implementa el men� principal de la aplicaci�n
 */
public class MenuPrincipal extends BasicGameState {

    private int stateID = -1;

    private Boton Bopciones, BSalir, Bcreditos, BColon, BPizarro, BMagallanes, BCortes;

    private Image fondo;

    private Sprite titulo, barquitoGato;

    private GameContainer contenedorJuego;

    private StateBasedGame estadosJuego;

    private Sound soundClick;

    private Point posMouse;

    private Music musicaFondo;

    private EstadoCargarGuardar estadoCargarGuardar;

    /**
	 * texto/fuente de la instruccion de qu� se debe hacer del menu principal
	 */
    private AngelCodeFont font;

    /**
	 * texto/fuente del nombre de Colon del menu principal
	 */
    private AngelCodeFont Colon;

    /**
	 * booleano para que aparezca el nombre de Colon cuando se pasa el raton
	 * encima de la imagen del conqui Colon
	 */
    private boolean pintaColon = false;

    /**
	 * texto/fuente del nombre de Pizarro del menu principal
	 */
    private AngelCodeFont Pizarro;

    /**
	 * booleano para que aparezca el nombre de Pizarro cuando se pasa el raton
	 * encima de la imagen del conqui Pizarro
	 */
    private boolean pintaPizarro = false;

    /**
	 * texto/fuente del nombre de Magallanes del menu principal
	 */
    private AngelCodeFont Magallanes;

    /**
	 * booleano para que aparezca el nombre de Magallanes cuando se pasa el
	 * raton encima de la imagen del conqui Magallanes
	 */
    private boolean pintaMagallanes = false;

    /**
	 *texto/fuente del nombre de Cortes del menu principal
	 */
    private AngelCodeFont Cortes;

    /**
	 * booleano para que aparezca el nombre de Cortes cuando se pasa el raton
	 * encima de la imagen del conqui Cortes
	 */
    private boolean pintaCortes = false;

    /**
	 * Constructora de la clase
	 * 
	 * @param Id
	 *            Identificador de la clase
	 */
    public MenuPrincipal(int Id) {
        this.stateID = Id;
    }

    /**
	 * M�todo que devuelve el identificador de la clase
	 * 
	 * @return El identificador de la clase
	 */
    @Override
    public int getID() {
        return this.stateID;
    }

    /**
	 * M�todo que inicializa los atributos de la clase
	 * 
	 * @param container
	 *            Clase de la ventana del juego
	 * @param game
	 *            Clase que tiene la coleccion de los estados
	 * @throws SlickException
	 *             Excepcion de la libreria Slick
	 */
    @Override
    public void init(GameContainer container, StateBasedGame game) throws SlickException {
        this.fondo = new Image("recursos/menuprincipal/fondoMuestra.jpg");
        this.BSalir = new Boton("recursos/menuprincipal/canionMuestra.png", new Point(300, 150), "Salir");
        this.Bopciones = new Boton("recursos/menuprincipal/pergaminoMuestra.png", new Point(150, 150), "Cargar Partida");
        this.Bcreditos = new Boton("recursos/menuprincipal/garfioMuestra.png", new Point(540, 150), "Configurar");
        this.contenedorJuego = container;
        this.estadosJuego = game;
        this.titulo = new Sprite("recursos/menuprincipal/titulo.png", new Point(-100, -200));
        this.soundClick = new Sound("recursos/menuprincipal/ballesta.ogg");
        this.BPizarro = new Boton("recursos/menuprincipal/pizarroSolo.png", new Point(), "Pizarro");
        this.BCortes = new Boton("recursos/menuprincipal/HernanSolo.png", new Point(), "Cort�s");
        this.BColon = new Boton("recursos/menuprincipal/ColonSolo.png", new Point(), "Col�n");
        this.BMagallanes = new Boton("recursos/menuprincipal/magallanesSolo.png", new Point(), "Magallanes");
        this.BPizarro.setPosicion(new Point(100, container.getHeight() - this.BPizarro.getAlto()));
        this.BCortes.setPosicion(new Point(this.BPizarro.getPosicion().x + this.BPizarro.getAncho(), container.getHeight() - this.BCortes.getAlto()));
        this.barquitoGato = new Sprite("recursos/menuprincipal/barquito.png", new Point(-30, -30));
        this.BColon.setPosicion(new Point(this.BCortes.getPosicion().x + this.BCortes.getAncho(), container.getHeight() - this.BColon.getAlto()));
        this.BMagallanes.setPosicion(new Point(this.BColon.getPosicion().x + this.BColon.getAncho(), container.getHeight() - this.BMagallanes.getAlto()));
        this.posMouse = new Point();
        this.musicaFondo = new Music("recursos/menuprincipal/eleconqui.ogg", true);
        try {
            font = new AngelCodeFont("recursos/fonts/raps.fnt", "recursos/fonts/raps_0.png");
            Colon = font;
            Pizarro = font;
            Magallanes = font;
            Cortes = font;
        } catch (SlickException e) {
            System.err.println(e.getMessage());
        }
        estadoCargarGuardar = new EstadoCargarGuardar();
    }

    /**
	 * M�todo que actualiza la vista a la velocidad indicada en la clase
	 * principal que son 60frames por segundo
	 * 
	 * @param container
	 *            Clase de la ventana del juego
	 * @param game
	 *            Clase que tiene la coleccion de los estados
	 * @param g
	 *            Clase para la parte grafica entera
	 * @throws SlickException
	 *             Exception Excepcion de la libreria Slick
	 */
    @Override
    public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
        this.fondo.draw();
        this.titulo.pinta();
        this.BSalir.pinta();
        this.Bopciones.pinta();
        this.Bcreditos.pinta();
        Color col;
        col = new Color(0, 0, 0, 125);
        this.BPizarro.getImagen().drawFlash(this.BPizarro.getPosicion().x + 7, this.BPizarro.getPosicion().y - 3, this.BPizarro.getAncho(), this.BPizarro.getAlto(), col);
        this.BPizarro.pinta();
        this.BCortes.getImagen().drawFlash(this.BCortes.getPosicion().x + 7, this.BCortes.getPosicion().y - 3, this.BCortes.getAncho(), this.BCortes.getAlto(), col);
        this.BCortes.pinta();
        this.BColon.getImagen().drawFlash(this.BColon.getPosicion().x + 7, this.BColon.getPosicion().y - 3, this.BColon.getAncho(), this.BColon.getAlto(), col);
        this.BColon.pinta();
        this.BMagallanes.getImagen().drawFlash(this.BMagallanes.getPosicion().x + 7, this.BMagallanes.getPosicion().y - 3, this.BMagallanes.getAncho(), this.BMagallanes.getAlto(), col);
        this.BMagallanes.pinta();
        font.drawString(150, 300, "Haz click encima de un conqui", Color.orange);
        if (pintaColon) {
            Colon.drawString(BColon.getCentro().x - 60, BColon.getCentro().y + 80, "Colon", Color.orange);
        }
        if (pintaPizarro) {
            Pizarro.drawString(BPizarro.getCentro().x - 60, BPizarro.getCentro().y + 80, "Pizarro", Color.orange);
        }
        if (pintaMagallanes) {
            Magallanes.drawString(BMagallanes.getCentro().x - 60, BMagallanes.getCentro().y + 80, "Magallanes", Color.orange);
        }
        if (pintaCortes) {
            Cortes.drawString(BCortes.getCentro().x - 60, BCortes.getCentro().y + 80, "Cortes", Color.orange);
        }
        this.barquitoGato.pinta();
    }

    /**
	 * M�todo que actualiza la l�gica de la clase
	 * 
	 * @param container
	 *            Clase de la ventana del juego
	 * @param game
	 *            Clase que tiene la coleccion de los estados
	 * @param delta
	 *            Intervalo de tiempo entre update y update
	 * @throws SlickException
	 *             Exception Excepcion de la libreria Slick
	 */
    @Override
    public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
        this.titulo.acercarseCentro(new Point(container.getWidth() / 2, 30 + titulo.getHeight() / 2));
        this.barquitoGato.acercarseCentroNav(posMouse);
        Input entrada = container.getInput();
        if (this.BSalir.interseccionaConPunto(new Point(entrada.getMouseX(), entrada.getMouseY()))) {
            if (this.BSalir.getImagen().getColor(entrada.getMouseX() - BSalir.getPosicion().x, entrada.getMouseY() - BSalir.getPosicion().y).getAlpha() != 0) {
                this.BSalir.setPintaCaption(true);
            }
        } else {
            this.BSalir.setPintaCaption(false);
        }
        if (this.Bopciones.interseccionaConPunto(new Point(entrada.getMouseX(), entrada.getMouseY()))) {
            if (this.Bopciones.getImagen().getColor(entrada.getMouseX() - Bopciones.getPosicion().x, entrada.getMouseY() - Bopciones.getPosicion().y).getAlpha() != 0) {
                this.Bopciones.setPintaCaption(true);
            }
        } else {
            this.Bopciones.setPintaCaption(false);
        }
        if (this.Bcreditos.interseccionaConPunto(new Point(entrada.getMouseX(), entrada.getMouseY()))) {
            if (this.Bcreditos.getImagen().getColor(entrada.getMouseX() - Bcreditos.getPosicion().x, entrada.getMouseY() - Bcreditos.getPosicion().y).getAlpha() != 0) {
                this.Bcreditos.setPintaCaption(true);
            }
        } else {
            this.Bcreditos.setPintaCaption(false);
        }
        if (this.BColon.interseccionaConPunto(new Point(entrada.getMouseX(), entrada.getMouseY()))) {
            if (this.BColon.getImagen().getColor(entrada.getMouseX() - BColon.getPosicion().x, entrada.getMouseY() - BColon.getPosicion().y).getAlpha() != 0) {
                pintaColon = true;
            }
        } else {
            pintaColon = false;
        }
        if (this.BPizarro.interseccionaConPunto(new Point(entrada.getMouseX(), entrada.getMouseY()))) {
            if (this.BPizarro.getImagen().getColor(entrada.getMouseX() - BPizarro.getPosicion().x, entrada.getMouseY() - BPizarro.getPosicion().y).getAlpha() != 0) {
                pintaPizarro = true;
            }
        } else {
            pintaPizarro = false;
        }
        if (this.BCortes.interseccionaConPunto(new Point(entrada.getMouseX(), entrada.getMouseY()))) {
            if (this.BCortes.getImagen().getColor(entrada.getMouseX() - BCortes.getPosicion().x, entrada.getMouseY() - BCortes.getPosicion().y).getAlpha() != 0) {
                pintaCortes = true;
            }
        } else {
            pintaCortes = false;
        }
        if (this.BMagallanes.interseccionaConPunto(new Point(entrada.getMouseX(), entrada.getMouseY()))) {
            if (this.BMagallanes.getImagen().getColor(entrada.getMouseX() - BMagallanes.getPosicion().x, entrada.getMouseY() - BMagallanes.getPosicion().y).getAlpha() != 0) {
                pintaMagallanes = true;
            }
        } else {
            pintaMagallanes = false;
        }
    }

    /**
	 * M�todo que se ejecuta cuando mueves el rat�n
	 * 
	 * @param oldx
	 *            coordenada x antigua
	 * @param oldy
	 *            coordenada y antigua
	 * @param newx
	 *            coordenada x nueva
	 * @param newy
	 *            coordenada y nueva
	 */
    public void mouseMoved(int oldx, int oldy, int newx, int newy) {
        this.posMouse.x = newx;
        this.posMouse.y = newy;
    }

    /**
	 * M�todo que se ejecuta cuando pulsas el bot�n izquierdo del rat�n
	 * 
	 * @param button
	 *            identificador del bot�n del rat�n
	 * @param x
	 *            La coordenada X donde est� el puntero del rat�n en la pantalla
	 *            cuando se suelta el bot�n
	 * @param y
	 *            La coordenada Y donde est� el puntero del rat�n en la pantalla
	 *            cuando se suelta el bot�n
	 */
    public void mousePressed(int button, int x, int y) {
        if (this.BSalir.interseccionaConPunto(new Point(x, y))) if (this.BSalir.getImagen().getColor(x - BSalir.getPosicion().x, y - BSalir.getPosicion().y).getAlpha() != 0) {
            this.contenedorJuego.exit();
        }
        if (this.Bopciones.interseccionaConPunto(new Point(x, y))) if (this.Bopciones.getImagen().getColor(x - Bopciones.getPosicion().x, y - Bopciones.getPosicion().y).getAlpha() != 0) {
            if (InfoJuego.getInstance().getModoJuego() == InfoJuego.ModoJuego.Local && estadoCargarGuardar.cargar()) {
                try {
                    estadosJuego.getState(GameStates.TABLERO).init(contenedorJuego, estadosJuego);
                } catch (SlickException e) {
                    System.err.println("Error creando el tablero");
                    System.err.println(e.getMessage());
                    e.printStackTrace();
                }
                while (this.soundClick.playing()) ;
                this.estadosJuego.enterState(clickandlearn.conquis.multiplayer.GameStates.TABLERO);
            }
        }
        if (this.Bcreditos.interseccionaConPunto(new Point(x, y))) if (this.Bcreditos.getImagen().getColor(x - Bcreditos.getPosicion().x, y - Bcreditos.getPosicion().y).getAlpha() != 0) {
            this.estadosJuego.enterState(GameStates.MENU_CONFIGURACION);
        }
        if (this.BColon.interseccionaConPunto(new Point(x, y))) if (this.BColon.getImagen().getColor(x - BColon.getPosicion().x, y - BColon.getPosicion().y).getAlpha() != 0) {
            this.soundClick.play();
            InfoJuego.getInstance().setRutaJuego(0);
            try {
                estadosJuego.getState(GameStates.TABLERO).init(contenedorJuego, estadosJuego);
            } catch (SlickException e) {
                System.err.println("Error creando el tablero de Colon");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            while (this.soundClick.playing()) ;
            if (InfoJuego.getInstance().getModoJuego() == InfoJuego.ModoJuego.Local) {
                this.estadosJuego.enterState(clickandlearn.conquis.singleplayer.GameStates.MENU_PLAYERS);
            } else {
                InfoRed.getInstance().getCliente().setRuta();
                this.estadosJuego.enterState(clickandlearn.conquis.multiplayer.GameStates.TABLERO);
            }
        }
        if (this.BMagallanes.interseccionaConPunto(new Point(x, y))) if (this.BMagallanes.getImagen().getColor(x - BMagallanes.getPosicion().x, y - BMagallanes.getPosicion().y).getAlpha() != 0) {
            this.soundClick.play();
            InfoJuego.getInstance().setRutaJuego(1);
            try {
                estadosJuego.getState(GameStates.TABLERO).init(contenedorJuego, estadosJuego);
            } catch (SlickException e) {
                System.err.println("Error creando el tablero de Magallanes");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            while (this.soundClick.playing()) ;
            if (InfoJuego.getInstance().getModoJuego() == InfoJuego.ModoJuego.Local) {
                this.estadosJuego.enterState(clickandlearn.conquis.singleplayer.GameStates.MENU_PLAYERS);
            } else {
                InfoRed.getInstance().getCliente().setRuta();
                this.estadosJuego.enterState(clickandlearn.conquis.multiplayer.GameStates.TABLERO);
            }
        }
        if (this.BPizarro.interseccionaConPunto(new Point(x, y))) if (this.BPizarro.getImagen().getColor(x - BPizarro.getPosicion().x, y - BPizarro.getPosicion().y).getAlpha() != 0) {
            this.soundClick.play();
            InfoJuego.getInstance().setRutaJuego(2);
            try {
                estadosJuego.getState(GameStates.TABLERO).init(contenedorJuego, estadosJuego);
            } catch (SlickException e) {
                System.err.println("Error creando el tablero de Pizarro");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            while (this.soundClick.playing()) ;
            if (InfoJuego.getInstance().getModoJuego() == InfoJuego.ModoJuego.Local) {
                this.estadosJuego.enterState(clickandlearn.conquis.singleplayer.GameStates.MENU_PLAYERS);
            } else {
                InfoRed.getInstance().getCliente().setRuta();
                this.estadosJuego.enterState(clickandlearn.conquis.multiplayer.GameStates.TABLERO);
            }
        }
        if (this.BCortes.interseccionaConPunto(new Point(x, y))) if (this.BCortes.getImagen().getColor(x - BCortes.getPosicion().x, y - BCortes.getPosicion().y).getAlpha() != 0) {
            this.soundClick.play();
            InfoJuego.getInstance().setRutaJuego(3);
            try {
                estadosJuego.getState(GameStates.TABLERO).init(contenedorJuego, estadosJuego);
            } catch (SlickException e) {
                System.err.println("Error creando el tablero de Cortes");
                System.err.println(e.getMessage());
                e.printStackTrace();
            }
            while (this.soundClick.playing()) ;
            if (InfoJuego.getInstance().getModoJuego() == InfoJuego.ModoJuego.Local) {
                this.estadosJuego.enterState(clickandlearn.conquis.singleplayer.GameStates.MENU_PLAYERS);
            } else {
                InfoRed.getInstance().getCliente().setRuta();
                this.estadosJuego.enterState(clickandlearn.conquis.multiplayer.GameStates.TABLERO);
            }
        }
    }

    /**
	 * M�todo que reinicia el estado para cada vez que vuelvas a entrar
	 * 
	 * @param container
	 *            Ventana de juego
	 * @param game
	 *            Coleccion de estados
	 */
    public void enter(GameContainer container, StateBasedGame game) {
        if (!this.musicaFondo.playing()) {
            this.musicaFondo.play();
        }
    }

    public void leave(GameContainer container, StateBasedGame game) {
        if (this.musicaFondo.playing()) this.musicaFondo.stop();
    }
}
