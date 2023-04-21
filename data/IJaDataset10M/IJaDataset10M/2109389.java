package problem9;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Board extends JPanel {

    public Board() {
        setPreferredSize(new Dimension(500, 550));
        this.addMouseListener(new MyMouseListener());
        cnt = 0;
        start = false;
        calculateSize();
    }

    public void restart() {
        stop();
        vx = vy = -1;
        cnt = 0;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                board[i][j] = false;
                color[i][j] = Color.BLACK;
            }
        }
        drawBuffer();
        repaint();
        start = true;
        startTime = Calendar.getInstance().getTime();
        task = new TimerTask() {

            @Override
            public void run() {
                passedTime = Calendar.getInstance().getTime().getTime() - startTime.getTime();
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                    }
                });
            }
        };
        timer.schedule(task, Calendar.getInstance().getTime(), 500);
    }

    public void stop() {
        if (!start) {
            return;
        }
        task.cancel();
        timer.purge();
        start = false;
    }

    public void paint(Graphics g) {
        if (buffer == null || (buffer.getWidth(null) != this.getWidth() && buffer.getHeight(null) != this.getHeight())) {
            buffer = this.createImage(this.getWidth(), this.getHeight());
            drawBuffer();
        }
        g.drawImage(buffer, 0, 0, null);
    }

    /**
	 * Create a result.
	 * @param c
	 * @return
	 */
    public boolean getResult() {
        stop();
        return getResultHelp(0);
    }

    private boolean getResultHelp(int c) {
        if (c == 8) {
            drawBuffer();
            return true;
        }
        if (hasPut(c)) {
            return getResultHelp(c + 1);
        }
        for (int i = 0; i < 8; ++i) {
            if (check(c, i)) {
                board[c][i] = true;
                if (getResultHelp(c + 1)) {
                    return true;
                }
                board[c][i] = false;
            }
        }
        return false;
    }

    /**
	 * Judge whether column c has be put a queue
	 * @param c
	 * @return
	 */
    private boolean hasPut(int c) {
        for (int i = 0; i < 8; ++i) {
            if (board[c][i]) {
                return true;
            }
        }
        return false;
    }

    private void drawBuffer() {
        calculateSize();
        Graphics2D g2d = (Graphics2D) buffer.getGraphics();
        g2d.clearRect(0, 0, gridWidth + margin, gridHeight + margin);
        g2d.setColor(Color.BLACK);
        for (int i = 0; i < 9; ++i) {
            g2d.drawLine(margin, margin + i * gridh, gridWidth + margin, margin + i * gridh);
            g2d.drawLine(margin + i * gridw, margin, margin + i * gridw, gridHeight + margin);
        }
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (board[i][j]) {
                    drawChess(i, j, g2d);
                }
            }
        }
        repaint();
    }

    /**
	 * Draw a chess in (x,y)
	 * @param x
	 * @param y
	 * @param g2d
	 */
    private void drawChess(int x, int y, Graphics2D g2d) {
        int radius = gridh > gridw ? gridw : gridh;
        radius -= 10;
        int cx = x * gridw + (gridw - radius) / 2 + margin;
        int cy = y * gridh + (gridh - radius) / 2 + margin;
        Color oldc = g2d.getColor();
        g2d.setColor(color[x][y]);
        g2d.fillOval(cx, cy, radius, radius);
        g2d.setColor(oldc);
    }

    private void drawTime() {
        int x = margin + gridw * 3;
        int y = 3 * margin + gridh * 8;
        int m, s;
        s = (int) passedTime / 1000;
        m = s / 60;
        s %= 60;
        Graphics2D g = (Graphics2D) buffer.getGraphics();
        g.clearRect(x, y - 2 * margin + 2, gridw * 3, gridh * 2);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        g.drawString("" + m, x, y);
        g.drawString(":", x + gridw, y);
        g.drawString("" + s, x + 2 * gridw, y);
        repaint();
    }

    /**
	 * Calculate the value of some members
	 */
    private void calculateSize() {
        int w = this.getWidth();
        int h = this.getHeight();
        margin = 30;
        infoW = 50;
        gridWidth = (w - 2 * margin) & (~0x7);
        gridHeight = (h - 3 * margin - infoW) & (~0x7);
        gridw = gridWidth / 8;
        gridh = gridHeight / 8;
    }

    /**
	 * Check if the queue can be put in (x,y)
	 * @param x
	 * @param y
	 * @return true means can, or can not
	 */
    private boolean check(int x, int y) {
        for (int i = 0; i < 8; ++i) {
            if (i != x && board[i][y]) {
                vx = i;
                vy = y;
                return false;
            }
            if (i != y && board[x][i]) {
                vx = x;
                vy = i;
                return false;
            }
            if (i != 0 && x - i >= 0 && y - i >= 0 && board[x - i][y - i]) {
                vx = x - i;
                vy = y - i;
                return false;
            }
            if (i != 0 && x + i < 8 && y + i < 8 && board[x + i][y + i]) {
                vx = x + i;
                vy = y + i;
                return false;
            }
            if (i != 0 && x - i >= 0 && y + i < 8 && board[x - i][y + i]) {
                vx = x - i;
                vy = y + i;
                return false;
            }
            if (i != 0 && x + i < 8 && y - i >= 0 && board[x + i][y - i]) {
                vx = x + i;
                vy = y - i;
                return false;
            }
        }
        return true;
    }

    private int gridw, gridh;

    private int gridWidth, gridHeight;

    private int margin;

    private int infoW;

    private Image buffer = null;

    private Date startTime;

    private long passedTime;

    private boolean[][] board = new boolean[8][8];

    private Color[][] color = new Color[8][8];

    private int cnt;

    private boolean start;

    private int vx, vy;

    private Timer timer = new Timer(true);

    private TimerTask task = null;

    private Board me = this;

    private class MyMouseListener extends MouseAdapter {

        public void mouseClicked(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }
            if (!start) {
                return;
            }
            int x = (e.getX() - margin) / gridw;
            int y = (e.getY() - margin) / gridh;
            if (x >= 8 || y >= 8 || x < 0 || y < 0) {
                return;
            }
            board[x][y] = !board[x][y];
            if (!check(x, y)) {
                color[vx][vy] = Color.RED;
                drawBuffer();
                JOptionPane.showMessageDialog(me, "Invalidate Position!" + "(" + vx + "," + vy + ")", "Wrong!!", JOptionPane.WARNING_MESSAGE);
                color[vx][vy] = Color.BLACK;
                board[x][y] = !board[x][y];
            }
            if (board[x][y]) {
                ++cnt;
            } else {
                --cnt;
            }
            drawBuffer();
            if (cnt == 8) {
                stop();
                JOptionPane.showMessageDialog(me, "Congradulation", "You win!\n" + "Use time:" + passedTime / 60 + "m" + passedTime % 60 + "s", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }
}
