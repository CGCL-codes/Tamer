package br.gov.sample.demoiselle.escola.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import br.gov.framework.demoiselle.core.bean.IPojo;

/**
 * Esta classe mapeia a tabela aluno por anotações
 * 
 * @author SERPRO/CETEC/CTCTA
 */
@Entity
@Table(name = "aluno")
@SequenceGenerator(name = "sq_aluno", sequenceName = "sq_aluno")
public class Aluno implements IPojo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sq_aluno")
    @Column(name = "id_aluno")
    private Long id;

    @Column(length = 50)
    private String nome;

    @Column(length = 50)
    private String pai;

    @Column(length = 50)
    private String mae;

    @Temporal(value = TemporalType.DATE)
    private Date nascimento;

    @Column(length = 100)
    private String foto;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Endereco> enderecos;

    @ManyToMany
    private Set<Turma> turmas;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Contato contato;

    @Column(length = 11)
    private String cpf;

    public Aluno() {
        enderecos = new HashSet<Endereco>();
        contato = new Contato();
        contato.setTelefones(new HashSet<Telefone>());
        contato.setEmails(new HashSet<Email>());
        turmas = new HashSet<Turma>();
    }

    public Aluno(long id) {
        this();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPai() {
        return pai;
    }

    public void setPai(String pai) {
        this.pai = pai;
    }

    public String getMae() {
        return mae;
    }

    public void setMae(String mae) {
        this.mae = mae;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public Set<Endereco> getEnderecos() {
        return enderecos;
    }

    public List<Endereco> getListaEndereco() {
        return new ArrayList<Endereco>(enderecos);
    }

    public List<Turma> getListaTurma() {
        return new ArrayList<Turma>(turmas);
    }

    public void setEnderecos(Set<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    public Set<Turma> getTurmas() {
        return turmas;
    }

    public void setTurmas(Set<Turma> turmas) {
        this.turmas = turmas;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Contato getContato() {
        return contato;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Aluno [id=" + id + ", nome=" + nome + "]";
    }
}
