package com.jme3.gde.core.sceneexplorer.nodes.actions.impl;

import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.gde.core.sceneexplorer.nodes.actions.AbstractNewControlAction;
import com.jme3.gde.core.sceneexplorer.nodes.actions.NewControlAction;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.Control;

/**
 *
 * @author normenhansen
 */
@org.openide.util.lookup.ServiceProvider(service = NewControlAction.class)
public class NewRigidBodyAction extends AbstractNewControlAction {

    public NewRigidBodyAction() {
        name = "Static RigidBody";
    }

    @Override
    protected Control doCreateControl(Spatial spatial) {
        RigidBodyControl control = spatial.getControl(RigidBodyControl.class);
        if (control != null) {
            spatial.removeControl(control);
        }
        Node parent = spatial.getParent();
        spatial.removeFromParent();
        control = new RigidBodyControl(0);
        if (parent != null) {
            parent.attachChild(spatial);
        }
        return control;
    }
}
