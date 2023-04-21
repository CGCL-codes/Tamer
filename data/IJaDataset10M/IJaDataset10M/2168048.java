package client.gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.ImageIcon;
import client.domein.dammen.*;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class DammenPanel extends javax.swing.JPanel {

    private static final long serialVersionUID = 1L;

    private ImageIcon tzwart = new ImageIcon(getClass().getClassLoader().getResource("pic/tegel-zwart.png"));

    private ImageIcon twit = new ImageIcon(getClass().getClassLoader().getResource("pic/tegel-wit.png"));

    private ImageIcon prood = new ImageIcon(getClass().getClassLoader().getResource("pic/pion-rood.png"));

    private ImageIcon pblauw = new ImageIcon(getClass().getClassLoader().getResource("pic/pion-blauw.png"));

    private ImageIcon prood_dubbel = new ImageIcon(getClass().getClassLoader().getResource("pic/pion-rood-dubbel.png"));

    private ImageIcon pblauw_dubbel = new ImageIcon(getClass().getClassLoader().getResource("pic/pion-blauw-dubbel.png"));

    private DammenGame game;

    private DammenFrame frame;

    public DammenPanel(DammenFrame frame, DammenGame game) {
        super();
        this.game = game;
        game.addDammenListener(new DammenListener() {

            public void doRepaint() {
                doMyRepaint();
            }

            public void doEinde(String hoe) {
                doMyEinde(hoe);
            }
        });
        this.frame = frame;
        initGUI();
    }

    private void initGUI() {
        try {
            setPreferredSize(new Dimension(600, 600));
            this.addMouseListener(new MouseAdapter() {

                public void mouseClicked(MouseEvent e) {
                    myMouseClicked(e);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void paint(Graphics g) {
        super.paint(g);
        for (int i = 0; i < game.getVeldGrootte().x; i++) {
            for (int j = 0; j < game.getVeldGrootte().y; j++) {
                if (((i % 2) + (j % 2)) != 1) tzwart.paintIcon(this, g, j * 75, i * 75); else twit.paintIcon(this, g, j * 75, i * 75);
            }
        }
        for (Speler s : game.getSpelers()) {
            for (Pion p : s.getPionnen()) {
                if (s.getKleur() == Color.RED) if (p.isDubbel()) prood_dubbel.paintIcon(this, g, p.getLocatie().x * 75, p.getLocatie().y * 75); else prood.paintIcon(this, g, p.getLocatie().x * 75, p.getLocatie().y * 75); else if (p.isDubbel()) pblauw_dubbel.paintIcon(this, g, p.getLocatie().x * 75, p.getLocatie().y * 75); else pblauw.paintIcon(this, g, p.getLocatie().x * 75, p.getLocatie().y * 75);
            }
        }
        g.drawLine(600, 0, 600, 600);
        Graphics2D g2 = (Graphics2D) g;
        if (game.getSelectedPoint() != null) {
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.ORANGE);
            g2.drawRect(game.getSelectedPoint().x * 75 + 1, game.getSelectedPoint().y * 75 + 1, 72, 72);
        }
    }

    private void myMouseClicked(MouseEvent e) {
        if (!game.isGameAlive() || !game.isMijnBeurt()) return;
        Point p = new Point(e.getX() / 75, e.getY() / 75);
        try {
            if (game.getSelectedPoint() != null) if (game.getSelectedPoint().equals(p)) game.setSelectedPoint(null); else game.doMove(p); else game.setSelectedPoint(p);
        } catch (IllegalArgumentException ex) {
            frame.addError(ex.getMessage());
        }
        repaint();
    }

    private void doMyRepaint() {
        repaint();
    }

    private void doMyEinde(String hoe) {
        frame.addError("U Heeft " + hoe);
    }
}
