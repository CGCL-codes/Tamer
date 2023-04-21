package com.memoire.bu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.CompoundBorder;

/**
 * A panel where the user can choose which elements to
 * put on his desktop.
 *
 * see below.
 */
public class BuDesktopPreferencesPanel extends BuAbstractPreferencesPanel implements ActionListener, BuBorders {

    private static final int NBL = 18;

    private static final String[] CODES = new String[] { "leftcolumn.visible", "specificbar.visible", "rightcolumn.visible", "statusbar.visible", "columns.swapped", "assistant.visible", "button.icon", "tool.icon", "button.text", "tool.text", "toolbar.floatable", "splashscreen.visible", "toolbar.rollover", "desktop.tabbed", "desktop.grid", "desktop.snap", "desktop.dots", "antialias.all" };

    private static final boolean[] DEFAUTS = new boolean[] { true, true, true, true, false, true, true, true, true, true, true, false, true, false, false, false, false, false };

    private static final String[] INTITULES = new String[] { "Colonne gauche", "Outils sp�cifiques", "Colonne droite", "Barre d'�tat", "Colonnes invers�es", "Assistant", "Ic�ne de bouton", "Ic�ne d'outil", "Texte de bouton", "Texte d'outil", "D�tachement des barres", "Ecran d'accueil", "Survol des barres", "Bureau � onglets", "Grille", "Magn�tisme", "Points", "Anticr�nelage" };

    BuCommonInterface appli_;

    BuPreferences options_;

    BuCheckBox[] cbEtats_;

    public String getTitle() {
        return getS("Bureau");
    }

    public String getCategory() {
        return getS("Visuel");
    }

    public BuDesktopPreferencesPanel(BuCommonInterface _appli) {
        super();
        appli_ = _appli;
        options_ = BuPreferences.BU;
        setLayout(new BuVerticalLayout(5, true, false));
        setBorder(EMPTY5555);
        build();
        updateComponents();
    }

    protected void build() {
        BuGridLayout lo = new BuGridLayout(2, 5, 5, true, true, false, false);
        lo.setSameWidth(true);
        lo.setSameHeight(true);
        BuPanel p1 = new BuPanel(lo);
        p1.setBorder(new CompoundBorder(new BuTitledBorder(getTitle()), EMPTY5555));
        cbEtats_ = new BuCheckBox[NBL];
        for (int i = 0; i < NBL; i++) {
            cbEtats_[i] = new BuCheckBox(getS(INTITULES[i]));
            cbEtats_[i].setName("cb" + CODES[i].replace('.', '_').toUpperCase());
            cbEtats_[i].setActionCommand(CODES[i].replace('.', '_').toUpperCase());
            cbEtats_[i].addActionListener(this);
            p1.add(cbEtats_[i]);
        }
        add(p1);
    }

    public void actionPerformed(ActionEvent _evt) {
        setDirty(true);
    }

    public boolean isPreferencesValidable() {
        return true;
    }

    public void validatePreferences() {
        fillTable();
        options_.writeIniFile();
    }

    public boolean isPreferencesCancelable() {
        return true;
    }

    public void cancelPreferences() {
        options_.readIniFile();
        updateComponents();
    }

    protected void fillTable() {
        for (int i = 0; i < NBL; i++) options_.putBooleanProperty(CODES[i], cbEtats_[i].isSelected());
        setDirty(false);
    }

    protected void updateComponents() {
        for (int i = 0; i < NBL; i++) cbEtats_[i].setSelected(options_.getBooleanProperty(CODES[i], DEFAUTS[i]));
        setDirty(false);
    }
}
