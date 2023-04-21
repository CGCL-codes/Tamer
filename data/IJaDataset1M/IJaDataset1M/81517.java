package br.com.locadora.bean;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "seq_id_clientes", sequenceName = "seq_id_clientes", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_id_clientes")
    @Column(name = "id_cliente")
    private Integer idCliente;

    private String cel;

    private String cpf;

    private String fone;

    private String nome;

    private String rg;

    private String sobrenome;

    @ManyToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @OneToMany(mappedBy = "cliente")
    private List<Locacao> locacoes;

    public Cliente() {
    }

    public final Integer getIdCliente() {
        return this.idCliente;
    }

    public final void setIdCliente(Integer idCliente) {
        this.idCliente = idCliente;
    }

    public final String getCel() {
        return this.cel;
    }

    public final void setCel(String cel) {
        this.cel = cel;
    }

    public final String getCpf() {
        return this.cpf;
    }

    public final void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public final String getFone() {
        return this.fone;
    }

    public final void setFone(String fone) {
        this.fone = fone;
    }

    public final String getNome() {
        return this.nome;
    }

    public final void setNome(String nome) {
        this.nome = nome;
    }

    public final String getRg() {
        return this.rg;
    }

    public final void setRg(String rg) {
        this.rg = rg;
    }

    public final String getSobrenome() {
        return this.sobrenome;
    }

    public final void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public final Endereco getEndereco() {
        return this.endereco;
    }

    public final void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public final List<Locacao> getLocacoes() {
        return this.locacoes;
    }

    public final void setLocacoes(List<Locacao> locacoes) {
        this.locacoes = locacoes;
    }
}
