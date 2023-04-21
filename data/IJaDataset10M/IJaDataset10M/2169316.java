package de.uni_leipzig.lots.webfrontend.formbeans.property;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;

/**
 * @author Alexander Kiel
 * @version $Id: StringProperty.java,v 1.8 2007/10/23 06:29:49 mai99bxd Exp $
 */
public interface StringProperty extends Property {

    /**
     * Returns the value of this property.
     *
     * @return the value of this property or <tt>null</tt> if the property is unset
     */
    @Nullable
    String getValue();

    /**
     * Sets the value of this property.
     * <p/>
     * Usually the value comes from a HTTP request parameter. To support <tt>null</tt> semantics, the value
     * will be trimmed and set to <tt>null</tt> if it represents an empty string. This means: if you call
     * <tt>setValue("")</tt>, <tt>getValue()</tt> will return <tt>null</tt>.
     *
     * @param value
     */
    void setValue(@Nullable String value);
}
