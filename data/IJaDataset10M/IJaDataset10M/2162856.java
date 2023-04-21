package org.exist.xquery;

import org.exist.dom.QName;
import org.exist.xquery.util.ExpressionDumper;
import org.exist.xquery.value.BooleanValue;
import org.exist.xquery.value.GroupedValueSequenceTable;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceIterator;
import org.exist.xquery.value.Type;

/**
 * Represents a quantified expression: "some ... in ... satisfies", 
 * "every ... in ... satisfies".
 * 
 * @author Wolfgang Meier (wolfgang@exist-db.org)
 */
public class QuantifiedExpression extends BindingExpression {

    public static final int SOME = 0;

    public static final int EVERY = 1;

    private final int mode;

    /**
	 * @param context
	 */
    public QuantifiedExpression(XQueryContext context, int mode) {
        super(context);
        switch(mode) {
            case SOME:
            case EVERY:
                this.mode = mode;
                break;
            default:
                throw new IllegalArgumentException("QuantifiedExpression");
        }
    }

    public void analyze(AnalyzeContextInfo contextInfo, OrderSpec orderBy[], GroupSpec groupBy[]) throws XPathException {
        LocalVariable mark = context.markLocalVariables(false);
        try {
            context.declareVariableBinding(new LocalVariable(QName.parse(context, varName, null)));
            contextInfo.setParent(this);
            inputSequence.analyze(contextInfo);
            returnExpr.analyze(contextInfo);
        } finally {
            context.popLocalVariables(mark);
        }
    }

    public Sequence eval(Sequence contextSequence, Item contextItem, Sequence resultSequence, GroupedValueSequenceTable groupedSequence) throws XPathException {
        if (context.getProfiler().isEnabled()) {
            context.getProfiler().start(this);
            context.getProfiler().message(this, Profiler.DEPENDENCIES, "DEPENDENCIES", Dependency.getDependenciesName(this.getDependencies()));
            if (contextSequence != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "CONTEXT SEQUENCE", contextSequence);
            if (contextItem != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "CONTEXT ITEM", contextItem.toSequence());
            if (resultSequence != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "RESULT SEQUENCE", resultSequence);
        }
        LocalVariable var = new LocalVariable(QName.parse(context, varName, null));
        Sequence inSeq = inputSequence.eval(contextSequence, contextItem);
        if (sequenceType != null) {
            if (!inSeq.isEmpty() && !Type.subTypeOf(inSeq.getItemType(), sequenceType.getPrimaryType())) throw new XPathException(this, ErrorCodes.XPTY0004, "Invalid type for variable $" + varName + ". Expected " + Type.getTypeName(sequenceType.getPrimaryType()) + ", got " + Type.getTypeName(inSeq.getItemType()), inSeq);
        }
        boolean found = (mode == EVERY) ? true : false;
        boolean canDecide = (mode == EVERY) ? true : false;
        for (SequenceIterator i = inSeq.iterate(); i.hasNext(); ) {
            canDecide = true;
            Item item = i.nextItem();
            var.setValue(item.toSequence());
            if (sequenceType == null) var.checkType();
            Sequence satisfiesSeq;
            LocalVariable mark = context.markLocalVariables(false);
            try {
                context.declareVariableBinding(var);
                satisfiesSeq = returnExpr.eval(contextSequence, contextItem);
            } finally {
                context.popLocalVariables(mark);
            }
            if (sequenceType != null) {
                if (!Type.subTypeOf(sequenceType.getPrimaryType(), Type.NODE)) {
                    if (!Type.subTypeOf(item.toSequence().getItemType(), sequenceType.getPrimaryType())) throw new XPathException(this, ErrorCodes.XPTY0004, "Invalid type for variable $" + varName + ". Expected " + Type.getTypeName(sequenceType.getPrimaryType()) + ", got " + Type.getTypeName(contextItem.toSequence().getItemType()), inSeq);
                } else if (!Type.subTypeOf(item.getType(), Type.NODE)) throw new XPathException(this, ErrorCodes.XPTY0004, "Invalid type for variable $" + varName + ". Expected " + Type.getTypeName(Type.NODE) + " (or more specific), got " + Type.getTypeName(item.getType()), inSeq); else var.checkType();
            }
            found = satisfiesSeq.effectiveBooleanValue();
            if ((mode == SOME) && found) break;
            if ((mode == EVERY) && !found) break;
        }
        Sequence result = canDecide && found ? BooleanValue.TRUE : BooleanValue.FALSE;
        if (context.getProfiler().isEnabled()) context.getProfiler().end(this, "", result);
        return result;
    }

    public void dump(ExpressionDumper dumper) {
        dumper.display(mode == SOME ? "some" : "every");
        dumper.display(" $").display(varName).display(" in");
        dumper.startIndent();
        inputSequence.dump(dumper);
        dumper.endIndent().nl();
        dumper.display("satisfies");
        dumper.startIndent();
        returnExpr.dump(dumper);
        dumper.endIndent();
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(mode == SOME ? "some" : "every");
        result.append(" $").append(varName).append(" in");
        result.append(" ");
        result.append(inputSequence.toString());
        result.append(" ");
        result.append("satisfies");
        result.append(" ");
        result.append(returnExpr.toString());
        result.append(" ");
        return result.toString();
    }

    public int returnsType() {
        return Type.BOOLEAN;
    }

    public int getDependencies() {
        return Dependency.CONTEXT_ITEM | Dependency.CONTEXT_SET;
    }
}
