package pipboy.persistency.example;

import pipboy.persistency.annotation.PersistentRecord;

@PersistentRecord
public class AnotherRecord {

    public AnotherRecord(long id) {
        this.setId(id);
    }

    public AnotherRecord() {
    }

    private long idx;

    public long getId() {
        return idx;
    }

    public void setId(long id) {
        this.idx = id;
    }
}
