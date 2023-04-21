package com.volantis.styling.impl.engine;

import com.volantis.mcs.themes.StyleValues;
import com.volantis.styling.sheet.CompiledStyleSheet;
import java.util.List;

/**
 * Encapsulates all the information relating to an element that is being
 * processed by the styling engine.
 *
 * @mock.generate
 */
public interface ElementStackFrame {

    /**
     * Get the namespace.
     *
     * @return The namespace.
     */
    String getNamespace();

    /**
     * Set the namespace.
     *
     * @param namespace The new namespace.
     */
    void setNamespace(String namespace);

    /**
     * Get the local name.
     *
     * @return The local name.
     */
    String getLocalName();

    /**
     * Set the local name.
     *
     * @param localName The new local name.
     */
    void setLocalName(String localName);

    /**
     * Get the child count.
     *
     * @return The child count.
     */
    int getChildCount();

    /**
     * Set the child count.
     *
     * @param childCount The new child count.
     */
    void setChildCount(int childCount);

    /**
     * Get the style values.
     *
     * @return The style values.
     */
    StyleValues getValues();

    /**
     * Set the style values.
     *
     * @param values The new style values.
     */
    void setValues(StyleValues values);

    /**
     * Increment the child count.
     */
    void incrementChildCount();

    /**
     * Set the id of the element being processed by the styling engine
     * @param elementId
     */
    void setElementId(int elementId);

    /**
     * Get the id of the element being processed by the styling engine
     * @return the element id.
     */
    int getElementId();

    /**
     * Set the style sheet which represents any inline style value applied to
     * the element.
     * @param styleSheet
     */
    void setInlineStyleSheet(CompiledStyleSheet styleSheet);

    /**
     * Get the style sheet which represents any inline style value applied to
     * the element.
     * @return styleSheet
     */
    CompiledStyleSheet getInlineStyleSheet();

    /**
     * Get the style sheet list which represents any nested styles applied to
     * the element.
     * @return styleSheetList
     */
    List getNestedStyleSheets();

    /**
     * Add the style sheet to nested style sheet list
     * @param  styleSheet
     */
    void addNestedStyleSheet(CompiledStyleSheet styleSheet);
}
