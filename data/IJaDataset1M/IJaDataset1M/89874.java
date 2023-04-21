package com.usoog.hextd.tower;

import com.usoog.commons.gamecore.gamemanager.GameManager;
import com.usoog.commons.gamecore.tile.Tile;
import com.usoog.hextd.Constants;
import com.usoog.hextd.tower.TowerBase.ModeType;
import com.usoog.tdcore.message.MessageTowerSetMode;

/**
 *
 * @author  hylke
 */
public class PanelTowerSettingsDouble extends PanelTowerSettings {

    private String[] modeValues1;

    private String[] modeValues2;

    private int mode1 = 0;

    private int mode2 = 0;

    private HexTDTower t;

    private GameManager gameManager;

    private boolean loading = false;

    private boolean enabled = true;

    /**
	 * Creates new form PanelTowerSettingsDouble
	 * 0 = Lock
	 * 1 = Targeting
	 * 2 = Group
	 */
    public PanelTowerSettingsDouble(HexTDTower t, GameManager gm) {
        super();
        this.gameManager = gm;
        initComponents();
        this.t = t;
    }

    @Override
    public void setModes(TowerBase.ModeType m, String[] modes) {
        switch(m) {
            case Targeting:
                this.loading = true;
                this.modeValues1 = modes;
                this.jComboBox_mode1.removeAllItems();
                for (int i = 0; i < this.modeValues1.length; i++) {
                    this.jComboBox_mode1.addItem(this.modeValues1[i]);
                }
                this.jComboBox_mode1.setSelectedIndex(this.mode1);
                this.jLabel_mode1.setText(this.modeValues1[this.mode1]);
                this.loading = false;
                break;
            case Group:
                this.loading = true;
                this.modeValues2 = modes;
                this.jComboBox_mode2.removeAllItems();
                for (int i = 0; i < this.modeValues2.length; i++) {
                    this.jComboBox_mode2.addItem(this.modeValues2[i]);
                }
                this.jComboBox_mode2.setSelectedIndex(this.mode2);
                this.jLabel_mode2.setText(this.modeValues2[this.mode2]);
                this.loading = false;
                break;
        }
    }

    @Override
    public int getMode(TowerBase.ModeType m) {
        switch(m) {
            case Targeting:
                return mode1;
            case Group:
                return mode2;
        }
        return -1;
    }

    @Override
    public void setMode(TowerBase.ModeType m, int v, boolean suppress) {
        loading = suppress;
        switch(m) {
            case Targeting:
                mode1 = v;
                jComboBox_mode1.setSelectedIndex(mode1);
                jLabel_mode1.setText(modeValues1[mode1]);
                if (!t.getMode(m.name()).equals(modeValues1[mode1])) {
                    System.err.println("PanelTowerSettingsSingle::setMode tower and pts not synced?");
                }
                break;
            case Group:
                mode2 = v;
                jComboBox_mode2.setSelectedIndex(mode2);
                jLabel_mode2.setText(modeValues2[mode2]);
                if (!t.getMode(m.name()).equals(modeValues2[mode2])) {
                    System.err.println("PanelTowerSettingsSingle::setMode tower and pts not synced?");
                }
                break;
        }
        loading = false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public void setEnabled(boolean enable) {
        this.enabled = enable;
        this.jComboBox_mode1.setVisible(enable);
        this.jComboBox_mode2.setVisible(enable);
        this.jLabel_mode1.setVisible(!enable);
        this.jLabel_mode2.setVisible(!enable);
    }

    @Override
    public void setCanLock(boolean b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jLabel1 = new javax.swing.JLabel();
        jComboBox_mode1 = new javax.swing.JComboBox();
        jComboBox_mode2 = new javax.swing.JComboBox();
        jLabel_mode1 = new javax.swing.JLabel();
        jLabel_mode2 = new javax.swing.JLabel();
        setBackground(Constants.colorBackGround);
        setForeground(Constants.colorForeGround);
        setLayout(new java.awt.GridBagLayout());
        jLabel1.setBackground(Constants.colorBackGround);
        jLabel1.setForeground(Constants.colorForeGround);
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Target Modes");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.01;
        add(jLabel1, gridBagConstraints);
        jComboBox_mode1.setBackground(Constants.colorBackGround);
        jComboBox_mode1.setForeground(Constants.colorForeGround);
        jComboBox_mode1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_mode1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_mode1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.01;
        add(jComboBox_mode1, gridBagConstraints);
        jComboBox_mode2.setBackground(Constants.colorBackGround);
        jComboBox_mode2.setForeground(Constants.colorForeGround);
        jComboBox_mode2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jComboBox_mode2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_mode2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.01;
        add(jComboBox_mode2, gridBagConstraints);
        jLabel_mode1.setBackground(Constants.colorBackGround);
        jLabel_mode1.setForeground(Constants.colorForeGround);
        jLabel_mode1.setText("jLabel2");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.weightx = 0.01;
        add(jLabel_mode1, gridBagConstraints);
        jLabel_mode2.setBackground(Constants.colorBackGround);
        jLabel_mode2.setForeground(Constants.colorForeGround);
        jLabel_mode2.setText("jLabel3");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.01;
        add(jLabel_mode2, gridBagConstraints);
    }

    private void jComboBox_mode1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!this.loading) {
            mode1 = jComboBox_mode1.getSelectedIndex();
            Tile tile = t.getTile();
            MessageTowerSetMode messageSetMode = new MessageTowerSetMode(-1, -1, tile.getGridCol(), tile.getGridRow(), ModeType.Targeting.name(), "" + mode1);
            gameManager.addLocalPlayerAction(messageSetMode);
        }
    }

    private void jComboBox_mode2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (!this.loading) {
            mode2 = jComboBox_mode2.getSelectedIndex();
            Tile tile = t.getTile();
            MessageTowerSetMode messageSetMode = new MessageTowerSetMode(-1, -1, tile.getGridCol(), tile.getGridRow(), ModeType.Group.name(), "" + mode2);
            gameManager.addLocalPlayerAction(messageSetMode);
        }
    }

    private javax.swing.JComboBox jComboBox_mode1;

    private javax.swing.JComboBox jComboBox_mode2;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel_mode1;

    private javax.swing.JLabel jLabel_mode2;
}
