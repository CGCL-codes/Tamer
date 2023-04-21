package it.simplerecords.util.test;

import java.util.Calendar;
import it.simplerecords.annotations.DoNotPrint;
import it.simplerecords.util.Printable;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PrintableTest {

    public class Bean extends Printable {

        public int id;

        public String description;

        public Object obj;

        public int[] values;
    }

    public class BeanSkipDescription extends Printable {

        public int id;

        @DoNotPrint
        public String description;

        public int[] values;

        public Object obj;
    }

    public class BeanSkipAll extends Printable {

        @DoNotPrint
        public int id;

        @DoNotPrint
        public String description;

        @DoNotPrint
        public int[] values;

        @DoNotPrint
        public Object obj;
    }

    private Bean bean;

    private Bean beanNull;

    private BeanSkipDescription beanSkipDescription;

    private BeanSkipAll beanSkipAll;

    @Before
    public void setUp() {
        bean = new Bean();
        bean.id = 15;
        bean.description = "Description";
        bean.values = new int[] { 1, 2, 3, 4, 5 };
        bean.obj = Calendar.getInstance().getTime();
        beanNull = new Bean();
        beanNull.id = 0;
        beanNull.description = null;
        beanNull.values = null;
        beanSkipDescription = new BeanSkipDescription();
        beanSkipDescription.id = 15;
        beanSkipDescription.description = "Description";
        beanSkipDescription.values = new int[] { 1, 2, 3, 4, 5 };
        beanSkipAll = new BeanSkipAll();
        beanSkipAll.id = 15;
        beanSkipAll.description = "Description";
        beanSkipAll.values = new int[] { 1, 2, 3, 4, 5 };
    }

    @Test
    public void testToString() {
        assertEquals("", bean.toString());
    }
}
