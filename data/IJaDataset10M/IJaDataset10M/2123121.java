package org.jtools.mapper;

import java.util.Calendar;
import java.util.Date;
import org.jpattern.mapper.Mapper;
import org.jtools.mapper.helper.CompoundHelper;
import org.jtools.mapper.helper.FromDateHelper;
import org.junit.Test;
import static org.junit.Assert.*;

public class CompoundHelperTest {

    @Test
    public void defaultFromDate() {
        CompoundHelper h = new CompoundHelper();
        h.createFromDate();
        Mapper<Object, Object> x = h.toMapper(Object.class, Object.class);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(1965, Calendar.JUNE, 5);
        assertEquals("1965-06-05", x.map(cal.getTime()));
    }

    @Test
    public void definedDateFormat() {
        CompoundHelper h = new CompoundHelper();
        ((FromDateHelper) h.createFromDate()).createDateFormat().setPattern("yyyyMMdd");
        Mapper<Object, Object> x = h.toMapper(Object.class, Object.class);
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(1965, Calendar.JUNE, 5);
        assertEquals("19650605", x.map(cal.getTime()));
        assertEquals("1965-06-05", x.map("1965-06-05"));
    }
}
