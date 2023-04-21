package ch.epfl.lsr.adhoc.simulator.testing;

import java.io.*;

/**
 * @author Boris
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class TestFileTransfer {

    public static void main(String argc[]) {
        OutputStream is;
        byte buf[] = new byte[1024];
        int len;
        try {
            FileInputStream in = new FileInputStream(argc[0]);
            FileOutputStream out = new FileOutputStream("boris.LOG");
            while ((len = in.read(buf)) > 0) {
                out.write(buf);
                out.flush();
            }
            out.close();
            in.close();
        } catch (FileNotFoundException file_e) {
            file_e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
