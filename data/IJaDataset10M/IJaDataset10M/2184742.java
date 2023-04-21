package org.openxml4j.document.wordprocessing.model.table;

import org.dom4j.Element;
import org.dom4j.QName;
import org.openxml4j.document.wordprocessing.WordDocument;
import org.openxml4j.document.wordprocessing.WordprocessingML;

/**
 * @author   CDubettier
 */
public class TableBorder {

    /**
	 */
    private BorderStyle lineStyle;

    /**
	 * size of the border
	 */
    private int size;

    /**
	 */
    private int space;

    /**
	 * open XML color are in format RRGGBB (hex)
	 */
    private String color;

    private final String DEFAULT_COLOR = WordprocessingML.VALUE_AUTO;

    public TableBorder() {
        lineStyle = BorderStyle.BORDER_STYLE_SINGLE;
        size = 4;
        space = 0;
        color = DEFAULT_COLOR;
    }

    /**
	 * @param lineStyle value from BorderStyle (like BORDER_STYLE_SINGLE)
	 * @param size
	 * @param color open XML color are in format RRGGBB (hex)
	 */
    public TableBorder(BorderStyle lineStyle, int size, String color) {
        this.lineStyle = lineStyle;
        this.size = size;
        this.color = color;
        space = 0;
    }

    /**
	 * add border properties as open XML tags
	 * build something like w:val="single" w:sz="4" w:space="0" w:color="auto"
	 * @param rootDocument
	 * @param borderElement
	 */
    private void addBorderOpenXmlProperties(Element borderElement) {
        borderElement.addAttribute(new QName(WordprocessingML.ATTRIBUTE_VAL, WordDocument.namespaceWord), lineStyle.toString());
        borderElement.addAttribute(new QName(WordprocessingML.ATTRIBUTE_SIZE, WordDocument.namespaceWord), (new Integer(size)).toString());
        borderElement.addAttribute(new QName(WordprocessingML.ATTRIBUTE_SPACE, WordDocument.namespaceWord), (new Integer(space)).toString());
        borderElement.addAttribute(new QName(WordprocessingML.ATTRIBUTE_COLOR, WordDocument.namespaceWord), color);
    }

    public void build(Element tableElement) {
        Element borderXml = tableElement.addElement(new QName(WordprocessingML.TABLE_BORDER_TAG_NAME, WordDocument.namespaceWord));
        Element borderTopXml = borderXml.addElement(new QName(WordprocessingML.TABLE_BORDER_TOP_TAG_NAME, WordDocument.namespaceWord));
        Element borderBottomXml = borderXml.addElement(new QName(WordprocessingML.TABLE_BORDER_BOTTOM_TAG_NAME, WordDocument.namespaceWord));
        Element borderLeftXml = borderXml.addElement(new QName(WordprocessingML.TABLE_BORDER_LEFT_TAG_NAME, WordDocument.namespaceWord));
        Element borderRightXml = borderXml.addElement(new QName(WordprocessingML.TABLE_BORDER_RIGHT_TAG_NAME, WordDocument.namespaceWord));
        Element borderInsideVXml = borderXml.addElement(new QName(WordprocessingML.TABLE_BORDER_INSIDE_V_TAG_NAME, WordDocument.namespaceWord));
        Element borderinsideHXml = borderXml.addElement(new QName(WordprocessingML.TABLE_BORDER_INSIDE_H_TAG_NAME, WordDocument.namespaceWord));
        addBorderOpenXmlProperties(borderTopXml);
        addBorderOpenXmlProperties(borderBottomXml);
        addBorderOpenXmlProperties(borderLeftXml);
        addBorderOpenXmlProperties(borderRightXml);
        addBorderOpenXmlProperties(borderInsideVXml);
        addBorderOpenXmlProperties(borderinsideHXml);
    }

    /**
	 * @return   the color
	 * @uml.property  name="color"
	 */
    public String getColor() {
        return color;
    }

    /**
	 * should be in openXml format (ie RRGGBB in hex code)
	 * @param  color
	 * @uml.property  name="color"
	 */
    public void setColor(String color) {
        this.color = color;
    }

    /**
	 * @return   the lineStyle
	 * @uml.property  name="lineStyle"
	 */
    public BorderStyle getLineStyle() {
        return lineStyle;
    }

    /**
	 * @see BorderStyle
	 * @param  lineStyle
	 * @uml.property  name="lineStyle"
	 */
    public void setLineStyle(BorderStyle lineStyle) {
        this.lineStyle = lineStyle;
    }

    /**
	 * @return   the size
	 * @uml.property  name="size"
	 */
    public int getSize() {
        return size;
    }

    /**
	 * @param size   the size to set
	 * @uml.property  name="size"
	 */
    public void setSize(int size) {
        this.size = size;
    }

    /**
	 * @return   the space
	 * @uml.property  name="space"
	 */
    public int getSpace() {
        return space;
    }

    /**
	 * @param space   the space to set
	 * @uml.property  name="space"
	 */
    public void setSpace(int space) {
        this.space = space;
    }
}
