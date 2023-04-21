package com.farukcankaya.simplemodel.ui;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.ui.AbstractDLTKUILanguageToolkit;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.jface.preference.IPreferenceStore;
import com.farukcankaya.simplemodel.Activator;

public class SimplemodelUILanguageToolkit extends AbstractDLTKUILanguageToolkit {

    public SimplemodelUILanguageToolkit() {
    }

    @Override
    public IDLTKLanguageToolkit getCoreToolkit() {
        return SimplemodelLanguageToolkit.INSTANCE;
    }

    @Override
    public IPreferenceStore getPreferenceStore() {
        return Activator.getDefault().getPreferenceStore();
    }

    @Override
    public String getEditorId(Object inputElement) {
        return super.getEditorId(inputElement);
    }

    @Override
    public ScriptSourceViewerConfiguration createSourceViewerConfiguration() {
        return new SimplemodelSourceViewerConfiguration(getTextTools().getColorManager(), getPreferenceStore(), null, getPartitioningId());
    }
}
