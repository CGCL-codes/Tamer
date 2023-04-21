package FourRowSolitaire;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 * Class: FireworksDisplay
 * 
 * Description: The FireworksDisplay class manages the win animation for Four Row Solitaire.
 * 
 * @author Matt Stephen
 */
public class FireworksDisplay extends JPanel implements ActionListener {

    public final int NUM_FIREWORKS;

    public final int FIREWORKS_SIZE;

    public static final int SET_DELAY = 10;

    public static final int FIREWORKS_TIME = 30;

    private int[] x;

    ;

    private int[] y;

    private Color[] colors;

    private int[][] xx;

    private int[][] yy;

    private int num = 0;

    private int numSets = 0;

    private int startValue = 0;

    private Timer timer = new Timer(100, this);

    private Random random = new Random();

    public FireworksDisplay(int num, int size) {
        NUM_FIREWORKS = num;
        FIREWORKS_SIZE = size;
        x = new int[NUM_FIREWORKS];
        y = new int[NUM_FIREWORKS];
        colors = new Color[NUM_FIREWORKS];
        xx = new int[NUM_FIREWORKS][FIREWORKS_SIZE];
        yy = new int[NUM_FIREWORKS][FIREWORKS_SIZE];
        setBackground(Color.BLACK);
    }

    public void restartDisplay() {
        timer.stop();
        num = 0;
        for (int i = 0; i < x.length; i++) {
            x[i] = (int) (Math.random() * 300) + 300;
            for (int j = 0; j < FIREWORKS_SIZE; j++) {
                int xOffset = random.nextInt(151);
                double signCheck = Math.random();
                if (signCheck <= .5) {
                    xx[i][j] = -xOffset;
                } else {
                    xx[i][j] = xOffset;
                }
            }
        }
        for (int i = 0; i < y.length; i++) {
            y[i] = (int) (Math.random() * 200) + 300;
            for (int j = 0; j < FIREWORKS_SIZE; j++) {
                int yOffset = random.nextInt(151);
                double signCheck = Math.random();
                if (signCheck <= .5) {
                    yy[i][j] = -yOffset;
                } else {
                    yy[i][j] = yOffset;
                }
            }
        }
        for (int i = 0; i < colors.length; i++) {
            colors[i] = randomColor();
        }
        timer.start();
    }

    public Color randomColor() {
        double rand = Math.random();
        if (rand <= .1) {
            return Color.RED;
        } else if (rand <= .2) {
            return Color.BLUE;
        } else if (rand <= .3) {
            return Color.YELLOW;
        } else if (rand <= .4) {
            return Color.GREEN;
        } else if (rand <= .5) {
            return Color.ORANGE;
        } else if (rand <= .6) {
            return Color.CYAN;
        } else if (rand <= .7) {
            return Color.MAGENTA;
        } else if (rand <= .8) {
            return Color.PINK;
        } else if (rand <= .9) {
            return Color.WHITE;
        } else {
            return new Color(153, 50, 205);
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.RED);
        g.drawString("You Win! -- Click to Close.", 340, 550);
        if (numSets < 5) {
            for (int i = startValue; i < startValue + 2; i++) {
                if (num < 2 * FIREWORKS_TIME / 3) {
                    int x0 = 0;
                    int y0 = getHeight() - (num * y[i] / (2 * FIREWORKS_TIME / 3));
                    if (i % 2 == 0) {
                        x0 = num * x[i] / (2 * FIREWORKS_TIME / 3);
                    } else {
                        x0 = getWidth() - num * x[i] / (2 * FIREWORKS_TIME / 3);
                    }
                    g.setColor(colors[i]);
                    g.drawRect(x0, y0, 5, 5);
                } else {
                    num -= Math.ceil(2 * FIREWORKS_TIME / 3.0);
                    for (int j = 0; j < FIREWORKS_SIZE; j++) {
                        g.setColor(colors[i]);
                        if (i % 2 == 0) {
                            g.drawLine(x[i], getHeight() - y[i], x[i] + (num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        } else {
                            g.drawLine(getWidth() - x[i], getHeight() - y[i], getWidth() - (x[i] + num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        }
                    }
                    num += Math.ceil(2 * FIREWORKS_TIME / 3.0);
                }
            }
        } else if (numSets < 10) {
            for (int i = startValue; i < startValue + 3; i++) {
                if (num < 2 * FIREWORKS_TIME / 3) {
                    int x0 = 0;
                    int y0 = getHeight() - (num * y[i] / (2 * FIREWORKS_TIME / 3));
                    if (i % 2 == 0) {
                        x0 = num * x[i] / (2 * FIREWORKS_TIME / 3);
                    } else {
                        x0 = getWidth() - num * x[i] / (2 * FIREWORKS_TIME / 3);
                    }
                    g.setColor(colors[i]);
                    g.drawRect(x0, y0, 5, 5);
                } else {
                    num -= Math.ceil(2 * FIREWORKS_TIME / 3.0);
                    for (int j = 0; j < FIREWORKS_SIZE; j++) {
                        g.setColor(colors[i]);
                        if (i % 2 == 0) {
                            g.drawLine(x[i], getHeight() - y[i], x[i] + (num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        } else {
                            g.drawLine(getWidth() - x[i], getHeight() - y[i], getWidth() - (x[i] + num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        }
                    }
                    num += Math.ceil(2 * FIREWORKS_TIME / 3.0);
                }
            }
        } else if (numSets < 15) {
            for (int i = startValue; i < startValue + 4; i++) {
                if (num < 2 * FIREWORKS_TIME / 3) {
                    int x0 = 0;
                    int y0 = getHeight() - (num * y[i] / (2 * FIREWORKS_TIME / 3));
                    if (i % 2 == 0) {
                        x0 = num * x[i] / (2 * FIREWORKS_TIME / 3);
                    } else {
                        x0 = getWidth() - num * x[i] / (2 * FIREWORKS_TIME / 3);
                    }
                    g.setColor(colors[i]);
                    g.drawRect(x0, y0, 5, 5);
                } else {
                    num -= Math.ceil(2 * FIREWORKS_TIME / 3.0);
                    for (int j = 0; j < FIREWORKS_SIZE; j++) {
                        g.setColor(colors[i]);
                        if (i % 2 == 0) {
                            g.drawLine(x[i], getHeight() - y[i], x[i] + (num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        } else {
                            g.drawLine(getWidth() - x[i], getHeight() - y[i], getWidth() - (x[i] + num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        }
                    }
                    num += Math.ceil(2 * FIREWORKS_TIME / 3.0);
                }
            }
        } else if (numSets < 20) {
            for (int i = startValue; i < startValue + 5; i++) {
                if (num < 2 * FIREWORKS_TIME / 3) {
                    int x0 = 0;
                    int y0 = getHeight() - (num * y[i] / (2 * FIREWORKS_TIME / 3));
                    if (i % 2 == 0) {
                        x0 = num * x[i] / (2 * FIREWORKS_TIME / 3);
                    } else {
                        x0 = getWidth() - num * x[i] / (2 * FIREWORKS_TIME / 3);
                    }
                    g.setColor(colors[i]);
                    g.drawRect(x0, y0, 5, 5);
                } else {
                    num -= Math.ceil(2 * FIREWORKS_TIME / 3.0);
                    for (int j = 0; j < FIREWORKS_SIZE; j++) {
                        g.setColor(colors[i]);
                        if (i % 2 == 0) {
                            g.drawLine(x[i], getHeight() - y[i], x[i] + (num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        } else {
                            g.drawLine(getWidth() - x[i], getHeight() - y[i], getWidth() - (x[i] + num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        }
                    }
                    num += Math.ceil(2 * FIREWORKS_TIME / 3.0);
                }
            }
        } else if (numSets < 25) {
            for (int i = startValue; i < startValue + 10; i++) {
                if (num < 2 * FIREWORKS_TIME / 3) {
                    int x0 = 0;
                    int y0 = getHeight() - (num * y[i] / (2 * FIREWORKS_TIME / 3));
                    if (i % 2 == 0) {
                        x0 = num * x[i] / (2 * FIREWORKS_TIME / 3);
                    } else {
                        x0 = getWidth() - num * x[i] / (2 * FIREWORKS_TIME / 3);
                    }
                    g.setColor(colors[i]);
                    g.drawRect(x0, y0, 5, 5);
                } else {
                    num -= Math.ceil(2 * FIREWORKS_TIME / 3.0);
                    for (int j = 0; j < FIREWORKS_SIZE; j++) {
                        g.setColor(colors[i]);
                        if (i % 2 == 0) {
                            g.drawLine(x[i], getHeight() - y[i], x[i] + (num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        } else {
                            g.drawLine(getWidth() - x[i], getHeight() - y[i], getWidth() - (x[i] + num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        }
                    }
                    num += Math.ceil(2 * FIREWORKS_TIME / 3.0);
                }
            }
        } else if (numSets < 26) {
            for (int i = startValue; i < x.length; i++) {
                if (num < 2 * FIREWORKS_TIME / 3) {
                    int x0 = 0;
                    int y0 = getHeight() - (num * y[i] / (2 * FIREWORKS_TIME / 3));
                    if (i % 2 == 0) {
                        x0 = num * x[i] / (2 * FIREWORKS_TIME / 3);
                    } else {
                        x0 = getWidth() - num * x[i] / (2 * FIREWORKS_TIME / 3);
                    }
                    g.setColor(colors[i]);
                    g.drawRect(x0, y0, 5, 5);
                } else {
                    num -= Math.ceil(2 * FIREWORKS_TIME / 3.0);
                    for (int j = 0; j < FIREWORKS_SIZE; j++) {
                        g.setColor(colors[i]);
                        if (i % 2 == 0) {
                            g.drawLine(x[i], getHeight() - y[i], x[i] + (num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        } else {
                            g.drawLine(getWidth() - x[i], getHeight() - y[i], getWidth() - (x[i] + num * xx[i][j] / (NUM_FIREWORKS / 3)), getHeight() - (y[i] + (num * yy[i][j] / (NUM_FIREWORKS / 3))));
                        }
                    }
                    num += Math.ceil(2 * FIREWORKS_TIME / 3.0);
                }
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            if (num >= FIREWORKS_TIME) {
                num = 0;
                numSets++;
                startValue = random.nextInt(x.length / 2);
            }
            num++;
            if (numSets >= 26) {
                timer.stop();
            } else {
                repaint();
            }
        }
    }
}
