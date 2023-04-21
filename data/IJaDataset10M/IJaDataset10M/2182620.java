package org.gvsig.gui.beans.colorslideredition;

import java.awt.Color;

/**
 * 
 * @version 26/09/2007
 * @author BorSanZa - Borja S�nchez Zamorano (borja.sanchez@iver.es)
 */
public class ItemColorSlider {

    private double value = 0;

    private double interpolated = 50;

    private Color color = Color.black;

    private boolean visible = true;

    private String name = "";

    private Object tag = null;

    /**
	 * Seleted:
	 * -1: No seleccionado
	 * 1: Seleccionado el item
	 * 2: Seleccionado la interpolacion izquierda
	 */
    private int selected = -1;

    public ItemColorSlider(double value, Color color) {
        setValue(value);
        setColor(color);
    }

    /**
	 * @return the color
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * @param color the color to set
	 */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
	 * @return the interpolation
	 */
    public double getInterpolated() {
        return interpolated;
    }

    /**
	 * @param interpolation the interpolation to set
	 */
    public void setInterpolated(double interpolation) {
        this.interpolated = interpolation;
        if (this.interpolated < 5) this.interpolated = 5;
        if (this.interpolated > 95) this.interpolated = 95;
    }

    /**
	 * @return the value
	 */
    public double getValue() {
        return value;
    }

    /**
	 * @param value the value to set
	 */
    public void setValue(double value) {
        this.value = value;
        if (this.value < 0) this.value = 0;
        if (this.value > 100) this.value = 100;
    }

    /**
	 * @return the visible
	 */
    public boolean isVisible() {
        return visible;
    }

    /**
	 * @param visible the visible to set
	 */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
	 * @return the selected
	 */
    public int getSelected() {
        return selected;
    }

    /**
	 * @param selected the selected to set
	 */
    public void setSelected(int selected) {
        switch(selected) {
            case 1:
            case 2:
                this.selected = selected;
                break;
            default:
                this.selected = -1;
                break;
        }
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * Obtiene el campo extra de cada elemento
	 * @return
	 */
    public Object getTag() {
        return tag;
    }

    /**
	 * Especifica un campo extra a cada elemento
	 * @param tag
	 */
    public void setTag(Object tag) {
        this.tag = tag;
    }
}
