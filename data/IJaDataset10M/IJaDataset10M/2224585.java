package ubersoldat.net.udppackets;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;

public abstract class UdpPacket<T extends UdpPacket<?>> {

    public void test() throws IOException {
        try {
            String filename = this.getClass().getSimpleName();
            filename = filename.substring(1, filename.length());
            filename = "samples/X" + filename.toUpperCase();
            FileInputStream in = new FileInputStream(filename + ".srv");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int tmp;
            while ((tmp = in.read()) != -1) {
                out.write(tmp);
            }
            out.close();
            byte[] data = out.toByteArray();
            DatagramPacket p = new DatagramPacket(data, data.length);
            T bla = readPacket(p);
            p = writePacket(bla);
            FileOutputStream fout = new FileOutputStream(filename + ".srv_out");
            fout.write(p.getData(), p.getOffset(), p.getLength());
            fout.close();
        } catch (FileNotFoundException e) {
        }
        try {
            String filename = this.getClass().getSimpleName();
            filename = filename.substring(1, filename.length());
            filename = "sampels/X" + filename.toUpperCase();
            FileInputStream in = new FileInputStream(filename + ".cli");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int tmp;
            while ((tmp = in.read()) != -1) {
                out.write(tmp);
            }
            out.close();
            byte[] data = out.toByteArray();
            DatagramPacket p = new DatagramPacket(data, data.length);
            T bla = readPacket(p);
            p = writePacket(bla);
            FileOutputStream fout = new FileOutputStream(filename + ".cli_out");
            fout.write(p.getData(), p.getOffset(), p.getLength());
            fout.close();
        } catch (FileNotFoundException e) {
        }
    }

    public static void Inflate(String filename) throws IOException {
        FileInputStream in = new FileInputStream(filename);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int tmp;
        while ((tmp = in.read()) != -1) {
            out.write(tmp);
        }
        out.close();
        in.close();
        byte[] data = out.toByteArray();
        DatagramPacket p = new DatagramPacket(data, data.length);
        XFF bla = new XFF().readPacket(p);
        p = bla.getDatagramPacket();
        FileOutputStream fout = new FileOutputStream(filename);
        fout.write(p.getData(), p.getOffset(), p.getLength());
        fout.close();
    }

    public abstract T readPacket(DatagramPacket p);

    public abstract DatagramPacket writePacket(T data);
}
