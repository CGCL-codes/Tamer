package it.uniromadue.portaleuni.form;

import java.util.ArrayList;
import it.uniromadue.portaleuni.base.BaseForm;

public class VisualizzaECancellaPrenotazioniForm extends BaseForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private ArrayList prenotazioniTable;

    private String rowID;

    /**
	 * @return the prenotazioniTable
	 */
    public ArrayList getPrenotazioniTable() {
        return prenotazioniTable;
    }

    /**
	 * @param prenotazioniTable the prenotazioniTable to set
	 */
    public void setPrenotazioniTable(ArrayList prenotazioniTable) {
        this.prenotazioniTable = prenotazioniTable;
    }

    /**
	 * @return the rowID
	 */
    public String getRowID() {
        return rowID;
    }

    /**
	 * @param rowID the rowID to set
	 */
    public void setRowID(String rowID) {
        this.rowID = rowID;
    }
}
