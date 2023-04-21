package org.gocha.simplelang;

import org.antlr.runtime.RecognizerSharedState;
import org.antlr.runtime.TokenStream;
import org.gocha.script.runtime.Memory;
import org.gocha.script.runtime.ScriptEngine;

/**
 * @author gocha
 */
public class BasicParser extends org.antlr.runtime.Parser implements ScriptEngine {

    public BasicParser(TokenStream input) {
        super(input);
    }

    public BasicParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    private Memory memory = null;

    @Override
    public Memory getMemory() {
        return memory;
    }

    @Override
    public void setMemory(Memory memo) {
        memory = memo;
    }
}
