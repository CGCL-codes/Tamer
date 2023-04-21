package org.weras.portal.clientes.domain.app.hemocentro;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.weras.portal.clientes.domain.ObjetoPersistente;

@Entity
@Table(name = "Guia")
public class Guia extends ObjetoPersistente {

    private static final long serialVersionUID = 1L;

    private Integer registroANS;

    private Integer numero;

    private Date dataEmissao;

    private Long numeroCarteira;

    private Date validadeCarteira;

    private String nome;

    private String codigoOperadora;

    private String nomeContratadoSolicitante;

    private String nomeProfissionalSolicitante;

    private String conselhoSolicitante;

    private Integer numeroConselho;

    private String uf;

    private Boolean caraterUrgencia;

    private String indicacaoClinica;

    public Integer getRegistroANS() {
        return registroANS;
    }

    public void setRegistroANS(Integer registroANS) {
        this.registroANS = registroANS;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public Long getNumeroCarteira() {
        return numeroCarteira;
    }

    public void setNumeroCarteira(Long numeroCarteira) {
        this.numeroCarteira = numeroCarteira;
    }

    public Date getValidadeCarteira() {
        return validadeCarteira;
    }

    public void setValidadeCarteira(Date validadeCarteira) {
        this.validadeCarteira = validadeCarteira;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoOperadora() {
        return codigoOperadora;
    }

    public void setCodigoOperadora(String codigoOperadora) {
        this.codigoOperadora = codigoOperadora;
    }

    public String getNomeContratadoSolicitante() {
        return nomeContratadoSolicitante;
    }

    public void setNomeContratadoSolicitante(String nomeContratadoSolicitante) {
        this.nomeContratadoSolicitante = nomeContratadoSolicitante;
    }

    public String getNomeProfissionalSolicitante() {
        return nomeProfissionalSolicitante;
    }

    public void setNomeProfissionalSolicitante(String nomeProfissionalSolicitante) {
        this.nomeProfissionalSolicitante = nomeProfissionalSolicitante;
    }

    public String getConselhoSolicitante() {
        return conselhoSolicitante;
    }

    public void setConselhoSolicitante(String conselhoSolicitante) {
        this.conselhoSolicitante = conselhoSolicitante;
    }

    public Integer getNumeroConselho() {
        return numeroConselho;
    }

    public void setNumeroConselho(Integer numeroConselho) {
        this.numeroConselho = numeroConselho;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public Boolean getCaraterUrgencia() {
        return caraterUrgencia;
    }

    public void setCaraterUrgencia(Boolean caraterUrgencia) {
        this.caraterUrgencia = caraterUrgencia;
    }

    public String getIndicacaoClinica() {
        return indicacaoClinica;
    }

    public void setIndicacaoClinica(String indicacaoClinica) {
        this.indicacaoClinica = indicacaoClinica;
    }
}
