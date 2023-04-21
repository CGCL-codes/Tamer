package org.chernovia.games.arcade.funkball;

import java.applet.Applet;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import org.chernovia.lib.sims.ca.CA_Listener;
import org.chernovia.lib.sims.ca.fishbowl.FishBowl2D;

public class FunkBallApplet extends Applet implements CA_Listener, KeyListener {

    private static final long serialVersionUID = 6912652978939899772L;

    public static final String RES_DIR = "/res/funkball/", DOT_IMG = "dot", MOB_IMG = "mob", AVAT_IMG = "avat", IMG_SFX = ".jpg";

    public static final int ARENA_X = 800, ARENA_Y = 600, NUM_DOTS = 100;

    public int REFRESH_RATE = 10, DELAY = 125;

    private FishBowl2D bowl;

    private Arena arena;

    @Override
    public void init() {
        addKeyListener(this);
        setSize(800, 800);
        setBackground(Color.green);
        bowl = new FishBowl2D(8, 8, 16, 1);
        bowl.addListener(this);
        arena = new Arena(ARENA_X, ARENA_Y, NUM_DOTS, bowl);
        arena.setBackground(Color.black);
        arena.addKeyListener(arena);
        add(arena);
        add(arena.statFld);
    }

    @Override
    public void start() {
        bowl.start();
    }

    public void startingRun() {
    }

    public void nextTick(int t) {
        arena.movePlayer();
        arena.moveMobs();
        if (t % REFRESH_RATE == 0) {
            arena.repaint();
            try {
                bowl.sleep(DELAY);
            } catch (InterruptedException ignore) {
            }
        }
    }

    public void finishedRun() {
    }

    public void resize(int dim) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }
}
