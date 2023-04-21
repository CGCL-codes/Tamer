package br.com.hsj.importador.entidades.wazzup;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the tbusuario database table.
 * 
 */
@Entity
@Table(name = "tbusuario")
public class Tbusuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cod_usu", unique = true, nullable = false)
    private int codUsu;

    @Column(columnDefinition = "enum('Y','N')")
    private String alterarloja;

    @Column(columnDefinition = "enum('Y','N')")
    private String alterartabela;

    @Column(columnDefinition = "enum('Y','N')")
    private String alterarvendedor;

    @Column(columnDefinition = "enum('Y','N')")
    private String ativarbloqueio;

    @Temporal(TemporalType.DATE)
    private Date bloqueio;

    @Column(name = "cod_dep")
    private int codDep;

    @Column(name = "des_usu", nullable = false, length = 80)
    private String desUsu;

    private int etiqpadrao;

    private int idcliente;

    private int idconta;

    private int idloja;

    private int idtabela;

    private int idvendedor;

    private int nivel;

    @Column(nullable = false, length = 20)
    private String senha;

    @Column(columnDefinition = "enum('Y','N')")
    private String sugestao;

    @Lob()
    @Column(name = "ult_atu")
    private String ultAtu;

    public Tbusuario() {
    }

    public int getCodUsu() {
        return this.codUsu;
    }

    public void setCodUsu(int codUsu) {
        this.codUsu = codUsu;
    }

    public String getAlterarloja() {
        return this.alterarloja;
    }

    public void setAlterarloja(String alterarloja) {
        this.alterarloja = alterarloja;
    }

    public String getAlterartabela() {
        return this.alterartabela;
    }

    public void setAlterartabela(String alterartabela) {
        this.alterartabela = alterartabela;
    }

    public String getAlterarvendedor() {
        return this.alterarvendedor;
    }

    public void setAlterarvendedor(String alterarvendedor) {
        this.alterarvendedor = alterarvendedor;
    }

    public String getAtivarbloqueio() {
        return this.ativarbloqueio;
    }

    public void setAtivarbloqueio(String ativarbloqueio) {
        this.ativarbloqueio = ativarbloqueio;
    }

    public Date getBloqueio() {
        return this.bloqueio;
    }

    public void setBloqueio(Date bloqueio) {
        this.bloqueio = bloqueio;
    }

    public int getCodDep() {
        return this.codDep;
    }

    public void setCodDep(int codDep) {
        this.codDep = codDep;
    }

    public String getDesUsu() {
        return this.desUsu;
    }

    public void setDesUsu(String desUsu) {
        this.desUsu = desUsu;
    }

    public int getEtiqpadrao() {
        return this.etiqpadrao;
    }

    public void setEtiqpadrao(int etiqpadrao) {
        this.etiqpadrao = etiqpadrao;
    }

    public int getIdcliente() {
        return this.idcliente;
    }

    public void setIdcliente(int idcliente) {
        this.idcliente = idcliente;
    }

    public int getIdconta() {
        return this.idconta;
    }

    public void setIdconta(int idconta) {
        this.idconta = idconta;
    }

    public int getIdloja() {
        return this.idloja;
    }

    public void setIdloja(int idloja) {
        this.idloja = idloja;
    }

    public int getIdtabela() {
        return this.idtabela;
    }

    public void setIdtabela(int idtabela) {
        this.idtabela = idtabela;
    }

    public int getIdvendedor() {
        return this.idvendedor;
    }

    public void setIdvendedor(int idvendedor) {
        this.idvendedor = idvendedor;
    }

    public int getNivel() {
        return this.nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public String getSenha() {
        return this.senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSugestao() {
        return this.sugestao;
    }

    public void setSugestao(String sugestao) {
        this.sugestao = sugestao;
    }

    public String getUltAtu() {
        return this.ultAtu;
    }

    public void setUltAtu(String ultAtu) {
        this.ultAtu = ultAtu;
    }
}
