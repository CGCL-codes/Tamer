package br.com.xp.galera.domain;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "SENHA")
    private String senha;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "SEXO")
    private String sexo;

    @Column(name = "DATA_NASCIMENTO")
    private Date dataNascimento;

    @Column(name = "ESTADO_CIVIL")
    private String estadoCivil;

    @Column(name = "ULTIMO_LOGIN")
    private Date ultimoLogin;

    @Column(name = "URL_FOTO")
    private String urlFoto;

    @Column(name = "SOBRE")
    private String sobre;

    @OneToMany
    @JoinTable(name = "AMIGOS", joinColumns = { @JoinColumn(name = "USUARIO_ID_1") }, inverseJoinColumns = { @JoinColumn(name = "USUARIO_ID_2") })
    private Collection<Usuario> amigos;

    @ManyToMany
    @JoinTable(name = "USUARIO_COMUNIDADE", joinColumns = { @JoinColumn(name = "USUARIO_ID") }, inverseJoinColumns = { @JoinColumn(name = "COMUNIDADE_ID") })
    private Collection<Comunidade> comunidades;

    public Collection<Comunidade> getComunidades() {
        return comunidades;
    }

    public void setComunidades(Collection<Comunidade> comunidades) {
        this.comunidades = comunidades;
    }

    public Collection<Usuario> getAmigos() {
        return amigos;
    }

    public void setAmigos(Collection<Usuario> amigos) {
        this.amigos = amigos;
    }

    public Integer getIdade() {
        Integer idade;
        Calendar dataCorrente = GregorianCalendar.getInstance();
        Calendar dataNasc = GregorianCalendar.getInstance();
        dataNasc.setTimeInMillis(getDataNascimento().getTime());
        idade = dataCorrente.get(Calendar.YEAR) - dataNasc.get(Calendar.YEAR);
        if (dataCorrente.get(Calendar.MONTH) < dataNasc.get(Calendar.MONTH)) {
            idade--;
        } else if (dataCorrente.get(Calendar.MONTH) == dataNasc.get(Calendar.MONTH) && dataCorrente.get(Calendar.DAY_OF_MONTH) < dataNasc.get(Calendar.DAY_OF_MONTH)) {
            idade--;
        }
        return idade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public Date getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(Date ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
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

    public String getUrlFoto() {
        return urlFoto;
    }

    public void setUrlFoto(String urlFoto) {
        this.urlFoto = urlFoto;
    }

    public String getSobre() {
        return sobre;
    }

    public void setSobre(String sobre) {
        this.sobre = sobre;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
