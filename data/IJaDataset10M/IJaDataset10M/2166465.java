package prototipo;

import BE.torneo;
import BE.division;
import BL.torneoBL;
import BL.divisionBL;
import javax.swing.JOptionPane;

/**
 *
 * @author  Billy
 */
public class frmActualizarDivisiones extends javax.swing.JInternalFrame {

    division admDivision;

    torneo miTorneo;

    torneoBL eltorneoBL;

    frmAdmTorneo fat;

    frmAgregarJugadores faj;

    ArrayListComboBoxModel modeloCombo;

    divisionBL divBL;

    int idDivisionElegida;

    int indPtsxBye;

    double valorxBye;

    /** Creates new form frmAgregarDiviones */
    public frmActualizarDivisiones(torneo miTorneo, frmAdmTorneo fat) {
        initComponents();
        this.fat = fat;
        this.miTorneo = miTorneo;
        this.eltorneoBL = new torneoBL();
        this.divBL = new divisionBL(miTorneo.getIdTorneo());
        this.idDivisionElegida = this.fat.dameIdDivisionElegido();
        admDivision = this.divBL.getDivision(this.idDivisionElegida);
        this.lblTipoTorneo.setText("1");
        this.lblNumFechas.setText("1");
        this.txtNombreTorneo.setText(miTorneo.getnombreTorneo());
        this.txtNombreDivision.setText(admDivision.getnombreDivision());
        this.cmbTipoTorneo.setSelectedIndex(admDivision.getidTipoTorneo() - 1);
        if (admDivision.getnumFechas() > 3) this.cmblNumFechas.setSelectedIndex(admDivision.getnumFechas() - 3); else this.cmblNumFechas.setSelectedIndex(0);
        String strPtsxBye = admDivision.getPtosxBye() + "";
        if (strPtsxBye.equals("0") || strPtsxBye.equals("0.0")) indPtsxBye = 0; else if (strPtsxBye.equals("0.5")) indPtsxBye = 1; else if (strPtsxBye.equals("1") || strPtsxBye.equals("1.0")) indPtsxBye = 2;
        this.cmbPtosxBye.setSelectedIndex(indPtsxBye);
        this.jtaDescripcion.setText(admDivision.getDescripcion());
        this.lblTipoTorneo.setVisible(false);
        this.lblNumFechas.setVisible(false);
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNombreDivision = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        cmbTipoTorneo = new javax.swing.JComboBox();
        lblTipoTorneo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaDescripcion = new javax.swing.JTextArea();
        jLabel6 = new javax.swing.JLabel();
        cmblNumFechas = new javax.swing.JComboBox();
        lblNumFechas = new javax.swing.JLabel();
        txtNombreTorneo = new javax.swing.JTextField();
        lblPtosxBye = new javax.swing.JLabel();
        cmbPtosxBye = new javax.swing.JComboBox();
        setClosable(true);
        setIconifiable(true);
        setTitle("Actualizar Division de Torneo");
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jLabel1.setText("Nombre de Divisi�n:");
        jLabel2.setText("Nombre de Torneo:");
        jLabel4.setText("Descripci�n:");
        btnActualizar.setText("Actualizar");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });
        btnLimpiar.setText("Limpiar");
        jLabel3.setText("Tipo de Torneo:");
        cmbTipoTorneo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sistema Suizo", "Todos contra todos (1ronda)", "Todos contra todos (2rondas)" }));
        cmbTipoTorneo.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbTipoTorneoItemStateChanged(evt);
            }
        });
        lblTipoTorneo.setText("_");
        jtaDescripcion.setColumns(20);
        jtaDescripcion.setRows(5);
        jScrollPane1.setViewportView(jtaDescripcion);
        jLabel6.setText("N�mero de Fechas:");
        cmblNumFechas.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20" }));
        cmblNumFechas.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmblNumFechasItemStateChanged(evt);
            }
        });
        lblNumFechas.setText("_");
        txtNombreTorneo.setEditable(false);
        lblPtosxBye.setText("Puntos por BYE:");
        cmbPtosxBye.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "0", "0.5", "1" }));
        cmbPtosxBye.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbPtosxByeItemStateChanged(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(txtNombreTorneo, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE).addComponent(jLabel2).addComponent(jLabel1).addComponent(txtNombreDivision, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE).addComponent(lblTipoTorneo)).addComponent(cmbTipoTorneo, 0, 202, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE).addComponent(lblNumFechas).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmblNumFechas, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(lblPtosxBye).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE).addComponent(cmbPtosxBye, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jLabel4).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(btnLimpiar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 49, Short.MAX_VALUE).addComponent(btnActualizar).addGap(19, 19, 19)))));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtNombreTorneo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(8, 8, 8).addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(txtNombreDivision, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(lblTipoTorneo)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(cmbTipoTorneo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(15, 15, 15).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel6).addComponent(cmblNumFechas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblNumFechas)).addGap(14, 14, 14).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(cmbPtosxBye, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(lblPtosxBye)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(17, 17, 17).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(btnActualizar).addComponent(btnLimpiar))));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE).addContainerGap()));
        pack();
    }

    private void cmbPtosxByeItemStateChanged(java.awt.event.ItemEvent evt) {
        if (this.cmbPtosxBye.getSelectedItem().toString().equals("0")) {
            System.out.println("00000");
            valorxBye = 0;
        } else if (this.cmbPtosxBye.getSelectedItem().toString().equals("0.5")) {
            System.out.println("05050");
            valorxBye = 0.5;
        } else if (this.cmbPtosxBye.getSelectedItem().toString().equals("1")) {
            System.out.println("111111");
            valorxBye = 1;
        }
    }

    private void cmblNumFechasItemStateChanged(java.awt.event.ItemEvent evt) {
        this.lblNumFechas.setText(this.cmblNumFechas.getSelectedItem().toString());
    }

    private void cmbTipoTorneoItemStateChanged(java.awt.event.ItemEvent evt) {
        if (this.cmbTipoTorneo.getSelectedItem().toString().equals("Sistema Suizo")) {
            this.lblTipoTorneo.setText("1");
            this.cmblNumFechas.setEnabled(true);
        }
        if (this.cmbTipoTorneo.getSelectedItem().toString().equals("Todos contra todos (1ronda)")) {
            this.lblTipoTorneo.setText("2");
            this.cmblNumFechas.setEnabled(false);
        }
        if (this.cmbTipoTorneo.getSelectedItem().toString().equals("Todos contra todos (2rondas)")) {
            this.lblTipoTorneo.setText("3");
            this.cmblNumFechas.setEnabled(false);
        }
    }

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {
        admDivision.setnombreDivision(this.txtNombreDivision.getText());
        admDivision.setDescripcion(this.jtaDescripcion.getText());
        admDivision.setnumJugadores(Integer.parseInt("0"));
        admDivision.setRondaActual(Integer.parseInt("0"));
        admDivision.setnumFechas(Integer.parseInt(this.lblNumFechas.getText()));
        admDivision.setTerminado('0');
        admDivision.setDesempate(0);
        admDivision.setidTorneo(this.miTorneo.getIdTorneo());
        admDivision.setidTipoTorneo(Integer.parseInt(this.lblTipoTorneo.getText()));
        admDivision.setPtosxBye(this.valorxBye);
        divisionBL admDivisionBL = new divisionBL(this.miTorneo.getIdTorneo());
        if (admDivision.getRondaActual() == 0) {
            if (admDivisionBL.actualizarDivisionActual(admDivision)) {
                JOptionPane.showMessageDialog(this, "Los datos han sido actualizados correctamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
                this.fat.actualizardatosDivision(admDivision);
            } else JOptionPane.showMessageDialog(this, "Ha Ocurrido un error. No se han grabado los datos", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        } else JOptionPane.showMessageDialog(this, "Ya empezo la division. No se actualizar�n los datos", "Informacion", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
    }

    private javax.swing.JButton btnActualizar;

    private javax.swing.JButton btnLimpiar;

    private javax.swing.JComboBox cmbPtosxBye;

    private javax.swing.JComboBox cmbTipoTorneo;

    private javax.swing.JComboBox cmblNumFechas;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel6;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JTextArea jtaDescripcion;

    private javax.swing.JLabel lblNumFechas;

    private javax.swing.JLabel lblPtosxBye;

    private javax.swing.JLabel lblTipoTorneo;

    private javax.swing.JTextField txtNombreDivision;

    private javax.swing.JTextField txtNombreTorneo;
}
