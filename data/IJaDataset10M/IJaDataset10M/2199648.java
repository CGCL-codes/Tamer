package org.xmlcml.jumbo3;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import uk.co.demon.ursus.dom.PMRDelegate;
import org.xmlcml.cml.CMLException;
import org.xmlcml.cml.CMLUnits;

/** holds an integer with max and minimum values */
public class IntegerValImpl extends NumericValImpl implements org.xmlcml.cml.CMLIntegerVal {

    protected int maximum = Integer.MAX_VALUE;

    protected int minimum = Integer.MIN_VALUE;

    protected int intValue;

    protected String getClassTagName() {
        return ELEMENT_NAMES[INTEGER];
    }

    public IntegerValImpl() {
        super();
    }

    public IntegerValImpl(Element element) {
        super(element);
    }

    /** new CMLIntegerVal in document context */
    public IntegerValImpl(Document document) {
        super(ELEMENT_NAMES[INTEGER], document);
    }

    /** new object in document context */
    public IntegerValImpl(String tagName, Document document) {
        super(tagName, document);
    }

    public IntegerValImpl(Document document, int intValue, String builtin, String title, String id, String dictRef, String conventionName, CMLUnits units, int minimum, int maximum) {
        super(ELEMENT_NAMES[INTEGER], document, "" + intValue, builtin, title, id, dictRef, conventionName, units);
        this.setIntValue(intValue);
        this.setMinimum(minimum);
        this.setMaximum(maximum);
    }

    ;

    /** new CMLIntegerVal in document context; sets int value and builtin (if not null) */
    public IntegerValImpl(Document document, int intValue, String builtin) {
        this(document);
        this.setIntValue(intValue);
        if (builtin != null && !(builtin.equals(""))) {
            this.setBuiltin(builtin);
        }
    }

    protected void init() {
        setDataType(INTEGER_TYPE);
        this.setTagName(ELEMENT_NAMES[INTEGER]);
    }

    public int getIntValue() {
        return this.intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
        this.setStringValue("" + intValue);
    }

    /** set maximum value; must be greater than minimum */
    public void setMaximum(int maximum) {
        if (minimum < maximum) this.maximum = maximum;
    }

    /** maximum value, set by default to Integer.MAX_VALUE (2147483647) */
    public int getMaximum() {
        String maxVal = this.getAttribute(MAX);
        if (!PMRDelegate.isEmptyAttribute(maxVal)) {
            maximum = Integer.parseInt(maxVal);
        }
        return maximum;
    }

    /** set minimum value; must be less than maximum */
    public void setMinimum(int minimum) {
        if (minimum < maximum) this.minimum = minimum;
    }

    /** minimum value, set by default to Integer.MIN_VALUE (-2147483648) */
    public int getMinimum() {
        String minVal = this.getAttribute(MIN);
        if (!PMRDelegate.isEmptyAttribute(minVal)) {
            minimum = Integer.parseInt(minVal);
        }
        return minimum;
    }

    public boolean processDOM() throws CMLException {
        if (!domNeedsProcessing) return true;
        ;
        this.processString();
        try {
            intValue = Integer.parseInt(stringValue);
        } catch (NumberFormatException nfe) {
            throw new CMLException("Bad Integer: " + stringValue);
        }
        domNeedsProcessing = false;
        return true;
    }
}
