package de.fzj.unicore.plugins.cpmd.converter;

/** 
 *
 * @author  huber
 * @version 
 */
public class Atom {

    double x, y, z;

    String type;

    /** Creates new Atom */
    public Atom(double x, double y, double z, String type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }
}
