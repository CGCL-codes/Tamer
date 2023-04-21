package net.sourceforge.binml.datatype;

import java.io.IOException;
import net.sourceforge.binml.core.DataSource;

/**
 * @author Fredrik Hederstierna
 * @version $Id: $
 * 
 * Created: 2006
 * 
 * Copyright 2006 Purple Scout AB, Malmoe, Sweden
 */
public abstract class Struct extends BinMLTypeAdapter implements BinMLType {

    /**
	 * Struct variables, overriden by subclass
	 */
    public BinMLType struct[] = new BinMLType[0];

    public Struct() {
    }

    public Struct(BinMLType struct[], boolean littleEndian) {
        this.littleEndian = littleEndian;
        this.struct = struct;
    }

    public Struct(String name, boolean littleEndian) {
        this.littleEndian = littleEndian;
        this.name = name;
    }

    public String getString() {
        return name;
    }

    public void set(BinMLType struct[]) {
        this.struct = struct;
    }

    public int sizeofInBits() {
        int size = 0;
        for (int i = 0; i < struct.length; i++) {
            if (struct[i] != null) {
                size += struct[i].sizeofInBits();
            }
        }
        return size;
    }

    public void read(DataSource src) throws Exception {
        if (src != null) {
            if (struct != null) {
                for (int i = 0; i < struct.length; i++) {
                    if (struct[i] != null) {
                        if (littleEndian) {
                            struct[i].setLittleEndian();
                        } else {
                            struct[i].setBigEndian();
                        }
                        struct[i].read(src);
                    }
                }
            }
        } else {
            throw new IOException();
        }
    }

    public int getInt() {
        return -1;
    }

    public int getInt(String name) {
        int val = -1;
        if (struct != null) {
            for (int i = 0; i < struct.length; i++) {
                if (struct[i] != null) {
                    if (struct[i].getName().equals(name)) {
                        val = struct[i].getInt();
                        break;
                    }
                }
            }
        }
        return val;
    }

    public int getOffset(String name) {
        int val = 0;
        if (struct != null) {
            for (int i = 0; i < struct.length; i++) {
                if (struct[i] != null) {
                    if (struct[i].getName().equals(name)) {
                        break;
                    }
                    val += struct[i].sizeof();
                }
            }
        }
        return val;
    }

    public void dump() {
        if (struct != null) {
            System.out.println("Struct::dump: struct len " + struct.length + "\n");
            for (int i = 0; i < struct.length; i++) {
                if (struct[i] != null) {
                    struct[i].dump();
                    System.out.println(struct[i].getName() + " = " + struct[i].getInt() + "\n");
                }
            }
        } else {
            System.out.println("Struct::dump: WARNING struct was NULL\n");
        }
    }

    public void setLittleEndian(boolean isLittle) {
        this.littleEndian = isLittle;
    }
}
