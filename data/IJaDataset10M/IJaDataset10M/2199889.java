package org.exist.xquery;

import org.exist.memtree.MemTreeBuilder;
import org.exist.xquery.util.ExpressionDumper;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceIterator;

/**
 * Implements a dynamic text constructor. Contrary to {@link org.exist.xquery.TextConstructor},
 * the character content of a DynamicTextConstructor is determined only at evaluation time.
 * 
 * @author wolf
 */
public class DynamicTextConstructor extends NodeConstructor {

    private final Expression content;

    /**
     * @param context
     */
    public DynamicTextConstructor(XQueryContext context, Expression contentExpr) {
        super(context);
        this.content = new Atomize(context, contentExpr);
    }

    public Expression getContent() {
        return content;
    }

    public void analyze(AnalyzeContextInfo contextInfo) throws XPathException {
        super.analyze(contextInfo);
        contextInfo.setParent(this);
        content.analyze(contextInfo);
    }

    public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
        if (context.getProfiler().isEnabled()) {
            context.getProfiler().start(this);
            context.getProfiler().message(this, Profiler.DEPENDENCIES, "DEPENDENCIES", Dependency.getDependenciesName(this.getDependencies()));
            if (contextSequence != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "CONTEXT SEQUENCE", contextSequence);
            if (contextItem != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "CONTEXT ITEM", contextItem.toSequence());
        }
        if (newDocumentContext) context.pushDocumentContext();
        Sequence result;
        try {
            Sequence contentSeq = content.eval(contextSequence, contextItem);
            if (contentSeq.isEmpty()) result = Sequence.EMPTY_SEQUENCE; else {
                MemTreeBuilder builder = context.getDocumentBuilder();
                context.proceed(this, builder);
                StringBuilder buf = new StringBuilder();
                for (SequenceIterator i = contentSeq.iterate(); i.hasNext(); ) {
                    context.proceed(this, builder);
                    Item next = i.nextItem();
                    if (buf.length() > 0) buf.append(' ');
                    buf.append(next.toString());
                }
                if (!newDocumentContext && buf.length() == 0) result = Sequence.EMPTY_SEQUENCE; else {
                    int nodeNr = builder.characters(buf);
                    result = builder.getDocument().getNode(nodeNr);
                }
            }
        } finally {
            if (newDocumentContext) context.popDocumentContext();
        }
        if (context.getProfiler().isEnabled()) context.getProfiler().end(this, "", result);
        return result;
    }

    public void dump(ExpressionDumper dumper) {
        dumper.display("text {");
        dumper.startIndent();
        content.dump(dumper);
        dumper.endIndent();
        dumper.nl().display("}");
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("text {");
        result.append(content.toString());
        result.append("}");
        return result.toString();
    }

    public void resetState(boolean postOptimization) {
        super.resetState(postOptimization);
        content.resetState(postOptimization);
    }

    public void accept(ExpressionVisitor visitor) {
        visitor.visitTextConstructor(this);
    }
}
