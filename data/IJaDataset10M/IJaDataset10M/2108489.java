package abstrasy.bedesk.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JToolTip;

/**
 * Abstrasy Interpreter
 *
 * Copyright : Copyright (c) 2006-2012, Luc Bruninx.
 *
 * Concédée sous licence EUPL, version 1.1 uniquement (la «Licence»).
 *
 * Vous ne pouvez utiliser la présente oeuvre que conformément à la Licence.
 * Vous pouvez obtenir une copie de la Licence à l’adresse suivante:
 *
 *   http://www.osor.eu/eupl
 *
 * Sauf obligation légale ou contractuelle écrite, le logiciel distribué sous
 * la Licence est distribué "en l’état", SANS GARANTIES OU CONDITIONS QUELLES
 * QU’ELLES SOIENT, expresses ou implicites.
 *
 * Consultez la Licence pour les autorisations et les restrictions
 * linguistiques spécifiques relevant de la Licence.
 *
 *
 * @author Luc Bruninx
 * @version 1.0
 *
 * Note:
 * ====
 *
 * Le code suivant est offert sous sous la licence mentionnée ci-dessus par
 * la société BEDESK sprl dans le cadre du support du projet Abstrasy.
 *
 */
public class HalloButton extends JButton {

    private int halloAlpha = 0;

    private static final Color hallo32 = new Color(96, 96, 96, 32);

    private int roundDiameter = 10;

    private String _text = "";

    private boolean withText = true;

    boolean toolTipCreated = false;

    public HalloButton() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setText(String text) {
        _text = text;
        if (withText) {
            super.setText(_text);
        } else {
            super.setText("");
        }
    }

    public boolean isWithText() {
        return withText;
    }

    public void setWithText(boolean withText) {
        this.withText = withText;
        this.setText(_text);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        if (halloAlpha == 32) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setPaint(hallo32);
            g2.fillRoundRect(0, 0, this.getWidth(), this.getHeight(), roundDiameter, roundDiameter);
        }
        super.paintComponent(g);
    }

    private void jbInit() throws Exception {
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        this.addMouseListener(new HalloButton_this_mouseAdapter(this));
    }

    public JToolTip createToolTip() {
        toolTipCreated = true;
        return super.createToolTip();
    }

    void this_mouseEntered(MouseEvent e) {
        halloAlpha = 32;
        this.repaint();
    }

    void this_mouseExited(MouseEvent e) {
        halloAlpha = 0;
        this.repaint();
        if (toolTipCreated) {
            toolTipCreated = false;
        }
    }

    public int getHalloAlpha() {
        return halloAlpha;
    }

    public void setHalloAlpha(int halloAlpha) {
        this.halloAlpha = halloAlpha;
        this.repaint();
    }

    public int getRoundDiameter() {
        return roundDiameter;
    }

    public void setRoundDiameter(int roundDiameter) {
        this.roundDiameter = roundDiameter;
    }

    private class HalloButton_this_mouseAdapter extends MouseAdapter {

        HalloButton adaptee;

        HalloButton_this_mouseAdapter(HalloButton adaptee) {
            this.adaptee = adaptee;
        }

        public void mouseEntered(MouseEvent e) {
            adaptee.this_mouseEntered(e);
        }

        public void mouseExited(MouseEvent e) {
            adaptee.this_mouseExited(e);
        }
    }
}
