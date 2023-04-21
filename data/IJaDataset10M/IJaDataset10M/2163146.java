package edu.mit.csail.pag.amock.representation;

import edu.mit.csail.pag.amock.util.*;

public class TweakResult implements Result {

    private final CodeBlock tweaks = new IndentingCodeBlock();

    private String tweakClass = null;

    private static final ClassName TWEAK_STATE_CLASS = ClassName.fromDotted("edu.mit.csail.pag.amock.jmock.TweakState");

    public void addTweak(FieldTweak t) {
        tweaks.addChunk(t);
    }

    public void resolveNames(ClassNameResolver cr, VariableNameBaseResolver vr) {
        tweakClass = cr.getSourceName(TWEAK_STATE_CLASS);
        tweaks.resolveNames(cr, vr);
    }

    public void printSource(LinePrinter p) {
        p.line("new " + tweakClass + "() { public void go() {");
        tweaks.printSource(p);
        p.line("}}");
    }

    public MultiSet<ProgramObject> getProgramObjects() {
        MultiSet<ProgramObject> pos = new MultiSet<ProgramObject>();
        pos.addAll(tweaks.getProgramObjects());
        return pos;
    }

    public boolean shouldAppear() {
        return true;
    }
}
