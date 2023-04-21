package org.vrforcad.controller.gui.dialog.lights;

import java.awt.Dimension;
import javax.swing.Box;
import javax.swing.JPanel;
import javax.vecmath.Color3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;
import org.vrforcad.controller.gui.dialog.OneVal;
import org.vrforcad.controller.gui.dialog.ThreeVals;
import org.vrforcad.lib.lights.GenericLight;

/**
 * This class adjust the directional light attributes.
 *  
 * @version 1.0 
 * @author Daniel Cioi <dan.cioi@vrforcad.org>
 */
public class DirectionalLightAdjust extends AdjustLightColor {

    private static final long serialVersionUID = 1L;

    private GenericLight light;

    private Tuple3f color;

    private Tuple3f direction;

    /**
	 * Default constrcutor
	 * @param light
	 */
    public DirectionalLightAdjust(GenericLight light) {
        super("Directional Light");
        this.light = light;
        initialize();
    }

    @Override
    protected void addMoreThreeVals(JPanel vPanel) {
        vPanel.add(new ThreeVals(this, 1, "X", "Y", "Z", OneVal.TYPE_DISTANCE_METERS));
        vPanel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    @Override
    public void setTuple(int index, Tuple3f tuple) {
        if (index == 0) {
            color = new Color3f(tuple);
        } else {
            direction = new Vector3f(tuple);
        }
        Tuple3f[] tuples = new Tuple3f[2];
        tuples[0] = color;
        tuples[1] = direction;
        light.setTuple(tuples);
    }

    @Override
    public Tuple3f getTuple(int index) {
        if (index == 0) {
            color = light.getTuple()[0];
            return color;
        } else {
            direction = light.getTuple()[1];
            return direction;
        }
    }

    @Override
    protected void enableDisable(boolean ed) {
        light.setEnable(ed);
    }
}
