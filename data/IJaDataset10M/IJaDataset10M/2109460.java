package gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.PixelGrabber;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class ImageObjectifier {

    private static final String dest = "output.txt";

    private final File[] images;

    private DataOutputStream out;

    private DataInputStream in;

    public ImageObjectifier(String p) {
        File path = new File(p);
        try {
            out = new DataOutputStream(new FileOutputStream(dest));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        images = path.listFiles(new ImageFilter());
        for (File f : images) System.out.println(f.getName());
    }

    public void run() {
        int t;
        for (File f : images) {
            System.out.println(f.getPath());
            try {
                out.writeBytes("private static final byte [] " + f.getName().substring(0, f.getName().indexOf('.')) + " = {");
                FileInputStream in = new FileInputStream(f);
                t = in.available();
                int info[] = new int[t];
                for (int i = 0; i < t; i++) {
                    info[i] = in.read();
                }
                for (int i = 0; i < info.length; i++) {
                    String output = Integer.toHexString(info[i]);
                    if (output.length() == 1) {
                        output = "0" + output;
                    } else if (output.length() > 2) {
                        output = output.substring(output.length() - 2);
                    }
                    out.writeBytes("(byte)0x" + output);
                    if ((t - i) != 1) out.writeBytes(",");
                    if ((i % 10) == 0) {
                        out.writeBytes("\n");
                    }
                }
                out.writeBytes("};");
                out.writeBytes("\n\n\n\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ImageFilter implements FileFilter {

        public boolean accept(File pathname) {
            if (pathname.getName().endsWith("jpg") || pathname.getName().endsWith("png")) return true;
            return false;
        }
    }
}
