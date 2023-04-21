package com.entelience.test.test11objects;

import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.objects.portal.Top;
import com.entelience.objects.portal.ObjectType;
import com.entelience.objects.portal.PortalRequestObject;
import com.entelience.objects.portal.TimeScale;

public class test09Portal {

    @Test
    public void test01_top_get_object_type() throws Exception {
        Top t = new Top();
        System.out.println(t.getObjectType());
        assertTrue(t.getObjectType() == ObjectType.TOP);
    }

    @Test
    public void test02_portal_request_object_timescale_date_format() throws Exception {
        PortalRequestObject pro = new PortalRequestObject();
        for (TimeScale ts : TimeScale.values()) {
            pro.setTimeScale(ts);
            try {
                SimpleDateFormat format = new SimpleDateFormat(pro.getXAxisDateFormat(), Locale.US);
            } catch (Exception e) {
                System.out.println("Failed : " + ts + ", " + pro.getXAxisDateFormat());
                fail(ts.toString() + " " + e.toString());
            }
        }
    }
}
