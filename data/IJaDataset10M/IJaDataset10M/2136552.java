package com.google.code.cana.pojo.financeiro;

import java.util.Date;
import com.google.code.cana.pojo.pessoas.Usuario;

/**
 * Registro de pagamento de gratifica��o a um colaborador
 * 
 * @author Rodrigo Barbosa Lira
 * 
 */
public class Gratificacao implements ItemExtrato, Comparable<Gratificacao> {

    private Integer gratificacaoId;

    private Date dataCadastro;

    private Date dataRegistro;

    private Double valorPago;

    private Usuario usuario;

    private String observacao;

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
	 * @return the dataRegistro
	 */
    public Date getDataRegistro() {
        return dataRegistro;
    }

    /**
	 * @param dataRegistro
	 *            the dataRegistro to set
	 */
    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    /**
	 * @return the gratificacaoId
	 */
    public Integer getGratificacaoId() {
        return gratificacaoId;
    }

    /**
	 * @param gratificacaoId
	 *            the gratificacaoId to set
	 */
    public void setGratificacaoId(Integer gratificacaoId) {
        this.gratificacaoId = gratificacaoId;
    }

    /**
	 * @return the usuario
	 */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
	 * @param usuario
	 *            the usuario to set
	 */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
	 * @return the valorPago
	 */
    public Double getValorPago() {
        return valorPago;
    }

    /**
	 * @param valorPago
	 *            the valorPago to set
	 */
    public void setValorPago(Double valorPago) {
        this.valorPago = valorPago;
    }

    public Double getValorMovimentacao() {
        return getValorPago();
    }

    /**
	 * @return the observacao
	 */
    public String getObservacao() {
        return observacao;
    }

    /**
	 * @param observacao
	 *            the observacao to set
	 */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDescricaoItem() {
        return "Gratifica��o para o colaborador " + getUsuario().getNome();
    }

    public int compareTo(Gratificacao o) {
        return getDataRegistro().compareTo(o.getDataRegistro());
    }
}
