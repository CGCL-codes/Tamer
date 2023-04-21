package org.nakedobjects.eclipse.shared.util;

import org.eclipse.jface.action.IAction;
import org.nakedobjects.eclipse.shared.Activator;

/**
 * Static methods for mucking around with actions.
 */
public final class ActionUtil {

    private ActionUtil() {
    }

    public static void setupLabelAndImage(final IAction action, final String text, final String image) {
        action.setImageDescriptor(ImageUtil.getImageDescriptor(Activator.getDefault(), image));
        action.setToolTipText(Activator.getResourceString(text));
    }
}
