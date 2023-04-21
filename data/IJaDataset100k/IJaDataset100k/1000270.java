package net.sf.brightside.PokerMaster.gui;

import net.sf.brightside.PokerMaster.core.spring.ApplicationContextProvider;
import net.sf.brightside.PokerMaster.core.spring.ApplicationContextProviderSingleton;
import net.sf.brightside.PokerMaster.exceptions.*;
import net.sf.brightside.PokerMaster.metamodel.IPokerTableActions;
import net.sf.brightside.PokerMaster.metamodel.Player;
import net.sf.brightside.PokerMaster.metamodel.Pot;
import net.sf.brightside.PokerMaster.metamodel.beans.GraphicsCardBean;
import net.sf.brightside.PokerMaster.metamodel.beans.HandPlayBean;
import net.sf.brightside.PokerMaster.metamodel.beans.GraphicsCardBeanTest;
import javax.swing.*;
import org.springframework.context.ApplicationContext;
import ca.ualberta.cs.poker.Hand;
import java.awt.*;
import java.awt.event.*;

public class PokerTablePanel extends JPanel implements IPokerTableActions, MouseListener {

    private Player p1;

    private Player p2;

    private IPokerTableActions handPlay;

    private static final boolean DEBUG = false;

    public PokerTablePanel(IPokerTableActions hp) {
        super();
        setBackground(Color.green);
        setOpaque(true);
        setMinimumSize(new Dimension(600, 480));
        setPreferredSize(new Dimension(600, 480));
        setMaximumSize(new Dimension(600, 480));
        setHandPlay(hp);
    }

    public void setHandPlay(IPokerTableActions hp) {
        handPlay = hp;
        p1 = hp.getP1();
        p2 = hp.getP2();
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Hand h1 = p1.getHand();
        drawHand(g, h1, 100, 30);
        Hand h2 = p2.getHand();
        drawHand(g, h2, 100, 300);
        g.setColor(Color.black);
        g.drawString(handPlay.getPot().toString(), 300, 230);
        g.drawString(p1.getName() + ": " + p1.getMoney(), 110, 20);
        g.drawString(p2.getName() + ": " + p2.getMoney(), 110, 443);
    }

    private void drawHand(Graphics g, Hand h, int x, int y) {
        for (int i = 0; i < 5; i++) {
            GraphicsCardBean sc = (GraphicsCardBean) h.getCard(i + 1);
            ImageIcon scIcon = sc.getImageIcon();
            g.drawImage(scIcon.getImage(), x + 82 * i, y, 79, 123, scIcon.getImageObserver());
        }
    }

    public void call(Player p) throws BankruptException {
        handPlay.call(p);
        repaint();
    }

    public void bet(Player p, int amount) throws BankruptException {
        handPlay.bet(p, amount);
        repaint();
    }

    public void fold(Player p) {
        handPlay.fold(p);
        JOptionPane.showMessageDialog(this, p2.getName() + " folds!");
    }

    public void discard(Player p) {
        handPlay.discard(p);
        if (DEBUG == false && p == p2) repaint();
    }

    public Player whowins() throws TieException {
        return handPlay.whowins();
    }

    public void setP1(Player p) {
        p1 = p;
        handPlay.setP1(p);
    }

    public Player getP1() {
        return handPlay.getP1();
    }

    public void setP2(Player p) {
        p2 = p;
        handPlay.setP2(p);
    }

    public Player getP2() {
        return handPlay.getP2();
    }

    public Pot getPot() {
        return handPlay.getPot();
    }

    public void setPot(Pot pot) {
        handPlay.setPot(pot);
    }

    public void mouseClicked(MouseEvent e) {
    }

    private int clickOnCard(int x, int y) {
        if (y >= 300 && y <= 423) for (int i = 0; i < 5; i++) {
            if (x >= 115 + 82 * i && x <= 115 + 82 * i + 79) return i + 1;
        }
        return -1;
    }

    public void mousePressed(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        int i = clickOnCard(x, y);
        if (i != -1) {
            Hand h = p2.getHand();
            GraphicsCardBean c = (GraphicsCardBean) h.getCard(i);
            if (c.isFaceDown()) {
                c.setFaceDown(false);
                c.setForDiscard(false);
            } else {
                c.setFaceDown(true);
                c.setForDiscard(true);
            }
            repaint();
        }
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }
}
