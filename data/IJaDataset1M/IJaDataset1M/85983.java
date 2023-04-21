package russotto.zplet.zmachine;

import java.util.*;
import java.awt.*;
import java.net.*;
import java.io.*;
import java.applet.Applet;
import russotto.iff.*;

public abstract class ZHeader {

    protected byte memory_image[];

    protected static final int VERSION = 0x00;

    protected static final int FLAGS1 = 0x01;

    public static final int RELEASE = 0x02;

    protected static final int HIGH_BASE = 0x04;

    protected static final int INITIAL_PC = 0x06;

    protected static final int DICTIONARY = 0x08;

    protected static final int OBJECT_TABLE = 0x0A;

    protected static final int GLOBAL_TABLE = 0x0C;

    protected static final int STATIC_BASE = 0x0E;

    protected static final int FLAGS2 = 0x10;

    public static final int SERIAL_NUMBER = 0x12;

    protected static final int ABBREV_TABLE = 0x18;

    protected static final int FILE_LENGTH = 0x1A;

    public static final int FILE_CHECKSUM = 0x1C;

    protected static final int STD_REVISION = 0x32;

    public int version() {
        return memory_image[VERSION];
    }

    public static int image_version(byte[] memory_image) {
        return memory_image[VERSION];
    }

    public int high_base() {
        return (((int) memory_image[HIGH_BASE] << 8) & 0xFF00) | (((int) memory_image[HIGH_BASE + 1]) & 0x00FF);
    }

    public int initial_pc() {
        return (((int) memory_image[INITIAL_PC] << 8) & 0xFF00) | (((int) memory_image[INITIAL_PC + 1]) & 0x00FF);
    }

    public int dictionary() {
        return (((int) memory_image[DICTIONARY] << 8) & 0xFF00) | (((int) memory_image[DICTIONARY + 1]) & 0x00FF);
    }

    public int object_table() {
        return (((int) memory_image[OBJECT_TABLE] << 8) & 0xFF00) | (((int) memory_image[OBJECT_TABLE + 1]) & 0x00FF);
    }

    public int global_table() {
        return (((int) memory_image[GLOBAL_TABLE] << 8) & 0xFF00) | (((int) memory_image[GLOBAL_TABLE + 1]) & 0x00FF);
    }

    public int static_base() {
        return (((int) memory_image[STATIC_BASE] << 8) & 0xFF00) | (((int) memory_image[STATIC_BASE + 1]) & 0x00FF);
    }

    public boolean transcripting() {
        return ((memory_image[FLAGS2 + 1] & 1) == 1);
    }

    public void set_transcripting(boolean onoff) {
        if (onoff) memory_image[FLAGS2 + 1] |= 1; else memory_image[FLAGS2 + 1] &= 0xFE;
    }

    public int abbrev_table() {
        return (((int) memory_image[ABBREV_TABLE] << 8) & 0xFF00) | (((int) memory_image[ABBREV_TABLE + 1]) & 0x00FF);
    }

    public boolean force_fixed() {
        return ((memory_image[FLAGS2 + 1] & 2) == 2);
    }

    public void set_revision(int major, int minor) {
        memory_image[STD_REVISION] = (byte) major;
        memory_image[STD_REVISION + 1] = (byte) minor;
    }

    public short release() {
        return (short) (((memory_image[RELEASE] & 0xFF) << 8) | (memory_image[RELEASE + 1] & 0xFF));
    }

    public short checksum() {
        return (short) (((memory_image[FILE_CHECKSUM] & 0xFF) << 8) | (memory_image[FILE_CHECKSUM + 1] & 0xFF));
    }

    public abstract int file_length();
}
