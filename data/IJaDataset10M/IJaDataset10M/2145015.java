package com.entelience.test.test11objects;

import java.util.List;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;
import com.entelience.test.OurDbTestCase;
import com.entelience.objects.raci.RaciObjectType;
import com.entelience.raci.RaciDb;

public class test08RaciObjectType extends OurDbTestCase {

    @Test
    public void test01_check_object_and_db_in_sync() throws Exception {
        db.begin();
        RaciDb rdb = new RaciDb();
        rdb.setDb(db);
        List<RaciObjectType> list = rdb.getAllRaciObjectType();
        assertEquals("all minus reserver & undef", list.size(), RaciObjectType.values().length - 2);
        assertFalse(list.contains(RaciObjectType.RESERVED));
        assertFalse(list.contains(RaciObjectType.UNDEFINED));
        db.exit();
    }
}
