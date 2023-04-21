package org.wilmascope.forcelayout;

import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import org.wilmascope.control.GraphControl;
import org.wilmascope.control.WilmaMain;
import org.wilmascope.forcelayout.ForceManager.UnknownForceTypeException;
import org.wilmascope.gui.SpinnerSlider;

/**
 *
 * @author  administrator
 * @version
 */
public class ForceControlPanel extends javax.swing.JPanel {

    /**
	 * 
	 */
    private static final long serialVersionUID = -3117841329252532238L;

    private Force force;

    private GraphControl.Cluster cluster;

    private ForceLayout forceLayout;

    JCheckBox enabledCheckBox = new JCheckBox();

    SpinnerSlider forceSlider;

    public ForceControlPanel(GraphControl.Cluster cluster, Force force) {
        this.cluster = cluster;
        this.force = force;
        this.forceLayout = (ForceLayout) cluster.getLayoutEngine();
        this.add(enabledCheckBox, null);
        forceSlider = new SpinnerSlider(force.getTypeName(), 0, 100, (int) ((float) force.getStrengthConstant() * 10f));
        forceSlider.addChangeListener(new javax.swing.event.ChangeListener() {

            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                forceSliderStateChanged();
            }
        });
        this.add(forceSlider, null);
        Force existingForce;
        if ((existingForce = forceLayout.getForce(force.getTypeName())) != null) {
            enabledCheckBox.setSelected(true);
            this.force = existingForce;
        } else {
            try {
                this.force = ForceManager.getInstance().createForce(force.getTypeName());
            } catch (UnknownForceTypeException e1) {
                WilmaMain.showErrorDialog("Unknown Force Type!", e1);
            }
            forceSlider.setEnabled(false);
        }
        enabledCheckBox.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                enabledCheckBox_actionPerformed(e);
            }
        });
    }

    void forceSliderStateChanged() {
        force.setStrengthConstant(forceSlider.getValue() / 10f);
        cluster.unfreeze();
    }

    void enabledCheckBox_actionPerformed(ActionEvent e) {
        if (forceSlider.isEnabled()) {
            forceSlider.setEnabled(false);
            forceLayout.removeForce(force);
        } else {
            forceSlider.setEnabled(true);
            forceLayout.addForce(force);
        }
        cluster.unfreeze();
    }
}
