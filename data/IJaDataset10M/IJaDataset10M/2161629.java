package com.datas.bean.model.organizacija;

import java.util.Date;
import com.datas.bean.model.AbstractEntity;

public class OrganizacionaJedinica extends AbstractEntity {

    private Long idJedinice;

    private String oznakaJedinice;

    private Long idOrganizacije;

    private String nazivJedinice;

    private Long idOrganizacioneStrukture;

    private Integer status;

    private Long idNadredjeneJedinice;

    private Boolean pdv;

    private Long idMesta;

    private String adresa;

    private String telefon;

    private Date datumOtvaranja;

    private Date datumZatvaranja;

    public Long getIdJedinice() {
        return idJedinice;
    }

    public void setIdJedinice(Long idJedinice) {
        this.idJedinice = idJedinice;
    }

    public String getOznakaJedinice() {
        return oznakaJedinice;
    }

    public void setOznakaJedinice(String oznakaJedinice) {
        this.oznakaJedinice = oznakaJedinice;
    }

    public Long getIdOrganizacije() {
        return idOrganizacije;
    }

    public void setIdOrganizacije(Long idOrganizacije) {
        this.idOrganizacije = idOrganizacije;
    }

    public String getNazivJedinice() {
        return nazivJedinice;
    }

    public void setNazivJedinice(String nazivJedinice) {
        this.nazivJedinice = nazivJedinice;
    }

    public Long getIdOrganizacioneStrukture() {
        return idOrganizacioneStrukture;
    }

    public void setIdOrganizacioneStrukture(Long idOrganizacioneStrukture) {
        this.idOrganizacioneStrukture = idOrganizacioneStrukture;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getIdNadredjeneJedinice() {
        return idNadredjeneJedinice;
    }

    public void setIdNadredjeneJedinice(Long idNadredjeneJedinice) {
        this.idNadredjeneJedinice = idNadredjeneJedinice;
    }

    public Boolean getPdv() {
        return pdv;
    }

    public void setPdv(Boolean pdv) {
        this.pdv = pdv;
    }

    public Long getIdMesta() {
        return idMesta;
    }

    public void setIdMesta(Long idMesta) {
        this.idMesta = idMesta;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public Date getDatumOtvaranja() {
        return datumOtvaranja;
    }

    public void setDatumOtvaranja(Date datumOtvaranja) {
        this.datumOtvaranja = datumOtvaranja;
    }

    public Date getDatumZatvaranja() {
        return datumZatvaranja;
    }

    public void setDatumZatvaranja(Date datumZatvaranja) {
        this.datumZatvaranja = datumZatvaranja;
    }
}
