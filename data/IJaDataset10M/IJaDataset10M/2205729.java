package de.hbrs.inf.atarrabi.test.model;

import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import de.hbrs.inf.atarrabi.enums.EntryType;
import de.hbrs.inf.atarrabi.model.Entry;

public class EntryTest {

    private Entry entry = null;

    @BeforeClass
    public void beforeClass() {
        entry = new Entry();
    }

    @AfterClass
    public void afterClass() {
    }

    @Test
    public void testSetGetId() {
        entry.setCeraId(1l);
        assert 1l == entry.getCeraId();
    }

    @Test
    public void testSetGetCeraId() {
        entry.setCeraId(2l);
        assert 2l == entry.getCeraId();
    }

    @Test
    public void testSetGetTitle() {
        entry.setTitle("TestTitle");
        assert "TestTitle".equals(entry.getTitle());
    }

    @Test
    public void testSetGetAcronmy() {
        entry.setEntityAcronym("TestAcronym");
        assert "TestAcronym".equals(entry.getEntityAcronym());
    }

    @Test
    public void testSetGetType() {
        entry.setType(EntryType.DATASET_GROUP);
        assert EntryType.DATASET_GROUP == entry.getType();
    }
}
