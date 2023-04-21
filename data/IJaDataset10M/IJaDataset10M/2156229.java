package com.sixlegs.png;

import java.io.IOException;
import java.util.Map;

class Chunk_cHRM extends PngChunk {

    public void read(int type, PngInputStream in, PngImage png) throws IOException {
        checkLength(in.getRemaining(), 32);
        float[] array = new float[8];
        for (int i = 0; i < 8; i++) array[i] = in.readInt() / 100000f;
        Map props = png.getProperties();
        if (!props.containsKey(PngConstants.CHROMATICITY)) props.put(PngConstants.CHROMATICITY, array);
    }
}
