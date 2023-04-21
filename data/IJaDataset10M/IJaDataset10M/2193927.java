package analiseSintatica.Regras.Expressoes;

import AnaliseLexicaFinal.EstruturaDeDados.LinkToken;
import analiseSintatica.ErroSintaticoException;
import analiseSintatica.Regras.Regra;
import analiseSintatica.estruturadados.NoArvore;
import java.util.LinkedList;

/**
 *
 * @author matheus
 */
public class RegraOpIncremento extends Regra {

    protected static Regra instancia;

    protected String estrutura = "'++' | '--'";

    /**
     * Construtor privado necessario ao singleton
     */
    private RegraOpIncremento() {
    }

    /**
     * Retorna a unica instancia dessa classe.
     * @return
     */
    public static RegraOpIncremento getInstance() {
        if (instancia == null) {
            instancia = new RegraOpIncremento();
        }
        return (RegraOpIncremento) instancia;
    }

    /**
     * Analisando o token atual. Essa regra eh bem simples, pois soh tem
     * terminais
     * @param listaTokens
     * @return
     * @throws Exception
     */
    @Override
    public NoArvore analisar(LinkedList<LinkToken> listaTokens) throws ErroSintaticoException {
        NoArvore naoterminalOpIncremento = new NoArvore("<op_incremento>", false);
        if (listaTokens.getFirst().getToken().equals("++")) {
            naoterminalOpIncremento.adicionaFilho(reconhecerOpIncrementoadicao(listaTokens));
        } else {
            naoterminalOpIncremento.adicionaFilho(reconhecerOpIncrementosubtracao(listaTokens));
        }
        return naoterminalOpIncremento;
    }

    /**
     * Reconhece terminal "++"
     * @param tokens - Tokens do codigo
     * @return - Sub-arvore sintatica
     */
    public NoArvore reconhecerOpIncrementoadicao(LinkedList<LinkToken> tokens) {
        tokens.removeFirst();
        return new NoArvore("++", true);
    }

    /**
     * Reconhece terminal "--"
     * @param tokens - Tokens do codigo
     * @return - Sub-arvore sintatica
     */
    public NoArvore reconhecerOpIncrementosubtracao(LinkedList<LinkToken> tokens) {
        tokens.removeFirst();
        return new NoArvore("--", true);
    }

    @Override
    protected void setPrimeiro() {
        this.primeiro.add("++");
        this.primeiro.add("--");
    }

    @Override
    protected void setSeguinte() {
        seguinte.add(";");
        seguinte.add("+");
        seguinte.add("-");
        seguinte.add("<");
        seguinte.add("<=");
        seguinte.add(">");
        seguinte.add(">=");
        seguinte.add("==");
        seguinte.add("!=");
        seguinte.add("&&");
        seguinte.add(";");
        seguinte.add(",");
        seguinte.add("}");
        seguinte.add(")");
        seguinte.add("(");
        seguinte.add("{");
        seguinte.add("identificador");
        seguinte.add("numeroInteiro");
        seguinte.add("numeroFlutuante");
        seguinte.add("charconst");
        seguinte.add("++");
        seguinte.add("--");
        seguinte.add("&");
        seguinte.add("*");
    }
}
