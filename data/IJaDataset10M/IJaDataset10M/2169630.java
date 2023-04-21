package net.sf.saxon.style;

import net.sf.saxon.expr.Expression;
import net.sf.saxon.instruct.ApplyImports;
import net.sf.saxon.instruct.Executable;
import net.sf.saxon.om.AttributeCollection;
import net.sf.saxon.om.Axis;
import net.sf.saxon.om.AxisIterator;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.trans.XPathException;
import net.sf.saxon.type.Type;
import net.sf.saxon.value.Whitespace;

/**
* An xsl:apply-imports element in the stylesheet
*/
public class XSLApplyImports extends StyleElement {

    /**
    * Determine whether this node is an instruction.
    * @return true - it is an instruction
    */
    public boolean isInstruction() {
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
        AxisIterator kids = iterateAxis(Axis.CHILD);
        while (true) {
            NodeInfo child = (NodeInfo) kids.next();
            if (child == null) {
                break;
            }
            if (child instanceof XSLWithParam) {
            } else if (child.getNodeKind() == Type.TEXT) {
                if (!Whitespace.isWhite(child.getStringValueCS())) {
                    compileError("No character data is allowed within xsl:apply-imports", "XTSE0010");
                }
            } else {
                compileError("Child element " + child.getDisplayName() + " is not allowed within xsl:apply-imports", "XTSE0010");
            }
        }
    }

    public Expression compile(Executable exec) throws XPathException {
        ApplyImports inst = new ApplyImports(backwardsCompatibleModeIsEnabled());
        inst.setActualParameters(getWithParamInstructions(exec, false, inst), getWithParamInstructions(exec, true, inst));
        return inst;
    }
}
