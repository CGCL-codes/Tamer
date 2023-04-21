package com.bluemarsh.jswat.ui.breakpoint;

import com.bluemarsh.jswat.core.breakpoint.Breakpoint;
import com.bluemarsh.jswat.core.breakpoint.BreakpointFactory;
import com.bluemarsh.jswat.core.breakpoint.ClassBreakpoint;
import com.bluemarsh.jswat.core.util.NameValuePair;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.DefaultComboBoxModel;
import org.openide.util.NbBundle;

/**
 * Class ClassPanel is the specific editor for a ClassBreakpoint.
 *
 * @author  Nathan Fiedler
 */
public class ClassPanel extends AbstractAdapter implements ItemListener {

    /** silence the compiler warnings */
    private static final long serialVersionUID = 1L;

    /** Type of class breakpoint. */
    private static enum Type {

        BOTH, PREPARE, UNLOAD
    }

    ;

    /** The stop-on combo model. */
    private DefaultComboBoxModel stopOnModel;

    /** Breakpoint to update. */
    private Breakpoint breakpoint;

    /**
     * Creates new form ClassPanel.
     */
    public ClassPanel() {
        initComponents();
        stopOnModel = new DefaultComboBoxModel();
        String label = NbBundle.getMessage(getClass(), "CTL_Class_StopOn_Prepare");
        NameValuePair<Type> pair = new NameValuePair<Type>(label, Type.PREPARE);
        stopOnModel.addElement(pair);
        label = NbBundle.getMessage(getClass(), "CTL_Class_StopOn_Unload");
        pair = new NameValuePair<Type>(label, Type.UNLOAD);
        stopOnModel.addElement(pair);
        label = NbBundle.getMessage(getClass(), "CTL_Class_StopOn_Both");
        pair = new NameValuePair<Type>(label, Type.BOTH);
        stopOnModel.addElement(pair);
        stopOnComboBox.setModel(stopOnModel);
    }

    @Override
    public boolean canCreateBreakpoint() {
        return true;
    }

    @Override
    public Breakpoint createBreakpoint(BreakpointFactory factory) {
        NameValuePair<?> pair = (NameValuePair<?>) stopOnComboBox.getSelectedItem();
        Type type = (Type) pair.getValue();
        boolean prepare = true;
        boolean unload = true;
        switch(type) {
            case PREPARE:
                unload = false;
                break;
            case UNLOAD:
                prepare = false;
                break;
        }
        return factory.createClassBreakpoint(null, prepare, unload);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String msg = validateInput();
            fireInputPropertyChange(msg);
            if (msg == null) {
                saveParameters(breakpoint);
            }
        }
    }

    @Override
    public void loadParameters(Breakpoint bp) {
        ClassBreakpoint cb = (ClassBreakpoint) bp;
        boolean prepare = cb.getStopOnPrepare();
        boolean unload = cb.getStopOnUnload();
        Type type;
        if (prepare && unload) {
            type = Type.BOTH;
        } else if (prepare) {
            type = Type.PREPARE;
        } else {
            type = Type.UNLOAD;
        }
        for (int ii = stopOnModel.getSize() - 1; ii >= 0; ii--) {
            NameValuePair<?> pair = (NameValuePair<?>) stopOnModel.getElementAt(ii);
            Type pt = (Type) pair.getValue();
            if (pt.equals(type)) {
                stopOnModel.setSelectedItem(pair);
            }
        }
        if (breakpoint == null) {
            stopOnComboBox.addItemListener(this);
        }
        breakpoint = bp;
    }

    @Override
    public void saveParameters(Breakpoint bp) {
        NameValuePair<?> pair = (NameValuePair<?>) stopOnComboBox.getSelectedItem();
        Type type = (Type) pair.getValue();
        boolean prepare = true;
        boolean unload = true;
        switch(type) {
            case PREPARE:
                unload = false;
                break;
            case UNLOAD:
                prepare = false;
                break;
        }
        ClassBreakpoint cb = (ClassBreakpoint) bp;
        cb.setStopOnPrepare(prepare);
        cb.setStopOnUnload(unload);
    }

    @Override
    public String validateInput() {
        return null;
    }

    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        useFilterLabel = new javax.swing.JLabel();
        stopOnLabel = new javax.swing.JLabel();
        stopOnComboBox = new javax.swing.JComboBox();
        setLayout(new java.awt.GridBagLayout());
        setBorder(javax.swing.BorderFactory.createTitledBorder(java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/ui/breakpoint/Form").getString("LBL_Class_Border")));
        useFilterLabel.setText(java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/ui/breakpoint/Form").getString("LBL_Class_UseFilter"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        add(useFilterLabel, gridBagConstraints);
        stopOnLabel.setLabelFor(stopOnComboBox);
        stopOnLabel.setText(java.util.ResourceBundle.getBundle("com/bluemarsh/jswat/ui/breakpoint/Form").getString("LBL_Class_StopOn"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 12);
        add(stopOnLabel, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 5);
        add(stopOnComboBox, gridBagConstraints);
    }

    private javax.swing.JComboBox stopOnComboBox;

    private javax.swing.JLabel stopOnLabel;

    private javax.swing.JLabel useFilterLabel;
}
