package whf.file.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import whf.framework.log.Log;
import whf.framework.log.LogFactory;
import whf.framework.resource.thread.AbstractPoolableThread;

/**
 * 将文件写入硬盘的服务
 * @author wanghaifeng
 * @create Nov 14, 2006 10:38:49 PM
 * 
 */
public class FormFileSaveThread extends AbstractPoolableThread {

    private static Log log = LogFactory.getLog(FormFileSaveThread.class);

    private String fileName;

    private InputStream data;

    public FormFileSaveThread(InputStream formFile, String fileName) {
        this.data = formFile;
        this.fileName = fileName;
    }

    @Override
    public void onTimeout() {
    }

    public String getFileName() {
        return this.fileName;
    }

    public void run() {
        try {
            byte[] buff = new byte[1024];
            String dir = this.fileName.substring(0, this.fileName.lastIndexOf(System.getProperty("file.separator")));
            File file = new File(dir);
            if (!file.exists()) file.mkdirs();
            System.out.println(dir);
            System.out.println(fileName);
            FileOutputStream fos = new FileOutputStream(this.fileName);
            int readCount = data.read(buff);
            while (readCount >= 0) {
                fos.write(buff, 0, readCount);
                readCount = data.read(buff);
            }
        } catch (Exception e) {
            log.error(this, e);
        }
    }
}
