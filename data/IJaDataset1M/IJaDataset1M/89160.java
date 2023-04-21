package net.sourceforge.binml.datatype;

import net.sourceforge.binml.core.BinaryParseException;
import net.sourceforge.binml.core.DataSource;

/**
 * Interface that describes BinML data type.
 * 
 * @author Fredrik Hederstierna
 */
public interface BinMLType {

    public void setParent(BinMLType parent);

    public BinMLType getParent();

    public int sizeof();

    public int sizeofInBits();

    public int getInt();

    public String getString() throws BinaryParseException;

    public BinMLEnum getEnum();

    public void setLittleEndian();

    public void setBigEndian();

    public boolean isPointer();

    public BinMLType getPointer();

    public int getPointerDim(BinMLType parentObj);

    public int getPointerOffset(BinMLType parentObj, DataSource src, int offset) throws BinaryParseException;

    public String getName();

    public void setName(String name);

    public void read(DataSource src) throws Exception;

    public void dump();
}
