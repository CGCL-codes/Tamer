package gui;

import common.Vec3f;
import renderer.Renderer;

/**
 * Basisklasse für eine Komponente des UI im OpenGL Fenster.
 *
 * @author timo
 *
 */
public abstract class CUIComponent extends C3DObject {

    /**
    * Größe der UI Komponente.
    */
    protected Vec3f mSize;

    /**
    * Farbe der UI Komponente.
    */
    protected float[] mColor = new float[4];

    /**
    * Gibt an, ob die Komponente gezeigt wird.
    */
    protected boolean isVisible;

    /**
    * Konstruktor. Initialisiert eine UI Komponente für den übergebenen
    * Renderer.
    *
    * @param r
    *           Der Renderer.
    */
    public CUIComponent(final Renderer r) {
        super(r);
        setColor(1f, 1f, 1f, 1f);
    }

    /**
    * Stellt die Größe der Komponente ein.
    *
    * @param size
    *           Neue Größe der Komponente.
    */
    public void setSize(final Vec3f size) {
        mSize = size;
    }

    /**
    * Gibt die Größe der Komponente zurück.
    *
    * @return Größe der Komponente.
    */
    public final Vec3f getSize() {
        return mSize;
    }

    /**
    * Legt fest, ob die Komponente sichtbar (true) oder unsichtbar (false) ist.
    *
    * @param b
    *           Falls true ist die Komponente sichtbar, wenn false, dann nicht.
    */
    public void setVisible(final boolean b) {
        isVisible = b;
    }

    /**
    * Gibt zurück, ob die Komponente gerade sichtbar ist oder nicht.
    *
    * @return true wenn sichtbar, sonst false.
    */
    public boolean isVisible() {
        return isVisible;
    }

    /**
    * Stellt die Farbe der Komponente auf den RGB Wert.
    *
    * @param r
    *           Rot-Anteil der Farbe
    * @param g
    *           Grün-Anteil der Farbe
    * @param b
    *           Blau-Anteil der Farbe
    */
    public final void setColor(final byte r, final byte g, final byte b) {
        setColor((float) r / 256.0f, g / 256.0f, b / 256.0f);
    }

    /**
    * Stellt die Farbe der Komponente auf den RGBA Wert.
    *
    * @param r
    *           Rot-Anteil der Farbe
    * @param g
    *           Grün-Anteil der Farbe
    * @param b
    *           Blau-Anteil der Farbe.
    * @param alpha
    *           Alphawert.
    */
    public final void setColor(final byte r, final byte g, final byte b, final byte alpha) {
        setColor(r / 256f, g / 256f, b / 256, alpha / 256);
    }

    /**
    * Stellt die Farbe der Komponente auf den RGB Wert.
    *
    * @param r
    *           Rot-Anteil
    * @param g
    *           Grün-Anteil
    * @param b
    *           Blau-Anteil
    */
    public final void setColor(final float r, final float g, final float b) {
        mColor[0] = r;
        mColor[1] = g;
        mColor[2] = b;
        mColor[3] = 1.0f;
    }

    /**
    * Stellt die Farbe der Komponente auf den RGBA Wert.
    *
    * @param r
    *           Rot-Anteil
    * @param g
    *           Grün-Anteil
    * @param b
    *           Blau-Anteil
    * @param alpha
    *           Alpha-Wert
    */
    public final void setColor(final float r, final float g, final float b, final float alpha) {
        setColor(r, g, b);
        mColor[3] = alpha;
    }

    /**
    * Wird aufgerufen, wenn eine Taste gedrückt wurde.
    *
    * @param c
    *           Character der Taste, die gedrückt wurde.
    * @return True wenn angenommen, sonst false.
    */
    public abstract boolean onKeyDown(char c);

    /**
    * Wird aufgerufen, wenn die Komponente ausgewählt wurde.
    *
    * @return True wenn angenommen, sonst false.
    */
    public abstract boolean onFocus();

    /**
    * Wird aufgerufen, wenn die Komponente nicht mehr ausgewählt ist.
    *
    * @return True wenn angenommen, sonst false.
    */
    public abstract boolean onFocusLost();

    /**
    * Gibt an, ob die Komponente ausgewählt werden kann oder nicht.
    *
    * @return True wenn Komponente ausgewählt werden kannst, sonst false.
    */
    public abstract boolean isSelectable();
}
