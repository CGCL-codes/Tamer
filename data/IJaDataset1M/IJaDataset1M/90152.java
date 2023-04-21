package brgm.chipie;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * Component to be used as tabComponent;
 * Contains a JLabel to show the text and 
 * a JButton to close the tab it belongs to 
 */
public class OperationTabComponent extends JPanel {

    public static Image greenLight = null;

    public static Image orangeLight = null;

    private static Image closeButton = null;

    private static Image closeButtonOver = null;

    private static Image paramButton = null;

    private static Image paramButtonOver = null;

    private final JTabbedPane pane;

    private StateButton state;

    private SetParameterButton spb;

    private CloseButton cb;

    private JLabel label;

    private Color backgroundLabel;

    private boolean isBackgroundSelected;

    public OperationTabComponent(final JTabbedPane pane, boolean closable, boolean withParams) {
        super(new FlowLayout(FlowLayout.LEFT, 0, 0));
        try {
            if (closeButton == null) {
                closeButton = ImageIO.read(new File(System.getProperty("user.dir") + "/images/closeButton.png"));
                closeButtonOver = ImageIO.read(new File(System.getProperty("user.dir") + "/images/closeButtonOver.png"));
                paramButton = ImageIO.read(new File(System.getProperty("user.dir") + "/images/paramButton.png"));
                paramButtonOver = ImageIO.read(new File(System.getProperty("user.dir") + "/images/paramButtonOver.png"));
                greenLight = ImageIO.read(new File(System.getProperty("user.dir") + "/images/feu_vert.png"));
                orangeLight = ImageIO.read(new File(System.getProperty("user.dir") + "/images/feu_orange.png"));
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Images for tab operations not found");
        }
        if (pane == null) {
            throw new NullPointerException("TabbedPane is null");
        }
        this.pane = pane;
        setOpaque(false);
        addMouseListener(tabMouseListener);
        state = new StateButton();
        add(state, FlowLayout.LEFT);
        label = new JLabel() {

            public String getText() {
                int i = pane.indexOfTabComponent(OperationTabComponent.this);
                if (i != -1) {
                    return pane.getTitleAt(i);
                }
                return null;
            }
        };
        label.setAlignmentX(LEFT_ALIGNMENT);
        backgroundLabel = this.getBackground();
        add(label, FlowLayout.LEFT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        if (withParams) {
            spb = new SetParameterButton();
            add(spb);
        }
        if (closable) {
            cb = new CloseButton();
            add(cb);
        }
        setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
    }

    public void setBusyMode(boolean b) {
        state.setBusyMode(b);
        state.paintImmediately(state.getBounds());
    }

    public void setBackgroundSelected(boolean b) {
        isBackgroundSelected = b;
        if (b) {
            label.setBackground(Color.orange);
            this.setBackground(Color.orange);
            this.repaint();
        } else {
            label.setBackground(backgroundLabel);
            this.setBackground(backgroundLabel);
            label.repaint();
        }
    }

    public void doBackgroundSelect() {
        OperationTabComponent c;
        for (int i = 0; i < pane.getTabCount(); i++) {
            c = (OperationTabComponent) pane.getTabComponentAt(i);
            if (c.isBackgroundSelected) c.setBackgroundSelected(false);
        }
        setBackgroundSelected(true);
    }

    /**
     * 
     * @author morel
     *
     */
    private class CloseButton extends JButton implements MouseListener {

        private boolean modeOver = false;

        public CloseButton() {
            int size = 16;
            setPreferredSize(new Dimension(size + 4, size));
            setToolTipText("Fermer cet onglet");
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            setRolloverEnabled(true);
            addMouseListener(this);
        }

        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                CloseButton button = (CloseButton) component;
                button.setModeOver(true);
                button.repaint();
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                CloseButton button = (CloseButton) component;
                button.setModeOver(false);
                button.repaint();
            }
        }

        public void mouseClicked(MouseEvent e) {
            int i = pane.indexOfTabComponent(OperationTabComponent.this);
            if (i != -1) {
                pane.remove(i);
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        ;

        public void mouseReleased(MouseEvent e) {
        }

        ;

        public void setModeOver(boolean b) {
            modeOver = b;
        }

        public void updateUI() {
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            g2.setColor(Color.BLACK);
            if (isBackgroundSelected) {
                g2.setColor(Color.orange);
            }
            int delta = 2;
            if (modeOver) {
                g2.drawImage(closeButtonOver, delta, delta, null);
            } else {
                g2.drawImage(closeButton, delta, delta, null);
            }
            g2.dispose();
        }
    }

    /**
     * 
     * @author morel
     *
     */
    private class SetParameterButton extends JButton implements MouseListener {

        private boolean modeOver;

        public SetParameterButton() {
            int size = 16;
            setPreferredSize(new Dimension(size + 4, size));
            setToolTipText("Changer les valeurs des param�tres");
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            setRolloverEnabled(true);
            addMouseListener(this);
        }

        public void mouseEntered(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                SetParameterButton button = (SetParameterButton) component;
                button.setModeOver(true);
                button.repaint();
            }
        }

        public void mouseExited(MouseEvent e) {
            Component component = e.getComponent();
            if (component instanceof AbstractButton) {
                SetParameterButton button = (SetParameterButton) component;
                button.setModeOver(false);
                button.repaint();
            }
        }

        public void mouseClicked(MouseEvent e) {
            int i = pane.indexOfTabComponent(OperationTabComponent.this);
            if (i != -1) {
                OperationFrame f = (OperationFrame) pane.getComponentAt(i);
                f.showParametersFrame(e.getXOnScreen(), e.getYOnScreen());
            }
        }

        public void mousePressed(MouseEvent e) {
        }

        ;

        public void mouseReleased(MouseEvent e) {
        }

        ;

        public void setModeOver(boolean b) {
            modeOver = b;
        }

        public void updateUI() {
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            if (getModel().isPressed()) {
                g2.translate(1, 1);
            }
            if (isBackgroundSelected) {
                g2.setColor(Color.orange);
            }
            if (getModel().isRollover()) {
                g2.setColor(Color.GREEN);
            }
            int delta = 2;
            if (modeOver) {
                g2.drawImage(paramButtonOver, delta, delta, null);
            } else {
                g2.drawImage(paramButton, delta, delta, null);
            }
            g2.dispose();
        }
    }

    /**
     * 
     * @author morel
     *
     */
    private class StateButton extends JButton {

        private boolean busyMode;

        public StateButton() {
            int size = 16;
            setPreferredSize(new Dimension(size + 4, size));
            setContentAreaFilled(false);
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            setBusyMode(false);
        }

        public void updateUI() {
        }

        public void setBusyMode(boolean b) {
            busyMode = b;
            setToolTipText(busyMode ? "Calcul en cours ..." : "Calcul�");
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            if (isBackgroundSelected) {
                g2.setColor(Color.orange);
            }
            int delta = 2;
            if (busyMode) {
                g2.drawImage(orangeLight, delta, delta, null);
            } else {
                g2.drawImage(greenLight, delta, delta, null);
            }
            g2.dispose();
        }
    }

    /**
     * 
     */
    private static final MouseListener tabMouseListener = new MouseAdapter() {

        public void mouseClicked(MouseEvent e) {
            OperationTabComponent cRef = (OperationTabComponent) e.getComponent();
            if (e.isControlDown()) {
                cRef.doBackgroundSelect();
            } else {
                cRef.pane.setSelectedIndex(cRef.pane.indexOfTabComponent(cRef));
            }
        }
    };

    public boolean isBackgroundSelected() {
        return isBackgroundSelected;
    }
}
