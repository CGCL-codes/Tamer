package org.amadeus.audiocustominfo.mp3.lyrics3;

import org.amadeus.audiocustominfo.mp3.InvalidTagException;
import org.amadeus.audiocustominfo.mp3.TagConstant;
import org.amadeus.audiocustominfo.mp3.TagOptionSingleton;
import org.amadeus.audiocustominfo.mp3.id3.FrameBodySYLT;
import org.amadeus.audiocustominfo.mp3.id3.FrameBodyUSLT;
import org.amadeus.audiocustominfo.mp3.object.AbstractMP3Object;
import org.amadeus.audiocustominfo.mp3.object.ObjectID3v2LyricLine;
import org.amadeus.audiocustominfo.mp3.object.ObjectLyrics3Line;
import org.amadeus.audiocustominfo.mp3.object.ObjectLyrics3TimeStamp;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Lyrics multi line text. Timestamps can be used anywhere in the text in any order. Timestamp format is [mm:ss] (no
 * spaces allowed in the timestamps).
 *
 * @author Eric Farng
 * @version $Revision: 1.1 $
 */
public class FieldBodyLYR extends AbstractLyrics3v2FieldBody {

    private ArrayList lines = new ArrayList();

    /**
     * Creates a new FieldBodyLYR object.
     */
    public FieldBodyLYR() {
        super();
    }

    /**
     * Creates a new FieldBodyLYR object.
     */
    public FieldBodyLYR(final FieldBodyLYR copyObject) {
        super(copyObject);
        ObjectLyrics3Line oldObject;
        for (int i = 0; i < copyObject.lines.size(); i++) {
            oldObject = (ObjectLyrics3Line) copyObject.lines.get(i);
            AbstractMP3Object newObject = new ObjectLyrics3Line(oldObject);
            this.lines.add(newObject);
        }
    }

    /**
     * Creates a new FieldBodyLYR object.
     */
    public FieldBodyLYR(final String line) {
        readString(line);
    }

    /**
     * Creates a new FieldBodyLYR object.
     */
    public FieldBodyLYR(final FrameBodySYLT sync) {
        addLyric(sync);
    }

    /**
     * Creates a new FieldBodyLYR object.
     */
    public FieldBodyLYR(final FrameBodyUSLT unsync) {
        addLyric(unsync);
    }

    /**
     * Creates a new FieldBodyLYR object.
     */
    public FieldBodyLYR(final RandomAccessFile file) throws InvalidTagException, java.io.IOException {
        this.read(file);
    }

    public String getIdentifier() {
        return "LYR";
    }

    public void setLyric(final String str) {
        readString(str);
    }

    public String getLyric() {
        return writeString();
    }

    public int getSize() {
        int size = 0;
        ObjectLyrics3Line line;
        for (int i = 0; i < this.lines.size(); i++) {
            line = (ObjectLyrics3Line) this.lines.get(i);
            size += (line.getSize() + 2);
        }
        return size;
    }

    public boolean isSubsetOf(final Object object) {
        if ((object instanceof FieldBodyLYR) == false) {
            return false;
        }
        final ArrayList superset = ((FieldBodyLYR) object).lines;
        for (int i = 0; i < this.lines.size(); i++) {
            if (superset.contains(this.lines.get(i)) == false) {
                return false;
            }
        }
        return super.isSubsetOf(object);
    }

    public void addLyric(final FrameBodySYLT sync) {
        final Iterator iterator = sync.iterator();
        ObjectLyrics3Line newLine;
        ObjectID3v2LyricLine currentLine;
        ObjectLyrics3TimeStamp timeStamp;
        final HashMap lineMap = new HashMap();
        while (iterator.hasNext()) {
            currentLine = (ObjectID3v2LyricLine) iterator.next();
            currentLine = new ObjectID3v2LyricLine(currentLine);
            timeStamp = new ObjectLyrics3TimeStamp("Time Stamp");
            timeStamp.setTimeStamp(currentLine.getTimeStamp(), sync.getTimeStampFormat());
            if (lineMap.containsKey(currentLine.getText())) {
                newLine = (ObjectLyrics3Line) lineMap.get(currentLine.getText());
                newLine.addTimeStamp(timeStamp);
            } else {
                newLine = new ObjectLyrics3Line("Lyric Line");
                newLine.setLyric(currentLine);
                newLine.setTimeStamp(timeStamp);
                lineMap.put(currentLine.getText(), newLine);
                this.lines.add(newLine);
            }
        }
    }

    public void addLyric(final FrameBodyUSLT unsync) {
        final ObjectLyrics3Line line = new ObjectLyrics3Line("Lyric Line");
        line.setLyric(new String(unsync.getLyric()));
        this.lines.add(line);
        appendToObjectList(line);
    }

    public boolean equals(final Object obj) {
        if ((obj instanceof FieldBodyLYR) == false) {
            return false;
        }
        final FieldBodyLYR fieldBodyLYR = (FieldBodyLYR) obj;
        if (this.lines.equals(fieldBodyLYR.lines) == false) {
            return false;
        }
        return super.equals(obj);
    }

    public boolean hasTimeStamp() {
        boolean present = false;
        for (int i = 0; i < this.lines.size(); i++) {
            if (((ObjectLyrics3Line) this.lines.get(i)).hasTimeStamp()) {
                present = true;
            }
        }
        return present;
    }

    public Iterator iterator() {
        return this.lines.iterator();
    }

    protected void setupObjectList() {
    }

    public void read(final RandomAccessFile file) throws InvalidTagException, java.io.IOException {
        final String lineString;
        byte[] buffer = new byte[5];
        file.read(buffer, 0, 5);
        final int size = Integer.parseInt(new String(buffer, 0, 5));
        if ((size == 0) && (TagOptionSingleton.getInstance().isLyrics3KeepEmptyFieldIfRead() == false)) {
            throw new InvalidTagException("Lyircs3v2 Field has size of zero.");
        }
        buffer = new byte[size];
        file.read(buffer);
        lineString = new String(buffer);
        readString(lineString);
    }

    public String toString() {
        String str = getIdentifier() + " : ";
        for (int i = 0; i < this.lines.size(); i++) {
            str += this.lines.get(i).toString();
        }
        return str;
    }

    public void write(final RandomAccessFile file) throws java.io.IOException {
        final int size;
        int offset = 0;
        byte[] buffer = new byte[5];
        String str;
        size = getSize();
        str = Integer.toString(size);
        for (int i = 0; i < (5 - str.length()); i++) {
            buffer[i] = (byte) '0';
        }
        offset += (5 - str.length());
        for (int i = 0; i < str.length(); i++) {
            buffer[i + offset] = (byte) str.charAt(i);
        }
        offset += str.length();
        file.write(buffer, 0, 5);
        if (size > 0) {
            str = writeString();
            buffer = new byte[str.length()];
            for (int i = 0; i < str.length(); i++) {
                buffer[i] = (byte) str.charAt(i);
            }
            file.write(buffer);
        }
    }

    private void readString(final String lineString) {
        String token;
        int offset = 0;
        int delim = lineString.indexOf(TagConstant.SEPERATOR_LINE);
        this.lines = new ArrayList();
        ObjectLyrics3Line line;
        while (delim >= 0) {
            token = lineString.substring(offset, delim);
            line = new ObjectLyrics3Line("Lyric Line");
            line.setLyric(token);
            this.lines.add(line);
            appendToObjectList(line);
            offset = delim + TagConstant.SEPERATOR_LINE.length();
            delim = lineString.indexOf(TagConstant.SEPERATOR_LINE, offset);
        }
        if (offset < lineString.length()) {
            token = lineString.substring(offset);
            line = new ObjectLyrics3Line("Lyric Line");
            line.setLyric(token);
            this.lines.add(line);
            appendToObjectList(line);
        }
    }

    private String writeString() {
        ObjectLyrics3Line line;
        String str = "";
        for (int i = 0; i < this.lines.size(); i++) {
            line = (ObjectLyrics3Line) this.lines.get(i);
            str += (line.writeString() + TagConstant.SEPERATOR_LINE);
        }
        return str;
    }
}
