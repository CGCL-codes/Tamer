package net.sourceforge.tile3d.view.writeFile.core.model;

public class X3dDoor {

    float width;

    float pos;

    float height;

    /**
	 * @return Returns the width.
	 */
    public float getWidth() {
        return width;
    }

    /**
	 * @param p_width
	 *            The width to set.
	 */
    public void setWidth(float p_width) {
        width = p_width;
    }

    /**
	 * @return Returns the height.
	 */
    public float getHeight() {
        return height;
    }

    /**
	 * @param p_height
	 *            The height to set.
	 */
    public void setHeight(float p_height) {
        height = p_height;
    }

    /**
	 * @return Returns the pos.
	 */
    public float getPos() {
        return pos;
    }

    /**
	 * @param p_pos
	 *            The pos to set.
	 */
    public void setPos(float p_pos) {
        pos = p_pos;
    }

    /**
         * toString methode: creates a String representation of the object
         * @return the String representation
         * @author info.vancauwenberge.tostring plugin
    
         */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("X3dDoor[");
        buffer.append("width = ").append(width);
        buffer.append(", pos = ").append(pos);
        buffer.append(", height = ").append(height);
        buffer.append("]");
        return buffer.toString();
    }
}
