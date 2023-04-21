package easyplay.breakout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BreakoutGame extends MouseMotionAdapter implements ActionListener {

    protected static final int WIDTH = 300;

    protected static final int HEIGHT = 400;

    protected static final int BLOCKSIZE = 20;

    protected static final int BLOCKROWS = 10;

    protected static final int BLOCKCOLS = 15;

    protected final int BLOCKS = 150;

    private final int PADDLEWIDTH = 20;

    private final Color COLOURS[] = new Color[] { new Color(32, 53, 144), new Color(134, 166, 248) };

    private int delay;

    private boolean[][] s;

    protected int lives;

    protected int score;

    private int level;

    private int left;

    private int currentX;

    private int currentY;

    private int direction;

    private int movement;

    private int paddleX;

    private int lastHit;

    private Timer timer;

    private Screen frame;

    private JLabel status;

    public BreakoutGame() {
        set();
    }

    protected void setScreen(Screen f) {
        frame = f;
    }

    protected void start() {
        timer = new Timer(delay, this);
        timer.start();
    }

    protected void stop() {
        timer.stop();
    }

    protected void restart() {
        timer = new Timer(delay, this);
        timer.start();
    }

    protected void setDifficulty(int dif) {
        delay = dif;
    }

    protected void set() {
        level = 1;
        initScreen();
        lives = 3;
        score = 0;
        left = BreakoutLevels.level1size;
        currentX = WIDTH / 2;
        currentY = HEIGHT;
        movement = -1;
        direction = 1;
        paddleX = WIDTH / 2;
        lastHit = 0;
    }

    protected void setStatus(JLabel s) {
        status = s;
    }

    protected void updateStatus() {
        status.setText("Score: " + score + " Lives: " + lives);
    }

    protected void updateStatus(String s) {
        status.setText(s);
    }

    private synchronized void blockHit(int x, int y) {
        if (s[y][x] == true) {
            int row = (int) Math.floor(y / BLOCKSIZE);
            int col = (int) Math.floor(x / BLOCKSIZE);
            int r = row * BLOCKSIZE;
            int c = col * BLOCKSIZE;
            int i = r;
            int n;
            while (i < r + 20) {
                n = c;
                while (n < c + 20) {
                    s[i][n] = false;
                    n++;
                }
                i++;
            }
            if ((c != 0) && (s[r][c - 1]) == false) direction = direction * -1; else if ((c != WIDTH) && (s[r][c + 1] == false)) direction = direction * -1; else movement = movement * -1;
            score++;
            updateStatus();
            left--;
            lastHit = 0;
        } else {
            lastHit++;
            if (lastHit == 2000) {
                direction = direction * -1;
                lastHit = 0;
            }
        }
        notifyAll();
    }

    protected synchronized void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, BLOCKROWS * BLOCKSIZE, WIDTH, ((HEIGHT + 10) - (BLOCKROWS * BLOCKSIZE)));
        int y = 0;
        int x = 0;
        int n = 0;
        while (y < (BLOCKROWS * BLOCKSIZE)) {
            while (x < WIDTH) {
                if (s[y][x]) {
                    g.setColor(COLOURS[n % 2]);
                    g.fillRect(x, y, BLOCKSIZE, BLOCKSIZE);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(x, y, BLOCKSIZE, BLOCKSIZE);
                }
                x = x + BLOCKSIZE;
                n++;
            }
            y = y + BLOCKSIZE;
            x = 0;
        }
        g.setColor(Color.WHITE);
        g.fillOval(currentX, currentY, 7, 7);
        g.fillRect(paddleX - PADDLEWIDTH, HEIGHT, PADDLEWIDTH * 2, 10);
        notifyAll();
    }

    private synchronized void initScreen() {
        movement = -1;
        direction = 1;
        paddleX = WIDTH / 2;
        lastHit = 0;
        if (level == 1) {
            s = BreakoutLevels.level1();
            left = BreakoutLevels.level1size;
        } else if (level == 2) {
            s = BreakoutLevels.level2();
            left = BreakoutLevels.level2size;
        } else {
            s = BreakoutLevels.randomlevel();
            left = BreakoutLevels.randomlevelsize;
        }
        notifyAll();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            currentX = currentX + direction;
            currentY = currentY + movement;
            if (left == 0) {
                level++;
                initScreen();
            } else if (currentY == 0) movement = 1; else if (currentY == HEIGHT - 1) {
                if ((currentX <= paddleX + PADDLEWIDTH) && (currentX >= paddleX - PADDLEWIDTH)) {
                    movement = -1;
                    if ((currentX <= paddleX + PADDLEWIDTH) && (currentX > paddleX)) direction = 1; else if ((currentX > paddleX - PADDLEWIDTH) && (currentX < paddleX)) direction = -1;
                } else {
                    lives--;
                    updateStatus();
                    if (lives == 0) {
                        timer.stop();
                        updateStatus("Game Over, you scored " + score);
                    }
                    currentX = paddleX;
                    movement = -1;
                    direction = 1;
                }
            } else if (currentX == 0) direction = 1; else if (currentX == WIDTH - 1) direction = -1;
            blockHit(currentX, currentY);
            frame.redraw();
        }
    }

    public void mouseMoved(MouseEvent e) {
        paddleX = e.getX();
    }
}
