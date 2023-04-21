package net.sf.magicmap.client.utils;

import java.awt.Component;

/**
 * Created by IntelliJ IDEA.
 * User: Jan
 * Date: 11.10.2006
 * Time: 18:06:18
 * To change this template use File | Settings | File Templates.
 */
public class ComponentFinder {

    public ComponentFinder() {
    }

    /**
     * Finds a parent component of a given type.
     * 
     * @param component the childcomponent
     * @param clazz the class of the child components parent to search for.
     * @return null or the first parent of "component" with class "clazz"
     */
    public Component getParentByClass(final Component component, final Class clazz) {
        Component c = component;
        while (c != null) {
            c = c.getParent();
            if (clazz.isAssignableFrom(c.getClass())) return c;
        }
        return null;
    }
}
