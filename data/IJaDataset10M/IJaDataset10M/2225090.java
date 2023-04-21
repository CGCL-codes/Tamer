package kr.ac.ssu.imc.durubi.report.viewer.components;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import javax.accessibility.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.awt.font.*;
import kr.ac.ssu.imc.durubi.report.viewer.*;
import kr.ac.ssu.imc.durubi.report.viewer.components.maskings.*;
import kr.ac.ssu.imc.durubi.report.viewer.components.query.DRQuery;
import kr.ac.ssu.imc.durubi.report.viewer.preview.DRReportCreate;
import org.w3c.dom.*;
import org.xml.sax.*;

public class DRODynamicWordBox extends DRObject {

    private Vector tTextElements;

    private String sTextSource;

    private JTextPane tTextPane;

    private JScrollPane tSPane;

    private EventListener[] tELs = null;

    private MutableAttributeSet tFontAttribute;

    private DRQuery tQuery;

    private int nFieldIndex;

    private boolean isBold = false;

    private boolean isItalic = false;

    private boolean isUnderline = false;

    private DRMaskingModel maskOpt;

    public DRODynamicWordBox(Point tStartLoc, Dimension tSize, DRQuery tQuery, int nFieldIndex, String sFontType, int nFontSize, Color tFontColor, int nAlignment, Color tBackground, Color tBorderColor, int nBorderWidth) {
        super(tStartLoc, tSize);
        oShape.nType = DRShapeInfo.RECT;
        oSelectView.setStyle(DRSelectView.CONTAINER);
        nType = DYNAMIC;
        this.tQuery = tQuery;
        this.nFieldIndex = nFieldIndex;
        oFont.sFamily = sFontType;
        oFont.nSize = nFontSize;
        oFont.nAlign = nAlignment;
        oShape.oColorFg = tFontColor;
        oShape.oColorBg = tBackground;
        oBorder.oColor = tBorderColor;
        oBorder.nWidth = nBorderWidth;
        oBorder.bVisible = true;
        tTextPane = createTextPane();
        if (sTextSource != null) tTextPane.setText(this.sTextSource);
        changeBackground(oShape.oColorBg);
        setLayout(null);
        tSPane = new JScrollPane(tTextPane, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tSPane.setBounds(oShape.nXRelative, oShape.nYRelative, oShape.nWidth + 1, oShape.nHeight + 1);
        add(tSPane);
        tSPane.setBorder(new LineBorder(oBorder.oColor, oBorder.nWidth));
        tSPane.setBackground(Color.white);
        tTextPane.setCaretPosition(0);
        oSelectView.bSelected = false;
        tTextPane.setEditable(oSelectView.bSelected);
        tTextPane.setEditable(oSelectView.bSelected);
    }

    public JTextPane getTextPane() {
        return tTextPane;
    }

    public void changeFontType(String sFontFamily) {
        tTextPane.selectAll();
        tFontAttribute = new SimpleAttributeSet();
        StyleConstants.setFontFamily(tFontAttribute, sFontFamily);
        tTextPane.getStyledDocument().setCharacterAttributes(tTextPane.getSelectionStart(), tTextPane.getSelectionEnd(), tFontAttribute, false);
        oFont.sFamily = sFontFamily;
    }

    public void changeFontSize(int nSize) {
        tTextPane.selectAll();
        tFontAttribute = new SimpleAttributeSet();
        StyleConstants.setFontSize(tFontAttribute, nSize);
        tTextPane.getStyledDocument().setCharacterAttributes(tTextPane.getSelectionStart(), tTextPane.getSelectionEnd(), tFontAttribute, false);
        oFont.nSize = nSize;
    }

    public void changeFontColor(Color tColor) {
        tTextPane.selectAll();
        tFontAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(tFontAttribute, tColor);
        tTextPane.getStyledDocument().setCharacterAttributes(tTextPane.getSelectionStart(), tTextPane.getSelectionEnd(), tFontAttribute, false);
        oShape.oColorFg = tColor;
    }

    public void changeBold(boolean bValue) {
        tTextPane.selectAll();
        tFontAttribute = new SimpleAttributeSet();
        StyleConstants.setBold(tFontAttribute, !bValue);
        tTextPane.getStyledDocument().setCharacterAttributes(tTextPane.getSelectionStart(), tTextPane.getSelectionEnd(), tFontAttribute, false);
        isBold = !bValue;
    }

    public void changeItalic(boolean bValue) {
        tTextPane.selectAll();
        tFontAttribute = new SimpleAttributeSet();
        StyleConstants.setItalic(tFontAttribute, !bValue);
        tTextPane.getStyledDocument().setCharacterAttributes(tTextPane.getSelectionStart(), tTextPane.getSelectionEnd(), tFontAttribute, false);
        isItalic = !bValue;
    }

    public void changeUnderlined(boolean bValue) {
        tTextPane.selectAll();
        tFontAttribute = new SimpleAttributeSet();
        StyleConstants.setUnderline(tFontAttribute, !bValue);
        tTextPane.getStyledDocument().setCharacterAttributes(tTextPane.getSelectionStart(), tTextPane.getSelectionEnd(), tFontAttribute, false);
        isUnderline = !bValue;
    }

    public void changeAlignment(int nAlignment) {
        tTextPane.selectAll();
        tFontAttribute = new SimpleAttributeSet();
        StyleConstants.setAlignment(tFontAttribute, nAlignment);
        tTextPane.getStyledDocument().setParagraphAttributes(tTextPane.getSelectionStart(), tTextPane.getSelectionEnd(), tFontAttribute, false);
        oFont.nAlign = nAlignment;
    }

    /** �׵θ��� �β��� �ٲ۴�.*/
    public void changeLineWidth(int nWidth) {
        oBorder.nWidth = nWidth;
        tSPane.setBorder(new DRLineBorder(oBorder.oColor, oBorder.nWidth, oBorder.nType));
    }

    /** ���� ���� �ٲ۴�.*/
    public void changeBackground(Color tColor) {
        oShape.oColorBg = tColor;
        tTextPane.setBackground(oShape.oColorBg);
    }

    /** �׵θ��� ������ �ٲ۴�.*/
    public void changeColor(Color tColor) {
        oBorder.oColor = tColor;
        tSPane.setBorder(new DRLineBorder(oBorder.oColor, oBorder.nWidth, oBorder.nType));
    }

    public void changeLineKind(int nKind) {
        oBorder.nType = nKind;
        tSPane.setBorder(new DRLineBorder(oBorder.oColor, oBorder.nWidth, oBorder.nType));
    }

    public void setObjectSize(Dimension tSize) {
        super.setObjectSize(tSize);
        if (tSPane != null) tSPane.setSize(oShape.nWidth + 1, oShape.nHeight + 1);
    }

    public org.w3c.dom.Element createElementNode(org.w3c.dom.Document tDocument) {
        super.createElementNode(tDocument);
        org.w3c.dom.Element tDWordElement = tDocument.createElement("rdoDynamicWordBox");
        tDWordElement.setAttribute("family", oFont.sFamily);
        tDWordElement.setAttribute("foreground", oShape.oColorFg.toString());
        tDWordElement.setAttribute("size", Integer.toString(oFont.nSize));
        tDWordElement.setAttribute("background", oShape.oColorBg.toString());
        tDWordElement.setAttribute("borderColor", oBorder.oColor.toString());
        tDWordElement.setAttribute("borderWidth", Integer.toString(oBorder.nWidth));
        tDWordElement.setAttribute("borderKind", Integer.toString(oBorder.nType));
        tDWordElement.setAttribute("isBold", new Boolean(isBold).toString());
        tDWordElement.setAttribute("isItalic", new Boolean(isItalic).toString());
        tDWordElement.setAttribute("isUnderline", new Boolean(isUnderline).toString());
        String sAlignment = "left";
        if (oFont.nAlign == StyleConstants.ALIGN_CENTER || oFont.nAlign == StyleConstants.ALIGN_JUSTIFIED) sAlignment = "center"; else if (oFont.nAlign == StyleConstants.ALIGN_RIGHT) sAlignment = "right";
        tDWordElement.setAttribute("halignment", sAlignment);
        tDWordElement.setAttribute("queryName", tQuery.getName());
        tDWordElement.setAttribute("fieldIndex", Integer.toString(nFieldIndex));
        oElement.appendChild(tDWordElement);
        if (this.maskOpt != null) {
            org.w3c.dom.Element maskingElem = this.maskOpt.createElementNode(tDocument);
            tDWordElement.appendChild(maskingElem);
        }
        return oElement;
    }

    public static DRObject createRDObject(org.w3c.dom.Element tElement, DRReportCreate tGDDoc) {
        int nPosX = -1, nPosY = -1, nWidth = -1, nHeight = -1;
        org.w3c.dom.NodeList oPosList = tElement.getChildNodes();
        for (int i = 0; i < oPosList.getLength(); i++) if (oPosList.item(i).getNodeName().equals("rdShape")) {
            org.w3c.dom.Element oPosElem = (org.w3c.dom.Element) oPosList.item(i);
            nPosX = Integer.parseInt(oPosElem.getAttribute("x"));
            nPosY = Integer.parseInt(oPosElem.getAttribute("y"));
            nWidth = Integer.parseInt(oPosElem.getAttribute("width"));
            nHeight = Integer.parseInt(oPosElem.getAttribute("height"));
            break;
        }
        boolean bFlag;
        org.w3c.dom.NodeList tNodeList = tElement.getChildNodes();
        org.w3c.dom.Node tWordBoxNode = null;
        org.w3c.dom.Node tWordBoxMaskNode = null;
        bFlag = false;
        for (int i = 0; i < tNodeList.getLength(); i++) if (tNodeList.item(i).getNodeName().equals("rdoDynamicWordBox")) {
            tWordBoxNode = (org.w3c.dom.Node) tNodeList.item(i);
            bFlag = true;
            break;
        }
        if (!bFlag) return null;
        org.w3c.dom.NodeList tWordBoxNodeList = tWordBoxNode.getChildNodes();
        for (int i = 0; i < tWordBoxNodeList.getLength(); i++) if (tWordBoxNodeList.item(i).getNodeName().equals("mModel")) {
            tWordBoxMaskNode = (org.w3c.dom.Node) tWordBoxNodeList.item(i);
            break;
        }
        String sFamily = ((org.w3c.dom.Element) tWordBoxNode).getAttribute("family");
        boolean isBold = ((org.w3c.dom.Element) tWordBoxNode).getAttribute("isBold").equals("true") ? true : false;
        boolean isItalic = ((org.w3c.dom.Element) tWordBoxNode).getAttribute("isItalic").equals("true") ? true : false;
        boolean isUnderline = ((org.w3c.dom.Element) tWordBoxNode).getAttribute("isUnderline").equals("true") ? true : false;
        Color tForeground = createColor(((org.w3c.dom.Element) tWordBoxNode).getAttribute("foreground"));
        int nSize = Integer.parseInt(((org.w3c.dom.Element) tWordBoxNode).getAttribute("size"));
        Color tBackground = createColor(((org.w3c.dom.Element) tWordBoxNode).getAttribute("background"));
        Color tBorderColor = createColor(((org.w3c.dom.Element) tWordBoxNode).getAttribute("borderColor"));
        int nBorderWidth = Integer.parseInt(((org.w3c.dom.Element) tWordBoxNode).getAttribute("borderWidth"));
        int nBorderKind = Integer.parseInt(((org.w3c.dom.Element) tWordBoxNode).getAttribute("borderKind"));
        int nHAlignment = StyleConstants.ALIGN_LEFT;
        String sHAlignment = ((org.w3c.dom.Element) tWordBoxNode).getAttribute("halignment");
        if (sHAlignment.equals("center") || sHAlignment.equals("just")) nHAlignment = StyleConstants.ALIGN_CENTER; else if (sHAlignment.equals("right")) nHAlignment = StyleConstants.ALIGN_RIGHT;
        String sQueryNameRes = ((org.w3c.dom.Element) tWordBoxNode).getAttribute("queryName");
        int nFieldIndexRes = Integer.parseInt(((org.w3c.dom.Element) tWordBoxNode).getAttribute("fieldIndex"));
        DRQuery tQueryRes = null;
        if (sQueryNameRes != null && sQueryNameRes.length() > 0) {
            if (tGDDoc.udsUsed()) {
                System.out.println("udsUsed True");
                for (int j = 0; j < tGDDoc.tListOfUDSs.size(); j++) if (sQueryNameRes.equals(((DROUserDataSet) tGDDoc.tListOfUDSs.get(j)).getName())) {
                    tQueryRes = (DROUserDataSet) tGDDoc.tListOfUDSs.get(j);
                    break;
                }
            } else {
                System.out.println("udsUsed false");
                for (int j = 0; j < tGDDoc.tListOfQueries.size(); j++) if (sQueryNameRes.equals(((DRQuery) tGDDoc.tListOfQueries.get(j)).getName())) {
                    tQueryRes = (DRQuery) tGDDoc.tListOfQueries.get(j);
                    break;
                }
            }
        }
        if (tQueryRes == null) return null;
        DRODynamicWordBox tRDODynamicWordBox = new DRODynamicWordBox(new Point(nPosX, nPosY), new Dimension(nWidth, nHeight), tQueryRes, nFieldIndexRes, sFamily, nSize, tForeground, nHAlignment, tBackground, tBorderColor, nBorderWidth);
        tRDODynamicWordBox.setupElementNode(tElement);
        tRDODynamicWordBox.changeBackground(tBackground);
        tRDODynamicWordBox.changeLineWidth(nBorderWidth);
        tRDODynamicWordBox.changeColor(tBorderColor);
        tRDODynamicWordBox.isBold = isBold;
        tRDODynamicWordBox.isItalic = isItalic;
        tRDODynamicWordBox.isUnderline = isUnderline;
        tRDODynamicWordBox.changeLineKind(nBorderKind);
        if (tWordBoxMaskNode != null) tRDODynamicWordBox.maskOpt = DRMaskingModel.createRDObject((org.w3c.dom.Element) tWordBoxMaskNode);
        return tRDODynamicWordBox;
    }

    public void paintContent(Graphics g) {
    }

    public void setSelected(boolean bValue) {
        if (bValue != oSelectView.bSelected) {
            oSelectView.bSelected = bValue;
            tTextPane.setEditable(false);
            revalidate();
            repaint();
        }
    }

    /** JTextPane ��ü�� ���ؼ� �ʱ�ȭ�� �Ŀ� �����Ѵ�.
   *  @return JTextPane ��ü.
   */
    private JTextPane createTextPane() {
        JTextPane tReturn = new JTextPane();
        String sText = createDynamicText(0, 0);
        if (sText != null && sText.length() > 0) this.sTextSource = (sText == null) ? null : sText;
        return tReturn;
    }

    private String createDynamicText(int nVertPageNum, int nHoriPageNum) {
        if (nVertPageNum >= tQuery.getResultRowCount()) return null;
        if (this.maskOpt != null) {
            String text = DRMaskingSet.processMasking(this.maskOpt, tQuery.getCellData(nVertPageNum, nFieldIndex));
            return text;
        } else return new String(tQuery.getCellData(nVertPageNum, nFieldIndex));
    }

    /** �����θ� �����ؼ� �����Ѵ�.
   *  @return �� ��ü�� ������ RDObject ��ü.
   */
    public DRObject getAClone() {
        DROWordBox tNewObject = new DROWordBox(getObjectLocation(), getObjectSize(), tTextPane.getText());
        tNewObject.getTextPane().selectAll();
        tNewObject.setStartSelection(tNewObject.getTextPane().getSelectionStart());
        tNewObject.setEndSelection(tNewObject.getTextPane().getSelectionEnd());
        tNewObject.changeBackground(oShape.oColorBg);
        tNewObject.changeLineWidth(oBorder.nWidth);
        tNewObject.changeColor(oBorder.oColor);
        tNewObject.changeAlignment(oFont.nAlign);
        tNewObject.changeFontColor(oShape.oColorFg);
        tNewObject.changeFontSize(oFont.nSize);
        tNewObject.changeFontType(oFont.sFamily);
        tNewObject.changeBold(!isBold);
        tNewObject.changeItalic(!isItalic);
        tNewObject.changeUnderlined(!isUnderline);
        tNewObject.changeLineKind(oBorder.nType);
        if (this.maskOpt != null) {
        }
        return tNewObject;
    }

    /**
   * �־��� �������� �ش��ϴ� �ۻ��ڸ� �������� ���ؼ� �����Ѵ�.
   * @param nVertPageNum ������ �� ��ȣ.
   * @param nHoriPageNum ������ �� ��ȣ.
   * @return ������ ��ȣ�� ��� ��� RDOWordBox �ۻ��� ��ü�� �����Ѵ�.
   */
    public DRObject getDynamicObject(int nVertPageNum, int nHoriPageNum) {
        String sText = createDynamicText(nVertPageNum, nHoriPageNum);
        DROWordBox tReturn = new DROWordBox(getObjectLocation(), getObjectSize(), sText);
        tReturn.setStartSelection(0);
        tReturn.setEndSelection(tReturn.getTextPane().getText().length());
        tReturn.changeBackground(oShape.oColorBg);
        tReturn.changeLineWidth(oBorder.nWidth);
        tReturn.changeColor(oBorder.oColor);
        tReturn.changeAlignment(oFont.nAlign);
        tReturn.changeFontColor(oShape.oColorFg);
        tReturn.changeFontSize(oFont.nSize);
        tReturn.changeFontType(oFont.sFamily);
        tReturn.changeBold(!isBold);
        tReturn.changeItalic(!isItalic);
        tReturn.changeUnderlined(!isUnderline);
        tReturn.changeLineKind(oBorder.nType);
        return tReturn;
    }

    /**
   * ������ ��ü���� �������� ��� ��ü�� ������ ������ �����Ѵ�.
   * @return ������ ��ü�� ��� �������� ��� ��ü�� ������ �����ϰ�,
   * ������ ��ü�� ��쿡�� -1�� �����Ѵ�.
   */
    public int getNumOfDynamicObjects() {
        if (tQuery == null) return -1;
        return tQuery.getResultRowCount();
    }

    /**
   * ������ ��ü���� �������� ��� ��ü�� ������ Ư�� ������ ��ġ�� ���� ������ ������ �����Ѵ�.
   * @param nVertPageNum ������ ������ ��ȣ
   * @return �������� ��� ������ ������ �����ϰ�, ������ ��ü�� ��쿡�� -1�� �����Ѵ�.
   */
    public int getNumOfDynamicObjects(int nVertPageNum) {
        return 1;
    }

    public void setMaskOpt(DRMaskingModel maskOpt) {
        this.maskOpt = maskOpt;
    }

    public DRMaskingModel getMaskOpt() {
        return this.maskOpt = maskOpt;
    }

    public Color getDynamicWordBoxTextColor() {
        return oShape.oColorFg;
    }

    public String getFontType() {
        return oFont.sFamily;
    }

    public String getDynamicWordBoxText() {
        return sTextSource;
    }

    public int getFontSize() {
        return oFont.nSize;
    }

    public String getQueryName() {
        String columnName = tQuery.getResultsTableData().getColumnNames(nFieldIndex);
        return new String(tQuery.getName() + "_" + columnName);
    }
}
