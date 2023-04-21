package colonsdelutbm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**Affiche le r�sulat des d�s*/
public class LancerDe extends JPanel {

    private int rand1;

    private int rand2;

    public LancerDe(int a, int b) {
        rand1 = a;
        rand2 = b;
    }

    /** Affiche des d�s en fonction des nombres obtenus lors du rand*/
    public void paintComponent(Graphics g) {
        switch(rand1) {
            case 1:
                try {
                    Image img = ImageIO.read(new File("des/de1.gif"));
                    g.drawImage(img, 40, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Image img = ImageIO.read(new File("des/de2.gif"));
                    g.drawImage(img, 40, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    Image img = ImageIO.read(new File("des/de3.gif"));
                    g.drawImage(img, 40, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    Image img = ImageIO.read(new File("des/de4.gif"));
                    g.drawImage(img, 40, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    Image img = ImageIO.read(new File("des/de5.gif"));
                    g.drawImage(img, 40, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    Image img = ImageIO.read(new File("des/de6.gif"));
                    g.drawImage(img, 40, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                g.setColor(Color.BLACK);
                break;
        }
        switch(rand2) {
            case 1:
                try {
                    Image img = ImageIO.read(new File("des/de1.gif"));
                    g.drawImage(img, 160, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                try {
                    Image img = ImageIO.read(new File("des/de2.gif"));
                    g.drawImage(img, 160, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 3:
                try {
                    Image img = ImageIO.read(new File("des/de3.gif"));
                    g.drawImage(img, 160, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 4:
                try {
                    Image img = ImageIO.read(new File("des/de4.gif"));
                    g.drawImage(img, 160, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 5:
                try {
                    Image img = ImageIO.read(new File("des/de5.gif"));
                    g.drawImage(img, 160, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 6:
                try {
                    Image img = ImageIO.read(new File("des/de6.gif"));
                    g.drawImage(img, 160, 10, this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                g.setColor(Color.BLACK);
                break;
        }
        Font font = new Font("Comic Sans MS", Font.PLAIN, 18);
        g.setFont(font);
        g.drawString("Vous avez obtenu un " + (rand1 + rand2) + " !!", 50, 150);
        if (!Jeu.debutJ) {
            if ((rand1 + rand2) == 7) {
                g.drawString("D�placez le bin�me glandeur !!", 20, 175);
            } else {
                g.drawString("Distribution des ressources...", 25, 175);
            }
        }
    }
}
