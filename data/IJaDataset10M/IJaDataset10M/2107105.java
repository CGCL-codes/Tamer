package de.offis.example_applications.sightseeing4u.common.style;

import component_interfaces.semanticmm4u.realization.compositor.realization.IOperator;
import component_interfaces.semanticmm4u.realization.media_elements_connector.provided.IMediaElementsAccessor;
import de.offis.semanticmm4u.global.Debug;

/**
 * This is no style, the class simply returns the original root operator.
 * Note: This class is not public, because the user should create the style classes
 * by the StyleManager.
 * 
 * @see StyleManager.
 */
class NoStyle implements ISS4UStyle {

    public IOperator applySightStyle(IOperator rootOperator, boolean isPDA, IMediaElementsAccessor mediaAccessor) {
        Debug.println("Using: NoStyle");
        return rootOperator;
    }

    public IOperator applyMapStyle(IOperator rootOperator, boolean isPDA, IMediaElementsAccessor mediaAccessor) {
        Debug.println("Using: NoStyle");
        return rootOperator;
    }
}
