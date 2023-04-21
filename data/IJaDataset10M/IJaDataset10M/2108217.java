package minimundo.automobilismo;

/**
 *
 * @author Lucas
 */
public class Combustivel {

    private String nome;

    private String composicao;

    private double preco;

    public Combustivel() {
    }

    /**
     * Método que retorna o nome
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método para atribuir o nome
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Método que retorna a composicao
     * @return composicao
     */
    public String getComposicao() {
        return composicao;
    }

    /**
     * Método para atribuir a composicao
     * @param composicao
     */
    public void setComposicao(String composicao) {
        this.composicao = composicao;
    }

    /**
     * Método que retorna o preco
     * @return preco
     */
    public double getPreco() {
        return preco;
    }

    /**
     * Método para atribuir o preco
     * @param preco
     */
    public void setPreco(double preco) {
        this.preco = preco;
    }
}
