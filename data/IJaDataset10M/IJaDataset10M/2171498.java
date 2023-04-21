package org.fest.swing.driver;

import java.awt.Component;
import javax.swing.JLabel;
import static org.fest.swing.query.JLabelTextQuery.textOf;
import static org.fest.swing.util.Strings.isDefaultToString;

/**
 * Understands the base implementation of all default readers in this package.
 *
 * @author Alex Ruiz
 */
public abstract class BaseValueReader {

    /**
   * Reads the value in the given renderer, or returns <code>null</code> if the renderer belongs to an unknown
   * component type. Internally, this method will call <code>getText()</code> if the given renderer is an instance of
   * <code>{@link JLabel}</code></li>
   * @param renderer the given renderer.
   * @return the value of the given renderer, or <code>null</code> if the renderer belongs to an unknown component
   *         type.
   */
    protected final String valueFrom(Component renderer) {
        if (renderer instanceof JLabel) return textOf((JLabel) renderer);
        return null;
    }

    /**
   * Returns the <code>toString</code> value from the given object. If the given object does not implement 
   * <code>toString</code>, this method will return <code>null</code>.
   * @param fromModel the given object.
   * @return the <code>toString</code> value from the given object, or <code>null</code> if the given object does not
   * implement <code>toString</code>.
   */
    protected final String valueFrom(Object fromModel) {
        if (fromModel == null) return null;
        String text = fromModel.toString();
        if (!isDefaultToString(text)) return text;
        return null;
    }
}
