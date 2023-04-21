package org.akrogen.tkui.dom.xul.dom.simples;

import org.akrogen.tkui.dom.xul.dom.XULControlElement;

/**
 * XUL scale interface. A scale (sometimes referred to as a "slider") allows the
 * user to select a value from a range. A bar displayed either horizontally or
 * vertically allows the user to select a value by dragging a thumb on the bar.
 * 
 * Use the orient attribute to specify the orientation of the scale. The default
 * value is horizontal which displays a horizontal scale. Lower values are to
 * the left and higher values are to the right. Set the orient attribute to
 * vertical to use a vertical scale.
 * 
 * The user may use the arrow keys to increment and decrement the value by one
 * unit, or the page up and page down keys to increment and decrement the value
 * by one page, as specified by the pageincrement attribute. The home and end
 * keys set the scale's value to the minimum and maximum values, respectively. A
 * scale will fire a change event whenever the scale's value is modified.
 * 
 * @version 1.0.0
 * @author <a href="mailto:angelo.zerr@gmail.com">Angelo ZERR</a>
 * 
 * @see http://developer.mozilla.org/en/docs/XUL:scale
 */
public interface Scale extends XULControlElement {

    public static final String INCREMENT_ATTR = "increment";

    public static final String MAX_ATTR = "max";

    public static final String MIN_ATTR = "min";

    public static final String PAGEINCREMENT_ATTR = "pageincrement";

    public static final String VALUE_ATTR = "value";

    /**
	 * Gets the value of the increment attribute. The amount by which the curpos
	 * (for scroll bars) or value (for number boxes) attribute changes by when
	 * the arrows are clicked. The default value is 1.
	 * 
	 * @return
	 */
    public int getIncrement();

    /**
	 * Sets the value of the increment attribute. The amount by which the curpos
	 * (for scroll bars) or value (for number boxes) attribute changes by when
	 * the arrows are clicked. The default value is 1.
	 * 
	 * @param increment
	 */
    public void setIncrement(int increment);

    /**
	 * Gets the value of the max attribute. The maximum value that the scale may
	 * be set to. The default value is 100.
	 * 
	 * @return
	 */
    public int getMax();

    /**
	 * Sets the value of the min attribute. The maximum value that the scale may
	 * be set to. The default value is 100.
	 * 
	 * @param max
	 */
    public void setMax(int max);

    /**
	 * Gets the value of the min attribute. The minimum value that the scale may
	 * be set to. The default value is 0.
	 * 
	 * @return
	 */
    public int getMin();

    /**
	 * Sets the value of the min attribute. The minimum value that the scale may
	 * be set to. The default value is 0.
	 * 
	 * @param min
	 */
    public void setMin(int min);

    /**
	 * Gets the value of the pageincrement attribute. The amount by which the
	 * value of the value attribute changes by when the PgUp or PdDn keys are
	 * pressed. The default value is 10.
	 * 
	 * @return
	 */
    public int getPageincrement();

    /**
	 * Sets the value of the pageincrement attribute. The amount by which the
	 * value of the value attribute changes by when the PgUp or PdDn keys are
	 * pressed. The default value is 10.
	 * 
	 * @param pageincrement
	 */
    public void setPageincrement(int pageincrement);

    /**
	 * Gets the value of the value attribute. The string attribute allows you to
	 * associate a data value with an element. It is not used for any specific
	 * purpose, but you can access it with a script for your own use.
	 * 
	 * @return
	 */
    public String getValue();

    /**
	 * Sets the value of the value attribute. The string attribute allows you to
	 * associate a data value with an element. It is not used for any specific
	 * purpose, but you can access it with a script for your own use.
	 * 
	 * @param value
	 */
    public void setValue(String value);
}
