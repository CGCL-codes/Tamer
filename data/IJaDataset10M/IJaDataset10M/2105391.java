package org.openconcerto.erp.preferences;

import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.element.SQLComponent;
import org.openconcerto.ui.DefaultGridBagConstraints;
import org.openconcerto.ui.preferences.DefaultPreferencePanel;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

public class NumerotationPreferencePanel extends DefaultPreferencePanel {

    private final SQLComponent sc;

    public NumerotationPreferencePanel() {
        this.setLayout(new GridBagLayout());
        final GridBagConstraints c = new DefaultGridBagConstraints();
        c.weighty = 1;
        c.weightx = 1;
        c.anchor = GridBagConstraints.NORTHWEST;
        this.sc = Configuration.getInstance().getDirectory().getElement("NUMEROTATION_AUTO").createDefaultComponent();
        sc.setOpaque(false);
        this.sc.uiInit();
        this.sc.select(2);
        this.add(this.sc, c);
    }

    public String getTitleName() {
        return "Numérotation";
    }

    public void storeValues() {
        this.sc.update();
    }

    public void restoreToDefaults() {
    }
}
