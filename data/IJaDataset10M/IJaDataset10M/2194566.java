package org.pvs.superpalitos.sp_lite;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.pvs.palitos.IA;
import org.pvs.palitos.Jugador;
import org.pvs.palitos.Partida;
import org.pvs.palitos.PartidaAdapter;
import org.pvs.superpalitos.sp_lite.gui.MainPanel;
import org.pvs.superpalitos.sp_lite.gui.PrePartidaPanel;
import org.pvs.superpalitos.sp_lite.gui.TableroPanel;
import org.pvs.superpalitos.sp_lite.util.ColorUtil;

/**
 * Clase controlador
 *
 * @author Angel Luis Calvo Ortega
 */
public class SuperPalitos extends PartidaAdapter {

    private static final String TITLE = "SuperPalitos Lite";

    public static final int WIDTH = 400;

    public static final int HEIGHT = 300;

    public static final Dimension SIZE = new Dimension(WIDTH, HEIGHT);

    private static final int STATE_STANDBY = 0;

    private static final int STATE_PREPLAYING = 1;

    private static final int STATE_PLAYING = 2;

    private static final int STATE_ENDED = 3;

    private static final String PANEL_MAIN = "main";

    private static final String PANEL_PRE_PLAY = "option";

    private static final String PANEL_PLAY = "tablero";

    public static final int DIFICULTAD_DEFECTO = IA.NORMAL;

    public static final String[] DIFICULTADES = { "Fácil", "Normal", "Difícil" };

    private static final int BASE_DELAY = 500;

    private static final int GAP_DELAY = 250;

    public static final String ABOUT = "<html><h2>Super Palitos Lite</h2>" + "Version 3.2<br/>" + "Pollo Verde Software 2010<br/>" + "Este programa es software libre y se distribuye bajo la licencia GPL 2." + "</html>";

    private final int[] marcador = new int[2];

    private Jugador j1, j2;

    private static SuperPalitos instance;

    private int state;

    private JFrame frame;

    private TableroPanel tablero;

    private CardLayout cardLayout;

    private Random random;

    public SuperPalitos() {
        state = STATE_STANDBY;
        random = new Random(System.nanoTime());
        resetMarcador();
    }

    private void resetMarcador() {
        marcador[0] = 0;
        marcador[1] = 0;
    }

    private void marcar(Jugador j) {
        marcador[j == j1 ? 1 : 0]++;
    }

    public static SuperPalitos getInstance() {
        if (instance == null) {
            instance = new SuperPalitos();
        }
        return instance;
    }

    protected void start() {
        frame = new JFrame(TITLE);
        tablero = new TableroPanel();
        cardLayout = new CardLayout();
        frame.setLayout(cardLayout);
        MainPanel p = new MainPanel();
        frame.add(p, PANEL_MAIN);
        frame.add(new PrePartidaPanel(), PANEL_PRE_PLAY);
        frame.add(tablero, PANEL_PLAY);
        cardLayout.show(frame.getContentPane(), PANEL_MAIN);
        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/sp.png"));
        frame.setIconImage(icon.getImage());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.pack();
        centerOnScreen(frame);
    }

    public void prePlay() {
        if (state == STATE_STANDBY) {
            cardLayout.show(frame.getContentPane(), PANEL_PRE_PLAY);
            state = STATE_PREPLAYING;
        }
    }

    public void play() {
        play(DIFICULTAD_DEFECTO, ColorUtil.COLOR_DEFECTO);
    }

    public void play(int dificultad, int color) {
        if (state == STATE_PREPLAYING) {
            j1 = tablero.createJugador("Jugador", color);
            j2 = new IA(dificultad, ColorUtil.NEGRO);
            tablero.setMarcador(marcador[0], marcador[1]);
            Partida partida = new Partida(j1, j2, tablero, true);
            cardLayout.show(frame.getContentPane(), PANEL_PLAY);
            partida.addPartidaListener(this);
            partida.start();
            state = STATE_PLAYING;
        } else if (state == STATE_ENDED) {
            Partida partida = new Partida(j1, j2, tablero, true);
            partida.addPartidaListener(this);
            partida.start();
            state = STATE_PLAYING;
        }
    }

    public void terminar() {
        cardLayout.show(frame.getContentPane(), PANEL_MAIN);
        state = STATE_STANDBY;
        resetMarcador();
    }

    @Override
    public void finPartida(Jugador winner) {
        marcar(winner);
        tablero.setMarcador(marcador[0], marcador[1]);
        state = STATE_ENDED;
    }

    @Override
    public void cambiaTurno(Jugador j) {
        if (j instanceof IA) {
            int r = random.nextInt(6);
            try {
                Thread.sleep(BASE_DELAY + r * GAP_DELAY);
            } catch (InterruptedException e) {
            }
        }
    }

    public void back() {
        if (state == STATE_PREPLAYING) {
            cardLayout.show(frame.getContentPane(), PANEL_MAIN);
            state = STATE_STANDBY;
        }
    }

    public void exit() {
        frame.dispose();
        System.exit(0);
    }

    public boolean isSound() {
        return true;
    }

    private static void centerOnScreen(JFrame w) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) (screenSize.getWidth() - w.getWidth()) / 2;
        int y = (int) (screenSize.getHeight() - w.getHeight()) / 2;
        w.setLocation(x, y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                getInstance().start();
            }
        });
    }
}
