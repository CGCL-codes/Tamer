package com.icteam.fiji.model;

import java.util.Set;

public class PersRelations {

    private static final long serialVersionUID = 1L;

    private Long CEntBsns;

    private RuoloOrgv ruoloOrgv;

    private Pers m_persEqv;

    private AreaGeogr m_areaGeogr;

    private Soc m_soc;

    private Locaz m_locaz;

    private Pers m_persOwnr;

    private Set<CodEstEntBsns> m_codEstEntBsnses;

    public Set<CodEstEntBsns> getCodEstEntBsnses() {
        return m_codEstEntBsnses;
    }

    public void setCodEstEntBsnses(Set<CodEstEntBsns> p_codEstEntBsnses) {
        m_codEstEntBsnses = p_codEstEntBsnses;
    }

    public Long getCEntBsns() {
        return CEntBsns;
    }

    public void setCEntBsns(Long p_CEntBsns) {
        CEntBsns = p_CEntBsns;
    }

    public RuoloOrgv getRuoloOrgv() {
        return ruoloOrgv;
    }

    public void setRuoloOrgv(RuoloOrgv p_ruoloOrgv) {
        ruoloOrgv = p_ruoloOrgv;
    }

    public Pers getPersEqv() {
        return m_persEqv;
    }

    public void setPersEqv(Pers p_persEqv) {
        m_persEqv = p_persEqv;
    }

    public AreaGeogr getAreaGeogr() {
        return m_areaGeogr;
    }

    public void setAreaGeogr(AreaGeogr p_areaGeogr) {
        m_areaGeogr = p_areaGeogr;
    }

    public Soc getSoc() {
        return m_soc;
    }

    public void setSoc(Soc p_soc) {
        m_soc = p_soc;
    }

    public Locaz getLocaz() {
        return m_locaz;
    }

    public void setLocaz(Locaz p_locaz) {
        m_locaz = p_locaz;
    }

    public Pers getPersOwnr() {
        return m_persOwnr;
    }

    public void setPersOwnr(Pers p_persOwnr) {
        m_persOwnr = p_persOwnr;
    }
}
