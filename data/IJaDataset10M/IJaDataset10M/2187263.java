package swtproto;

import data.*;
import data.ooimpl.*;

/**
     *
     * 	Eintrag, der in Katalogen und Best�nden des Programms verwendet wird.
     *
     *	@author Stefan Ulbrich
     *	@version 1.0
     */
public class BikeItem extends CatalogItemImpl {

    /**
       * 	Konstruktor. Erzeugt ein neues BikeItem.
       * 	@param sName Name des BikeItems
       *	@param qvValue QuoteValue (Verkaufspreis, Einkaufspreis)
       */
    public BikeItem(String sName, QuoteValue qvValue) {
        super(sName, qvValue);
    }

    /**
       * 	Setzt den Wert des BikeItems.
       *	@param qvValue setzen des QuoteValue eines bestimmten BikeItems
       *	@return void
       */
    public void setValue(QuoteValue qvValue) {
        super.setValue(qvValue);
    }

    /**
       * Gibt den Wert des BikeItems zur�ck.
       */
    public Value getValue() {
        return super.getValue();
    }

    /**
       * Gibt eine genaue Kopie des BikeItems zur�ck.
       */
    protected CatalogItemImpl getShallowClone() {
        return new BikeItem(new String(getName()), (QuoteValue) getValue().clone());
    }
}
