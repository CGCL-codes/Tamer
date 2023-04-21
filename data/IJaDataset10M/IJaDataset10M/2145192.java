package net.sf.hippopotam.presentation.field;

import java.awt.GridBagConstraints;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import net.sf.hippopotam.presentation.panel.SizeableScrollPane;

/**
 *
 */
public class MultiLineLookupField extends LookupField {

    public MultiLineLookupField() {
    }

    public MultiLineLookupField(LoookupFieldSelector selector, boolean allowNull) {
        super(selector, allowNull);
    }

    protected void addPart(StringBuffer sb, String name, boolean b) {
        if (!b) {
            sb.append("\n");
        }
        sb.append(name);
    }

    private JTextArea mJta;

    protected JTextComponent getTextComponent() {
        if (mJta == null) {
            mJta = new JTextArea();
            mJta.setOpaque(false);
        }
        return mJta;
    }

    protected void installTextComponent(GridBagConstraints constraints) {
        constraints.weighty = 1;
        constraints.fill = GridBagConstraints.BOTH;
        SizeableScrollPane sizeableScrollPane = new SizeableScrollPane(getTextComponent());
        sizeableScrollPane.setFixedHeight(52);
        add(sizeableScrollPane, constraints);
        getTextComponent().setFocusable(false);
    }
}
