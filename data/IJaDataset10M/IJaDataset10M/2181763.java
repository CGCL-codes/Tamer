package org.gvsig.remotesensing.scatterplot.chart;

import java.awt.Color;
import java.awt.Shape;
import java.util.ArrayList;
import org.jfree.data.Range;

/**
 * Clase que representa una Region de interes definida sobre el grafico.
 * 
 * @author Alejandro Mu�oz Sanchez (alejandro.munoz@uclm.es)  
 * @version 11/12/2007
 */
public class ROIChart {

    private ArrayList shapeList = null;

    private ArrayList rangos = null;

    private Color roiColor = null;

    private String name = null;

    private int bandaX = 0;

    private int bandaY = 0;

    /**
	 * Constructor
	 * */
    ROIChart(Color color, String name, int[] bands) {
        roiColor = color;
        shapeList = new ArrayList();
        rangos = new ArrayList();
        bandaX = bands[0];
        bandaY = bands[1];
        this.name = name;
    }

    /**
	 *  A�ade un shape a la lista de shapes de la roi. 
	 *  @shape shape que se a�ade
	 *  @range intervalo  abarcado por el shape  en los ejes x e y
	 * */
    void add(Shape shape, Range[] range) {
        shapeList.add(shape);
        rangos.add((Range[]) range);
    }

    /**
	 *  @return color de la roi
	 * */
    public Color getColor() {
        return roiColor;
    }

    /**
	 * Asigna el color a la roi
	 * @param color asignado
	 * */
    public void setColor(Color color) {
        roiColor = color;
    }

    /**
	 * Asigna el nombre de la roichart
	 * */
    public void setName(String roiName) {
        name = roiName;
    }

    /**
	 *  @return lista con los rangos
	 * */
    public ArrayList getRanges() {
        return rangos;
    }

    /**
	 *  @return lista con los shapes
	 * */
    public ArrayList getShapesList() {
        return shapeList;
    }

    /**
	 * @return nombre de la roiChart
	 * */
    public String getName() {
        return name;
    }

    public int getBandaX() {
        return bandaX;
    }

    public int getBandaY() {
        return bandaY;
    }

    public void setBandX(int bandX) {
        this.bandaX = bandX;
    }

    public void setBandY(int bandY) {
        this.bandaY = bandY;
    }
}
