package com.avaje.tests.basic;

import com.avaje.ebean.Ebean;
import com.avaje.tests.model.basic.OCar;
import com.avaje.tests.model.basic.OEngine;
import com.avaje.tests.model.basic.OGearBox;
import junit.framework.TestCase;

public class TestMultipleOneToOneIUD extends TestCase {

    public void test() {
        OEngine engine = new OEngine();
        engine.setShortDesc("engine 1");
        OGearBox gearBox = new OGearBox();
        gearBox.setBoxDesc("6 speed manual");
        gearBox.setSize(6);
        OCar car = new OCar();
        car.setVin("xx4534");
        car.setName("test car");
        car.setEngine(engine);
        Ebean.beginTransaction();
        try {
            Ebean.save(gearBox);
            Ebean.save(car);
            assertNotNull(car.getId());
            assertNotNull(engine.getEngineId());
            assertNotNull(gearBox.getId());
            Ebean.commitTransaction();
        } finally {
            Ebean.endTransaction();
        }
        OCar c2 = Ebean.find(OCar.class, car.getId());
        assertNotNull(c2);
        assertNotNull(c2.getEngine());
        assertNull(c2.getGearBox());
        c2.setGearBox(gearBox);
        Ebean.save(c2);
        OCar c3 = Ebean.find(OCar.class, car.getId());
        assertNotNull(c3);
        assertNotNull(c3.getEngine());
        assertNotNull(c3.getGearBox());
    }
}
