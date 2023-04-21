package com.mbn.pwing;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JMenuBar;
import com.mbn.pwing.util.Controller;

public class PMenuBar extends JMenuBar {

    private static final long serialVersionUID = 7026590716835696692L;

    private Color borderColor = new Color(240, 200, 220);

    public PMenuBar() {
        setController(Controller.getController(Controller.BLUE));
        this.setOpaque(false);
    }

    public void paint(Graphics g) {
        g.setColor(borderColor);
        g.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);
        g.setColor(Color.WHITE);
        g.drawLine(0, getHeight() - 2, getWidth(), getHeight() - 2);
        for (int i = 20; i >= 0; i--) {
            Color shade = getController().getMenuBarBGColor();
            int rC = shade.getRed();
            int gC = shade.getGreen();
            int bC = shade.getBlue();
            if (getController().getColor() == Controller.BLACK) {
                gC -= (i * 2);
                bC -= (i * 2);
                rC -= (i * 2);
            } else {
                gC -= i;
                bC -= (i * 2);
            }
            if (rC <= 0) {
                rC = 0;
            }
            if (gC <= 0) {
                gC = 0;
            }
            if (bC <= 0) {
                bC = 0;
            }
            g.setColor(new Color(rC, gC, bC));
            g.drawLine(0, i, getWidth(), i);
        }
        this.paintComponents(g);
    }

    Controller controller;

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
        setColors();
    }

    private void setColors() {
        borderColor = getController().getBorderColor();
    }
}
