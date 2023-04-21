package modelo;

/**
 * Modelo da entidade marca produto.
 * @author Paulo Ilenga
 */
public class MarcaProdutoModelo {

    private int id;

    private String nome;

    public MarcaProdutoModelo() {
    }

    public MarcaProdutoModelo(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public MarcaProdutoModelo(String nome) {
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
