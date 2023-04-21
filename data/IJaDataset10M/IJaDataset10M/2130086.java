package edu.gsbme.geometrykernel.data.brep.dimension;

import java.util.ArrayList;
import edu.gsbme.geometrykernel.kobject;

/**
 * This class describes the dimension information including axis names and length etc.
 * @author David
 *
 */
public class dimension extends kobject {

    public ArrayList<dim_axis> axis;

    public dimension(String id) {
        super(id);
        axis = new ArrayList<dim_axis>();
    }

    public dimension clone() {
        dimension clone = new dimension(this.getID());
        for (int i = 0; i < axis.size(); i++) {
            clone.axis.add(axis.get(i).clone());
        }
        return clone;
    }
}
