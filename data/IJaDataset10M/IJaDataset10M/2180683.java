package net.sf.gpproxy.ui2;

public interface HexData {

    public int getRowCount();

    public int getColumnCount();

    public int getLastRowSize();

    public byte getByte(int row, int col);

    public void setByte(int row, int col, byte value);

    public byte[] getRow(int row);

    public void insertBytes(int row, int col, int size, byte value);

    public void removeBytes(int row, int col, int size);
}
