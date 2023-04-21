package org.sodeja.swing.component.code;

import java.awt.HeadlessException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import org.sodeja.collections.ListUtils;
import org.sodeja.functional.Pair;
import org.sodeja.swing.component.ApplicationDialog;
import org.sodeja.swing.component.form.FormPanelGridData;
import org.sodeja.swing.component.form.FormUtils;
import org.sodeja.swing.component.form.NamedFormDialog;
import org.sodeja.swing.context.ApplicationContext;
import org.sodeja.swing.resource.ResourceConstants;

class AddLocalizationDialog<T extends ApplicationContext> extends NamedFormDialog<T> {

    private static final long serialVersionUID = 1194782941790952326L;

    private JComboBox cmbLocales;

    private JTextField tfValue;

    private Pair<Locale, String> result;

    public AddLocalizationDialog(ApplicationDialog<T> parent) throws HeadlessException {
        super(parent);
    }

    @Override
    protected String getResourceName() {
        return ResourceConstants.DLG_LOCALIZATION_ADD;
    }

    @Override
    protected void initComponentsDelegate(FormPanelGridData gridData) {
        cmbLocales = FormUtils.addLabeledCombo(ctx, this.getContentPane(), ResourceConstants.LBL_LOCALE, gridData);
        cmbLocales.setRenderer(new LocaleListRenderer(ctx));
        tfValue = FormUtils.addLabeledField(ctx, this.getContentPane(), ResourceConstants.LBL_VALUE, gridData);
        List<Locale> locales = ListUtils.asList(Locale.getAvailableLocales());
        Collections.sort(locales, new Comparator<Locale>() {

            public int compare(Locale o1, Locale o2) {
                return LocaleListRenderer.getDisplayName(o1, ctx).compareTo(LocaleListRenderer.getDisplayName(o2, ctx));
            }
        });
        for (Locale locale : locales) {
            cmbLocales.addItem(locale);
        }
    }

    @Override
    protected void postInitComponents() {
        setSize(300, 150);
        setModal(true);
        super.postInitComponents();
    }

    public Pair<Locale, String> showAddLocalization() {
        result = null;
        cmbLocales.setSelectedIndex(0);
        tfValue.setText("");
        setVisible(true);
        return result;
    }

    @Override
    protected void okCallback() {
        result = Pair.of((Locale) cmbLocales.getSelectedItem(), tfValue.getText());
        super.okCallback();
    }
}
