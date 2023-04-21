package org.ietr.preesm.mapper.model;

import net.sf.dftools.algorithm.model.AbstractEdgePropertyType;

/**
 * Extending propertyType
 * 
 * @author mpelcat
 */
public class EdgePropertyType extends AbstractEdgePropertyType<Integer> {

    int time;

    public EdgePropertyType(int time) {
        super();
        this.time = time;
    }

    @Override
    public AbstractEdgePropertyType<Integer> clone() {
        return null;
    }

    @Override
    public int intValue() {
        return time;
    }

    @Override
    public String toString() {
        return String.format("%d", time);
    }
}
