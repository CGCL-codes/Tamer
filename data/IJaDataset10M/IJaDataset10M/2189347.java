package net.sf.saxon.style;

import net.sf.saxon.expr.Expression;
import net.sf.saxon.expr.Literal;
import net.sf.saxon.instruct.Executable;
import net.sf.saxon.instruct.ValueOf;
import net.sf.saxon.om.*;
import net.sf.saxon.pattern.NodeKindTest;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.ItemType;
import net.sf.saxon.value.StringValue;
import net.sf.saxon.value.Whitespace;

/**
* Handler for xsl:text elements in stylesheet. <BR>
*/
public class XSLText extends XSLStringConstructor {

    private boolean disable = false;

    private StringValue value;

    /**
     * Determine the type of item returned by this instruction (only relevant if
     * it is an instruction).
     * @return the item type returned
     */
    protected ItemType getReturnedItemType() {
        return NodeKindTest.TEXT;
    }

    public void prepareAttributes() throws XPathException {
        String disableAtt = null;
        AttributeCollection atts = getAttributeList();
        for (int a = 0; a < atts.getLength(); a++) {
            int nc = atts.getNameCode(a);
            String f = getNamePool().getClarkName(nc);
            if (f.equals(StandardNames.DISABLE_OUTPUT_ESCAPING)) {
                disableAtt = Whitespace.trim(atts.getValue(a));
            } else {
                checkUnknownAttribute(nc);
            }
        }
        if (disableAtt != null) {
            if (disableAtt.equals("yes")) {
                disable = true;
            } else if (disableAtt.equals("no")) {
                disable = false;
            } else {
                compileError("disable-output-escaping attribute must be either 'yes' or 'no'", "XTSE0020");
            }
        }
    }

    public void validate() throws XPathException {
        AxisIterator kids = iterateAxis(Axis.CHILD);
        value = StringValue.EMPTY_STRING;
        while (true) {
            Item child = kids.next();
            if (child == null) {
                break;
            } else if (child instanceof StyleElement) {
                ((StyleElement) child).compileError("xsl:text must not contain child elements", "XTSE0010");
                return;
            } else {
                value = StringValue.makeStringValue(child.getStringValueCS());
            }
        }
        super.validate();
    }

    /**
     * Get the error code to be returned when the element has a select attribute but is not empty.
     * @return the error code defined for this condition, for this particular instruction
     */
    protected String getErrorCodeForSelectPlusContent() {
        return null;
    }

    public Expression compile(Executable exec) throws XPathException {
        return new ValueOf(Literal.makeLiteral(value), disable, false);
    }
}
