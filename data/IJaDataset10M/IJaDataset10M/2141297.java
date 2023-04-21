package ch.hsr.orm.codegen.ui.wizards;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import ch.hsr.orm.codegen.ui.core.AbstractCartridgeGenerator;
import ch.hsr.orm.codegen.ui.core.JPACartridgeGenerator;
import ch.hsr.orm.codegen.ui.wizards.page.CartridgeConfigPage;
import ch.hsr.orm.codegen.ui.wizards.page.JPACartridgeConfigPage;
import ch.hsr.orm.preferences.PreferenceConstants;

public class JPACartridgeWizard extends CartridgeWizard {

    @Override
    protected AbstractCartridgeGenerator getTemplate() {
        return new JPACartridgeGenerator();
    }

    @Override
    protected CartridgeConfigPage getConfigPage() {
        return new JPACartridgeConfigPage(selection);
    }

    @Override
    protected String getPreferencePrefix() {
        return PreferenceConstants.JPA_PREFIX;
    }

    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
    }
}
