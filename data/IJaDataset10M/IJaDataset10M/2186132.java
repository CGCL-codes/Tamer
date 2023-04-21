package analiseSintatica.Regras.Blocos;

import AnaliseLexicaFinal.EstruturaDeDados.LinkToken;
import analiseSintatica.ErroSintaticoException;
import analiseSintatica.Regras.Funcao.RegraFuncao;
import analiseSintatica.Regras.Regra;
import analiseSintatica.estruturadados.NoArvore;
import java.util.LinkedList;

/**
 *
 * @author amferraz
 */
public class RegraDeclsFinal extends Regra {

    protected static Regra instancia;

    /**
     * Construtor privado necessario ao singleton
     */
    private RegraDeclsFinal() {
    }

    /**
     * Retorna a unica instancia dessa classe.
     * @return
     */
    public static RegraDeclsFinal getInstance() {
        if (instancia == null) {
            instancia = new RegraDeclsFinal();
        }
        return (RegraDeclsFinal) instancia;
    }

    @Override
    public NoArvore analisar(LinkedList<LinkToken> listaTokens) throws ErroSintaticoException {
        NoArvore terminalDeclFinal = new NoArvore("<decls_final>", false);
        while (RegraFuncao.getInstance().getPrimeiro().contains(listaTokens.getFirst().getToken()) || RegraFuncao.getInstance().getPrimeiro().contains(listaTokens.getFirst().getLexema())) {
            try {
                terminalDeclFinal.adicionaFilho(RegraFuncao.getInstance().analisar(listaTokens));
            } catch (ErroSintaticoException e) {
                e.printStackTrace();
            }
        }
        return terminalDeclFinal;
    }

    @Override
    protected void setPrimeiro() {
        this.primeiro.add("char");
        this.primeiro.add("int");
        this.primeiro.add("float");
        this.primeiro.add("boolean");
        this.primeiro.add("void");
        this.primeiro.add("identificador");
    }

    @Override
    protected void setSeguinte() {
        this.seguinte.add("$");
    }
}
