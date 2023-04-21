package newxml.netconnect;

import com.jme.bounding.BoundingBox;
import com.jme.bounding.BoundingCapsule;
import com.jme.bounding.BoundingSphere;
import com.jme.bounding.OrientedBoundingBox;
import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

public class NetConnectControls extends javax.swing.JFrame {

    private Class[] bounds = new Class[] { null, BoundingBox.class, BoundingSphere.class, OrientedBoundingBox.class, BoundingCapsule.class };

    private Server server;

    public NetConnectControls(Server server) {
        this.server = server;
        initComponents();
        jcbSelectBounds.setModel(new DefaultComboBoxModel(bounds));
        jcbSelectBounds.setRenderer(new DefaultListCellRenderer() {

            @Override
            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                JLabel ret = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    ret.setText(" <none> ");
                } else {
                    ret.setText(((Class) value).getSimpleName());
                }
                return ret;
            }
        });
        jcbSelectBounds.setSelectedItem(server.getBounds());
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jcbSelectBounds = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jtfTexPath = new javax.swing.JTextField();
        butBrowseTexture = new javax.swing.JButton();
        cbClearScene = new javax.swing.JCheckBox();
        jButton1 = new javax.swing.JButton();
        butExport = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jProgressBar1 = new javax.swing.JProgressBar();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Controls");
        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jPanel1.setLayout(new java.awt.GridBagLayout());
        jLabel1.setText("Generate Bounds");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        jPanel1.add(jLabel1, gridBagConstraints);
        jcbSelectBounds.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jcbSelectBoundsActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jcbSelectBounds, gridBagConstraints);
        jLabel2.setText("Texture Path");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        jPanel1.add(jLabel2, gridBagConstraints);
        jtfTexPath.setEditable(false);
        jtfTexPath.setText(server.getTexturePath().toExternalForm());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jtfTexPath, gridBagConstraints);
        butBrowseTexture.setText("...");
        butBrowseTexture.setMargin(new java.awt.Insets(0, 0, 0, 0));
        butBrowseTexture.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butBrowseTextureActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        jPanel1.add(butBrowseTexture, gridBagConstraints);
        cbClearScene.setSelected(server.isClearSceneOnInput());
        cbClearScene.setText("Clear Scene On Input");
        cbClearScene.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        cbClearScene.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbClearSceneActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(cbClearScene, gridBagConstraints);
        jButton1.setText("Load...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        jPanel1.add(jButton1, gridBagConstraints);
        butExport.setText("Save...");
        butExport.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                butExportActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        jPanel1.add(butExport, gridBagConstraints);
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 217, Short.MAX_VALUE));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 319, Short.MAX_VALUE));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 100;
        gridBagConstraints.gridwidth = 100;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel1.add(jPanel2, gridBagConstraints);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        jProgressBar1.setString("");
        jProgressBar1.setStringPainted(true);
        getContentPane().add(jProgressBar1, java.awt.BorderLayout.SOUTH);
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 229) / 2, (screenSize.height - 459) / 2, 229, 459);
    }

    private void butBrowseTextureActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jfc = new JFileChooser();
        if (server.getTexturePath() != null) {
            try {
                jfc.setCurrentDirectory(new File(server.getTexturePath().toURI()));
            } catch (URISyntaxException ex) {
                Logger.getLogger(NetConnectControls.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                jtfTexPath.setText(jfc.getSelectedFile().toURI().toURL().toExternalForm());
                server.setTexturePath(jfc.getSelectedFile().toURI().toURL());
            } catch (MalformedURLException ex) {
                Logger.getLogger(NetConnectControls.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void jcbSelectBoundsActionPerformed(java.awt.event.ActionEvent evt) {
        server.setBounds((Class) jcbSelectBounds.getSelectedItem());
    }

    private void cbClearSceneActionPerformed(java.awt.event.ActionEvent evt) {
        server.setClearSceneOnInput(cbClearScene.isSelected());
    }

    private void butExportActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jfc = new JFileChooser();
        if (server.getExportPath() != null) {
            try {
                jfc.setSelectedFile(new File(server.getExportPath().toURI()));
            } catch (Exception ex) {
                Logger.getLogger(NetConnectControls.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jfc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jme");
            }

            @Override
            public String getDescription() {
                return ".jme";
            }
        });
        if (jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                if (!jfc.getSelectedFile().getName().toLowerCase().endsWith(".jme")) {
                    jfc.setSelectedFile(new File(jfc.getSelectedFile().getParentFile(), jfc.getSelectedFile().getName() + ".jme"));
                }
                server.sceneToBinary(jfc.getSelectedFile());
            } catch (IOException ex) {
                Logger.getLogger(NetConnectControls.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Could not save scene!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser jfc = new JFileChooser();
        if (server.getExportPath() != null) {
            try {
                jfc.setSelectedFile(new File(server.getExportPath().toURI()));
            } catch (Exception ex) {
                Logger.getLogger(NetConnectControls.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jfc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".jme");
            }

            @Override
            public String getDescription() {
                return ".jme";
            }
        });
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                server.loadScene(jfc.getSelectedFile());
            } catch (Exception ex) {
                Logger.getLogger(NetConnectControls.class.getName()).log(Level.SEVERE, null, ex);
                JOptionPane.showMessageDialog(this, "Could not load scene!", "Error", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private javax.swing.JButton butBrowseTexture;

    private javax.swing.JButton butExport;

    private javax.swing.JCheckBox cbClearScene;

    private javax.swing.JButton jButton1;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JProgressBar jProgressBar1;

    private javax.swing.JComboBox jcbSelectBounds;

    private javax.swing.JTextField jtfTexPath;
}
