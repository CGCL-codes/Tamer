package geovista.colorbrewer;

import java.awt.Color;
import geovista.common.color.BivariatePalette;

/**
 GeoVISTA Center (Penn State, Dept. of Geography)
 Java source file for the class BivariateScheme
 Copyright (c), 2004, GeoVISTA Center
 All Rights Reserved.
 Original Author: Biliang Zhou
 * 
 */
public class BivariateScheme implements BivariatePalette {

    String name;

    int hclass;

    int vclass;

    Color[][] colorinRGB;

    public static final int maxBivariateClasses = 15;

    public BivariateScheme() {
        colorinRGB = new Color[maxBivariateClasses][maxBivariateClasses];
    }

    public BivariateScheme(String name, int hclass, int vclass, Color[][] colorinRGB) {
        this.name = name;
        this.hclass = hclass;
        this.vclass = vclass;
        this.colorinRGB = colorinRGB;
    }

    public void overWrite(BivariateScheme scheme1) {
        scheme1.name = this.name;
        scheme1.hclass = this.hclass;
        scheme1.vclass = this.vclass;
        for (int i = 0; i < vclass; i++) {
            for (int j = 0; j < hclass; j++) {
                scheme1.colorinRGB[i][j] = this.colorinRGB[i][j];
            }
        }
    }

    public String getName() {
        return this.name;
    }

    public int getType() {
        int bivariateType = 0;
        if (this.name == "quaseqbellcurve1" || this.name == "quaseqbellcurve2" || this.name == "quaseqcone1") {
            bivariateType = QUALITATIVE_SEQUENTIAL;
        }
        if (this.name == "seqquabellcurve1" || this.name == "seqquabellcurve2" || this.name == "seqquacone1") {
            bivariateType = SEQUENTIAL_QUALITATIVE;
        }
        if (this.name == "divdivbellcurve1" || this.name == "divdivbellcurve2") {
            bivariateType = DIVERGING_DIVERGING;
        }
        if (this.name == "divseqellipsedown1" || this.name == "divseqtrapezoid1" || this.name == "divseqgrid1") {
            bivariateType = DIVERGING_SEQUENTIAL;
        }
        if (this.name == "seqdivellipsedown1" || this.name == "seqdivtrapezoid1" || this.name == "seqdivgrid1") {
            bivariateType = SEQUENTIAL_DIVERGING;
        }
        if (this.name == "seqseqgraydiamond1" || this.name == "seqseqgraydiamond2" || this.name == "seqseqnongraydiamond1") {
            bivariateType = SEQUENTIAL_SEQUENTIAL;
        }
        return bivariateType;
    }

    public Color[][] getColors(int cols, int rows) {
        Color[][] bivariatescheme = new Color[cols][rows];
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                bivariatescheme[i][j] = this.colorinRGB[j][i];
            }
        }
        return bivariatescheme;
    }
}
