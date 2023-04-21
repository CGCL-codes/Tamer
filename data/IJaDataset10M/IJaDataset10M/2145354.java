package br.com.prossys.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;

/**
 * Classe que representa a tabela empresa_candidata no banco de dados.
 * @author Victor Gutmann
 * @version 1.0
*/
@Entity
@Table(name = "Empresa_Candidata")
public class EmpresaCandidata implements Serializable {

    private Integer cdEmpresaCandidata;

    private String nomeFantasia;

    private String telefone1;

    private String email;

    private String telefone2;

    private String endereco;

    private String uf;

    private String cidade;

    private String bairro;

    private String pais;

    private String username;

    private String senha;

    private String cdCNPJ;

    private String razaoSocial;

    private String webpage;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Cd_Empresa_Candidata")
    public Integer getCdEmpresaCandidata() {
        return cdEmpresaCandidata;
    }

    public void setCdEmpresaCandidata(Integer cdEmpresaCandidata) {
        this.cdEmpresaCandidata = cdEmpresaCandidata;
    }

    @Column(name = "Nm_Fantasia")
    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    @Column(name = "Nu_Telefone1", nullable = false)
    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    @Column(name = "Nm_Email", nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "Nu_Telefone2")
    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    @Column(name = "En_Pessoa", nullable = false)
    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    @Column(name = "Sg_UF", nullable = false)
    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    @Column(name = "Nm_Cidade", nullable = false)
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Column(name = "Nm_Bairro", nullable = false)
    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    @Column(name = "Nm_Pais", nullable = false)
    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    @Column(name = "Nm_Username", nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "Cd_Senha", nullable = false)
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Column(name = "Cd_CNPJ", nullable = false)
    public String getCdCNPJ() {
        return cdCNPJ;
    }

    public void setCdCNPJ(String cdCNPJ) {
        this.cdCNPJ = cdCNPJ;
    }

    @Column(name = "Nm_Webpage")
    public String getWebpage() {
        return webpage;
    }

    public void setWebpage(String webpage) {
        this.webpage = webpage;
    }

    @Column(name = "Nm_Razao_Social", nullable = false)
    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }
}
