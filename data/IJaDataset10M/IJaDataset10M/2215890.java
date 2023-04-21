package compilador.analisadorLexico;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class PalavrasReservadas {

    public static final String CODIGO_NUMERO = "CODIGO_NUMERO";

    public static final String CODIGO_PONTUACAO = "CODIGO_PONTUACAO";

    public static final String CODIGO_PARENTISADOR = "CODIGO_PARENTISADOR";

    public static final String CODIGO_BOOLEAN = "CODIGO_BOOLEAN";

    public static final String CODIGO_SUPERTIPO = "CODIGO_SUPERTIPO";

    private Map<String, Integer> palavrasReservadas;

    public static final String CODIGO_VARIAVEL = "CODIGO_VARIAVEL";

    public static final String CODIGO_STRING = "CODIGO_STRING";

    public static final String CODIGO_METODO = "CODIGO_METODO";

    public static final String CODIGO_ARRAY = "CODIGO_ARRAY";

    /**
	 * Construtor da classe TabelaPalavrasReservadas que serve para
	 * construir a tabela de palavras reservadas.
	 * 
	 */
    public PalavrasReservadas() {
        palavrasReservadas = new TreeMap<String, Integer>();
        this.adicionaSimbolosBasicosLinguagem();
    }

    /**
	 * Adiciona os simbolos basicos da linguagem.
	 * 
	 */
    private void adicionaSimbolosBasicosLinguagem() {
        this.createCodigoIdentificador("<%");
        this.createCodigoIdentificador("%>");
        this.createCodigoIdentificador("dim");
        this.createCodigoIdentificador("if");
        this.createCodigoIdentificador("then");
        this.createCodigoIdentificador("else");
        this.createCodigoIdentificador("endif");
        this.createCodigoIdentificador("while");
        this.createCodigoIdentificador("endwhile");
        this.createCodigoIdentificador("sub");
        this.createCodigoIdentificador("endsub");
        this.createCodigoIdentificador("==");
        this.createCodigoIdentificador("=");
        this.createCodigoIdentificador("+");
        this.createCodigoIdentificador("-");
        this.createCodigoIdentificador("*");
        this.createCodigoIdentificador("/");
        this.createCodigoIdentificador("<>");
        this.createCodigoIdentificador("<");
        this.createCodigoIdentificador(">");
        this.createCodigoIdentificador("<=");
        this.createCodigoIdentificador(">=");
        this.createCodigoIdentificador(",");
        this.createCodigoIdentificador("const");
        this.createCodigoIdentificador("not");
        this.createCodigoIdentificador("(");
        this.createCodigoIdentificador(")");
        this.createCodigoIdentificador("sout");
        this.createCodigoIdentificador("sin");
        this.createCodigoIdentificador("and");
        this.createCodigoIdentificador("or");
        this.createCodigoIdentificador("[");
        this.createCodigoIdentificador("]");
        this.createCodigoIdentificador(CODIGO_NUMERO);
        this.createCodigoIdentificador(CODIGO_PONTUACAO);
        this.createCodigoIdentificador(CODIGO_PARENTISADOR);
        this.createCodigoIdentificador(CODIGO_VARIAVEL);
        this.createCodigoIdentificador(CODIGO_STRING);
        this.createCodigoIdentificador(CODIGO_METODO);
        this.createCodigoIdentificador(CODIGO_ARRAY);
    }

    /**
	 * Verifica se cont�m algum s�mbolo que faz parte do grupo de palavras
	 * reservadas.
	 * 
	 * @param simboloCurrent
	 * @return true se cont�m algum s�mbolo nas palavras reservadas.
	 */
    public boolean contem(String simboloCurrent) {
        Set<String> palavras_Reservadas = palavrasReservadas.keySet();
        return palavras_Reservadas.contains(simboloCurrent);
    }

    /**
	 * Tetorna o s�mbolo corrente.
	 * 
	 * @param simboloCurrent -
	 *            simbolo lido.
	 * @return int correspondente ao c�digo do s�mbolo.
	 */
    public int getCodigo(String simboloCurrent) {
        try {
            return palavrasReservadas.get(simboloCurrent);
        } catch (NullPointerException e) {
            return -1;
        }
    }

    /**
	 * cria a tabela com as palavras reservadas.
	 * 
	 * @param simboloCurrent -
	 *            simbolo lido.
	 */
    public void createCodigoIdentificador(String simboloCurrent) {
        palavrasReservadas.put(simboloCurrent, palavrasReservadas.keySet().size());
    }

    public String toString() {
        return palavrasReservadas.toString();
    }
}
