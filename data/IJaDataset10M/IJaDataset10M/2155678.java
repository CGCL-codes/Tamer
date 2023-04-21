package de.mpiwg.vspace.languages.preferencepage.navigation.factories;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import de.mpiwg.vspace.generation.navigation.service.NavigationEntry;
import de.mpiwg.vspace.languages.preferencepage.navigation.AVSpaceFieldEditor;
import de.mpiwg.vspace.languages.preferencepage.navigation.ImageVSpaceFieldEditor;
import de.mpiwg.vspace.navigation.fieldeditor.IProperty;

public class ImageVSpaceFieldEditorFactory extends AVSpaceFieldEditorFactory {

    public static final String ID = "VSpaceFieldEditorFactory.image";

    @Override
    public AVSpaceFieldEditor produce(Composite parent, IProperty property, NavigationEntry entry) {
        return new ImageVSpaceFieldEditor(parent, SWT.NONE, property, entry);
    }
}
