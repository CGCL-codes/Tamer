package matlab;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic Matlab variable from file bytes.
 * @author risto
 *
 */
public class Variable extends Chunk implements IVariable {

    List<Chunk> contents = new ArrayList<Chunk>();

    int CONTENTS_ARRAYFLAGS = 0;

    int CONTENTS_DIMENSIONS = 1;

    int CONTENTS_ARRAYNAME = 2;

    int CONTENTS_REALPART = 3;

    int CONTENTS_IMAGPART = 4;

    /**
	 * Read variable from bytes
	 * 
	 * @param data
	 * @param offset
	 * @throws IOException
	 */
    public Variable(byte[] data, int offset) throws IOException {
        super(data, offset);
        if (type != ChunkType.MATRIX) throw new IOException("Variable chunk not of type MATRIX!");
        parse();
    }

    /**
	 * Read contents, ArrayFlags, Dimensions, ArrayName, RealPart [, ImagPart]
	 * @throws IOException 
	 */
    private void parse() throws IOException {
        int off = 0;
        for (int i = 0; i < 3; i++) {
            Chunk c = new Chunk(bytes, off);
            contents.add(c);
            off += c.paddedLength();
        }
        Numeric c = new Numeric(bytes, off);
        contents.add(c);
        off += c.paddedLength();
        if (isComplex()) contents.add(new Numeric(bytes, off));
        System.out.println("Var " + getName() + " Type " + getType() + " Dim " + getDimensions()[0] + " x " + getDimensions()[1]);
    }

    /**
	 * Array Flags  operations
	 */
    private int getArrayFlags() {
        return Binary.intFrom(contents.get(CONTENTS_ARRAYFLAGS).bytes, 0);
    }

    public boolean isComplex() {
        return (getArrayFlags() & (1 << 13)) == 1;
    }

    public boolean isGlobal() {
        return (getArrayFlags() & (1 << 12)) == 1;
    }

    public boolean isLogical() {
        return (getArrayFlags() & (1 << 11)) == 1;
    }

    public ArrayType getType() {
        return ArrayType.valueOf((getArrayFlags() & 0xFF));
    }

    /**
	 * Dimensions
	 */
    public int[] getDimensions() {
        Chunk c = contents.get(CONTENTS_DIMENSIONS);
        int n = c.len / 4;
        int[] dim = new int[n];
        for (int i = 0; i < n; i++) {
            dim[i] = Binary.intFrom(c.bytes, i * 4);
        }
        return dim;
    }

    /**
	 * ArrayName
	 */
    public String getName() {
        Chunk c = contents.get(CONTENTS_ARRAYNAME);
        return new String(c.bytes).trim();
    }

    /**
	 * Numeric (or data) parts
	 */
    public boolean isDouble() {
        return getType() == ArrayType.DOUBLE;
    }

    public boolean isInt8() {
        return getType() == ArrayType.INT8;
    }

    public boolean isInt16() {
        return getType() == ArrayType.INT16;
    }

    public boolean isInt32() {
        return getType() == ArrayType.INT32;
    }

    public boolean isText() {
        return getType() == ArrayType.CHAR;
    }

    public boolean isInteger() {
        return isInt8() || isInt16() || isInt32();
    }

    public double[] getDoubleArray() {
        Numeric c = (Numeric) contents.get(CONTENTS_REALPART);
        if (c.da == null) c.convertToDouble();
        return c.da;
    }

    public int[] getIntegerArray() {
        Numeric c = (Numeric) contents.get(CONTENTS_REALPART);
        return c.ia;
    }

    public String getText() {
        try {
            Chunk c = contents.get(CONTENTS_REALPART);
            return new String(c.bytes, "UTF-16LE");
        } catch (UnsupportedEncodingException uee) {
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getName());
        if (isDouble()) {
            sb.append("=[");
            for (double d : getDoubleArray()) sb.append(String.format("%.2f,", d));
            sb.append("]");
        } else if (isText()) {
            sb.append("=" + getText());
        } else if (isInteger()) {
            sb.append("=[");
            for (int i : getIntegerArray()) sb.append(String.format("%d,", i));
            sb.append("]");
        } else {
            sb.append("[Type " + contents.get(CONTENTS_REALPART).type + "]");
        }
        return sb.toString();
    }
}
