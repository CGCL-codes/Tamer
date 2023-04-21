package hs485;

import java.beans.*;
import java.io.*;

public class FirmwareVersion {

    private int majorVersion = 0;

    private int minorVersion = 0;

    public FirmwareVersion() {
    }

    FirmwareVersion(int majorVersion, int minorVersion) {
        this.majorVersion = majorVersion;
        this.minorVersion = minorVersion;
    }

    public int getMajorVersion() {
        return majorVersion;
    }

    public int getMinorVersion() {
        return minorVersion;
    }

    public static FirmwareVersion readObject(InputStream in) throws IOException {
        FirmwareVersion result = null;
        XMLDecoder d = new XMLDecoder(new BufferedInputStream(in));
        result = (FirmwareVersion) d.readObject();
        d.close();
        return result;
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public void setMinorVersion(int minorVersion) {
        this.minorVersion = minorVersion;
    }

    public String toString() {
        return String.format("%d.%02d", majorVersion, minorVersion);
    }

    public void writeObject(OutputStream out) throws IOException {
        XMLEncoder e = new XMLEncoder(new BufferedOutputStream(out));
        e.writeObject(this);
        e.close();
    }
}
