package cn.edu.thss.iise.beehivez.server.index.petrinetindex.invertedindex.dictionary;

import java.io.IOException;
import cn.edu.thss.iise.beehivez.server.index.petrinetindex.invertedindex.fileAccess.RAFile;

public class DocumentItem extends DictionaryItem {

    public String value;

    public boolean isDelete = false;

    private long[] phrases;

    private int size = 0;

    /**
	 * �����ʱ ʹ�õĹ��캯��
	 * 
	 * @param pos
	 * @param v
	 */
    public DocumentItem(long pos, String v, int phraseAccount) {
        super();
        value = v;
        position = pos;
        isDirty = true;
        phrases = new long[phraseAccount];
    }

    /**
	 * ����ʱ�Ĺ��캯��
	 */
    public DocumentItem() {
        super();
    }

    public void addPhrase(long v) {
        phrases[size] = v;
        size++;
    }

    @Override
    public int getLength() {
        return 9 + value.length() + (phrases.length << 3);
    }

    @Override
    public void readIn(RAFile file, long pos) throws IOException {
        file.seek(pos);
        position = pos;
        isDirty = false;
        isDelete = file.readBoolean();
        int len = file.readInt();
        byte[] key = new byte[len];
        file.read(key);
        value = new String(key);
        size = file.readInt();
        phrases = new long[size];
        for (int i = 0; i < size; i++) phrases[i] = file.readLong();
    }

    @Override
    public void writeOut(RAFile file) throws Exception {
        if (phrases.length != size) throw new Exception("some phrases haven't been added!");
        file.seek(position);
        file.writeBoolean(isDelete);
        file.writeInt(value.length());
        file.write(value.getBytes());
        file.writeInt(phrases.length);
        isDirty = false;
        for (long l : phrases) file.writeLong(l);
    }

    public long[] getPhrases() {
        return phrases;
    }

    public void delete() {
        isDirty = true;
        isDelete = true;
    }
}
