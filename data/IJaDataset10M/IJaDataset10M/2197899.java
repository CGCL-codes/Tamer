package ucl.cs.testingEmulator.contexNotifierScripts;

import java.awt.GridBagLayout;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileSystemRegistry;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import java.awt.Dimension;
import java.io.IOException;
import javax.swing.JCheckBox;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import ucl.cs.testingEmulator.connection.file.EmulatedFileSystem;
import ucl.cs.testingEmulator.time.VirtualClock;
import ucl.cs.testingEmulator.time.VirtualClockStatus;

/**
 * @author -Michele Sama- aka -RAX-
 * 
 * University College London
 * Dept. of Computer Science
 * Gower Street
 * London WC1E 6BT
 * United Kingdom
 *
 * Email: M.Sama (at) cs.ucl.ac.uk
 *
 * Group:
 * Software Systems Engineering
 *
 */
public class CommandJPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    private JSlider jSliderTime = null;

    private JButton jButtonFilesystem = null;

    private JPanel jPanelBT = null;

    private JCheckBox jCheckBoxBTMyComputer = null;

    private JCheckBox jCheckBoxBTBossComputer = null;

    private JCheckBox jCheckBoxBTBossMobile = null;

    private JPanel jPanelGPSLocation = null;

    private JCheckBox jCheckBoxGPSMyOffice = null;

    private JCheckBox jCheckBoxGPSBossOffice = null;

    /**
	 * This is the default constructor
	 */
    public CommandJPanel() {
        super();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
        gridBagConstraints21.gridx = 0;
        gridBagConstraints21.gridy = 3;
        GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
        gridBagConstraints11.gridx = 0;
        gridBagConstraints11.gridy = 2;
        GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
        gridBagConstraints1.gridx = 0;
        gridBagConstraints1.gridy = 0;
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.VERTICAL;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.gridx = 0;
        this.setSize(300, 298);
        this.setLayout(new GridBagLayout());
        this.add(getJSliderTime(), gridBagConstraints);
        this.add(getJButtonFilesystem(), gridBagConstraints1);
        this.add(getJPanelBT(), gridBagConstraints11);
        this.add(getJPanelGPSLocation(), gridBagConstraints21);
    }

    /**
	 * This method initializes jSliderTime	
	 * 	
	 * @return javax.swing.JSlider	
	 */
    private JSlider getJSliderTime() {
        if (jSliderTime == null) {
            jSliderTime = new JSlider();
            jSliderTime.setMaximum(30);
            jSliderTime.setPaintLabels(true);
            jSliderTime.setMinorTickSpacing(1);
            jSliderTime.setMajorTickSpacing(5);
            jSliderTime.setValue(0);
            jSliderTime.setPaintTicks(true);
            jSliderTime.addChangeListener(new javax.swing.event.ChangeListener() {

                public void stateChanged(javax.swing.event.ChangeEvent e) {
                    int k = jSliderTime.getValue();
                    VirtualClock.getInstance().setStatus(VirtualClockStatus.TIME_STOPPED_STATUS);
                    VirtualClock.setSimulatedTime(k * 60 * 1000);
                }
            });
        }
        return jSliderTime;
    }

    /**
	 * This method initializes jButtonFilesystem	
	 * 	
	 * @return javax.swing.JButton	
	 */
    private JButton getJButtonFilesystem() {
        if (jButtonFilesystem == null) {
            jButtonFilesystem = new JButton();
            jButtonFilesystem.setText("FileSystem");
            jButtonFilesystem.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    FileSystemRegistry registry = FileSystemRegistry.getInstance();
                    EmulatedFileSystem sys = new EmulatedFileSystem("/Users/rax/j2meFakeRoot", "/NewRoot0", Connector.READ_WRITE);
                    registry.addFileSystem(sys);
                }
            });
        }
        return jButtonFilesystem;
    }

    /**
	 * This method initializes jPanelBT	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelBT() {
        if (jPanelBT == null) {
            GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
            gridBagConstraints4.gridx = 0;
            gridBagConstraints4.gridy = 2;
            GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
            gridBagConstraints3.gridx = 0;
            gridBagConstraints3.gridy = 1;
            GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
            gridBagConstraints2.gridx = 0;
            gridBagConstraints2.gridy = 0;
            jPanelBT = new JPanel();
            jPanelBT.setLayout(new GridBagLayout());
            jPanelBT.setBorder(BorderFactory.createTitledBorder(null, "BlueTooth", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
            jPanelBT.setPreferredSize(new Dimension(190, 94));
            jPanelBT.add(getJCheckBoxBTMyComputer(), gridBagConstraints2);
            jPanelBT.add(getJCheckBoxBTBossComputer(), gridBagConstraints3);
            jPanelBT.add(getJCheckBoxBTBossMobile(), gridBagConstraints4);
        }
        return jPanelBT;
    }

    /**
	 * This method initializes jCheckBoxBTMyComputer	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getJCheckBoxBTMyComputer() {
        if (jCheckBoxBTMyComputer == null) {
            jCheckBoxBTMyComputer = new JCheckBox();
            jCheckBoxBTMyComputer.setText("My Computer");
            jCheckBoxBTMyComputer.setPreferredSize(new Dimension(150, 22));
            jCheckBoxBTMyComputer.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (jCheckBoxBTMyComputer.isSelected()) {
                        addBTDevice("MyComputer");
                    } else {
                        removeBTDevice("MyComputer");
                    }
                }
            });
        }
        return jCheckBoxBTMyComputer;
    }

    /**
	 * This method initializes jCheckBoxBTBossComputer	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getJCheckBoxBTBossComputer() {
        if (jCheckBoxBTBossComputer == null) {
            jCheckBoxBTBossComputer = new JCheckBox();
            jCheckBoxBTBossComputer.setText("Boss Computer");
            jCheckBoxBTBossComputer.setPreferredSize(new Dimension(150, 22));
            jCheckBoxBTBossComputer.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (jCheckBoxBTBossComputer.isSelected()) {
                        addBTDevice("BossComputer");
                    } else {
                        removeBTDevice("BossComputer");
                    }
                }
            });
        }
        return jCheckBoxBTBossComputer;
    }

    /**
	 * This method initializes jCheckBoxBTBossMobile	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getJCheckBoxBTBossMobile() {
        if (jCheckBoxBTBossMobile == null) {
            jCheckBoxBTBossMobile = new JCheckBox();
            jCheckBoxBTBossMobile.setText("Boss Mobile");
            jCheckBoxBTBossMobile.setPreferredSize(new Dimension(150, 22));
            jCheckBoxBTBossMobile.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (jCheckBoxBTBossMobile.isSelected()) {
                        addBTDevice("BossMobile");
                    } else {
                        removeBTDevice("BossMobile");
                    }
                }
            });
        }
        return jCheckBoxBTBossMobile;
    }

    /**
	 * This method initializes jPanelGPSLocation	
	 * 	
	 * @return javax.swing.JPanel	
	 */
    private JPanel getJPanelGPSLocation() {
        if (jPanelGPSLocation == null) {
            GridBagConstraints gridBagConstraints6 = new GridBagConstraints();
            gridBagConstraints6.gridx = 0;
            gridBagConstraints6.gridy = 1;
            GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
            gridBagConstraints5.gridx = 0;
            gridBagConstraints5.gridy = 0;
            jPanelGPSLocation = new JPanel();
            jPanelGPSLocation.setLayout(new GridBagLayout());
            jPanelGPSLocation.setBorder(BorderFactory.createTitledBorder(null, "GPS Location", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
            jPanelGPSLocation.setPreferredSize(new Dimension(190, 72));
            jPanelGPSLocation.add(getJCheckBoxGPSMyOffice(), gridBagConstraints5);
            jPanelGPSLocation.add(getJCheckBoxGPSBossOffice(), gridBagConstraints6);
        }
        return jPanelGPSLocation;
    }

    /**
	 * This method initializes jCheckBoxGPSMyOffice	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getJCheckBoxGPSMyOffice() {
        if (jCheckBoxGPSMyOffice == null) {
            jCheckBoxGPSMyOffice = new JCheckBox();
            jCheckBoxGPSMyOffice.setText("My Office");
            jCheckBoxGPSMyOffice.setPreferredSize(new Dimension(150, 22));
        }
        return jCheckBoxGPSMyOffice;
    }

    /**
	 * This method initializes jCheckBoxGPSBossOffice	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */
    private JCheckBox getJCheckBoxGPSBossOffice() {
        if (jCheckBoxGPSBossOffice == null) {
            jCheckBoxGPSBossOffice = new JCheckBox();
            jCheckBoxGPSBossOffice.setText("Boss Office");
            jCheckBoxGPSBossOffice.setPreferredSize(new Dimension(150, 22));
        }
        return jCheckBoxGPSBossOffice;
    }

    protected void addBTDevice(String s) {
        RemoteDevice rem = new RemoteDevice("00:00:00:01:01");
        rem.setFriendlyName("MyComputer");
        DiscoveryAgent.getInstance().addRemoteDevice(rem, new DeviceClass(0));
    }

    protected void removeBTDevice(String s) {
        DiscoveryAgent agent = DiscoveryAgent.getInstance();
        for (int i = 0; i < agent.remoteDevicesCount(); i++) {
            RemoteDevice dev = agent.remoteDeviceAt(i);
            try {
                if (dev.getFriendlyName(true).equals("MyComputer")) {
                    agent.removeRemoteDevice(dev);
                    break;
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}
