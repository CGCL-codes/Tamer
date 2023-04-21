package com.v1r3n.bol.codegen.java;

import java.io.IOException;
import java.io.OutputStream;
import com.v1r3n.bol.CodeGenerationException;
import com.v1r3n.bol.parser.Operator;

/**
 * @author viren
 *
 */
public class OperatorGenerator extends JavaCodeGenerator {

    private Operator opr;

    OperatorGenerator(Operator opr) {
        this.opr = opr;
    }

    @Override
    public void generate(OutputStream out) throws CodeGenerationException, IOException {
        StringBuffer buffer = new StringBuffer(opr.getOperator());
        out.write(buffer.toString().getBytes());
        out.flush();
    }
}
