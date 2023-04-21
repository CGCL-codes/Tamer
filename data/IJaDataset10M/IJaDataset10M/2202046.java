package de.d3s.alricg.logic.basic;

import de.d3s.alricg.data.charElement.basic.Zeitrechnung;

/**
 * @author Vincent
 *
 */
public class Zeitumrechnung {

    /**
	 * 
	 * @param tagInMonat
	 * @param monatImJahr
	 * @param jahr
	 * @param zeitrechnung
	 * @return
	 */
    public int NachTageNachBF(int tagInMonat, int monatImJahr, int jahr, Zeitrechnung zeitrechnung) {
        int posOrNegMulti = 1;
        if (jahr < 0) posOrNegMulti = -1;
        if (tagInMonat <= 0) throw new IllegalArgumentException();
        if (monatImJahr <= 0) throw new IllegalArgumentException();
        switch(zeitrechnung) {
            case NachBosparansFall:
                if (jahr < 0) posOrNegMulti = -1;
                return (tagInMonat + (monatImJahr - 1) * 30 + Math.abs(jahr) * 365) * posOrNegMulti;
            case NachHal:
                if (jahr + 993 < 0) posOrNegMulti = -1;
                return (tagInMonat + (monatImJahr - 1) * 30 + Math.abs(jahr + 993) * 365) * posOrNegMulti;
            default:
                throw new IllegalArgumentException();
        }
    }
}
