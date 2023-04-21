package org.apache.harmony.unpack200.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.harmony.pack200.Pack200Exception;

/**
 * Local variable table
 */
public class LocalVariableTableAttribute extends BCIRenumberedAttribute {

    private int local_variable_table_length;

    private final int[] start_pcs;

    private final int[] lengths;

    private int[] name_indexes;

    private int[] descriptor_indexes;

    private final int[] indexes;

    private final CPUTF8[] names;

    private final CPUTF8[] descriptors;

    private int codeLength;

    private static CPUTF8 attributeName;

    public static void setAttributeName(CPUTF8 cpUTF8Value) {
        attributeName = cpUTF8Value;
    }

    public LocalVariableTableAttribute(int local_variable_table_length, int[] start_pcs, int[] lengths, CPUTF8[] names, CPUTF8[] descriptors, int[] indexes) {
        super(attributeName);
        this.local_variable_table_length = local_variable_table_length;
        this.start_pcs = start_pcs;
        this.lengths = lengths;
        this.names = names;
        this.descriptors = descriptors;
        this.indexes = indexes;
    }

    public void setCodeLength(int length) {
        codeLength = length;
    }

    protected int getLength() {
        return 2 + (10 * local_variable_table_length);
    }

    protected void writeBody(DataOutputStream dos) throws IOException {
        dos.writeShort(local_variable_table_length);
        for (int i = 0; i < local_variable_table_length; i++) {
            dos.writeShort(start_pcs[i]);
            dos.writeShort(lengths[i]);
            dos.writeShort(name_indexes[i]);
            dos.writeShort(descriptor_indexes[i]);
            dos.writeShort(indexes[i]);
        }
    }

    protected ClassFileEntry[] getNestedClassFileEntries() {
        ArrayList nestedEntries = new ArrayList();
        nestedEntries.add(getAttributeName());
        for (int i = 0; i < local_variable_table_length; i++) {
            nestedEntries.add(names[i]);
            nestedEntries.add(descriptors[i]);
        }
        ClassFileEntry[] nestedEntryArray = new ClassFileEntry[nestedEntries.size()];
        nestedEntries.toArray(nestedEntryArray);
        return nestedEntryArray;
    }

    protected void resolve(ClassConstantPool pool) {
        super.resolve(pool);
        name_indexes = new int[local_variable_table_length];
        descriptor_indexes = new int[local_variable_table_length];
        for (int i = 0; i < local_variable_table_length; i++) {
            names[i].resolve(pool);
            descriptors[i].resolve(pool);
            name_indexes[i] = pool.indexOf(names[i]);
            descriptor_indexes[i] = pool.indexOf(descriptors[i]);
        }
    }

    public String toString() {
        return "LocalVariableTable: " + +local_variable_table_length + " variables";
    }

    protected int[] getStartPCs() {
        return start_pcs;
    }

    public void renumber(List byteCodeOffsets) throws Pack200Exception {
        int[] unrenumbered_start_pcs = new int[start_pcs.length];
        System.arraycopy(start_pcs, 0, unrenumbered_start_pcs, 0, start_pcs.length);
        super.renumber(byteCodeOffsets);
        int maxSize = codeLength;
        for (int index = 0; index < lengths.length; index++) {
            int start_pc = start_pcs[index];
            int revisedLength = -1;
            int encodedLength = lengths[index];
            int indexOfStartPC = unrenumbered_start_pcs[index];
            int stopIndex = indexOfStartPC + encodedLength;
            if (stopIndex < 0) {
                throw new Pack200Exception("Error renumbering bytecode indexes");
            }
            if (stopIndex == byteCodeOffsets.size()) {
                revisedLength = maxSize - start_pc;
            } else {
                int stopValue = ((Integer) byteCodeOffsets.get(stopIndex)).intValue();
                revisedLength = stopValue - start_pc;
            }
            lengths[index] = revisedLength;
        }
    }

    public boolean equals(Object obj) {
        return this == obj;
    }
}
