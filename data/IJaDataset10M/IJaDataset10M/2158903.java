package ast;

import java.util.ArrayList;
import org.antlr.runtime.Token;
import ast.terminais.Identifier;
import ast.terminais.Tipo;

public class Declaracao_funcao extends Declaracao {

    private int index = 0;

    public Declaracao_funcao(Token payload) {
        super(payload);
    }

    public Identifier getId1() {
        return (Identifier) getChild(0);
    }

    public Lista_parametros getLista_parametros() {
        for (Object node : getChildren()) {
            if (node instanceof Lista_parametros) {
                return (Lista_parametros) node;
            }
        }
        return null;
    }

    public Tipo getTipo() {
        for (Object node : getChildren()) {
            if (node instanceof Tipo) {
                return (Tipo) node;
            }
        }
        return null;
    }

    public ArrayList<Declaracao_variaveis> getDeclaracao_variaveis() {
        ArrayList<Declaracao_variaveis> result = new ArrayList<Declaracao_variaveis>();
        for (Object node : getChildren()) {
            if (node instanceof Declaracao_variaveis) {
                Declaracao_variaveis decl = (Declaracao_variaveis) node;
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

    public Identifier getId2() {
        return (Identifier) getChild(getChildren().size() - 1);
    }

    @Override
    public Object accept(IVisitor visitor, Object o) {
        return visitor.visit(this, o);
    }
}
