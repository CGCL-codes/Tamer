package net.sf.imageCave.database;

import java.io.*;
import antlr.DumpASTVisitor;
import antlr.CommonAST;

public class DataQueryInterpreter {

    private CommonAST myAST;

    /** Constructor */
    public DataQueryInterpreter() {
    }

    /**
     * Reads an expression made of boolean operations between groups of field = "value"
     * @return an abstract syntax tree.
     * @param file a configuration file that follows the ImageCave CFG Syntax. 
     */
    public CommonAST read(String exp) {
        this.myAST = new CommonAST();
        try {
            DataQueryLexer lexer = new DataQueryLexer(new StringReader(exp));
            DataQueryParser parser = new DataQueryParser(lexer);
            parser.startRule();
            this.myAST = (CommonAST) parser.getAST();
        } catch (Exception e) {
            System.err.println("exception: " + e);
        }
        return this.myAST;
    }

    public boolean isMatch(DataRow dr, DataTable dt) {
        boolean flag = true;
        boolean match = false;
        CommonAST cond = (CommonAST) this.myAST.getFirstChild();
        CommonAST expr = (CommonAST) cond.getFirstChild();
        int lastBoolOp = -1;
        while (expr != null) {
            boolean prevFlag = flag;
            CommonAST id = (CommonAST) expr.getFirstChild().getFirstChild();
            CommonAST eq = (CommonAST) expr.getFirstChild().getNextSibling();
            CommonAST val = (CommonAST) eq.getNextSibling();
            flag = dr.fieldMatches(dt.fieldIndex(id.toString()), val.toString());
            if (lastBoolOp == 1) match = prevFlag | flag; else if (lastBoolOp == 0) match = prevFlag & flag; else match = flag;
            if (expr.getNextSibling() != null) {
                if (expr.getNextSibling().toString().equals("||")) {
                    lastBoolOp = 1;
                } else if (expr.getNextSibling().toString().equals("&&")) {
                    lastBoolOp = 0;
                }
            }
            try {
                expr = (CommonAST) expr.getNextSibling().getNextSibling();
            } catch (Exception e) {
                break;
            }
        }
        return match;
    }
}
