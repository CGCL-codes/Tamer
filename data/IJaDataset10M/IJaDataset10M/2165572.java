package net.sourceforge.jd.classfile.attributes;

import java.io.DataInputStream;
import java.io.IOException;

/**

        {  u2 start_pc;
            u2 length;
            u2 name_index;
            u2 descriptor_index;
            u2 index;
        } local_variable_table[local_variable_table_length];

 */
public class LocalVariableTable {

    private int start_pc;

    private int length;

    private int name_index;

    private int descriptor_index;

    private int index;

    public LocalVariableTable(DataInputStream dis) throws IOException {
        this(dis.readUnsignedShort(), dis.readUnsignedShort(), dis.readUnsignedShort(), dis.readUnsignedShort(), dis.readUnsignedShort());
    }

    public LocalVariableTable(int start_pc, int length, int name_index, int descriptor_index, int index) {
        this.start_pc = start_pc;
        this.length = length;
        this.name_index = name_index;
        this.descriptor_index = descriptor_index;
        this.index = index;
    }
}
