package com.catosist.run.application.venta;

import com.catosist.run.business.gestorruta.GestorRuta;
import com.catosist.run.business.gestorvehiculo.GestorVehiculo;
import com.catosist.run.business.gestorviaje.GestorViaje;
import com.catosist.run.services.ComprobanteDTO;
import com.catosist.run.services.ViajeDTO;
import java.awt.Dialog;
import java.util.Iterator;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author igorov
 */
public class DialogSeleccionViaje extends javax.swing.JDialog {

    /** Creates new form DialogSeleccionViaje */
    public DialogSeleccionViaje(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        agregarViajes();
    }

    public DialogSeleccionViaje(Dialog owner, boolean modal, ComprobanteDTO comprobanteDTO, GestorViaje gestorViaje) {
        super(owner, modal);
        initComponents();
        this.gestorViaje = gestorViaje;
        agregarViajes();
        this.comprobanteDTO = comprobanteDTO;
    }

    private void agregarViajes() {
        List<ViajeDTO> viajeDTOs = gestorViaje.getAll();
        DefaultComboBoxModel model = new DefaultComboBoxModel();
        for (Iterator<ViajeDTO> it = viajeDTOs.iterator(); it.hasNext(); ) {
            ViajeDTO viajeDTO = it.next();
            String ruta = viajeDTO.getRuta();
            String placa = viajeDTO.getPlacaVehiculo();
            model.addElement(viajeDTO.getId() + ", " + ruta + ", " + placa);
        }
        this.cmbViajes.setModel(model);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbViajes = new javax.swing.JComboBox();
        btnAceptar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnDesasociar = new javax.swing.JButton();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form");
        jPanel1.setName("jPanel1");
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(com.catosist.run.application.desktopapplication.DesktopApplication.class).getContext().getResourceMap(DialogSeleccionViaje.class);
        jLabel1.setFont(resourceMap.getFont("jLabel1.font"));
        jLabel1.setText(resourceMap.getString("jLabel1.text"));
        jLabel1.setName("jLabel1");
        cmbViajes.setFont(resourceMap.getFont("cmbViajes.font"));
        cmbViajes.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbViajes.setName("cmbViajes");
        btnAceptar.setText(resourceMap.getString("btnAceptar.text"));
        btnAceptar.setName("btnAceptar");
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });
        btnCancelar.setText(resourceMap.getString("btnCancelar.text"));
        btnCancelar.setName("btnCancelar");
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        btnDesasociar.setText(resourceMap.getString("btnDesasociar.text"));
        btnDesasociar.setName("btnDesasociar");
        btnDesasociar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesasociarActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(32, 32, 32).addComponent(cmbViajes, 0, 709, Short.MAX_VALUE)).addGroup(jPanel1Layout.createSequentialGroup().addGap(199, 199, 199).addComponent(btnAceptar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnDesasociar).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(btnCancelar))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(cmbViajes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(btnAceptar).addComponent(btnDesasociar).addComponent(btnCancelar)).addGap(26, 26, 26)));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
        pack();
    }

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {
        int viajeId = gestorViaje.getByIndex(cmbViajes.getSelectedIndex()).getId();
        comprobanteDTO.setViaje(viajeId);
        this.setVisible(false);
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
    }

    private void btnDesasociarActionPerformed(java.awt.event.ActionEvent evt) {
        comprobanteDTO.setViaje(-1);
        this.setVisible(false);
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                DialogSeleccionViaje dialog = new DialogSeleccionViaje(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {

                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    private javax.swing.JButton btnAceptar;

    private javax.swing.JButton btnCancelar;

    private javax.swing.JButton btnDesasociar;

    private javax.swing.JComboBox cmbViajes;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private GestorViaje gestorViaje;

    ComprobanteDTO comprobanteDTO;
}
