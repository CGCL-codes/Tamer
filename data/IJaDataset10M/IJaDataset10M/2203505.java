package de.mpiwg.vspace.languages.preferencepage.navigation;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import de.mpiwg.vspace.common.properties.CommonPropertiesProvider;
import de.mpiwg.vspace.generation.navigation.service.NavigationEntry;
import de.mpiwg.vspace.languages.preferencepage.navigation.internal.SimpleProperty;
import de.mpiwg.vspace.languages.preferencepage.service.LanguageDefaultNavigationEntryProvider;
import de.mpiwg.vspace.navigation.fieldeditor.IProperty;

public class ExtendedStringVSpaceFieldEditor extends StringVSpaceFieldEditor implements IBooleanPropertyChangedListener {

    private String labelCheckBox;

    private String propFieldCheckBox;

    private BooleanVSpaceFieldEditor checkBox;

    public ExtendedStringVSpaceFieldEditor(Composite parent, int style, IProperty property, NavigationEntry entry) {
        super(parent, style, property, entry);
        this.labelCheckBox = property.getPropertyUseDefaultLabel();
        this.propFieldCheckBox = property.getPropertyUseDefaultName();
    }

    @Override
    public void createContent() {
        super.createContent();
        if ((labelCheckBox != null) && (propFieldCheckBox != null)) {
            IProperty prop = new SimpleProperty(labelCheckBox, propFieldCheckBox);
            checkBox = new BooleanVSpaceFieldEditor(this, SWT.NONE, prop, entry);
            checkBox.addListener(this);
            GridData gd = new GridData();
            gd.horizontalSpan = 2;
            checkBox.setLayoutData(gd);
            IPreferenceStore ps = CommonPropertiesProvider.INSTANCE.getPreferenceStore();
            ps.setDefault(propFieldCheckBox, true);
            boolean useDefault = ps.getBoolean(propFieldCheckBox);
            if (useDefault) {
                String defaultText = LanguageDefaultNavigationEntryProvider.INSTANCE.getDefaultText(entry.getPropertyName());
                this.setText(defaultText);
                setEnabled(!useDefault);
            }
            checkBox.setPreferenceStore(ps);
            checkBox.createContent();
        }
    }

    @Override
    public void setDefaultValue(String defaultValue) {
    }

    @Override
    public void store() {
        boolean useDefault = checkBox.isChecked();
        if (useDefault) checkBox.store(); else {
            super.store();
            checkBox.store();
        }
    }

    public void booleanPropertyChanged(boolean newValue) {
        setEnabled(!newValue);
    }
}
