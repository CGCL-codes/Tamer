package ast;

import java.util.ArrayList;
import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTree;
import ast.terminais.Identifier;

public class Program extends Node {

    private static final Object Declaracao = null;

    public Program(Token payload) {
        super(payload);
    }

    public Header getCabecalho() {
        return (Header) getChild(0);
    }

    public ArrayList<Declaracao> getDeclaracoes() {
        ArrayList<Declaracao> result = new ArrayList<Declaracao>();
        for (Object node : getChildren()) {
            if (node instanceof Declaracao) {
                Declaracao decl = (Declaracao) node;
                result.add(decl);
            }
        }
        return result;
    }

    public Corpo getCorpo() {
        for (Object node : getChildren()) {
            if (node instanceof Corpo) {
                return (Corpo) node;
            }
        }
        return null;
    }

    public Identifier getId() {
        for (Object node : getChildren()) {
            if (node instanceof Identifier) {
                return (Identifier) node;
            }
        }
        return null;
    }

    @Override
    public Object accept(IVisitor visitor, Object o) {
        return visitor.visit(this, o);
    }
}
