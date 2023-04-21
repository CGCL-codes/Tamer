package risk.card;

import risk.game.*;
import java.util.*;

/**
 * Card symbolisiert eine Spielkarte des Spiels Risiko
 * und enthaelt die Attribute Laendername und Symbol.
 *
 */
public class Card {

    /**
     * Attribut Laendername
     */
    private Territory country;

    /**
     * Attribut Symbol, welches eines der vier Moeglichkeiten 'Infanterie',
     * 'Kavallerie', 'Artillerie' und 'Joker' annehmen kann.
     */
    private String symbol;

    /**
     * Enthaelt registrierte CardViews.
     */
    private ArrayList views = null;

    /**
     *
     */
    private CardView cv = null;

    public Card(Territory cname, String symbol) {
        this.country = cname;
        this.symbol = symbol;
        views = new ArrayList();
    }

    /**
     * Methode zum lesen des Ländernamens
     *
     * @return Name des Landes auf der Spielkarte
     */
    public String getCountryName() {
        return this.country.getName();
    }

    /**
     * Methode zum lesen des Symbols
     * @return Symbol auf der Spielkarte
     */
    public String getSymbol() {
        return this.symbol;
    }

    /**
     * Liefert das Land zurueck
     */
    public Territory getTerritory() {
        return this.country;
    }

    /**
     * Die Methode registriert einen View beim Model. 
     *
     * @param view Der zu registrierende DiceView.
     */
    public void registerView(CardView view) {
        this.cv = view;
        if (view == null) {
            throw new IllegalArgumentException("Cannot add null object to DiceModel");
        }
        views.add(view);
    }

    /**
     * Ermoeglicht das unregistrieren eines Views.
     *
     * @param view Der zu entfernende CardView.
     */
    public boolean unregisterView(CardView view) {
        int index = 0;
        boolean found = false;
        while (index < views.size()) {
            if (views.get(index) == view) {
                found = true;
                break;
            }
            index++;
        }
        if (found) {
            views.remove(index);
            return true;
        }
        return false;
    }

    public CardView getCardView() {
        return this.cv;
    }
}
