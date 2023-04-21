package cn.edu.thss.iise.beehivez.server.index.petrinetindex.invertedindex.dictionary;

import java.io.IOException;
import cn.edu.thss.iise.beehivez.server.index.petrinetindex.invertedindex.fileAccess.RAFile;

public class RelationItem extends DictionaryItem {

    private long documentPos;

    private long next;

    public RelationItem(long p, long d) {
        super();
        position = p;
        documentPos = d;
        isDirty = true;
    }

    public RelationItem(long p, long d, long n) {
        super();
        position = p;
        documentPos = d;
        next = n;
        isDirty = true;
    }

    public void setDocumentPos(long d) {
        documentPos = d;
        isDirty = true;
    }

    public long getNext() {
        return next;
    }

    public void setNext(long n) {
        next = n;
        isDirty = true;
    }

    public RelationItem() {
        super();
    }

    public long getDocumentPos() {
        return documentPos;
    }

    @Override
    public int getLength() {
        return 16;
    }

    @Override
    public void readIn(RAFile file, long pos) throws IOException {
        file.seek(pos);
        position = pos;
        documentPos = file.readLong();
        next = file.readLong();
        isDirty = false;
    }

    @Override
    public void writeOut(RAFile file) throws IOException {
        file.seek(position);
        file.writeLong(documentPos);
        file.writeLong(next);
        isDirty = false;
    }
}
