package net.sf.saxon.style;

import net.sf.saxon.expr.Expression;
import net.sf.saxon.instruct.Executable;
import net.sf.saxon.om.AttributeCollection;
import net.sf.saxon.trans.XPathException;

/**
* xsl:fallback element in stylesheet. <br>
*/
public class XSLFallback extends StyleElement {

    /**
    * Determine whether this node is an instruction.
    * @return true - it is an instruction
    */
    public boolean isInstruction() {
        return true;
    }

    /**
    * Determine whether this type of element is allowed to contain a template-body
    * @return true: yes, it may contain a template-body
    */
    public boolean mayContainSequenceConstructor() {
        return true;
    }

    public void prepareAttributes() throws XPathException {
        AttributeCollection atts = getAttributeList();
        for (int a = 0; a < atts.getLength(); a++) {
            int nc = atts.getNameCode(a);
            checkUnknownAttribute(nc);
        }
    }

    public void validate() throws XPathException {
    }

    public Expression compile(Executable exec) throws XPathException {
        return null;
    }
}
