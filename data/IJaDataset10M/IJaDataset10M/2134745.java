package com.volantis.mcs.protocols.html.css.emulator.renderer;

import com.volantis.mcs.dom.Element;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationAttributeValueRenderer;
import com.volantis.mcs.protocols.css.emulator.renderer.StyleEmulationPropertyRenderer;
import com.volantis.mcs.themes.StyleValue;
import com.volantis.synergetics.log.LogDispatcher;

/**
 * Renders the 'padding' style property as stylistic markup for HTML 3.2.
 * <p>
 * This will try and render a callpadding attribute containing a pixel length 
 * onto the containing table of a td. Any other elements will be ignored.
 */
public class HTML3_2PaddingEmulationPropertyRenderer implements StyleEmulationPropertyRenderer {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(HTML3_2PaddingEmulationPropertyRenderer.class);

    /**
     * Renders pixel values for us.
     */
    private static final StyleEmulationAttributeValueRenderer PIXELS_RENDERER = new HTML3_2PixelsEmulationAttributeValueRenderer();

    public void apply(Element element, StyleValue value) {
        if ("td".equals(element.getName())) {
            while ((element = element.getParent()) != null) {
                if ("table".equals(element.getName())) {
                    if (element.getAttributeValue("cellpadding") == null) {
                        String renderedPadding = PIXELS_RENDERER.render(value);
                        if (renderedPadding != null) {
                            element.setAttribute("cellpadding", renderedPadding);
                        }
                    } else {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Ignoring css padding '" + value + "' " + "on td - table already has cellpadding " + element.getAttributeValue("cellpadding"));
                        }
                    }
                    return;
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Ignoring css padding '" + value + "' " + "on td - no containing table found");
            }
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Ignoring css padding '" + value + "' " + "on " + element.getName() + " - padding only rendered on " + "td's");
            }
        }
    }
}
