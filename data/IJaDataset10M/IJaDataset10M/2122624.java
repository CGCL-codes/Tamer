package com.javampire.util.dao.nodes;

import com.javampire.util.dao.RecordFactory;

/**
 * TODO: document this.
 *
 * @author <a href="mailto:cnagy@ecircle.de">Csaba Nagy</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2007/04/10 10:22:00 $
 */
public class DummyDataNodeFactory extends ArrayListDataNodeFactory<DummyRecord> {

    public DummyDataNodeFactory() {
        super(new TestRecordFactory());
    }

    private static class TestRecordFactory implements RecordFactory<DummyRecord> {

        public DummyRecord newRecord() {
            return new DummyRecord();
        }

        public void copy(DummyRecord record, DummyRecord destination) {
            destination.value = record.value;
        }
    }
}
