package edu.cmu.ece.wnp5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 *
 * @author admin
 */
public class RoutingBackwardPacket {

    public long source;

    public long destination;

    public int id;

    public int routeLength;

    public final long[] route = new long[10];

    public int totalRouteLength;

    public void readFrom(DataInputStream dis) throws IOException {
        source = RoutingLayer.ADDR_CONST | dis.readUnsignedShort();
        destination = RoutingLayer.ADDR_CONST | dis.readUnsignedShort();
        id = dis.readUnsignedShort();
        dis.skipBytes(1);
        routeLength = dis.readUnsignedByte();
        if (routeLength > 10) throw new IllegalArgumentException("Too many addresses on route!");
        for (int i = 0; i < 10; i++) route[i] = RoutingLayer.ADDR_CONST | dis.readUnsignedShort();
        totalRouteLength = dis.readUnsignedByte();
    }

    public void writeTo(DataOutputStream dos) throws IOException {
        dos.writeShort((short) source);
        dos.writeShort((short) destination);
        dos.writeShort(id);
        dos.write(0x42);
        if (routeLength > 10) throw new IllegalArgumentException("Too many addresses on route!");
        dos.writeByte(routeLength);
        for (int i = 0; i < 10; i++) dos.writeShort((short) route[i]);
        dos.writeByte(totalRouteLength);
    }
}
