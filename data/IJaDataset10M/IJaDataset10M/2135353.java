package net.sf.jtonic.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.SwingUtilities;
import net.sf.jtonic.core.Master;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class GUI {

    private Master master;

    private SettingsView settingsview;

    private VendorInterface vi;

    private GuestInterface gi;

    private JFrame frame;

    private JMenuBar menuBar;

    private JButton jButtonStartSim;

    private JButton jButtonSettings;

    private JButton jButtonGI;

    private JButton jButtonVI;

    private JPanel contentPanel;

    private JMenuItem menuClose;

    private JMenu menuMain;

    /**
	* Auto-generated main method to display this JFrame
	*/
    public GUI(Master master) {
        super();
        this.master = master;
        getFrame().requestFocus();
        getFrame().setVisible(true);
    }

    public void start() {
        getJButtonSettings().setEnabled(false);
        getJButtonGI().setEnabled(true);
        getJButtonVI().setEnabled(true);
    }

    public void updateGUI() {
        getVendorInterface().updateGUI();
        getGuestInterface().updateGUI();
    }

    private VendorInterface getVendorInterface() {
        if (vi == null) {
            vi = new VendorInterface(master.getDrinks());
        }
        return vi;
    }

    private GuestInterface getGuestInterface() {
        if (gi == null) {
            gi = new GuestInterface(master.getProperties(), master.getDrinks());
        }
        return gi;
    }

    private JFrame getFrame() {
        if (frame == null) {
            frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.pack();
            frame.setSize(392, 271);
            frame.setTitle("jTonic 0.0");
            frame.setVisible(true);
            frame.getContentPane().add(getContentPanel(), BorderLayout.CENTER);
            frame.setJMenuBar(getThisMenuBar());
        }
        return frame;
    }

    private JMenuBar getThisMenuBar() {
        if (menuBar == null) {
            menuBar = new JMenuBar();
            menuBar.add(getMenuMain());
        }
        return menuBar;
    }

    private JMenu getMenuMain() {
        if (menuMain == null) {
            menuMain = new JMenu();
            menuMain.setText("jTonic");
            menuMain.add(getMenuClose());
        }
        return menuMain;
    }

    private JMenuItem getMenuClose() {
        if (menuClose == null) {
            menuClose = new JMenuItem();
            menuClose.setText("Exit");
            menuClose.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
        }
        return menuClose;
    }

    private JPanel getContentPanel() {
        if (contentPanel == null) {
            contentPanel = new JPanel();
            GroupLayout contentPanelLayout = new GroupLayout((JComponent) contentPanel);
            contentPanel.setLayout(contentPanelLayout);
            contentPanel.setPreferredSize(new java.awt.Dimension(419, 294));
            contentPanelLayout.setHorizontalGroup(contentPanelLayout.createSequentialGroup().addContainerGap().addGroup(contentPanelLayout.createParallelGroup().addComponent(getJButtonStartSim(), GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE).addComponent(getJButtonSettings(), GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(contentPanelLayout.createParallelGroup().addGroup(contentPanelLayout.createSequentialGroup().addComponent(getJButtonVI(), GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.LEADING, contentPanelLayout.createSequentialGroup().addComponent(getJButtonGI(), GroupLayout.PREFERRED_SIZE, 172, GroupLayout.PREFERRED_SIZE))).addContainerGap(100, Short.MAX_VALUE));
            contentPanelLayout.linkSize(SwingConstants.HORIZONTAL, new Component[] { getJButtonStartSim(), getJButtonVI(), getJButtonSettings(), getJButtonGI() });
            contentPanelLayout.setVerticalGroup(contentPanelLayout.createSequentialGroup().addContainerGap().addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(getJButtonStartSim(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE).addComponent(getJButtonVI(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(getJButtonSettings(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE).addComponent(getJButtonGI(), GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)).addContainerGap(243, 243));
            contentPanelLayout.linkSize(SwingConstants.VERTICAL, new Component[] { getJButtonStartSim(), getJButtonVI(), getJButtonSettings(), getJButtonGI() });
        }
        return contentPanel;
    }

    private JButton getJButtonStartSim() {
        if (jButtonStartSim == null) {
            jButtonStartSim = new JButton();
            jButtonStartSim.setText("Start Simulation");
            jButtonStartSim.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    master.startSimulation();
                }
            });
        }
        return jButtonStartSim;
    }

    private JButton getJButtonVI() {
        if (jButtonVI == null) {
            jButtonVI = new JButton();
            jButtonVI.setText("Vendor Interface");
            jButtonVI.setEnabled(false);
            jButtonVI.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    getVendorInterface().setVisible(true);
                }
            });
        }
        return jButtonVI;
    }

    private JButton getJButtonSettings() {
        if (jButtonSettings == null) {
            jButtonSettings = new JButton();
            jButtonSettings.setText("Settings");
            jButtonSettings.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    if (settingsview == null) {
                        settingsview = new SettingsView(master.getProperties());
                    }
                    settingsview.setVisible(true);
                }
            });
        }
        return jButtonSettings;
    }

    private JButton getJButtonGI() {
        if (jButtonGI == null) {
            jButtonGI = new JButton();
            jButtonGI.setText("Guest Interface");
            jButtonGI.setEnabled(false);
            jButtonGI.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent evt) {
                    getGuestInterface().setVisible(true);
                }
            });
        }
        return jButtonGI;
    }
}
