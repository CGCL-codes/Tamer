package com.jme.scene.state.lwjgl;

import org.lwjgl.opengl.GL11;
import com.jme.renderer.RenderContext;
import com.jme.scene.state.DitherState;
import com.jme.scene.state.lwjgl.records.DitherStateRecord;
import com.jme.system.DisplaySystem;

/**
 * <code>LWJGLDitherState</code> subclasses the DitherState using the LWJGL
 * API to set the dithering state of OpenGL.
 * 
 * @author Mark Powell
 * @author Joshua Slack - reworked for StateRecords.
 * @version $Id: LWJGLDitherState.java,v 1.9 2007/04/11 18:27:36 nca Exp $
 */
public class LWJGLDitherState extends DitherState {

    private static final long serialVersionUID = 1L;

    /**
	 * <code>set</code> sets the dithering state on if it is enabled, and sets
	 * it off otherwise.
	 * 
	 * @see com.jme.scene.state.DitherState#apply() ()
	 */
    public void apply() {
        RenderContext context = DisplaySystem.getDisplaySystem().getCurrentContext();
        DitherStateRecord record = (DitherStateRecord) context.getStateRecord(RS_DITHER);
        context.currentStates[RS_DITHER] = this;
        if (record != null) {
            if (!record.isValid() || record.enabled != isEnabled()) {
                if (isEnabled()) {
                    GL11.glEnable(GL11.GL_DITHER);
                } else {
                    GL11.glDisable(GL11.GL_DITHER);
                }
                record.enabled = isEnabled();
            }
            if (!record.isValid()) record.validate();
        }
    }

    @Override
    public DitherStateRecord createStateRecord() {
        return new DitherStateRecord();
    }
}
