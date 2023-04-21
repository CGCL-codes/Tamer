package gr.aueb.cs.nlg.Communications;

import java.io.*;
import java.nio.*;

public class Utils {

    public Utils() {
    }

    public static void readframe(DataInputStream ais, tnavframe aframe) throws IOException {
        try {
            byte[] ba = new byte[100];
            ais.read(ba, 0, 100);
            ByteBuffer bb = ByteBuffer.wrap(ba);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            byte b1 = bb.get();
            byte b2 = bb.get();
            byte b3 = bb.get();
            byte b4 = bb.get();
            aframe.framecontents = bb.getInt();
            aframe.packetcode = bb.getInt();
            aframe.packetid = bb.getInt();
            aframe.ts_sec = bb.getInt();
            aframe.ts_usec = bb.getInt();
            aframe.i[0] = bb.getInt();
            aframe.i[1] = bb.getInt();
            aframe.i[2] = bb.getInt();
            aframe.i[3] = bb.getInt();
            aframe.i[4] = bb.getInt();
            aframe.i[5] = bb.getInt();
            aframe.d[0] = bb.getDouble();
            aframe.d[1] = bb.getDouble();
            aframe.d[2] = bb.getDouble();
            aframe.d[3] = bb.getDouble();
            aframe.d[4] = bb.getDouble();
            aframe.d[5] = bb.getDouble();
            int datalen = bb.getInt();
            if (datalen != 0) {
                aframe.data = new byte[datalen];
                ais.read(aframe.data, 0, datalen);
            } else {
                aframe.data = null;
            }
        } catch (Exception e) {
        }
    }

    public static void readframeXML(DataInputStream ais, tnavframe aframe) throws IOException {
        try {
            byte[] ba = new byte[100];
            ais.readFully(ba, 0, 100);
            ByteBuffer bb = ByteBuffer.wrap(ba);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            byte b1 = bb.get();
            byte b2 = bb.get();
            byte b3 = bb.get();
            byte b4 = bb.get();
            aframe.framecontents = bb.getInt();
            aframe.packetcode = bb.getInt();
            aframe.packetid = bb.getInt();
            aframe.ts_sec = bb.getInt();
            aframe.ts_usec = bb.getInt();
            aframe.i[0] = bb.getInt();
            aframe.i[1] = bb.getInt();
            aframe.i[2] = bb.getInt();
            aframe.i[3] = bb.getInt();
            aframe.i[4] = bb.getInt();
            aframe.i[5] = bb.getInt();
            aframe.d[0] = bb.getDouble();
            aframe.d[1] = bb.getDouble();
            aframe.d[2] = bb.getDouble();
            aframe.d[3] = bb.getDouble();
            aframe.d[4] = bb.getDouble();
            aframe.d[5] = bb.getDouble();
            int datalen = bb.getInt();
            aframe.datalen = datalen;
            if (aframe.datalen != 0) {
                aframe.data = new byte[aframe.datalen];
                ais.readFully(aframe.data, 0, aframe.datalen);
            } else {
                aframe.data = null;
            }
        } catch (Exception e) {
            throw new IOException();
        }
    }

    public static void writeframe(DataOutputStream aos, tnavframe aframe) throws IOException {
        byte[] ba = new byte[100];
        ByteBuffer bb = ByteBuffer.wrap(ba);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put((byte) 211);
        bb.put((byte) 222);
        bb.put((byte) 233);
        bb.put((byte) 244);
        bb.putInt(aframe.framecontents);
        bb.putInt(aframe.packetcode);
        bb.putInt(aframe.packetid);
        bb.putInt(aframe.ts_sec);
        bb.putInt(aframe.ts_usec);
        bb.putInt(aframe.i[0]);
        bb.putInt(aframe.i[1]);
        bb.putInt(aframe.i[2]);
        bb.putInt(aframe.i[3]);
        bb.putInt(aframe.i[4]);
        bb.putInt(aframe.i[5]);
        bb.putDouble(aframe.d[0]);
        bb.putDouble(aframe.d[1]);
        bb.putDouble(aframe.d[2]);
        bb.putDouble(aframe.d[3]);
        bb.putDouble(aframe.d[4]);
        bb.putDouble(aframe.d[5]);
        if (aframe.data != null) {
            bb.putInt(aframe.data.length);
        } else {
            bb.putInt(0);
        }
        aos.write(ba, 0, 100);
        if (aframe.data != null) {
            aos.write(aframe.data, 0, aframe.data.length);
        }
    }

    public static void writeframeXML(DataOutputStream aos, int packet_code, String xmlData) throws IOException {
        try {
            System.err.println("writeframeXML");
            xmlData = xmlData + "\0";
            byte[] ba = new byte[104];
            ByteBuffer bb = ByteBuffer.wrap(ba);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put((byte) 211);
            bb.put((byte) 222);
            bb.put((byte) 233);
            bb.put((byte) 244);
            bb.putInt(1);
            bb.putInt(packet_code);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putInt(4 + xmlData.getBytes("UTF-8").length);
            bb.putInt(xmlData.getBytes("UTF-8").length);
            System.err.println("data send:" + xmlData + "$$");
            System.err.println("datalen send:" + (xmlData.getBytes("UTF-8").length));
            aos.write(ba, 0, 104);
            bb = ByteBuffer.wrap(xmlData.getBytes("UTF-8"));
            bb.order(ByteOrder.LITTLE_ENDIAN);
            aos.write(bb.array(), 0, xmlData.getBytes("UTF-8").length);
            aos.flush();
        } catch (Exception e) {
            throw new IOException();
        }
    }

    public static void declareProducedConsumedPackets(DataOutputStream aos, int num_of_produced_packets, int num_of_consumed_packets, int[] produced, int[] consumed, String module_name) throws IOException {
        String tmp = "";
        try {
            module_name = module_name + "\0";
            int module_name_size = module_name.getBytes("UTF-8").length;
            byte[] ba = new byte[100 + 4 + module_name_size + 4 * (1 + num_of_produced_packets + 1 + num_of_consumed_packets)];
            ByteBuffer bb = ByteBuffer.wrap(ba);
            bb.order(ByteOrder.LITTLE_ENDIAN);
            bb.put((byte) 211);
            bb.put((byte) 222);
            bb.put((byte) 233);
            bb.put((byte) 244);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putInt(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putDouble(0);
            bb.putInt(4 + module_name_size + 4 * (1 + num_of_produced_packets + 1 + num_of_consumed_packets));
            bb.putInt(module_name_size);
            bb.put((module_name).getBytes("UTF-8"));
            bb.putInt(num_of_produced_packets);
            for (int i = 0; i < num_of_produced_packets; i++) {
                bb.putInt(produced[i]);
            }
            bb.putInt(num_of_consumed_packets);
            for (int i = 0; i < num_of_consumed_packets; i++) {
                bb.putInt(consumed[i]);
            }
            aos.write(ba, 0, ba.length);
            aos.flush();
        } catch (Exception e) {
            throw new IOException();
        }
    }
}
