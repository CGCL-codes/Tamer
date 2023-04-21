package net.sf.xqparser.util;

import java.io.StringWriter;
import java.io.Writer;
import net.sf.xqparser.DefaultXPathVisitor;
import net.sf.xqparser.SimpleNode;
import net.sf.xqparser.XPathTreeConstants;

/**
 * Transform a SimpleNode tree into xquery syntax.
 * 
 * @author Damien Abos
 */
class XQParserWriter extends DefaultXPathVisitor<Writer> {

    protected static XQParserWriter instance = new XQParserWriter();

    public static String toString(SimpleNode simpleNode) {
        Writer writer = new StringWriter();
        String toString = null;
        try {
            toString = instance.visit(simpleNode, writer).toString();
        } catch (Exception e) {
        }
        return toString;
    }

    public static void write(SimpleNode simpleNode, Writer writer) throws Exception {
        instance.visit(simpleNode, writer);
    }

    protected XQParserWriter() {
    }

    protected Writer caseAbbrevForwardStep(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetValue() != null) {
            writer.append("@");
        }
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseAbbrevReverseStep(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("..");
        return writer;
    }

    protected Writer caseAdditiveExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(' ');
            writer.append(simpleNode.jjtGetValue().toString());
            writer.append(' ');
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseAndExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" and ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseAnyKindTest(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("node()");
        return writer;
    }

    protected Writer caseAposAttrContentChar(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseAttribNameOrWildcard(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetNumChildren() > 0) {
            simpleNode.childrenAccept(this, writer);
        } else {
            writer.append(simpleNode.jjtGetValue().toString());
        }
        return writer;
    }

    protected Writer caseAttributeTest(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("attribute(");
        if (simpleNode.jjtGetNumChildren() > 0) {
            SimpleNode attrNameNode = (SimpleNode) simpleNode.jjtGetChild(0);
            attrNameNode.jjtAccept(this, writer);
            if (simpleNode.jjtGetNumChildren() > 1) {
                writer.append(',');
                SimpleNode typeNameNode = (SimpleNode) simpleNode.jjtGetChild(1);
                typeNameNode.jjtAccept(this, writer);
            }
        }
        writer.append(")");
        return writer;
    }

    protected Writer caseBaseURIDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare base-uri ");
        SimpleNode uriNode = (SimpleNode) simpleNode.jjtGetChild(0);
        uriNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseBoundarySpaceDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare boundary-space ");
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseCaseClause(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("case ");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        if (getSimpleNodeId(childNode) == XPathTreeConstants.JJTVARNAME) {
            childNode.jjtAccept(this, writer);
            writer.append(" as ");
            index++;
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        }
        childNode.jjtAccept(this, writer);
        index++;
        writer.append(" return ");
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        writer.append(' ');
        return writer;
    }

    protected Writer caseCastableExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        if (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" castable as ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
        }
        return writer;
    }

    protected Writer caseCastExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        if (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" cast as ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
        }
        return writer;
    }

    protected Writer caseCDataSection(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("<![CDATA[");
        simpleNode.childrenAccept(this, writer);
        writer.append("]]>");
        return writer;
    }

    protected Writer caseCDataSectionChar(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseCharRef(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseCloseApos(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('\'');
        return writer;
    }

    protected Writer caseCloseQuot(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('"');
        return writer;
    }

    protected Writer caseCommentContentChar(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseCommentContentCharDash(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseCommentTest(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("comment()");
        return writer;
    }

    protected Writer caseComparisonExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(' ');
            writer.append(simpleNode.jjtGetValue().toString());
            writer.append(' ');
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseCompAttrConstructor(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("attribute ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseCompCommentConstructor(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("comment ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseCompDocConstructor(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("document ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseCompElemConstructor(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("element ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseCompPIConstructor(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("processing-instruction ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseCompTextConstructor(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("text ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseConstructionDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare construction ");
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseContextItemExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('.');
        return writer;
    }

    protected Writer caseCopyNamespacesDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare copy-namespaces ");
        SimpleNode preserveModeNode = (SimpleNode) simpleNode.jjtGetChild(0);
        writer.append(preserveModeNode.jjtGetValue().toString());
        writer.append(',');
        SimpleNode inheritModeNode = (SimpleNode) simpleNode.jjtGetChild(1);
        writer.append(inheritModeNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseDecimalLiteral(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseDefaultCollationDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare default collation ");
        SimpleNode uriNode = (SimpleNode) simpleNode.jjtGetChild(0);
        uriNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseDefaultNamespaceDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare default ");
        writer.append(simpleNode.jjtGetValue().toString());
        writer.append(" namespace ");
        SimpleNode uriNode = (SimpleNode) simpleNode.jjtGetChild(0);
        uriNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseDeleteExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("delete ");
        writer.append(simpleNode.jjtGetValue().toString());
        writer.append(' ');
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseDocumentTest(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("document-node(");
        simpleNode.childrenAccept(this, writer);
        writer.append(")");
        return writer;
    }

    protected Writer caseDoubleLiteral(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseElementContentChar(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseElementNameOrWildcard(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetNumChildren() > 0) {
            simpleNode.childrenAccept(this, writer);
        } else {
            writer.append(simpleNode.jjtGetValue().toString());
        }
        return writer;
    }

    protected Writer caseElementTest(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("element(");
        if (simpleNode.jjtGetNumChildren() > 0) {
            SimpleNode eltNameNode = (SimpleNode) simpleNode.jjtGetChild(0);
            eltNameNode.jjtAccept(this, writer);
            if (simpleNode.jjtGetNumChildren() > 1) {
                writer.append(',');
                SimpleNode typeNameNode = (SimpleNode) simpleNode.jjtGetChild(1);
                typeNameNode.jjtAccept(this, writer);
                if (simpleNode.jjtGetValue() != null) {
                    writer.append('?');
                }
            }
        }
        writer.append(")");
        return writer;
    }

    protected Writer caseEmptyOrderDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare default order empty ");
        SimpleNode specifierNode = (SimpleNode) simpleNode.jjtGetChild(0);
        writer.append(specifierNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseEmptyTagClose(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("/>");
        return writer;
    }

    protected Writer caseEndTagQName(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("</");
        writer.append(simpleNode.jjtGetValue().toString());
        writer.append('>');
        return writer;
    }

    protected Writer caseEscapeApos(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("''");
        return writer;
    }

    protected Writer caseEscapeQuot(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("\"\"");
        return writer;
    }

    protected Writer caseExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(",\n");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseExtensionContentChar(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseFLWORExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            if (index == simpleNode.jjtGetNumChildren() - 1) {
                writer.append("return ");
            }
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseForClause(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("for ");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            boolean isExprSingle = getSimpleNodeId(childNode) == XPathTreeConstants.JJTEXPRSINGLE;
            if (isExprSingle) {
                writer.append("in ");
            }
            childNode.jjtAccept(this, writer);
            index++;
            if (isExprSingle && index < simpleNode.jjtGetNumChildren()) {
                writer.append(',');
            }
        }
        return writer;
    }

    protected Writer caseForwardAxis(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        writer.append("::");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseFunctionCall(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        writer.append('(');
        while (index < simpleNode.jjtGetNumChildren()) {
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
            if (index < simpleNode.jjtGetNumChildren()) {
                writer.append(", ");
            }
        }
        writer.append(')');
        return writer;
    }

    protected Writer caseFunctionDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare ");
        if (simpleNode.jjtGetValue() != null) {
            writer.append("updating ");
        }
        writer.append("function ");
        SimpleNode qNameNode = (SimpleNode) simpleNode.jjtGetChild(0);
        qNameNode.jjtAccept(this, writer);
        int index = 1;
        writer.append('(');
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        if (getSimpleNodeId(childNode) == XPathTreeConstants.JJTPARAMLIST) {
            childNode.jjtAccept(this, writer);
            index++;
        }
        writer.append(')');
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        if (getSimpleNodeId(childNode) == XPathTreeConstants.JJTSEQUENCETYPE) {
            writer.append(" as ");
            childNode.jjtAccept(this, writer);
            index++;
        }
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        if (getSimpleNodeId(childNode) == XPathTreeConstants.JJTEXTERNAL) {
            writer.append("external");
        } else {
            writer.append(' ');
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseFunctionQName(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseIfExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("if(");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        writer.append(") then ");
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        writer.append(" else ");
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseInsertExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("insert ");
        writer.append(simpleNode.jjtGetValue().toString());
        writer.append(' ');
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseInsertExprTargetChoice(SimpleNode simpleNode, Writer writer) throws Exception {
        String value = simpleNode.jjtGetValue().toString();
        if (value.equals("first")) {
            writer.append(" as first into ");
        } else if (value.equals("last")) {
            writer.append(" as last into ");
        } else if (value.equals("before")) {
            writer.append(" before ");
        } else if (value.equals("after")) {
            writer.append(" after ");
        }
        return writer;
    }

    protected Writer caseInstanceofExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        if (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" instance of ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
        }
        return writer;
    }

    protected Writer caseIntegerLiteral(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseIntersectExceptExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(' ');
            writer.append(simpleNode.jjtGetValue().toString());
            writer.append(' ');
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseItemType(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetNumChildren() > 0) {
            simpleNode.childrenAccept(this, writer);
        } else {
            writer.append("item()");
        }
        return writer;
    }

    protected Writer caseLbrace(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('{');
        return writer;
    }

    protected Writer caseLbraceExprEnclosure(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('{');
        return writer;
    }

    protected Writer caseLCurlyBraceEscape(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("{{");
        return writer;
    }

    protected Writer caseLessThanOpOrTagO(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('<');
        return writer;
    }

    protected Writer caseLetClause(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("let ");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            boolean isExprSingle = getSimpleNodeId(childNode) == XPathTreeConstants.JJTEXPRSINGLE;
            if (isExprSingle) {
                writer.append(":= ");
            }
            childNode.jjtAccept(this, writer);
            index++;
            if (isExprSingle && index < simpleNode.jjtGetNumChildren()) {
                writer.append(',');
            }
        }
        return writer;
    }

    protected Writer caseMinus(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("-");
        return writer;
    }

    protected Writer caseModuleDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("module namespace ");
        SimpleNode ncNameNode = (SimpleNode) simpleNode.jjtGetChild(0);
        ncNameNode.jjtAccept(this, writer);
        writer.append('=');
        SimpleNode uriNode = (SimpleNode) simpleNode.jjtGetChild(1);
        uriNode.jjtAccept(this, writer);
        SimpleNode separatorNode = (SimpleNode) simpleNode.jjtGetChild(2);
        separatorNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseModuleImport(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("import module ");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        if (getSimpleNodeId(childNode) == XPathTreeConstants.JJTNCNAME) {
            writer.append("namespace ");
            childNode.jjtAccept(this, writer);
            index++;
            writer.append("=");
        }
        SimpleNode uriNode = (SimpleNode) simpleNode.jjtGetChild(index);
        uriNode.jjtAccept(this, writer);
        index++;
        if (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" at ");
            uriNode = (SimpleNode) simpleNode.jjtGetChild(index);
            uriNode.jjtAccept(this, writer);
            index++;
            while (index < simpleNode.jjtGetNumChildren()) {
                writer.append(",");
                uriNode = (SimpleNode) simpleNode.jjtGetChild(index);
                uriNode.jjtAccept(this, writer);
                index++;
            }
        }
        return writer;
    }

    protected Writer caseMultiplicativeExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(' ');
            writer.append(simpleNode.jjtGetValue().toString());
            writer.append(' ');
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseNCName(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetNumChildren() > 0) {
            SimpleNode qnameNode = (SimpleNode) simpleNode.jjtGetChild(0);
            qnameNode.jjtAccept(this, writer);
        } else {
            writer.append(simpleNode.jjtGetValue().toString());
        }
        return writer;
    }

    protected Writer caseNCNameColonStar(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseOccurrenceIndicator(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseOpenApos(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('\'');
        return writer;
    }

    protected Writer caseOpenQuot(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('"');
        return writer;
    }

    protected Writer caseOptionDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare option ");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        writer.append(' ');
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseOrderByClause(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetValue() != null) {
            writer.append("stable ");
        }
        writer.append("order by ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseOrderedExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("ordered ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseOrderingModeDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare ordering ");
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseOrderModifier(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(' ');
        for (int index = 0; index < simpleNode.jjtGetNumChildren(); index++) {
            SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            switch(getSimpleNodeId(childNode)) {
                case XPathTreeConstants.JJTASCENDING:
                    writer.append("ascending ");
                    break;
                case XPathTreeConstants.JJTDESCENDING:
                    writer.append("descending ");
                    break;
                case XPathTreeConstants.JJTGREATEST:
                    writer.append("empty greatest ");
                    break;
                case XPathTreeConstants.JJTLEAST:
                    writer.append("empty least ");
                    break;
                case XPathTreeConstants.JJTURILITERAL:
                    writer.append("collation ");
                    childNode.jjtAccept(this, writer);
                default:
                    break;
            }
        }
        return writer;
    }

    protected Writer caseOrderSpecList(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(", ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseOrExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" or ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseParam(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('$');
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        if (index < simpleNode.jjtGetNumChildren()) {
            writer.append(' ');
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseParamList(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(", ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseParenthesizedExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('(');
        simpleNode.childrenAccept(this, writer);
        writer.append(')');
        return writer;
    }

    protected Writer casePathExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        int previousId = XPathTreeConstants.JJTXPATH2;
        SimpleNode childNode = null;
        while (index < simpleNode.jjtGetNumChildren()) {
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            int nodeId = getSimpleNodeId(childNode);
            if (previousId == XPathTreeConstants.JJTSTEPEXPR && nodeId != XPathTreeConstants.JJTSLASHSLASH && nodeId != XPathTreeConstants.JJTSLASH) {
                writer.append('/');
            }
            childNode.jjtAccept(this, writer);
            if (nodeId != XPathTreeConstants.JJTSLASHSLASH && nodeId != XPathTreeConstants.JJTSLASH) {
                previousId = XPathTreeConstants.JJTSTEPEXPR;
            } else {
                previousId = getSimpleNodeId(childNode);
            }
            index++;
        }
        return writer;
    }

    protected Writer casePIContentChar(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer casePITarget(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer casePITest(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("processing-instruction(");
        simpleNode.childrenAccept(this, writer);
        writer.append(")");
        return writer;
    }

    protected Writer casePlus(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("+");
        return writer;
    }

    protected Writer casePositionalVar(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("at ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer casePragmaClose(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("#)");
        return writer;
    }

    protected Writer casePragmaOpen(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("(#");
        return writer;
    }

    protected Writer casePredefinedEntityRef(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer casePredicate(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('[');
        simpleNode.childrenAccept(this, writer);
        writer.append(']');
        return writer;
    }

    protected Writer caseProcessingInstructionEnd(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("?>");
        return writer;
    }

    protected Writer caseProcessingInstructionStart(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("<?");
        return writer;
    }

    protected Writer caseProcessingInstructionStartForElementContent(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("<?");
        return writer;
    }

    protected Writer caseQName(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetNumChildren() > 0) {
            SimpleNode functionQNameNode = (SimpleNode) simpleNode.jjtGetChild(0);
            functionQNameNode.jjtAccept(this, writer);
        } else {
            writer.append(simpleNode.jjtGetValue().toString());
        }
        return writer;
    }

    protected Writer caseQNameForPragma(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseQuantifiedExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        writer.append(' ');
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(' ');
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            boolean isExprSingle = getSimpleNodeId(childNode) == XPathTreeConstants.JJTEXPRSINGLE;
            if (isExprSingle) {
                writer.append("in ");
            }
            childNode.jjtAccept(this, writer);
            index++;
            if (isExprSingle) {
                if (index < simpleNode.jjtGetNumChildren() - 1) {
                    writer.append(',');
                } else if (index == simpleNode.jjtGetNumChildren() - 1) {
                    writer.append(" satisfies ");
                    childNode = (SimpleNode) simpleNode.jjtGetChild(index);
                    childNode.jjtAccept(this, writer);
                    index++;
                }
            }
        }
        return writer;
    }

    protected Writer caseQueryList(SimpleNode simpleNode, Writer writer) throws Exception {
        simpleNode.jjtGetChild(0).jjtAccept(this, writer);
        for (int i = 1; i < simpleNode.jjtGetNumChildren(); i++) {
            writer.append("%%%");
            writer.append('\n');
            simpleNode.jjtGetChild(i).jjtAccept(this, writer);
        }
        return writer;
    }

    protected Writer caseQuotAttrContentChar(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseRangeExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        if (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" to ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseRbrace(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('}');
        return writer;
    }

    protected Writer caseRCurlyBraceEscape(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("}}");
        return writer;
    }

    protected Writer caseRenameExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("rename node ");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        writer.append(" as ");
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseReplaceExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("replace ");
        if (simpleNode.jjtGetValue() != null) {
            writer.append("value of ");
        }
        writer.append("node ");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        writer.append(" with ");
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseRevalidationDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare revalidation ");
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseReverseAxis(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        writer.append("::");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseS(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseSchemaAttributeTest(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("schema-attribute(");
        simpleNode.childrenAccept(this, writer);
        writer.append(")");
        return writer;
    }

    protected Writer caseSchemaImport(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("import schema ");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        if (getSimpleNodeId(childNode) == XPathTreeConstants.JJTSCHEMAPREFIX) {
            childNode.jjtAccept(this, writer);
            index++;
        }
        SimpleNode uriNode = (SimpleNode) simpleNode.jjtGetChild(index);
        uriNode.jjtAccept(this, writer);
        index++;
        if (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" at ");
            uriNode = (SimpleNode) simpleNode.jjtGetChild(index);
            uriNode.jjtAccept(this, writer);
            index++;
            while (index < simpleNode.jjtGetNumChildren()) {
                writer.append(",");
                uriNode = (SimpleNode) simpleNode.jjtGetChild(index);
                uriNode.jjtAccept(this, writer);
                index++;
            }
        }
        return writer;
    }

    protected Writer caseSchemaPrefix(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetNumChildren() > 0) {
            writer.append("namespace ");
            SimpleNode ncNameNode = (SimpleNode) simpleNode.jjtGetChild(0);
            ncNameNode.jjtAccept(this, writer);
            writer.append("=");
        } else {
            writer.append("default element namespace ");
        }
        return writer;
    }

    protected Writer caseSeparator(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(';');
        writer.append('\n');
        return writer;
    }

    protected Writer caseSequenceType(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetNumChildren() > 0) {
            simpleNode.childrenAccept(this, writer);
        } else {
            writer.append("empty-sequence()");
        }
        return writer;
    }

    protected Writer caseSForPI(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseSForPragma(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseSlash(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('/');
        return writer;
    }

    protected Writer caseSlashSlash(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("//");
        return writer;
    }

    protected Writer caseStarColonNCName(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseStartTagClose(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('>');
        return writer;
    }

    protected Writer caseStartTagOpen(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('<');
        return writer;
    }

    protected Writer caseStringLiteral(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseTagQName(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseTextTest(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("text()");
        return writer;
    }

    protected Writer caseTransformExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("copy ");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        writer.append(" := ");
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        while (getSimpleNodeId(childNode) == XPathTreeConstants.JJTVARNAME) {
            writer.append(", ");
            childNode.jjtAccept(this, writer);
            index++;
            writer.append(" := ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        }
        writer.append(" modify ");
        childNode.jjtAccept(this, writer);
        index++;
        writer.append(" return ");
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseTreatExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        if (index < simpleNode.jjtGetNumChildren()) {
            writer.append(" treat as ");
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
        }
        return writer;
    }

    protected Writer caseTypeDeclaration(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("as ");
        return super.caseTypeDeclaration(simpleNode, writer);
    }

    protected Writer caseTypeswitchExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("typeswitch(");
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        writer.append(") ");
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        while (getSimpleNodeId(childNode) == XPathTreeConstants.JJTCASECLAUSE) {
            childNode.jjtAccept(this, writer);
            index++;
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        }
        writer.append("default ");
        if (getSimpleNodeId(childNode) == XPathTreeConstants.JJTVARNAME) {
            childNode.jjtAccept(this, writer);
            index++;
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        }
        writer.append("return ");
        childNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseUnionExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        childNode.jjtAccept(this, writer);
        index++;
        while (index < simpleNode.jjtGetNumChildren()) {
            writer.append(' ');
            writer.append(simpleNode.jjtGetValue().toString());
            writer.append(' ');
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseUnorderedExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("unordered ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseValidateExpr(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("validate ");
        int index = 0;
        SimpleNode childNode = null;
        while (index < simpleNode.jjtGetNumChildren()) {
            childNode = (SimpleNode) simpleNode.jjtGetChild(index);
            childNode.jjtAccept(this, writer);
            writer.append(' ');
            index++;
        }
        return writer;
    }

    protected Writer caseValidationMode(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append(simpleNode.jjtGetValue().toString());
        return writer;
    }

    protected Writer caseValueIndicator(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('=');
        return writer;
    }

    protected Writer caseVarDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("declare variable $");
        SimpleNode qNameNode = (SimpleNode) simpleNode.jjtGetChild(0);
        qNameNode.jjtAccept(this, writer);
        writer.append(" ");
        int index = 1;
        SimpleNode childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        if (getSimpleNodeId(childNode) == XPathTreeConstants.JJTTYPEDECLARATION) {
            childNode.jjtAccept(this, writer);
            index++;
            writer.append(' ');
        }
        childNode = (SimpleNode) simpleNode.jjtGetChild(index);
        if (getSimpleNodeId(childNode) == XPathTreeConstants.JJTEXTERNAL) {
            writer.append("external");
        } else {
            writer.append(":= ");
            childNode.jjtAccept(this, writer);
            index++;
        }
        return writer;
    }

    protected Writer caseVarName(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append('$');
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseVersionDecl(SimpleNode simpleNode, Writer writer) throws Exception {
        int index = 0;
        writer.append("xquery version ");
        SimpleNode versionNode = (SimpleNode) simpleNode.jjtGetChild(index);
        writer.append(versionNode.jjtGetValue().toString());
        index++;
        if (simpleNode.jjtGetNumChildren() == 3) {
            writer.append(" encoding ");
            SimpleNode encodingNode = (SimpleNode) simpleNode.jjtGetChild(index);
            writer.append(encodingNode.jjtGetValue().toString());
            index++;
        }
        SimpleNode separatorNode = (SimpleNode) simpleNode.jjtGetChild(index);
        separatorNode.jjtAccept(this, writer);
        return writer;
    }

    protected Writer caseWhereClause(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("where ");
        simpleNode.childrenAccept(this, writer);
        return writer;
    }

    protected Writer caseWildcard(SimpleNode simpleNode, Writer writer) throws Exception {
        if (simpleNode.jjtGetValue() != null) {
            writer.append("*");
        } else {
            simpleNode.childrenAccept(this, writer);
        }
        return writer;
    }

    protected Writer caseXmlCommentEnd(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("-->");
        return writer;
    }

    protected Writer caseXmlCommentStart(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("<!--");
        return writer;
    }

    protected Writer caseXmlCommentStartForElementContent(SimpleNode simpleNode, Writer writer) throws Exception {
        writer.append("<!--");
        return writer;
    }
}
