package org.akrogen.tkui.grammars.xul.layouts;

import org.akrogen.tkui.core.gui.layouts.IGuiLayoutHeightAware;
import org.akrogen.tkui.core.gui.layouts.IGuiLayoutWidthAware;

/**
 * XUL layout Data used to transform XUL layout (flex, ...) of XUL element
 * (textbox,...) into MigLayout String.
 * 
 * @author djo.mos
 * 
 */
public class BaseXULLayoutData implements IGuiLayoutWidthAware, IGuiLayoutHeightAware {

    public static final int NULL_INT_VALUE = -999;

    /**
	 * Orient constants : Used to specify whether the children of the element
	 * are oriented horizontally or vertically.
	 */
    public static final String ORIENT_HORIZONTAL = "horizontal";

    public static final String ORIENT_VERTICAL = "vertical";

    /**
	 * Commons constants for pack and align
	 */
    public static final String START = "start";

    public static final String END = "end";

    public static final String CENTER = "center";

    /**
	 * Pack constants
	 */
    public static final String PACK_START = START;

    public static final String PACK_END = END;

    public static final String PACK_CENTER = CENTER;

    /**
	 * Align constants
	 */
    public static final String ALIGN_START = START;

    public static final String ALIGN_END = END;

    public static final String ALIGN_CENTER = CENTER;

    public static final String ALIGN_BASELINE = "baseline";

    public static final String ALIGN_LEFT = "left";

    public static final String ALIGN_RIGHT = "right";

    /**
	 * flex
	 */
    protected int flex = 0;

    /**
	 * pack
	 */
    protected String pack = START;

    /**
	 * align
	 */
    protected String align = START;

    /**
	 * orient
	 */
    protected String orient = ORIENT_HORIZONTAL;

    /**
	 * width
	 */
    protected int width = NULL_INT_VALUE;

    protected int preferredWidth = NULL_INT_VALUE;

    protected String widthAsString = null;

    /**
	 * height
	 */
    protected int height = NULL_INT_VALUE;

    protected int preferredHeight = NULL_INT_VALUE;

    protected String heightAsString = null;

    protected int wrap = NULL_INT_VALUE;

    protected boolean newLine = false;

    /**
	 * Return flex. Indicates the flexibility of the element, which indicates
	 * how an element's container distributes remaining empty space among its
	 * children. Flexible elements grow and shrink to fit their given space.
	 * Elements with larger flex values will be made larger than elements with
	 * lower flex values, at the ratio determined by the two elements. The
	 * actual value is not relevant unless there are other flexible elements
	 * within the same container. Once the default sizes of elements in a box
	 * are calculated, the remaining space in the box is divided among the
	 * flexible elements, according to their flex ratios. Specifying a flex
	 * value of 0 has the same effect as leaving the flex attribute out
	 * entirely.
	 * 
	 * @return
	 */
    public int getFlex() {
        return flex;
    }

    /**
	 * Set flex. Indicates the flexibility of the element, which indicates how
	 * an element's container distributes remaining empty space among its
	 * children. Flexible elements grow and shrink to fit their given space.
	 * Elements with larger flex values will be made larger than elements with
	 * lower flex values, at the ratio determined by the two elements. The
	 * actual value is not relevant unless there are other flexible elements
	 * within the same container. Once the default sizes of elements in a box
	 * are calculated, the remaining space in the box is divided among the
	 * flexible elements, according to their flex ratios. Specifying a flex
	 * value of 0 has the same effect as leaving the flex attribute out
	 * entirely.
	 * 
	 * @param flex
	 */
    public void setFlex(int flex) {
        this.flex = flex;
    }

    /**
	 * Get pack : The pack attribute specifies where child elements of the box
	 * are placed when the box is larger that the size of the children. For
	 * boxes with horizontal orientation, it is used to indicate the position of
	 * children horizontally. For boxes with vertical orientation, it is used to
	 * indicate the position of children vertically. The align attribute is used
	 * to specify the position in the opposite direction. You can also specify
	 * the value of pack using the style property '-moz-box-pack'.
	 * 
	 * <ul>
	 * <li><b>start</b>: Child elements are placed starting from the left or
	 * top edge of the box. If the box is larger than the total size of the
	 * children, the extra space is placed on the right or bottom side. </li>
	 * <li><b>center</b>: Extra space is split equally along each side of the
	 * child elements, resulting the children being placed in the center of the
	 * box. </li>
	 * <li><b>end</b>: Child elements are placed on the right or bottom edge
	 * of the box. If the box is larger than the total size of the children, the
	 * extra space is placed on the left or top side.
	 * 
	 * @return
	 */
    public String getPack() {
        return pack;
    }

    /**
	 * Set pack : The pack attribute specifies where child elements of the box
	 * are placed when the box is larger that the size of the children. For
	 * boxes with horizontal orientation, it is used to indicate the position of
	 * children horizontally. For boxes with vertical orientation, it is used to
	 * indicate the position of children vertically. The align attribute is used
	 * to specify the position in the opposite direction. You can also specify
	 * the value of pack using the style property '-moz-box-pack'.
	 * 
	 * <ul>
	 * <li><b>start</b>: Child elements are placed starting from the left or
	 * top edge of the box. If the box is larger than the total size of the
	 * children, the extra space is placed on the right or bottom side. </li>
	 * <li><b>center</b>: Extra space is split equally along each side of the
	 * child elements, resulting the children being placed in the center of the
	 * box. </li>
	 * <li><b>end</b>: Child elements are placed on the right or bottom edge
	 * of the box. If the box is larger than the total size of the children, the
	 * extra space is placed on the left or top side.
	 * 
	 * @param pack
	 */
    public void setPack(String pack) {
        this.pack = pack;
    }

    /**
	 * Get align : The align attribute specifies how child elements of the box
	 * are aligned, when the size of the box is larger than the total size of
	 * the children. For boxes that have horizontal orientation, it specifies
	 * how its children will be aligned vertically. For boxes that have vertical
	 * orientation, it is used to specify how its children are algined
	 * horizontally. The pack attribute is related to the alignment but is used
	 * to specify the position in the opposite direction. You can also specify
	 * the value of align using the style property '-moz-box-align'.
	 * <ul>
	 * <li><b>start</b>: Child elements are aligned starting from the left or
	 * top edge of the box. If the box is larger than the total size of the
	 * children, the extra space is placed on the right or bottom side.</li>
	 * <li><b>center</b>: Extra space is split equally along each side of the
	 * child elements, resulting the children being placed in the center of the
	 * box.
	 * <li><b>end</b>: Child elements are placed on the right or bottom edge
	 * of the box. If the box is larger than the total size of the children, the
	 * extra space is placed on the left or top side.
	 * <li><b>baseline</b>: This value applies to horizontally oriented boxes
	 * only. It causes the child elements to be aligned so that their text
	 * labels are lined up.
	 * <li><b>stretch</b>: The child elements are stretched to fit the size of
	 * the box. For a horizontal box, the children are stretched to be the
	 * height of the box. For a vertical box, the children are stretched to be
	 * the width of the box. If the size of the box changes, the children
	 * stretch to fit. Use the flex attribute to create elements that stretch in
	 * the opposite direction.
	 * <li><b>left</b>: (Deprecated) The elements are aligned on their left
	 * edges.
	 * <li><b>center</b>: (Deprecated) The elements are centered horizontally.
	 * <li><b>right</b>: (Deprecated) The elements are aligned on their right
	 * edges.
	 * </ul>
	 * 
	 * @return
	 */
    public String getAlign() {
        return align;
    }

    /**
	 * Set align : The align attribute specifies how child elements of the box
	 * are aligned, when the size of the box is larger than the total size of
	 * the children. For boxes that have horizontal orientation, it specifies
	 * how its children will be aligned vertically. For boxes that have vertical
	 * orientation, it is used to specify how its children are algined
	 * horizontally. The pack attribute is related to the alignment but is used
	 * to specify the position in the opposite direction. You can also specify
	 * the value of align using the style property '-moz-box-align'.
	 * <ul>
	 * <li><b>start</b>: Child elements are aligned starting from the left or
	 * top edge of the box. If the box is larger than the total size of the
	 * children, the extra space is placed on the right or bottom side.</li>
	 * <li><b>center</b>: Extra space is split equally along each side of the
	 * child elements, resulting the children being placed in the center of the
	 * box.
	 * <li><b>end</b>: Child elements are placed on the right or bottom edge
	 * of the box. If the box is larger than the total size of the children, the
	 * extra space is placed on the left or top side.
	 * <li><b>baseline</b>: This value applies to horizontally oriented boxes
	 * only. It causes the child elements to be aligned so that their text
	 * labels are lined up.
	 * <li><b>stretch</b>: The child elements are stretched to fit the size of
	 * the box. For a horizontal box, the children are stretched to be the
	 * height of the box. For a vertical box, the children are stretched to be
	 * the width of the box. If the size of the box changes, the children
	 * stretch to fit. Use the flex attribute to create elements that stretch in
	 * the opposite direction.
	 * <li><b>left</b>: (Deprecated) The elements are aligned on their left
	 * edges.
	 * <li><b>center</b>: (Deprecated) The elements are centered horizontally.
	 * <li><b>right</b>: (Deprecated) The elements are aligned on their right
	 * edges.
	 * </ul>
	 * 
	 * @param align
	 */
    public void setAlign(String align) {
        if (ALIGN_LEFT.equals(align)) align = START; else if (ALIGN_RIGHT.equals(align)) align = END;
        this.align = align;
    }

    /**
	 * Get orient : Used to specify whether the children of the element are
	 * oriented horizontally or vertically. The default value depends on the
	 * element. You can also use the '-moz-box-orient' style property.
	 * <ul>
	 * <li>horizontal: Child elements of the element are placed next to each
	 * other in a row in the order that they appear in the XUL source. </li>
	 * <li>vertical: Child elements of the element are placed under each other
	 * in a column in the order that they appear in the XUL source. </li>
	 * </ul>
	 * 
	 * @return
	 */
    public String getOrient() {
        return orient;
    }

    /**
	 * Set orient : Used to specify whether the children of the element are
	 * oriented horizontally or vertically. The default value depends on the
	 * element. You can also use the '-moz-box-orient' style property.
	 * <ul>
	 * <li>horizontal: Child elements of the element are placed next to each
	 * other in a row in the order that they appear in the XUL source. </li>
	 * <li>vertical: Child elements of the element are placed under each other
	 * in a column in the order that they appear in the XUL source. </li>
	 * </ul>
	 * 
	 * @param orient
	 */
    public void setOrient(String orient) {
        this.orient = orient;
    }

    /**
	 * Get width : The width of the element in pixels. It is recommended that
	 * the CSS width property be used instead.
	 * 
	 * @return
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * Set width : The width of the element in pixels. It is recommended that
	 * the CSS width property be used instead.
	 * 
	 * @param width
	 */
    public void setWidth(int width) {
        this.width = width;
        this.widthAsString = null;
    }

    public void setPreferredWidth(int preferredWidth) {
        this.preferredWidth = preferredWidth;
    }

    public void setWidth(String width) {
        this.widthAsString = width;
        this.width = NULL_INT_VALUE;
    }

    /**
	 * Get height : The height of the element in pixels.
	 * 
	 * @return
	 */
    public int getHeight() {
        return height;
    }

    /**
	 * Set height : The height of the element in pixels.
	 * 
	 * @param height
	 */
    public void setHeight(int height) {
        this.height = height;
        this.heightAsString = null;
    }

    public void setPreferredHeight(int preferredHeight) {
        this.preferredHeight = preferredHeight;
    }

    public void setHeight(String height) {
        this.heightAsString = height;
        this.height = NULL_INT_VALUE;
    }

    protected String packOrAlign(String dirValue, String suffix) {
        StringBuffer res = new StringBuffer("align");
        res.append(suffix);
        res.append(" ");
        if (START.equals(dirValue)) res.append("0%"); else if (END.equals(dirValue)) res.append("100%"); else if (CENTER.equals(dirValue)) res.append("50%"); else if (ALIGN_BASELINE.equals(dirValue) && ("y".equals(suffix))) res.append("baseline"); else res.append("");
        return res.toString();
    }

    protected String getWidth(String width, String unit) {
        if (width == null) return null;
        StringBuffer res = new StringBuffer("");
        res.append(width);
        if (unit != null) res.append("px");
        return res.toString();
    }

    protected String getWidthConstraint() {
        if (width != NULL_INT_VALUE) {
            return getWidth("" + width, "px");
        }
        return getWidth(widthAsString, null);
    }

    protected String getHeight(String height, String unit) {
        if (height == null) return null;
        StringBuffer res = new StringBuffer("");
        res.append(height);
        if (unit != null) res.append("px");
        return res.toString();
    }

    protected String getHeightConstraint() {
        if (height != NULL_INT_VALUE) {
            return getHeight("" + height, "px");
        }
        return getHeight(heightAsString, null);
    }

    protected String getFlex(int flex) {
        StringBuffer res = new StringBuffer("growx");
        res.append((flex * 100));
        return res.toString();
    }

    public int getWrap() {
        return wrap;
    }

    public void setWrap(int wrap) {
        this.wrap = wrap;
    }

    public boolean isNewLine() {
        return newLine;
    }

    public void setNewLine(boolean newLine) {
        this.newLine = newLine;
    }

    public int getPreferredWidth() {
        return preferredWidth;
    }

    public int getPreferredHeight() {
        return preferredHeight;
    }
}
