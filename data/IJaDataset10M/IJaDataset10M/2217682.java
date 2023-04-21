package org.slasoi.studio.plugin.preferences;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.slasoi.studio.plugin.support.AuxSlasoi;

public class SlaSoiFrameworkRootFieldEditor extends DirectoryFieldEditor {

    public SlaSoiFrameworkRootFieldEditor(String name, String labelText, Composite parent) {
        super(name, labelText, parent);
    }

    @Override
    protected boolean doCheckState() {
        String error = AuxSlasoi.checkFrameworkRoot(getTextControl().getText());
        if (error != null) {
            setErrorMessage(error);
            return false;
        } else return true;
    }
}
