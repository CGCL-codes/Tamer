package java.variavel;

public class Variavel {

    String nome = null;

    String tipo = null;

    public Variavel(String tipo, String nome) {
        this.nome = nome;
        this.tipo = tipo;
    }

    public String codigoFonte() {
        return tipo + " " + nome;
    }

    public String getNome() {
        return this.nome;
    }
}
