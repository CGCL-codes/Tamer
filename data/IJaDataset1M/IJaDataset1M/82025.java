package com.ajah.jsvg;

import com.ajah.jsvg.interfaces.JSVGElement;
import com.ajah.jsvg.interfaces.common.GraphicsElementsEvents;
import com.ajah.jsvg.interfaces.common.LangSpaceAttrs;
import com.ajah.jsvg.interfaces.common.StdAttrs;
import com.ajah.jsvg.interfaces.common.TestAttrs;
import com.ajah.jsvg.interfaces.presentation.PresentationAttributesColor;
import com.ajah.jsvg.interfaces.presentation.PresentationAttributesFillStroke;
import com.ajah.jsvg.interfaces.presentation.PresentationAttributesGraphics;

/**
 * This class contains the code that is common among what W3C calls "Basic Shapes"
 *
 * @author Eric Savage <a href="mailto:esavage@ajah.com">esavage@ajah.com</a>
 * @version 1.0
 * @since JSVG 1.0
 */
public abstract class BasicShape implements JSVGElement, StdAttrs, TestAttrs, LangSpaceAttrs, PresentationAttributesColor, PresentationAttributesFillStroke, PresentationAttributesGraphics, GraphicsElementsEvents {

    protected String name = null;

    protected String classSvg = null;

    protected String style = null;

    protected String id = null;

    protected String xmlBase = null;

    protected String xmlLang = null;

    protected String xmlSpace = null;

    protected String requiredFeatures = null;

    protected String requiredExtensions = null;

    protected String systemLanguage = null;

    protected String onFocusIn = null;

    protected String onFocusOut = null;

    protected String onActivate = null;

    protected String onClick = null;

    protected String onMouseDown = null;

    protected String onMouseUp = null;

    protected String onMouseOver = null;

    protected String onMouseMove = null;

    protected String onMouseOut = null;

    protected String onLoad = null;

    /**
	 * Please note that the name is classSvg because getClass() is a method in Object and it would probably be unwise to override it.
	 *
	 * @param classSvg The class attribute
	 */
    public void setClassSvg(String classSvg) {
        this.classSvg = classSvg;
    }

    /**
     * Please note that the name is classSvg because getClass() is a method in Object and it would probably be unwise to override it.
     *
	 * @return class attribute
	 */
    public String getClassSvg() {
        return classSvg;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyle() {
        return style;
    }

    /**
	 * From the W3C Recommendation:
	 * "Standard XML attribute for assigning a unique name to an element."
	 *
	 * @param id The id attribute
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * From the W3C Recommendation:
	 * "Standard XML attribute for assigning a unique name to an element."
     *
	 * @return The id attribute
	 */
    public String getId() {
        return id;
    }

    /**
	 * From the W3C Recommendation:
	 * "Specifies a base URI other than the base URI of the document or external entity."
	 *
	 * @param xmlBase The xml:base attribute
	 */
    public void setXmlBase(String xmlBase) {
        this.xmlBase = xmlBase;
    }

    /**
	 * From the W3C Recommendation:
	 * "Specifies a base URI other than the base URI of the document or external entity."
     *
	 * @return The xml:base attribute
	 */
    public String getXmlBase() {
        return xmlBase;
    }

    public void setXmlLang(String xmlLang) {
        this.xmlLang = xmlLang;
    }

    public String getXmlLang() {
        return xmlLang;
    }

    public void setXmlSpace(String xmlSpace) {
        this.xmlSpace = xmlSpace;
    }

    public String getXmlSpace() {
        return xmlSpace;
    }

    /**
	 * From the W3C Recommendation:
	 * "The value is a list of feature strings, with the individual values separated by white space. Determines whether all of the named features are supported by the user agent. Only feature strings defined in this section (see below) are allowed. If all of the given features are supported, then the attribute evaluates to true; otherwise, the current element and its children are skipped and thus will not be rendered."
	 *
	 * @param classSvg The class attribute
	 * @see TestAttrs#setRequiredFeatures(String requiredFeatures)
	 */
    public void setRequiredFeatures(String requiredFeatures) {
        this.requiredFeatures = requiredFeatures;
    }

    /**
	 * From the W3C Recommendation:
	 * "The value is a list of feature strings, with the individual values separated by white space. Determines whether all of the named features are supported by the user agent. Only feature strings defined in this section (see below) are allowed. If all of the given features are supported, then the attribute evaluates to true; otherwise, the current element and its children are skipped and thus will not be rendered."
     *
	 * @return The requiredFeatures attribute
	 * @see TestAttrs#getRequiredFeatures()
	 */
    public String getRequiredFeatures() {
        return requiredFeatures;
    }

    /**
	 * From the W3C Recommendation:
	 * "The value is a list of URI references which identify the required extensions, with the individual values separated by white space. Determines whether all of the named extensions are supported by the user agent. If all of the given extensions are supported, then the attribute evaluates to true; otherwise, the current element and its children are skipped and thus will not be rendered."
	 *
	 * @param requiredExtensions The requiredExtensions attribute
	 */
    public void setRequiredExtensions(String requiredExtensions) {
        this.requiredExtensions = requiredExtensions;
    }

    /**
	 * From the W3C Recommendation:
	 * "The value is a list of URI references which identify the required extensions, with the individual values separated by white space. Determines whether all of the named extensions are supported by the user agent. If all of the given extensions are supported, then the attribute evaluates to true; otherwise, the current element and its children are skipped and thus will not be rendered."
     *
	 * @return The requiredExtensions attribute
	 */
    public String getRequiredExtensions() {
        return requiredExtensions;
    }

    /**
	 * From the W3C Recommendation:
	 * "The attribute value is a comma-separated list of language names as defined in [RFC3066]."
	 *
	 * @param systemLanguage The systemLanguage attribute
	 */
    public void setSystemLanguage(String systemLanguage) {
        this.systemLanguage = systemLanguage;
    }

    /**
	 * From the W3C Recommendation:
	 * "The attribute value is a comma-separated list of language names as defined in [RFC3066]."
     *
	 * @return The systemLanguage attribute
	 */
    public String getsystemLanguage() {
        return systemLanguage;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when an element receives focus, such as when a 'text' becomes selected."
	 *
	 * @param onFocusIn The onFocusIn attribute
	 */
    public void setOnFocusIn(String onFocusIn) {
        this.onFocusIn = onFocusIn;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when an element receives focus, such as when a 'text' becomes selected."
	 *
	 * @return The onFocusIn attribute
	 */
    public String getOnFocusIn() {
        return onFocusIn;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when an element loses focus, such as when a 'text' becomes unselected."
	 *
	 * @param onFocusOut The onFocusOut attribute
	 */
    public void setOnFocusOut(String onFocusOut) {
        this.onFocusOut = onFocusOut;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when an element loses focus, such as when a 'text' becomes unselected."
	 *
	 * @return The onFocusOut attribute
	 */
    public String getOnFocusOut() {
        return onFocusOut;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when an element is activated, for instance, thru a mouse click or a keypress. A numerical argument is provided to give an indication of the type of activation that occurs: 1 for a simple activation (e.g. a simple click or Enter), 2 for hyperactivation (for instance a double click or Shift Enter)."
	 *
	 * @param onActivate The onActivate attribute
	 */
    public void setOnActivate(String onActivate) {
        this.onActivate = onActivate;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when an element is activated, for instance, thru a mouse click or a keypress. A numerical argument is provided to give an indication of the type of activation that occurs: 1 for a simple activation (e.g. a simple click or Enter), 2 for hyperactivation (for instance a double click or Shift Enter)."
	 *
	 * @return The onActivate attribute
	 */
    public String getOnActivate() {
        return onActivate;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device button is clicked over an element. A click is defined as a mousedown and mouseup over the same screen location. The sequence of these events is: mousedown, mouseup, click. If multiple clicks occur at the same screen location, the sequence repeats with the detail attribute incrementing with each repetition."
	 *
	 * @param onClick The onClick attribute
	 */
    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device button is clicked over an element. A click is defined as a mousedown and mouseup over the same screen location. The sequence of these events is: mousedown, mouseup, click. If multiple clicks occur at the same screen location, the sequence repeats with the detail attribute incrementing with each repetition."
	 *
	 * @return The onClick attribute
	 */
    public String getOnClick() {
        return onClick;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device button is pressed over an element."
	 *
	 * @param onMouseDown The onMouseDown attribute
	 */
    public void setOnMouseDown(String onMouseDown) {
        this.onMouseDown = onMouseDown;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device button is pressed over an element."
	 *
	 * @return The onMouseDown attribute
	 */
    public String getOnMouseDown() {
        return onMouseDown;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device button is released over an element."
	 *
	 * @param onMouseUp The onMouseUp attribute
	 */
    public void setOnMouseUp(String onMouseUp) {
        this.onMouseUp = onMouseUp;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device button is released over an element."
	 *
	 * @return The onMouseUp attribute
	 */
    public String getOnMouseUp() {
        return onMouseUp;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device is moved onto an element."
	 *
	 * @param onMouseOver The onMouseOver attribute
	 */
    public void setOnMouseOver(String onMouseOver) {
        this.onMouseOver = onMouseOver;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device is moved onto an element."
	 *
	 * @return The onMouseOver attribute
	 */
    public String getOnMouseOver() {
        return onMouseOver;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device is moved while it is over an element."
	 *
	 * @param onMouseMove The onMouseMove attribute
	 */
    public void setOnMouseMove(String onMouseMove) {
        this.onMouseMove = onMouseMove;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device is moved while it is over an element."
	 *
	 * @return The onMouseMove attribute
	 */
    public String getOnMouseMove() {
        return onMouseMove;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device is moved away from an element."
	 *
	 * @param onMouseOut The onMouseOut attribute
	 */
    public void setOnMouseOut(String onMouseOut) {
        this.onMouseOut = onMouseOut;
    }

    /**
	 * From the W3C Recommendation:
	 * "Occurs when the pointing device is moved away from an element."
	 *
	 * @return The onMouseOut attribute
	 */
    public String getOnMouseOut() {
        return onMouseOut;
    }

    /**
	 * From the W3C Recommendation:
	 * "The event is triggered at the point at which the user agent has fully parsed the element and its descendants and is ready to act appropriately upon that element, such as being ready to render the element to the target device. Referenced external resources that are required must be loaded, parsed and ready to render before the event is triggered. Optional external resources are not required to be ready for the event to be triggered."
	 *
	 * @param onLoad The onLoad attribute
	 */
    public void setOnLoad(String onLoad) {
        this.onLoad = onLoad;
    }

    /**
	 * From the W3C Recommendation:
	 * "The event is triggered at the point at which the user agent has fully parsed the element and its descendants and is ready to act appropriately upon that element, such as being ready to render the element to the target device. Referenced external resources that are required must be loaded, parsed and ready to render before the event is triggered. Optional external resources are not required to be ready for the event to be triggered."
	 *
	 * @return The onLoad attribute
	 */
    public String getOnLoad() {
        return onLoad;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return null;
    }

    public void setColor(String color) {
    }

    public String getColorInterpolation() {
        return null;
    }

    public void setColorInterpolation(String colorInterpolation) {
    }

    public String getColorRendering() {
        return null;
    }

    public void setColorRendering(String colorRendering) {
    }
}
