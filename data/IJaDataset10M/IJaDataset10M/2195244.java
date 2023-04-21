package net.sf.saxon.style;

import net.sf.saxon.expr.Expression;
import net.sf.saxon.instruct.Executable;
import net.sf.saxon.type.ItemType;
import net.sf.saxon.om.AttributeCollection;
import net.sf.saxon.trans.XPathException;

/**
* Handler for xsl:matching-substring and xsl:non-matching-substring elements in stylesheet.
* New at XSLT 2.0<BR>
*/
public class XSLMatchingSubstring extends StyleElement {

    /**
     * Determine the type of item returned by this instruction (only relevant if
     * it is an instruction).
     * @return the item type returned
     */
    protected ItemType getReturnedItemType() {
        return getCommonChildItemType();
    }

    public void prepareAttributes() throws XPathException {
        AttributeCollection atts = getAttributeList();
        for (int a = 0; a < atts.getLength(); a++) {
            int nc = atts.getNameCode(a);
            checkUnknownAttribute(nc);
        }
    }

    /**
    * Determine whether this type of element is allowed to contain a template-body
    * @return true: yes, it may contain a template-body
    */
    public boolean mayContainSequenceConstructor() {
        return true;
    }

    public void validate() throws XPathException {
        if (!(getParent() instanceof XSLAnalyzeString)) {
            compileError(getDisplayName() + " must be immediately within xsl:analyze-string", "XTSE0010");
        }
    }

    public Expression compile(Executable exec) throws XPathException {
        throw new UnsupportedOperationException("XSLMatchingSubstring#compile() should not be called");
    }
}
