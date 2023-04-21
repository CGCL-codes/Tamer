package com.softberries.jasperberry.report;

import com.softberries.jasperberry.elements.JBReportElement;
import java.util.List;

/**
 * Represents base class for all report sections
 * like header, footer, detail etc.
 * @author Softberries Krzysztof Grajek
 */
public class JBReportContent {

    private int height;

    private List<JBReportElement> elements;

    private int margin;

    private String xmlName;

    public JBReportContent(String xmlname, List<JBReportElement> elements, int height) {
        this.height = height;
        this.elements = elements;
        this.xmlName = xmlname;
    }

    public JBReportContent(String xmlname, List<JBReportElement> elements) {
        this.margin = 1;
        this.height = calculateHeight(elements, this.margin);
        this.elements = elements;
        this.xmlName = xmlname;
    }

    public String getContent() {
        String header = "";
        if (this.height > 0) {
            header = "<" + this.xmlName + "><band height=\"" + this.height + "\" splitType=\"Stretch\">";
        } else {
            header = "<" + this.xmlName + "><band splitType=\"Stretch\">";
        }
        String content = "";
        if (elements != null) {
            for (JBReportElement e : this.elements) {
                if (e != null) content = content + e.getContent();
            }
        }
        String footer = "</band></" + this.xmlName + ">";
        return header + content + footer;
    }

    private int calculateHeight(List<JBReportElement> elements, int margin) {
        int i = 0;
        for (JBReportElement e : elements) {
            i = i + e.getHeight() + margin;
        }
        return i;
    }

    public List<JBReportElement> getElements() {
        return elements;
    }

    public void setElements(List<JBReportElement> elements) {
        this.elements = elements;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }

    public String getXmlName() {
        return xmlName;
    }

    public void setXmlName(String xmlName) {
        this.xmlName = xmlName;
    }
}
