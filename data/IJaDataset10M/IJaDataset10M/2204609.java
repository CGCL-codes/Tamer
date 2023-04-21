package net.sf.xml2cb.cobol.engine.impl;

import net.sf.xml2cb.cobol.engine.CobolAreaBuilder;
import net.sf.xml2cb.cobol.engine.CobolCompiler;
import net.sf.xml2cb.cobol.engine.CobolCompilerFactory;

public class CobolCompilerFactoryImpl extends CobolCompilerFactory {

    private CobolAreaBuilderImpl builder = new CobolAreaBuilderImpl();

    public CobolCompiler createCompiler() {
        return new CobolCompilerImpl(builder);
    }

    public CobolAreaBuilder createAreaBuilder() {
        return builder;
    }
}
