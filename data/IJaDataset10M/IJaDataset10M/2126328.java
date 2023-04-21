package view;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import model.*;

/**
 * A menü megjelenítésért felelős panel
 * @author nUMLock
 */
public class MenuPanel extends javax.swing.JPanel {

    private MainWindow mainWindow;

    private ArrayList images;

    private Game game;

    /**
     * menü aktuális állapota
     * 0-3: főmenü
     *  0: start
     *  1: levels
     *  2: help
     *  3: exit
     * 4-5: súgó
     * 6-10: pályaválasztás
     *   6: 1. pálya
     *   7: 2. pálya
     *   8: 3. pálya
     *   9: 4. pálya
     *  10: 5. pálya
     */
    private int menuState = 0;

    /**
     * Konstruktor
     * @param mw a főablak, ami a panelt tartalmazza
     * @param g az a Game objektum, ami az aktuális játék modellje
     */
    public MenuPanel(MainWindow mw, Game g) {
        mainWindow = mw;
        game = g;
        initComponents();
        images = new ArrayList();
        Image image;
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        MediaTracker mediaTracker = new MediaTracker(this);
        image = toolkit.getImage("images\\MenuPlay.png");
        images.add(image);
        mediaTracker.addImage(image, 0);
        image = toolkit.getImage("images\\MenuLevels.png");
        images.add(image);
        mediaTracker.addImage(image, 1);
        image = toolkit.getImage("images\\MenuHelp.png");
        images.add(image);
        mediaTracker.addImage(image, 2);
        image = toolkit.getImage("images\\MenuExit.png");
        images.add(image);
        mediaTracker.addImage(image, 3);
        image = toolkit.getImage("images\\HelpMenu1.png");
        images.add(image);
        mediaTracker.addImage(image, 4);
        image = toolkit.getImage("images\\HelpMenu2.png");
        images.add(image);
        mediaTracker.addImage(image, 5);
        image = toolkit.getImage("images\\SelectLevel1.png");
        images.add(image);
        mediaTracker.addImage(image, 6);
        image = toolkit.getImage("images\\SelectLevel2.png");
        images.add(image);
        mediaTracker.addImage(image, 7);
        image = toolkit.getImage("images\\SelectLevel3.png");
        images.add(image);
        mediaTracker.addImage(image, 8);
        image = toolkit.getImage("images\\SelectLevel4.png");
        images.add(image);
        mediaTracker.addImage(image, 9);
        image = toolkit.getImage("images\\SelectLevel5.png");
        images.add(image);
        mediaTracker.addImage(image, 10);
        try {
            mediaTracker.waitForAll();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
            System.exit(0);
        }
    }

    private void initComponents() {
        setPreferredSize(new java.awt.Dimension(800, 600));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 800, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 600, Short.MAX_VALUE));
    }

    /**
     * A panel billentyűzet figyelője. A menüben történő navigálást oldja meg.
     */
    void menuPanelKeyListener(KeyEvent evt) {
        if (menuState < 4) {
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                menuState = (menuState + 1) % 4;
            } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
                menuState = menuState - 1;
                if (menuState < 0) menuState = 3;
            } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                switch(menuState) {
                    case 0:
                        mainWindow.setInGame(true);
                        break;
                    case 1:
                        menuState = game.getLevelNumber() + 5;
                        break;
                    case 2:
                        menuState = 4;
                        break;
                    case 3:
                        System.exit(0);
                        break;
                }
            } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
        } else if (menuState < 6) {
            if (evt.getKeyCode() == KeyEvent.VK_DOWN) {
                menuState = 5;
            } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
                menuState = 4;
            } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                menuState = 2;
            }
        } else if (menuState < 11) {
            if (evt.getKeyCode() == KeyEvent.VK_RIGHT) {
                menuState = Math.min(menuState + 1, 10);
            } else if (evt.getKeyCode() == KeyEvent.VK_LEFT) {
                menuState = Math.max(menuState - 1, 6);
            } else if (evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
                menuState = 1;
            } else if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                game.setLevelNumber(menuState - 5);
                menuState = 1;
            }
        }
        repaint();
    }

    /**
     * Aktuális állapothoz tartozó kép megjelenítése
     * @param g
     */
    public void paintComponent(Graphics g) {
        g.drawImage((Image) images.get(menuState), 0, 0, null);
    }
}
