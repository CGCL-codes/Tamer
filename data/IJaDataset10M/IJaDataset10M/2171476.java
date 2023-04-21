package org.jcrpg.threed.scene.model.effect;

import org.jcrpg.apps.Jcrpg;
import org.jcrpg.threed.engine.program.EffectNode;
import org.jcrpg.threed.scene.model.SimpleModel;

public class EffectProgram {

    Class<? extends EffectNode> visualForm;

    public SimpleModel model = null;

    public static int PT_MOVE_FROM_SOURCE_TO_TARGET = 0;

    public int programType = PT_MOVE_FROM_SOURCE_TO_TARGET;

    public EffectProgram(Class<? extends EffectNode> visualForm) {
        this(visualForm, null);
    }

    public EffectProgram(Class<? extends EffectNode> visualForm, SimpleModel model) {
        this.visualForm = visualForm;
        this.model = model;
    }

    public EffectNode get3DVisualization() {
        try {
            EffectNode n = visualForm.newInstance();
            if (model != null) {
                n.addModelObject(model);
            }
            return n;
        } catch (Exception ex) {
            Jcrpg.LOGGER.warning(ex.toString());
            ex.printStackTrace();
            return null;
        }
    }
}
