package edu.ufpa.ppgcc.visualpseudo.semantic.evaluate;

import java.util.List;
import java.util.Stack;
import edu.ufpa.ppgcc.visualpseudo.exceptions.BreakException;
import edu.ufpa.ppgcc.visualpseudo.exceptions.GrammarException;
import edu.ufpa.ppgcc.visualpseudo.exceptions.SemanticException;
import edu.ufpa.ppgcc.visualpseudo.lexicon.TokenEntry;
import edu.ufpa.ppgcc.visualpseudo.semantic.SymbolTable;

public interface IEvaluable {

    public SymbolTable getLocalTable();

    public SymbolTable getMainTable();

    public List getComandos();

    public String getType(int i[], Object n) throws GrammarException, SemanticException;

    public boolean eval(Object v[], int i[], int o[]) throws GrammarException, SemanticException;

    public boolean run(Object[] v, TokenEntry name, Stack params) throws GrammarException, SemanticException, BreakException;
}
