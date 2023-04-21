package org.openconcerto.erp.core.common.component;

import org.openconcerto.erp.core.common.ui.AbstractArticleItemTable;
import org.openconcerto.sql.element.BaseSQLComponent;
import org.openconcerto.sql.element.SQLElement;
import org.openconcerto.sql.model.SQLInjector;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.model.SQLRowValues;
import java.math.BigDecimal;
import java.util.List;

public abstract class TransfertBaseSQLComponent extends BaseSQLComponent {

    public TransfertBaseSQLComponent(SQLElement element) {
        super(element);
    }

    /**
     * Chargement d'élément à partir d'une autre table ex : les éléments d'un BL dans une facture
     * 
     * @param table ItemTable du component de destination (ex : tableFacture)
     * @param elt element source (ex : BL)
     * @param id id de la row source
     * @param itemsElt elements des items de la source (ex : BL_ELEMENT)
     */
    public void loadItem(AbstractArticleItemTable table, SQLElement elt, int id, SQLElement itemsElt) {
        loadItem(table, elt, id, itemsElt, true);
    }

    public void loadItem(AbstractArticleItemTable table, SQLElement elt, int id, SQLElement itemsElt, boolean clear) {
        List<SQLRow> myListItem = elt.getTable().getRow(id).getReferentRows(itemsElt.getTable());
        if (myListItem.size() != 0) {
            SQLInjector injector = SQLInjector.createDefaultInjector(itemsElt.getTable(), table.getSQLElement().getTable());
            if (clear) {
                table.getModel().clearRows();
            }
            for (SQLRow rowElt : myListItem) {
                SQLRowValues createRowValuesFrom = injector.createRowValuesFrom(rowElt);
                if (createRowValuesFrom.getTable().getFieldsName().contains("POURCENT_ACOMPTE")) {
                    if (createRowValuesFrom.getObject("POURCENT_ACOMPTE") == null) {
                        createRowValuesFrom.put("POURCENT_ACOMPTE", new BigDecimal(100.0));
                    }
                }
                table.getModel().addRow(createRowValuesFrom);
                int rowIndex = table.getModel().getRowCount() - 1;
                table.getModel().fireTableModelModified(rowIndex);
            }
        } else {
            if (clear) {
                table.getModel().clearRows();
                table.getModel().addNewRowAt(0);
            }
        }
        table.getModel().fireTableDataChanged();
        table.repaint();
    }
}
