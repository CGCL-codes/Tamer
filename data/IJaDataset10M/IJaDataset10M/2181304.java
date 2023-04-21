package uk.ac.lkl.migen.system.server.manipulator.test;

import static org.junit.Assert.assertEquals;
import java.sql.SQLException;
import java.util.Set;
import uk.ac.lkl.common.util.database.DatabaseException;
import uk.ac.lkl.common.util.database.testing.EntityRow;
import uk.ac.lkl.common.util.database.testing.EntityTableSnapshotComparison;
import uk.ac.lkl.common.util.reflect.GenericClass;
import uk.ac.lkl.common.util.restlet.EntityMap;
import uk.ac.lkl.common.util.restlet.RestletException;
import uk.ac.lkl.common.util.restlet.server.EntityId;
import uk.ac.lkl.common.util.restlet.server.EntityManipulator;
import uk.ac.lkl.migen.system.server.Date;

public class DateManipulatorTester extends ManipulatorTester<Date> {

    public void testInsert() throws SQLException, RestletException, DatabaseException {
        insert(new Date(1997, 6, 7));
        EntityTableSnapshotComparison comparison = getTableComparison();
        Set<Integer> addedIds = comparison.getAddedIds();
        assertEquals(addedIds.size(), 1);
        int id = addedIds.iterator().next();
        EntityRow row = comparison.getAddedEntityRow(id);
        assertEquals(row.getFieldValue("year"), 1997);
        assertEquals(row.getFieldValue("month"), 6);
        assertEquals(row.getFieldValue("day"), 7);
    }

    public void testSelect() throws SQLException, RestletException {
        Date date1 = new Date(1998, 9, 10);
        Date date2 = select(date1);
        assertEquals(date1.getYear(), date2.getYear());
        assertEquals(date1.getMonth(), date2.getMonth());
        assertEquals(date1.getDay(), date2.getDay());
    }

    public void testUpdate() throws SQLException, RestletException, DatabaseException {
        Date date1 = new Date(1998, 9, 10);
        EntityManipulator<Date> manipulator = getManipulator();
        EntityId<Date> dateId = manipulator.insertObject(date1, new EntityMap());
        snapshot();
        Date date2 = new Date(1999, 10, 11);
        manipulator.updateObject(dateId, date2);
        snapshot();
        EntityTableSnapshotComparison comparison = getTableComparison();
        int count = comparison.getCommonIdCount();
        System.out.println(count);
        System.out.println(comparison.getChangedIds());
        assertEquals(comparison.getNewValue(dateId, "year"), 1999);
        assertEquals(comparison.getNewValue(dateId, "month"), 10);
        assertEquals(comparison.getNewValue(dateId, "day"), 11);
    }

    @Override
    protected GenericClass<Date> getEntityClass() {
        return GenericClass.getSimple(Date.class);
    }
}
