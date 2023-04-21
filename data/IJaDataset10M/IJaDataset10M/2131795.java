package org.jopenray.operation;

import java.awt.Color;

public class FillOperation extends Operation {

    public FillOperation(int x, int y, int width, int height, Color color) {
        allocate(16);
        setHeader(0xA2, x, y, width, height);
        buffer.addInt8(0xFF);
        buffer.addInt8(color.getBlue());
        buffer.addInt8(color.getGreen());
        buffer.addInt8(color.getRed());
    }

    @Override
    public void dump() {
        System.out.println("FillOperation");
    }
}
