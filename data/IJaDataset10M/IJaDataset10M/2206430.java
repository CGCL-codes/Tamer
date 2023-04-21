package parser;

import java.util.LinkedList;
import editor.Library;

public class ASTStart extends SimpleNode {

    public ASTStart(int id) {
        super(id);
    }

    public ASTStart(Parser p, int id) {
        super(p, id);
    }

    public String outsideMain = "";

    public LinkedList<Library> libs;

    /**
   * @return a pointer towards an ASTType holding the type of this subterm
   * @throws TypeException
   */
    public Type type() throws TypeException {
        Type prog = jjtGetChild(0).type();
        ASTType proctype = new ASTType(ParserTreeConstants.JJTTYPE);
        proctype.jjtSetValue("Proc");
        if (!prog.equals(proctype)) throw new TypeException("Error : final type must be of type Proc"); else return proctype;
    }

    /**
   * Code generation for the all tree 
   * (first call of this recursive function)
   * 
   */
    public String genCode(String s, int ntabs) throws TypeException {
        String libCode = "";
        if (libs != null) {
            for (Library l : libs) {
                libCode += l.code + "\n";
            }
        }
        ASTTermVar.STRUCT_ID = 0;
        ASTFun.funCntr = 0;
        SimpleNode.varId = 0;
        String main = "";
        String includeAndProto = "";
        includeAndProto += "#include \"happy.h\" \n\n";
        includeAndProto += libCode;
        main += "int main(void) { \n";
        for (int i = 0; i < jjtGetNumChildren(); i++) main += "\n" + this.jjtGetChild(i).genCode(s, 1);
        main += "\nreturn 0; \n";
        main += "} \n";
        main = includeAndProto + main;
        return main;
    }

    public void optimise(int childN) throws TypeException {
        this.children[0].optimise(0);
    }
}
