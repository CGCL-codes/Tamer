package com.road.user;

import org.testng.annotations.Test;
import com.road.geo.GeoIndex;
import com.road.geo.GeoObject;
import com.road.geo.Point;
import com.road.geo.Pointer;
import junit.framework.TestCase;

public class TestActiveUserIndex extends TestCase {

    @Test(groups = { "unit" })
    public void testSameRoad() throws UserException {
        Point pt = new Point("张江高科");
        Pointer othername = new Pointer("张江高科技园区", pt);
        GeoIndex geoIndex = new GeoIndex(new GeoObject[] { pt, othername });
        ActiveUser au1 = new ActiveUser("123", "张江高科");
        ActiveUser au2 = new ActiveUser("456", "玉兰香园");
        ActiveUserIndex ai = new ActiveUserIndex(new ActiveUser[] { au1, au2 }, geoIndex);
        assertEquals(1, ai.getUserCountOnSameRoad("张江高科"));
        ai.addNewUser(new ActiveUser("678", "张江高科技园区"));
        assertEquals(2, ai.getUserCountOnSameRoad("张江高科"));
        ai.removeUser("123");
    }
}
