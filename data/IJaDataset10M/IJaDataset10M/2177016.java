package com.datas.bean.model.maloprodaja.maloprodajniracun;

import java.math.BigDecimal;
import java.util.Date;
import com.datas.bean.model.AbstractEntity;

public class MaloprodajniRacun extends AbstractEntity {

    private Long idRacunaMaloprodaje;

    private String oznakaRacunaMaloprodaje;

    private String vrstaDokumenta;

    private Boolean proknjizeno;

    private Boolean storno;

    private Date datumKreiranja;

    private Date datumKnjizenja;

    private String idUser;

    private String napomena;

    private Long idKaseMaloprodaje;

    private Long idPartnera;

    private Boolean vojniRecept;

    private BigDecimal stopaRabata;

    private BigDecimal iznosRabata;

    private BigDecimal iznosGotovina;

    private BigDecimal iznosKartica;

    private BigDecimal iznosCek;

    private BigDecimal iznosUkupno;

    public Long getIdRacunaMaloprodaje() {
        return idRacunaMaloprodaje;
    }

    public void setIdRacunaMaloprodaje(Long idRacunaMaloprodaje) {
        this.idRacunaMaloprodaje = idRacunaMaloprodaje;
    }

    public String getOznakaRacunaMaloprodaje() {
        return oznakaRacunaMaloprodaje;
    }

    public void setOznakaRacunaMaloprodaje(String oznakaRacunaMaloprodaje) {
        this.oznakaRacunaMaloprodaje = oznakaRacunaMaloprodaje;
    }

    public String getVrstaDokumenta() {
        return vrstaDokumenta;
    }

    public void setVrstaDokumenta(String vrstaDokumenta) {
        this.vrstaDokumenta = vrstaDokumenta;
    }

    public Boolean getProknjizeno() {
        return proknjizeno;
    }

    public void setProknjizeno(Boolean proknjizeno) {
        this.proknjizeno = proknjizeno;
    }

    public Boolean getStorno() {
        return storno;
    }

    public void setStorno(Boolean storno) {
        this.storno = storno;
    }

    public Date getDatumKreiranja() {
        return datumKreiranja;
    }

    public void setDatumKreiranja(Date datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    public Date getDatumKnjizenja() {
        return datumKnjizenja;
    }

    public void setDatumKnjizenja(Date datumKnjizenja) {
        this.datumKnjizenja = datumKnjizenja;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public Long getIdKaseMaloprodaje() {
        return idKaseMaloprodaje;
    }

    public void setIdKaseMaloprodaje(Long idKaseMaloprodaje) {
        this.idKaseMaloprodaje = idKaseMaloprodaje;
    }

    public Long getIdPartnera() {
        return idPartnera;
    }

    public void setIdPartnera(Long idPartnera) {
        this.idPartnera = idPartnera;
    }

    public Boolean getVojniRecept() {
        return vojniRecept;
    }

    public void setVojniRecept(Boolean vojniRecept) {
        this.vojniRecept = vojniRecept;
    }

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

    public BigDecimal getIznosGotovina() {
        return iznosGotovina;
    }

    public void setIznosGotovina(BigDecimal iznosGotovina) {
        this.iznosGotovina = iznosGotovina;
    }

    public BigDecimal getIznosKartica() {
        return iznosKartica;
    }

    public void setIznosKartica(BigDecimal iznosKartica) {
        this.iznosKartica = iznosKartica;
    }

    public BigDecimal getIznosCek() {
        return iznosCek;
    }

    public void setIznosCek(BigDecimal iznosCek) {
        this.iznosCek = iznosCek;
    }

    public BigDecimal getIznosUkupno() {
        return iznosUkupno;
    }

    public void setIznosUkupno(BigDecimal iznosUkupno) {
        this.iznosUkupno = iznosUkupno;
    }
}
