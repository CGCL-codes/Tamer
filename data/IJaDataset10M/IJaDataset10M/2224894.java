package de.cnc.util;

import java.util.ArrayList;

/**
 * auf String typisierte ArrayList . <br/>
 *
 * <br/>
 * <b>Author:</b> <a href="http://www.heinerkuecker.de" target="_blank">Heiner K�cker</a><br/>
 * <br/>
 *
 * <br/>
 * <b>Author:</b> <a href="http://www.heinerkuecker.de" target="_blank">Heiner K�cker</a>
 * <br/>
 *
 * @author Heiner K�cker
 * @version $Id: StringList.java 373 2005-08-30 14:37:33Z marschal $
 * @deprecated use {@link de.cnc.util.StringArrayList instead}
 */
public class StringList {

    /**
   * eigentliche ArrayList
   */
    private ArrayList clArrayList = new ArrayList();

    /**
   * typisiertes get
   */
    public String get(int iPaIndex) {
        return (String) this.clArrayList.get(iPaIndex);
    }

    /**
   * typisiertes add
   */
    public boolean add(String strPaValue) {
        return this.clArrayList.add(strPaValue);
    }

    /**
   * size
   */
    public int size() {
        return this.clArrayList.size();
    }

    /**
   * Debug-Ausgabe
   */
    public String toString() {
        return "" + this.clArrayList;
    }
}
