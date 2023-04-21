package com.volantis.mcs.management.tracking;

/**
 * Provide a method by which information about a canvas can be obtained.
 * @volantis-api-include-in PublicAPI
 */
public interface CanvasDetails {

    /**
     * Return the type of the Canvas to which the details are related.
     */
    public CanvasType getCanvasType();

    /**
     * Return hte name of the Theme associated with this canvas.
     */
    public String getThemeName();

    /**
     * Return the name of the Layout associated with this Canvas
     */
    public String getLayoutName();

    /**
     * Return the tile of this canvas.
     */
    public String getTitle();
}
