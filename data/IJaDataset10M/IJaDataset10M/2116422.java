package org.jxul;

/**
 * @author Will Etson
 */
public interface XulProgressMeter extends XulElement {

    String ELEMENT = "progressmeter";

    /**
	 * A determined progressmeter is used in cases where you know how long an operation will take. Undeterminate progressmeters can be used when you don't and will typically be drawn as a spinning barber pole.
	 * <ul>
	 * <li>determined: The progress meter uses its value attribute to determine the amount of the bar that is filled in.</li>
	 * <li>undetermined: The progressmeter is undeterminate. </li>
	 * </ul>
	 */
    String ATTR_MODE = "mode";

    String ATTR_MODE_DETERMINED = "determined";

    String ATTR_MODE_UNDETERMINED = "undetermined";

    String getMode();

    void setMode(String mode);

    /**
	 * A percentage value that specifies the amount of the meter that is filled in. Because it is a percentage, it ranges from 0 to 100. 
	 */
    String ATTR_VALUE = "value";

    int getValue();

    void setValue(int value);

    String ATTR_LABEL = "label";

    String getLabel();

    void setLabel(String label);

    String ATTR_ONCLICK = "onclick";

    String getOnClick();

    void setOnClick(String onclick);
}
