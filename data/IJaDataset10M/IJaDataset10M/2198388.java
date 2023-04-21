package tesproject;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

/**
 *
 * @author d_frEak
 */
public class Key {

    private int dx;

    private int dy;

    private int i;

    private Image image;

    private Image image2;

    private ImageIcon li = new ImageIcon("img\\player\\d1.png");

    private ImageIcon li2 = new ImageIcon("img\\player\\d2.png");

    private ImageIcon li3 = new ImageIcon("img\\player\\d3.png");

    public Key() {
        dx = 0;
        dy = 0;
        i = 1;
    }

    void move() {
    }

    public int getX() {
        return dx;
    }

    public int getY() {
        return dy;
    }

    public int getI() {
        return i;
    }

    public void setI(int ni) {
        i = i + ni;
    }

    public void setX(int nx) {
        dx = dx + nx;
    }

    public void setY(int ny) {
        dy = dy + ny;
    }

    public ImageIcon getImage() {
        return li;
    }

    public ImageIcon getImage2() {
        return li2;
    }

    public ImageIcon getImage3() {
        return li3;
    }

    public void keyPressed(KeyEvent e) throws FileNotFoundException, IOException {
        char key = e.getKeyChar();
        if (key == 'a') {
            li = new ImageIcon("img\\player\\a1.png");
            li2 = new ImageIcon("img\\player\\a2.png");
            li3 = new ImageIcon("img\\player\\a3.png");
            dx = 50;
            dy = 25;
        }
        if (key == 'd') {
            li = new ImageIcon("img\\player\\d1.png");
            li2 = new ImageIcon("img\\player\\d2.png");
            li3 = new ImageIcon("img\\player\\d3.png");
            dx = -50;
            dy = -25;
        }
        if (key == 'w') {
            li = new ImageIcon("img\\player\\w1.png");
            li2 = new ImageIcon("img\\player\\w2.png");
            li3 = new ImageIcon("img\\player\\w3.png");
            dx = -50;
            dy = 25;
        }
        if (key == 's') {
            li = new ImageIcon("img\\player\\s1.png");
            li2 = new ImageIcon("img\\player\\s2.png");
            li3 = new ImageIcon("img\\player\\s3.png");
            dx = 50;
            dy = -25;
        }
        i = dx / 2;
    }
}
