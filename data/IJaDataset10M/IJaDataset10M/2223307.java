package com.google.gwt.dev.js;

import com.google.gwt.dev.js.ast.JsBlock;
import com.google.gwt.dev.js.ast.JsContext;
import com.google.gwt.dev.js.ast.JsProgram;
import com.google.gwt.dev.js.ast.JsProgramFragment;
import com.google.gwt.dev.js.ast.JsStatement;
import com.google.gwt.dev.util.TextOutput;

/**
 * Generates JavaScript source from an AST.
 */
public class JsSourceGenerationVisitor extends JsToStringGenerationVisitor {

    public JsSourceGenerationVisitor(TextOutput out) {
        super(out);
    }

    @Override
    public boolean visit(JsProgram x, JsContext<JsProgram> ctx) {
        return true;
    }

    @Override
    public boolean visit(JsProgramFragment x, JsContext<JsProgramFragment> ctx) {
        return true;
    }

    @Override
    public boolean visit(JsBlock x, JsContext<JsStatement> ctx) {
        printJsBlock(x, false, true);
        return false;
    }
}
