package br.edu.uncisal.farmacia.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;

/**
 * Indivíduos capacitados a fornecer produtos para o almoxarifado.
 * 
 * @author Augusto Oliveira
 * @author Igor Cavalcante
 * @see Endereco
 */
@Entity
@SequenceGenerator(name = "SEQ_CLOG", sequenceName = "sq_fornecedor")
public class Fornecedor extends Domain implements Serializable {

    private static final long serialVersionUID = -4668209780537312014L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "SEQ_CLOG")
    private Long id;

    @Column(length = 100)
    private String nome;

    @Column(name = "cpf_cnpj", length = 18)
    private String cpfCnpj;

    @Column(name = "razao_social", length = 100)
    private String razaoSocial;

    @Column(name = "inscricao_estadual", length = 18)
    private String inscricaoEstadual;

    @Column(length = 100)
    private String email;

    @Column(length = 14)
    private String telefone;

    @Column(length = 14)
    private String fax;

    @OneToOne(cascade = CascadeType.ALL)
    private Endereco Endereco;

    private String observacao;

    @JoinTable(name = "grupos_fornecedores", joinColumns = { @JoinColumn(name = "fornecedor_id", referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(name = "grupo_id", referencedColumnName = "id") })
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Grupo> grupos;

    @ManyToOne
    @JoinColumn(name = "tipo_fornecedor_id")
    private TipoFornecedor tipoFornecedor;

    /**
     * Define se a entidade é uma pessoa física ou jurídica.
     */
    @Column(name = "pessoa_juridica")
    private Boolean pessoaJuridica;

    @Column(length = 50)
    private String contato;

    @Column(name = "telefone_contato", length = 14)
    private String telefoneContato;

    @Override
    public Long getId() {
        return this.id;
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

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public Endereco getEndereco() {
        return Endereco;
    }

    public void setEndereco(Endereco endereco) {
        Endereco = endereco;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(List<Grupo> grupos) {
        this.grupos = grupos;
    }

    public Boolean getPessoaJuridica() {
        return pessoaJuridica;
    }

    public void setPessoaJuridica(Boolean pessoaJuridica) {
        this.pessoaJuridica = pessoaJuridica;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getTelefoneContato() {
        return telefoneContato;
    }

    public void setTelefoneContato(String telefoneContato) {
        this.telefoneContato = telefoneContato;
    }

    public TipoFornecedor getTipoFornecedor() {
        return tipoFornecedor;
    }

    public void setTipoFornecedor(TipoFornecedor tipoFornecedor) {
        this.tipoFornecedor = tipoFornecedor;
    }

    @Override
    public String toString() {
        return "[id: " + id + " nome: " + nome + " cpf/cnpj: " + cpfCnpj + " razao social: " + razaoSocial + " I.E: " + inscricaoEstadual + " e-mail: " + email + " telefone: " + telefone + " fax: " + fax + " endereco: " + Endereco + " observação: " + observacao + "]";
    }
}
