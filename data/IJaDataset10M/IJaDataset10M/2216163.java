package edu.ufpa.ppgcc.visualpseudo.semantic.evaluate;

import java.util.List;
import java.util.Stack;
import edu.ufpa.ppgcc.visualpseudo.base.SewingSemanticGrammar;
import edu.ufpa.ppgcc.visualpseudo.exceptions.BreakException;
import edu.ufpa.ppgcc.visualpseudo.exceptions.GrammarException;
import edu.ufpa.ppgcc.visualpseudo.exceptions.ReadException;
import edu.ufpa.ppgcc.visualpseudo.exceptions.SemanticException;
import edu.ufpa.ppgcc.visualpseudo.lexicon.TokenEntry;
import edu.ufpa.ppgcc.visualpseudo.semantic.SymbolTable;

public abstract class BaseEval extends SewingSemanticGrammar implements IEvaluable {

    private IEvaluable execute = null;

    public BaseEval(IEvaluable execute) {
        super();
        this.l = execute.getComandos();
        this.execute = execute;
    }

    public void readWord() throws ReadException {
    }

    public List getComandos() {
        return l;
    }

    public SymbolTable getLocalTable() {
        return execute.getLocalTable();
    }

    public SymbolTable getMainTable() {
        return execute.getMainTable();
    }

    public boolean run(Object[] v, TokenEntry name, Stack params) throws GrammarException, SemanticException, BreakException {
        return execute.run(v, name, params);
    }

    public String getType(int i[], Object n) throws GrammarException, SemanticException {
        return execute.getType(i, n);
    }

    public boolean eval(Object[] v, int[] i, int[] o) throws GrammarException, SemanticException {
        return execute.eval(v, i, o);
    }
}
