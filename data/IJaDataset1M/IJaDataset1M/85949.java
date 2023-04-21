package com.replica.replicaisland;

/** 
 * Implements rendering of a drawable object for a game object.  If a drawable is set on this
 * component it will be passed to the renderer and drawn on the screen every frame.  Drawable
 * objects may be set to be "camera-relative" (meaning their screen position is relative to the
 * location of the camera focus in the scene) or not (meaning their screen position is relative to
 * the origin at the lower-left corner of the display).
 */
public class RenderComponent extends GameComponent {

    private DrawableObject mDrawable;

    private int mPriority;

    private boolean mCameraRelative;

    private Vector2 mPositionWorkspace;

    private Vector2 mScreenLocation;

    private Vector2 mDrawOffset;

    public RenderComponent() {
        super();
        setPhase(ComponentPhases.DRAW.ordinal());
        mPositionWorkspace = new Vector2();
        mScreenLocation = new Vector2();
        mDrawOffset = new Vector2();
        reset();
    }

    @Override
    public void reset() {
        mPriority = 0;
        mCameraRelative = true;
        mDrawable = null;
        mDrawOffset.zero();
    }

    public void update(float timeDelta, BaseObject parent) {
        if (mDrawable != null) {
            RenderSystem system = sSystemRegistry.renderSystem;
            if (system != null) {
                mPositionWorkspace.set(((GameObject) parent).getPosition());
                mPositionWorkspace.add(mDrawOffset);
                if (mCameraRelative) {
                    CameraSystem camera = sSystemRegistry.cameraSystem;
                    ContextParameters params = sSystemRegistry.contextParameters;
                    mScreenLocation.x = (mPositionWorkspace.x - camera.getFocusPositionX() + (params.gameWidth / 2));
                    mScreenLocation.y = (mPositionWorkspace.y - camera.getFocusPositionY() + (params.gameHeight / 2));
                }
                if (mDrawable.visibleAtPosition(mScreenLocation)) {
                    system.scheduleForDraw(mDrawable, mPositionWorkspace, mPriority, mCameraRelative);
                } else if (mDrawable.getParentPool() != null) {
                    sSystemRegistry.drawableFactory.release(mDrawable);
                    mDrawable = null;
                }
            }
        }
    }

    public DrawableObject getDrawable() {
        return mDrawable;
    }

    public void setDrawable(DrawableObject drawable) {
        mDrawable = drawable;
    }

    public void setPriority(int priority) {
        mPriority = priority;
    }

    public int getPriority() {
        return mPriority;
    }

    public void setCameraRelative(boolean relative) {
        mCameraRelative = relative;
    }

    public void setDrawOffset(float x, float y) {
        mDrawOffset.set(x, y);
    }
}
