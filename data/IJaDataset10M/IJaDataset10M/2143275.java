package src.backend;

import src.gui.*;
import src.backend.*;
import java.util.ArrayList;

public class LinsChunk extends Chunk implements IChunk {

    private ArrayList entries = new ArrayList(0);

    private int numEntries;

    public LinsChunk(Level level, ChunkHeader header) {
        super(level, header);
    }

    public LinsChunk() {
        super();
    }

    public LinsEntry getEntry(int entry) {
        if (entry < this.entries.size()) {
            return (LinsEntry) this.entries.get(entry);
        } else {
            System.out.println("ObjsChunk does not contain that many entries. Returning highest entry.");
            return (LinsEntry) this.entries.get(this.entries.size() - 1);
        }
    }

    public void read() {
        super.read();
        MapWriter mw = this.level.getMapData().getMapWriter();
        this.numEntries = (int) this.header.getLength() / LinsEntry.ENTRY_LENGTH;
        mw.seek(this.header.getOffset() + getLevel().getMapData().getEntryHeaderSize());
        for (int i = 0; i < this.numEntries; i++) {
            LinsEntry linsEntry = new LinsEntry(this);
            linsEntry.read();
            this.entries.add(linsEntry);
        }
    }

    public void write() {
        super.write();
        MapWriter mw = this.level.getMapData().getMapWriter();
        mw.seek(this.header.getOffset() + getLevel().getMapData().getEntryHeaderSize() + getLevel().getOffset());
        for (int i = 0; i < this.entries.size(); i++) {
            ((LinsEntry) this.entries.get(i)).write();
        }
    }

    public int getNumEntries() {
        return this.entries.size();
    }

    public int getEntryLength() {
        return LinsEntry.ENTRY_LENGTH;
    }

    public int calc() {
        return super.calc();
    }

    public LinsEntry addEntry() {
        LinsEntry o = new LinsEntry(this);
        int len = getEntryLength();
        this.numEntries++;
        return o;
    }

    public void addEntry(LinsEntry oe) {
        this.entries.add(oe);
        this.numEntries++;
    }

    public LinsEntry removeEntry(LinsEntry o) {
        for (int i = 0; i < this.entries.size(); i++) {
            if (o.equals(getEntry(i))) {
                this.entries.remove(i);
                this.numEntries--;
                return o;
            }
        }
        System.out.println("That LinsEntry does not exist in the LinsChunk and can't be removed.");
        return o;
    }
}
