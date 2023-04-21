package org.pockit.model;

import org.pockit.PockIt;

public class SettingEditorModel extends ThreadedModel {

    private Setting[] settings = null;

    public SettingEditorModel(PockIt parent) {
        super(parent);
        Setting[] tempSettings = { Setting.username, Setting.password, Setting.service_url };
        settings = tempSettings;
    }

    public void whileRunning() {
    }

    public Setting[] getSettings() {
        return settings;
    }

    /**
	 * @deprecated
	 */
    public void saveSettings(org.pockit.view.lowfi.SettingEditorView view) {
    }
}
