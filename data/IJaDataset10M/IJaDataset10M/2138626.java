package kr.ac.ssu.imc.whitehole.report.viewer.rdobjects.rdcrosstab;

import java.awt.*;
import kr.ac.ssu.imc.whitehole.report.viewer.RVGDDoc;
import kr.ac.ssu.imc.whitehole.report.viewer.rdobjects.RDObject;
import kr.ac.ssu.imc.whitehole.report.viewer.rdobjects.RVQuery;
import kr.ac.ssu.imc.whitehole.report.viewer.rdobjects.rdmaskings.*;

/**
 * ���̺��� �����ϴ� ���� ��Ÿ���� �� Ŭ�����̴�.
 * nType ���� ��� ���� �� ������ �����ȴ�.
 * nType ���� Label, QueryResult, Function, Image ���� ������ �ִ�.
 * Label ������ sText�� ������ �� ���ο� ����ϰ�, QueryResult ������ sQueryName :
 * sQueryField ������ ���ǹ� ����� �� ���ο� ����Ѵ�. Image ������
 * sImagePath�� �׸��� �� ���ο� ����Ѵ�. Function ������ ���� ���ǵ� ��갪
 * ���� �� ���ο� ����Ѵ�.
 */
public final class RDCTLabelModel extends RDCTAbstractLabelModel {

    public String sFontType;

    public int nFontSize;

    public int nFontStyle;

    public String sText;

    public int groupType;

    public int rowIndex;

    public int columnIndex;

    public int roleInTheGroup;

    public int functionType;

    /**
    * @deprecated
    */
    public String sQueryName;

    public String sQueryField;

    public int nFunctionIndex;

    public String sImagePath;

    public int nImageHeight;

    public int nImageWidth;

    public int masterRowIndex = -1;

    public int masterColumnIndex = -1;

    /**
    * ���� �����ϴ� ���� ��ü�� ���� ����.
    */
    public RVQuery oQuery;

    /**
    * ���� �����ϴ� ������ �ʵ忡 ���� ����.
    * ���ΰ��� ������ �ʵ� ��Ͽ� �����Ѵ�(0..n-1). ���� ������ �������� ���� ��쿡�� -1���� ���´�.
    */
    public int nQueryField;

    private static final String DEFAULT_FONT = "����";

    private static final int DEFAULT_FONTSIZE = 10;

    public static final int TOP_LEFT_CELL = 0;

    public static final int COLUMN_HEADER = 1;

    public static final int ROW_HEADER = 2;

    public static final int VALUE_TABLE = 3;

    public static final int NONE = 0;

    public static final int VALUE = 1;

    public static final int SUMMARY = 2;

    public static final int TITLE = 3;

    public static final int NONE_FOPT = 0;

    public static final int SUM_FOPT = 1;

    public static final int AVG_FOPT = 2;

    public static final int MAX_FOPT = 3;

    public static final int MIN_FOPT = 4;

    public static final int CNT_FOPT = 5;

    /**
    * @roseuid 3B1C91CE00FA
    */
    public RDCTLabelModel() {
        super();
        nType = LABEL;
        sFontType = DEFAULT_FONT;
        nFontSize = DEFAULT_FONTSIZE;
        nFontStyle = Font.PLAIN;
        sText = "";
        groupType = VALUE_TABLE;
        roleInTheGroup = NONE;
        functionType = NONE_FOPT;
    }

    /**
    * @roseuid 3B1C91CE012C
    */
    public void drawCell(Graphics g, int x, int y) {
        return;
    }

    /**
    * ���� ������ �������� ��ȯ�Ѵ�.
    */
    public void setLabelType() {
        if (nType == RDCTAbstractLabelModel.LABEL) return;
        nType = RDCTAbstractLabelModel.LABEL;
        sText = "";
    }

    /**
    * ���� ������ ���������� ��ȯ�Ѵ�.
    */
    public void setQueryType(int nFieldIndex) {
        if (nType == RDCTAbstractLabelModel.QUERY_RESULT) return;
        nType = RDCTAbstractLabelModel.QUERY_RESULT;
        nQueryField = nFieldIndex;
    }

    /**
    * ���� ������ ���������� ��ȯ�Ѵ�.
    */
    public void setQueryType(String nFieldName) {
        nType = RDCTAbstractLabelModel.QUERY_RESULT;
        sText = nFieldName;
    }

    /**
    * ���� ������ �̹��������� ��ȯ�Ѵ�.
    */
    public void setImageType() {
        if (nType == RDCTAbstractLabelModel.IMAGE) return;
        nType = RDCTAbstractLabelModel.IMAGE;
    }

    /**
    * �� ��ü��  Ŭ���� �ǵ�����.
    */
    public RDCTAbstractLabelModel getStaticClone() {
        RDCTLabelModel oReturn = new RDCTLabelModel();
        super.setStaticClone(oReturn);
        oReturn.nType = nType;
        oReturn.sFontType = sFontType;
        oReturn.nFontSize = nFontSize;
        oReturn.nFontStyle = nFontStyle;
        oReturn.sText = sText;
        oReturn.sQueryField = sQueryField;
        oReturn.nFunctionIndex = nFunctionIndex;
        oReturn.sImagePath = sImagePath;
        oReturn.nImageHeight = nImageHeight;
        oReturn.nImageWidth = nImageWidth;
        oReturn.oQuery = oQuery;
        oReturn.nQueryField = nQueryField;
        oReturn.groupType = groupType;
        oReturn.columnIndex = columnIndex;
        oReturn.rowIndex = rowIndex;
        oReturn.roleInTheGroup = roleInTheGroup;
        oReturn.functionType = functionType;
        if (maskOpt != null) oReturn.maskOpt = maskOpt.getAClone(); else oReturn.maskOpt = null;
        return oReturn;
    }

    /**
    * ���� ���ڿ��� �����Ѵ�.
    */
    public void changeCellText(String sText) {
        this.sText = sText;
    }

    public String getText() {
        return sText;
    }

    /**
    * ���� ���� �ʵ带 �����Ѵ�.
    */
    public void changeQueryField(int nIndex) {
        if (oQuery == null || oQuery.getResultColumnCount() <= nIndex) nQueryField = -1; else nQueryField = nIndex;
    }

    /**
    * ���� ������ �����Ѵ�.
    */
    public void changeQuery(RVQuery oQuery) {
        this.oQuery = oQuery;
        changeQueryField(-1);
    }

    public void setStartX(int x) {
        nStartXLoc = x;
    }

    public int getStartX() {
        return nStartXLoc;
    }

    public void setStartY(int y) {
        nStartYLoc = y;
    }

    public int getStartY() {
        return nStartYLoc;
    }

    public org.w3c.dom.Element createElementNode(org.w3c.dom.Document oDocument) {
        org.w3c.dom.Element oLabelElem = oDocument.createElement("rdCTLabel");
        super.setElementNode(oDocument, oLabelElem);
        oLabelElem.setAttribute("text", sText);
        oLabelElem.setAttribute("labelType", Integer.toString(nType));
        oLabelElem.setAttribute("groupType", Integer.toString(groupType));
        oLabelElem.setAttribute("query", sQueryName);
        oLabelElem.setAttribute("field", sQueryField);
        oLabelElem.setAttribute("fontSize", Integer.toString(nFontSize));
        oLabelElem.setAttribute("fontType", sFontType);
        oLabelElem.setAttribute("row", Integer.toString(rowIndex));
        oLabelElem.setAttribute("column", Integer.toString(columnIndex));
        oLabelElem.setAttribute("role", Integer.toString(roleInTheGroup));
        if (this.maskOpt != null) {
            org.w3c.dom.Element oLabelMaskElem = this.maskOpt.createElementNode(oDocument);
            oLabelElem.appendChild(oLabelMaskElem);
        }
        return oLabelElem;
    }

    public static RDCTLabelModel createModelFromNode(org.w3c.dom.Node oColCellNode, RVGDDoc oGDDoc) {
        String labelType = ((org.w3c.dom.Element) oColCellNode).getAttribute("labelType");
        String groupType = ((org.w3c.dom.Element) oColCellNode).getAttribute("groupType");
        String x = ((org.w3c.dom.Element) oColCellNode).getAttribute("x");
        String y = ((org.w3c.dom.Element) oColCellNode).getAttribute("y");
        String width = ((org.w3c.dom.Element) oColCellNode).getAttribute("width");
        String labelHeight = ((org.w3c.dom.Element) oColCellNode).getAttribute("height");
        String text = ((org.w3c.dom.Element) oColCellNode).getAttribute("text");
        String row = ((org.w3c.dom.Element) oColCellNode).getAttribute("row");
        String column = ((org.w3c.dom.Element) oColCellNode).getAttribute("column");
        String query = ((org.w3c.dom.Element) oColCellNode).getAttribute("query");
        String field = ((org.w3c.dom.Element) oColCellNode).getAttribute("field");
        String fontSize = ((org.w3c.dom.Element) oColCellNode).getAttribute("fontSize");
        String fontType = ((org.w3c.dom.Element) oColCellNode).getAttribute("fontType");
        String colorFg = ((org.w3c.dom.Element) oColCellNode).getAttribute("colorFg");
        String colorBg = ((org.w3c.dom.Element) oColCellNode).getAttribute("colorBg");
        String alignHori = ((org.w3c.dom.Element) oColCellNode).getAttribute("alignHori");
        String role = ((org.w3c.dom.Element) oColCellNode).getAttribute("role");
        String masterRowIndex = ((org.w3c.dom.Element) oColCellNode).getAttribute("masterRowIndex");
        String masterColumnIndex = ((org.w3c.dom.Element) oColCellNode).getAttribute("masterColumnIndex");
        String drawable = ((org.w3c.dom.Element) oColCellNode).getAttribute("drawable");
        String functionType = ((org.w3c.dom.Element) oColCellNode).getAttribute("functionType");
        org.w3c.dom.NodeList oColCellList = oColCellNode.getChildNodes();
        org.w3c.dom.Node oMaskingNode = null;
        if (oColCellList != null) {
            for (int i = 0; i < oColCellList.getLength(); i++) {
                if (oColCellList.item(i).getNodeName().equals("mModel")) {
                    oMaskingNode = oColCellList.item(i);
                    continue;
                }
            }
        }
        RDCTLabelModel newLabelModel = new RDCTLabelModel();
        if (new Integer(labelType).intValue() == RDCTAbstractLabelModel.QUERY_RESULT) newLabelModel.setQueryType(new Integer(labelType).intValue()); else newLabelModel.setLabelType();
        newLabelModel.setSize(new Integer(width).intValue(), new Integer(labelHeight).intValue());
        newLabelModel.changeCellText(text);
        newLabelModel.setStartX(new Integer(x).intValue());
        newLabelModel.setStartY(new Integer(y).intValue());
        newLabelModel.setGroupInTheTable(Integer.parseInt(groupType));
        newLabelModel.rowIndex = Integer.parseInt(row);
        newLabelModel.columnIndex = Integer.parseInt(column);
        newLabelModel.sQueryName = query;
        newLabelModel.sQueryField = field;
        newLabelModel.nFontSize = Integer.parseInt(fontSize);
        newLabelModel.sFontType = fontType;
        newLabelModel.oColorFg = RDObject.createColor(colorFg);
        newLabelModel.oColorBg = RDObject.createColor(colorBg);
        newLabelModel.nAlignHori = Integer.parseInt(alignHori);
        newLabelModel.roleInTheGroup = Integer.parseInt(role);
        newLabelModel.masterRowIndex = Integer.parseInt(masterRowIndex);
        newLabelModel.masterColumnIndex = Integer.parseInt(masterColumnIndex);
        newLabelModel.bDrawable = drawable.equals("true") ? true : false;
        newLabelModel.functionType = Integer.parseInt(functionType);
        if (oMaskingNode != null) {
            newLabelModel.maskOpt = RDMaskingModel.createRDObject((org.w3c.dom.Element) oMaskingNode);
        }
        return newLabelModel;
    }

    public void setGroupInTheTable(int groupType) {
        this.groupType = groupType;
    }

    public int getGourpInTheTable() {
        return groupType;
    }
}
