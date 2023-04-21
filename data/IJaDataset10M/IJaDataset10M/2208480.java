package configlib;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Anton Gravestam
 */
public class XMLArchiverOut implements ArchiverInOut {

    private static String LINE_CHANGE = "\n";

    private static String HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>" + LINE_CHANGE;

    private static String TAB = "    ";

    private static String TYPE_ADD_DOUBLE = " type = \"double\" ";

    private static String TYPE_ADD_FLOAT = " type = \"float\" ";

    private static String TYPE_ADD_INT = " type = \"int\" ";

    private static String TYPE_ADD_LONG = " type = \"long\" ";

    private static String TYPE_ADD_BYTE = " type = \"byte\" ";

    private static String TYPE_ADD_SHORT = " type = \"short\" ";

    private static String TYPE_ADD_STRING = " type = \"string\" ";

    private static String TYPE_ADD_CLASS = " type = ";

    private static String TYPE_ADD_BOOLEAN = " type = \"boolean\" ";

    OutputStream os = null;

    private int currentTabDepth = 0;

    private boolean writType = true;

    private boolean writeXMLHeader = true;

    private String rootTag;

    public XMLArchiverOut() {
    }

    public XMLArchiverOut(OutputStream os) throws IOException {
        this(os, false);
    }

    public XMLArchiverOut(OutputStream os, boolean writeXMLHeader) throws IOException {
        this.os = os;
        this.writeXMLHeader = writeXMLHeader;
        if (writeXMLHeader) {
            os.write(HEADER.getBytes());
        }
    }

    public XMLArchiverOut(OutputStream os, boolean writeXMLHeader, String rootTag) throws IOException {
        this.os = os;
        this.writeXMLHeader = writeXMLHeader;
        this.rootTag = rootTag;
        if (writeXMLHeader) {
            os.write(HEADER.getBytes());
        }
    }

    public void setOutputStream(OutputStream os) throws IOException {
        this.os = os;
        if (writeXMLHeader) {
            os.write(HEADER.getBytes());
        }
    }

    public void put(String name, byte v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_BYTE : "";
        putXMLLeaf(name, "" + v, typeAdd);
    }

    public void put(String name, short v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_SHORT : "";
        putXMLLeaf(name, "" + v, typeAdd);
    }

    public void put(String name, int v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_INT : "";
        putXMLLeaf(name, "" + v, typeAdd);
    }

    public void put(String name, long v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_LONG : "";
        putXMLLeaf(name, "" + v, typeAdd);
    }

    public void put(String name, float v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_FLOAT : "";
        putXMLLeaf(name, "" + v, typeAdd);
    }

    public void put(String name, double v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_DOUBLE : "";
        putXMLLeaf(name, "" + v, typeAdd);
    }

    public void put(String name, String v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_STRING : "";
        putXMLLeaf(name, "" + v, typeAdd);
    }

    public boolean getWriteXMLHeader() {
        return writeXMLHeader;
    }

    public void setWriteXMLHeader(boolean writeXMLHeader) {
        this.writeXMLHeader = writeXMLHeader;
    }

    public void setWriteTypes(boolean b) {
        writType = b;
    }

    private void putXMLLeaf(String name, String value, String type) throws IOException {
        String nodeString = tab(currentTabDepth) + "<" + name + type + ">" + value + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
    }

    private String tab(int i) {
        String ret = "";
        for (int j = 0; j < i; j++) {
            ret += TAB;
        }
        return ret;
    }

    public void putBytes(String name, List<Byte> collection) throws IOException {
        String typeAdd = writType ? TYPE_ADD_CLASS + "\"" + "list" + "\"" : "";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        for (Byte value : collection) {
            put("element", value);
        }
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
    }

    public void putInts(String name, List<Integer> collection) throws IOException {
        String typeAdd = writType ? TYPE_ADD_CLASS + "\"" + "list" + "\"" : "";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        for (Integer value : collection) {
            put("element", value);
        }
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
    }

    public void putDoubles(String name, List<Double> collection) throws IOException {
        String typeAdd = writType ? TYPE_ADD_CLASS + "\"" + "list" + "\"" : "";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        for (Double value : collection) {
            put("element", value);
        }
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
    }

    public void putFloats(String name, List<Float> collection) throws IOException {
        String typeAdd = writType ? TYPE_ADD_CLASS + "\"" + "list" + "\"" : "";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        for (Float value : collection) {
            put("element", value);
        }
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
    }

    public void putShorts(String name, List<Short> collection) throws IOException {
        String typeAdd = writType ? TYPE_ADD_CLASS + "\"" + "list" + "\"" : "";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        for (Short value : collection) {
            put("element", value);
        }
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
    }

    public void putLongs(String name, List<Long> collection) throws IOException {
        String typeAdd = writType ? TYPE_ADD_CLASS + "\"" + "list" + "\"" : "";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        for (Long value : collection) {
            put("element", value);
        }
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
    }

    public void putStrings(String name, List<String> collection) throws IOException {
        String typeAdd = writType ? TYPE_ADD_CLASS + "\"" + "list" + "\"" : "";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        for (String value : collection) {
            put("element", value);
        }
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
    }

    public void put(String name, boolean v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_BOOLEAN : "";
        putXMLLeaf(name, "" + v, typeAdd);
    }

    public int inout(String name, int v) throws IOException {
        put(name, v);
        return v;
    }

    public long inout(String name, long v) throws IOException {
        put(name, v);
        return v;
    }

    public byte inout(String name, byte v) throws IOException {
        put(name, v);
        return v;
    }

    public short inout(String name, short v) throws IOException {
        put(name, v);
        return v;
    }

    public float inout(String name, float v) throws IOException {
        put(name, v);
        return v;
    }

    public boolean inout(String name, boolean v) throws IOException {
        put(name, v);
        return v;
    }

    public String inout(String name, String v) throws IOException {
        put(name, v);
        return v;
    }

    public double inout(String name, double v) throws IOException {
        put(name, v);
        return v;
    }

    public Serializable inout(String name, Serializable v) throws IOException {
        String typeAdd = TYPE_ADD_CLASS + "\"" + v.getClass().getName() + "\"";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        v.serialize(this);
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        return v;
    }

    public List inout(String name, List v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_CLASS + "\"" + "list" + "\"" : "";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        for (Object value : v) {
            inout("element", (Serializable) value);
        }
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        return v;
    }

    public List<Integer> inoutIntegerList(String name, List<Integer> v) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Long> inoutLongList(String name, List<Long> v) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Byte> inoutByteList(String name, List<Byte> v) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Short> inoutShortList(String name, List<Short> v) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Float> inoutFloatList(String name, List<Float> v) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Boolean> inoutBooleanList(String name, List<Boolean> v) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<String> inoutStringList(String name, List<String> v) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Double> inoutDoubleList(String name, List<Double> v) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List inoutSerializableList(String name, List v) throws IOException {
        String typeAdd = writType ? TYPE_ADD_CLASS + "\"" + "list" + "\"" : "";
        String nodeString = tab(currentTabDepth) + "<" + name + typeAdd + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        currentTabDepth++;
        for (Object value : v) {
            inout("element", (Serializable) value);
        }
        currentTabDepth--;
        nodeString = tab(currentTabDepth) + "</" + name + ">" + LINE_CHANGE;
        os.write(nodeString.getBytes());
        return v;
    }
}
