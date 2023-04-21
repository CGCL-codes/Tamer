package ch.unibe.inkml.util;

import java.util.List;
import java.util.Vector;
import ch.unibe.inkml.InkTraceGroup;
import ch.unibe.inkml.InkTraceLeaf;
import ch.unibe.inkml.InkTraceView;
import ch.unibe.inkml.InkTraceViewContainer;
import ch.unibe.inkml.InkTraceViewLeaf;

public abstract class TraceVisitor extends AbstractTraceFilter {

    private Vector<TraceViewFilter> traceFilters = new Vector<TraceViewFilter>();

    public void visit(InkTraceViewContainer container) {
        if (pass(container)) {
            visitHook(container);
        }
    }

    public void visit(InkTraceGroup inkTraceGroup) {
        inkTraceGroup.delegateVisitor(this);
    }

    public void visit(InkTraceLeaf inkTraceLeaf) {
    }

    public void visit(InkTraceViewLeaf leaf) {
        if (pass(leaf)) {
            visitHook(leaf);
        }
    }

    protected void visitHook(InkTraceViewContainer container) {
        container.delegateVisitor(this);
    }

    protected abstract void visitHook(InkTraceViewLeaf leaf);

    public void visitAll(List<InkTraceViewLeaf> list) {
        for (InkTraceView view : list) {
            view.accept(this);
        }
    }

    public void addTraceFilter(TraceViewFilter traceFilter) {
        this.traceFilters.add(traceFilter);
    }

    public boolean pass(InkTraceView view) {
        for (TraceViewFilter tf : traceFilters) {
            if (!tf.pass(view)) return false;
        }
        return true;
    }

    public void removeTraceFilter(TraceViewFilter traceFilter) {
        traceFilters.remove(traceFilter);
    }

    public void clearTraceFilters() {
        traceFilters.clear();
    }

    public Vector<TraceViewFilter> getTraceFilters() {
        return traceFilters;
    }

    public TraceViewFilter getTraceFilter() {
        return this;
    }

    public void go(InkTraceView root) {
        root.accept(this);
    }
}
