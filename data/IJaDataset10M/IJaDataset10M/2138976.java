package xbird.xquery.func;

import xbird.xquery.XQueryException;
import xbird.xquery.dm.value.Item;
import xbird.xquery.dm.value.Sequence;
import xbird.xquery.dm.value.literal.XString;
import xbird.xquery.dm.value.sequence.ValueSequence;
import xbird.xquery.meta.DynamicContext;
import xbird.xquery.type.Type;
import xbird.xquery.type.xs.StringType;

/**
 * 
 * <DIV lang="en"></DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 */
public class ExampleFunction extends BuiltInFunction {

    public static final String SYMBOL = EXT_NSPREFIX + ":example-func";

    public static final String expected = "works fine!";

    public ExampleFunction() {
        super(SYMBOL, StringType.STRING);
    }

    @Override
    protected FunctionSignature[] signatures() {
        final FunctionSignature[] s = new FunctionSignature[1];
        s[0] = new FunctionSignature(getName(), new Type[0]);
        return s;
    }

    @SuppressWarnings("unchecked")
    public Sequence eval(Sequence<? extends Item> contextSeq, ValueSequence argv, DynamicContext dynEnv) throws XQueryException {
        return new XString(expected);
    }
}
