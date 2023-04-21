package net.esle.sinadura.gui.sections.preferences.windows;

import java.util.logging.Logger;
import net.esle.sinadura.gui.application.LanguageResource;
import net.esle.sinadura.gui.application.ResourceHelper;
import net.esle.sinadura.gui.sections.global.windows.InfoDialog;
import net.esle.sinadura.gui.sections.preferences.components.PasswordStringFieldEditor;
import net.esle.sinadura.gui.sections.preferences.helpers.PreferencesHelper;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Display;

public class ProxyPreferences extends FieldEditorPreferencePage {

    private static Logger logger = Logger.getLogger(ProxyPreferences.class.getName());

    private StringFieldEditor host = null;

    private StringFieldEditor port = null;

    private BooleanFieldEditor enable = null;

    public ProxyPreferences() {
        super(GRID);
    }

    @Override
    protected void createFieldEditors() {
        host = new StringFieldEditor(PreferencesHelper.PROXY_HOST, LanguageResource.getLanguage().getString("preferences.proxy.host"), getFieldEditorParent());
        addField(host);
        port = new StringFieldEditor(PreferencesHelper.PROXY_PORT, LanguageResource.getLanguage().getString("preferences.proxy.port"), getFieldEditorParent());
        addField(port);
        StringFieldEditor user = new StringFieldEditor(PreferencesHelper.PROXY_USER, LanguageResource.getLanguage().getString("preferences.proxy.user"), getFieldEditorParent());
        addField(user);
        PasswordStringFieldEditor pass = new PasswordStringFieldEditor(PreferencesHelper.PROXY_PASS, LanguageResource.getLanguage().getString("preferences.proxy.pass"), getFieldEditorParent());
        addField(pass);
        StringFieldEditor non_host = new StringFieldEditor(PreferencesHelper.PROXY_NON_PROXY, LanguageResource.getLanguage().getString("preferences.proxy.non_proxy"), getFieldEditorParent());
        addField(non_host);
        enable = new BooleanFieldEditor(PreferencesHelper.PROXY_ENABLE, LanguageResource.getLanguage().getString("preferences.proxy.enable"), getFieldEditorParent());
        addField(enable);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        super.propertyChange(event);
    }

    @Override
    public boolean performOk() {
        boolean ok = false;
        if (enable != null && enable.getBooleanValue() && (host.getStringValue() == null || host.getStringValue().equals("") || port.getStringValue() == null || port.getStringValue().equals(""))) {
            InfoDialog id = new InfoDialog(this.getShell());
            id.open(LanguageResource.getLanguage().getString("error.proxy_configuration"));
            logger.severe(LanguageResource.getLanguage().getString("error.proxy_configuration"));
        } else {
            ok = super.performOk();
            ResourceHelper.configureProxy();
        }
        return ok;
    }
}
