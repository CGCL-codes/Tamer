package com.semipol.PHPCheckclipse.model.filters.StructureChecker;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import com.semipol.PHPCheckclipse.Activator;

/**
 * Initializer for the defaults of StructureChecker.
 * 
 * @author Johannes Wienke
 */
public class StructureCheckerInitializer extends AbstractPreferenceInitializer {

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(StructureChecker.P_BRACE_AFTER_CLASS, StructureChecker.M_BRACE_AFTER_CLASS_NEWLINE);
        store.setDefault(StructureChecker.P_BRACE_AFTER_FUNCTION, StructureChecker.M_BRACE_AFTER_FUNCTION_NEWLINE);
        store.setDefault(StructureChecker.P_SPACE_AROUND_CONTROLS, true);
        store.setDefault(StructureChecker.P_USE_BRACES, true);
        store.setDefault(StructureChecker.P_LINE_COMMENT, StructureChecker.M_LINE_COMMENT_CSTYLE);
        store.setDefault(StructureChecker.P_CLASS_NAMES, StructureChecker.M_CLASS_NAMES_CSTYLE);
        store.setDefault(StructureChecker.P_FUNCTION_NAMES, StructureChecker.M_FUNCTION_NAMES_CAMEL_CASE);
        store.setDefault(StructureChecker.P_PRIVATE_FUNCTION_NAMES, true);
    }
}
