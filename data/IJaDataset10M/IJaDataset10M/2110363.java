package org.swingerproject.accessors.impl;

import java.awt.Dimension;
import org.swingerproject.accessors.IAttributeAccessor;
import org.swingerproject.components.SwingerAdapter;

/**
 * Accessor for width attribute
 * 
 * @author hadrien
 */
public class WidthAccessor implements IAttributeAccessor {

    public void setValue(SwingerAdapter<?> target, String width) {
        Dimension size = target.getSize();
        if (size == null) {
            size = new Dimension();
        }
        size.width = Integer.parseInt(width);
        target.setSize(size);
    }

    public String getValue(SwingerAdapter<?> source) {
        return Integer.toString(source.getSize().width);
    }
}
