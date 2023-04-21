package autootje;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

/**
 *
 * @author frankkie
 */
public class GamePanel extends JPanel {

    public BufferedImage back;

    public int dex = 1;

    public int dey = 1;

    public int direction = 0;

    public int speed = 0;

    public boolean outsideWrap = false;

    public static int cL = 50;

    public static double twee = 2;

    public static double drie = 3;

    public static int zes = 6;

    public int backScrollGrensH = this.getWidth() / 4;

    public int backScrollGrensV = this.getHeight() / 4;

    public int backx = 0;

    public int backy = 0;

    public GamePanel() {
        this.setLayout(null);
        this.setBorder(new LineBorder(Color.black));
        this.setFocusable(true);
        this.addKeyListener(new KeyListener() {

            public void keyTyped(KeyEvent e) {
            }

            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    draaiLinks(e.isShiftDown());
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    draaiRechts(e.isShiftDown());
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    speedUp(e.isShiftDown());
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    speedDown(e.isShiftDown());
                }
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    speed = 0;
                    doeResize();
                }
                if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
                    cL++;
                }
                if (e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
                    cL--;
                }
                if (e.getKeyCode() == KeyEvent.VK_Q) {
                    twee += 0.1;
                }
                if (e.getKeyCode() == KeyEvent.VK_A) {
                    twee -= 0.1;
                }
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    drie += 0.1;
                }
                if (e.getKeyCode() == KeyEvent.VK_S) {
                    drie -= 0.1;
                }
            }

            public void keyReleased(KeyEvent e) {
            }
        });
        try {
            back = ImageIO.read(this.getClass().getResource("rommel.gif"));
        } catch (Exception ex) {
            Main.drukaf("FOUT:\n" + ex);
        }
        this.addComponentListener(new ComponentAdapter() {

            @Override
            public void componentResized(ComponentEvent e) {
                doeResize();
            }
        });
        Timer klok = new Timer(100, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                doeStep();
            }
        });
        klok.setRepeats(true);
        klok.start();
    }

    public void doeStep() {
        if (speed != 0) {
            if (speed > 0) {
                speed--;
            } else {
                speed++;
            }
            if (speed > 300) {
                speed = 300;
            }
            if (speed < -300) {
                speed = -300;
            }
        }
        dey += (int) ((Math.sin((-direction) * (Math.PI / 180))) * speed);
        dex += (int) ((Math.cos((-direction) * (Math.PI / 180))) * speed);
        if (outsideWrap) {
            if (dex > this.getWidth()) {
                dex -= this.getWidth();
            }
            if (dey > this.getHeight()) {
                dey -= this.getHeight();
            }
            if (dex < 0) {
                dex += this.getWidth();
            }
            if (dey < 0) {
                dey += this.getHeight();
            }
        } else {
            if (dex > (this.getWidth() - backScrollGrensH)) {
                backx -= dex - (this.getWidth() - backScrollGrensH);
                dex = (this.getWidth() - backScrollGrensH) - 1;
            }
            if (dex < (backScrollGrensH)) {
                backx -= dex - (backScrollGrensH);
                dex = (backScrollGrensH) + 1;
            }
            if (dey > (this.getHeight() - backScrollGrensV)) {
                backy -= dey - (this.getHeight() - backScrollGrensV);
                dey = (this.getHeight() - backScrollGrensV) - 1;
            }
            if (dey < (backScrollGrensV)) {
                backy -= dey - (backScrollGrensV);
                dey = (backScrollGrensV) + 1;
            }
        }
        this.repaint();
    }

    public void speedUp(boolean shift) {
        if (shift) {
            speed += 6;
        } else {
            speed += 3;
        }
    }

    public void speedDown(boolean shift) {
        if (shift) {
            speed -= 6;
        } else {
            speed -= 3;
        }
    }

    public void draaiLinks(boolean shift) {
        if (shift) {
            dir(6);
        } else {
            dir(3);
        }
    }

    public void draaiRechts(boolean shift) {
        if (shift) {
            dir(-6);
        } else {
            dir(-3);
        }
    }

    public void dir(int a) {
        direction += a;
        if (direction >= 360) {
            direction -= 360;
        }
        if (direction < 0) {
            direction += 360;
        }
        this.repaint();
    }

    public void doeResize() {
        dex = (this.getWidth() / 2);
        dey = (this.getHeight() / 2);
        speed = 0;
        direction = 0;
        backScrollGrensH = this.getWidth() / 4;
        backScrollGrensV = this.getHeight() / 4;
        backx = 0;
        backy = 0;
    }

    @Override
    protected void paintComponent(Graphics g) {
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(back, backx, backy, null);
        g.drawString("Direction: " + direction + "    Speed: " + speed + "    cL: " + cL + "   twee: " + twee + "   drie: " + drie, 5, 35);
        g.setColor(Color.black);
        g.drawLine(dex, dey, dex + (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180))), dey + (int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180))));
        g.drawLine(dex - (int) ((cL / drie) * Math.sin((direction + 0) * (Math.PI / 180))), dey - (int) ((cL / drie) * Math.cos((direction + 0) * (Math.PI / 180))), dex, dey);
        g.drawLine(dex, dey, dex + (int) ((cL / drie) * Math.sin((direction + 0) * (Math.PI / 180))), dey + (int) ((cL / drie) * Math.cos((direction + 0) * (Math.PI / 180))));
        g.drawLine((dex + (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180)))) - ((int) ((cL / drie) * Math.sin((direction + 0) * (Math.PI / 180)))), (dey - (int) ((cL / drie) * Math.cos((direction + 0) * (Math.PI / 180)))) + ((int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180)))), dex + (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180))), dey + (int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180))));
        g.drawLine((dex + (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180)))) + ((int) ((cL / drie) * Math.sin((direction + 0) * (Math.PI / 180)))), (dey + (int) ((cL / drie) * Math.cos((direction + 0) * (Math.PI / 180)))) + ((int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180)))), dex + (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180))), dey + (int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180))));
        g.drawLine(dex - (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180))), dey - (int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180))), dex - (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180))), dey - (int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180))));
        g.drawLine((dex - (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180)))) - ((int) ((cL / drie) * Math.sin((direction - 0) * (Math.PI / 180)))), (dey - (int) ((cL / drie) * Math.cos((direction - 0) * (Math.PI / 180)))) + ((int) ((cL / twee) * Math.cos((direction - 90) * (Math.PI / 180)))), dex - (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180))), dey - (int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180))));
        g.drawLine((dex - (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180)))) + ((int) ((cL / drie) * Math.sin((direction - 0) * (Math.PI / 180)))), (dey + (int) ((cL / drie) * Math.cos((direction - 0) * (Math.PI / 180)))) + ((int) ((cL / twee) * Math.cos((direction - 90) * (Math.PI / 180)))), dex - (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180))), dey - (int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180))));
        g.drawLine((dex - (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180)))) - ((int) ((cL / drie) * Math.sin((direction - 0) * (Math.PI / 180)))), (dey - (int) ((cL / drie) * Math.cos((direction - 0) * (Math.PI / 180)))) + ((int) ((cL / twee) * Math.cos((direction - 90) * (Math.PI / 180)))), dex - (int) ((cL / drie) * Math.sin((direction + 0) * (Math.PI / 180))), dey - (int) ((cL / drie) * Math.cos((direction + 0) * (Math.PI / 180))));
        g.drawLine(dex + (int) ((cL / drie) * Math.sin((direction + 0) * (Math.PI / 180))), dey + (int) ((cL / drie) * Math.cos((direction + 0) * (Math.PI / 180))), (dex - (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180)))) + ((int) ((cL / drie) * Math.sin((direction - 0) * (Math.PI / 180)))), (dey + (int) ((cL / drie) * Math.cos((direction - 0) * (Math.PI / 180)))) + ((int) ((cL / twee) * Math.cos((direction - 90) * (Math.PI / 180)))));
        g.drawLine((dex + (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180)))) - ((int) ((cL / drie) * Math.sin((direction + 0) * (Math.PI / 180)))), (dey - (int) ((cL / drie) * Math.cos((direction + 0) * (Math.PI / 180)))) + ((int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180)))), dex - (int) ((cL / drie) * Math.sin((direction + 0) * (Math.PI / 180))), dey - (int) ((cL / drie) * Math.cos((direction + 0) * (Math.PI / 180))));
        g.drawLine((dex + (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180)))) + ((int) ((cL / drie) * Math.sin((direction + 0) * (Math.PI / 180)))), (dey + (int) ((cL / drie) * Math.cos((direction + 0) * (Math.PI / 180)))) + ((int) ((cL / twee) * Math.cos((direction + 90) * (Math.PI / 180)))), (dex - (int) ((cL / twee) * Math.sin((direction + 90) * (Math.PI / 180)))) + ((int) ((cL / drie) * Math.sin((direction - 0) * (Math.PI / 180)))), (dey + (int) ((cL / drie) * Math.cos((direction - 0) * (Math.PI / 180)))) + ((int) ((cL / twee) * Math.cos((direction - 90) * (Math.PI / 180)))));
        g.setColor(Color.red);
        g.fillOval(dex - 4, dey - 4, 8, 8);
    }
}
