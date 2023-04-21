package org.xith3d.ui.hud.base;

/**
 * A Widget implementing this interface can get a Border.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface BorderSettable {

    /**
     * Sets the Border to use for this BorderSettable Widget.
     * 
     * @param border the new Border (<i>null</i> for no border)
     */
    public void setBorder(Border border);

    /**
     * Creates a new Border from the given Border.Desctiption and invokes
     * setBorder(Border).
     * 
     * @see #setBorder(Border)
     * 
     * @param borderDesc the Border.Description to create the new Border from (<i>null</i> for no border)
     */
    public void setBorder(Border.Description borderDesc);

    /**
     * @return the Border used for this BorderSettable Widget
     */
    public Border getBorder();
}
