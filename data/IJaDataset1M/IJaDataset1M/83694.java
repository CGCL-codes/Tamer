package librodeesher;

import java.awt.Toolkit;

/**
 *
 * @author  jorge
 */
public class ElegirComunProfesionalGUI extends javax.swing.JFrame {

    String tipo;

    Categoria cat;

    int maxElegir;

    Personaje pj;

    String cuando;

    String[] listadoHabilidades = null;

    /** Creates new form ElegirComunProfesional */
    ElegirComunProfesionalGUI(String tmp_tipo, Categoria tmp_cat, int habilidades, Personaje tmp_pj, String tmp_cuando) {
        tipo = tmp_tipo;
        cat = tmp_cat;
        maxElegir = habilidades;
        pj = tmp_pj;
        cuando = tmp_cuando;
        initComponents();
        setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - (int) (this.getWidth() / 2), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - (int) (this.getHeight() / 2));
        TipoCheckBox.setText(tipo);
        Inicializar();
    }

    ElegirComunProfesionalGUI(String tmp_tipo, Categoria tmp_cat, int habilidades, Personaje tmp_pj, String tmp_cuando, String[] tmp_listadoHabilidades) {
        tipo = tmp_tipo;
        cat = tmp_cat;
        maxElegir = habilidades;
        pj = tmp_pj;
        cuando = tmp_cuando;
        listadoHabilidades = tmp_listadoHabilidades;
        initComponents();
        setLocation((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth() / 2 - (int) (this.getWidth() / 2), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight() / 2 - (int) (this.getHeight() / 2));
        TipoCheckBox.setText(tipo);
        Inicializar();
    }

    private void Inicializar() {
        TipoCheckBox.setText(tipo);
        ActualizaHabilidadesRestantes();
        CategoriaTextField.setText(cat.DevolverNombre());
        ActualizarHabilidadesComboBox();
    }

    private void ActualizaHabilidadesRestantes() {
        NumeroTextField.setText(maxElegir - pj.ContarHabilidadesEspeciales(cat, tipo) + "");
    }

    void ActualizarHabilidadesComboBox() {
        HabilidadesComboBox.removeAllItems();
        if (listadoHabilidades == null) {
            try {
                cat.OrdenarHabilidades();
                for (int j = 0; j < cat.listaHabilidades.size(); j++) {
                    Habilidad hab = cat.listaHabilidades.get(j);
                    HabilidadesComboBox.addItem(hab.DevolverNombre());
                }
            } catch (NullPointerException npe) {
            }
        } else {
            for (int j = 0; j < listadoHabilidades.length; j++) {
                HabilidadesComboBox.addItem(listadoHabilidades[j]);
            }
        }
    }

    private void initComponents() {
        HabilidadesComboBox = new javax.swing.JComboBox();
        TipoCheckBox = new javax.swing.JCheckBox();
        NumeroTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        CategoriaTextField = new javax.swing.JTextField();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Selección de Habilidades Propicias");
        setResizable(false);
        HabilidadesComboBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HabilidadesComboBoxActionPerformed(evt);
            }
        });
        TipoCheckBox.setText("Ok");
        TipoCheckBox.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        TipoCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoCheckBoxActionPerformed(evt);
            }
        });
        NumeroTextField.setEditable(false);
        jLabel1.setText("Categoria:");
        jLabel2.setText("Habilidad");
        jLabel3.setText("Elegir:");
        CategoriaTextField.setEditable(false);
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(HabilidadesComboBox, 0, 228, Short.MAX_VALUE).addComponent(CategoriaTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 228, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(TipoCheckBox).addComponent(jLabel3).addComponent(NumeroTextField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))).addComponent(jLabel2)).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(CategoriaTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(HabilidadesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(NumeroTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(27, 27, 27).addComponent(TipoCheckBox))).addContainerGap(17, Short.MAX_VALUE)));
        pack();
    }

    private void TipoCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {
        Habilidad hab = pj.DevolverHabilidadDeNombre(HabilidadesComboBox.getSelectedItem().toString());
        if (pj.ContarHabilidadesEspeciales(cat, tipo) >= maxElegir) TipoCheckBox.setSelected(false);
        if (tipo.equals("Común")) {
            if (TipoCheckBox.isSelected()) {
                if (cuando.equals("profesion")) hab.HacerComunProfesion();
                if (cuando.equals("raza")) hab.HacerComunRaza();
                if (cuando.equals("cultura")) hab.HacerComunCultura();
            } else {
                if (cuando.equals("profesion")) hab.NoEsComunProfesion();
                if (cuando.equals("raza")) hab.NoEsComunRaza();
                if (cuando.equals("cultura")) hab.NoEsComunCultura();
            }
        }
        if (tipo.equals("Profesional")) {
            if (TipoCheckBox.isSelected()) {
                hab.HacerProfesional();
            } else hab.NoEsProfesional();
        }
        if (tipo.equals("Restringida")) {
            if (TipoCheckBox.isSelected()) hab.HacerRestringida(); else hab.NoEsRestringidaRaza();
            TipoCheckBox.setSelected(hab.EsComun());
        }
        ActualizaHabilidadesRestantes();
    }

    private void HabilidadesComboBoxActionPerformed(java.awt.event.ActionEvent evt) {
        Habilidad hab = pj.DevolverHabilidadDeNombre(HabilidadesComboBox.getSelectedItem().toString());
        if (tipo.equals("Común")) {
            if (cuando.equals("profesion")) TipoCheckBox.setSelected(hab.EsComunProfesion());
            if (cuando.equals("raza")) TipoCheckBox.setSelected(hab.EsComunRaza());
            if (cuando.equals("cultura")) TipoCheckBox.setSelected(hab.EsComunCultura());
        }
        if (tipo.equals("Profesional")) TipoCheckBox.setSelected(hab.EsProfesional());
        if (tipo.equals("Restringida")) TipoCheckBox.setSelected(hab.EsRestringida());
    }

    private javax.swing.JTextField CategoriaTextField;

    private javax.swing.JComboBox HabilidadesComboBox;

    private javax.swing.JTextField NumeroTextField;

    private javax.swing.JCheckBox TipoCheckBox;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;
}
