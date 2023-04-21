package br.ufal.ic.ptl.abstractsyntaxtrees;

import br.ufal.ic.ptl.syntaticanalyzer.lexer.SourcePosition;

public abstract class TypeDenoter extends AST {

    public TypeDenoter(SourcePosition thePosition) {
        super(thePosition);
    }

    public abstract boolean equals(Object obj);
}
