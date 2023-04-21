package gui.Tiro;

import gui.*;
import engine.tiros.TiroPlayer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;
import util.Timer;

public class GuiTiroPersonagem extends GuiTiro {

    public GuiTiroPersonagem(PainelAcao painel, TiroPlayer personagem, int time, int i) {
        super(painel, personagem, time);
        ind = i;
        img = (new ImageIcon(filename.concat(Integer.toString(i) + ".png"))).getImage();
        t_per = personagem;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        AffineTransform tiro_ = new AffineTransform();
        tiro_.translate(tiro.getRelX(), tiro.getRelY());
        if (t_per.isleft()) {
            tiro_.translate(60, 0);
            tiro_.scale(-1.0, 1.0);
        }
        g2D.drawImage(img, tiro_, null);
    }

    public void update(Timer timer) {
    }

    public int getInd() {
        return ind;
    }

    private Image img;

    private TiroPlayer t_per;

    private int ind;

    private static final String filename = "Imagens/tiro/marco/";
}
