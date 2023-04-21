package com.pentagaia.eclipse.sgs.internal.util;

import org.eclipse.core.runtime.CoreException;
import com.pentagaia.eclipse.sgs.api.ISgsMutableApplicationConfig;

/**
 * A helper interface used by the ui
 * 
 * @author mepeisen
 */
public interface IPropertyUiDefinition {

    /**
     * tests if this property is protected
     * @return {@code true} if the property is protected
     */
    boolean isProtected();

    /**
     * tests if this property is read only
     * @return {@code true} if this property is read only
     */
    boolean isReadOnly();

    /**
     * Stores the property value
     * @param config project configuration
     * @param value the ui value
     * @throws CoreException
     */
    void storeValue(final ISgsMutableApplicationConfig config, final String value) throws CoreException;

    /**
     * Returns the property value
     * @param config project configuration
     * @return value presented at ui
     */
    String getValue(final ISgsMutableApplicationConfig config);
}
