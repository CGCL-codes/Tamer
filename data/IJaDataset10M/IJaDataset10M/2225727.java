package br.gov.demoiselle.escola.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import br.gov.framework.demoiselle.core.bean.IPojo;

@Entity
@Table(name = "usuario")
public class Usuario implements IPojo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "sq_usuario")
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nome", length = 100)
    private String nome;

    @Column(name = "cpf", length = 100)
    private String cpf;

    @Column(name = "login", length = 100)
    private String login;

    @Column(name = "senha", length = 100)
    private String senha;

    @Column(name = "skin", length = 100)
    private String skin;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<PapelUsuario> papeis;

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Usuario() {
    }

    public Usuario(String nome, String login, String senha, String skin, Integer tipo) {
        super();
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.skin = skin;
    }

    public Usuario(String cpf) {
        this.cpf = cpf;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<PapelUsuario> getPapeis() {
        return papeis;
    }

    public void setPapeis(Set<PapelUsuario> papeis) {
        this.papeis = papeis;
    }

    public List<PapelUsuario> getListaPapeis() {
        return new ArrayList<PapelUsuario>(papeis);
    }
}
