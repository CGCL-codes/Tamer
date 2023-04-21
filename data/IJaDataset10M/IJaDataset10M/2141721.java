package org.jmesa.core.message;

import java.io.Serializable;

/**
 * @since 2.0
 * @author Jeff Johnston
 */
public interface Messages extends Serializable {

    /**
     * Get the resource property.
     */
    public String getMessage(String code);

    /**
     * Get the resource property.
     */
    public String getMessage(String code, Object[] args);
}
