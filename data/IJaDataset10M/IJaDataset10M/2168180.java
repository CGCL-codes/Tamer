package br.usp.lexico;

/**
 *
 * @author nathalia, Bruno Grisi
 */
public class Simbolo {

    private int codigo;

    private String nome;

    private String tipo;

    private String categoria;

    private boolean declarado = false;

    public static final String FUNCAO = "FUNCAO   ";

    public static final String VETOR = "VETOR    ";

    public static final String PARAMETRO = "PARAMETRO";

    public static final String VARIAVEL = "VARIAVEL";

    public Simbolo(int codigo, String nome, String tipo) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean getDeclarado() {
        return declarado;
    }

    public void setDeclarado() {
        this.declarado = true;
    }
}
