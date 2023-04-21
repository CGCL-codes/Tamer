package ingrGast.gui.dialogs;

import ingrGast.gui.*;
import ingrGast.management.Manager;

/**
 *
 * @author Blizarazu
 */
public class EditarGrupoDialog extends javax.swing.JDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = -1145485567158250236L;

    /**
     * Creates new form EditarGrupoDialog
     * @param parent
     * @param modal
     */
    public EditarGrupoDialog(MainFrame parent, boolean modal) {
        super(parent, modal);
        this.owner = parent;
        this.manager = this.owner.getManager();
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Editar Grupo");
        setResizable(false);
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel2.setText("Selecciona un Grupo:");
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(this.manager.getGruposNombres()));
        jComboBox1.setSelectedItem(null);
        jComboBox1.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComboBox1ItemStateChanged(evt);
            }
        });
        jLabel3.setText("Nuevo Nombre del Grupo:");
        jTextField1.setEnabled(false);
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextField1KeyReleased(evt);
            }
        });
        jButton1.setText("Guardar Cambio");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton2.setText("Cancelar");
        jButton2.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jComboBox1, 0, 189, Short.MAX_VALUE).addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 189, Short.MAX_VALUE))).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jButton2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jButton1))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jButton2).addComponent(jButton1))));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setData();
        this.manager.editarGrupo(this.nombre, this.nuevoNombre);
        if (this.owner.getCurrentDialog() == this) this.owner.setCurrentDialog(null);
        this.owner.updateData();
        this.dispose();
    }

    private void jTextField1KeyReleased(java.awt.event.KeyEvent evt) {
        if (jTextField1.getText().length() > 0) jButton1.setEnabled(true); else jButton1.setEnabled(false);
    }

    private void jComboBox1ItemStateChanged(java.awt.event.ItemEvent evt) {
        if (jComboBox1.getSelectedIndex() >= 0) {
            jTextField1.setEnabled(true);
            jTextField1.setText(jComboBox1.getSelectedItem().toString().trim());
        } else {
            jTextField1.setEnabled(false);
            jTextField1.setText("");
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        if (this.owner.getCurrentDialog() == this) this.owner.setCurrentDialog(null);
        this.dispose();
    }

    private void setData() {
        this.nombre = jComboBox1.getSelectedItem().toString().trim();
        this.nuevoNombre = jTextField1.getText().trim();
    }

    private javax.swing.JButton jButton1;

    private javax.swing.JButton jButton2;

    private javax.swing.JComboBox jComboBox1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JTextField jTextField1;

    private MainFrame owner;

    private Manager manager;

    private String nombre;

    private String nuevoNombre;
}
