package com.cell.j2se;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import com.cell.CIO;
import com.cell.gfx.IPalette;

class CPalette implements IPalette {

    private byte[] data_;

    private short color_count_;

    private short transparent_color_index_;

    CPalette(String file) {
        this.load(file);
    }

    CPalette(byte[] data, short color_count, short transparent_color_index) {
        this.data_ = data;
        this.color_count_ = color_count;
        this.transparent_color_index_ = transparent_color_index;
    }

    CPalette(CPalette src) {
        this.data_ = Arrays.copyOf(src.data_, src.data_.length);
        this.color_count_ = src.color_count_;
        this.transparent_color_index_ = src.transparent_color_index_;
    }

    @Override
    public IPalette clone() {
        return new CPalette(this);
    }

    void load(String file) {
        try {
            if (file.endsWith(".act")) {
                ByteArrayInputStream bais = CIO.loadStream(file);
                if (bais != null) loadACT(bais);
            }
        } catch (Exception exp) {
            exp.printStackTrace();
            this.data_ = null;
        }
    }

    private void loadACT(InputStream is) throws IOException {
        int size = is.available();
        this.data_ = new byte[256 * 3];
        is.read(this.data_);
        if (size > 256 * 3) {
            int byte1 = is.read();
            int byte2 = is.read();
            color_count_ = (short) (((int) byte1 << 8) | (int) byte2);
            int byte3 = is.read();
            int byte4 = is.read();
            transparent_color_index_ = (short) (((int) byte3 << 8) | (int) byte4);
        } else {
            color_count_ = 256;
            transparent_color_index_ = -1;
        }
    }

    @Override
    public byte[] getIndexColors() {
        return this.data_;
    }

    @Override
    public int getIndexColorCount() {
        return this.color_count_;
    }

    @Override
    public byte[] getTransparentColor() {
        if ((0 <= this.transparent_color_index_) && (this.transparent_color_index_ < 256)) {
            byte[] color = new byte[3];
            int offset = this.transparent_color_index_ * 3;
            color[0] = this.data_[offset];
            color[1] = this.data_[offset + 1];
            color[2] = this.data_[offset + 2];
            return color;
        }
        return null;
    }

    @Override
    public int getTransparentColorIndex() {
        return this.transparent_color_index_;
    }

    @Override
    public void dispose() {
        this.data_ = null;
        this.color_count_ = 0;
        this.transparent_color_index_ = -1;
    }
}

;
