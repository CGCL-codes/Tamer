package net.face2face.ui.chartpanel;

import java.awt.Color;

/**
 * Simple BarModel always returning the memory activity.
 * @author pdelorme
 */
public class MemoryBarModel implements BarModel {

    private long memoryUnit = 1024 * 1024;

    private Color color = Color.green;

    public String getName() {
        return "Memory";
    }

    /**
	 * memory is in Megabytes.
	 */
    public String getUnits() {
        return "MByte";
    }

    /**
	 * Memory is traditionaly green!
	 */
    public Color getColor() {
        return color;
    }

    /**
	 * Memory is traditionaly green (but you can change it).
	 */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
	 * Returns the total Memory as a String..
	 */
    public String getInformation() {
        return "total Memory =" + (int) (Runtime.getRuntime().totalMemory() / memoryUnit) + " " + getUnits();
    }

    /**
	 * returns the total memory in megabytes.
	 */
    public float getHigherValue() {
        return Runtime.getRuntime().totalMemory() / memoryUnit;
    }

    /**
	 * always return 0.
	 */
    public float getLowerValue() {
        return 0;
    }

    /**
	 * return the memory used (totalMemeory-freeMemory) in megabytes.
	 */
    public float getValue() {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / memoryUnit;
    }
}
