package org.openconcerto.erp.generationDoc.gestcomm;

import org.openconcerto.erp.generationDoc.AbstractSheetXMLWithDate;
import org.openconcerto.erp.preferences.PrinterNXProps;
import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLRow;

public class CommandeXmlSheet extends AbstractSheetXMLWithDate {

    public static final String TEMPLATE_ID = "Commande";

    public static final String TEMPLATE_PROPERTY_NAME = "LocationCmd";

    @Override
    public SQLRow getRowLanguage() {
        SQLRow rowFournisseur = this.row.getForeignRow("ID_FOURNISSEUR");
        if (rowFournisseur.getTable().contains("ID_LANGUE")) {
            return rowFournisseur.getForeignRow("ID_LANGUE");
        } else {
            return super.getRowLanguage();
        }
    }

    public CommandeXmlSheet(SQLRow row) {
        super(row);
        this.printer = PrinterNXProps.getInstance().getStringProperty("CmdPrinter");
        this.elt = Configuration.getInstance().getDirectory().getElement("COMMANDE");
    }

    @Override
    public String getDefaultTemplateId() {
        return TEMPLATE_ID;
    }

    @Override
    public String getName() {
        return "Commande_" + row.getString("NUMERO");
    }
}
