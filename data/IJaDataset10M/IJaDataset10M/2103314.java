package com.halware.eclipseutil.util;

import org.eclipse.jface.dialogs.MessageDialog;
import com.halware.eclipseutil.Activator;

/**
 * Static methods for mucking around with dialogs.
 */
public final class DialogUtil {

    private DialogUtil() {
    }

    public static void showNotImplementedDialog() {
        try {
            MessageDialog.openWarning(null, null, Activator.getResourceString("NotImplementedJob.Msg"));
            return;
        } finally {
        }
    }

    public static void showWarning(String message) {
        try {
            MessageDialog.openWarning(null, null, Activator.getResourceString(message));
            return;
        } finally {
        }
    }
}
