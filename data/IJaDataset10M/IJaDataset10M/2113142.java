package panda.query.scan;

import static java.sql.Types.INTEGER;
import java.util.ArrayList;
import java.util.List;
import panda.file.Block;
import panda.query.struct.Attribute;
import panda.query.struct.Constant;
import panda.query.struct.IntConstant;
import panda.query.struct.StringConstant;
import panda.record.RecordPage;
import panda.record.Schema;
import panda.record.TableToken;
import panda.transaction.Transaction;

public class ChunkScanner implements Scanner {

    private List<RecordPage> pages;

    private int start, end, current;

    private Schema sch;

    private RecordPage p;

    public ChunkScanner(TableToken tt, int start, int end, Transaction tx) {
        pages = new ArrayList<RecordPage>();
        this.start = start;
        this.end = end;
        this.sch = tt.getSchema();
        String fn = tt.getFilename();
        for (int i = start; i <= end; i++) {
            Block blk = new Block(fn, i);
            pages.add(new RecordPage(blk, tt, tx));
        }
        init();
    }

    public void init() {
        moveTo(start);
    }

    @Override
    public boolean next() {
        while (true) {
            if (p.next()) return true;
            if (current == end) return false;
            moveTo(current + 1);
        }
    }

    @Override
    public void close() {
        for (RecordPage p : pages) p.close();
    }

    @Override
    public Constant getValue(Attribute attr) {
        if (sch.getType(attr) == INTEGER) return new IntConstant(p.getInt(attr.getAttributeName())); else return new StringConstant(p.getString(attr.getAttributeName()));
    }

    public boolean hasAttribute(Attribute name) {
        return sch.hasAttribute(name);
    }

    private void moveTo(int no) {
        current = no;
        p = pages.get(current - start);
        p.moveTo(-1);
    }
}
