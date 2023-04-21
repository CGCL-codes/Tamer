package jmotor.util;

import junit.framework.TestCase;
import org.junit.Test;
import java.util.Date;

/**
 * Component:
 * Description:
 * Date: 11-8-18
 *
 * @author Andy.Ai
 */
public class DateUtilsTest extends TestCase {

    public void test() {
        testFormatDate(new Date());
    }

    @Test
    public void testFormatDate(Date date) {
        System.out.println(DateUtils.formatDate(date));
    }
}
