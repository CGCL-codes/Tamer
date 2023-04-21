package nl.flotsam.preon.util;

import java.util.Iterator;
import java.util.List;
import nl.flotsam.limbo.Expression;
import nl.flotsam.preon.Builder;
import nl.flotsam.preon.Codec;
import nl.flotsam.preon.DecodingException;
import nl.flotsam.preon.Resolver;
import nl.flotsam.preon.buffer.BitBuffer;
import nl.flotsam.preon.util.EvenlyDistributedLazyList;
import junit.framework.TestCase;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

public class LazyListTest extends TestCase {

    private BitBuffer buffer;

    private Codec codec;

    private Builder builder;

    private Resolver resolver;

    private Expression<Integer, Resolver> sizeExpr;

    @SuppressWarnings("unchecked")
    public void setUp() {
        buffer = createMock(BitBuffer.class);
        codec = createMock(Codec.class);
        builder = createMock(Builder.class);
        resolver = createMock(Resolver.class);
        sizeExpr = createMock(Expression.class);
    }

    public void testTakingElement() throws DecodingException {
        Object value = new Object();
        expect(codec.getSize()).andReturn(sizeExpr);
        expect(sizeExpr.eval(resolver)).andReturn(20);
        buffer.setBitPos(20);
        expect(codec.decode(buffer, resolver, builder)).andReturn(value);
        replay(buffer, codec, resolver, builder, sizeExpr);
        EvenlyDistributedLazyList<Object> list = new EvenlyDistributedLazyList<Object>(codec, 0, buffer, 10, builder, resolver);
        list.get(1);
        verify(buffer, codec, resolver, builder, sizeExpr);
    }

    public void testIndexToLow() {
        expect(codec.getSize()).andReturn(sizeExpr);
        expect(sizeExpr.eval(resolver)).andReturn(20);
        replay(buffer, codec, resolver, builder, sizeExpr);
        try {
            EvenlyDistributedLazyList<Object> list = new EvenlyDistributedLazyList<Object>(codec, 0, buffer, 10, builder, resolver);
            list.get(-1);
            fail();
        } catch (IndexOutOfBoundsException iobe) {
        }
        verify(buffer, codec, resolver, builder, sizeExpr);
    }

    public void testIndexToHigh() {
        expect(codec.getSize()).andReturn(sizeExpr);
        expect(sizeExpr.eval(resolver)).andReturn(20);
        replay(buffer, codec, resolver, builder, sizeExpr);
        try {
            EvenlyDistributedLazyList<Object> list = new EvenlyDistributedLazyList<Object>(codec, 0, buffer, 10, builder, resolver);
            list.get(10);
            fail();
        } catch (IndexOutOfBoundsException iobe) {
        }
        verify(buffer, codec, resolver, builder, sizeExpr);
    }

    public void testSubList() throws DecodingException {
        Object value = new Object();
        expect(codec.getSize()).andReturn(sizeExpr).times(2);
        expect(sizeExpr.eval(resolver)).andReturn(20).times(2);
        buffer.setBitPos(20);
        expect(codec.decode(buffer, resolver, builder)).andReturn(value);
        replay(buffer, codec, resolver, builder, sizeExpr);
        EvenlyDistributedLazyList<Object> list = new EvenlyDistributedLazyList<Object>(codec, 0, buffer, 10, builder, resolver);
        List<Object> sublist = list.subList(1, 3);
        sublist.get(0);
        verify(buffer, codec, resolver, builder, sizeExpr);
    }

    public void testIterator() throws DecodingException {
        Object value = new Object();
        expect(codec.getSize()).andReturn(sizeExpr);
        expect(sizeExpr.eval(resolver)).andReturn(20);
        buffer.setBitPos(0);
        expect(codec.decode(buffer, resolver, builder)).andReturn(value);
        buffer.setBitPos(20);
        expect(codec.decode(buffer, resolver, builder)).andReturn(value);
        buffer.setBitPos(40);
        expect(codec.decode(buffer, resolver, builder)).andReturn(value);
        replay(buffer, codec, resolver, builder, sizeExpr);
        EvenlyDistributedLazyList<Object> list = new EvenlyDistributedLazyList<Object>(codec, 0, buffer, 3, builder, resolver);
        Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }
        verify(buffer, codec, resolver, builder, sizeExpr);
    }
}
