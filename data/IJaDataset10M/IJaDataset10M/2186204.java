package sts.gui.prefs;

import sts.framework.*;
import sts.hibernate.*;
import sts.gui.bindings.PreferenceBindings;
import kellinwood.hibernate_util.*;
import kellinwood.meshi.form.*;
import kellinwood.meshi.hibernate.*;
import net.sf.hibernate.*;
import javax.swing.*;
import java.util.*;
import sts.gui.*;

/**
 *
 * @author ken
 */
public class PreferencesDialog extends AbstractEntityDialog {

    /** Creates a new instance of CreatePreferenceDialog */
    public PreferencesDialog(java.awt.Frame parent) {
        super(parent);
        init();
        this.setTitle("Edit Preferences");
    }

    public void init() {
        super.init();
        ScreenLocationManager.configureDialog(this, 500, 400);
    }

    protected AbstractBindings createBindings() throws Exception {
        Preference p = sts.framework.Framework.onlyInstance().getPreferences();
        PreferenceBindings b = new PreferenceBindings(p);
        return b;
    }

    protected Collection createEditors() {
        Map editors = new LinkedHashMap();
        editors.putAll(bindings.getEditorMap());
        editors.remove("id");
        editors.remove("regattaId");
        return editors.values();
    }

    protected void createOrUpdate(Object entity) throws HibernateException {
        Framework.onlyInstance().savePreferences();
    }
}
