package net.sourceforge.xconf.toolbox.generic;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import java.util.List;

public class ListUtilsTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldApplyClosureOnEachElementInList() throws Exception {
        Closure closure = createMock(Closure.class);
        Object first = new Object();
        Object second = new Object();
        closure.apply(first);
        closure.apply(second);
        replay(closure);
        ListUtils.each(ListUtils.toList(first, second), closure);
        verify(closure);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldApplyClosureOnEachElementInVarargs() throws Exception {
        Closure closure = createMock(Closure.class);
        Object one = new Object();
        Object two = new Object();
        closure.apply(one);
        closure.apply(two);
        replay(closure);
        ListUtils.each(closure, one, two);
        verify(closure);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldApplyFilterToEachElementInList() throws Exception {
        Filter filter = createMock(Filter.class);
        Object one = new Object();
        Object two = new Object();
        expect(filter.accept(one)).andReturn(false);
        expect(filter.accept(two)).andReturn(true);
        replay(filter);
        List result = ListUtils.filter(ListUtils.toList(one, two), filter);
        assertEquals(1, result.size());
        assertSame(two, result.get(0));
        verify(filter);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldApplyFilterToEachElementInVarargs() throws Exception {
        Filter filter = createMock(Filter.class);
        Object one = new Object();
        Object two = new Object();
        expect(filter.accept(one)).andReturn(true);
        expect(filter.accept(two)).andReturn(false);
        replay(filter);
        List result = ListUtils.filter(filter, one, two);
        assertEquals(1, result.size());
        assertSame(one, result.get(0));
        verify(filter);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldApplyConverterToEachElementInList() throws Exception {
        Converter<Object, String> converter = createMock(Converter.class);
        Object obj1 = new Object();
        Object obj2 = new Object();
        String str1 = "s1";
        String str2 = "s2";
        expect(converter.convert(obj1)).andReturn(str1);
        expect(converter.convert(obj2)).andReturn(str2);
        replay(converter);
        List result = ListUtils.convert(ListUtils.toList(obj1, obj2), converter);
        assertEquals(2, result.size());
        assertSame(str1, result.get(0));
        assertSame(str2, result.get(1));
        verify(converter);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldApplyConverterToEachElementInVarargs() throws Exception {
        Converter<Object, String> converter = createMock(Converter.class);
        Object o1 = new Object();
        Object o2 = new Object();
        String s1 = "s1";
        String s2 = "s2";
        expect(converter.convert(o1)).andReturn(s1);
        expect(converter.convert(o2)).andReturn(s2);
        replay(converter);
        List result = ListUtils.convert(converter, o1, o2);
        assertEquals(2, result.size());
        assertSame(s1, result.get(0));
        assertSame(s2, result.get(1));
        verify(converter);
    }

    @Test
    public void shouldReturnTheFirstItemFromTheList() throws Exception {
        assertEquals(new Integer(1), ListUtils.first(ListUtils.toList(1, 2, 3)));
    }

    @Test
    public void shouldReturnDefaultValueForFirstWhenListIsEmpty() throws Exception {
        assertEquals(new Integer(1), ListUtils.first(ListUtils.empty(), 1));
    }

    @Test
    public void shouldReturnTheLastItemFromTheList() throws Exception {
        assertEquals(new Integer(3), ListUtils.last(ListUtils.toList(1, 2, 3)));
    }

    @Test
    public void shouldReturnDefaultValueForLastWhenListIsEmpty() throws Exception {
        assertEquals(new Integer(1), ListUtils.last(ListUtils.empty(), 1));
    }

    @Test
    public void shouldReturnExpectedHeadPair() throws Exception {
        assertEquals(Pair.create(1, ListUtils.toList(2, 3)), ListUtils.head(ListUtils.toList(1, 2, 3)));
    }

    @Test
    public void shouldReturnExpectedTailPair() throws Exception {
        assertEquals(Pair.create(3, ListUtils.toList(1, 2)), ListUtils.tail(ListUtils.toList(1, 2, 3)));
    }
}
