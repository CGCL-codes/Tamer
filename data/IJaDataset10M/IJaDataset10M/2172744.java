package jpm.combatforce.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import jpm.combatforce.logic.Game;
import jpm.combatforce.logic.GameFactory;
import jtbs.event.ApplicationEventHandler;
import jtbs.event.ApplicationEventHandlerFactory;
import jtbs.event.ApplicationListener;
import jtbs.event.AwtEventThreadApplicationListener;
import jtbs.event.derived.EnableGamePanelEvent;
import jtbs.event.derived.TurnEndEvent;
import jtbs.prefs.GuiPreferences;

/**
 * @author 527843
 */
public class GamePanel extends jtbs.ui.GamePanel {

    protected Game game = GameFactory.getInstance();

    protected Point clickPoint, cursorPoint;

    protected ApplicationEventHandler applicationEventHandler = ApplicationEventHandlerFactory.getInstance();

    protected JButton et = new JButton("End Turn");

    protected JPanel p = new JPanel();

    public GamePanel(Dimension dimension, Game game) {
        super(dimension, game);
        this.game = game;
        setPreferredSize(dimension);
        this.applicationEventHandler.addApplicationListener(new AwtEventThreadApplicationListener<EnableGamePanelEvent>() {

            public void onEvent() {
                repaint();
            }
        }, this);
        applicationEventHandler.addApplicationListener(new ApplicationListener<EnableGamePanelEvent>() {

            public void onEvent() {
                et.setVisible(true);
            }
        }, this);
        add(et);
        et.addActionListener(this);
        repaint();
    }

    public void paint(Graphics g) {
        Color color = GuiPreferences.getInstance().colorMap("N", game.getActivePlayer().getId());
        setBackground(color);
        g.setColor(color);
        g.fillRect(0, 0, getWidth(), getHeight());
        super.paint(g);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void updateClickPoint(Point p) {
        clickPoint = p;
        repaint();
    }

    public void updateCursorPoint(Point p) {
        cursorPoint = p;
        repaint();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(et)) {
            et.setVisible(false);
            applicationEventHandler.onApplicationEvent(new TurnEndEvent(this));
        }
    }
}
