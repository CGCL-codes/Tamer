package org.perlipse.internal.debug.ui.launchConfigurations;

import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.debug.core.DLTKDebugPreferenceConstants;
import org.eclipse.dltk.debug.ui.launchConfigurations.MainLaunchConfigurationTab;
import org.perlipse.core.PerlCoreConstants;

/**
 * main launch configuration tab for perl scripts
 */
public class PerlLaunchConfigurationTab extends MainLaunchConfigurationTab {

    public PerlLaunchConfigurationTab(String mode) {
        super(mode);
    }

    @Override
    protected boolean breakOnFirstLinePrefEnabled(PreferencesLookupDelegate delegate) {
        return delegate.getBoolean(PerlCoreConstants.PLUGIN_ID, DLTKDebugPreferenceConstants.PREF_DBGP_BREAK_ON_FIRST_LINE);
    }

    @Override
    protected boolean dbpgLoggingPrefEnabled(PreferencesLookupDelegate delegate) {
        return delegate.getBoolean(PerlCoreConstants.PLUGIN_ID, DLTKDebugPreferenceConstants.PREF_DBGP_ENABLE_LOGGING);
    }

    @Override
    public String getNatureID() {
        return PerlCoreConstants.NATURE_ID;
    }
}
