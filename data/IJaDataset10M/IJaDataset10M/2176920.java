package br.com.lopes.gci.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * Funcionario entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "funcionario", catalog = "gcibd")
public class Funcionario implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8456371238511876862L;

    @Id
    @Column(name = "codigo", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "perfil")
    private Perfil perfil = new Perfil();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diretor")
    private Funcionario diretor;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status")
    private Dominio status = new Dominio();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gerente")
    private Funcionario gerente;

    @Column(name = "email", length = 60)
    private String email;

    @Column(name = "cpf", length = 11)
    private String cpf;

    @Column(name = "nome", length = 50)
    private String nome;

    @Column(name = "telefone_comercial", length = 12)
    private String telefoneComercial;

    @Column(name = "ramal", length = 5)
    private String ramal;

    @Column(name = "telefone_residencial", length = 12)
    private String telefoneResidencial;

    @Column(name = "telefone_celular", length = 12)
    private String telefoneCelular;

    @Column(name = "senha", length = 100)
    private String senha;

    @Column(name = "rg", length = 25)
    private String rg;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco")
    private Endereco endereco = new Endereco();

    @Column(name = "login_func", nullable = false, length = 30)
    private String loginFunc;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "gerente")
    private List<Funcionario> funcionariosPorGerente = new ArrayList<Funcionario>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "diretor")
    private List<Funcionario> funcionariosPorDiretor = new ArrayList<Funcionario>();

    /** default constructor */
    public Funcionario() {
    }

    public Funcionario(Integer codigo) {
        this.codigo = codigo;
    }

    /** minimal constructor */
    public Funcionario(Integer codigo, String loginFunc) {
        this.codigo = codigo;
        this.loginFunc = loginFunc;
    }

    /** full constructor */
    public Funcionario(Integer codigo, Perfil perfil, Funcionario funcionarioByDiretor, Dominio status, Funcionario funcionarioByGerente, String email, String cpf, String nome, String telefoneComercial, String ramal, String telefoneResidencial, String telefoneCelular, String senha, String rg, Endereco endereco, String loginFunc) {
        this.codigo = codigo;
        this.perfil = perfil;
        this.diretor = funcionarioByDiretor;
        this.status = status;
        this.gerente = funcionarioByGerente;
        this.email = email;
        this.cpf = cpf;
        this.nome = nome;
        this.telefoneComercial = telefoneComercial;
        this.ramal = ramal;
        this.telefoneResidencial = telefoneResidencial;
        this.telefoneCelular = telefoneCelular;
        this.senha = senha;
        this.rg = rg;
        this.endereco = endereco;
        this.loginFunc = loginFunc;
    }

    /**
	 * @return the codigo
	 */
    public Integer getCodigo() {
        return codigo;
    }

    /**
	 * @return the perfil
	 */
    public Perfil getPerfil() {
        return perfil;
    }

    /**
	 * @return the diretor
	 */
    public Funcionario getDiretor() {
        return diretor;
    }

    /**
	 * @return the status
	 */
    public Dominio getStatus() {
        return status;
    }

    /**
	 * @return the gerente
	 */
    public Funcionario getGerente() {
        return gerente;
    }

    /**
	 * @return the email
	 */
    public String getEmail() {
        return email;
    }

    /**
	 * @return the cpf
	 */
    public String getCpf() {
        return cpf;
    }

    /**
	 * @return the nome
	 */
    public String getNome() {
        return nome;
    }

    /**
	 * @return the telefoneComercial
	 */
    public String getTelefoneComercial() {
        return telefoneComercial;
    }

    /**
	 * @return the ramal
	 */
    public String getRamal() {
        return ramal;
    }

    /**
	 * @return the telefoneResidencial
	 */
    public String getTelefoneResidencial() {
        return telefoneResidencial;
    }

    /**
	 * @return the telefoneCelular
	 */
    public String getTelefoneCelular() {
        return telefoneCelular;
    }

    /**
	 * @return the senha
	 */
    public String getSenha() {
        return senha;
    }

    /**
	 * @return the rg
	 */
    public String getRg() {
        return rg;
    }

    /**
	 * @return the endereco
	 */
    public Endereco getEndereco() {
        return endereco;
    }

    /**
	 * @return the loginFunc
	 */
    public String getLoginFunc() {
        return loginFunc;
    }

    /**
	 * @return the funcionariosPorGerente
	 */
    public List<Funcionario> getFuncionariosPorGerente() {
        return funcionariosPorGerente;
    }

    /**
	 * @return the funcionariosPorDiretor
	 */
    public List<Funcionario> getFuncionariosPorDiretor() {
        return funcionariosPorDiretor;
    }

    /**
	 * @param codigo the codigo to set
	 */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    /**
	 * @param perfil the perfil to set
	 */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    /**
	 * @param diretor the diretor to set
	 */
    public void setDiretor(Funcionario diretor) {
        this.diretor = diretor;
    }

    /**
	 * @param status the status to set
	 */
    public void setStatus(Dominio status) {
        this.status = status;
    }

    /**
	 * @param gerente the gerente to set
	 */
    public void setGerente(Funcionario gerente) {
        this.gerente = gerente;
    }

    /**
	 * @param email the email to set
	 */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * @param cpf the cpf to set
	 */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
	 * @param nome the nome to set
	 */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
	 * @param telefoneComercial the telefoneComercial to set
	 */
    public void setTelefoneComercial(String telefoneComercial) {
        this.telefoneComercial = telefoneComercial;
    }

    /**
	 * @param ramal the ramal to set
	 */
    public void setRamal(String ramal) {
        this.ramal = ramal;
    }

    /**
	 * @param telefoneResidencial the telefoneResidencial to set
	 */
    public void setTelefoneResidencial(String telefoneResidencial) {
        this.telefoneResidencial = telefoneResidencial;
    }

    /**
	 * @param telefoneCelular the telefoneCelular to set
	 */
    public void setTelefoneCelular(String telefoneCelular) {
        this.telefoneCelular = telefoneCelular;
    }

    /**
	 * @param senha the senha to set
	 */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
	 * @param rg the rg to set
	 */
    public void setRg(String rg) {
        this.rg = rg;
    }

    /**
	 * @param endereco the endereco to set
	 */
    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    /**
	 * @param loginFunc the loginFunc to set
	 */
    public void setLoginFunc(String loginFunc) {
        this.loginFunc = loginFunc;
    }

    /**
	 * @param funcionariosPorGerente the funcionariosPorGerente to set
	 */
    public void setFuncionariosPorGerente(List<Funcionario> funcionariosPorGerente) {
        this.funcionariosPorGerente = funcionariosPorGerente;
    }

    /**
	 * @param funcionariosPorDiretor the funcionariosPorDiretor to set
	 */
    public void setFuncionariosPorDiretor(List<Funcionario> funcionariosPorDiretor) {
        this.funcionariosPorDiretor = funcionariosPorDiretor;
    }
}
