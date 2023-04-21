package org.openmobster.device.agent.service.database;

/**
 * @author openmobster@gmail.com
 */
public abstract class AbstractTable {

    private long id;

    private Record record;

    public AbstractTable() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }
}
