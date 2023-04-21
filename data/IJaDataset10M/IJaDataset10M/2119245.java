package org.outerj.pollo.xmleditor.displayspec;

import org.outerj.pollo.xmleditor.ElementColorIcon;
import org.outerj.pollo.xmleditor.exception.PolloException;
import org.outerj.pollo.xmleditor.util.NestedNodeMap;
import org.outerj.pollo.util.ColorFormat;
import org.w3c.dom.Element;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * An implementation of the IDisplaySpecification that can be used
 * for any XML file.
 *
 * It can both generate colors on the fly or automatically assign colors.
 *
 * @author Bruno Dumon
 */
public class GenericDisplaySpecification implements IDisplaySpecification {

    /** Default color for elements. */
    protected Color defaultColor;

    /** Color to use as the background of the XmlEditor. */
    protected Color backgroundColor = new Color(235, 235, 235);

    /** Contains the instances of the ElementSpec class */
    protected NestedNodeMap elementSpecs = new NestedNodeMap();

    /** Indicates if this implementation should randomly assign colors. */
    protected boolean useRandomColors = false;

    protected int treeType;

    /** List of colors to use for elements. The list is stolen from somewhere
     * in koffice.
     */
    protected static Color[] colors = { new Color(229, 229, 229), new Color(255, 255, 224), new Color(255, 239, 213), new Color(238, 221, 130), new Color(135, 206, 250), new Color(143, 188, 143), new Color(204, 204, 204), new Color(240, 255, 255), new Color(255, 235, 205), new Color(211, 211, 211), new Color(255, 160, 122), new Color(218, 112, 214), new Color(178, 178, 178), new Color(248, 248, 255), new Color(250, 235, 215), new Color(255, 182, 193), new Color(135, 206, 235), new Color(102, 205, 170), new Color(153, 153, 153), new Color(240, 255, 240), new Color(255, 228, 225), new Color(176, 224, 230), new Color(210, 180, 140), new Color(255, 127, 80), new Color(255, 245, 238), new Color(230, 230, 250), new Color(127, 255, 212), new Color(238, 130, 238), new Color(154, 205, 50), new Color(240, 248, 255), new Color(255, 228, 196), new Color(216, 191, 216), new Color(244, 164, 96), new Color(218, 165, 32), new Color(255, 248, 220), new Color(255, 228, 181), new Color(173, 216, 230), new Color(233, 150, 122), new Color(72, 209, 204), new Color(255, 240, 245), new Color(255, 222, 173), new Color(152, 251, 152), new Color(189, 183, 107), new Color(188, 143, 143), new Color(253, 245, 230), new Color(255, 218, 185), new Color(255, 215, 0), new Color(127, 255, 0), new Color(219, 112, 147), new Color(245, 245, 245), new Color(238, 232, 170), new Color(173, 255, 47), new Color(169, 169, 169), new Color(0, 250, 154), new Color(255, 255, 240), new Color(255, 250, 205), new Color(245, 222, 179), new Color(176, 196, 222), new Color(124, 252, 0), new Color(255, 99, 71), new Color(255, 250, 250), new Color(224, 255, 255), new Color(220, 220, 220), new Color(255, 105, 180), new Color(0, 255, 127), new Color(245, 255, 250), new Color(250, 250, 210), new Color(240, 230, 140), new Color(144, 238, 144), new Color(221, 160, 221), new Color(250, 128, 114), new Color(205, 133, 63), new Color(255, 250, 240), new Color(250, 240, 230), new Color(175, 238, 238), new Color(190, 190, 190), new Color(240, 128, 128), new Color(100, 149, 237), new Color(245, 245, 220), new Color(255, 192, 203), new Color(222, 184, 135), new Color(64, 224, 208), new Color(132, 112, 255) };

    protected static int numberOfColors = colors.length;

    protected int colorPointer = 0;

    public void init(HashMap initParams) throws PolloException {
        defaultColor = new Color(255, 255, 255);
        String useRandomColorsParam = (String) initParams.get("use-random-colors");
        if (useRandomColorsParam != null && useRandomColorsParam.equals("true")) useRandomColors = true; else {
            String fixedColorParam = (String) initParams.get("fixed-color");
            if (fixedColorParam != null) {
                try {
                    defaultColor = ColorFormat.parseHexColor(fixedColorParam);
                } catch (Exception e) {
                    throw new PolloException("fixed-color parameter not correct: " + e.getMessage(), e);
                }
            }
        }
        String backgroundColorParam = (String) initParams.get("background-color");
        if (backgroundColorParam != null) {
            try {
                defaultColor = ColorFormat.parseHexColor(backgroundColorParam);
            } catch (Exception e) {
                throw new PolloException("background-color parameter not correct: " + e.getMessage(), e);
            }
        }
        String treeType = (String) initParams.get("treetype");
        if (treeType != null && treeType.equals("classic")) this.treeType = IDisplaySpecification.CLASSIC_TREE; else this.treeType = IDisplaySpecification.POLLO_TREE;
    }

    protected void addElementSpec(ElementSpec elementSpec) {
        elementSpecs.put(elementSpec.nsUri, elementSpec.localName, elementSpec);
    }

    public ElementSpec getElementSpec(String uri, String localName, Element parent) {
        ElementSpec elementSpec = (ElementSpec) elementSpecs.get(uri, localName);
        if (elementSpec == null) {
            elementSpec = new ElementSpec();
            elementSpec.nsUri = uri;
            elementSpec.localName = localName;
            elementSpec.attributesToShow = new ArrayList();
            if (useRandomColors) {
                elementSpec.backgroundColor = colors[colorPointer % numberOfColors];
                colorPointer++;
            } else {
                elementSpec.backgroundColor = defaultColor;
            }
            elementSpec.textColor = Color.black;
            elementSpec.icon = new ElementColorIcon(elementSpec.backgroundColor);
            addElementSpec(elementSpec);
        }
        return elementSpec;
    }

    public ElementSpec getElementSpec(Element element) {
        return getElementSpec(element.getNamespaceURI(), element.getLocalName(), null);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public int getTreeType() {
        return treeType;
    }
}
