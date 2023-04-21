package org.exist.xslt.expression;

import org.exist.interpreter.ContextAtExist;
import org.exist.xquery.AnalyzeContextInfo;
import org.exist.xquery.XPathException;
import org.exist.xquery.util.ExpressionDumper;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xslt.XSLContext;
import org.exist.xslt.pattern.Pattern;
import org.w3c.dom.Attr;

/**
 * <!-- Category: instruction -->
 * <xsl:if
 *   test = expression>
 *   <!-- Content: sequence-constructor -->
 * </xsl:if>
 * 
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public class If extends SimpleConstructor {

    private String attr_test = null;

    private XSLPathExpr test = null;

    public If(XSLContext context) {
        super(context);
    }

    public void setToDefaults() {
        attr_test = null;
        test = null;
    }

    public void prepareAttribute(ContextAtExist context, Attr attr) throws XPathException {
        String attr_name = attr.getNodeName();
        if (attr_name.equals(TEST)) {
            attr_test = attr.getValue();
        }
    }

    public void analyze(AnalyzeContextInfo contextInfo) throws XPathException {
        super.analyze(contextInfo);
        if (attr_test == null) throw new XPathException("error, no test at xsl:if");
        test = new XSLPathExpr(getXSLContext());
        Pattern.parse(contextInfo.getContext(), attr_test, test);
    }

    public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
        if (test.eval(contextSequence, contextItem).effectiveBooleanValue()) return super.eval(contextSequence, contextItem);
        return Sequence.EMPTY_SEQUENCE;
    }

    public void dump(ExpressionDumper dumper) {
        dumper.display("<xsl:if");
        if (attr_test != null) dumper.display(" test = " + attr_test);
        dumper.display("> ");
        super.dump(dumper);
        dumper.display("</xsl:if>");
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("<xsl:if");
        if (attr_test != null) result.append(" test = " + attr_test.toString());
        result.append("> ");
        result.append(super.toString());
        result.append("</xsl:if>");
        return result.toString();
    }
}
