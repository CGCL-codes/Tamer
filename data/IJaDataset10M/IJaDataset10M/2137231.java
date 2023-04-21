package emulator.hardware.io.peripherals.iec;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import emulator.util.ByteBufferReader;

public class FsDriverFileChannel implements FsDriverChannel {

    private static final int MAX_FILE_BUF = 65535;

    FsDirectory directory = null;

    DriveStatus drive_status = null;

    ByteBuffer buffer = null;

    public FsDriverFileChannel(IecSimFsDriver driver) {
        drive_status = driver.getDriveStatus();
        directory = driver.getDirectory();
    }

    @Override
    public void open(String name) {
        if (directory == null) drive_status.setStatus(DriveStatus.NO_DISK, 0, 0); else if (name.equals("$")) {
            buffer = ByteBuffer.allocate(MAX_FILE_BUF);
            buffer.clear();
            directory.getDirectory(buffer);
            buffer.flip();
            drive_status.setStatus(DriveStatus.OK, 0, 0);
        } else {
            String file_path = directory.getFilePath(name);
            if (file_path != null) {
                FileInputStream file;
                try {
                    file = new FileInputStream(file_path);
                    if (file != null) {
                        buffer = ByteBuffer.allocate(MAX_FILE_BUF);
                        buffer.clear();
                        file.getChannel().read(buffer);
                        buffer.flip();
                        drive_status.setStatus(DriveStatus.OK, 0, 0);
                        return;
                    }
                } catch (FileNotFoundException e) {
                } catch (IOException e) {
                    drive_status.setStatus(DriveStatus.READ_ERROR_SYNC, 0, 0);
                }
            }
            drive_status.setStatus(DriveStatus.FILE_NOT_FOUND, 0, 0);
        }
    }

    @Override
    public void close() {
        buffer = null;
    }

    @Override
    public void write(ByteBuffer data) {
        while (data.hasRemaining()) {
            data.get();
        }
    }

    @Override
    public int read() {
        if (buffer != null && buffer.hasRemaining()) return ByteBufferReader.getByte(buffer);
        return -1;
    }

    @Override
    public void rewind(int offset) {
        if (buffer != null) buffer.position(buffer.position() - offset);
    }
}
