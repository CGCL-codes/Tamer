package com.atosorigin.nl.brainfuck.compiler.curses;

import com.atosorigin.nl.brainfuck.compiler.base.CompilerConfiguration;
import com.atosorigin.nl.brainfuck.compiler.v1.V1ConfigurationBuilder;
import com.google.code.brainfuck4j.model.brainfuck.Instruction;

/**
 * @author a108600
 *
 */
public class CursesConfigurationBuilder extends V1ConfigurationBuilder {

    /**
	 * 
	 */
    public CursesConfigurationBuilder() {
        super();
    }

    @Override
    public CompilerConfiguration createConfiguration() {
        CompilerConfiguration config = super.createConfiguration();
        config.setConstructorCompiler(new CursesConstructorCompiler());
        config.getCompilers().put(Instruction.INPUT, new CursesInputCompiler());
        return config;
    }
}
