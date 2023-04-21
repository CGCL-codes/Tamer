package record;

public class Record {

    private int blknum;

    private int id;

    public Record(int blknum, int id) {
        this.blknum = blknum;
        this.id = id;
    }

    public int blockNumber() {
        return blknum;
    }

    public int id() {
        return id;
    }

    public boolean equals(Record r) {
        return blknum == r.blknum && id == r.id;
    }

    @Override
    public String toString() {
        return "[" + blknum + ", " + id + "]";
    }
}
