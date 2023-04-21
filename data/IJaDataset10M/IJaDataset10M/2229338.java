package org.ekstrabilet.stadium;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.ScrollPane;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import javax.swing.JPanel;
import org.ekstrabilet.stadium.beans.Sector;
import org.ekstrabilet.stadium.beans.Stadium;
import org.ekstrabilet.stadium.beans.Tribune;
import org.ekstrabilet.stadium.constants.StadiumConstants;
import org.ekstrabilet.stadium.constants.StadiumState;

/**
 * 
 * @author Maciej Koch
 *
 * Renders all stadium preview.
 */
public class TribunesView extends JPanel implements MouseListener {

    Stadium stadium;

    StadiumViewPane stadiumViewPane;

    View view = new View();

    public TribunesView(StadiumViewPane stadiumViewPane) {
        this.stadiumViewPane = stadiumViewPane;
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        g.fillRect(0, 0, StadiumConstants.viewWidth, StadiumConstants.viewHeight);
        if (stadium == null) return;
        g.setColor(Color.green);
        g.fillRect(StadiumConstants.pX, StadiumConstants.pY, StadiumConstants.pW, StadiumConstants.pH);
        view.renderTribunes(g, stadium);
        view.renderSigns(g, stadium);
    }

    /**
	 * Calculates size of each of sector.
	 * @param stadium
	 */
    public void setStadium(Stadium stadium) {
        if (stadium == null) return;
        this.stadium = stadium;
        Tribune[] tribunes = stadium.getTribunes();
        int m1 = tribunes[0].getCapacity();
        int x1 = StadiumConstants.pW;
        int row1 = m1 / (x1 / StadiumConstants.seatS) + 1;
        int y1 = StadiumConstants.seatS * row1;
        int m2 = tribunes[2].getCapacity();
        int y2 = StadiumConstants.pH + 2 * y1;
        int row2 = m2 / (y2 / StadiumConstants.seatS) + 1;
        int x2 = StadiumConstants.seatS * row2;
        tribunes[0].set(StadiumConstants.pX, StadiumConstants.pY - y1, x1, y1);
        tribunes[1].set(StadiumConstants.pX, StadiumConstants.pY + StadiumConstants.pH, x1, y1);
        tribunes[2].set(StadiumConstants.pX - x2, StadiumConstants.pY - (y2 - StadiumConstants.pH) / 2, x2, y2);
        tribunes[3].set(StadiumConstants.pX + StadiumConstants.pW, StadiumConstants.pY - (y2 - StadiumConstants.pH) / 2, x2, y2);
        for (int i = 0; i < 4; i++) {
            List<Sector> s = tribunes[i].getSectors();
            for (int j = 0; j < s.size(); j++) {
                if (i < 2) {
                    float width = tribunes[i].width / s.size();
                    s.get(j).set(tribunes[i].x + j * width, tribunes[i].y, width, tribunes[i].height);
                } else {
                    float height = tribunes[i].height / s.size();
                    s.get(j).set(tribunes[i].x, tribunes[i].y + j * height, tribunes[i].width, height);
                }
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (stadium == null) return;
        for (Tribune t : stadium.getTribunes()) for (Sector s : t.getSectors()) if (s.isOn(e.getX(), e.getY())) {
            stadiumViewPane.getSectorView().setSector(s, t.getNumber());
            stadiumViewPane.getSectorView().restart();
            stadiumViewPane.setState(StadiumState.SECTORS);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
}
