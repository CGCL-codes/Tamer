package br.com.fiapbank.dominio.entidade;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * Representa um cliente simples de uma conta bancaria
 * 
 * @author robson.oliveira
 * 
 * @see {@link Conta} {@link Endereco} {@link Renda} 
 *
 */
@Entity
@Table(name = "CLIENTE")
public class Cliente {

    @Id
    @Column(name = "ID")
    @GeneratedValue
    private Long id;

    @Column(name = "NOME", nullable = false)
    private String nome;

    @Column(name = "RG", nullable = false)
    private String rg;

    @Column(name = "CPF", nullable = false)
    private String cpf;

    @OneToOne(mappedBy = "cliente")
    private Conta conta;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "RENDA_ID")
    private Renda renda;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ENDERECO_ID")
    private Endereco endereco;

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

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Conta getConta() {
        return conta;
    }

    public void setConta(Conta conta) {
        this.conta = conta;
    }

    public Renda getRenda() {
        return renda;
    }

    public void setRenda(Renda renda) {
        this.renda = renda;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
