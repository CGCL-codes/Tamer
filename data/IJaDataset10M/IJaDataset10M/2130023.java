package net.esle.sinadura.gui.sections.preferences.windows;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.esle.sinadura.gui.application.LanguageResource;
import net.esle.sinadura.gui.sections.preferences.helpers.PreferencesHelper;
import org.eclipse.jface.preference.PreferenceDialog;
import org.eclipse.jface.preference.PreferenceManager;
import org.eclipse.jface.preference.PreferenceNode;
import org.eclipse.jface.preference.PreferenceStore;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class PreferencesManager {

    private static Logger logger = Logger.getLogger(PreferencesManager.class.getName());

    /**
	 * @param mainShell
	 */
    public void run(Shell mainShell) {
        PreferenceManager mgr = new PreferenceManager();
        GeneralPreferences generalPreferences = new GeneralPreferences();
        generalPreferences.setTitle(LanguageResource.getLanguage().getString("preferences.main.title"));
        generalPreferences.setDescription(LanguageResource.getLanguage().getString("preferences.main.description") + "\n");
        ProxyPreferences proxyPreferences = new ProxyPreferences();
        proxyPreferences.setTitle(LanguageResource.getLanguage().getString("preferences.proxy.title"));
        proxyPreferences.setDescription(LanguageResource.getLanguage().getString("preferences.proxy.description") + "\n");
        SignAppearancePreferences signAppearancePreferences = new SignAppearancePreferences();
        signAppearancePreferences.setTitle(LanguageResource.getLanguage().getString("preferences.sign.appearance.title"));
        signAppearancePreferences.setDescription(LanguageResource.getLanguage().getString("preferences.sign.appearance.description") + "\n");
        ValidatePreferences validatePreferences = new ValidatePreferences();
        validatePreferences.setTitle(LanguageResource.getLanguage().getString("preferences.validate.title"));
        validatePreferences.setDescription(LanguageResource.getLanguage().getString("preferences.validate.description") + "\n");
        CertPreferences certPreferences = new CertPreferences();
        certPreferences.setTitle(LanguageResource.getLanguage().getString("preferences.cert.title"));
        certPreferences.setDescription(LanguageResource.getLanguage().getString("preferences.cert.description") + "\n");
        SoftwareCertPreferences softwareCertPreferences = new SoftwareCertPreferences();
        softwareCertPreferences.setTitle(LanguageResource.getLanguage().getString("preferences.cert.software.title"));
        softwareCertPreferences.setDescription(LanguageResource.getLanguage().getString("preferences.cert.software.description") + "\n");
        HardwareCertPreferences hardwareCertPreferences = new HardwareCertPreferences();
        hardwareCertPreferences.setTitle(LanguageResource.getLanguage().getString("preferences.cert.hardware.title"));
        hardwareCertPreferences.setDescription(LanguageResource.getLanguage().getString("preferences.cert.hardware.description") + "\n");
        TrustedCertsPreferences trustedPreferences = new TrustedCertsPreferences();
        trustedPreferences.setTitle(LanguageResource.getLanguage().getString("preferences.trusted.title"));
        trustedPreferences.setDescription(LanguageResource.getLanguage().getString("preferences.trusted.description") + "\n");
        CacheCertsPreferences cachePreferences = new CacheCertsPreferences();
        cachePreferences.setTitle(LanguageResource.getLanguage().getString("preferences.cache.title"));
        cachePreferences.setDescription(LanguageResource.getLanguage().getString("preferences.cache.description") + "\n");
        PreferenceNode generalNode = new PreferenceNode("generalNode", generalPreferences);
        PreferenceNode proxyNode = new PreferenceNode("proxyNode", proxyPreferences);
        PreferenceNode signAppearanceNode = new PreferenceNode("signAppearanceNode", signAppearancePreferences);
        PreferenceNode validateNode = new PreferenceNode("validateNode", validatePreferences);
        PreferenceNode certNode = new PreferenceNode("certNode", certPreferences);
        PreferenceNode softwareCertNode = new PreferenceNode("softwareCertNode", softwareCertPreferences);
        PreferenceNode hardwareCertNode = new PreferenceNode("hardwareCertNode", hardwareCertPreferences);
        PreferenceNode trustedNode = new PreferenceNode("trustedNode", trustedPreferences);
        PreferenceNode cacheNode = new PreferenceNode("cacheNode", cachePreferences);
        mgr.addToRoot(generalNode);
        mgr.addToRoot(proxyNode);
        mgr.addToRoot(signAppearanceNode);
        mgr.addTo(signAppearanceNode.getId(), validateNode);
        mgr.addToRoot(certNode);
        mgr.addTo(certNode.getId(), softwareCertNode);
        mgr.addTo(certNode.getId(), hardwareCertNode);
        mgr.addToRoot(trustedNode);
        mgr.addTo(trustedNode.getId(), cacheNode);
        PreferenceDialog dlg = new PreferenceDialog(mainShell, mgr);
        PreferenceStore ps = PreferencesHelper.getPreferences();
        try {
            ps.load();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "", e);
        }
        dlg.setPreferenceStore(ps);
        dlg.open();
        try {
            ps.save();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "", e);
        }
    }

    /**
	 * The application entry point
	 *
	 * @param mainShell
	 */
    public void abrirVentana(Shell mainShell) {
        this.run(mainShell);
    }
}
