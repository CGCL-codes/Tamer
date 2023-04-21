package com.usoog.creepattack.ui;

import com.usoog.creepattack.CAGameManager;
import com.usoog.creepattack.CAScaledCanvas;
import com.usoog.creepattack.CreepAttack;
import com.usoog.creepattack.language.CALanguageCenter;
import com.usoog.creepattack.ui.gamephase.TitleScreenGui;
import com.usoog.creepattack.ui.panel.LicenseFrame;
import com.usoog.creepattack.util.TextRefreshable;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.lang.reflect.InvocationTargetException;
import java.util.Locale;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a part of the GUI of Creep Attack. All other elements
 * need to be added to this class.
 *
 * @author Jimmy Axenhus
 */
public class Gui extends JPanel implements TextRefreshable {

    /**
	 * The logger for this class.
	 */
    private static final Logger LOGGER = LoggerFactory.getLogger(Gui.class);

    /**
	 * The Canvas used for the gui.
	 */
    private CAScaledCanvas canvas = new CAScaledCanvas();

    /**
	 * The CreepAttackApplet instance.
	 */
    private final CAGameManager gameManager;

    /**
	 * The CALanguageCenter used.
	 */
    private final CALanguageCenter languageCenter;

    /**
	 * The CreepAttackApplet instance this GUI is running at.
	 */
    private final CreepAttack creepAttack;

    /**
	 * Creates new form Gui.
	 *
	 * @param creepAttack The CreepAttackApplet to attach the GUI to.
	 */
    public Gui(CreepAttack creepAttack) {
        super();
        this.creepAttack = creepAttack;
        gameManager = creepAttack.getGameManager();
        languageCenter = gameManager.getTextCenter();
        try {
            startGui();
        } catch (Exception e) {
            LOGGER.error("Failed to start GUI!", e);
            throw new RuntimeException(e);
        }
    }

    /**
	 * Method to get the scaled canvas.
	 *
	 * @return The scaled canvas in use.
	 */
    public CAScaledCanvas getScaledCanvas() {
        return canvas;
    }

    /**
	 * Method to get the GameManager attached to this GUI.
	 *
	 * @return The GameManager attached to this GUI.
	 */
    public CAGameManager getGameManager() {
        return gameManager;
    }

    /**
	 * Method to clear the center panel.
	 */
    public void clearCenterPanel() {
        center.removeAll();
    }

    /**
	 * The panel to set as the main panel.
	 *
	 * @param panel The panel to set.
	 */
    public void setCenterPanel(JPanel panel) {
        center.removeAll();
        center.add(panel, BorderLayout.CENTER);
        invalidate();
        validate();
        repaint();
    }

    /**
	 * Private method to start the GUI thread.
	 *
	 * @throws InterruptedException If the GUI got interrupted.
	 * @throws InvocationTargetException If the GUI failed to be created.
	 */
    private void startGui() throws InterruptedException, InvocationTargetException {
        initComponents();
        addLanguageMenu();
        addSpeedMenu();
        setContent(new TitleScreenGui(Gui.this));
        setVisible(true);
        LOGGER.info("GUI has started");
    }

    /**
	 * Helper method to add the langauge switcher menu.
	 * TODO: the switcher isn't working correctly. why? disposing the gui
	 * and starting a new won't help.
	 */
    private void addLanguageMenu() {
        for (Locale loc : languageCenter.getLocales()) {
            languageMenu.add(getLanguageItem(loc));
        }
    }

    /**
	 * Helper method to get a language item for the menu.
	 *
	 * @param l The locale/language to get the item for.
	 * @return A JMenuItem.
	 */
    private JMenuItem getLanguageItem(final Locale l) {
        JMenuItem item = new JMenuItem(l.getDisplayLanguage(l));
        item.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                languageCenter.setLocale(l);
                doRefreshText(Gui.this);
            }
        });
        return item;
    }

    /**
	 * Method to recursively refresh the text and update for this container. It will
	 * travel the entire tree, looking for components implementing the
	 * TextRefreshable interface and call them.
	 *
	 * @param container The Container to check for TextRefreshable instances.
	 */
    private void doRefreshText(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof TextRefreshable) {
                TextRefreshable t = (TextRefreshable) c;
                t.refreshText();
            }
            if (c instanceof Container) {
                doRefreshText((Container) c);
            }
        }
    }

    /**
	 * Helper method to add the speed items to the menu.
	 */
    private void addSpeedMenu() {
        double[] speeds = { 0.25, 0.5, 1, 2, 4, 6, 10, 15, 20, 30, 50 };
        for (double i : speeds) {
            speedMenu.add(getSpeedItem(i));
        }
    }

    /**
	 * Helper method to get the menu speed select item for the specified
	 * speed.
	 *
	 * @param i The speed to get the item for.
	 * @return A JMenuItem with a mouse listener attached to listen for changed speed.
	 */
    private JMenuItem getSpeedItem(final double i) {
        JMenuItem item = new JMenuItem((int) (i * 100) + "%");
        item.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                LOGGER.info("Setting speed {}", i);
                gameManager.getGameLoopManager().setSpeed(i);
            }
        });
        return item;
    }

    /**
	 * Helper method to set the panel in the center.
	 *
	 * @param panel The panel to set.
	 */
    public void setContent(JPanel panel) {
        center.removeAll();
        center.add(panel);
        panel.setVisible(true);
        invalidate();
        validate();
        repaint();
    }

    @Override
    public void refreshText() {
        speedMenu.setText(languageCenter.getString("speed"));
    }

    public void setStatus(String status) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public String getStatus() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
	 * This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        menu = new javax.swing.JPopupMenu();
        languageMenu = new javax.swing.JMenu();
        speedMenu = new javax.swing.JMenu();
        licenseItem = new javax.swing.JMenuItem();
        topBar = new javax.swing.JPanel();
        menuButton = new javax.swing.JButton();
        center = new javax.swing.JPanel();
        rightBar = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        bottomBar = new javax.swing.JPanel();
        radio = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        languageMenu.setText("Language");
        menu.add(languageMenu);
        speedMenu.setText(languageCenter.getString("speed"));
        menu.add(speedMenu);
        licenseItem.setText(languageCenter.getString("license"));
        licenseItem.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                licenseItemMousePressed(evt);
            }
        });
        menu.add(licenseItem);
        setBackground(new java.awt.Color(1, 1, 1));
        setName("Creep Attack");
        setLayout(new java.awt.GridBagLayout());
        topBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        topBar.setFocusable(false);
        topBar.setLayout(new java.awt.BorderLayout());
        menuButton.setText(languageCenter.getString("menu"));
        menuButton.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuButtonMousePressed(evt);
            }
        });
        topBar.add(menuButton, java.awt.BorderLayout.LINE_START);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        add(topBar, gridBagConstraints);
        center.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        center.setFocusable(false);
        center.setLayout(new java.awt.BorderLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(center, gridBagConstraints);
        rightBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        rightBar.setLayout(new java.awt.GridBagLayout());
        jLabel3.setText("jLabel3");
        rightBar.add(jLabel3, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        add(rightBar, gridBagConstraints);
        bottomBar.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        bottomBar.setLayout(new java.awt.GridBagLayout());
        radio.setPreferredSize(new java.awt.Dimension(100, 20));
        javax.swing.GroupLayout radioLayout = new javax.swing.GroupLayout(radio);
        radio.setLayout(radioLayout);
        radioLayout.setHorizontalGroup(radioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 100, Short.MAX_VALUE));
        radioLayout.setVerticalGroup(radioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 20, Short.MAX_VALUE));
        bottomBar.add(radio, new java.awt.GridBagConstraints());
        jLabel1.setText("jLabel1");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 492, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(151, 151, 151).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(246, Short.MAX_VALUE))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 44, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addContainerGap(13, Short.MAX_VALUE))));
        bottomBar.add(jPanel1, new java.awt.GridBagConstraints());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 0.1;
        add(bottomBar, gridBagConstraints);
    }

    /**
	 * Method to handle that the user has hit the Menu button.
	 *
	 * @param evt The actual event which occurred.
	 */
    private void menuButtonMousePressed(java.awt.event.MouseEvent evt) {
        menu.show(evt.getComponent(), menuButton.getX(), menuButton.getY() + menuButton.getHeight());
    }

    /**
	 * Called when a license item was clicked.
	 *
	 * @param evt Ignored.
	 */
    private void licenseItemMousePressed(java.awt.event.MouseEvent evt) {
        LicenseFrame license = new LicenseFrame();
        license.setLocationRelativeTo(this);
        license.pack();
        license.setVisible(true);
    }

    private javax.swing.JPanel bottomBar;

    private javax.swing.JPanel center;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JMenu languageMenu;

    private javax.swing.JMenuItem licenseItem;

    private javax.swing.JPopupMenu menu;

    private javax.swing.JButton menuButton;

    private javax.swing.JPanel radio;

    private javax.swing.JPanel rightBar;

    private javax.swing.JMenu speedMenu;

    private javax.swing.JPanel topBar;
}
