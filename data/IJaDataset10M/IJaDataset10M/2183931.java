package vista.editores;

import org.omg.uml.foundation.core.Feature;
import org.omg.uml.foundation.datatypes.ScopeKindEnum;
import org.omg.uml.foundation.datatypes.VisibilityKindEnum;
import vista.VistaEdicion;

/**
 *
 * @author  Juan Timoteo Ponce Ortiz
 */
public class EditorCaracteristica extends javax.swing.JPanel implements VistaEdicion {

    private Feature elemento;

    /** Creates new form EditorCaracteristica */
    public EditorCaracteristica(Feature elemento) {
        this.elemento = elemento;
        initComponents();
    }

    private void initComponents() {
        comboVisibilidad = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        chkEstatico = new javax.swing.JCheckBox();
        setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        comboVisibilidad.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ninguna", "Publica", "Privada", "Protegida", "Paquete" }));
        jLabel1.setText("Visibilidad:");
        chkEstatico.setText("Estatico");
        chkEstatico.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        chkEstatico.setMargin(new java.awt.Insets(0, 0, 0, 0));
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(chkEstatico).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel1).addGap(17, 17, 17).addComponent(comboVisibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(85, 85, 85)));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(comboVisibilidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel1).addComponent(chkEstatico)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private javax.swing.JCheckBox chkEstatico;

    private javax.swing.JComboBox comboVisibilidad;

    private javax.swing.JLabel jLabel1;

    public void updateModelo() {
        int index = comboVisibilidad.getSelectedIndex();
        switch(index) {
            case 0:
                elemento.setVisibility(null);
                break;
            case 1:
                elemento.setVisibility(VisibilityKindEnum.VK_PUBLIC);
                break;
            case 2:
                elemento.setVisibility(VisibilityKindEnum.VK_PRIVATE);
                break;
            case 3:
                elemento.setVisibility(VisibilityKindEnum.VK_PROTECTED);
                break;
            case 4:
                elemento.setVisibility(VisibilityKindEnum.VK_PACKAGE);
                break;
        }
        if (chkEstatico.isSelected()) elemento.setOwnerScope(ScopeKindEnum.SK_CLASSIFIER);
    }

    public void addListener(EditorListener listener) {
    }

    public void setModelo(Object modelo) {
        this.elemento = (Feature) modelo;
    }

    public void updateVista() {
        if (elemento.getVisibility() == null) comboVisibilidad.setSelectedIndex(0); else if (elemento.getVisibility() == VisibilityKindEnum.VK_PUBLIC) comboVisibilidad.setSelectedIndex(1); else if (elemento.getVisibility() == VisibilityKindEnum.VK_PRIVATE) comboVisibilidad.setSelectedIndex(2); else if (elemento.getVisibility() == VisibilityKindEnum.VK_PROTECTED) comboVisibilidad.setSelectedIndex(3); else if (elemento.getVisibility() == VisibilityKindEnum.VK_PACKAGE) comboVisibilidad.setSelectedIndex(4);
        if (elemento.getOwnerScope() == ScopeKindEnum.SK_CLASSIFIER) chkEstatico.setSelected(true); else chkEstatico.setSelected(false);
    }

    public String getTitulo() {
        return "Editor caracteristica";
    }
}
