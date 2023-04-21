package org.horen.ui.preferences;

import org.eclipse.core.runtime.IAdaptable;

/**
 * HACK: We use this class to open the property dialog to show our
 * preference pages. Especially, this is only for the database preference
 * page.
 * 
 * @author Steffen
 */
public class PropertyPreferenceDummy2 implements IAdaptable {

    @Override
    public Object getAdapter(Class adapter) {
        return null;
    }
}
