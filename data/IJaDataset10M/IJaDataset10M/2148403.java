package br.com.petrobras.model;

import java.io.Serializable;

public class GrupoVO implements Serializable {

    private static final long serialVersionUID = 2770134899245113557L;

    private Integer cd;

    private String nome;

    private Boolean administrador;

    public GrupoVO() {
        this.cd = -1;
        this.nome = "";
        this.administrador = false;
    }

    public GrupoVO(Integer cd, String nome, Boolean administrador) {
        this.cd = cd;
        this.nome = nome;
        this.administrador = administrador;
    }

    public Integer getCd() {
        return cd;
    }

    public void setCd(Integer cd) {
        this.cd = cd;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String toString() {
        return "[" + this.cd + "," + this.nome + "," + this.administrador + "]";
    }

    public Boolean getAdministrador() {
        return administrador;
    }

    public void setAdministrador(Boolean administrador) {
        this.administrador = administrador;
    }
}
