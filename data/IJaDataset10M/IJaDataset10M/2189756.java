package zaphod.toy.sampleserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class OutputThread implements Runnable {

    private final OutputStream os;

    /**
     * ������ Ŭ���̾�Ʈ���� ������ �׸�����
     */
    private static final File pictureFile = new File("c:/a.jpg");

    private final Thread it;

    public OutputThread(OutputStream outputStream, Thread it) {
        this.os = outputStream;
        this.it = it;
    }

    public void run() {
        try {
            InputStream is = new FileInputStream(pictureFile);
            int i;
            while (true) {
                i = is.read();
                if (i == -1) {
                    break;
                }
                os.write(i);
            }
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        it.interrupt();
    }
}
