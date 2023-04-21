package org.eclipse.dltk.freemarker.internal.ui.preferences;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.freemarker.core.FreemarkerLanguageToolkit;
import org.eclipse.dltk.ui.preferences.BuildPathsPropertyPage;
import org.eclipse.dltk.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.ui.IWorkbenchPropertyPage;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;

/**
 * Freemarker Build Path Project Property page.
 * 
 */
public class FreemarkerBuildPathPropertyPage extends BuildPathsPropertyPage implements IWorkbenchPropertyPage {

    public FreemarkerBuildPathPropertyPage() {
    }

    @Override
    protected BuildpathsBlock createBuildPathBlock(IWorkbenchPreferenceContainer pageContainer) {
        return new FreemarkerBuildPathsBlock(new BusyIndicatorRunnableContext(), this, getSettings().getInt(INDEX), false, pageContainer);
    }

    public IDLTKLanguageToolkit getLanguageToolkit() {
        return FreemarkerLanguageToolkit.getDefault();
    }
}
