package org.xith3d.ui.hud.base;

/**
 * A Widget implementing this interface can get a padding.
 * 
 * @author Marvin Froehlich (aka Qudus)
 */
public interface PaddingSettable {

    /**
     * Sets padding for this PaddingSettable Widget.<br>
     * Padding translates local coordinates and shrinks the clipping area.
     * 
     * @param paddingBottom
     * @param paddingRight
     * @param paddingTop
     * @param paddingLeft
     */
    public void setPadding(float paddingBottom, float paddingRight, float paddingTop, float paddingLeft);

    /**
     * Sets padding for this PaddingSettable Widget.<br>
     * Padding translates local coordinates and shrinks the clipping area.
     * 
     * @param padding padding for <i>bottom</i>, <i>right</i>, <i>top</i> and <i>left</i>
     */
    public void setPadding(float padding);

    /**
     * @return bottom-padding for this PaddingSettable Widget.<br>
     * Padding translates local coordinates and shrinks the clipping area.
     */
    public float getPaddingBottom();

    /**
     * @return right-padding for this PaddingSettable Widget.<br>
     * Padding translates local coordinates and shrinks the clipping area.
     */
    public float getPaddingRight();

    /**
     * @return top-padding for this PaddingSettable Widget.<br>
     * Padding translates local coordinates and shrinks the clipping area.
     */
    public float getPaddingTop();

    /**
     * @return left-padding for this PaddingSettable Widget.<br>
     * Padding translates local coordinates and shrinks the clipping area.
     */
    public float getPaddingLeft();
}
