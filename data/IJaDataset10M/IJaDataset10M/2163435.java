package org.chernovia.lib.graphics;

/**
 * Write a description of interface ImageGenerator here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public interface IntMatrixSource {

    public int getMatrixWidth();

    public int getMatrixHeight();

    public int getMinMatrixValue();

    public int getMaxMatrixValue();

    public int[][] getMatrix();
}
