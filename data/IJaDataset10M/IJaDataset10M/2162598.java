package net.sf.isnake.ui;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import javax.swing.*;
import net.sf.isnake.core.GameController;
import net.sf.isnake.core.iSnakeConfiguration;

/**
 * Displays the Panel with iSnake Logo(with reflection) and having gradient in background
 * This panel is displayed when the game (application) is executed
 *
 * @author  Abhishek Dutta (adutta.np@gmail.com)
 * @version $Id: IntroLogoPanel.java 146 2008-05-07 02:39:19Z thelinuxmaniac $
 */
public class IntroLogoPanel extends JPanel implements KeyListener {

    public static String PANEL_NAME = "IntroLogoPanel";

    private GameController gameController;

    private UIResources uiResources;

    private iSnakeConfiguration conf;

    private int logoX;

    private int logoY;

    /** Creates a new instance of IntroLogoPanel */
    public IntroLogoPanel(GameController gc, iSnakeConfiguration isc, UIResources uir) {
        setGameController(gc);
        setUiResources(uir);
        setConf(isc);
        setMaximumSize(getConf().getMaxDim());
        setMinimumSize(getConf().getMinDim());
        setPreferredSize(getConf().getPrefDim());
        setLogoX(conf.getPrefDim().width / 2 - getUiResources().getISnakeLogoReflct().getWidth(null) / 2);
        setLogoY(conf.getPrefDim().width / 2 - getUiResources().getISnakeLogoReflct().getHeight(null) / 2);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(uiResources.getHintForQualityProcessing());
        g2.setPaint(new GradientPaint(400, 0, getUiResources().getColorIntroPanelGrad(), 400, 600, Color.WHITE));
        g2.fillRect(0, 0, conf.getPrefDim().width, conf.getPrefDim().height);
        g2.setPaint(Color.BLACK);
        g2.drawImage(getUiResources().getISnakeLogoReflct(), getLogoX(), getLogoY(), null);
        g2.setFont(getUiResources().getFontZekton().deriveFont(24.0F));
        FontRenderContext context = g2.getFontRenderContext();
        Rectangle2D bounds = getUiResources().getFontZekton().getStringBounds(UIResources.GAME_TITLE, context);
        g2.drawString("iSnake - Intelligent Multiplayer Snake", getLogoX() - (int) (bounds.getWidth() * 6), getLogoY() - (int) (bounds.getHeight() * 20));
    }

    public UIResources getUiResources() {
        return uiResources;
    }

    public void setUiResources(UIResources uiResources) {
        this.uiResources = uiResources;
    }

    public iSnakeConfiguration getConf() {
        return conf;
    }

    public void setConf(iSnakeConfiguration conf) {
        this.conf = conf;
    }

    public int getLogoY() {
        return logoY;
    }

    public void setLogoY(int logoY) {
        this.logoY = logoY;
    }

    public int getLogoX() {
        return logoX;
    }

    public void setLogoX(int logoX) {
        this.logoX = logoX;
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        getGameController().getMainPanel().getFrame().removeKeyListener(this);
        getGameController().getMainPanel().showPanel(MainMenuPanel.PANEL_NAME);
    }

    public void keyReleased(KeyEvent e) {
    }

    public GameController getGameController() {
        return gameController;
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }
}
