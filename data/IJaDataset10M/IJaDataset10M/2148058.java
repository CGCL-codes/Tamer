package org.jscsi.scsi.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class StringFieldValue implements FieldValue {

    private String value;

    public int getBitLength() {
        return value.length() * 8;
    }

    public List<Object> getValues() {
        List<Object> vals = new ArrayList<Object>();
        vals.add(this.value);
        return vals;
    }

    public Iterator<Iterable<Boolean>> iterator() {
        List<Iterable<Boolean>> list = new ArrayList<Iterable<Boolean>>(1);
        list.add(new Iterable<Boolean>() {

            public Iterator<Boolean> iterator() {
                return new ByteArrayFieldValue.BitIterator(value.getBytes());
            }
        });
        return list.iterator();
    }

    public int getLength() {
        return value.length();
    }

    public FieldType getType() {
        return FieldType.STRING;
    }

    @Override
    public String toString() {
        return "string(" + this.getLength() + "):\"" + this.value + "\"";
    }

    public Parser parse(String input, int offset) throws IOException {
        String[] elems = input.split(":");
        if (elems.length != 2) {
            throw new IOException("field value does not indicate type or value (column " + offset + ")");
        }
        int length = BitFieldValue.parseFieldLength("string", elems[0], offset);
        if (elems[1].length() != length) {
            throw new IOException("indicated field length (" + length + ") not equal to value length (column " + (offset + "string(".length()) + ")");
        }
        this.value = elems[1];
        return this;
    }
}
