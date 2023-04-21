package org.jw.web.rdc.integration.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * AssociacaoJuridica entity. @author MyEclipse Persistence Tools
 */
public class AssociacaoJuridica implements java.io.Serializable {

    private Long cnpj;

    private String razaoSocial;

    private String logradouro;

    private Integer numero;

    private String compl;

    private String cidade;

    private String estado;

    private String cep;

    private boolean mesmoEnderecoCorresp;

    private String enderecoCorresp;

    private Integer numeroCorresp;

    private String complementoCorresp;

    private String cidadeCorresp;

    private String estadoCorresp;

    private String cepCorresp;

    private Long caixaPostalCorresp;

    private String comentarios;

    private Saj saj;

    private Set obras = new HashSet(0);

    private Set diretorAJs = new HashSet(0);

    private Set<ContaAJ> contaAJs = new HashSet<ContaAJ>(0);

    /** default constructor */
    public AssociacaoJuridica() {
    }

    /** minimal constructor */
    public AssociacaoJuridica(Saj saj) {
        this.saj = saj;
    }

    /** full constructor */
    public AssociacaoJuridica(Saj saj, String razaoSocial, String logradouro, Integer numero, String compl, String cidade, String estado, String cep, String enderecoCorresp, Integer numeroCorresp, String complementoCorresp, String cidadeCorresp, String estadoCorresp, String cepCorresp, Long caixaPostalCorresp, String comentarios, Set obras, Set diretorAJs, Set contaAJs) {
        this.saj = saj;
        this.razaoSocial = razaoSocial;
        this.logradouro = logradouro;
        this.numero = numero;
        this.compl = compl;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
        this.enderecoCorresp = enderecoCorresp;
        this.numeroCorresp = numeroCorresp;
        this.complementoCorresp = complementoCorresp;
        this.cidadeCorresp = cidadeCorresp;
        this.estadoCorresp = estadoCorresp;
        this.cepCorresp = cepCorresp;
        this.caixaPostalCorresp = caixaPostalCorresp;
        this.comentarios = comentarios;
        this.obras = obras;
        this.diretorAJs = diretorAJs;
        this.contaAJs = contaAJs;
    }

    public Long getCnpj() {
        return this.cnpj;
    }

    public void setCnpj(Long cnpj) {
        this.cnpj = cnpj;
    }

    public Saj getSaj() {
        return this.saj;
    }

    public void setSaj(Saj saj) {
        this.saj = saj;
    }

    public String getRazaoSocial() {
        return this.razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getLogradouro() {
        return this.logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return this.numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCompl() {
        return this.compl;
    }

    public void setCompl(String compl) {
        this.compl = compl;
    }

    public String getCidade() {
        return this.cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return this.cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return Retorna o valor do campo 'mesmoEnderecoCorresp'.
     */
    public boolean isMesmoEnderecoCorresp() {
        return this.mesmoEnderecoCorresp;
    }

    /**
     * @param mesmoEnderecoCorresp Determina o novo valor do campo 'mesmoEnderecoCorresp'.
     */
    public void setMesmoEnderecoCorresp(boolean mesmoEnderecoCorresp) {
        this.mesmoEnderecoCorresp = mesmoEnderecoCorresp;
    }

    public String getEnderecoCorresp() {
        return this.enderecoCorresp;
    }

    public void setEnderecoCorresp(String enderecoCorresp) {
        this.enderecoCorresp = enderecoCorresp;
    }

    public Integer getNumeroCorresp() {
        return this.numeroCorresp;
    }

    public void setNumeroCorresp(Integer numeroCorresp) {
        this.numeroCorresp = numeroCorresp;
    }

    public String getComplementoCorresp() {
        return this.complementoCorresp;
    }

    public void setComplementoCorresp(String complementoCorresp) {
        this.complementoCorresp = complementoCorresp;
    }

    public String getCidadeCorresp() {
        return this.cidadeCorresp;
    }

    public void setCidadeCorresp(String cidadeCorresp) {
        this.cidadeCorresp = cidadeCorresp;
    }

    public String getEstadoCorresp() {
        return this.estadoCorresp;
    }

    public void setEstadoCorresp(String estadoCorresp) {
        this.estadoCorresp = estadoCorresp;
    }

    public String getCepCorresp() {
        return this.cepCorresp;
    }

    public void setCepCorresp(String cepCorresp) {
        this.cepCorresp = cepCorresp;
    }

    public Long getCaixaPostalCorresp() {
        return this.caixaPostalCorresp;
    }

    public void setCaixaPostalCorresp(Long caixaPostalCorresp) {
        this.caixaPostalCorresp = caixaPostalCorresp;
    }

    public String getComentarios() {
        return this.comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    public Set getObras() {
        return this.obras;
    }

    public void setObras(Set obras) {
        this.obras = obras;
    }

    public Set getDiretorAJs() {
        return this.diretorAJs;
    }

    public void setDiretorAJs(Set diretorAJs) {
        this.diretorAJs = diretorAJs;
    }

    public Set<ContaAJ> getContaAJs() {
        return this.contaAJs;
    }

    public void setContaAJs(Set<ContaAJ> contaAJs) {
        this.contaAJs = contaAJs;
    }
}
