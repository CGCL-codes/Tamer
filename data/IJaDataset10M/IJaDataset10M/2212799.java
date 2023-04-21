package com.ars_eclectica.traceview;

import java.io.*;
import java.net.*;
import java.util.*;

/**
 * The <code>ABIFile</code> class encapsulates all of the information 
 * contained in an ABI trace file. This class implements the
 * <code>TraceFile</code> interface for files produced by the ABI PRISM 377
 * DNA Sequencer.
 *
 * @author	Luke McCarthy
 * @version	1.0 2001-09-27
 */
public class ABIFile implements TraceFile {

    public static final int TAG_LEN = 4;

    public static final int REC_LEN = 28;

    private int traceLength;

    private int sequenceLength;

    private short[] A;

    private short[] C;

    private short[] G;

    private short[] T;

    private short max;

    private String sequence;

    private short[] centers;

    /**
	 * Constructs a new <code>ABIFile</code> object from a File object
	 * pointing to a local .ab1 file.
	 *
	 * @param	file	.ab1 file from which to read the trace data
	 *
	 * @throws	FileFormatException if the parameter is not an ABI file
	 * @throws	IOException if there is an error reading the file
	 * @throws	SecurityException if there is an error reading the file  
	 */
    public ABIFile(File file) throws FileFormatException, IOException, SecurityException {
        RandomAccessFile f = null;
        try {
            f = new RandomAccessFile(file, "r");
        } catch (IllegalArgumentException e) {
        }
        if (!isABI(f)) {
            throw new FileFormatException(file.getPath() + " is not an ABI trace file");
        }
        char[] fwo = null;
        f.seek(18);
        int len = f.readInt();
        f.seek(26);
        int off = f.readInt();
        ABIRecord[] data = new ABIRecord[12];
        ABIRecord[] pbas = new ABIRecord[2];
        ABIRecord[] ploc = new ABIRecord[2];
        for (f.seek(off); len > 0; len--) {
            ABIRecord rec = new ABIRecord(f);
            if (rec.tag.equals("DATA")) {
                try {
                    data[rec.n - 1] = rec;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("ABI record contains erroneous n field");
                }
            } else if (rec.tag.equals("FWO_")) {
                fwo = ((String) rec.data).toCharArray();
            } else if (rec.tag.equals("PBAS")) {
                pbas[rec.n - 1] = rec;
            } else if (rec.tag.equals("PLOC")) {
                ploc[rec.n - 1] = rec;
            }
        }
        traceLength = data[8].len;
        sequenceLength = pbas[1].len;
        A = new short[traceLength];
        C = new short[traceLength];
        G = new short[traceLength];
        T = new short[traceLength];
        max = Short.MIN_VALUE;
        for (int i = 0; i < 4; i++) {
            f.seek(data[8 + i].off);
            short[] current = traceArray(fwo[i]);
            for (int j = 0; j < traceLength; j++) {
                current[j] = f.readShort();
                if (current[j] > max) max = current[j];
            }
        }
        byte[] buf = new byte[sequenceLength];
        f.seek(pbas[1].off);
        f.readFully(buf);
        sequence = new String(buf);
        centers = new short[sequenceLength];
        f.seek(ploc[1].off);
        for (int i = 0; i < sequenceLength; i++) centers[i] = f.readShort();
        f.close();
    }

    /**
	 * Constructs a new <code>ABIFile</code> object from a URL object
	 * pointing to a remote .ab1 file. Note that the file can't be too
	 * remote if we're running as an applet because applets only phone home.
	 *
	 * @param	url 	.ab1 file from which to read the trace data
	 *
	 * @throws	FileFormatException if the parameter is not an ABI file
	 * @throws	IOException if there is an error accessing the URL
	 */
    public ABIFile(URL url) throws FileFormatException, IOException {
        URLConnection connection = url.openConnection();
        int contentLength = connection.getContentLength();
        if (contentLength <= 0) throw new RuntimeException(url + " contained no content");
        byte[] content = new byte[contentLength];
        DataInputStream dis = new DataInputStream(connection.getInputStream());
        dis.readFully(content);
        dis.close();
        dis = new DataInputStream(new ByteArrayInputStream(content));
        if (!isABI(dis)) {
            throw new FileFormatException(url + " is not an ABI trace file");
        }
        char[] fwo = null;
        dis.reset();
        dis.skipBytes(18);
        int len = dis.readInt();
        dis.skipBytes(4);
        int off = dis.readInt();
        ABIRecord[] data = new ABIRecord[12];
        ABIRecord[] pbas = new ABIRecord[2];
        ABIRecord[] ploc = new ABIRecord[2];
        dis.reset();
        dis.skipBytes(off);
        for (; len > 0; len--) {
            ABIRecord rec = new ABIRecord(dis);
            if (rec.tag.equals("DATA")) {
                try {
                    data[rec.n - 1] = rec;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println("ABI record contains erroneous n field");
                }
            } else if (rec.tag.equals("FWO_")) {
                fwo = ((String) rec.data).toCharArray();
            } else if (rec.tag.equals("PBAS")) {
                pbas[rec.n - 1] = rec;
            } else if (rec.tag.equals("PLOC")) {
                ploc[rec.n - 1] = rec;
            }
        }
        traceLength = data[8].len;
        sequenceLength = pbas[1].len;
        A = new short[traceLength];
        C = new short[traceLength];
        G = new short[traceLength];
        T = new short[traceLength];
        max = Short.MIN_VALUE;
        for (int i = 0; i < 4; i++) {
            dis.reset();
            dis.skipBytes(data[8 + i].off);
            short[] current = traceArray(fwo[i]);
            for (int j = 0; j < traceLength; j++) {
                current[j] = dis.readShort();
                if (current[j] > max) max = current[j];
            }
        }
        byte[] buf = new byte[sequenceLength];
        dis.reset();
        dis.skipBytes(pbas[1].off);
        dis.readFully(buf);
        sequence = new String(buf);
        centers = new short[sequenceLength];
        dis.reset();
        dis.skipBytes(ploc[1].off);
        for (int i = 0; i < sequenceLength; i++) centers[i] = dis.readShort();
    }

    /** 
	 * Checks that a given file is actually an ABI trace file.
	 *
	 * @param	f	file to check
	 *
	 * @return	true if the parameter is an ABI file, false otherwise
	 *
	 * @throws	IOException if there is an error reading the file
	 */
    public static boolean isABI(RandomAccessFile f) throws IOException {
        byte[] tag = new byte[TAG_LEN];
        f.seek(0);
        f.readFully(tag);
        if (new String(tag).equals("ABIF")) return true;
        f.seek(26);
        f.readFully(tag);
        return new String(tag).equals("ABIF");
    }

    /**
	 * Checks that a given DataInputStream is coming from an actual ABI
	 * trace file.
	 *
	 * @param	in	the input stream to check
	 *
	 * @return	true if the parameter is an ABI file, false otherwise
	 *
	 * @throws	IOException if there is an error reading the stream
	 */
    public static boolean isABI(DataInputStream in) throws IOException {
        byte[] tag = new byte[TAG_LEN];
        in.reset();
        in.readFully(tag);
        if (new String(tag).equals("ABIF")) return true;
        in.skip(22);
        in.readFully(tag);
        return new String(tag).equals("ABIF");
    }

    /**
	 * Returns the trace array corresponding to the specified character.
	 *
	 * @param	c	a character specifying the trace array (A, T, C or G)
	 *
	 * @return	the specified trace array
	 */
    private short[] traceArray(char c) {
        switch(Character.toUpperCase(c)) {
            case 'A':
                return A;
            case 'C':
                return C;
            case 'G':
                return G;
            case 'T':
                return T;
            default:
                return null;
        }
    }

    public int traceAt(char nucleotide, int pos) {
        return (int) traceArray(nucleotide)[pos];
    }

    public char baseAt(int pos) {
        return sequence.charAt(pos);
    }

    public int centerOf(int pos) {
        return (int) centers[pos];
    }

    public String getSequence() {
        return sequence;
    }

    public int getTraceLength() {
        return traceLength;
    }

    public int getSequenceLength() {
        return sequenceLength;
    }

    public int getHighestPeak() {
        return (int) max;
    }
}

/**
 * The <code>ABIRecord</code> class encapsulates the data contained in a
 * single 28-byte record from an ABI trace file.
 *
 * @author	Luke McCarthy
 * @version	1.0 2001-09-27
 */
class ABIRecord {

    String tag;

    int n;

    short type;

    short size;

    int len;

    int total_size;

    Object data;

    int off;

    int cryptic;

    /**
	 * Constructs a new <code>ABIRecord</code>, reading data starting from
	 * the current position in the specified file.
	 *
	 * @param	f	file from which to read the data
	 *
	 * @throws	IOException if there is an error reading the file
	 */
    public ABIRecord(RandomAccessFile f) throws IOException {
        byte[] tmp = new byte[ABIFile.TAG_LEN];
        f.readFully(tmp);
        tag = new String(tmp);
        n = f.readInt();
        type = f.readShort();
        size = f.readShort();
        len = f.readInt();
        total_size = f.readInt();
        if (total_size > 4) {
            off = f.readInt();
        } else {
            switch(type) {
                case 2:
                    f.readFully(tmp);
                    data = new String(tmp);
                    break;
                case 4:
                    data = new Integer(f.readInt());
                    break;
                case 7:
                    data = new Float(f.readFloat());
                    break;
                case 18:
                    f.readFully(tmp);
                    data = new String(tmp);
                    break;
                default:
                    f.readFully(tmp);
                    data = tmp;
                    break;
            }
        }
        cryptic = f.readInt();
    }

    /**
	 * Constructs a new <code>ABIRecord</code>, reading data starting from
	 * the current position in the specified input stream.
	 *
	 * @param	in	stream from which to read the data
	 *
	 * @throws	IOException if there is an error reading the stream
	 */
    public ABIRecord(DataInputStream in) throws IOException {
        byte[] tmp = new byte[ABIFile.TAG_LEN];
        in.readFully(tmp);
        tag = new String(tmp);
        n = in.readInt();
        type = in.readShort();
        size = in.readShort();
        len = in.readInt();
        total_size = in.readInt();
        if (total_size > 4) {
            off = in.readInt();
        } else {
            switch(type) {
                case 2:
                    in.readFully(tmp);
                    data = new String(tmp);
                    break;
                case 4:
                    data = new Integer(in.readInt());
                    break;
                case 7:
                    data = new Float(in.readFloat());
                    break;
                case 18:
                    in.readFully(tmp);
                    data = new String(tmp);
                    break;
                default:
                    in.readFully(tmp);
                    data = tmp;
                    break;
            }
        }
        cryptic = in.readInt();
    }

    /**
	 * Returns the record as a String for debugging purposes.
	 *
	 * @return	the record as a String
	 */
    public String toString() {
        StringBuffer buf = new StringBuffer(256);
        buf.append(tag);
        buf.append(" ");
        buf.append(n);
        buf.append(" ");
        buf.append(type);
        buf.append(" ");
        buf.append(size);
        buf.append(" ");
        buf.append(len);
        buf.append(" ");
        buf.append(total_size);
        buf.append(" ");
        if (total_size > 4) buf.append(off); else buf.append(data);
        buf.append(" ");
        buf.append(cryptic);
        return buf.toString();
    }
}
