package org.exist.xslt.expression;

import org.exist.interpreter.ContextAtExist;
import org.exist.xquery.AnalyzeContextInfo;
import org.exist.xquery.AnyNodeTest;
import org.exist.xquery.Constants;
import org.exist.xquery.Expression;
import org.exist.xquery.LocationStep;
import org.exist.xquery.PathExpr;
import org.exist.xquery.XPathException;
import org.exist.xquery.util.ExpressionDumper;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceIterator;
import org.exist.xquery.value.ValueSequence;
import org.exist.xslt.XSLContext;
import org.exist.xslt.pattern.Pattern;
import org.w3c.dom.Attr;

/**
 * <!-- Category: instruction -->
 * <xsl:for-each
 *   select = expression>
 *   <!-- Content: (xsl:sort*, sequence-constructor) -->
 * </xsl:for-each>
 * 
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public class ForEach extends SimpleConstructor {

    private String attr_select = null;

    private XSLPathExpr select = null;

    private PathExpr childNodes = null;

    private Sort sort = null;

    public ForEach(XSLContext context) {
        super(context);
        childNodes = new PathExpr(getContext());
        childNodes.add(new LocationStep(getContext(), Constants.CHILD_AXIS, new AnyNodeTest()));
    }

    public void setToDefaults() {
        attr_select = null;
        select = null;
        childNodes = null;
        sort = null;
    }

    public void prepareAttribute(ContextAtExist context, Attr attr) throws XPathException {
        String attr_name = attr.getNodeName();
        if (attr_name.equals(SELECT)) {
            attr_select = attr.getValue();
        }
    }

    public void analyze(AnalyzeContextInfo contextInfo) throws XPathException {
        boolean atRootCall = false;
        if (attr_select != null) {
            select = new XSLPathExpr(getXSLContext());
            Pattern.parse(contextInfo.getContext(), attr_select, select);
            if ((contextInfo.getFlags() & DOT_TEST) != 0) {
                atRootCall = true;
                _check_(select);
                contextInfo.removeFlag(DOT_TEST);
            }
        }
        for (Expression expr : steps) {
            if (expr instanceof Sort) {
                if (sort != null) throw new XPathException("double at sort");
                sort = (Sort) expr;
                steps.remove(expr);
            }
        }
        super.analyze(contextInfo);
        if (atRootCall) contextInfo.addFlag(DOT_TEST);
    }

    public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
        Sequence result = new ValueSequence();
        Sequence selected = select.eval(contextSequence, contextItem);
        if (sort != null) selected = sort.eval(selected, null);
        for (SequenceIterator iterInner = selected.iterate(); iterInner.hasNext(); ) {
            Item each = iterInner.nextItem();
            Sequence answer = super.eval(contextSequence, each);
            result.addAll(answer);
        }
        return result;
    }

    public void dump(ExpressionDumper dumper) {
        dumper.display("<xsl:for-each");
        if (select != null) {
            dumper.display(" select = ");
            select.dump(dumper);
        }
        dumper.display("> ");
        super.dump(dumper);
        dumper.display("</xsl:for-each>");
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("<xsl:for-each");
        if (select != null) result.append(" select = " + select.toString());
        result.append("> ");
        result.append(super.toString());
        result.append("</xsl:for-each>");
        return result.toString();
    }
}
