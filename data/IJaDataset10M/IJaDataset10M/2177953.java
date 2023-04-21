package gralej.blocks.configurator;

import gralej.Config;
import gralej.blocks.BlockLayout;
import gralej.blocks.LabelStyle;
import java.awt.BorderLayout;
import javax.swing.UIManager;

/**
 * @author  Martin
 */
public class BlockConfiguratorDialog extends javax.swing.JDialog implements BlockConfigurator.Handler {

    BlockLayoutEditor _layed;

    LabelStyleEditor _labed;

    MiscSettingsEditor _misced;

    BlockConfigurator _configurator;

    Config _cfg;

    private boolean _okayed;

    public BlockConfiguratorDialog(java.awt.Window parent, boolean modal) {
        this(parent, modal, Config.currentConfig());
    }

    public BlockConfiguratorDialog(java.awt.Window parent, boolean modal, Config cfg) {
        super(parent);
        setModal(modal);
        _cfg = cfg;
        initComponents();
        _configurator = new BlockConfigurator(this, cfg);
        _treePanel.setLayout(new BorderLayout());
        _treePanel.add(_configurator.getUI());
        pack();
    }

    public boolean isOkayed() {
        return _okayed;
    }

    private MiscSettingsEditor misced() {
        if (_misced == null) _misced = new MiscSettingsEditor(this, false, _configurator.getStyle());
        return _misced;
    }

    private BlockLayoutEditor layed() {
        if (_layed == null) _layed = new BlockLayoutEditor(this, false, _configurator.getStyle());
        return _layed;
    }

    private LabelStyleEditor labed() {
        if (_labed == null) _labed = new LabelStyleEditor(this, false, _configurator.getStyle());
        return _labed;
    }

    public void modifyLabelStyle(LabelStyle style) {
        labed().reset(style);
        if (!labed().isVisible()) labed().setVisible(true);
    }

    public void modifyBlockLayout(BlockLayout layout) {
        layed().reset(layout);
        if (!layed().isVisible()) layed().setVisible(true);
    }

    public void modifyMiscSettings() {
        if (!misced().isVisible()) misced().setVisible(true);
    }

    public void updateMessage(String message) {
        _lblMessage.setText(message);
    }

    public void clearMessage() {
        _lblMessage.setText("");
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        _bCancel = new javax.swing.JButton();
        _bOk = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        _lblMessage = new javax.swing.JLabel();
        _treePanel = new javax.swing.JPanel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("AVM Tree View Configurator");
        _bCancel.setText("Cancel");
        _bCancel.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _bCancelActionPerformed(evt);
            }
        });
        _bOk.setText("OK");
        _bOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                _bOkActionPerformed(evt);
            }
        });
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        _lblMessage.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(_lblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(_lblMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE).addContainerGap()));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(_bCancel, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(_bOk, javax.swing.GroupLayout.Alignment.TRAILING))));
        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] { _bCancel, _bOk });
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(_bOk).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(_bCancel)).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))));
        javax.swing.GroupLayout _treePanelLayout = new javax.swing.GroupLayout(_treePanel);
        _treePanel.setLayout(_treePanelLayout);
        _treePanelLayout.setHorizontalGroup(_treePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 355, Short.MAX_VALUE));
        _treePanelLayout.setVerticalGroup(_treePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 222, Short.MAX_VALUE));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(_treePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap().addComponent(_treePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()));
        pack();
    }

    private void _bCancelActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    private void _bOkActionPerformed(java.awt.event.ActionEvent evt) {
        _okayed = true;
        _configurator.getStyle().updateConfig();
        dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.err.println("-- failed to set the system's native look and feel");
                }
                BlockConfiguratorDialog dialog = new BlockConfiguratorDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.JButton _bCancel;

    private javax.swing.JButton _bOk;

    private javax.swing.JLabel _lblMessage;

    private javax.swing.JPanel _treePanel;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;
}
