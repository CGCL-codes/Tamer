package com.digdia.app.gwt.client.model.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author vita
 */
public class PenyakitDTO implements IsSerializable {

    private Integer id;

    private String namaPenyakit;

    private String penyebab;

    private String solusi;

    public PenyakitDTO(Integer id) {
        this.id = id;
    }

    public PenyakitDTO() {
    }

    public PenyakitDTO(Integer id, String namaPenyakit, String penyebab, String solusi) {
        this.id = id;
        this.namaPenyakit = namaPenyakit;
        this.penyebab = penyebab;
        this.solusi = solusi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaPenyakit() {
        return namaPenyakit;
    }

    public void setNamaPenyakit(String namaPenyakit) {
        this.namaPenyakit = namaPenyakit;
    }

    public String getPenyebab() {
        return penyebab;
    }

    public void setPenyebab(String penyebab) {
        this.penyebab = penyebab;
    }

    public String getSolusi() {
        return solusi;
    }

    public void setSolusi(String solusi) {
        this.solusi = solusi;
    }

    @Override
    public String toString() {
        return "PenyakitDTO{" + "id=" + id + '}';
    }
}
