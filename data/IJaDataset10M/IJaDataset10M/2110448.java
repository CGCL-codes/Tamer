package org.vmtarchitect.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.vmtarchitect.utils.Settings;
import org.vmtarchitect.vmtmElements.ElementComputer;
import org.vmtarchitect.vmtmElements.ElementNet;
import org.vmtarchitect.vmtmElements.TeamElement;
import org.vmtarchitect.vmtmElements.TeamElements;

public class ElementsPreferencesDialog extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel jContentPane = null;

    private JButton jButtonOpenVM = null;

    private JTextField jTFvmPathFile = null;

    private JTextField jTFvmName = null;

    private JLabel jLabel = null;

    private JLabel jLabel1 = null;

    private JLabel jLabel2 = null;

    private JTextField jTFvmDelay = null;

    private JPanel jPanelVm = null;

    private JComboBox jCBvmStartSuccession = null;

    private JLabel jLabel11 = null;

    private JPanel jPanelLan = null;

    private JLabel jLabel3 = null;

    private JLabel jLabel21 = null;

    private JLabel jLabel12 = null;

    private JTextField jTFlanSpeed = null;

    private JTextField jTFlanPacketLost = null;

    private TeamElements netElements;

    private TeamElement operatedElement;

    private ElementComputer operComp;

    private ElementNet operNet;

    private JButton jButtonOk = null;

    private JButton jButtonAnuluj = null;

    private JTextField jTFlanName = null;

    private JPanel canvas = null;

    /**
	 * @param owner
	 */
    public ElementsPreferencesDialog(Frame owner, TeamElements net, JPanel drawSurface) {
        super(owner);
        canvas = drawSurface;
        netElements = net;
        this.operatedElement = net.getSelectedElement();
        initialize();
    }

    public ElementsPreferencesDialog(TeamElements net) {
        super();
        netElements = net;
        this.operatedElement = net.getSelectedElement();
        initialize();
    }

    /**
	 * This method initializes this
	 * 
	 * @return void
	 */
    private void initialize() {
        this.setSize(437, 204);
        this.setContentPane(getJContentPane());
        if (operatedElement.getElementType() == TeamElements.elementComp) {
            this.setTitle("Ustawienia maszyny wirtualnej");
            this.addWindowListener(new java.awt.event.WindowAdapter() {

                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.out.println("Zapis danych o elemencie sieci w programie");
                }
            });
        } else if (operatedElement.getElementType() == TeamElements.elementNet) {
            this.setTitle("Ustawienia sieci wirtualnej");
        }
    }

    /**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel2 = new JLabel();
            jLabel2.setText("Nazwa");
            jLabel2.setBounds(new Rectangle(15, 27, 41, 15));
            jLabel1 = new JLabel();
            jLabel1.setText("Opóźnienie");
            jLabel1.setBounds(new Rectangle(0, 46, 71, 15));
            jLabel = new JLabel();
            jLabel.setText("Plik vmx");
            jLabel.setBounds(new Rectangle(9, 5, 53, 15));
            jContentPane = new JPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getJButtonOk(), null);
            jContentPane.add(getJButtonAnuluj(), null);
            if (operatedElement.getElementType() == TeamElements.elementComp) {
                operComp = (ElementComputer) operatedElement;
                jContentPane.add(getJPanelVm(), null);
                jTFvmPathFile.setText(operComp.getVmFile());
                jTFvmName.setText(operComp.getElementName());
                jTFvmDelay.setText(Integer.toString(operComp.getDelayStartSek()));
                jCBvmStartSuccession.setSelectedIndex(operComp.getBootSuccesions() - 1);
            } else if (operatedElement.getElementType() == TeamElements.elementNet) {
                operNet = (ElementNet) operatedElement;
                jContentPane.add(getJPanelLan(), null);
                jTFlanName.setText(operNet.getElementName());
                jTFlanPacketLost.setText(Integer.toString(operNet.getLanPacketLoss()));
                jTFlanSpeed.setText(Integer.toString(operNet.getLanSpeed()));
                this.setName("Ustawienia wirtualnej sieci");
            }
        }
        return jContentPane;
    }

    /**
	 * This method initializes jButtonOpenVM
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getJButtonOpenVM() {
        if (jButtonOpenVM == null) {
            jButtonOpenVM = new JButton();
            jButtonOpenVM.setText("Otwórz");
            jButtonOpenVM.setLocation(new Point(292, 2));
            jButtonOpenVM.setSize(new Dimension(80, 20));
            jButtonOpenVM.setPreferredSize(new Dimension(80, 20));
            jButtonOpenVM.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    final JFileChooser fc = new JFileChooser();
                    if (Settings.getPath() != null) {
                        fc.setCurrentDirectory(new File(Settings.getPath()));
                    }
                    int returnVal = fc.showOpenDialog(jButtonOpenVM);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        File file = fc.getSelectedFile();
                        if (file.canRead() && file.canWrite()) {
                            Settings.setPath(file.getParent());
                            jTFvmPathFile.setText(file.getAbsolutePath());
                        }
                    } else {
                        System.out.println("Open command cancelled by user.");
                    }
                }
            });
        }
        return jButtonOpenVM;
    }

    /**
	 * This method initializes jTFvmPathFile
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getJTFvmPathFile() {
        if (jTFvmPathFile == null) {
            jTFvmPathFile = new JTextField();
            jTFvmPathFile.setEditable(false);
            jTFvmPathFile.setBounds(new Rectangle(71, 5, 221, 15));
            jTFvmPathFile.setPreferredSize(new Dimension(221, 19));
        }
        return jTFvmPathFile;
    }

    /**
	 * This method initializes jTFvmName
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getJTFvmName() {
        if (jTFvmName == null) {
            jTFvmName = new JTextField();
            jTFvmName.setPreferredSize(new Dimension(221, 19));
            jTFvmName.setBounds(new Rectangle(71, 25, 221, 15));
        }
        return jTFvmName;
    }

    /**
	 * This method initializes jTFvmDelay
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getJTFvmDelay() {
        if (jTFvmDelay == null) {
            jTFvmDelay = new JTextField();
            jTFvmDelay.setPreferredSize(new Dimension(221, 19));
            jTFvmDelay.setBounds(new Rectangle(71, 45, 221, 15));
        }
        return jTFvmDelay;
    }

    /**
	 * This method initializes jPanelVm
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanelVm() {
        if (jPanelVm == null) {
            jLabel11 = new JLabel();
            jLabel11.setText("Kolejność");
            jLabel11.setBounds(new Rectangle(5, 67, 61, 15));
            jPanelVm = new JPanel();
            jPanelVm.setLayout(null);
            jPanelVm.setVisible(true);
            jPanelVm.setBounds(new Rectangle(28, 29, 372, 87));
            jPanelVm.add(jLabel, null);
            jPanelVm.add(jLabel2, null);
            jPanelVm.add(jLabel1, null);
            jPanelVm.add(getJTFvmPathFile(), null);
            jPanelVm.add(getJTFvmName(), null);
            jPanelVm.add(getJButtonOpenVM(), null);
            jPanelVm.add(getJCBvmStartSuccession(), null);
            jPanelVm.add(jLabel11, null);
            jPanelVm.add(getJTFvmDelay(), null);
        }
        return jPanelVm;
    }

    /**
	 * This method initializes jCBvmStartSuccession
	 * 
	 * @return javax.swing.JComboBox
	 */
    private JComboBox getJCBvmStartSuccession() {
        if (jCBvmStartSuccession == null) {
            jCBvmStartSuccession = new JComboBox();
            jCBvmStartSuccession.setBounds(new Rectangle(150, 67, 47, 15));
            setjCBvmStartSuccession();
        }
        return jCBvmStartSuccession;
    }

    /**
	 * This method initializes jPanelLan
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getJPanelLan() {
        if (jPanelLan == null) {
            jLabel12 = new JLabel();
            jLabel12.setText("Procent straconych pakietów ");
            jLabel12.setBounds(new Rectangle(0, 40, 183, 15));
            jLabel21 = new JLabel();
            jLabel21.setText("Prędkość");
            jLabel21.setBounds(new Rectangle(63, 21, 56, 15));
            jLabel3 = new JLabel();
            jLabel3.setText("Nazwa");
            jLabel3.setBounds(new Rectangle(71, 2, 41, 15));
            jPanelLan = new JPanel();
            jPanelLan.setLayout(null);
            jPanelLan.setBounds(new Rectangle(9, 43, 404, 57));
            jPanelLan.setVisible(true);
            jPanelLan.add(jLabel3, null);
            jPanelLan.add(jLabel21, null);
            jPanelLan.add(jLabel12, null);
            jPanelLan.add(getJTFlanSpeed(), null);
            jPanelLan.add(getJTFlanPacketLost(), null);
            jPanelLan.add(getJTFlanName(), null);
        }
        return jPanelLan;
    }

    /**
	 * This method initializes jTFlanSpeed
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getJTFlanSpeed() {
        if (jTFlanSpeed == null) {
            jTFlanSpeed = new JTextField();
            jTFlanSpeed.setPreferredSize(new Dimension(221, 19));
            jTFlanSpeed.setBounds(new Rectangle(183, 21, 221, 15));
        }
        return jTFlanSpeed;
    }

    /**
	 * This method initializes jTFlanPacketLost
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getJTFlanPacketLost() {
        if (jTFlanPacketLost == null) {
            jTFlanPacketLost = new JTextField();
            jTFlanPacketLost.setPreferredSize(new Dimension(221, 19));
            jTFlanPacketLost.setBounds(new Rectangle(183, 40, 221, 15));
        }
        return jTFlanPacketLost;
    }

    /**
	 * This method initializes jButtonOk
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getJButtonOk() {
        if (jButtonOk == null) {
            jButtonOk = new JButton();
            jButtonOk.setText("OK");
            jButtonOk.setSize(new Dimension(75, 20));
            jButtonOk.setLocation(new Point(265, 133));
            jButtonOk.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    if (operatedElement.getElementType() == TeamElements.elementComp) {
                        operComp.setElementName(jTFvmName.getText());
                        operComp.setVmFile(jTFvmPathFile.getText());
                        operComp.setDelayStartSek(Integer.parseInt(jTFvmDelay.getText()));
                        netElements.changeCompSuccesion(jCBvmStartSuccession.getSelectedIndex() + 1, operComp.getBootSuccesions());
                        operComp.setBootSuccesions(jCBvmStartSuccession.getSelectedIndex() + 1);
                    } else {
                        if (operatedElement.getElementType() == TeamElements.elementNet) {
                            operNet.setElementName(jTFlanName.getText());
                            operNet.setLanSpeed(Integer.parseInt(jTFlanSpeed.getText()));
                            operNet.setLanPacketLoss(Integer.parseInt(jTFlanPacketLost.getText()));
                        }
                    }
                    close();
                }
            });
        }
        return jButtonOk;
    }

    /**
	 * This method initializes jButtonAnuluj
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getJButtonAnuluj() {
        if (jButtonAnuluj == null) {
            jButtonAnuluj = new JButton();
            jButtonAnuluj.setText("Anuluj");
            jButtonAnuluj.setSize(new Dimension(75, 20));
            jButtonAnuluj.setLocation(new Point(95, 133));
        }
        jButtonAnuluj.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent e) {
                close();
            }
        });
        return jButtonAnuluj;
    }

    /**
	 * This method initializes jTFlanName
	 * 
	 * @return javax.swing.JTextField
	 */
    private JTextField getJTFlanName() {
        if (jTFlanName == null) {
            jTFlanName = new JTextField();
            jTFlanName.setPreferredSize(new Dimension(221, 19));
            jTFlanName.setBounds(new Rectangle(183, 2, 221, 15));
        }
        return jTFlanName;
    }

    private void setjCBvmStartSuccession() {
        for (int i = 0; i < netElements.howManyElementsExactType(TeamElements.elementComp); i++) jCBvmStartSuccession.addItem(Integer.toString(i + 1));
    }

    private void close() {
        this.getOwner().repaint();
        canvas.repaint();
        this.dispose();
    }
}
