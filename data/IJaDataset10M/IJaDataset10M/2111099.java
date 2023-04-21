package com.viators.beans;

import java.util.Date;

/**
 *
 * @author mvalentin
 */
public class Sisde_pasajerosBean {

    private int npers_codigo;

    private Date dpasa_creacion;

    private Date dpasa_modifica;

    private int nlipa_codigo;

    private int nvehi_codigo;

    private int ntipa_codigo;

    public Date getDpasa_creacion() {
        return dpasa_creacion;
    }

    public void setDpasa_creacion(Date dpasa_creacion) {
        this.dpasa_creacion = dpasa_creacion;
    }

    public Date getDpasa_modifica() {
        return dpasa_modifica;
    }

    public void setDpasa_modifica(Date dpasa_modifica) {
        this.dpasa_modifica = dpasa_modifica;
    }

    public int getNlipa_codigo() {
        return nlipa_codigo;
    }

    public void setNlipa_codigo(int nlipa_codigo) {
        this.nlipa_codigo = nlipa_codigo;
    }

    public int getNpers_codigo() {
        return npers_codigo;
    }

    public void setNpers_codigo(int npers_codigo) {
        this.npers_codigo = npers_codigo;
    }

    public int getNtipa_codigo() {
        return ntipa_codigo;
    }

    public void setNtipa_codigo(int ntipa_codigo) {
        this.ntipa_codigo = ntipa_codigo;
    }

    public int getNvehi_codigo() {
        return nvehi_codigo;
    }

    public void setNvehi_codigo(int nvehi_codigo) {
        this.nvehi_codigo = nvehi_codigo;
    }
}
