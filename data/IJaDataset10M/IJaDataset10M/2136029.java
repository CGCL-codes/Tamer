package jbanco;

/**
 *
 * @author Chakal
 */
public class Pessoa {

    private String nome;

    private String endereco;

    private String cidade;

    private String bairro;

    private String estado;

    private String email;

    private String telefone;

    public Pessoa() {
        nome = null;
        endereco = null;
        cidade = null;
        bairro = null;
        estado = null;
        email = null;
        telefone = null;
    }

    public Pessoa(String nome, String telefone, String cidade) {
        this.nome = nome;
        endereco = null;
        this.cidade = cidade;
        bairro = null;
        estado = null;
        email = null;
        this.telefone = telefone;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String end) {
        this.endereco = end;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
}
