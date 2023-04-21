package de.uniwue.dltk.textmarker.internal.ui.wizards;

import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.wizards.NewSourceModulePage;
import org.eclipse.dltk.ui.wizards.NewSourceModuleWizard;
import de.uniwue.dltk.textmarker.internal.ui.TextMarkerImages;

public class TextMarkerFileCreationWizard extends NewSourceModuleWizard {

    public static final String ID_WIZARD = "de.uniwue.dltk.textmarker.internal.ui.wizards.TextMarkerFileCreationWizard";

    public TextMarkerFileCreationWizard() {
        setDefaultPageImageDescriptor(TextMarkerImages.DESC_WIZBAN_FILE_CREATION);
        setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
        setWindowTitle("Create TextMarker File");
    }

    @Override
    protected NewSourceModulePage createNewSourceModulePage() {
        return new TextMarkerFileCreationPage();
    }
}
