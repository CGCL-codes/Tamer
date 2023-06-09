package uchicago.src.sim.parameter.rpl;

public class ASTcompilationUnit extends SimpleNode {

    public ASTcompilationUnit(int id) {
        super(id);
    }

    public ASTcompilationUnit(RPLParser p, int id) {
        super(p, id);
    }

    public void preProcess(RPLCompiler compiler) {
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            SimpleNode node = (SimpleNode) jjtGetChild(i);
            node.preProcess(compiler);
        }
    }

    public void compile(RPLCompiler compile) {
        for (int i = 0; i < jjtGetNumChildren(); i++) {
            SimpleNode node = (SimpleNode) jjtGetChild(i);
            node.compile(compile);
        }
    }
}
