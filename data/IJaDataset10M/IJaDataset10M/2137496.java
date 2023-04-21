package edu.mit.lcs.haystack.lucene.index;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.InputStream;
import org.apache.lucene.store.OutputStream;
import java.io.IOException;
import java.util.Vector;

final class SegmentInfos extends Vector {

    public static final int FORMAT = -1;

    public int counter = 0;

    private long version = 0;

    public final SegmentInfo info(int i) {
        return (SegmentInfo) elementAt(i);
    }

    public final void read(Directory directory) throws IOException {
        InputStream input = directory.openFile("segments");
        try {
            int format = input.readInt();
            if (format < 0) {
                if (format < FORMAT) throw new IOException("Unknown format version: " + format);
                version = input.readLong();
                counter = input.readInt();
            } else {
                counter = format;
            }
            for (int i = input.readInt(); i > 0; i--) {
                SegmentInfo si = new SegmentInfo(input.readString(), input.readInt(), directory);
                addElement(si);
            }
            if (format >= 0) {
                if (input.getFilePointer() >= input.length()) version = 0; else version = input.readLong();
            }
        } finally {
            input.close();
        }
    }

    public final void write(Directory directory) throws IOException {
        OutputStream output = directory.createFile("segments.new");
        try {
            output.writeInt(FORMAT);
            output.writeLong(++version);
            output.writeInt(counter);
            output.writeInt(size());
            for (int i = 0; i < size(); i++) {
                SegmentInfo si = info(i);
                output.writeString(si.name);
                output.writeInt(si.docCount);
            }
        } finally {
            output.close();
        }
        directory.renameFile("segments.new", "segments");
    }

    /**
   * version number when this SegmentInfos was generated.
   */
    public long getVersion() {
        return version;
    }

    /**
   * Current version number from segments file.
   */
    public static long readCurrentVersion(Directory directory) throws IOException {
        InputStream input = directory.openFile("segments");
        int format = 0;
        long version = 0;
        try {
            format = input.readInt();
            if (format < 0) {
                if (format < FORMAT) throw new IOException("Unknown format version: " + format);
                version = input.readLong();
            }
        } finally {
            input.close();
        }
        if (format < 0) return version;
        SegmentInfos sis = new SegmentInfos();
        sis.read(directory);
        return sis.getVersion();
    }
}
