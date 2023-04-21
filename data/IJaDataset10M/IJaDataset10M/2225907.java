package de.flaxen.jdvdslideshow.ui;

import de.flaxen.jdvdslideshow.effects.Scroll;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author  develop
 */
public class ScrollJPanel extends EffectJPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6795580115230387686L;

    public void accepting() {
        super.accepting();
        Scroll scroll = (Scroll) this.effect;
        scroll.setDirection((Scroll.Direction) this.directionJCombobox1.getSelectedItem());
    }

    /** Creates new form ScrollJPanel */
    public ScrollJPanel() {
        initComponents();
    }

    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        BorderLayout layout = new BorderLayout();
        this.setLayout(layout);
        this.add(jPanel1, BorderLayout.WEST);
        directionJCombobox1 = new de.flaxen.jdvdslideshow.ui.DirectionJCombobox();
        directionJCombobox1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                directionJCombobox1ActionPerformed(evt);
            }
        });
        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(Translator.languageBundle.getString("direction")));
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addComponent(directionJCombobox1, javax.swing.GroupLayout.DEFAULT_SIZE, 342, Short.MAX_VALUE).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(directionJCombobox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    private de.flaxen.jdvdslideshow.ui.DirectionJCombobox directionJCombobox1;

    private javax.swing.JPanel jPanel1;

    @Override
    protected void setEffectToControl() {
        Scroll scroll = (Scroll) this.effect;
        this.directionJCombobox1.setSelectedItem(scroll.getDirection());
    }

    private void directionJCombobox1ActionPerformed(ActionEvent evt) {
        this.accept();
    }
}
