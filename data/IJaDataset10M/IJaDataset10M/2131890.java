package aitgame.tilegame.ingameDisplay;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Window;
import aitgame.graphics.ScreenManager;
import aitgame.input.InputManager;

public abstract class IngameDisplayBase {

    protected InputManager inputManager;

    protected boolean isRunning;

    protected int FONT_SIZE;

    protected Window window;

    protected ScreenManager screen;

    public IngameDisplayBase() {
    }

    ;

    public IngameDisplayBase(InputManager inputManager) {
        this.inputManager = inputManager;
    }

    public abstract void draw(Graphics2D g, int screenWidth, int screenHeight);

    public abstract void CheckInput(long elapsedTime);

    public void show(ScreenManager screen) {
        this.screen = screen;
        window = screen.getFullScreenWindow();
        FONT_SIZE = window.getFont().getSize();
        long startTime = System.currentTimeMillis();
        long currTime = startTime;
        isRunning = true;
        inputManager.setDETECT_INITAL_PRESS_ONLYBehavior();
        while (isRunning) {
            long elapsedTime = System.currentTimeMillis() - currTime;
            currTime += elapsedTime;
            CheckInput(elapsedTime);
            Graphics2D g = screen.getGraphics();
            draw(g, screen.getWidth(), screen.getHeight());
            g.dispose();
            screen.update();
            try {
                Thread.sleep(20);
            } catch (InterruptedException ex) {
            }
        }
        window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
        inputManager.setNormalBehavior();
    }
}
