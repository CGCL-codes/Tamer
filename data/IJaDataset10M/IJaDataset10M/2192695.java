package org.axsl.area;

/**
 * The Area containing the region-body.
 */
public interface RegionBodyRefArea extends Area {

    /**
     * Returns the main reference-area.
     * @return The main reference-area for this body region.
     */
    MainRefArea getMainRefArea();

    /**
     * Returns the before-float reference-area.
     * @return The before-float reference-area for this body region, or null
     * if there is none.
     */
    BeforeFloatRefArea getBeforeFloatRefArea();

    /**
     * Returns the footnote reference-area.
     * @return The footnote reference-area for this body region, or null if
     * there is none.
     */
    FootnoteRefArea getFootnoteRefArea();
}
