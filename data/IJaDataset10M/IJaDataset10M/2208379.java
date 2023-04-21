package com.jaccount.model.strategies;

import com.jaccount.model.Buchung;

public class UIStrategySperre implements UIStrategy {

    Buchung buchung;

    int initSize;

    public UIStrategySperre(Buchung buchung) {
        this.buchung = buchung;
    }

    public Object get(int i) {
        if (i == 11) {
            return buchung.Sperre1;
        }
        if (i == 12) {
            return buchung.Sperre2;
        }
        return null;
    }

    public void set(int i, Object value) {
        if (i == 11) {
            buchung.Sperre1 = value.toString();
        }
        if (i == 12) {
            buchung.Sperre2 = value.toString();
        }
    }
}
