package shef.nlp.supple.utils;

import java.util.ArrayList;

public class SynSemPair {

    public ArrayList syntax;

    public ArrayList semantics;

    public SynSemPair(ArrayList syn, ArrayList sem) {
        syntax = syn;
        semantics = sem;
    }

    public ArrayList getSyntax() {
        return syntax;
    }

    public ArrayList getSemnatics() {
        return semantics;
    }
}
