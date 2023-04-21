package org.tex4java.presenter;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.tex4java.Manager;

/**
 * The presenter.
 *
 * This program and the accompanying materials are made available under
 * the terms of the Common Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/cpl-v10.html
 * 
 * @author <a href="mailto:paladin@cs.tu-berlin.de">Peter Annuss </a>
 * @author <a href="mailto:thomas@dohmke.de">Thomas Dohmke </a>
 * @version $Revision: 1.2 $
 */
public class Presenter extends JFrame implements AdjustmentListener {

    protected Manager manager;

    public BoxPanel boxPanel;

    GraphicsDevice device;

    boolean fullScreen = true;

    boolean fullScreenSupported = false;

    MenuMain menu = null;

    javax.swing.Timer timer;

    Presenter child = null;

    Presenter parent = null;

    private JScrollBar verticalScrollbar;

    private JScrollBar horizontalScrollbar;

    public Presenter(Manager manager) throws Exception {
        this.manager = manager;
        GraphicsEnvironment graphEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
        device = graphEnv.getDefaultScreenDevice();
        fullScreenSupported = device.isFullScreenSupported();
    }

    public void run(boolean runFullScreen) {
        run(runFullScreen, null, null);
    }

    public void run(boolean runFullScreen, BoxPanel oldBoxPanel, MenuMain menu) {
        this.fullScreen = runFullScreen;
        setTitle("TeXPresenter");
        this.addWindowListener(new WindowAdapter() {

            public void windowDeactivated(WindowEvent e) {
                handleWindowDeactivated(e);
            }

            public void windowClosing(WindowEvent e) {
                handleWindowClosing(e);
            }
        });
        this.addKeyListener(new KeyAdapter() {

            public void keyPressed(KeyEvent e) {
                handleKeyPressed(e);
            }
        });
        if (oldBoxPanel == null) {
            this.boxPanel = new BoxPanel(manager);
            setSize(new Dimension(640, 480));
        } else {
            this.boxPanel = new BoxPanel(oldBoxPanel);
        }
        if (!fullScreen) {
            verticalScrollbar = new JScrollBar(Scrollbar.VERTICAL, 0, 0, -1000, 1000);
            verticalScrollbar.addAdjustmentListener(this);
            horizontalScrollbar = new JScrollBar(Scrollbar.HORIZONTAL, 0, 0, -1000, 1000);
            horizontalScrollbar.addAdjustmentListener(this);
            horizontalScrollbar.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    handleKeyPressed(e);
                }
            });
            verticalScrollbar.addKeyListener(new KeyAdapter() {

                public void keyPressed(KeyEvent e) {
                    handleKeyPressed(e);
                }
            });
        }
        addToPane(boxPanel);
        boxPanel.countPages();
        boxPanel.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                handleMousePressed(e);
            }
        });
        if (menu == null) {
            this.menu = new MenuMain(this, boxPanel);
        } else {
            this.menu = menu;
            this.menu.presenter = this;
            this.menu.boxPanel = boxPanel;
        }
        if ((fullScreenSupported) && (fullScreen)) {
            this.setUndecorated(true);
            this.setResizable(false);
            device.setFullScreenWindow(this);
            this.validate();
            timer = new javax.swing.Timer(250, new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    boxPanel.slideRec = null;
                    boxPanel.repaint();
                    timer.stop();
                }
            });
            timer.start();
        } else {
            this.setJMenuBar(this.menu.menu);
            this.setVisible(true);
        }
    }

    public void addToPane(BoxPanel boxPanel) {
        if (!fullScreen) {
            getContentPane().removeAll();
            GridBagLayout gb = new GridBagLayout();
            getContentPane().setLayout(gb);
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.fill = GridBagConstraints.VERTICAL;
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 0;
            c.weighty = 1;
            gb.setConstraints(verticalScrollbar, c);
            c.gridx = 0;
            c.gridy = 1;
            c.fill = GridBagConstraints.HORIZONTAL;
            c.weightx = 1;
            c.weighty = 0;
            gb.setConstraints(horizontalScrollbar, c);
            c.gridx = 0;
            c.gridy = 0;
            c.gridwidth = 1;
            c.gridheight = 1;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.CENTER;
            c.weightx = 1;
            c.weighty = 1;
            gb.setConstraints(boxPanel, c);
            getContentPane().add(boxPanel);
            getContentPane().add(verticalScrollbar);
            getContentPane().add(horizontalScrollbar);
        } else {
            getContentPane().add(boxPanel, BorderLayout.CENTER);
        }
    }

    public void handleMousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            boxPanel.nextPage();
        }
        if (e.getButton() == MouseEvent.BUTTON3) {
            menu.popupMenuView((Component) e.getSource(), e.getX(), e.getY());
        }
        menu.menuView.updateItems();
    }

    public void handleWindowDeactivated(WindowEvent e) {
        if (fullScreen) {
            menu.setItemFullScreen(false);
            parent.boxPanel.set(this.boxPanel);
            this.dispose();
            parent.boxPanel.repaint();
            parent.menu.presenter = parent;
            parent.menu.boxPanel = parent.boxPanel;
        }
    }

    public void handleWindowClosing(WindowEvent e) {
        if (fullScreen) {
            handleWindowDeactivated(e);
        } else {
            System.exit(0);
        }
    }

    public void handleKeyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_ESCAPE) && (fullScreen)) {
            handleWindowDeactivated(null);
        }
        if (e.getKeyCode() == KeyEvent.VK_PAGE_UP) {
            boxPanel.prevPage();
            e.consume();
        }
        if (e.getKeyCode() == KeyEvent.VK_HOME) {
            boxPanel.goToPage(0);
            e.consume();
        }
        if (e.getKeyCode() == KeyEvent.VK_END) {
            boxPanel.goToPage(boxPanel.maxPages);
            e.consume();
        }
        if ((e.getKeyCode() == KeyEvent.VK_PAGE_DOWN) || (e.getKeyCode() == KeyEvent.VK_SPACE) || (e.getKeyCode() == KeyEvent.VK_ENTER)) {
            boxPanel.nextPage();
            e.consume();
        }
        int offset = 0;
        if (e.isShiftDown() && !e.isControlDown() && !e.isAltDown()) {
            offset = 10;
        }
        if (e.isShiftDown() && e.isControlDown() && !e.isAltDown()) {
            offset = 20;
        }
        if (!e.isShiftDown() && !e.isControlDown() && e.isAltDown()) {
            offset = 30;
        }
        if (e.isShiftDown() && e.isControlDown() && !e.isAltDown()) {
            offset = 40;
        }
        if (e.isShiftDown() && !e.isControlDown() && e.isAltDown()) {
            offset = 50;
        }
        if (!e.isShiftDown() && e.isControlDown() && e.isAltDown()) {
            offset = 60;
        }
        if (e.isShiftDown() && e.isControlDown() && e.isAltDown()) {
            offset = 70;
        }
        if (e.getKeyCode() == KeyEvent.VK_1) {
            boxPanel.goToPage(0 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            boxPanel.goToPage(1 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            boxPanel.goToPage(2 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_4) {
            boxPanel.goToPage(3 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_5) {
            boxPanel.goToPage(4 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_6) {
            boxPanel.goToPage(5 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_7) {
            boxPanel.goToPage(6 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_8) {
            boxPanel.goToPage(7 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_9) {
            boxPanel.goToPage(8 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_0) {
            boxPanel.goToPage(9 + offset);
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            boxPanel.forward = true;
            boxPanel.stopAnimation();
            boxPanel.resetAnimate();
            boxPanel.slideRec = null;
            boxPanel.repaint();
            e.consume();
        } else if (e.getKeyCode() == KeyEvent.VK_O) {
            boxPanel.x0 = 0;
            boxPanel.y0 = 0;
            if (!fullScreen) {
                verticalScrollbar.setValue(0);
                horizontalScrollbar.setValue(0);
            }
            boxPanel.slideRec = null;
            boxPanel.repaint();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (fullScreen) {
                boxPanel.y0 -= 2 * (e.isShiftDown() ? 10 : 1);
                boxPanel.slideRec = null;
                boxPanel.repaint();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (fullScreen) {
                boxPanel.y0 += 2 * (e.isShiftDown() ? 10 : 1);
                boxPanel.slideRec = null;
                boxPanel.repaint();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (fullScreen) {
                boxPanel.x0 -= 2 * (e.isShiftDown() ? 10 : 1);
                boxPanel.slideRec = null;
                boxPanel.repaint();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (fullScreen) {
                boxPanel.x0 += 2 * (e.isShiftDown() ? 10 : 1);
                boxPanel.slideRec = null;
                boxPanel.repaint();
            }
        }
        menu.menuView.updateItems();
    }

    public void startFullScreen(MenuMain menu) {
        try {
            child = new Presenter(manager);
            child.parent = this;
            child.run(true, boxPanel, menu);
        } catch (Exception x) {
        }
    }

    public void stopFullScreen() {
        handleWindowDeactivated(null);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        boxPanel.x0 = -1 * horizontalScrollbar.getValue();
        boxPanel.y0 = -1 * verticalScrollbar.getValue();
        boxPanel.slideRec = null;
        boxPanel.repaint();
    }

    public static Image getImage(String filename, Component component) {
        Image image = component.getToolkit().getImage(filename);
        MediaTracker tracker = new MediaTracker(component);
        tracker.addImage(image, 0);
        try {
            tracker.waitForAll();
        } catch (InterruptedException ie) {
        }
        return image;
    }
}
