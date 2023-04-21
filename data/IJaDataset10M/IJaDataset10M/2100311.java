package gov.gemat.sejus.beans;

public class ContatoBean {

    private Integer idContado;

    private String nome;

    private String email;

    private String telefone;

    private String endereco;

    private String cidade;

    private Integer idUF;

    public final Integer getIdContado() {
        return idContado;
    }

    public final void setIdContado(Integer idContado) {
        this.idContado = idContado;
    }

    public final String getNome() {
        return nome;
    }

    public final void setNome(String nome) {
        this.nome = nome;
    }

    public final String getEmail() {
        return email;
    }

    public final void setEmail(String email) {
        this.email = email;
    }

    public final String getTelefone() {
        return telefone;
    }

    public final void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public final String getEndereco() {
        return endereco;
    }

    public final void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public final String getCidade() {
        return cidade;
    }

    public final void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public final Integer getIdUF() {
        return idUF;
    }

    public final void setIdUF(Integer idUF) {
        this.idUF = idUF;
    }
}
