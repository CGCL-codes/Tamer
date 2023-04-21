package org.jbeans;

import java.util.EventListener;

/**
 * This interface is the event listener interface for conversion
 * events. These events occur when a BeanProperty or similar class
 * is setting a property value and automatically converting the
 * property value to the correct type for the property.
 * The different types of events that this listener watchs are:<br>
 * <li> Before property value conversion when the property is being set
 * <li> After property value conversion when the property is being set
 * <li> After property value conversion when the conversion failed
 * @author  Brian Pontarelli
 */
public interface ConversionListener extends EventListener {

    /**
     * This handle method is called when the property value being set is going
     * to be auto-converted but has not been converted yet. 
     */
    void handlePreConversion(ConversionEvent event);

    /**
     * This handle method is called when the property value being set has just been
     * successfully auto-converted
     */
    void handlePostConversion(ConversionEvent event);

    /**
     * This handle method is called when the property value being set has been 
     * converted and the conversion failed
     */
    void handleFailedConversion(ConversionEvent event);
}
