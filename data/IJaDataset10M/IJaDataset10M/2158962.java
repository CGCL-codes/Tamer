package org.web3d.x3d.palette.items;

import org.openide.util.NbBundle;
import javax.swing.text.JTextComponent;
import org.openide.util.HelpCtx;
import static org.web3d.x3d.types.X3DSchemaData.*;

/**
 * HANIMDISPLACERCustomizer.java
 * Created on 2 May 2008
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey and Don Brutzman
 * @version $Id$
 */
public class HANIMDISPLACERCustomizer extends BaseCustomizer {

    private HANIMDISPLACER displacer;

    private JTextComponent target;

    /** Creates new form ARC2DCustomizer */
    public HANIMDISPLACERCustomizer(HANIMDISPLACER displacer, JTextComponent target) {
        super(displacer);
        this.displacer = displacer;
        this.target = target;
        HelpCtx.setHelpIDString(this, "HANIMDISPLACER_ELEM_HELPID");
        initComponents();
        nameTextField.setText(this.displacer.getName());
        coordIndexTF.setText(this.displacer.getCoordIndex());
        displacementsTF.setText(this.displacer.getDisplacements());
        weightTF.setText(this.displacer.getWeight());
        setDefaultDEFname();
    }

    private void setDefaultDEFname() {
        super.getDEFUSEpanel().setDefaultDEFname(nameTextField.getText() + NbBundle.getMessage(getClass(), getNameKey()));
    }

    private void initComponents() {
        nameTextField = new javax.swing.JTextField();
        coordIndexTF = new javax.swing.JTextField();
        displacementsTF = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpanel1 = getDEFUSEpanel();
        weightTF = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        nameTextField.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameTextFieldActionPerformed(evt);
            }
        });
        nameTextField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                nameTextFieldPropertyChange(evt);
            }
        });
        coordIndexTF.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                coordIndexTFActionPerformed(evt);
            }
        });
        jLabel1.setText("name");
        jLabel2.setText("coordIndex");
        jLabel3.setText("displacements");
        jLabel4.setText("weight");
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(dEFUSEpanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(jLabel3).addComponent(jLabel2).addComponent(jLabel1).addComponent(jLabel4)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(displacementsTF, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE).addComponent(weightTF, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE).addComponent(nameTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE).addComponent(coordIndexTF, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)))).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(dEFUSEpanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel1).addComponent(nameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(coordIndexTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(displacementsTF, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(weightTF, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4)).addGap(0, 0, Short.MAX_VALUE)));
        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] { coordIndexTF, displacementsTF, nameTextField });
    }

    private void nameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {
        setDefaultDEFname();
    }

    private void coordIndexTFActionPerformed(java.awt.event.ActionEvent evt) {
    }

    private void nameTextFieldPropertyChange(java.beans.PropertyChangeEvent evt) {
    }

    @Override
    public String getNameKey() {
        return "NAME_X3D_HANIMDISPLACER";
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
        displacer.setName(nameTextField.getText().trim());
        displacer.setCoordIndex(coordIndexTF.getText().trim());
        displacer.setDisplacements(displacementsTF.getText().trim());
        if (HANIMDISPLACER_ATTR_WEIGHT_REQD || !((weightTF.getText().trim().compareTo("0") == 0) || (weightTF.getText().trim().compareTo("0.0") == 0))) displacer.setWeight(weightTF.getText().trim());
    }

    private javax.swing.JTextField coordIndexTF;

    private javax.swing.JTextField displacementsTF;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JLabel jLabel2;

    private javax.swing.JLabel jLabel3;

    private javax.swing.JLabel jLabel4;

    private javax.swing.JTextField nameTextField;

    private javax.swing.JTextField weightTF;
}
