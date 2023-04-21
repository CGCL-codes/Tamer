package com.datas.bean.model.maloprodaja.maloprodajniracun;

import java.math.BigDecimal;

/**
 * @author kimi
 * 
 */
public class MaloprodajniRacunModel {

    private BigDecimal stopaRabata;

    private BigDecimal iznosRabata;

    private BigDecimal ukupnaVrednost;

    public static final int STOPA_RABATA_CHANGED = 1;

    public static final int UKUPNA_VREDNOST_CHANGED = 2;

    public BigDecimal getStopaRabata() {
        return stopaRabata;
    }

    public void setStopaRabata(BigDecimal stopaRabata) {
        this.stopaRabata = stopaRabata;
    }

    public BigDecimal getIznosRabata() {
        return iznosRabata;
    }

    public void setIznosRabata(BigDecimal iznosRabata) {
        this.iznosRabata = iznosRabata;
    }

    public BigDecimal getUkupnaVrednost() {
        return ukupnaVrednost;
    }

    public void setUkupnaVrednost(BigDecimal ukupnaVrednost) {
        this.ukupnaVrednost = ukupnaVrednost;
    }
}
