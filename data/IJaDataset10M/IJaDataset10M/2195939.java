package org.openintents.tools.simulator.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openintents.tools.simulator.Global;
import org.openintents.tools.simulator.SensorSimulator;
import org.openintents.tools.simulator.SimulatorInstances;
import org.openintents.tools.simulator.TelnetSimulator;
import org.openintents.tools.simulator.view.help.AboutWindow;

/**
 * Main class of our sensor simulator. This class creates a Frame that is filled
 * by SensorSimulator class, creates a menu bar and panel for tabs.
 * 
 * @author Josip Balic
 */
public class SensorSimulatorMain extends JPanel implements WindowListener, ChangeListener, ItemListener {

    private static final long serialVersionUID = -7248177045429153977L;

    public SimulatorInstances simulatorInstances = new SimulatorInstances();

    private SensorSimulator mSimulator;

    private TelnetSimulator mTelnet;

    private JLabel mStatusBar;

    /**
	 * Create the GUI and show it. For thread safety, this method should be
	 * invoked from the event-dispatching thread.
	 */
    private void createAndShowGUI() {
        JFrame frame = new JFrame("SensorSimulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(Toolkit.getDefaultToolkit().getImage(Global.ICON_SENSOR_SIMULATOR_PATH));
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel menuBar = new JPanel();
        JPanel contentPanel = new JPanel();
        mStatusBar = new JLabel();
        JPanel statusPanel = new JPanel();
        fillMainPanel(menuBar, contentPanel, frame);
        fillStatusPanel(statusPanel, frame);
        menuBar.setBorder(new EmptyBorder(Global.BORDER_VSIZE, Global.BORDER_HSIZE, Global.BORDER_VSIZE, Global.BORDER_HSIZE));
        mainPanel.add(menuBar, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(statusPanel, BorderLayout.SOUTH);
        mainPanel.setPreferredSize(new Dimension(Global.W_FRAME + 150, Global.H_FRAME));
        frame.add(mainPanel);
        frame.setVisible(true);
        frame.pack();
    }

    private void fillStatusPanel(JPanel statusPanel, JFrame frame) {
        statusPanel.setLayout(new GridLayout(1, 1));
        statusPanel.setDoubleBuffered(false);
        statusPanel.add(mStatusBar);
        statusPanel.setBorder(BorderFactory.createLoweredBevelBorder());
    }

    /**
	 * Creates the main panel, with a menu bar and a container panel.
	 * 
	 * @param menubar
	 * @param contentPanel
	 * @param frame
	 * 
	 * @return menu bar panel
	 */
    private void fillMainPanel(JPanel menuBar, JPanel contentPanel, JFrame frame) {
        contentPanel.setLayout(new BorderLayout());
        menuBar.setLayout(new BoxLayout(menuBar, BoxLayout.X_AXIS));
        JButton simulatorButton = createSimulatorButton(contentPanel, frame);
        menuBar.add(simulatorButton);
        JButton telnetButton = createTelnetButton(contentPanel, frame);
        menuBar.add(telnetButton);
        JButton settingsButton = createSettingsButton(contentPanel, frame);
        menuBar.add(Box.createHorizontalGlue());
        settingsButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        menuBar.add(settingsButton);
        JButton aboutButton = createAboutButton();
        aboutButton.setAlignmentX(Component.RIGHT_ALIGNMENT);
        menuBar.add(aboutButton);
    }

    private JButton createSettingsButton(final JPanel contentPanel, final JFrame frame) {
        JButton menuButton = new JButton(Global.MENU_SETTINGS);
        SpringLayout layout = new SpringLayout();
        final JPanel settingsPanel = new JPanel(layout);
        settingsPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Global.COLOR_ENABLE_BLUE), BorderFactory.createEmptyBorder(Global.BORDER_HSIZE, Global.BORDER_HSIZE, Global.BORDER_HSIZE, Global.BORDER_HSIZE)));
        settingsPanel.add(mSimulator.view.getSettingsPanel());
        menuButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.removeAll();
                contentPanel.add(settingsPanel);
                printStatus("Settings");
                contentPanel.validate();
                contentPanel.repaint();
            }
        });
        menuButton.setToolTipText("Settings");
        return menuButton;
    }

    private JButton createTelnetButton(final JPanel contentPanel, final JFrame frame) {
        JButton menuButton = new JButton(Global.MENU_CONSOLE);
        mTelnet = new TelnetSimulator(this);
        final JScrollPane telnetScroll = new JScrollPane(mTelnet.view);
        telnetScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        menuButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.removeAll();
                contentPanel.add(telnetScroll);
                printStatus("Telnet Simulator");
                contentPanel.validate();
                contentPanel.repaint();
            }
        });
        menuButton.setToolTipText("Telnet Simulator");
        return menuButton;
    }

    private JButton createSimulatorButton(final JPanel contentPanel, final JFrame frame) {
        JButton menuButton = new JButton(Global.MENU_SENSOR_SIMULATOR);
        mSimulator = new SensorSimulator(this);
        setFirstSimulatorInstance(mSimulator);
        final JScrollPane simulatorScroll = new JScrollPane(mSimulator.view);
        simulatorScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        contentPanel.add(simulatorScroll);
        printStatus("Sensor Simulator");
        menuButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.removeAll();
                contentPanel.add(simulatorScroll);
                printStatus("Sensor Simulator");
                contentPanel.repaint();
                frame.pack();
            }
        });
        menuButton.setToolTipText("Sensor Simulator");
        return menuButton;
    }

    private JButton createAboutButton() {
        JButton menuButton = new JButton(Global.MENU_ABOUT);
        menuButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                AboutWindow wnd = new AboutWindow();
                wnd.setVisible(true);
            }
        });
        menuButton.setToolTipText("About");
        return menuButton;
    }

    /**
	 * Method that adds instance of our first simulator tab to
	 * SimulatorInstances
	 * 
	 * @param sensorSimulator
	 *            , SensorSimulator instance we want to add.
	 */
    private void setFirstSimulatorInstance(SensorSimulator simulator) {
        simulatorInstances.addSimulator(simulator);
    }

    /** Add a listener for window events. */
    void addWindowListener(Window w) {
        w.addWindowListener(this);
    }

    /**
	 * Main method of SensorSimulatorMain class.
	 * 
	 * @param args
	 *            , String[] arguments used to run this GUI.
	 */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
            UIManager.put("control", Global.BACK);
            UIManager.put("text", Global.TEXT);
            UIManager.put("scrollbar", Global.TEXT);
            UIManager.put("nimbusBlueGrey", Global.BUTTON);
            UIManager.put("nimbusBase", Global.TAB);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        final SensorSimulatorMain mainSensorSimulator = new SensorSimulatorMain();
        new Global().initGlobal();
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                mainSensorSimulator.createAndShowGUI();
            }
        });
    }

    /**
	 * Called to set status in the program status bar.
	 * 
	 * @param status
	 */
    public void printStatus(String status) {
        mStatusBar.setText(" " + status);
    }

    /**
	 * This method is invoked when action happens.
	 * 
	 * @param e
	 *            , ActionEvent that generated action.
	 */
    @Override
    public void windowDeiconified(WindowEvent arg0) {
    }

    @Override
    public void windowIconified(WindowEvent arg0) {
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
    }

    @Override
    public void itemStateChanged(ItemEvent arg0) {
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
