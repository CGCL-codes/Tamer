package org.openconcerto.erp.injector;

import org.openconcerto.sql.model.DBRoot;
import org.openconcerto.sql.model.SQLInjector;
import org.openconcerto.sql.model.SQLTable;

public class FactureCommandeSQLInjector extends SQLInjector {

    public FactureCommandeSQLInjector(final DBRoot root) {
        super(root, "SAISIE_VENTE_FACTURE", "COMMANDE");
        final SQLTable tableFacture = getSource();
        final SQLTable tableCommande = getDestination();
        mapDefaultValues(tableCommande.getField("SOURCE"), tableFacture.getName());
        map(tableFacture.getKey(), tableCommande.getField("IDSOURCE"));
    }
}
