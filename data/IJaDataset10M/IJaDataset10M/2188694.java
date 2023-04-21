package utils;

import java.util.Date;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GetterUtilTest extends TestCase {

    private static Log logger = LogFactory.getLog(GetterUtilTest.class.getName());

    public void testGetDefaultFormattedDate() {
        String date = "01/24/2008";
        String formattedDate = GetterUtil.getDefaultFormattedDate(date);
        logger.info(formattedDate);
        String d = "November 10, 2008";
        System.out.println(new Date(d));
    }
}
