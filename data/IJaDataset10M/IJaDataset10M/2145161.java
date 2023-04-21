package com.anotherbigidea.flash.structs;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import com.anotherbigidea.io.InStream;
import com.anotherbigidea.io.OutStream;

public class ButtonRecord {

    public static final int BUTTON_HITTEST = 0x08;

    public static final int BUTTON_DOWN = 0x04;

    public static final int BUTTON_OVER = 0x02;

    public static final int BUTTON_UP = 0x01;

    protected int flags;

    protected int id;

    protected int layer;

    protected Matrix matrix;

    public int getCharId() {
        return id;
    }

    public int getLayer() {
        return layer;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public int getFlags() {
        return flags;
    }

    public boolean isHitTest() {
        return ((flags & BUTTON_HITTEST) != 0);
    }

    public boolean isDown() {
        return ((flags & BUTTON_DOWN) != 0);
    }

    public boolean isOver() {
        return ((flags & BUTTON_OVER) != 0);
    }

    public boolean isUp() {
        return ((flags & BUTTON_UP) != 0);
    }

    public void setCharId(int id) {
        this.id = id;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    /**
     * Read a button record array
     */
    public static Vector read(InStream in) throws IOException {
        Vector records = new Vector();
        int firstByte = 0;
        while ((firstByte = in.readUI8()) != 0) {
            records.addElement(new ButtonRecord(in, firstByte));
        }
        return records;
    }

    /**
     * Write a button record array
     */
    public static void write(OutStream out, Vector records) throws IOException {
        for (Enumeration myEnum = records.elements(); myEnum.hasMoreElements(); ) {
            ButtonRecord rec = (ButtonRecord) myEnum.nextElement();
            rec.write(out);
        }
        out.writeUI8(0);
    }

    public ButtonRecord(int id, int layer, Matrix matrix, int flags) {
        this.id = id;
        this.layer = layer;
        this.matrix = matrix;
        this.flags = flags;
    }

    protected ButtonRecord(InStream in, int firstByte) throws IOException {
        flags = firstByte;
        id = in.readUI16();
        layer = in.readUI16();
        matrix = new Matrix(in);
    }

    protected void write(OutStream out) throws IOException {
        out.writeUI8(flags);
        out.writeUI16(id);
        out.writeUI16(layer);
        matrix.write(out);
    }

    @Override
    public String toString() {
        return "layer=" + layer + " id=" + id + " flags=" + Integer.toBinaryString(flags) + " " + matrix;
    }
}
