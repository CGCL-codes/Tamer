package com.googlecode.gchartjava;

/**
 * Curve super type.
 *
 * @author Julien Chastang (julien.c.chastang at gmail dot com)
 */
interface Curve extends Plot {

    /**
     * Define a fill area color below this plot. Order is important! If you have
     * multiple plots, you may have your fill areas obscure each other. You may
     * have to experiment to get the result you want.
     *
     * @param color
     *            The color of the fill area that will appear below this
     *            particular plot. cannot be null.
     */
    void setFillAreaColor(final Color color);
}
