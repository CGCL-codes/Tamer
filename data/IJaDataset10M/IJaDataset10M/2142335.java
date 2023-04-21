package com.gameloft.amru.clashofwar;

import javax.microedition.lcdui.game.GameCanvas;

/**
 * This game will have 3 screens. Those screens will share the same instance of
 * this canvas class to display their selves. The reason of the canvas sharing is
 * as stated in GameCanvas API: "Since a unique buffer is provided for each
 * GameCanvas instance, it is preferable to re-use a single GameCanvas instance
 * in the interests of minimizing heap usage."
 * @author amrullah
 */
public class ScreenHandler extends GameCanvas implements Runnable {

    private WelcomeScreen scrWelcome;

    private MenuScreen scrMenu;

    private GameScreen scrGame;

    private AbstractScreen currentScreen;

    private static ScreenHandler mySelf;

    private TheMIDlet midlet;

    private ScreenHandler(TheMIDlet m) {
        super(false);
        this.midlet = m;
        this.setFullScreenMode(true);
        scrWelcome = new WelcomeScreen(this, getGraphics());
        scrMenu = new MenuScreen(this, getGraphics());
        scrGame = new GameScreen(this, this.getGraphics());
        currentScreen = scrMenu;
    }

    public static ScreenHandler getInstance(TheMIDlet m) {
        if (mySelf == null) {
            mySelf = new ScreenHandler(m);
        }
        return mySelf;
    }

    protected void keyPressed(int keyCode) {
        int act = getGameAction(keyCode);
        currentScreen.keyPressed(act, keyCode);
        if (act == 0 && keyCode == 35) {
            stop();
            midlet.exitMIDlet();
        }
    }

    public void run() {
        scrWelcome.display();
        try {
            Thread.sleep(GameDesign.SPLASH_DELAY);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        scrMenu.paintScreen();
    }

    public void switchScreen(int menuIndex) {
        switch(menuIndex) {
            case 0:
                currentScreen = scrGame;
                scrGame.start();
                break;
            case 2:
                stop();
                midlet.exitMIDlet();
                break;
        }
    }

    public void stop() {
        currentScreen.stop();
        mySelf = null;
    }

    public void gameOver() {
        if (currentScreen instanceof GameScreen) {
            ((GameScreen) currentScreen).gameOver();
        }
    }
}
