package br.com.primolltec.bean;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Endereco")
public class Endereco implements Serializable {

    private static final long serialVersionUID = 4760850530952867865L;

    public Endereco() {
        super();
    }

    private int id;

    private String rua;

    private String bairro;

    private String cidade;

    private int numero;

    private String cep;

    private String referencia;

    @Id
    @GeneratedValue
    public int getId() {
        return id;
    }

    private void setId(int id) {
        this.id = id;
    }

    @Column(name = "rua", nullable = false, length = 30)
    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    @Column(name = "bairro", nullable = false, length = 30)
    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    @Column(name = "cidade", nullable = false, length = 30)
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Column(name = "numero", nullable = false, length = 9)
    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    @Column(name = "cep", nullable = true, length = 8)
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Column(name = "referencia", nullable = true, length = 255)
    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String toString() {
        return String.valueOf("Endereco " + getId());
    }
}
