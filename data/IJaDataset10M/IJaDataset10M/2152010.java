package com.iver.cit.gvsig.fmap.drivers;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.edition.DataWare;
import com.iver.cit.gvsig.fmap.core.FShape;

public class ConcreteMemoryDriver extends MemoryDriver {

    int shapeType = FShape.MULTI;

    String name;

    public void setShapeType(int shapeType) {
        this.shapeType = shapeType;
    }

    public int getShapeType() {
        return shapeType;
    }

    /**
     *
     */
    public ConcreteMemoryDriver() {
    }

    public String getName() {
        return name;
    }

    public DriverAttributes getDriverAttributes() {
        return null;
    }

    public int[] getPrimaryKeys() throws ReadDriverException {
        return null;
    }

    public void write(DataWare dataWare) throws ReadDriverException {
    }

    public boolean isWritable() {
        return true;
    }
}
