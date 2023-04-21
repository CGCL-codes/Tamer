package xbird.xquery.func.seq;

import java.util.Iterator;
import java.util.List;
import xbird.xquery.DynamicError;
import xbird.xquery.XQueryException;
import xbird.xquery.dm.value.Item;
import xbird.xquery.dm.value.Sequence;
import xbird.xquery.dm.value.sequence.ValueSequence;
import xbird.xquery.expr.XQExpression;
import xbird.xquery.func.BuiltInFunction;
import xbird.xquery.func.FunctionSignature;
import xbird.xquery.meta.DynamicContext;
import xbird.xquery.meta.StaticContext;
import xbird.xquery.type.*;

/**
 * fn:zero-or-one($arg as item()*) as item()?.
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public final class ZeroOrOne extends BuiltInFunction {

    private static final long serialVersionUID = 7998331512034933930L;

    public static final String SYMBOL = "fn:zero-or-one";

    public ZeroOrOne() {
        super(SYMBOL, TypeRegistry.safeGet("item()?"));
    }

    protected FunctionSignature[] signatures() {
        final FunctionSignature[] s = new FunctionSignature[1];
        s[0] = new FunctionSignature(getName(), new Type[] { SequenceType.ANY_ITEMS });
        return s;
    }

    @Override
    public ZeroOrOne staticAnalysis(StaticContext context, List<? extends XQExpression> params) throws XQueryException {
        assert (params != null);
        final XQExpression p = params.get(0);
        this._returnType = p.getType().prime();
        assert (Type.Occurrence.OCC_ZERO_OR_ONE.accepts(_returnType.quantifier().getAlignment()));
        return this;
    }

    public Sequence eval(final Sequence<? extends Item> contextSeq, final ValueSequence argv, final DynamicContext dynEnv) throws XQueryException {
        assert (argv != null && argv.size() == 1);
        final Item arg = argv.getItem(0);
        final Type argType = arg.getType();
        final Type.Occurrence quantifier = argType.quantifier();
        if (!Type.Occurrence.OCC_ZERO_OR_ONE.accepts(quantifier.getAlignment())) {
            final Iterator<? extends Item> argItor = arg.iterator();
            if (argItor.hasNext()) {
                final Item first = argItor.next();
                assert (first != null);
                if (argItor.hasNext()) {
                    throw new DynamicError("err:FORG0004", "Invalid result sequence type was detected, $arg contains one or more items: " + arg.getType());
                }
            }
        }
        return arg;
    }
}
