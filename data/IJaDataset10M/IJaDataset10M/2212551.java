package xbird.xquery.func.seq;

import java.util.Iterator;
import java.util.Stack;
import xbird.util.iterator.ReverseIterator;
import xbird.xquery.XQueryException;
import xbird.xquery.dm.value.*;
import xbird.xquery.dm.value.sequence.ValueSequence;
import xbird.xquery.func.BuiltInFunction;
import xbird.xquery.func.FunctionSignature;
import xbird.xquery.meta.*;
import xbird.xquery.type.SequenceType;
import xbird.xquery.type.Type;

/**
 * fn:reverse($arg as item()*) as item()*.
 * <DIV lang="en">
 * Reverses the order of items in a sequence. If $arg is the empty sequence, 
 * the empty sequence is returned.
 * </DIV>
 * <DIV lang="ja"></DIV>
 * 
 * @author Makoto YUI (yuin405+xbird@gmail.com)
 * @link http://www.w3.org/TR/xquery-operators/#func-reverse
 */
public final class Reverse extends BuiltInFunction {

    private static final long serialVersionUID = 2154542136553587421L;

    public static final String SYMBOL = "fn:reverse";

    public Reverse() {
        super(SYMBOL, SequenceType.ANY_ITEMS);
    }

    protected FunctionSignature[] signatures() {
        final FunctionSignature[] s = new FunctionSignature[1];
        s[0] = new FunctionSignature(getName(), new Type[] { SequenceType.ANY_ITEMS });
        return s;
    }

    public Sequence eval(Sequence<? extends Item> contextSeq, ValueSequence argv, DynamicContext dynEnv) throws XQueryException {
        assert (argv != null && argv.size() == 1);
        Item arg = argv.getItem(0);
        return new Reverser(arg, dynEnv);
    }

    private static final class Reverser extends AbstractSequence<Item> {

        private static final long serialVersionUID = -7288851327130322738L;

        private Stack<Item> _itemStack = null;

        private final Sequence<Item> _src;

        Reverser(Sequence<Item> src, DynamicContext dynEnv) {
            super(dynEnv);
            assert (src != null);
            this._src = src;
        }

        public boolean next(IFocus focus) throws XQueryException {
            final Iterator<Item> baseItor = focus.getBaseFocus();
            if (baseItor.hasNext()) {
                final Item next = baseItor.next();
                focus.setContextItem(next);
                return true;
            }
            return false;
        }

        public Focus<Item> iterator() {
            if (_itemStack == null) {
                final Stack<Item> st = new Stack<Item>();
                final IFocus<Item> srcItor = _src.iterator();
                for (Item it : srcItor) {
                    st.push(it);
                }
                srcItor.closeQuietly();
                this._itemStack = st;
            }
            final Iterator<Item> baseItor = new ReverseIterator<Item>(_itemStack);
            return new Focus<Item>(this, baseItor, _dynEnv);
        }

        public Type getType() {
            return _src.getType();
        }

        @Override
        public boolean isEmpty() {
            return _src.isEmpty();
        }
    }
}
