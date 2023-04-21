package org.netbeans.beaninfo.editors;

import java.awt.Rectangle;

/** A property editor for Rectangle class.
* @author   Petr Hamernik
*/
public class RectangleEditor extends ArrayOfIntSupport {

    public RectangleEditor() {
        super("java.awt.Rectangle", 4);
    }

    /** Abstract method for translating the value from getValue() method to array of int. */
    int[] getValues() {
        Rectangle rect = (Rectangle) getValue();
        return new int[] { rect.x, rect.y, rect.width, rect.height };
    }

    /** Abstract method for translating the array of int to value
    * which is set to method setValue(XXX)
    */
    void setValues(int[] val) {
        if ((val[0] < 0) || (val[1] < 0) || (val[2] < 0) || (val[3] < 0)) {
            String msg = NbBundle.getMessage(DimensionEditor.class, "CTL_NegativeSize");
            IllegalArgumentException iae = new IllegalArgumentException("Negative value");
            throw iae;
        } else setValue(new Rectangle(val[0], val[1], val[2], val[3]));
    }

    public boolean supportsCustomEditor() {
        return true;
    }

    public java.awt.Component getCustomEditor() {
        return new RectangleCustomEditor(this);
    }

    /** @return the format of value set in property editor. */
    String getHintFormat() {
        return NbBundle.getMessage(RectangleEditor.class, "CTL_HintFormatRE");
    }

    /** Provides name of XML tag to use for XML persistence of the property value */
    protected String getXMLValueTag() {
        return "Rectangle";
    }
}
