package net.sourceforge.sdm.util;

/**
 */
public class ColumnProperty {

    private static final long serialVersionUID = 1158663692777367742L;

    public String name = "";

    public int width;

    public int position;

    public ColumnProperty() {
    }

    public ColumnProperty(String name, int width, int position) {
        this.name = name;
        this.width = width;
        this.position = position;
    }

    /**
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * @return
	 */
    public int getPosition() {
        return position;
    }

    /**
	 * @return
	 */
    public int getWidth() {
        return width;
    }

    /**
	 * @param string
	 */
    public void setName(String string) {
        name = string;
    }

    /**
	 * @param i
	 */
    public void setPosition(int i) {
        position = i;
    }

    /**
	 * @param i
	 */
    public void setWidth(int i) {
        width = i;
    }
}
