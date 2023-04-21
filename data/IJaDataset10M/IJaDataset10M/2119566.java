package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.border.LineBorder;
import model.*;

/**
 * @author Gerhard
 * @version 08-02-2011
 * Project: Vossen en Konijnen
 * This is where we summon a Histograph.
 * This is used so we can easily track how much actors of a species are alive
 */
public class HistographView extends AbstractView {

    private static final long serialVersionUID = -1248040652084522600L;

    /**
	 * Abstractview gets the right size (see below)
	 */
    public HistographView(Simulator simulator) {
        super(simulator);
    }

    /**
	 * Gets actors and calculates their worth in pixels
	 */
    private int[] calculateHeights() {
        int[] actorCountArray = simulator.getActors();
        int[] heights;
        double heighest = 0;
        double factor = 0;
        for (int i = 1; i < actorCountArray.length; i++) {
            if (actorCountArray[i] > heighest) {
                heighest = actorCountArray[i];
            }
        }
        factor = heighest / 100;
        heights = new int[actorCountArray.length - 1];
        for (int i = 1; i < actorCountArray.length; i++) {
            double a = actorCountArray[i];
            double h = a / factor;
            int h2 = (int) h;
            heights[i - 1] = h2;
        }
        return heights;
    }

    /**
	 * Method, gets statuses
	 */
    public void paintComponent(Graphics g) {
        int[] heights = calculateHeights();
        new LineBorder(Color.black, 1);
        g.setColor(Color.white);
        g.fillRect(0, 0, 172, 153);
        g.setColor(Color.white);
        g.fillRect(10, 17, 140, 120);
        Color rcolor = simulator.getColor(Rabbit.class);
        int rvpos = 100 - heights[0] + 27;
        g.setColor(rcolor);
        g.fillRect(20, rvpos, 15, heights[0]);
        Color fcolor = simulator.getColor(Fox.class);
        int fvpos = 100 - heights[1] + 27;
        g.setColor(fcolor);
        g.fillRect(55, fvpos, 15, heights[1]);
        Color bcolor = simulator.getColor(Redfox.class);
        int bvpos = 100 - heights[2] + 27;
        g.setColor(bcolor);
        g.fillRect(90, bvpos, 15, heights[2]);
        Color hcolor = simulator.getColor(Hunter.class);
        int hvpos = 100 - heights[3] + 27;
        g.setColor(hcolor);
        g.fillRect(125, hvpos, 15, heights[3]);
    }

    protected void createView() {
        setSize(172, 154);
    }
}
