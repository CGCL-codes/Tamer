package alice.cartago.examples.roomsworld;

import java.io.*;

/**
 *
 * @author michelepiunti
 */
public class trash implements Serializable {

    static final long serialVersionUID = 0;

    private location loc;

    /** Creates a new instance of trash */
    public trash() {
        this.loc = new location();
    }

    /** Creates a new instance of trash */
    public trash(double x, double y) {
        this.loc = new location(x, y);
    }

    /** Creates a new instance of trash */
    public trash(location trl) {
        this.loc = trl;
    }

    public location getLocation() {
        return loc;
    }

    public String toString() {
        return "Trash {\n" + getLocation() + "\nType(" + ")" + "\n }";
    }
}
