package com.google.gwt.dev.js;

import com.google.gwt.thirdparty.guava.common.base.Preconditions;
import com.google.gwt.thirdparty.javascript.jscomp.AbstractCompiler;
import com.google.gwt.thirdparty.javascript.jscomp.SourceAst;
import com.google.gwt.thirdparty.javascript.jscomp.SourceFile;
import com.google.gwt.thirdparty.javascript.rhino.InputId;
import com.google.gwt.thirdparty.javascript.rhino.Node;

/**
 * Maps the JavaScript AST to a Closure Compiler input source.
 */
public class ClosureJsAst implements SourceAst {

    private static final long serialVersionUID = 1L;

    private Node root;

    private final InputId inputId;

    public ClosureJsAst(InputId inputId, Node root) {
        Preconditions.checkNotNull(root);
        this.inputId = inputId;
        this.root = root;
    }

    @Override
    public void clearAst() {
        root = null;
    }

    @Override
    public Node getAstRoot(AbstractCompiler compiler) {
        return root;
    }

    @Override
    public InputId getInputId() {
        return inputId;
    }

    @Override
    public SourceFile getSourceFile() {
        return null;
    }

    public String getSourceName() {
        return null;
    }

    @Override
    public void setSourceFile(SourceFile file) {
        throw new UnsupportedOperationException("ClosureJsAst cannot be associated with a SourceFile instance.");
    }
}
