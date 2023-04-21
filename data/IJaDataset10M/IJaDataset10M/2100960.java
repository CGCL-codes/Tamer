package org.web3d.x3d.palette.items;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.text.JTextComponent;
import org.web3d.x3d.types.X3DNode;

/**
 * BaseContainerNodeCustomizerCustomizer.java
 * Created on Sep 12, 2007, 3:05 PM
 *
 * MOVES Institute
 * Naval Postgraduate School, Monterey, CA, USA
 * www.nps.edu
 *
 * @author Mike Bailey
 * @version $Id$
 */
public abstract class BaseContainerNodeCustomizer extends BaseCustomizer {

    private X3DNode node;

    private JTextComponent target;

    private DecimalFormat formatter = new DecimalFormat("#.######");

    public BaseContainerNodeCustomizer(X3DNode node, JTextComponent target) {
        super(node);
        this.node = node;
        this.target = target;
        initComponents();
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
    }

    private void initComponents() {
        org.web3d.x3d.palette.items.DEFUSEpanel dEFUSEpanel1 = getDEFUSEpanel();
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(dEFUSEpanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(dEFUSEpanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
    }

    @Override
    public void unloadInput() throws IllegalArgumentException {
        unLoadDEFUSE();
    }
}
