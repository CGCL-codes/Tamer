package org.jactr.io.antlr3.writer.lisp;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jactr.io.generator.CodeGeneratorFactory;
import org.jactr.io.writer.CodeGeneratorWriter;

/**
 * @author developer
 */
public class LispModelWriter extends CodeGeneratorWriter {

    /**
   * logger definition
   */
    private static final Log LOGGER = LogFactory.getLog(LispModelWriter.class);

    /**
   * @param codeGenerator
   * @param trimImports
   */
    public LispModelWriter(boolean trimImports) {
        super(CodeGeneratorFactory.getCodeGenerator("lisp"), trimImports);
    }

    public LispModelWriter() {
        this(false);
    }
}
