package org.jw.web.rdc.integration.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * GrupoMaterial entity. @author MyEclipse Persistence Tools
 */
public class GrupoMaterial implements java.io.Serializable {

    private Integer codigo;

    private String descricao;

    private Set empresas = new HashSet(0);

    private Set itemListaMaterials = new HashSet(0);

    private Set valorGastos = new HashSet(0);

    /** default constructor */
    public GrupoMaterial() {
    }

    /** minimal constructor */
    public GrupoMaterial(Integer codigo) {
        this.codigo = codigo;
    }

    /** full constructor */
    public GrupoMaterial(Integer codigo, String descricao, Set empresas, Set itemListaMaterials, Set valorGastos) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.empresas = empresas;
        this.itemListaMaterials = itemListaMaterials;
        this.valorGastos = valorGastos;
    }

    public Integer getCodigo() {
        return this.codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set getEmpresas() {
        return this.empresas;
    }

    public void setEmpresas(Set empresas) {
        this.empresas = empresas;
    }

    public Set getItemListaMaterials() {
        return this.itemListaMaterials;
    }

    public void setItemListaMaterials(Set itemListaMaterials) {
        this.itemListaMaterials = itemListaMaterials;
    }

    public Set getValorGastos() {
        return this.valorGastos;
    }

    public void setValorGastos(Set valorGastos) {
        this.valorGastos = valorGastos;
    }
}
