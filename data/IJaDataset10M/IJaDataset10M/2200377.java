package com.volantis.mcs.css.version;

import com.volantis.styling.properties.StyleProperty;
import com.volantis.shared.iteration.IterationAction;
import com.volantis.mcs.themes.StyleShorthand;
import com.volantis.mcs.themes.StyleSyntax;
import java.util.Map;

/**
 * The main entry point for an object structure which describes which parts
 * of MCS Themes a particular version of CSS supports.
 *
 * @mock.generate
 */
public interface CSSVersion {

    /**
     * Return the CSS property for the property provided.
     * 
     * The CSS property describes which parts of the MCS property is supported
     * in this version of CSS.
     *
     * @param property the MCS property to query for.
     * @return the CSS property which describes which parts of the MCS
     *      property supplied is supported.
     */
    CSSProperty getProperty(StyleProperty property);

    /**
     * Return the CSS property for the property name provided.
     * 
     * The CSS property describes which parts of the MCS property is supported
     * in this version of CSS.
     *
     * @param propertyName the MCS property name to query for.
     * @return the CSS property which describes which parts of the MCS
     *      property supplied is supported.
     */
    CSSProperty getProperty(String propertyName);

    /**
     * Returns name of CSS Property which is an alias for 
     * the specified MCS property in this version of CSS.
     *
     * @param property the MCS property to query for
     * @return name of CSS property or null if no alias supported
     */
    String getPropertyAlias(StyleProperty property);

    /**
     * Returns true if the shorthand is supported in this
     * version of CSS.
     *
     * @param shorthand the shorthand.
     * @return true if it shorthand is supported.
     */
    boolean supportsShorthand(StyleShorthand shorthand);

    /**
     * Returns true if the shorthand is supported in this
     * version of CSS.
     *
     * @param shorthandName the shorthand name.
     * @return true if it shorthand is supported.
     */
    boolean supportsShorthand(String shorthandName);

    /**
     * Returns true if the pseudo selector id supplied is supported in this
     * version of CSS.
     *
     * @param selectorId the id of the pseudo selector.
     * @return true if the id is supported.
     */
    boolean supportsPseudoSelectorId(String selectorId);

    /**
     * Returns true if the syntax is supported in this version of CSS.
     *
     * @param syntax    which may be supported
     * @return true if the syntax is supported, false otherwise
     */
    boolean supportsSyntax(StyleSyntax syntax);

    IterationAction iterate(CSSPropertyIteratee iteratee);

    /**
     * Return a Map (may be empty, but will NOT be <code>null</code>) keyed by
     * element names that can require remapping since the device doesn't
     * support specifying a style via CSS - instead, an attribute on an element
     * should be used.
     *
     * Each value in the Map is another map, keyed by attribute name, with the
     * value being the corresponding expression used to create the attribute
     * value.
     *
     * Changes to the returned Map will be be reflected in the underlying field
     * in a CSSVersion instance.
     *
     * @return a Map - not null.
     */
    Map getRemappableElements();

    /**
     * Set the map of remappable elements. The expected structure of the
     * contents of the map is defined in {@link #getRemappableElements()}.
     *
     * @param remappableElements a Map - not null.
     * @see #getRemappableElements()
     */
    void setRemappableElements(Map remappableElements);
}
