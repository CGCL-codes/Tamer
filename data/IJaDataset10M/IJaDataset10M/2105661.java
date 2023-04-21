package cz.zcu.fav.os.vm;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Main window for starting up virtual machine. Implemented as singleton class.
 *
 * @author Tomáš Hofhans
 * @since 1.11.2009
 */
public class VMStartupWindow extends JFrame {

    /** Default SVUID. */
    private static final long serialVersionUID = 1L;

    /** Frame width. */
    private static final int WIDTH = 250;

    /** Frame height. */
    private static final int HEIGHT = 150;

    /** Singleton instance. */
    private static VMStartupWindow instance;

    /** Main window panel. */
    private JPanel mainPanel;

    /** User name. */
    private JTextField name;

    /** Observable for listeners. */
    private VMStartupWObservable observable;

    private GridBagConstraints gbc;

    private GridBagLayout gbl;

    private static final int CEN = GridBagConstraints.CENTER;

    private static final int NON = GridBagConstraints.NONE;

    /**
   * Hidden constructor.
   * @param config configuration
   */
    private VMStartupWindow() {
        super();
        init();
    }

    /**
   * Initialization.
   */
    private void init() {
        mainPanel = new JPanel();
        gbl = new GridBagLayout();
        mainPanel.setLayout(gbl);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        name = new JTextField("User");
        name.setPreferredSize(new Dimension(150, 25));
        observable = new VMStartupWObservable();
        add(mainPanel);
        JButton lStartUp = new JButton("Start up VM");
        lStartUp.addActionListener(new ActionListener() {

            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                instance.observable.clickStartAction();
            }
        });
        JButton lEnd = new JButton("Turn off");
        lEnd.addActionListener(new ActionListener() {

            /** {@inheritDoc} */
            public void actionPerformed(ActionEvent e) {
                instance.observable.clickTurnOffAction();
            }
        });
        add(name, 0, 0, 2, 1, 1.0, 1.0, NON, CEN, mainPanel);
        add(lEnd, 0, 1, 1, 1, 1.0, 1.0, NON, CEN, mainPanel);
        add(lStartUp, 1, 1, 1, 1, 1.0, 1.0, NON, CEN, mainPanel);
        setBounds(200, 200, WIDTH, HEIGHT);
        setTitle("VMM");
        setVisible(true);
        repaint();
    }

    /**
   * Get setted name.
   * @return setted name in field
   */
    public String getName() {
        return name.getText();
    }

    /** Add component to layout. */
    private void add(Component c, int x, int y, int s, int v, double rs, double rv, int fill, int k, Container cont) {
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.gridwidth = s;
        gbc.gridheight = v;
        gbc.weightx = rs;
        gbc.weighty = rv;
        gbc.fill = fill;
        gbc.anchor = k;
        gbl.setConstraints(c, gbc);
        cont.add(c);
    }

    /**
   * Get singleton instance.
   * @return singleton instance
   */
    public static VMStartupWindow getInstance() {
        if (instance == null) {
            synchronized (VMStartupWindow.class) {
                if (instance == null) {
                    instance = new VMStartupWindow();
                }
            }
        }
        return instance;
    }

    /**
   * Enumeration VM startup window actions.
   * Will be use as parameters for listeners.
   * @author Tomáš Hofhans
   * @since 3.11.2009
   */
    public enum VMStartupWindowAction {

        /** New virtual machine start. */
        START, /** Turn off virtual machine manager. */
        TURN_OFF
    }

    /**
   * Inner class inherited from observable.
   *
   * @author Tomáš Hofhans
   */
    private static class VMStartupWObservable extends Observable {

        /**
     * When we click to end button notify observers.
     */
        private void clickTurnOffAction() {
            setChanged();
            notifyObservers(VMStartupWindowAction.TURN_OFF);
        }

        /**
     * When we click to start button notify observers.
     */
        private void clickStartAction() {
            setChanged();
            notifyObservers(VMStartupWindowAction.START);
        }
    }

    /**
   * Registration to observer.
   *
   * @param o observer
   */
    public void registerToObserver(Observer o) {
        observable.addObserver(o);
    }
}
