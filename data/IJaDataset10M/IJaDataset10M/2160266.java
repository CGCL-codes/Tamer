package ca.ubc.jquery.engine.tyruba.java;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import ca.ubc.jquery.engine.tyruba.java.context.ContextTracker;

public class DummyFactGenerator extends ASTVisitor {

    public DummyFactGenerator(ContextTracker con) {
    }

    public void generate(CompilationUnit t) {
        t.accept(this);
    }
}
