package org.xaware.ide.xadev.gui.xmleditor.xaware.parser;

import java.util.StringTokenizer;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.jdom.Attribute;
import org.jdom.Element;
import org.xaware.shared.util.logging.XAwareLogger;

/**
 * This class is used to parse XMLDisplayDef element of userconfig.xml file.
 * 
 * @author T Vasu
 * @version 1.0
 */
public class XMLDisplayDef {

    /** Holds AttributeSet string. */
    public static final String ATTRIBUTE_SET = "AttributeSet";

    /** Holds Elements string. */
    public static final String ELEMENTS = "Elements";

    /** Holds AttributeName string. */
    public static final String ATTRIBUTE_NAME = "AttributeName";

    /** Holds AttributeValue string. */
    public static final String ATTRIBUTE_VALUE = "AttributeValue";

    /** Holds Content string. */
    public static final String CONTENT = "Content";

    /** Holds foregroundColor string. */
    public static final String FOREGROUND_COLOR = "foregroundColor";

    /** Holds backgroundColor string. */
    public static final String BACKGROUND_COLOR = "backgroundColor";

    /** Holds bold string. */
    public static final String BOLD = "bold";

    /** holds italic string. */
    public static final String ITALIC = "italic";

    /** Logger for XAware. */
    private static final XAwareLogger logger = XAwareLogger.getXAwareLogger(XMLDisplayDef.class.getName());

    /** Holds AttributeSet of Elements. */
    private TextAttribute elements;

    /** Holds AttributeSet of AttributeName. */
    private TextAttribute attributeName;

    /** Holds AttributeSet of AttributeValue. */
    private TextAttribute attributeValue;

    /** Holds AttributeSet of Content. */
    private TextAttribute content;

    /**
     * Creates a new XMLDisplayDef object.
     */
    public XMLDisplayDef() {
        elements = new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        attributeName = new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        attributeValue = new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        content = new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
    }

    /**
     * Creates a new XMLDisplayDef object.
     * 
     * @param defElem
     *            XMLDisplayDef element.
     */
    public XMLDisplayDef(final Element defElem) {
        final Element elementsElem = defElem.getChild(ELEMENTS);
        if (elementsElem != null) {
            elements = parseAttributeSet(elementsElem.getChild(ATTRIBUTE_SET));
        } else {
            logger.info("No definition for Elements AttributeSet");
        }
        final Element attNamesElem = defElem.getChild(ATTRIBUTE_NAME);
        if (attNamesElem != null) {
            attributeName = parseAttributeSet(attNamesElem.getChild(ATTRIBUTE_SET));
        } else {
            logger.info("No definition for AttributeName AttributeSet");
        }
        final Element attValuesElem = defElem.getChild(ATTRIBUTE_VALUE);
        if (attValuesElem != null) {
            attributeValue = parseAttributeSet(attValuesElem.getChild(ATTRIBUTE_SET));
        } else {
            logger.info("No definition for AttributeValue AttributeSet");
        }
        final Element contentElem = defElem.getChild(CONTENT);
        if (contentElem != null) {
            content = parseAttributeSet(contentElem.getChild(ATTRIBUTE_SET));
        } else {
            logger.info("No definition for Content AttributeSet");
        }
    }

    /**
     * Method to parse AttributeSet of an element.
     * 
     * @param curAttSet
     *            AttributeSet.
     * 
     * @return parsed AttributeSet.
     */
    public static TextAttribute parseAttributeSet(final Element curAttSet) {
        Attribute foreColorAttr = null;
        Attribute backColorAttr = null;
        Attribute boldAttr = null;
        Attribute italicAttr = null;
        int bold = 0;
        int italic = 0;
        Color foregroundColor = null;
        Color backgroundColor = null;
        TextAttribute retVal = null;
        try {
            if (curAttSet != null) {
                foreColorAttr = curAttSet.getAttribute(FOREGROUND_COLOR);
                try {
                    final StringTokenizer st = new StringTokenizer(foreColorAttr.getValue(), " \t\n\r\f,:;");
                    final int[] rgb = new int[3];
                    rgb[0] = Integer.parseInt(st.nextToken());
                    rgb[1] = Integer.parseInt(st.nextToken());
                    rgb[2] = Integer.parseInt(st.nextToken());
                    foregroundColor = new Color(Display.getCurrent(), rgb[0], rgb[1], rgb[2]);
                } catch (final Exception ex) {
                    logger.info("Exception parsing rgb for foreground color : " + ex);
                }
                backColorAttr = curAttSet.getAttribute(BACKGROUND_COLOR);
                try {
                    final StringTokenizer st = new StringTokenizer(backColorAttr.getValue(), " \t\n\r\f,:;");
                    final int[] rgb = new int[3];
                    rgb[0] = Integer.parseInt(st.nextToken());
                    rgb[1] = Integer.parseInt(st.nextToken());
                    rgb[2] = Integer.parseInt(st.nextToken());
                    if (!((rgb[0] == 255) && (rgb[1] == 255) && (rgb[2] == 255))) {
                        backgroundColor = new Color(Display.getCurrent(), rgb[0], rgb[1], rgb[2]);
                    }
                } catch (final Exception ex) {
                    logger.info("Exception parsing rgb for background color : " + ex);
                }
                boldAttr = curAttSet.getAttribute(BOLD);
                try {
                    bold = Boolean.valueOf(boldAttr.getValue()).booleanValue() ? SWT.BOLD : SWT.NONE;
                } catch (final Exception e) {
                    logger.info("Exception setting bold : " + e);
                }
                italicAttr = curAttSet.getAttribute(ITALIC);
                try {
                    italic = Boolean.valueOf(italicAttr.getValue()).booleanValue() ? SWT.ITALIC : SWT.NONE;
                } catch (final Exception e) {
                    logger.info("Exception setting italic : " + e);
                }
                retVal = new TextAttribute(foregroundColor, backgroundColor, bold | italic);
            }
        } catch (final Exception e) {
            logger.info("Exception parsing Element's Attribute Set : " + e);
            retVal = new TextAttribute(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
        }
        return retVal;
    }

    /**
     * Method to get parsed AttributeSet of Elements.
     * 
     * @return parsed AttributeSet.
     */
    public TextAttribute getSetForElem() {
        return elements;
    }

    /**
     * Method to get parsed AttributeSet of AttributeName.
     * 
     * @return parsed AttributeSet.
     */
    public TextAttribute getSetForAttName() {
        return attributeName;
    }

    /**
     * Method to get parsed AttributeSet of AttributeValue.
     * 
     * @return parsed AttributeSet.
     */
    public TextAttribute getSetForAttValue() {
        return attributeValue;
    }

    /**
     * Method to get parsed AttributeSet of Content.
     * 
     * @return parsed AttributeSet.
     */
    public TextAttribute getSetForContent() {
        return content;
    }

    /**
     * Method to return elements and their corresponding AttributeSet.
     * 
     * @return string.
     */
    @Override
    public String toString() {
        String retVal = "Elements : " + elements + "\n";
        retVal += ("Att Name : " + attributeName + "\n");
        retVal += ("Att Value : " + attributeValue + "\n");
        retVal += ("Content : " + content + "\n");
        return retVal;
    }
}
