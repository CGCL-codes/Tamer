package de.mpiwg.vspace.sitemap.transformation.properties;

import org.eclipse.jface.preference.IPreferencePage;
import de.mpiwg.vspace.extension.interfaces.IVSpacePreferencePage;

public class SitemapPreferencePage implements IVSpacePreferencePage {

    public IPreferencePage getPreferencePage() {
        return new SitemapJfacePreferencePage();
    }

    public void setPreferencePage(IPreferencePage page) {
    }

    public String getPreferencePageId() {
        return "de.mpiwg.vspace.sitemap.preferences";
    }

    public String getPreferencePagePath() {
        return "";
    }
}
