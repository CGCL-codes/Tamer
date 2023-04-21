package com.criteria2jpql.common;

import static org.junit.Assert.assertEquals;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Omer Sunercan
 */
public class TestJpaQueryValueTranslator {

    enum TestEnum {

        CONSTANT_1, CONSTANT_2
    }

    ;

    private JpaQueryValueTranslator translator;

    @Before
    public void setUp() {
        translator = new JpaQueryValueTranslator();
    }

    @Test
    public void testTranslateString() throws Exception {
        assertEquals("'hi'", translator.translateValue("hi"));
        assertEquals("'%heLLo'", translator.translateValue("%heLLo"));
        assertEquals("'quot''ed'", translator.translateValue("quot'ed"));
    }

    @Test
    public void testTranslateEnum() throws Exception {
        assertEquals("1", translator.translateValue(TestEnum.CONSTANT_2));
    }

    @Test
    public void testTranslateDates() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 8, 21, 11, 20, 0);
        calendar.set(Calendar.MILLISECOND, 135);
        Date currentTime = calendar.getTime();
        assertEquals(":java_util_Date_2010_09_21_11_20_00_135", translator.translateValue(currentTime));
        assertEquals(":java_util_Calendar_2010_09_21_11_20_00_135", translator.translateValue(calendar));
        assertEquals(":java_sql_Date_2010_09_21", translator.translateValue(new java.sql.Date(currentTime.getTime())));
        assertEquals(":java_sql_Time_11_20_00", translator.translateValue(new java.sql.Time(currentTime.getTime())));
        assertEquals(":java_sql_Timestamp_2010_09_21_11_20_00_135", translator.translateValue(new Timestamp(currentTime.getTime())));
        assertEquals("CURRENT_DATE", translator.translateValue(ReservedValue.CURRENT_DATE));
    }

    @Test
    public void testGetNonParametricValues() throws Exception {
        translator.translateValue("Hello");
        translator.translateValue(new Integer(3));
        Map<String, Object> parametricValues = translator.getParametricValues();
        assertEquals(0, parametricValues.size());
    }

    @Test
    public void testGetParametricValues() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2010, 8, 21, 11, 20, 0);
        calendar.set(Calendar.MILLISECOND, 135);
        Date currentTime = calendar.getTime();
        Map<String, Object> parametricValues = translator.getParametricValues();
        assertEquals(0, parametricValues.size());
        translator.translateValue(currentTime);
        assertEquals(0, parametricValues.size());
        parametricValues = translator.getParametricValues();
        assertEquals(1, parametricValues.size());
        translator.translateValue(new java.sql.Date(calendar.getTimeInMillis()));
        translator.translateValue(new java.sql.Time(calendar.getTimeInMillis()));
        translator.translateValue(new java.sql.Timestamp(calendar.getTimeInMillis()));
        translator.translateValue(calendar);
        parametricValues = translator.getParametricValues();
        assertEquals(5, parametricValues.size());
        Object parameter = parametricValues.get("java_util_Date_2010_09_21_11_20_00_135");
        assertEquals(currentTime, parameter);
        parameter = parametricValues.get("java_util_Calendar_2010_09_21_11_20_00_135");
        assertEquals(calendar, parameter);
        parameter = parametricValues.get("java_sql_Date_2010_09_21");
        assertEquals(new java.sql.Date(currentTime.getTime()), parameter);
        parameter = parametricValues.get("java_sql_Time_11_20_00");
        assertEquals(new java.sql.Time(currentTime.getTime()), parameter);
        parameter = parametricValues.get("java_sql_Timestamp_2010_09_21_11_20_00_135");
        assertEquals(new java.sql.Timestamp(currentTime.getTime()), parameter);
    }
}
