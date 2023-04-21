package edu.pw.treegrid.server.reportmodel;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import xalan.xml.XMLUtils;
import edu.pw.treegrid.server.classmodel.SimpleAttribute;
import edu.pw.treegrid.server.query.ColumnNode;
import edu.pw.treegrid.server.query.Node;
import edu.pw.treegrid.server.service.XMLMarshaller;
import edu.pw.treegrid.shared.ReportColumnCategory;
import edu.pw.treegrid.shared.ReportColumnType;

public class ExpressionColumn extends ReportSimpleColumn {

    public static final String EXPRESSION_ELEMENT = "expression";

    public ExpressionColumn(String name) {
        super(name);
        setHierarchyCandidate(false);
        setType(ReportColumnType.INTEGER);
        setFormat("#,###");
    }

    public ReportColumnCategory getCategory() {
        if (this.isVisible()) {
            return ReportColumnCategory.MEASURE;
        } else {
            return ReportColumnCategory.MEASURE_NOT_VISIBLE;
        }
    }

    @Override
    public Node buildQueryNode() {
        ColumnNode cn = new ExpressionColumnNode();
        cn.setColumnAlias(this.getName());
        cn.setColumnName(this.getName());
        cn.setReportColumn(this);
        return cn;
    }

    @Override
    public SimpleAttribute getSimpleAttribute() {
        return null;
    }

    private static class ExpressionColumnNode extends ColumnNode {

        @Override
        public String getBaseExpressionString() {
            return "0";
        }

        @Override
        public String getExpressionString() {
            return getBaseExpressionString();
        }

        @Override
        public boolean isMeasure() {
            return true;
        }
    }

    @Override
    protected void serializeToXML(Document document, Element columnElement) {
        super.serializeToXML(document, columnElement);
        Element expressionElement = document.createElement(EXPRESSION_ELEMENT);
        expressionElement.setTextContent(this.expression);
        columnElement.appendChild(expressionElement);
    }

    @Override
    protected void deserializeFromXML(Element e, XMLMarshaller marshaller) {
        super.deserializeFromXML(e, marshaller);
        this.expression = XMLUtils.getElementsText(e, EXPRESSION_ELEMENT);
    }

    @Override
    protected boolean isSerialize() {
        return true;
    }

    private String expression;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }
}
