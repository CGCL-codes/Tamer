package uchicago.src.sim.parameter.rpl;

import java.util.List;

public class ASTblockdef extends SimpleNode {

    String blockName;

    public ASTblockdef(int id) {
        super(id);
    }

    public ASTblockdef(RPLParser p, int id) {
        super(p, id);
    }

    public void preProcess(RPLCompiler compiler) {
        blockName = (String) children[0].getInfo();
        children[1].preProcess(compiler);
        if (blockName.equals("main")) {
            compiler.setHasMain(true);
        }
    }

    public void compile(RPLCompiler compiler) {
        if (compiler.hasGlobalVariable(blockName)) {
            String message = "variable '" + blockName + "' is already defined";
            throw compiler.createCompilerException(message, this.beginLine);
        }
        BlockRPLParameter blockP = new BlockRPLParameter(blockName);
        ASTsuite suite = (ASTsuite) children[1];
        List list = suite.getConstants();
        for (int i = 0, n = list.size(); i < n; i++) {
            ASTConstant c = (ASTConstant) list.get(i);
            c.compile(compiler);
            blockP.addChildConstant(c.getRPLConstant());
        }
        list = suite.getReferences();
        for (int i = 0, n = list.size(); i < n; i++) {
            String varName = (String) list.get(i);
            if (!compiler.isParameter(varName)) {
                String message = "parameter reference '" + varName + "' is undefined";
                throw compiler.createCompilerException(message, this.beginLine);
            }
            RPLParameter p = compiler.getParameter(varName);
            p.addToParent(blockP);
        }
        list = suite.getSubBlocks();
        for (int i = 0, n = list.size(); i < n; i++) {
            ASTsubBlock block = (ASTsubBlock) list.get(i);
            block.compile(compiler);
            RPLParameter p = compiler.getParameter(block.getBlockName());
            p.addToParent(blockP);
        }
        compiler.addParameter(blockName, blockP);
        if (blockName.equals("main")) {
            compiler.setMain(blockP);
        }
    }
}
