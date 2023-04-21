package de.knowwe.core.renderer;

import de.d3web.we.kdom.rendering.StyleRenderer;

/**
 * Adds functionality to render a specific Background Color. Used especially in
 * Rendering XCLRelations. Needed because RelationWeight shouldnt be highlighted
 * with Relation.
 *
 * Note 1: Adds an id in the span. So Highlighting the text is still possible.
 * KDomSectionHighlightingRenderer offers an id too. Consider using this one. If
 * you want correct highlighting look up "highlightingNode"-method in KnowWE.js.
 * There you can specify in which depth you can find the id tag for the marker.
 * For Example for XCLRelations it is the 3. span.
 *
 * Note 2: Can be used instead of FontColorRenderer. Just set the background
 * argument to null.
 *
 * @author Johannes Dienst
 */
public class FontColorBackgroundRenderer extends StyleRenderer {

    /**
	 * Use static Method getRenderer() instead!
	 *
	 * @param color
	 * @param background
	 */
    public FontColorBackgroundRenderer(String color, String background) {
        super((color == null ? "" : color + ";") + (background == null ? "" : "background-color:" + background));
    }

    /**
	 * Use static Method getRenderer() instead!
	 *
	 * @param color
	 * @param background
	 */
    public FontColorBackgroundRenderer(String cssClass, String color, String background) {
        super(cssClass, (color == null ? "" : color + ";") + (background == null ? "" : "background-color:" + background));
    }

    /**
	 * When normal functionality as in FontColorRenderer: Set background null;
	 *
	 * @param color
	 * @param background
	 * @return
	 */
    public static FontColorBackgroundRenderer getRenderer(String color, String background) {
        return new FontColorBackgroundRenderer(color, background);
    }

    /**
	 * Allows for setting the class attribute.
	 *
	 * @param cssClass
	 * @param color
	 * @param background
	 * @return
	 */
    public static FontColorBackgroundRenderer getRenderer(String cssClass, String color, String background) {
        return new FontColorBackgroundRenderer(cssClass, color, background);
    }
}
