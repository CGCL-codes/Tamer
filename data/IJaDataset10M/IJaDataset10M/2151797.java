package com.v1r3n.bol.codegen.java;

import java.io.IOException;
import java.io.OutputStream;
import com.v1r3n.bol.CodeGenerationException;

/**
 * @author viren
 *
 */
public class NOPGenerator extends JavaCodeGenerator {

    @Override
    public void generate(OutputStream out) throws CodeGenerationException, IOException {
    }
}
