package edu.ufpa.ppgcc.visualpseudo.lexicon.token;

import edu.ufpa.ppgcc.visualpseudo.base.SewingPrimitiveGrammar;
import edu.ufpa.ppgcc.visualpseudo.exceptions.GrammarException;
import edu.ufpa.ppgcc.visualpseudo.exceptions.ReadException;

public class LogicToken extends SewingPrimitiveGrammar {

    public LogicToken(String s) {
        super(s);
    }

    public LogicToken() {
        super();
    }

    public void readWord() throws ReadException {
        s = "VERDADEIRO";
    }

    public boolean S(int[] i, int[] o) throws GrammarException {
        return LOGICO(i, o);
    }

    private boolean LOGICO(int i[], int o[]) throws GrammarException {
        int i1[] = { 0 };
        return x(LOGIC.TRUE, i, i1) && (!isLetOuDig(i1, o) || equal(i1, s.length())) && attr(o, i1[0]) || x(LOGIC.FALSE, i, i1) && (!isLetOuDig(i1, o) || equal(i1, s.length())) && attr(o, i1[0]);
    }

    public static void main(String[] args) {
        new LogicToken().execute();
    }
}
