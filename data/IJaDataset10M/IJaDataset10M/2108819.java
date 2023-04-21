package net.engine;

import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({ "ALL" })
public class GameRunnable implements Runnable {

    public static int NANOS_IN_MILLI = 1000000;

    protected volatile boolean running = false;

    protected List<GamePanel> gamePanels;

    protected Statistics statistics;

    protected long period;

    private Thread animator;

    public GameRunnable() {
        gamePanels = new ArrayList<GamePanel>();
        statistics = new Statistics();
        startGame();
    }

    public GameRunnable(GamePanel gamePanel) {
        this();
        addGamePanel(gamePanel);
    }

    public void addGamePanel(GamePanel gamePanel) {
        gamePanels.add(gamePanel);
        gamePanel.setRunnable(this);
    }

    public void run() {
        create();
        gameLoop();
        destroy();
    }

    private void create() {
        running = true;
    }

    private void startGame() {
        if (animator == null || !running) {
            animator = new Thread(this);
            animator.start();
        }
        period = 16 * NANOS_IN_MILLI;
    }

    public void stopGame() {
        running = false;
    }

    private void destroy() {
        System.exit(0);
    }

    private void gameLoop() {
        long overtime = 0;
        while (running) {
            long startTime = System.nanoTime();
            for (GamePanel gamePanel : gamePanels) {
                if (gamePanel.isEnabled()) {
                    if (gamePanel.abusedBuffer == null) {
                        gamePanel.createBackBuffer();
                    }
                    if (gamePanel.backBuffer != null) {
                        try {
                            gamePanel.update();
                            gamePanel.render();
                            gamePanel.paintScreen();
                        } catch (Throwable throwable) {
                            throw new RuntimeException("Gamepanel " + gamePanel.getClass().getSimpleName() + " threw and exception", throwable);
                        }
                    }
                }
            }
            overtime = sleep(startTime, overtime);
        }
    }

    private long sleep(long beforeTime, long overtime) {
        long afterTime = System.nanoTime();
        long timeDiff = afterTime - beforeTime;
        long sleepTime = (period - timeDiff) - overtime;
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime / NANOS_IN_MILLI);
            } catch (InterruptedException e) {
            }
            overtime = (System.nanoTime() - afterTime) - sleepTime;
        } else {
            Thread.yield();
            sleepTime = 0;
            overtime = 0;
        }
        statistics.setImmOvertime(overtime);
        statistics.setImmFrameTime(timeDiff);
        statistics.setImmSleepTime(sleepTime);
        return overtime;
    }
}
