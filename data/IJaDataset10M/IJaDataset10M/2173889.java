package org.yarl.db;

import org.yarl.db.Equipment;
import org.yarl.db.EquipmentComparator;
import org.yarl.util.Log4jConfigurator;
import org.apache.log4j.Logger;
import junit.framework.TestCase;

public class EquipmentComparatorTest extends TestCase {

    private static Logger log4j = Logger.getLogger(EquipmentComparatorTest.class);

    public void testCompare() {
        Log4jConfigurator.configure();
        Equipment e1 = new Equipment();
        e1.setEquipmentId(99);
        e1.setDefaulter(true);
        e1.setNickname("Wadsworth");
        e1.setNotes("notes");
        e1.setPersonId(999);
        e1.setWorkoutTypeId(123);
        Equipment e2 = (Equipment) e1.clone();
        log4j.debug("e1: " + e1.toString());
        log4j.debug("e2: " + e2.toString());
        EquipmentComparator ec = new EquipmentComparator();
        assertTrue(ec.compare(e1, e2) == 0);
        e1.setNickname("Aadsworth");
        assertTrue(ec.compare(e1, e2) < 0);
        e1.setNickname("Zadsworth");
        assertTrue(ec.compare(e1, e2) > 0);
    }
}
