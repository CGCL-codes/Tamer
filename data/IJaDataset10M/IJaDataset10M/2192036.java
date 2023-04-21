package net.sf.javaage.tools.spritecreator;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import net.sf.javaage.graphic.JAPanel;
import net.sf.javaage.graphic.sprite.JASprite;
import net.sf.javaage.graphic.sprite.SpriteManager;

/**
 * @author Fabian Spillner
 * Feb 24, 2005
 */
public class Preview extends JFrame implements WindowListener {

    JASprite sprite;

    public Preview(SpriteManager spriteManager, String actionName) {
        this.addWindowListener(this);
        sprite = new JASprite(spriteManager, actionName);
        JAPanel pane = new JAPanel();
        pane.addWidget(sprite);
        getContentPane().add(pane);
        setSize(200, 200);
        setVisible(true);
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        sprite.stopAnimation();
        sprite.getSpriteManager().reset();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }
}
