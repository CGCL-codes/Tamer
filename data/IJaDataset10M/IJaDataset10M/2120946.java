package vete.Interfaz;

/**
 *
 * @author  Lisandro
 */
public class BuscarCliente extends javax.swing.JFrame {

    /** Creates new form BuscarCliente */
    public BuscarCliente() {
        initComponents();
    }

    private void initComponents() {
        jpGeneralCliente = new javax.swing.JPanel();
        tfCodigoCliente = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        tfNombreCliente = new javax.swing.JFormattedTextField();
        jLabel15 = new javax.swing.JLabel();
        tfCuitCliente = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jComboBox4 = new javax.swing.JComboBox();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        tfMailCliente = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        tfDescuentoCliente = new javax.swing.JTextField();
        jComboBox5 = new javax.swing.JComboBox();
        cbCondicionIvaCliente = new javax.swing.JComboBox();
        jLabel67 = new javax.swing.JLabel();
        jpDomicilioCliente = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jTextField6 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jComboBox6 = new javax.swing.JComboBox();
        jLabel57 = new javax.swing.JLabel();
        jLabel58 = new javax.swing.JLabel();
        jComboBox19 = new javax.swing.JComboBox();
        jComboBox20 = new javax.swing.JComboBox();
        jLabel59 = new javax.swing.JLabel();
        jbBuscarCliente = new javax.swing.JButton();
        jbCerrarBuscarCliente = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jpGeneralCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));
        jLabel8.setFont(new java.awt.Font("Arial", 0, 12));
        jLabel8.setText("Código");
        jLabel11.setFont(new java.awt.Font("Arial", 0, 12));
        jLabel11.setText("Nombre");
        jLabel15.setFont(new java.awt.Font("Arial", 0, 12));
        jLabel15.setText("CUIT");
        jLabel12.setText("Teléfono");
        jComboBox4.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Casa", "Trabajo", "Movil" }));
        jLabel13.setText("Mail");
        jLabel14.setText("Descuento");
        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", " %" }));
        cbCondicionIvaCliente.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Consumidor Final", "Responsable Inscripto" }));
        jLabel67.setText("Condición de IVA");
        javax.swing.GroupLayout jpGeneralClienteLayout = new javax.swing.GroupLayout(jpGeneralCliente);
        jpGeneralCliente.setLayout(jpGeneralClienteLayout);
        jpGeneralClienteLayout.setHorizontalGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpGeneralClienteLayout.createSequentialGroup().addContainerGap().addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpGeneralClienteLayout.createSequentialGroup().addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpGeneralClienteLayout.createSequentialGroup().addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jpGeneralClienteLayout.createSequentialGroup().addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel8).addComponent(tfCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(6, 6, 6).addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(tfNombreCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpGeneralClienteLayout.createSequentialGroup().addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpGeneralClienteLayout.createSequentialGroup().addComponent(tfDescuentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox5, 0, 0, Short.MAX_VALUE)).addComponent(jComboBox4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpGeneralClienteLayout.createSequentialGroup().addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField3, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)).addComponent(cbCondicionIvaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel67)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)).addGroup(jpGeneralClienteLayout.createSequentialGroup().addComponent(jLabel12).addGap(317, 317, 317))).addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel13).addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(tfCuitCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE).addComponent(tfMailCliente)))).addComponent(jLabel14)).addContainerGap()));
        jpGeneralClienteLayout.setVerticalGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpGeneralClienteLayout.createSequentialGroup().addContainerGap().addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel8).addComponent(jLabel11).addComponent(jLabel15)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tfCodigoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(tfNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(tfCuitCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel12).addComponent(jLabel13)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(tfMailCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel14).addComponent(jLabel67)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jpGeneralClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(tfDescuentoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(cbCondicionIvaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jpDomicilioCliente.setBorder(javax.swing.BorderFactory.createTitledBorder("Domicio"));
        jLabel16.setText("Calle");
        jLabel17.setText("Numero");
        jLabel18.setText("Piso");
        jLabel55.setText("Depto");
        jLabel56.setText("Localidad");
        jComboBox6.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Capital", "Las Heras", "Guaymallén", "Godoy Cruz" }));
        jLabel58.setText("Provincia");
        jComboBox19.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Mendoza", "Córdoba", "Buenos Aires", "Tucuman" }));
        jComboBox20.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Argentina", "Bolivia", "Chile", "Brasil" }));
        jLabel59.setText("País");
        javax.swing.GroupLayout jpDomicilioClienteLayout = new javax.swing.GroupLayout(jpDomicilioCliente);
        jpDomicilioCliente.setLayout(jpDomicilioClienteLayout);
        jpDomicilioClienteLayout.setHorizontalGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addContainerGap().addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel56).addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel16).addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel58, javax.swing.GroupLayout.Alignment.TRAILING)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel17))).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jComboBox19, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))).addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 205, Short.MAX_VALUE).addComponent(jLabel57).addGap(46, 46, 46)).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addGap(11, 11, 11).addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel18).addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(18, 18, 18).addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel55).addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel59)).addContainerGap()).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jComboBox20, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()))));
        jpDomicilioClienteLayout.setVerticalGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addContainerGap().addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addComponent(jLabel17).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addComponent(jLabel16).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addComponent(jLabel18).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jTextField9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addComponent(jLabel55).addGap(29, 29, 29))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jpDomicilioClienteLayout.createSequentialGroup().addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel56).addComponent(jLabel57)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(jpDomicilioClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jComboBox19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jComboBox20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel58).addComponent(jLabel59)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jbBuscarCliente.setText("Buscar");
        jbCerrarBuscarCliente.setText("Cerrar");
        jbCerrarBuscarCliente.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jbCerrarBuscarClienteMouseClicked(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jpDomicilioCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jpGeneralCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(428, Short.MAX_VALUE).addComponent(jbBuscarCliente).addGap(18, 18, 18).addComponent(jbCerrarBuscarCliente).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jpGeneralCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jpDomicilioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jbCerrarBuscarCliente).addComponent(jbBuscarCliente)).addContainerGap()));
        pack();
    }

    private void jbCerrarBuscarClienteMouseClicked(java.awt.event.MouseEvent evt) {
        this.dispose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new BuscarCliente().setVisible(true);
            }
        });
    }

    private javax.swing.JComboBox cbCondicionIvaCliente;

    private javax.swing.JComboBox jComboBox19;

    private javax.swing.JComboBox jComboBox20;

    private javax.swing.JComboBox jComboBox4;

    private javax.swing.JComboBox jComboBox5;

    private javax.swing.JComboBox jComboBox6;

    private javax.swing.JLabel jLabel11;

    private javax.swing.JLabel jLabel12;

    private javax.swing.JLabel jLabel13;

    private javax.swing.JLabel jLabel14;

    private javax.swing.JLabel jLabel15;

    private javax.swing.JLabel jLabel16;

    private javax.swing.JLabel jLabel17;

    private javax.swing.JLabel jLabel18;

    private javax.swing.JLabel jLabel55;

    private javax.swing.JLabel jLabel56;

    private javax.swing.JLabel jLabel57;

    private javax.swing.JLabel jLabel58;

    private javax.swing.JLabel jLabel59;

    private javax.swing.JLabel jLabel67;

    private javax.swing.JLabel jLabel8;

    private javax.swing.JTextField jTextField2;

    private javax.swing.JTextField jTextField3;

    private javax.swing.JTextField jTextField6;

    private javax.swing.JTextField jTextField7;

    private javax.swing.JTextField jTextField8;

    private javax.swing.JTextField jTextField9;

    private javax.swing.JButton jbBuscarCliente;

    private javax.swing.JButton jbCerrarBuscarCliente;

    private javax.swing.JPanel jpDomicilioCliente;

    private javax.swing.JPanel jpGeneralCliente;

    private javax.swing.JFormattedTextField tfCodigoCliente;

    private javax.swing.JTextField tfCuitCliente;

    private javax.swing.JTextField tfDescuentoCliente;

    private javax.swing.JTextField tfMailCliente;

    private javax.swing.JFormattedTextField tfNombreCliente;
}
