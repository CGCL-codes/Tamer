package org.norecess.nolatte.primitives.arithmetic;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.junit.Assert.assertEquals;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.norecess.nolatte.ast.Datum;
import org.norecess.nolatte.ast.Text;
import org.norecess.nolatte.types.IDataTypeFilter;

public class SubtractAccumulatorTest {

    private IDataTypeFilter myDataTypeFilter;

    private Datum myDatum0;

    private Datum myDatum1;

    private Datum myDatum2;

    private SubtractAccumulator mySubtractAccumulator;

    @Before
    public void setUp() {
        myDataTypeFilter = createMock(IDataTypeFilter.class);
        myDatum0 = createMock(Datum.class);
        myDatum1 = createMock(Datum.class);
        myDatum2 = createMock(Datum.class);
        mySubtractAccumulator = new SubtractAccumulator(myDataTypeFilter);
    }

    public void replay() {
        EasyMock.replay(myDataTypeFilter, myDatum0, myDatum1, myDatum2);
    }

    @After
    public void verify() {
        EasyMock.verify(myDataTypeFilter, myDatum0, myDatum1, myDatum2);
    }

    @Test
    public void should_return_0_by_default() {
        replay();
        assertEquals(new Text("0"), mySubtractAccumulator.getAccumulation());
    }

    @Test
    public void should_accumulate_once() {
        expect(myDataTypeFilter.getNumber(myDatum0)).andReturn(2);
        replay();
        mySubtractAccumulator.first(myDatum0);
        assertEquals(new Text("2"), mySubtractAccumulator.getAccumulation());
    }

    @Test
    public void should_accumulate_many_times() {
        expect(myDataTypeFilter.getNumber(myDatum0)).andReturn(10);
        expect(myDataTypeFilter.getNumber(myDatum1)).andReturn(5);
        expect(myDataTypeFilter.getNumber(myDatum2)).andReturn(2);
        replay();
        mySubtractAccumulator.first(myDatum0);
        mySubtractAccumulator.accumulate(myDatum1);
        mySubtractAccumulator.accumulate(myDatum2);
        assertEquals(new Text("3"), mySubtractAccumulator.getAccumulation());
    }
}
