package deltree.file;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author demangep
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FileSplit {

    public static int BUFFER_SIZE = 512;

    private String srcName;

    private int maxSize;

    /**
	 * 
	 */
    public FileSplit(String[] args) {
        try {
            srcName = args[0];
            maxSize = Integer.parseInt(args[1]);
        } catch (Exception e) {
            System.err.println("Usage:\nfilesplt.jar srcname maxsize(in Kb)");
        }
    }

    public void run() {
        FileInputStream src;
        try {
            src = new FileInputStream(srcName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }
        FileOutputStream dest;
        FileChannel srcC = src.getChannel();
        ByteBuffer buf = ByteBuffer.allocateDirect(BUFFER_SIZE);
        try {
            int i = 1;
            int fileNo = 0;
            long maxByte = this.maxSize << 10;
            long nbByte = srcC.size();
            long nbFile = (nbByte / maxByte) + 1;
            for (fileNo = 0; fileNo < nbFile; fileNo++) {
                long fileByte = 0;
                String destName = srcName + "_" + fileNo;
                dest = new FileOutputStream(destName);
                FileChannel destC = dest.getChannel();
                while ((i > 0) && fileByte < maxByte) {
                    i = srcC.read(buf);
                    buf.flip();
                    fileByte += i;
                    destC.write(buf);
                    buf.compact();
                }
                destC.close();
                dest.close();
            }
        } catch (IOException e1) {
            e1.printStackTrace();
            return;
        }
    }

    public static void main(String[] args) {
        new FileSplit(args).run();
    }
}
