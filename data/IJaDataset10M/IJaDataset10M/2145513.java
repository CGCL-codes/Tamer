package org.fudaa.dodico.crue.comparaison.config;

import org.apache.commons.lang.StringUtils;

public class ConfCompareListe extends AbstractConfCompare {

    private String attribut;

    private boolean bidirect;

    /**
   * @return the attribut
   */
    public String getAttribut() {
        return attribut;
    }

    /**
   * @param attribut the attribut to set
   */
    public void setAttribut(String attribut) {
        this.attribut = StringUtils.trim(attribut);
    }

    /**
   * @return the bidirect
   */
    public boolean isBidirect() {
        return bidirect;
    }

    /**
   * @param bidirect the bidirect to set
   */
    public void setBidirect(boolean bidirect) {
        this.bidirect = bidirect;
    }
}
