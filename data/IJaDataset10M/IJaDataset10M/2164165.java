package com.emental.mindraider.ui.panels;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import com.emental.mindraider.core.MindRaider;

/**
 * Preferences: profile panel.
 *
 * @author Martin.Dvorak
 */
public class ProfileJPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    public JCheckBox forceLocale;

    public static final String CZECH = "Czech";

    public static final String ENGLISH = "English";

    public static final String ITALIAN = "Italian";

    public static final String[] localeLabels = new String[] { CZECH, ENGLISH, ITALIAN };

    public final JComboBox locales = new JComboBox(localeLabels);

    /**
     * Constructor.
     */
    public ProfileJPanel() {
        setLayout(new BorderLayout());
        JPanel topBorderPanel = new JPanel();
        topBorderPanel.setLayout(new BorderLayout());
        topBorderPanel.add(new JLabel("   "), BorderLayout.NORTH);
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
        p.setBorder(new TitledBorder(" Your Personal Info "));
        p.add(new JLabel(" Name:"));
        JTextField username = new JTextField();
        username.setEnabled(false);
        p.add(username);
        p.add(new JLabel(" FOAF URL:"));
        JTextField foaf = new JTextField("");
        foaf.setEnabled(false);
        p.add(foaf);
        topBorderPanel.add(p, BorderLayout.CENTER);
        JPanel systemAndLanguagePanel = new JPanel();
        systemAndLanguagePanel.setLayout(new BorderLayout());
        p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
        p.setBorder(new TitledBorder(" Language "));
        p.add(new JLabel(" Force locale:"));
        forceLocale = new JCheckBox("(restart needed)");
        forceLocale.setSelected(MindRaider.profile.isOverrideSystemLocale());
        p.add(forceLocale);
        forceLocale.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent evt) {
                if (evt.getStateChange() == ItemEvent.SELECTED) {
                    locales.setEnabled(true);
                } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
                    locales.setEnabled(false);
                }
            }
        });
        p.add(new JLabel());
        locales.setEnabled(forceLocale.isSelected());
        int index = 0;
        for (int i = 0; i < localeLabels.length; i++) {
            if (localeLabels[i].equals(MindRaider.profile.getCustomLocale())) {
                index = i;
                break;
            }
        }
        locales.setSelectedIndex(index);
        p.add(locales);
        systemAndLanguagePanel.add(p, BorderLayout.NORTH);
        p = new JPanel();
        p.setLayout(new GridLayout(2, 2));
        p.setBorder(new TitledBorder(" System Info "));
        p.add(new JLabel(" Login:"));
        JTextField systemLogin = new JTextField(MindRaider.user.getName());
        systemLogin.setEnabled(false);
        p.add(systemLogin);
        p.add(new JLabel(" Home:"));
        JTextField home = new JTextField(MindRaider.user.getHome());
        home.setEnabled(false);
        p.add(home);
        systemAndLanguagePanel.add(p, BorderLayout.SOUTH);
        topBorderPanel.add(systemAndLanguagePanel, BorderLayout.SOUTH);
        add(topBorderPanel, BorderLayout.NORTH);
    }
}
