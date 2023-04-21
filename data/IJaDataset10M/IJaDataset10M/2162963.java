package Gerichte;

import Sale.Stock;
import Sale.StockItem;
import Sale.Catalog;
import Sale.CatalogItem;
import Tools.Debug;
import Tools.Defines;

/**
Das Catalog-Gericht ist der formale Eintrag in die Speisekarte.
Diese Klasse kapselt alle Gerichtspezifischen Funktionen und Attribute, wie Name, Nummer in Speisekarte, Preis und die benutzten Zutaten*/
public class CCatGericht extends CatalogItem {

    /**die Verbale Beschreibung*/
    protected String sVerbal;

    /**der Preis des Gerichtes*/
    protected float fPreis;

    /**der Zutatenstock
    @see Sale.Stock*/
    protected Stock sZutaten;

    /**der Konstruktor, er konstruiert ein neues Object
    @param Nummer die Nummer des Gerichtes in der Speisekarte,<BR> 0 ==> es handelt sich um eine verbale Bestellung,<BR> entspricht dem key-Attribut von CatalogItem
    @param Verbal die verbale Beschreibung des Gerichtes
    @param Preis der Preis
    @param Zutaten ein Stock von Zutaten*/
    public CCatGericht(String Nummer, String Verbal, float Preis, Stock Zutaten) {
        super(Nummer);
        Debug.proc("CCatGericht.CCatgericht(" + Nummer + "," + Verbal + ")");
        sVerbal = Verbal;
        fPreis = Preis;
        sZutaten = Zutaten;
        return;
    }

    /**gibt Nummer des Gerichtes zur�ck (= key)*/
    public String getNummer() {
        Debug.proc("CCatGericht.getNummer():" + getKey());
        return getKey();
    }

    /**gibt die verbale Beschreibung zur�ck*/
    public String getVerbal() {
        Debug.proc("CCatGericht.getVerbal():" + sVerbal);
        return sVerbal;
    }

    /**gibt Preis zur�ck*/
    public float getPreis() {
        Debug.proc("CCatGericht.getPreis():" + fPreis);
        return fPreis;
    }

    /**gibt den Zutatenstock zur�ck*/
    public Stock getZutaten() {
        Debug.proc("CCatGericht.getZutaten()");
        return sZutaten;
    }

    /**gibt den Preis zur�ck*/
    public float getValue() {
        Debug.proc("CCatGericht.getValue()(returns Preis)");
        return getPreis();
    }

    /**vergleicht zwei Catalog - Gerichte*/
    public boolean equals(Object o) {
        Debug.proc("CCatGericht.equals()");
        CCatGericht co = (CCatGericht) o;
        boolean e;
        e = (getKey() == co.getKey());
        e &= (sVerbal == co.sVerbal);
        e &= (fPreis == co.fPreis);
        e &= (sZutaten == co.sZutaten);
        return e;
    }

    /**klont das Gericht*/
    public Object clone() {
        Debug.proc("CCatGericht.clone()");
        CCatGericht nc = new CCatGericht(getKey(), sVerbal, fPreis, sZutaten);
        return nc;
    }

    /**erzeugt das zugeh�rige StockItem*/
    public StockItem createStockItem() {
        Debug.proc("CCatGericht.createStockItem()");
        CStockGericht sg = new CStockGericht(this);
        return sg;
    }
}
