package org.galagosearch.tupleflow.typebuilder;

/**
 *
 * @author trevor
 */
public class OrderedFieldSpecification {

    public OrderedFieldSpecification(Direction direction, String name) {
        this.direction = direction;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Direction getDirection() {
        return direction;
    }

    protected Direction direction;

    protected String name;
}
