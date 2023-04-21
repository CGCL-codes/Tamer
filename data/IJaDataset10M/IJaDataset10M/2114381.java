package com.iv.flash.gif;

import java.io.IOException;
import java.io.InputStream;

abstract class GifSpecialBlock extends GifBlock {

    void readDataBlocks(InputStream ins, StringBuffer data) throws IOException, GifException {
        int size;
        data.setLength(0);
        while ((size = Gif.unsignedByte(ins)) != 0) {
            if (ins.read(d_buf, 0, size) != size) throw new GifException("Not enough bytes available for data block.");
            data.append(new String(d_buf, 0, size));
        }
        if (data.length() == 0) throw new GifException("Zero length data blocks");
    }

    private byte[] d_buf = new byte[256];
}

;
