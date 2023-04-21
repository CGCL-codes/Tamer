package org.exist.xquery.functions.fn;

import org.exist.dom.NewArrayNodeSet;
import org.exist.dom.NodeProxy;
import org.exist.dom.NodeSet;
import org.exist.dom.QName;
import org.exist.xquery.AnalyzeContextInfo;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Dependency;
import org.exist.xquery.Expression;
import org.exist.xquery.Function;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Profiler;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.BooleanValue;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.Item;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceIterator;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

public class FunNot extends Function {

    public static final FunctionSignature signature = new FunctionSignature(new QName("not", Function.BUILTIN_FUNCTION_NS), " Returns true if the effective boolean " + "value is false, and false if the effective boolean value is true. \n\n $arg is reduced to an effective boolean value by applying " + "the fn:boolean() function.", new SequenceType[] { new FunctionParameterSequenceType("arg", Type.ITEM, Cardinality.ZERO_OR_MORE, "The input items") }, new FunctionReturnSequenceType(Type.BOOLEAN, Cardinality.EXACTLY_ONE, "the negated effective boolean value (ebv) of $arg"));

    @SuppressWarnings("unused")
    private boolean inWhereClause = false;

    public FunNot(XQueryContext context) {
        super(context, signature);
    }

    public void analyze(AnalyzeContextInfo contextInfo) throws XPathException {
        super.analyze(contextInfo);
        inWhereClause = (contextInfo.getFlags() & IN_WHERE_CLAUSE) != 0;
    }

    public int returnsType() {
        return Type.subTypeOf(getArgument(0).returnsType(), Type.NODE) ? Type.NODE : Type.BOOLEAN;
    }

    public int getDependencies() {
        return Dependency.CONTEXT_SET | getArgument(0).getDependencies();
    }

    public Sequence eval(Sequence contextSequence, Item contextItem) throws XPathException {
        if (context.getProfiler().isEnabled()) {
            context.getProfiler().start(this);
            context.getProfiler().message(this, Profiler.DEPENDENCIES, "DEPENDENCIES", Dependency.getDependenciesName(this.getDependencies()));
            if (contextSequence != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "CONTEXT SEQUENCE", contextSequence);
            if (contextItem != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "CONTEXT ITEM", contextItem.toSequence());
        }
        if (contextItem != null) contextSequence = contextItem.toSequence();
        Sequence result;
        Expression arg = getArgument(0);
        if (Type.subTypeOf(arg.returnsType(), Type.NODE) && (contextSequence == null || contextSequence.isPersistentSet()) && !Dependency.dependsOn(arg, Dependency.CONTEXT_ITEM)) {
            if (contextSequence == null || contextSequence.isEmpty()) {
                result = evalBoolean(contextSequence, contextItem, arg);
            } else {
                result = contextSequence.toNodeSet().copy();
                if (inPredicate) {
                    for (SequenceIterator i = result.iterate(); i.hasNext(); ) {
                        NodeProxy item = (NodeProxy) i.nextItem();
                        item.addContextNode(getExpressionId(), item);
                        if (contextId != Expression.NO_CONTEXT_ID) item.addContextNode(contextId, item); else item.addContextNode(getExpressionId(), item);
                    }
                }
                Sequence argSeq = arg.eval(result);
                NodeSet argSet;
                if (contextId != Expression.NO_CONTEXT_ID) argSet = argSeq.toNodeSet().getContextNodes(contextId); else argSet = argSeq.toNodeSet().getContextNodes(getExpressionId());
                result = ((NodeSet) result).except(argSet);
            }
        } else {
            return evalBoolean(contextSequence, contextItem, arg);
        }
        if (context.getProfiler().isEnabled()) context.getProfiler().end(this, "", result);
        return result;
    }

    /**
	 * @param contextSequence
	 * @param contextItem
	 * @param arg
	 * @return
	 * @throws XPathException
	 */
    private Sequence evalBoolean(Sequence contextSequence, Item contextItem, Expression arg) throws XPathException {
        Sequence seq = arg.eval(contextSequence, contextItem);
        return seq.effectiveBooleanValue() ? BooleanValue.FALSE : BooleanValue.TRUE;
    }
}
