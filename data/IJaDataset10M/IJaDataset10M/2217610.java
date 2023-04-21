package absyn;

import symbol.Symbol;

public class FuncDecl extends Decl {

    public Symbol name;

    public TypeFields params;

    public Symbol type;

    public Expr body;

    public FuncDecl(int pos, Symbol name, TypeFields params, Symbol type, Expr body) {
        super(pos);
        this.name = name;
        this.params = params;
        this.type = type;
        this.body = body;
    }

    public FuncDecl(int pos, Symbol name, TypeFields params, Expr body) {
        this(pos, name, params, null, body);
    }
}
