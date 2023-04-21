package net.sf.jdpa.cg.model;

import java.util.List;
import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Andreas Nilsson
 */
public class NewInitializedArray implements Expression {

    private String componentType;

    private Expression[] elements;

    public NewInitializedArray(String componentType, Expression[] elements) {
        if (componentType == null) {
            throw new IllegalArgumentException("Component type can't be null");
        } else if (elements == null) {
            throw new IllegalArgumentException("Elements can't be null");
        } else {
            this.componentType = componentType;
            this.elements = new Expression[elements.length];
            for (int i = 0; i < elements.length; i++) {
                if (elements[i] == null) {
                    throw new IllegalArgumentException("Element " + i + " can't be null");
                }
                this.elements[i] = elements[i];
            }
        }
    }

    public String getComponentType() {
        return componentType;
    }

    public Expression[] getElements() {
        Expression[] copy = new Expression[elements.length];
        System.arraycopy(elements, 0, copy, 0, elements.length);
        return copy;
    }

    public ConstructType getConstructType() {
        return ConstructType.NEW_INITIALIZED_ARRAY;
    }

    public int hashCode() {
        return componentType.hashCode() ^ Arrays.hashCode(elements);
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof NewInitializedArray)) {
            return false;
        } else {
            NewInitializedArray array = (NewInitializedArray) obj;
            return array.componentType.equals(componentType) && Arrays.equals(array.elements, elements);
        }
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("new ").append(componentType).append("[] {");
        for (int i = 0; i < elements.length; i++) {
            buffer.append(elements[i]);
            if (i != elements.length - 1) {
                buffer.append(", ");
            }
        }
        buffer.append("}");
        return buffer.toString();
    }
}
