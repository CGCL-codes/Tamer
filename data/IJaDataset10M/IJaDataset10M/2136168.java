package jdos.win.loader.winpe;

import java.io.IOException;

public class HeaderImageImportDescriptor {

    public static final int SIZE = 20;

    public long Characteristics_or_OriginalFirstThunk;

    public long TimeDateStamp;

    public long ForwarderChain;

    public long Name;

    public long FirstThunk;

    public void load(LittleEndianFile is) throws IOException {
        Characteristics_or_OriginalFirstThunk = is.readUnsignedInt();
        TimeDateStamp = is.readUnsignedInt();
        ForwarderChain = is.readUnsignedInt();
        Name = is.readUnsignedInt();
        FirstThunk = is.readUnsignedInt();
    }
}
