package org.formaria.editor.project.events;

import org.formaria.editor.project.pages.components.PropertiesData;
import org.formaria.editor.project.pages.components.PropertyHelper;

/**
 * A listener for component properties
 * <p>Copyright: Copyright (c) Formaria Ltd., 2001-2004</p>
 * @version 1.0
 */
public interface PropertiesListener {

    /**
   * Get the property data from a helper.
   * @param helper
   * @return
   */
    public PropertiesData[] getProperties(PropertyHelper helper);
}
