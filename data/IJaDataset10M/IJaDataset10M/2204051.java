package Core.PSI;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import API.BitOutputStream;
import API.MyUTIL;
import API.StreamType;
import API.Descriptor.Descriptor;
import API.PSI.PMTStream;

/**
 * @author SungHun Park (dr.superchamp@gmail.com)
 *
 */
public class PMTStreamDefaultImpl implements PMTStream {

    protected StreamType stream_type = StreamType.ISO_IEC_Reserved;

    protected int elementary_PID = 0;

    protected List<Descriptor> descs = new Vector<Descriptor>();

    public PMTStreamDefaultImpl(StreamType type, int pid) {
        stream_type = type;
        elementary_PID = pid;
    }

    @Override
    public int getElementaryPID() {
        return elementary_PID;
    }

    @Override
    public StreamType getStreamType() {
        return stream_type;
    }

    @Override
    public void setElementaryPID(int pid) {
        elementary_PID = pid;
    }

    @Override
    public void setStreamType(StreamType type) {
        stream_type = type;
    }

    @Override
    public int getSizeInBytes() {
        return 5 + getDescriptorsLength();
    }

    @Override
    public byte[] toByteArray() {
        BitOutputStream os = new BitOutputStream(getSizeInBytes() * Byte.SIZE);
        os.writeFromLSB(stream_type.getValue(), 8);
        os.writeFromLSB(0, 3);
        os.writeFromLSB(elementary_PID, 13);
        os.writeFromLSB(0, 4);
        os.writeFromLSB(getDescriptorsLength(), 12);
        Iterator<Descriptor> it = getDescriptors();
        while (it.hasNext()) os.write(it.next().toByteArray());
        return os.toByteArray();
    }

    @Override
    public String toXMLString(int base_space) {
        String str = new String();
        str += (MyUTIL.whiteSpaceStr(base_space) + "<PMTStream>\n");
        str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<stream_type>" + stream_type.getValue() + "</stream_type>\n");
        str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<elementary_PID>" + elementary_PID + "</elementary_PID>\n");
        if (getDescriptorSize() > 0) {
            Iterator<Descriptor> it = getDescriptors();
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "<DescriptorLoop>\n");
            while (it.hasNext()) str += it.next().toXMLString(base_space + 4);
            str += (MyUTIL.whiteSpaceStr(base_space + 2) + "</DescriptorLoop>\n");
        }
        str += (MyUTIL.whiteSpaceStr(base_space) + "</PMTStream>\n");
        return str;
    }

    @Override
    public boolean addDescriptor(Descriptor desc) {
        descs.add(desc);
        return true;
    }

    @Override
    public boolean addDescriptorAt(int index, Descriptor desc) {
        if (index < 0 || index > descs.size()) return false;
        descs.add(index, desc);
        return true;
    }

    @Override
    public Descriptor getDescriptorAt(int index) {
        if (index < 0 || index >= descs.size()) return null;
        return descs.get(index);
    }

    @Override
    public Iterator<Descriptor> getDescriptors() {
        return descs.iterator();
    }

    @Override
    public int getDescriptorSize() {
        return descs.size();
    }

    @Override
    public int getDescriptorsLength() {
        int desc_length = 0;
        Iterator<Descriptor> it = descs.iterator();
        while (it.hasNext()) desc_length += it.next().getSizeInBytes();
        return desc_length;
    }

    @Override
    public void removeAllDescriptors() {
        descs.removeAll(descs);
    }

    @Override
    public boolean removeDescriptor(Descriptor desc) {
        return descs.remove(desc);
    }

    @Override
    public boolean removeDescriptorAt(int index) {
        if (index < 0 || index >= descs.size()) return false;
        descs.remove(index);
        return true;
    }

    @Override
    public boolean setDescriptorAt(int index, Descriptor desc) {
        if (index < 0 || index >= descs.size()) return false;
        descs.set(index, desc);
        return true;
    }
}
