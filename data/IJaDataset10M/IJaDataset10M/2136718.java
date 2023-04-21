package moduledefault.elicitedbases.postgresrafael.view.jpanel;

import moduledefault.elicitedbases.postgresrafael.util.jtable.RafaelJTable;
import java.util.Vector;
import moduledefault.elicitedbases.postgresrafael.model.beans.Coluna;

/**
 *
 * @author  Rafael
 */
public class JPPreviaDados extends javax.swing.JPanel {

    /** Creates new form JPPreviaDados */
    public JPPreviaDados() {
        initComponents();
        rafaelJTable.setVisible(true);
    }

    public void addColuna(Vector v) {
        rafaelJTable.addColumn(v);
    }

    public int getNColunas() {
        return rafaelJTable.getColumnCount();
    }

    public int getNLinhas() {
        return rafaelJTable.getRowCount();
    }

    public void remColuna(String col) {
        rafaelJTable.removeColumn(col);
    }

    public Object[][] getInput() {
        return rafaelJTable.getInput();
    }

    public Object[] getOutput() {
        return rafaelJTable.getOutput();
    }

    public Vector rafaelJTable1GetColumnIdentifiers() {
        return rafaelJTable.getColumnIdentifiers();
    }

    public RafaelJTable getRafaelJTable1() {
        return this.rafaelJTable;
    }

    private void jCheckBoxHasMetaAtributeItemStateChanged() {
        this.rafaelJTable.setMetaAtribute(jCheckBoxHasMetaAtribute.isSelected());
    }

    private void initComponents() {
        jScrollPane1 = new javax.swing.JScrollPane();
        rafaelJTable = new moduledefault.elicitedbases.postgresrafael.util.jtable.RafaelJTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jCheckBoxHasMetaAtribute = new javax.swing.JCheckBox();
        setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Pré-Visualização dos Dados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Calibri", 1, 16)));
        rafaelJTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        rafaelJTable.setMetaAtribute(true);
        jScrollPane1.setViewportView(rafaelJTable);
        jPanel1.setBackground(RafaelJTable.colorMeta);
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setMaximumSize(new java.awt.Dimension(15, 15));
        jPanel1.setMinimumSize(new java.awt.Dimension(15, 15));
        jPanel1.setPreferredSize(new java.awt.Dimension(15, 15));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 13, Short.MAX_VALUE));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 13, Short.MAX_VALUE));
        jLabel1.setFont(new java.awt.Font("Calibri", 0, 12));
        jLabel1.setText("Atributo Alvo");
        jCheckBoxHasMetaAtribute.setFont(new java.awt.Font("Calibri", 0, 12));
        jCheckBoxHasMetaAtribute.setSelected(true);
        jCheckBoxHasMetaAtribute.setText("Apresenta atributo Alvo");
        jCheckBoxHasMetaAtribute.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jCheckBoxHasMetaAtributeItemStateChanged(evt);
            }
        });
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addComponent(jCheckBoxHasMetaAtribute).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 442, Short.MAX_VALUE).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel1))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 416, Short.MAX_VALUE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jCheckBoxHasMetaAtribute).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()));
    }

    private void jCheckBoxHasMetaAtributeItemStateChanged(java.awt.event.ItemEvent evt) {
        jCheckBoxHasMetaAtributeItemStateChanged();
    }

    private javax.swing.JCheckBox jCheckBoxHasMetaAtribute;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JPanel jPanel1;

    private javax.swing.JScrollPane jScrollPane1;

    private moduledefault.elicitedbases.postgresrafael.util.jtable.RafaelJTable rafaelJTable;
}
