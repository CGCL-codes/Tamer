package com.abra.j2xb;

import junit.framework.TestCase;
import com.abra.j2xb.exampleBeans.types.DateTypes;
import com.abra.j2xb.exampleBeans.types.BinaryTypes;
import com.abra.j2xb.beans.exceptions.MOBeansException;
import com.abra.j2xb.beans.xmlModel.MoXmlBindingModel;
import com.abra.j2xb.beans.xmlBinding.xmlBeans.XmlBeansPersister;
import java.io.IOException;
import java.io.File;
import java.util.GregorianCalendar;

/**
 * @author Yoav Abrahami
 * @version 1.0, May 1, 2008
 * @since   JDK1.5
 */
public class TestTypes extends TestCase {

    static Class classToTestDate = DateTypes.class;

    static String testNameDate = "DateTypes";

    static Class classToTestBinary = BinaryTypes.class;

    static String testNameBinary = "BinaryTypes";

    private DateTypes createDateTypes() {
        DateTypes dates = new DateTypes();
        dates.setCalDate(new GregorianCalendar(2007, 11, 3, 9, 32, 11));
        dates.setCalDateTime(new GregorianCalendar(2007, 11, 3, 9, 32, 11));
        dates.setCalTime(new GregorianCalendar(2007, 11, 3, 9, 32, 11));
        dates.setCalGDay(new GregorianCalendar(2007, 11, 3, 9, 32, 11));
        dates.setCalGMonth(new GregorianCalendar(2007, 11, 3, 9, 32, 11));
        dates.setCalGMonthDay(new GregorianCalendar(2007, 11, 3, 9, 32, 11));
        dates.setCalGYear(new GregorianCalendar(2007, 11, 3, 9, 32, 11));
        dates.setCalGYearMonth(new GregorianCalendar(2007, 11, 3, 9, 32, 11));
        dates.setSqlDate(new java.sql.Date(107, 11, 3));
        dates.setUtilDate(new java.util.Date(107, 11, 3));
        dates.setSqlTime(new java.sql.Time(9, 32, 11));
        dates.setSqlTimestamp(new java.sql.Timestamp(107, 11, 3, 9, 32, 11, 123));
        return dates;
    }

    private DateTypes createEmptyDateTypes() {
        DateTypes dates = new DateTypes();
        return dates;
    }

    private BinaryTypes createBinaryTypes() {
        BinaryTypes dates = new BinaryTypes();
        dates.setBinary1(new byte[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, -10, -20, -30, -40, -50, -60, -70, -80, -90, -100, -110 });
        dates.setBinary2(new byte[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, -10, -20, -30, -40, -50, -60, -70, -80, -90, -100, -110 });
        dates.setBinary3(new byte[] { 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, -10, -20, -30, -40, -50, -60, -70, -80, -90, -100, -110 });
        return dates;
    }

    private BinaryTypes createEmptyBinaryTypes() {
        BinaryTypes dates = new BinaryTypes();
        return dates;
    }

    private void init() {
        File tmp = new File(".tmp");
        if (!tmp.exists()) {
            tmp.mkdir();
        }
    }

    public void testDateXmlBeans() throws MOBeansException, IOException {
        init();
        MoXmlBindingModel xmlBindingModel = new MoXmlBindingModel();
        xmlBindingModel.addBean(classToTestDate);
        XmlBeansPersister xmlBeansPersister = new XmlBeansPersister();
        StandardSerializeScenario scenario = new StandardSerializeScenario(xmlBindingModel, xmlBeansPersister, classToTestDate, testNameDate, "xmlBeans");
        scenario.execute(createDateTypes());
        scenario.execute(createEmptyDateTypes());
    }

    public void testBinaryXmlBeans() throws MOBeansException, IOException {
        init();
        MoXmlBindingModel xmlBindingModel = new MoXmlBindingModel();
        xmlBindingModel.addBean(classToTestBinary);
        XmlBeansPersister xmlBeansPersister = new XmlBeansPersister();
        StandardSerializeScenario scenario = new StandardSerializeScenario(xmlBindingModel, xmlBeansPersister, classToTestBinary, testNameBinary, "xmlBeans");
        scenario.execute(createBinaryTypes());
        scenario.execute(createEmptyBinaryTypes());
    }
}
