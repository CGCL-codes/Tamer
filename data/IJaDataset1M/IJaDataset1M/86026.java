package net.sf.gham.core.gui.preferences;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.prefs.Preferences;
import javax.swing.JDialog;
import javax.swing.JFrame;
import net.sf.gham.core.gui.preferences.general.CurrencyPreferencesPanel;
import net.sf.gham.core.gui.preferences.general.ProxyPreferencesPanel;
import net.sf.gham.core.gui.preferences.general.SkinPreferencesPanel;
import net.sf.gham.swing.preferences.PreferencesTab;
import net.sf.gham.swing.util.PreferencesManager;
import net.sf.jtwa.Messages;
import net.sf.jtwa.preferences.LanguagePreferencesPanel;

/**
 * @author fabio
 *
 */
public class GeneralPreferencesPanel extends PreferencesTab {

    private LanguagePreferencesPanel languagePreferencesPanel;

    private SkinPreferencesPanel skinPreferencesPanel;

    private ProxyPreferencesPanel proxyPreferencesPanel;

    private CurrencyPreferencesPanel currencyPreferencesPanel;

    public GeneralPreferencesPanel() {
        super(Messages.getString("General"));
        languagePreferencesPanel = new LanguagePreferencesPanel();
        skinPreferencesPanel = new SkinPreferencesPanel();
        proxyPreferencesPanel = new ProxyPreferencesPanel();
        currencyPreferencesPanel = new CurrencyPreferencesPanel();
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 1;
        c.gridy = GridBagConstraints.RELATIVE;
        add(languagePreferencesPanel, c);
        add(currencyPreferencesPanel, c);
        add(skinPreferencesPanel, c);
        add(proxyPreferencesPanel, c);
    }

    @Override
    public void load(Component parent) {
        Preferences node = PreferencesManager.getNode(getClass());
        currencyPreferencesPanel.load();
        proxyPreferencesPanel.load(node);
        skinPreferencesPanel.load();
        languagePreferencesPanel.initTranslationCombo();
    }

    @Override
    public void okPresses(int callId) {
        Preferences node = PreferencesManager.getNode(getClass());
        proxyPreferencesPanel.save(node);
        languagePreferencesPanel.save();
        skinPreferencesPanel.save(node);
        currencyPreferencesPanel.save();
    }

    @Override
    public boolean canClose(JDialog dialog) {
        return true;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        GeneralPreferencesPanel p = new GeneralPreferencesPanel();
        p.load(f);
        f.add(p);
        f.setMinimumSize(new Dimension(100, 200));
        f.setPreferredSize(new Dimension(100, 200));
        f.setVisible(true);
    }
}
