package com.cresus.test;

import java.util.Date;
import java.util.Iterator;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import com.cresus.Cresus;
import com.cresus.model.Operation;
import com.cresus.model.OperationList;
import com.cresus.model.SQLiteOperationList;
import com.cresus.model.SimpleOperationList;

public class SQLiteOperationListTest extends ActivityInstrumentationTestCase2<Cresus> {

    private SQLiteOperationList _operations;

    private int[] values = { 112, 236, 4512, -352, 8965, 4500, -3200 };

    private int[] dates = { 123, 1234, 12345, 123456, 1234567, 12345678, 123456789 };

    private int amount = 14773;

    private int spent = -3552;

    private int gain = 18325;

    public SQLiteOperationListTest() {
        super("com.cresus", Cresus.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        getActivity().getApplicationContext().deleteDatabase("test");
        _operations = new SQLiteOperationList(getActivity().getApplicationContext(), new SimpleOperationList("test"), "test");
        for (int i = 0; i < values.length; ++i) {
            _operations.createNewOperation(new Date(dates[i]), values[i], "op" + Integer.toString(i));
        }
    }

    protected void tearDown() {
        _operations.close();
        try {
            super.tearDown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void testAmount() {
        assertEquals(amount, _operations.amount(), 0);
    }

    public void testIncome() {
        assertEquals(gain, _operations.income(), 0);
    }

    public void testSpent() {
        assertEquals(spent, _operations.spent(), 0);
    }

    public void testOperationCount() {
        assertEquals(dates.length, _operations.numberOfOperations(), 0);
    }

    public void testOperationList() {
        int currentPos = 0;
        for (Iterator<Operation> iterator = _operations.operations(); iterator.hasNext(); ) {
            Operation type = iterator.next();
            assertEquals(dates[currentPos], type.getDate().getTime());
            assertEquals(values[currentPos], type.getAmount());
            ++currentPos;
        }
    }

    public void testReload() {
        SQLiteOperationList newList = new SQLiteOperationList(getActivity().getApplicationContext(), new SimpleOperationList("test"), "test");
        assertEquals(dates.length, newList.numberOfOperations(), 0);
        int currentPos = 0;
        for (Iterator<Operation> iterator = newList.operations(); iterator.hasNext(); ) {
            Operation type = iterator.next();
            Log.d("testReload", Long.toString(dates[currentPos]) + " " + Integer.toString(values[currentPos]) + " -> " + Long.toString(type.getDate().getTime()) + " " + Integer.toString(type.getAmount()));
            assertEquals(dates[currentPos], type.getDate().getTime());
            assertEquals(values[currentPos], type.getAmount());
            ++currentPos;
        }
        newList.close();
    }

    public void testSortDate() {
        _operations.sort(new OperationList.DateComparator());
        assertEquals(_operations.getOperation(0).getDate(), new Date(dates[0]));
        assertEquals(_operations.getOperation(1).getDate(), new Date(dates[1]));
        assertEquals(_operations.getOperation(2).getDate(), new Date(dates[2]));
        assertEquals(_operations.getOperation(3).getDate(), new Date(dates[3]));
        assertEquals(_operations.getOperation(4).getDate(), new Date(dates[4]));
        assertEquals(_operations.getOperation(5).getDate(), new Date(dates[5]));
        assertEquals(_operations.getOperation(6).getDate(), new Date(dates[6]));
    }

    public void testSortAmount() {
        _operations.sort(new OperationList.AmountComparator());
        assertEquals(_operations.getOperation(0).getAmount(), values[6]);
        assertEquals(_operations.getOperation(1).getAmount(), values[3]);
        assertEquals(_operations.getOperation(2).getAmount(), values[0]);
        assertEquals(_operations.getOperation(3).getAmount(), values[1]);
        assertEquals(_operations.getOperation(4).getAmount(), values[5]);
        assertEquals(_operations.getOperation(5).getAmount(), values[2]);
        assertEquals(_operations.getOperation(6).getAmount(), values[4]);
    }

    public void testSortLabel() {
        _operations.sort(new OperationList.LabelComparator());
        assertEquals(_operations.getOperation(0).getLabel(), "op" + Integer.toString(0));
        assertEquals(_operations.getOperation(1).getLabel(), "op" + Integer.toString(1));
        assertEquals(_operations.getOperation(2).getLabel(), "op" + Integer.toString(2));
        assertEquals(_operations.getOperation(3).getLabel(), "op" + Integer.toString(3));
        assertEquals(_operations.getOperation(4).getLabel(), "op" + Integer.toString(4));
        assertEquals(_operations.getOperation(5).getLabel(), "op" + Integer.toString(5));
        assertEquals(_operations.getOperation(6).getLabel(), "op" + Integer.toString(6));
    }
}
