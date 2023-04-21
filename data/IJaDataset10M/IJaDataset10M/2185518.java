package com.google.code.cana.pojo.insumos;

import java.io.Serializable;
import java.util.Date;

/**
 * Mat�ria Prima descreve os insumos necess�rios � fabrica��o de produto.
 * 
 * @author Rodrigo Barbosa Lira
 * 
 */
public class MateriaPrima implements Serializable, Comparable<MateriaPrima> {

    private static final long serialVersionUID = 3250023768297993272L;

    private Integer materiaPrimaId;

    private String nome;

    private String unidade;

    private Date dataCadastro;

    private Double quantidade = 0.0;

    @SuppressWarnings("unused")
    private String nomeItemComboBox;

    /**
	 * @return the nomeItemComboBox
	 */
    public String getNomeItemComboBox() {
        return this.nome + " (" + this.unidade + ")";
    }

    /**
	 * @param nomeItemComboBox the nomeItemComboBox to set
	 */
    public void setNomeItemComboBox(String itemComboBox) {
        this.nomeItemComboBox = itemComboBox;
    }

    /**
	 * @return the dataCadastro
	 */
    public Date getDataCadastro() {
        return dataCadastro;
    }

    /**
	 * @param dataCadastro
	 *            the dataCadastro to set
	 */
    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    /**
	 * @return the materiaPrimaId
	 */
    public Integer getMateriaPrimaId() {
        return materiaPrimaId;
    }

    /**
	 * @param materiaPrimaId
	 *            the materiaPrimaId to set
	 */
    public void setMateriaPrimaId(Integer materiaPrimaId) {
        this.materiaPrimaId = materiaPrimaId;
    }

    /**
	 * @return the nome
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * @param nome
	 *            the nome to set
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
	 * @return the unidade
	 */
    public String getUnidade() {
        return unidade;
    }

    /**
	 * @param unidade
	 *            the unidade to set
	 */
    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    @Override
    public boolean equals(Object arg0) {
        try {
            MateriaPrima mp = (MateriaPrima) arg0;
            if (this.getMateriaPrimaId().equals(mp.getMateriaPrimaId())) {
                return true;
            } else return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return nome.hashCode();
    }

    public int compareTo(MateriaPrima arg0) {
        return this.getMateriaPrimaId().compareTo(arg0.getMateriaPrimaId());
    }

    /**
	 * @return the quantidade
	 */
    public Double getQuantidade() {
        return quantidade;
    }

    /**
	 * @param quantidade the quantidade to set
	 */
    public void setQuantidade(Double quantidade) {
        this.quantidade = quantidade;
    }
}
