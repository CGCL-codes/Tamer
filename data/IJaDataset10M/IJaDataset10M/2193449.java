package Primeiro;

public class Video {

    private float taxaExtra;

    private String status;

    private int codigo;

    private String titulo;

    private float valor;

    private Locadora locadora;

    private Locacao locacao;

    public static final String ESTADO_DISPONIVEL = "Disponivel";

    public static final String ESTADO_ALUGADO = "Locado";

    public static final String ESTADO_DANIFICADO = "Danificado";

    public Video(int codigo, String titulo, float valor) {
        this.status = ESTADO_DISPONIVEL;
        this.codigo = codigo;
        this.titulo = titulo;
        this.valor = valor;
    }

    /**
     *  Metodo muito estranho. Ele so chama outro metodo... estranho
     *
     *
     * @return true caso locado com sucesso
     * @return false caso ocorra falha na locação
     */
    public void locar() {
        this.alterarStatus(ESTADO_ALUGADO);
    }

    public void devolver() {
    }

    public void buscarFilme() {
    }

    public float getTaxaExtra() {
        return taxaExtra;
    }

    public void setTaxaExtra(float taxaExtra) {
        this.taxaExtra = taxaExtra;
    }

    public String getStatus() {
        return status;
    }

    public void alterarStatus(String status) {
        if ((!status.equals(ESTADO_ALUGADO) && !status.equals(ESTADO_DANIFICADO) && !status.equals(ESTADO_DISPONIVEL)) || (status == null)) System.out.println("Não foi possível definir o estado"); else this.status = status;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Locadora getLocadora() {
        return locadora;
    }

    public void setLocadora(Locadora locadora) {
        this.locadora = locadora;
    }

    public Locacao getLocacao() {
        return locacao;
    }

    public void setLocacao(Locacao locacao) {
        this.locacao = locacao;
    }
}
