package homura.hde.impl.lwjgl.gfx.state;

import homura.hde.app.HDEView;
import homura.hde.core.renderer.RenderContext;
import homura.hde.core.scene.state.DitherState;
import homura.hde.core.scene.state.record.DitherStateRecord;
import org.lwjgl.opengl.GL11;

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
	 * @see homura.hde.core.scene.state.DitherState#apply() ()
	 */
    public void apply() {
        RenderContext context = HDEView.getDisplaySystem().getCurrentContext();
        DitherStateRecord record = (DitherStateRecord) context.getStateRecord(RS_DITHER);
        context.currentStates[RS_DITHER] = this;
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

    @Override
    public DitherStateRecord createStateRecord() {
        return new DitherStateRecord();
    }
}
