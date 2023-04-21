package parser;

public class ASTLeq extends SimpleNode {

    public ASTLeq(int id) {
        super(id);
    }

    public ASTLeq(Parser p, int id) {
        super(p, id);
    }

    /**
   * @return a pointer towards an ASTType holding the type of this subterm
   * @throws TypeException
   */
    public Type type() throws TypeException {
        ASTType inttype = new ASTType(ParserTreeConstants.JJTTYPE);
        inttype.jjtSetValue("Int");
        Type childType;
        for (Node child : this.children) {
            childType = child.type();
            if (!childType.equals(inttype)) {
                throw new TypeException("Error at line " + line + " : 'Leq' operande must be used with Int-type variables");
            }
        }
        ASTType booltype = new ASTType(ParserTreeConstants.JJTTYPE);
        booltype.jjtSetValue("Bool");
        return booltype;
    }

    public String genCode(String s, int ntabs) throws TypeException {
        ASTType lettype = new ASTType(ParserTreeConstants.JJTTYPE);
        lettype.jjtSetValue("let");
        ASTType inttype = new ASTType(ParserTreeConstants.JJTTYPE);
        inttype.jjtSetValue("Int");
        ASTType booltype = new ASTType(ParserTreeConstants.JJTTYPE);
        booltype.jjtSetValue("Bool");
        String retour = new String("");
        String[] var = new String[jjtGetNumChildren()];
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            if (this.jjtGetChild(i) instanceof parser.ASTTermVar) var[i] = "" + this.toString() + "TermV" + this.nextVarId(); else var[i] = "" + this.toString() + this.jjtGetChild(i) + this.nextVarId();
        }
        String[] ret = new String[jjtGetNumChildren()];
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            ret[i] = this.jjtGetChild(i).genCode(var[i], ntabs);
        }
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            if (this.jjtGetChild(i).type().equals(booltype)) retour = retour + tabs(ntabs) + "char " + var[i] + ";\n"; else if (this.jjtGetChild(i).type().equals(inttype)) retour = retour + tabs(ntabs) + "int " + var[i] + ";\n";
        }
        retour = retour + ret[0] + "\n";
        retour = retour + ret[1] + "\n";
        retour = retour + tabs(ntabs) + s + " = (" + var[0] + " <= " + var[1] + ");";
        return retour;
    }

    public void optimise(int childN) throws TypeException {
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            children[i].optimise(i);
        }
        boolean intChildren = true;
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            if (!(children[i] instanceof ASTInt)) intChildren = false;
        }
        if (intChildren) {
            String newValue = "false";
            if (Integer.valueOf(children[0].genCode("", 0)) <= Integer.valueOf(children[1].genCode("", 0))) newValue = "true";
            ASTBool opti = new ASTBool(ParserTreeConstants.JJTBOOL);
            opti.parent = this.parent;
            opti.value = "" + newValue;
            this.parent.jjtSetChild(childN, opti);
        }
    }
}
