package se.sics.mspsim.chip;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import se.sics.mspsim.core.MSP430Core;

public class FileAT45DB extends AT45DB {

    private static final int FLASH_SIZE = PAGE_SIZE * NUM_PAGES;

    private RandomAccessFile file;

    private FileChannel fileChannel;

    private FileLock fileLock;

    public FileAT45DB(MSP430Core cpu, String filename) {
        super(cpu);
        if (filename == null) {
            filename = "flash.bin";
        }
        if (!openFile(filename)) {
            Matcher m = Pattern.compile("(.+?)(\\d*)(\\.[^.]+)").matcher(filename);
            if (m.matches()) {
                String baseName = m.group(1);
                String c = m.group(2);
                String extName = m.group(3);
                int count = 1;
                if (c != null && c.length() > 0) {
                    count = Integer.parseInt(c) + 1;
                }
                for (int i = 0; !openFile(baseName + count + extName) && i < 100; i++, count++) ;
            }
        }
        if (fileLock == null) {
            throw new IllegalStateException("failed to open flash file '" + filename + '\'');
        }
        try {
            file.setLength(FLASH_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean openFile(String filename) {
        try {
            file = new RandomAccessFile(filename, "rw");
            fileChannel = file.getChannel();
            fileLock = fileChannel.tryLock();
            if (fileLock != null) {
                if (DEBUG) log("using flash file '" + filename + '\'');
                return true;
            } else {
                fileChannel.close();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            closeFile();
            return false;
        }
    }

    private void closeFile() {
        try {
            if (fileLock != null) {
                fileLock.release();
                fileLock = null;
            }
            if (fileChannel != null) {
                fileChannel.close();
                fileChannel = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void seek(long pos) throws IOException {
        file.seek(pos);
    }

    public int read(byte[] b) throws IOException {
        return file.read(b);
    }

    public void write(byte[] b) throws IOException {
        file.write(b);
    }

    public void stateChanged(int state) {
    }
}
