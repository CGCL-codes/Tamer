package ubersoldat.net.udppackets;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import ubersoldat.io.SoldatInputStream;
import ubersoldat.io.SoldatOutputStream;
import ubersoldat.net.SoldatHelper;

public class X15 extends UdpPacket<X15> {

    private int PlayerIndex;

    private Point.Float Point;

    private Point.Float Velocity;

    private int Direction;

    private int Unknown0;

    private int Unknown1;

    public int getPlayerIndex() {
        return PlayerIndex;
    }

    public void setPlayerIndex(int playerIndex) {
        PlayerIndex = playerIndex;
    }

    public Point.Float getPoint() {
        return Point;
    }

    public void setPoint(Point.Float point) {
        Point = point;
    }

    public Point.Float getVelocity() {
        return Velocity;
    }

    public void setVelocity(Point.Float velocity) {
        Velocity = velocity;
    }

    public int getDirection() {
        return Direction;
    }

    public void setDirection(int direction) {
        Direction = direction;
    }

    public int getUnknown0() {
        return Unknown0;
    }

    public void setUnknown0(int unknown0) {
        Unknown0 = unknown0;
    }

    public int getUnknown1() {
        return Unknown1;
    }

    public void setUnknown1(int unknown1) {
        Unknown1 = unknown1;
    }

    @Override
    public X15 readPacket(DatagramPacket p) {
        try {
            ByteArrayInputStream tmpIn = new ByteArrayInputStream(p.getData(), p.getOffset(), p.getLength());
            SoldatInputStream in = new SoldatInputStream(tmpIn);
            X15 obj = new X15();
            in.skip(1);
            obj.setPlayerIndex(in.read());
            obj.setPoint(new Point.Float(in.readFloat(), in.readFloat()));
            obj.setVelocity(new Point.Float(in.readFloat(), in.readFloat()));
            obj.setDirection(in.read());
            obj.setUnknown0(in.read());
            obj.setUnknown1(in.read());
            System.out.println("X15 - Unknown0:" + obj.getUnknown0());
            System.out.println("X15 - Unknown1:" + obj.getUnknown1());
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public DatagramPacket writePacket(X15 obj) {
        try {
            ByteArrayOutputStream tmpOut = new ByteArrayOutputStream();
            SoldatOutputStream out = new SoldatOutputStream(tmpOut);
            out.write(SoldatHelper.idx15);
            out.write(obj.getPlayerIndex());
            out.writeFloat(obj.getPoint().x);
            out.writeFloat(obj.getPoint().y);
            out.writeFloat(obj.getVelocity().x);
            out.writeFloat(obj.getVelocity().y);
            out.write(obj.getDirection());
            out.write(obj.getUnknown0());
            out.write(obj.getUnknown1());
            out.flush();
            out.close();
            byte[] data = tmpOut.toByteArray();
            return new DatagramPacket(data, data.length);
        } catch (Exception e) {
            return null;
        }
    }
}
