package util;

/**
 * Formatador.java
 * Classe responsável pela centralização dos métodos de
 * formatação e conversão de tipos numéricos.
 *
 * @author parceirokid e ednilo
 */
public class Formatador {

    /**
     * Transforma uma String que representa um valor double
     * no padrão brasileiro de pontuação (ponto como separador de
     * milhar e virgula como separador decial) e converte para
     * o tipo de dados Double.
     * @param valor Valor double após a formatação.
     * @return
     */
    public static Double formatarDouble(String valor) {
        valor = valor.replace(".", "");
        valor = valor.replace(",", ".");
        return new Double(valor);
    }
}
