package com.jme.scene.state.lwjgl;

import org.lwjgl.opengl.GL11;
import com.jme.renderer.RenderContext;
import com.jme.scene.state.ShadeState;
import com.jme.scene.state.lwjgl.records.ShadeStateRecord;
import com.jme.system.DisplaySystem;

/**
 * <code>LWJGLShadeState</code> subclasses the ShadeState class using the
 * LWJGL API to access OpenGL to set the shade state.
 * 
 * @author Mark Powell
 * @author Joshua Slack - reworked for StateRecords.
 * @version $Id: LWJGLShadeState.java 4131 2009-03-19 20:15:28Z blaine.dev $
 */
public class LWJGLShadeState extends ShadeState {

    private static final long serialVersionUID = 1L;

    /**
     * Constructor instantiates a new <code>LWJGLShadeState</code> object.
     *  
     */
    public LWJGLShadeState() {
        super();
    }

    /**
     * <code>set</code> sets the OpenGL shade state to that specified by the
     * state.
     * 
     * @see com.jme.scene.state.ShadeState#apply() ()
     */
    public void apply() {
        RenderContext<?> context = DisplaySystem.getDisplaySystem().getCurrentContext();
        ShadeStateRecord record = (ShadeStateRecord) context.getStateRecord(StateType.Shade);
        context.currentStates[StateType.Shade.ordinal()] = this;
        int toApply = isEnabled() ? getGLShade() : GL11.GL_SMOOTH;
        if (!record.isValid() || toApply != record.lastShade) {
            GL11.glShadeModel(toApply);
            record.lastShade = toApply;
        }
        if (!record.isValid()) record.validate();
    }

    private int getGLShade() {
        switch(shadeMode) {
            case Flat:
                return GL11.GL_FLAT;
            case Smooth:
                return GL11.GL_SMOOTH;
        }
        throw new IllegalStateException("unknown shade mode: " + shadeMode);
    }

    @Override
    public ShadeStateRecord createStateRecord() {
        return new ShadeStateRecord();
    }
}
