package net.ramponi.perfmeter.rstat;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import net.ramponi.perfmeter.*;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JTextField;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.BoxLayout;

public class PerfmeterConfig extends JPanel implements PluginConfigurator {

    static final long serialVersionUID = 6141892085620792882L;

    private int sampleTime = 2;

    private boolean mustClose = false;

    DefaultListModel hosts = new DefaultListModel();

    RstatPlugin plugin = null;

    boolean checkBoxesStates[] = null;

    private javax.swing.JPanel jPanel = null;

    private javax.swing.JPanel jPanel2 = null;

    private javax.swing.JCheckBox jCheckBoxCPU = null;

    private javax.swing.JCheckBox jCheckBoxLoad = null;

    private javax.swing.JCheckBox jCheckBoxDisk = null;

    private javax.swing.JCheckBox jCheckBoxPage = null;

    private javax.swing.JCheckBox jCheckBoxContext = null;

    private javax.swing.JCheckBox jCheckBoxSwap = null;

    private javax.swing.JCheckBox jCheckBoxInterrupts = null;

    private javax.swing.JCheckBox jCheckBoxPackets = null;

    private javax.swing.JCheckBox jCheckBoxCollisions = null;

    private javax.swing.JCheckBox jCheckBoxErrors = null;

    private javax.swing.JCheckBox jCheckBoxes[] = null;

    private javax.swing.JPanel jPanel4 = null;

    private javax.swing.JPanel jPanel5 = null;

    private javax.swing.JPanel jPanel6 = null;

    private javax.swing.JLabel jLabel = null;

    private javax.swing.JComboBox jComboBoxHost = null;

    private javax.swing.JButton jButtonAdd = null;

    private javax.swing.JPanel jPanel7 = null;

    private javax.swing.JList jListHost = null;

    private javax.swing.JPanel jPanel8 = null;

    private javax.swing.JButton jButtonUp = null;

    private javax.swing.JButton jButtonRemove = null;

    private javax.swing.JButton jButtonDown = null;

    private javax.swing.JScrollPane jScrollPane = null;

    private JPanel jPanel9 = null;

    private JLabel jLabel1 = null;

    private JTextField jTextField = null;

    /**
     * This is the default constructor for readExternal
     */
    public PerfmeterConfig() {
        super();
        initialize();
    }

    public PerfmeterConfig(RstatPlugin plugin) {
        super();
        this.plugin = plugin;
        initialize();
        map();
    }

    public void initFromArgs(String args[]) {
        hosts.clear();
        for (int i = 0; i < args.length; i++) hosts.addElement(args[i]);
    }

    public void setPlugin(RstatPlugin plugin) {
        this.plugin = plugin;
        plugin.configurationChange();
    }

    public void map() {
        hosts.clear();
        Iterator it = plugin.getHosts().iterator();
        while (it.hasNext()) hosts.addElement(it.next());
        checkBoxesStates = new boolean[jCheckBoxes.length];
        for (int i = 0; i < jCheckBoxes.length; i++) checkBoxesStates[i] = jCheckBoxes[i].isSelected();
        getJTextFieldSampleTime().setText(Integer.toString(sampleTime));
    }

    public String[] getHosts() {
        String ret[] = new String[hosts.size()];
        for (int i = 0; i < ret.length; i++) ret[i] = (String) hosts.get(i);
        return ret;
    }

    public void refresh() {
        map();
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        int hostsSize = in.readInt();
        hosts.clear();
        for (int i = 0; i < hostsSize; i++) hosts.addElement(in.readObject());
        checkBoxesStates = (boolean[]) in.readObject();
        for (int i = 0; i < checkBoxesStates.length; i++) jCheckBoxes[i].setSelected(checkBoxesStates[i]);
        sampleTime = in.readInt();
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(hosts.getSize());
        for (int i = 0; i < hosts.getSize(); i++) out.writeObject(hosts.get(i));
        out.writeObject(checkBoxesStates);
        out.writeInt(sampleTime);
    }

    /**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
        this.setLayout(new java.awt.BorderLayout());
        this.add(getJPanel(), java.awt.BorderLayout.NORTH);
        this.add(getJPanel4(), java.awt.BorderLayout.CENTER);
        this.setSize(300, 319);
    }

    /**
     * This method initializes jPanel
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel() {
        if (jPanel == null) {
            jPanel = new javax.swing.JPanel();
        }
        return jPanel;
    }

    /**
     * This method initializes jPanel2
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel2() {
        if (jPanel2 == null) {
            jPanel2 = new javax.swing.JPanel();
            java.awt.GridLayout layGridLayout2 = new java.awt.GridLayout();
            layGridLayout2.setRows(3);
            layGridLayout2.setColumns(4);
            jPanel2.setLayout(layGridLayout2);
            jPanel2.add(getJCheckBoxCPU(), null);
            jPanel2.add(getJCheckBoxLoad(), null);
            jPanel2.add(getJCheckBoxDisk(), null);
            jPanel2.add(getJCheckBoxPage(), null);
            jPanel2.add(getJCheckBoxContext(), null);
            jPanel2.add(getJCheckBoxSwap(), null);
            jPanel2.add(getJCheckBoxInterrupts(), null);
            jPanel2.add(getJCheckBoxPackets(), null);
            jPanel2.add(getJCheckBoxCollisions(), null);
            jPanel2.add(getJCheckBoxErrors(), null);
            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Graph", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
            jPanel2.setPreferredSize(new java.awt.Dimension(334, 100));
            jPanel2.setMaximumSize(new java.awt.Dimension(32767, 100));
            jPanel2.setMinimumSize(new java.awt.Dimension(334, 100));
            jCheckBoxes = new JCheckBox[] { getJCheckBoxCPU(), getJCheckBoxLoad(), getJCheckBoxDisk(), getJCheckBoxPage(), getJCheckBoxContext(), getJCheckBoxSwap(), getJCheckBoxInterrupts(), getJCheckBoxPackets(), getJCheckBoxCollisions(), getJCheckBoxErrors() };
        }
        return jPanel2;
    }

    /**
     * This method initializes jCheckBoxCPU
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxCPU() {
        if (jCheckBoxCPU == null) {
            jCheckBoxCPU = new javax.swing.JCheckBox();
            jCheckBoxCPU.setText("CPU");
            jCheckBoxCPU.setSelected(true);
        }
        return jCheckBoxCPU;
    }

    /**
     * This method initializes jCheckBoxLoad
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxLoad() {
        if (jCheckBoxLoad == null) {
            jCheckBoxLoad = new javax.swing.JCheckBox();
            jCheckBoxLoad.setText("Load");
            jCheckBoxLoad.setSelected(true);
        }
        return jCheckBoxLoad;
    }

    /**
     * This method initializes jCheckBoxDisk
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxDisk() {
        if (jCheckBoxDisk == null) {
            jCheckBoxDisk = new javax.swing.JCheckBox();
            jCheckBoxDisk.setText("Disk");
            jCheckBoxDisk.setSelected(true);
        }
        return jCheckBoxDisk;
    }

    /**
     * This method initializes jCheckBoxPage
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxPage() {
        if (jCheckBoxPage == null) {
            jCheckBoxPage = new javax.swing.JCheckBox();
            jCheckBoxPage.setText("Page");
            jCheckBoxPage.setSelected(true);
        }
        return jCheckBoxPage;
    }

    /**
     * This method initializes jCheckBoxContext
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxContext() {
        if (jCheckBoxContext == null) {
            jCheckBoxContext = new javax.swing.JCheckBox();
            jCheckBoxContext.setText("Context");
            jCheckBoxContext.setSelected(true);
        }
        return jCheckBoxContext;
    }

    /**
     * This method initializes jCheckBoxSwap
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxSwap() {
        if (jCheckBoxSwap == null) {
            jCheckBoxSwap = new javax.swing.JCheckBox();
            jCheckBoxSwap.setText("Swap");
        }
        return jCheckBoxSwap;
    }

    /**
     * This method initializes jCheckBoxInterrupts
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxInterrupts() {
        if (jCheckBoxInterrupts == null) {
            jCheckBoxInterrupts = new javax.swing.JCheckBox();
            jCheckBoxInterrupts.setText("Interrupts");
            jCheckBoxInterrupts.setSelected(true);
        }
        return jCheckBoxInterrupts;
    }

    /**
     * This method initializes jCheckBoxPackets
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxPackets() {
        if (jCheckBoxPackets == null) {
            jCheckBoxPackets = new javax.swing.JCheckBox();
            jCheckBoxPackets.setText("Packets");
            jCheckBoxPackets.setSelected(true);
        }
        return jCheckBoxPackets;
    }

    /**
     * This method initializes jCheckBoxCollisions
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxCollisions() {
        if (jCheckBoxCollisions == null) {
            jCheckBoxCollisions = new javax.swing.JCheckBox();
            jCheckBoxCollisions.setText("Collisions");
        }
        return jCheckBoxCollisions;
    }

    /**
     * This method initializes jCheckBoxErrors
     * 
     * @return javax.swing.JCheckBox
     */
    private javax.swing.JCheckBox getJCheckBoxErrors() {
        if (jCheckBoxErrors == null) {
            jCheckBoxErrors = new javax.swing.JCheckBox();
            jCheckBoxErrors.setText("Errors");
        }
        return jCheckBoxErrors;
    }

    /**
     * This method initializes jButtonOk
     * 
     * @return javax.swing.JButton
     */
    public void actionOk() {
        applyChanges();
    }

    private void applyChanges() {
        try {
            sampleTime = Integer.parseInt(getJTextFieldSampleTime().getText());
            sampleTime = Math.max(sampleTime, 1);
        } catch (Exception ex) {
        }
        plugin.configurationChange();
        map();
    }

    public void actionApply() {
        applyChanges();
    }

    /**
     * This method initializes jButtonCancel
     * 
     * @return javax.swing.JButton
     */
    public void actionCancel() {
        mustClose = true;
        PerfmeterConfig.this.firePropertyChange("configuration", null, null);
        for (int i = 0; i < jCheckBoxes.length; i++) jCheckBoxes[i].setSelected(checkBoxesStates[i]);
        mustClose = false;
    }

    /**
     * This method initializes jPanel4
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel4() {
        if (jPanel4 == null) {
            jPanel4 = new javax.swing.JPanel();
            jPanel4.setLayout(new BoxLayout(getJPanel4(), BoxLayout.Y_AXIS));
            jPanel4.add(getJPanel9(), null);
            jPanel4.add(getJPanel2(), null);
            jPanel4.add(getJPanel5(), null);
        }
        return jPanel4;
    }

    /**
     * This method initializes jPanel5
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel5() {
        if (jPanel5 == null) {
            jPanel5 = new javax.swing.JPanel();
            jPanel5.setLayout(new java.awt.BorderLayout());
            jPanel5.add(getJPanel6(), java.awt.BorderLayout.NORTH);
            jPanel5.add(getJPanel7(), java.awt.BorderLayout.CENTER);
            jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hosts", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, null, null));
        }
        return jPanel5;
    }

    /**
     * This method initializes jPanel6
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel6() {
        if (jPanel6 == null) {
            jPanel6 = new javax.swing.JPanel();
            jPanel6.setLayout(new java.awt.BorderLayout());
            jPanel6.add(getJLabel(), java.awt.BorderLayout.WEST);
            jPanel6.add(getJComboBoxHost(), java.awt.BorderLayout.CENTER);
            jPanel6.add(getJButtonAdd(), java.awt.BorderLayout.EAST);
            jPanel6.setMaximumSize(new java.awt.Dimension(2147483647, 26));
        }
        return jPanel6;
    }

    /**
     * This method initializes jLabel
     * 
     * @return javax.swing.JLabel
     */
    private javax.swing.JLabel getJLabel() {
        if (jLabel == null) {
            jLabel = new javax.swing.JLabel();
            jLabel.setText("Host");
        }
        return jLabel;
    }

    /**
     * This method initializes jComboBoxHost
     * 
     * @return javax.swing.JComboBox
     */
    private javax.swing.JComboBox getJComboBoxHost() {
        if (jComboBoxHost == null) {
            jComboBoxHost = new javax.swing.JComboBox();
            jComboBoxHost.setEditable(true);
            jComboBoxHost.addItemListener(new java.awt.event.ItemListener() {

                public void itemStateChanged(java.awt.event.ItemEvent e) {
                    addHost();
                }
            });
        }
        return jComboBoxHost;
    }

    private void addHost() {
        if (!"".equals(getJComboBoxHost().getSelectedItem())) {
            String host = (String) getJComboBoxHost().getSelectedItem();
            if (!hosts.contains(host)) {
                hosts.addElement(host);
                getJListHost().ensureIndexIsVisible(hosts.getSize() - 1);
                getJComboBoxHost().getEditor().selectAll();
            }
            boolean found = false;
            for (int i = 0; i < getJComboBoxHost().getItemCount(); i++) {
                if (host.equals(getJComboBoxHost().getItemAt(i))) found = true;
            }
            if (!found) getJComboBoxHost().addItem(host);
        }
    }

    /**
     * This method initializes jButtonAdd
     * 
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonAdd() {
        if (jButtonAdd == null) {
            jButtonAdd = new javax.swing.JButton();
            jButtonAdd.setText("Add");
            jButtonAdd.setPreferredSize(new java.awt.Dimension(80, 26));
            jButtonAdd.setMinimumSize(new java.awt.Dimension(80, 26));
            jButtonAdd.setMaximumSize(new java.awt.Dimension(80, 26));
            jButtonAdd.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    addHost();
                }
            });
        }
        return jButtonAdd;
    }

    /**
     * This method initializes jPanel7
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel7() {
        if (jPanel7 == null) {
            jPanel7 = new javax.swing.JPanel();
            jPanel7.setLayout(new java.awt.BorderLayout());
            jPanel7.add(getJPanel8(), java.awt.BorderLayout.EAST);
            jPanel7.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        }
        return jPanel7;
    }

    /**
     * This method initializes jListHost
     * 
     * @return javax.swing.JList
     */
    private javax.swing.JList getJListHost() {
        if (jListHost == null) {
            jListHost = new javax.swing.JList();
            jListHost.setVisibleRowCount(5);
            jListHost.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
            jListHost.setModel(hosts);
            jListHost.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            jListHost.addKeyListener(new java.awt.event.KeyAdapter() {

                public void keyPressed(java.awt.event.KeyEvent e) {
                    if (e.getKeyCode() == 127) remove();
                }
            });
        }
        return jListHost;
    }

    /**
     * This method initializes jPanel8
     * 
     * @return javax.swing.JPanel
     */
    private javax.swing.JPanel getJPanel8() {
        if (jPanel8 == null) {
            jPanel8 = new javax.swing.JPanel();
            jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.Y_AXIS));
            jPanel8.add(getJButtonRemove(), null);
            jPanel8.add(getJButtonUp(), null);
            jPanel8.add(getJButtonDown(), null);
            jPanel8.setComponentOrientation(java.awt.ComponentOrientation.LEFT_TO_RIGHT);
        }
        return jPanel8;
    }

    /**
     * This method initializes jButtonUp
     * 
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonUp() {
        if (jButtonUp == null) {
            jButtonUp = new javax.swing.JButton();
            jButtonUp.setText("Up");
            jButtonUp.setPreferredSize(new java.awt.Dimension(80, 26));
            jButtonUp.setMaximumSize(new java.awt.Dimension(80, 26));
            jButtonUp.setMinimumSize(new java.awt.Dimension(80, 26));
            jButtonUp.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int idx = getJListHost().getSelectedIndex();
                    if (idx > 0) {
                        String tmp = (String) hosts.getElementAt(idx);
                        hosts.setElementAt(hosts.getElementAt(idx - 1), idx);
                        hosts.setElementAt(tmp, idx - 1);
                        getJListHost().setSelectedIndex(idx - 1);
                    }
                }
            });
        }
        return jButtonUp;
    }

    /**
     * This method initializes jButtonRemove
     * 
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonRemove() {
        if (jButtonRemove == null) {
            jButtonRemove = new javax.swing.JButton();
            jButtonRemove.setText("Remove");
            jButtonRemove.setMaximumSize(new java.awt.Dimension(80, 26));
            jButtonRemove.setPreferredSize(new java.awt.Dimension(80, 26));
            jButtonRemove.setMinimumSize(new java.awt.Dimension(80, 26));
            jButtonRemove.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    remove();
                }
            });
        }
        return jButtonRemove;
    }

    /**
     * This method initializes jButtonDown
     * 
     * @return javax.swing.JButton
     */
    private javax.swing.JButton getJButtonDown() {
        if (jButtonDown == null) {
            jButtonDown = new javax.swing.JButton();
            jButtonDown.setText("Down");
            jButtonDown.setMaximumSize(new java.awt.Dimension(80, 26));
            jButtonDown.setPreferredSize(new java.awt.Dimension(80, 26));
            jButtonDown.setMinimumSize(new java.awt.Dimension(80, 26));
            jButtonDown.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent e) {
                    int idx = getJListHost().getSelectedIndex();
                    if (idx < hosts.getSize() - 1) {
                        String tmp = (String) hosts.getElementAt(idx);
                        hosts.setElementAt(hosts.getElementAt(idx + 1), idx);
                        hosts.setElementAt(tmp, idx + 1);
                        getJListHost().setSelectedIndex(idx + 1);
                    }
                }
            });
        }
        return jButtonDown;
    }

    /**
     * This method initializes jScrollPane
     * 
     * @return javax.swing.JScrollPane
     */
    private javax.swing.JScrollPane getJScrollPane() {
        if (jScrollPane == null) {
            jScrollPane = new javax.swing.JScrollPane();
            jScrollPane.setViewportView(getJListHost());
        }
        return jScrollPane;
    }

    public JPanel getPanel() {
        return this;
    }

    public int columns() {
        return hosts.size();
    }

    public GraphModel getModel(int row, int column) {
        int rows = rows();
        if (rows == 0) throw new ArrayIndexOutOfBoundsException(rows + "==" + 0);
        int i = 0;
        for (int computedRow = 0; i < jCheckBoxes.length; i++) if (jCheckBoxes[i].isSelected()) {
            if (computedRow == row) break;
            computedRow++;
        }
        return plugin.getModel(column, i);
    }

    public int rows() {
        int res = 0;
        for (int i = 0; i < jCheckBoxes.length; i++) res += jCheckBoxes[i].isSelected() ? 1 : 0;
        return res;
    }

    public boolean mustClose() {
        return mustClose;
    }

    public int getSampleTime() {
        return sampleTime;
    }

    public String getName() {
        return "RStat";
    }

    public String getDescription() {
        return "RStat Plugin";
    }

    private void remove() {
        int idx = getJListHost().getSelectedIndex();
        if (idx >= 0) hosts.remove(idx);
        getJListHost().setSelectedIndex(idx >= hosts.getSize() - 1 ? hosts.getSize() - 1 : idx);
    }

    /**
     * This method initializes jPanel9	
     * 	
     * @return javax.swing.JPanel	
     */
    private JPanel getJPanel9() {
        if (jPanel9 == null) {
            jLabel1 = new JLabel();
            jLabel1.setText("Sample Time (second)");
            GridLayout gridLayout = new GridLayout();
            gridLayout.setRows(1);
            gridLayout.setHgap(0);
            gridLayout.setVgap(0);
            gridLayout.setColumns(2);
            jPanel9 = new JPanel();
            jPanel9.setMaximumSize(new Dimension(32767, 50));
            jPanel9.setPreferredSize(new Dimension(264, 50));
            jPanel9.setBorder(BorderFactory.createTitledBorder(null, "Parameters", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
            jPanel9.setLayout(gridLayout);
            jPanel9.add(jLabel1, null);
            jPanel9.add(getJTextFieldSampleTime(), null);
        }
        return jPanel9;
    }

    /**
     * This method initializes jTextField	
     * 	
     * @return javax.swing.JTextField	
     */
    private JTextField getJTextFieldSampleTime() {
        if (jTextField == null) {
            jTextField = new JTextField();
            jTextField.setMinimumSize(new java.awt.Dimension(4, 16));
            jTextField.setPreferredSize(new java.awt.Dimension(4, 16));
        }
        return jTextField;
    }
}
