package org.easygen.ui.util;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author Mektoub
 * Created on 20 mars 07
 */
public class SWTUtils {

    /**
     * @param pUseDefaultsButton
     * @return
     */
    public static boolean isSelected(Button pButton) {
        if (pButton != null && pButton.getSelection()) return true;
        return false;
    }

    /**
     * @param control
     * @return
     */
    public static boolean isEmpty(Control control) {
        return !isNotEmpty(control);
    }

    /**
     * @param control
     * @return
     */
    public static boolean isNotEmpty(Control control) {
        if (control instanceof Text) return SWTUtils.isNotEmpty((Text) control); else if (control instanceof Combo) return SWTUtils.isNotEmpty((Combo) control); else if (control instanceof Combo) return SWTUtils.isNotEmpty((Combo) control);
        return true;
    }

    /**
     * @param pProjectNameField
     * @return
     */
    public static boolean isEmpty(Text pField) {
        return !isNotEmpty(pField);
    }

    /**
     * @param pProjectNameField
     * @return
     */
    public static boolean isNotEmpty(Text pField) {
        if (pField == null || pField.getText() == null || pField.getText().trim().length() == 0) return false;
        return true;
    }

    /**
     * @param pProjectNameField
     * @return
     */
    public static boolean isEmpty(Label pField) {
        return !isNotEmpty(pField);
    }

    /**
     * @param pProjectNameField
     * @return
     */
    public static boolean isNotEmpty(Label pField) {
        if (pField == null || pField.getText() == null || pField.getText().trim().length() == 0) return false;
        return true;
    }

    /**
	 * @param args
	 */
    public static boolean isEmpty(Combo combo) {
        return !isNotEmpty(combo);
    }

    /**
	 * @param args
	 */
    public static boolean isNotEmpty(Combo combo) {
        if (combo == null || combo.getText() == null || combo.getText().trim().length() == 0) return false;
        return true;
    }

    /**
     * @param pControl
     * @return
     */
    public static String getText(Control pControl) {
        if (pControl instanceof Text) return ((Text) pControl).getText();
        if (pControl instanceof Combo) return ((Combo) pControl).getText();
        if (pControl instanceof Label) return ((Label) pControl).getText();
        return "";
    }

    /**
     * @param pShell
     * @param pJar_filter
     * @return
     */
    public static String createFileDialog(Shell pShell, String[] pFilter) {
        FileDialog dialog = new FileDialog(pShell, SWT.OPEN);
        dialog.setFilterExtensions(pFilter);
        return dialog.open();
    }
}
