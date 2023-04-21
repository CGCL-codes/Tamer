package org.openconcerto.erp.preferences;

import org.openconcerto.erp.core.humanresources.payroll.report.EtatChargesPayeSheet;
import org.openconcerto.erp.core.humanresources.payroll.report.FichePayeSheet;
import org.openconcerto.erp.core.humanresources.payroll.report.LivrePayeSheet;
import org.openconcerto.utils.Tuple2;

public class GenerationDocumentPayePreferencePanel extends AbstractGenerationDocumentPreferencePanel {

    public GenerationDocumentPayePreferencePanel() {
        super();
        this.mapKeyLabel.put(Tuple2.create(FichePayeSheet.TEMPLATE_ID, "LocationFichePaye"), "Fiche paye");
        this.mapKeyLabel.put(Tuple2.create(LivrePayeSheet.TEMPLATE_ID, "LocationLivrePaye"), "Livre paye");
        this.mapKeyLabel.put(Tuple2.create(EtatChargesPayeSheet.TEMPLATE_ID, "LocationEtatChargesPaye"), "Etat des charges");
    }

    public String getTitleName() {
        return "Destination des documents générés";
    }
}
