package com.avaje.tests.singleTableInheritance;

import com.avaje.ebean.Ebean;
import com.avaje.tests.singleTableInheritance.model.PalletLocation;
import com.avaje.tests.singleTableInheritance.model.PalletLocationExternal;
import com.avaje.tests.singleTableInheritance.model.Zone;
import com.avaje.tests.singleTableInheritance.model.ZoneExternal;
import junit.framework.Assert;
import junit.framework.TestCase;
import java.util.List;

public class TestInheritQuery extends TestCase {

    public void test() {
        ZoneExternal zone = new ZoneExternal();
        zone.setAttribute("ABC");
        Ebean.save(zone);
        PalletLocationExternal location = new PalletLocationExternal();
        location.setZone(zone);
        location.setAttribute("123");
        Ebean.save(location);
        List<PalletLocation> locations = Ebean.find(PalletLocation.class).where().eq("zone", zone).findList();
        Assert.assertNotNull(locations);
        Assert.assertEquals(1, locations.size());
        PalletLocation rereadLoc = locations.get(0);
        Assert.assertTrue(rereadLoc instanceof PalletLocation);
        Zone rereadZone = rereadLoc.getZone();
        Assert.assertNotNull(rereadZone);
        Assert.assertTrue(rereadZone instanceof ZoneExternal);
    }
}
