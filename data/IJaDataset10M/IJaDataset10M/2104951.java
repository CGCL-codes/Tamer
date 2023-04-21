package org.openconcerto.erp.core.humanresources.payroll.ui;

import org.openconcerto.erp.preferences.AbstractImpressionPreferencePanel;
import java.util.HashMap;
import java.util.Map;

public class ImpressionPayePreferencePanel extends AbstractImpressionPreferencePanel {

    public ImpressionPayePreferencePanel() {
        super();
        final Map<String, String> m = new HashMap<String, String>();
        m.put("EtatChargesPayePrinter", "Etat des charges");
        m.put("FichePayePrinter", "Fiche paye");
        m.put("LivrePayePrinter", "Livre paye");
        uiInit(m);
    }

    public String getTitleName() {
        return "Impression paye";
    }
}
