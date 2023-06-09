package moduloConsulta;

import ModuloDiagnosticador.procesadorEntradaSalida;
import java.util.*;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 *
 * @author rayner
 */
public class AnadirCasoGUI extends javax.swing.JFrame {

    /** Creates new form AnadirCasoGUI */
    private procesadorEntradaSalida procesador;

    private ArrayList<Integer> sintomasEncontrados;

    private String[] sintomasNombre;

    private final int SINT_NO_PRESENTE = -1;

    private final int SINT_PRESENTE = 1;

    private final int SINT = 0;

    private DefaultListModel lmSintomas = new DefaultListModel();

    private DefaultListModel lmNoSintomas = new DefaultListModel();

    private DefaultListModel lmSiSintomas = new DefaultListModel();

    public AnadirCasoGUI() {
        initComponents();
        inicializar_estado();
    }

    private void inicializar_estado() {
        jcbSexo.removeAllItems();
        jcbSexo.addItem("Masculino");
        jcbSexo.addItem("Femenino");
        jtfEdad.setText("");
        procesador = new procesadorEntradaSalida();
        String temp[] = procesador.getEnfermedadesNames();
        sintomasNombre = procesador.getSintomasNames();
        jcbEnfermedades.removeAllItems();
        for (int i = 0; i < temp.length; i++) {
            jcbEnfermedades.addItem(temp[i]);
        }
        jlSintomas.setModel(lmSintomas);
        jlSintomasPresentes.setModel(lmSiSintomas);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jcbEnfermedades = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jbGuardar = new javax.swing.JButton();
        jbCancelar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jtfSintoma = new javax.swing.JTextField();
        jbBuscar = new javax.swing.JButton();
        jbSintomasToSi = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jlSintomas = new javax.swing.JList();
        jScrollPane6 = new javax.swing.JScrollPane();
        jlSintomasPresentes = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        jbSiToSintomas = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jcbSexo = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jtfEdad = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtaRecomendacion = new javax.swing.JTextArea();
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 18));
        jLabel1.setText("Añadir Caso Clínico");
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap(323, Short.MAX_VALUE).addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(314, 314, 314)));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Agregar Enfermedad"));
        jcbEnfermedades.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel2.setText("Elija alguna enfermedad:");
        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addContainerGap().addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jcbEnfermedades, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel4Layout.setVerticalGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel4Layout.createSequentialGroup().addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jcbEnfermedades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jbGuardar.setText("Guardar");
        jbGuardar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbGuardarActionPerformed(evt);
            }
        });
        jbCancelar.setText("Cancelar");
        jbCancelar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCancelarActionPerformed(evt);
            }
        });
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingreso de Sintomas o Signos"));
        jtfSintoma.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jtfSintomaActionPerformed(evt);
            }
        });
        jbBuscar.setText("IR");
        jbBuscar.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbBuscarActionPerformed(evt);
            }
        });
        jbSintomasToSi.setText(">");
        jbSintomasToSi.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSintomasToSiActionPerformed(evt);
            }
        });
        jlSintomas.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", " " };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane2.setViewportView(jlSintomas);
        jlSintomasPresentes.setModel(new javax.swing.AbstractListModel() {

            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };

            public int getSize() {
                return strings.length;
            }

            public Object getElementAt(int i) {
                return strings[i];
            }
        });
        jScrollPane6.setViewportView(jlSintomasPresentes);
        jLabel5.setText("Sintomas y signos presentes");
        jbSiToSintomas.setText("<");
        jbSiToSintomas.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbSiToSintomasActionPerformed(evt);
            }
        });
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jtfSintoma, javax.swing.GroupLayout.PREFERRED_SIZE, 334, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jbBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)).addGap(18, 18, 18).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(jPanel3Layout.createSequentialGroup().addComponent(jbSiToSintomas, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18)).addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup().addComponent(jbSintomasToSi, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18))).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5).addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap()));
        jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addGap(32, 32, 32).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jtfSintoma, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jbBuscar).addComponent(jLabel5)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup().addGap(126, 126, 126).addComponent(jbSintomasToSi).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 54, Short.MAX_VALUE).addComponent(jbSiToSintomas).addGap(84, 84, 84)));
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Paciente"));
        jLabel3.setText("Sexo:");
        jcbSexo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jLabel4.setText("Edad:");
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(3, 3, 3).addComponent(jcbSexo, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jtfEdad, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jcbSexo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4).addComponent(jtfEdad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addContainerGap(16, Short.MAX_VALUE)));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Recomendacion"));
        jtaRecomendacion.setColumns(20);
        jtaRecomendacion.setRows(5);
        jScrollPane1.setViewportView(jtaRecomendacion);
        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE));
        jPanel5Layout.setVerticalGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel5Layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE).addContainerGap()));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jbGuardar, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addComponent(jbCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(jPanel3, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGap(56, 56, 56).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jbGuardar).addComponent(jbCancelar)))).addContainerGap()));
        pack();
    }

    private void jbCancelarActionPerformed(java.awt.event.ActionEvent evt) {
        new MenuPrincipalGUI().setVisible(true);
        dispose();
    }

    private void jtfSintomaActionPerformed(java.awt.event.ActionEvent evt) {
        String query = jtfSintoma.getText();
        sintomasEncontrados = procesador.buscarSintomas(query);
        lmSintomas.clear();
        for (int i = 0; i < sintomasEncontrados.size(); i++) {
            lmSintomas.addElement(sintomasNombre[sintomasEncontrados.get(i)]);
        }
    }

    private void jbBuscarActionPerformed(java.awt.event.ActionEvent evt) {
        String query = jtfSintoma.getText();
        sintomasEncontrados = procesador.buscarSintomas(query);
        lmSintomas.clear();
        for (int i = 0; i < sintomasEncontrados.size(); i++) {
            lmSintomas.addElement(sintomasNombre[sintomasEncontrados.get(i)]);
        }
    }

    private void jbSintomasToSiActionPerformed(java.awt.event.ActionEvent evt) {
        int indice = jlSintomas.getSelectedIndex();
        if (indice == -1) {
            JOptionPane.showMessageDialog(null, "No selecciono ningun item");
            return;
        }
        procesador.setCheckPosition(sintomasEncontrados.get(indice), SINT_PRESENTE);
        procesador.sintToSi(sintomasEncontrados.get(indice));
        lmSintomas.remove(indice);
        lmSiSintomas.addElement(sintomasNombre[sintomasEncontrados.get(indice)]);
        sintomasEncontrados.remove(indice);
    }

    private void jbSiToSintomasActionPerformed(java.awt.event.ActionEvent evt) {
        int indice = jlSintomasPresentes.getSelectedIndex();
        if (indice == -1) {
            JOptionPane.showMessageDialog(null, "No selecciono ningun item");
            return;
        }
        int p = procesador.siToSint(indice);
        procesador.setCheckPosition(p, SINT);
        sintomasEncontrados.add(p);
        lmSiSintomas.remove(indice);
        if (sintomasNombre[p].indexOf(jtfSintoma.getText()) != -1) {
            lmSintomas.addElement(sintomasNombre[p]);
        }
    }

    private void jbGuardarActionPerformed(java.awt.event.ActionEvent evt) {
        String edad = jtfEdad.getText();
        if (edad.length() == 0) {
            JOptionPane.showMessageDialog(null, "edad debe ser un dato numerico", "ERROR!!", 0);
            return;
        }
        for (int i = 0; i < edad.length(); i++) {
            if (edad.charAt(i) < '0' || edad.charAt(i) > '9') {
                JOptionPane.showMessageDialog(null, "edad debe ser un dato numerico", "ERROR!!", 0);
                return;
            }
        }
        procesador.guardarCaso(jtaRecomendacion.getText(), jcbSexo.getSelectedIndex(), Integer.parseInt(jtfEdad.getText()), jcbEnfermedades.getSelectedIndex());
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new AnadirCasoGUI().setVisible(true);
            }
        });
    }

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JLabel jLabel5;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JPanel jPanel2;

    private javax.swing.JPanel jPanel3;

    private javax.swing.JPanel jPanel4;

    private javax.swing.JPanel jPanel5;

    private javax.swing.JScrollPane jScrollPane1;

    private javax.swing.JScrollPane jScrollPane2;

    private javax.swing.JScrollPane jScrollPane6;

    private javax.swing.JButton jbBuscar;

    private javax.swing.JButton jbCancelar;

    private javax.swing.JButton jbGuardar;

    private javax.swing.JButton jbSiToSintomas;

    private javax.swing.JButton jbSintomasToSi;

    private javax.swing.JComboBox jcbEnfermedades;

    private javax.swing.JComboBox jcbSexo;

    private javax.swing.JList jlSintomas;

    private javax.swing.JList jlSintomasPresentes;

    private javax.swing.JTextArea jtaRecomendacion;

    private javax.swing.JTextField jtfEdad;

    private javax.swing.JTextField jtfSintoma;
}
