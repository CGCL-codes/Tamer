package emulator.hardware.io.peripherals.iec;

import java.nio.ByteBuffer;

public class FsDriverCmdChannel implements FsDriverChannel {

    IecSimFsDriver driver = null;

    byte[] buffer = null;

    int pos;

    public FsDriverCmdChannel(IecSimFsDriver driver) {
        this.driver = driver;
    }

    @Override
    public void open(String name) {
        exec(name);
    }

    private void exec(String name) {
        setupStatus();
    }

    private void setupStatus() {
        DriveStatus drive_status = driver.getDriveStatus();
        buffer = drive_status.readStatus().getBytes();
        pos = 0;
    }

    @Override
    public void close() {
        buffer = null;
    }

    @Override
    public void write(ByteBuffer data) {
    }

    @Override
    public int read() {
        if (buffer == null) setupStatus();
        if (pos < buffer.length) {
            int b = buffer[pos++];
            return (b < 0 ? b + 256 : b);
        }
        return -1;
    }

    @Override
    public void rewind(int offset) {
        pos -= offset;
        if (pos < 0) pos = 0;
    }
}
