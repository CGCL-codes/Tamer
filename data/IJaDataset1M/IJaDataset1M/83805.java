package net.sf.jode.obfuscator;

import net.sf.jode.bytecode.BasicBlocks;

public interface CodeAnalyzer extends CodeTransformer {

    public void analyzeCode(MethodIdentifier parent, BasicBlocks bb);
}
