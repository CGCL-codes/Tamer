package de.definitives.creoleconverter.document;

import java.util.ArrayList;
import java.util.List;

/**
 * Objects of this class can contain child components. This class does not
 * represent a markup element by itself.
 *
 * @author Christian Helmbold
 */
public abstract class Container implements Component {

    protected List<Component> childs = new ArrayList<Component>();

    public void add(Component child) {
        childs.add(child);
    }

    public List<Component> getChilds() {
        return childs;
    }
}
