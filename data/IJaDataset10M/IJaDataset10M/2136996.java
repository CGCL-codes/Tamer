package sfa.projetIHM.horloge.view;

import java.awt.Font;
import java.awt.Graphics;
import sfa.projetIHM.horloge.models.HorlogeM;

public class HorlogeDigital extends HorlogeV {

    public HorlogeDigital(int t, HorlogeM m) {
        super(t, m);
    }

    @Override
    public void paint(Graphics g) {
        int font = (getParent().getWidth() + getParent().getHeight()) / 15;
        Font font2 = new Font("Verdana", Font.BOLD, font);
        g.setFont(font2);
        g.translate(getParent().getWidth() / 2 - 2 * font, getParent().getHeight() / 2);
        String heure;
        String minute;
        String seconde;
        int h = (int) getModele().getHeure();
        int m = (int) getModele().getMinute();
        int s = (int) getModele().getSeconde();
        if (h < 10) heure = "0" + h; else heure = "" + h;
        if (m < 10) minute = "0" + m; else minute = "" + m;
        if (s < 10) seconde = "0" + s; else seconde = "" + s;
        if (getVisible()) getModele().setTextHeure(heure + ":" + minute + ":" + seconde + ":" + (int) getModele().getDixieme()); else getModele().setTextHeure(heure + ":" + minute + ":" + seconde);
        g.drawString(getModele().getTextHeure(), 0, 0);
    }
}
