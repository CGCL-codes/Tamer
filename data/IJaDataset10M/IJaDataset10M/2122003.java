package mdes.slick.sui;

/**
 * A class that is used to create layerable window overlays.
 * <p>
 * A window is active when one of its children has the focus. A window that is layerable will
 * change its Z index when focused, bringing the window to the "front". The default layer is 
 * the layer at which the window should be at.
 * 
 * @author davedes
 */
public class Window extends Container {

    private boolean active = false;

    private boolean alwaysOnTop = false;

    public Window() {
        this(true);
    }

    protected Window(boolean updateAppearance) {
        super(false);
        setZIndex(getDefaultLayer());
        if (updateAppearance) updateAppearance();
    }

    public void updateAppearance() {
        setAppearance(Sui.getSkin().getWindowAppearance(this));
    }

    /**
     * Used by displays to activate a window. 
     */
    protected void setActive(boolean active) {
        this.active = active;
        int z = getDefaultLayer();
        if (active) z++;
        if (isAlwaysOnTop()) z += 2;
        setZIndex(z);
    }

    public boolean isActive() {
        return active;
    }

    public boolean isAlwaysOnTop() {
        return alwaysOnTop;
    }

    public void setAlwaysOnTop(boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
        if (alwaysOnTop) setZIndex(getDefaultLayer() + 2); else setZIndex(getDefaultLayer());
    }

    protected int getDefaultLayer() {
        return MODAL_LAYER;
    }
}
