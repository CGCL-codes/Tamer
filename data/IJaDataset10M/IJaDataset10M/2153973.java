package net.sourceforge.rombrowser.roms;

import java.util.zip.*;
import java.io.*;
import net.sourceforge.rombrowser.roms.*;
import net.sourceforge.rombrowser.gui.*;
import net.sourceforge.rombrowser.util.*;

public class SNESHandler implements ROMHandler {

    public void processFile(ROMFile rf) {
        int offset = 512;
        rf.setProperty("rom-CRC", ROMFile.createChecksum(rf, new CRC32(), offset));
        try {
            int l = Long.valueOf(rf.getProperty("romfile-size")).intValue();
            offset = l % 0x8000;
            if (offset != 512) {
                System.err.println("Trying new header size, " + offset + " for " + rf.getName());
                rf.setProperty("rom-CRC", ROMFile.createChecksum(rf, new CRC32(), 0));
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
