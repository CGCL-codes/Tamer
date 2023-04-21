package org.springframework.richclient.preference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.dialog.TreeCompositeDialogPage;
import org.springframework.richclient.settings.Settings;
import org.springframework.util.Assert;

public class PreferenceDialog extends TitledPageApplicationDialog {

    private List preferencePages = new ArrayList();

    private Settings settings;

    public PreferenceDialog() {
        this("preferenceDialog");
    }

    public PreferenceDialog(String dialogId) {
        super(new TreeCompositeDialogPage(dialogId));
    }

    private void addPage(PreferencePage page) {
        Assert.isTrue(!isControlCreated(), "Add pages before control is created.");
        preferencePages.add(page);
        page.setPreferenceDialog(this);
    }

    public void addPreferencePage(PreferencePage page) {
        addPage(page);
        getPageContainer().addPage(page);
    }

    public void addPreferencePage(PreferencePage parent, PreferencePage page) {
        addPage(page);
        getPageContainer().addPage(parent, page);
    }

    private TreeCompositeDialogPage getPageContainer() {
        return (TreeCompositeDialogPage) getDialogPage();
    }

    public Settings getSettings() {
        return settings;
    }

    public boolean onFinish() {
        for (Iterator iter = preferencePages.iterator(); iter.hasNext(); ) {
            PreferencePage page = (PreferencePage) iter.next();
            if (!page.onFinish()) {
                return false;
            }
        }
        if (settings != null) {
            try {
                settings.save();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void setSettings(Settings settings) {
        Assert.notNull(settings, "Settings cannot be null.");
        this.settings = settings;
    }
}
