package org.lcelb.opengl.renderers;

import org.lcelb.opengl.IModelViewModifier;

/**
 * @author gbrocard
 *
 * 7 d�c. 06
 */
public abstract class AbstractRenderer {

    public static final float SLOW_MOVE_STEP = 0.1f;

    public static final float NORMAL_MOVE_STEP = 1.0f;

    public static final float FAST_MOVE_STEP = 10.0f;

    /**
   * Model view notifier.
   */
    private IModelViewModifier _modifier;

    /**
   * Get model view modifier.
   * @param modifier_p
   * void
   */
    public void setModelViewModifier(IModelViewModifier modifier_p) {
        _modifier = modifier_p;
    }

    /**
   * Get model view modifier.
   * @return
   * IModelViewModifier
   */
    public IModelViewModifier getModelViewModifier() {
        return _modifier;
    }

    /**
   * Do something before model view is transformed.<br>
   * Default implementation does nothing.
   */
    public void beforeModelViewTransformation() {
    }

    /**
   * Render opengl part of the scene.
   * void
   */
    public abstract void render();

    /**
   * Get step value for a new movement, whatever the direction may be.
   * @return
   * float
   */
    public abstract float getMoveStep();

    /**
   * Do renderer unique initialisation.
   * void
   */
    public abstract void init();
}
