package util.templateGen;

import util.Xml;
import java.util.Vector;

public class XmlTask {

    public Vector incomingPackets = new Vector();

    public Vector outgoingPackets = new Vector();

    public static String generatedelay = new String("DELAY");

    public static String packetsize = new String("SIZE");

    public static void setDelay(int delay) {
        generatedelay = "" + delay;
    }

    public static void setPacketSize(int size) {
        packetsize = "" + size;
    }

    public int id;

    public String type;

    public XmlTask(String type, int id) {
        this.type = type;
        this.id = id;
    }

    public void addIncomingPacket(int id, int src) {
        xmlPacketStruct newpacket = new xmlPacketStruct();
        newpacket.setSrc(src);
        newpacket.setId(id);
        if (incomingPackets.size() > 0) {
            if (((xmlPacketStruct) incomingPackets.elementAt(0)).src >= newpacket.src) {
                incomingPackets.add(0, newpacket);
                return;
            }
            for (int i = 0; i < incomingPackets.size() - 1; i++) {
                if (((xmlPacketStruct) incomingPackets.elementAt(i)).src <= newpacket.src && ((xmlPacketStruct) incomingPackets.elementAt(i + 1)).src >= newpacket.src) {
                    incomingPackets.add(i + 1, newpacket);
                    return;
                }
            }
        }
        incomingPackets.add(newpacket);
    }

    public void addOutgoingPacket(int id, int dest) {
        xmlPacketStruct newpacket = new xmlPacketStruct();
        newpacket.setDest(dest);
        newpacket.setId(id);
        if (outgoingPackets.size() > 0) {
            if (((xmlPacketStruct) outgoingPackets.elementAt(0)).dest >= newpacket.dest) {
                outgoingPackets.add(0, newpacket);
                return;
            }
            for (int i = 0; i < outgoingPackets.size() - 1; i++) {
                if (((xmlPacketStruct) outgoingPackets.elementAt(i)).dest <= newpacket.dest && ((xmlPacketStruct) outgoingPackets.elementAt(i + 1)).dest >= newpacket.dest) {
                    outgoingPackets.add(i + 1, newpacket);
                    return;
                }
            }
        }
        outgoingPackets.add(newpacket);
    }

    public void writeToXml() {
        Xml.opnBlk("task", "type=\"" + type + "\" id=\"" + id + "\"");
        Xml.opnBlk("wait");
        for (int i = 0; i < incomingPackets.size(); i++) {
            ((xmlPacketStruct) incomingPackets.elementAt(i)).writeToXml();
        }
        Xml.clsBlk();
        Xml.opnBlk("generate", "delay=\"" + generatedelay + "\"");
        for (int i = 0; i < outgoingPackets.size(); i++) {
            ((xmlPacketStruct) outgoingPackets.elementAt(i)).writeToXml();
        }
        Xml.clsBlk();
        Xml.clsBlk();
    }

    public class xmlPacketStruct {

        public int id = -1;

        public int src = -1;

        public int dest = -1;

        public void setId(int id) {
            this.id = id;
        }

        public void setSrc(int src) {
            this.src = src;
        }

        public void setDest(int dest) {
            this.dest = dest;
        }

        public void writeToXml() {
            if (src != -1) {
                Xml.tagLess("packet", "id=\"" + id + "\" src=\"" + src + "\" size=\"" + XmlTask.packetsize + "\"");
            } else {
                Xml.tagLess("packet", "id=\"" + id + "\" dest=\"" + dest + "\" size=\"" + XmlTask.packetsize + "\"");
            }
        }
    }
}
